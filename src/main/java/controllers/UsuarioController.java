/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import static java.lang.System.out;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.RollbackException;
import javax.persistence.TypedQuery;
import models.Album;
import models.Artista;
import models.Cancion;
import models.Cliente;
import models.Playlist;
import models.Usuario;
import org.mindrot.jbcrypt.BCrypt;
import persistence.AlbumJpaController;
import persistence.ArtistaJpaController;
import persistence.CancionJpaController;
import persistence.ClienteJpaController;
import persistence.PlaylistJpaController;
import persistence.UsuarioJpaController;
import persistence.exceptions.PreexistingEntityException;

/*
import models.Artista;
import models.Cliente;
import models.Usuario;
 */

/**
 *
 * @author Machichu
 */
public class UsuarioController implements IUsuarioController {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("grupo6_Spotify");
    PlaylistJpaController auxPlay = new PlaylistJpaController(emf);
    AlbumJpaController auxAlbum = new AlbumJpaController(emf);
    CancionJpaController auxCan = new CancionJpaController(emf);
    UsuarioJpaController usrController = new UsuarioJpaController(emf);
    ClienteJpaController auxCliente =  new ClienteJpaController(emf);

    //  private ClienteJpaController cliente_ctr = new ClienteJpaController(emf);
    public List<String> obtenerNombresClientes() {
        EntityManager em = emf.createEntityManager();
        try {
            // Consulta para obtener solo los objetos de tipo Cliente
            List<Cliente> clientes = em.createQuery("SELECT c FROM Cliente c", Cliente.class).getResultList();

            return clientes.stream()
                    .map(cliente -> cliente.getNick())
                    .collect(Collectors.toList());
        } finally {
            em.close();
        }
        //     cliente_ctr.
    }
    
    public String tipoUsuario(String nick){
        EntityManager em = emf.createEntityManager();
        return(String) em.createNativeQuery("Select tipo_usuario from usuario where nick='"+nick+"'").getSingleResult(); 
    }

    public Object[][] obtenerDatosCliente(String nick) {
        EntityManager em = emf.createEntityManager();
        try {
            List<Cliente> clientes = em.createQuery("SELECT c FROM Cliente c WHERE c.nick = :nick", Cliente.class).setParameter("nick", nick).getResultList();
            Object[][] data = new Object[clientes.size()][9];

            for (int i = 0; i < clientes.size(); i++) {
                Cliente cliente = clientes.get(i);  // Obtener el cliente individual

                data[i][0] = cliente.getNick();     // Asignar valores al arreglo
                data[i][1] = cliente.getNombre();
                data[i][2] = cliente.getApellido();
                data[i][3] = cliente.getMail();
                data[i][4] = cliente.getFecNac();
                data[i][5] = cliente.getImagen();
                data[i][6] = cliente.getEstado();
                data[i][7] = cliente.getFecSub();
                data[i][8] = cliente.getTipo();
            }
            return data;

        } finally {
            em.close();
        }
    }

    public Object[][] obtenerDatosClientes() {
        EntityManager em = emf.createEntityManager();
        try {
            List<Cliente> clientes = em.createQuery("SELECT c FROM Cliente c", Cliente.class).getResultList();
            Object[][] data = new Object[clientes.size()][6];

            for (int i = 0; i < clientes.size(); i++) {
                Cliente cliente = clientes.get(i);  // Obtener el cliente individual

                // Asignar valores al arreglo
                data[i][0] = cliente.getNombre();
                data[i][1] = cliente.getNick();
            }
            return data;

        } finally {
            em.close();
        }
    }

    public void registroUsuario(String nickname, String nombre, String apellido, String mail, LocalDate FecNac, String imagen, String biografia, String link, String tipo, String contraseña) throws Exception {
        UsuarioJpaController aux = new UsuarioJpaController(emf);
        Usuario usr;
        String contraseñaEncriptada;
        contraseñaEncriptada = hashPassword(contraseña);
        if (tipo.equals("artista")) {
            usr = new Artista(nickname, nombre, apellido, mail, FecNac, imagen, biografia, link, contraseñaEncriptada);
        } else {//si es cliente
            usr = new Cliente(nickname, nombre, apellido, mail, FecNac, imagen, contraseñaEncriptada);
        }
        try {
            aux.create(usr);
        } catch (PreexistingEntityException | RollbackException ex) {
            System.out.print(ex);
            throw ex;
        }
    }

    public void registrarPlaylistFavorita(String nick, String nombrePlaylist) throws Exception {
        UsuarioJpaController aux = new UsuarioJpaController(emf);
        String playlistid = nombrePlaylist.trim();
        int indicePlay = playlistid.indexOf('-');
        String idPlayString = playlistid.substring(0, indicePlay).trim();
        int idPlaylist = Integer.parseInt(idPlayString);
        Playlist playlist = auxPlay.findPlaylist(idPlaylist);
        Cliente cliente = (Cliente) aux.findUsuario(nick);
        cliente.getPlaylistFavoritos().add(playlist);
        aux.edit(cliente);

    }

    public List<String> obtenerNombresDePlaylistsNoFavoritas(String clienteNick) {
        EntityManager em = emf.createEntityManager();
        UsuarioJpaController aux = new UsuarioJpaController(emf);

        Cliente cliente = (Cliente) aux.findUsuario(clienteNick);

        List<Integer> idsPlaylistsFavoritas = cliente.getPlaylistFavoritos().stream()
                .map(Playlist::getId)
                .collect(Collectors.toList());

        List<Playlist> playlistsNoFavoritas;
        if (idsPlaylistsFavoritas.isEmpty()) {
            playlistsNoFavoritas = auxPlay.findPlaylistEntities();
        } else {
            playlistsNoFavoritas = em.createQuery("SELECT p FROM Playlist p WHERE p.id NOT IN :ids", Playlist.class)
                    .setParameter("ids", idsPlaylistsFavoritas)
                    .getResultList();
        }

        return playlistsNoFavoritas.stream()
                .map(playlist -> playlist.getId() + " - " + playlist.getNombre())
                .collect(Collectors.toList());
    }

    public Object[][] obtenerDatosArtista(String nick) {
        EntityManager em = emf.createEntityManager();
        try {
            List<Artista> artistas = em.createQuery("SELECT a FROM Artista a WHERE a.nick = :nick", Artista.class).setParameter("nick", nick).getResultList();
            Object[][] data = new Object[artistas.size()][8];

            for (int i = 0; i < artistas.size(); i++) {
                Artista artista = artistas.get(i);

                data[i][0] = artista.getNick();
                data[i][1] = artista.getNombre();
                data[i][2] = artista.getApellido();
                data[i][3] = artista.getMail();
                data[i][4] = artista.getFecNac();
                data[i][5] = artista.getImagen();
                data[i][6] = artista.getBiografia();
                data[i][7] = artista.getDireccionWeb();
            }
            return data;

        } finally {
            em.close();
        }
    }

    public Object[][] obtenerDatosArtistas() {
        EntityManager em = emf.createEntityManager();
        try {
            List<Artista> artistas = em.createQuery("SELECT a FROM Artista a", Artista.class).getResultList();
            Object[][] data = new Object[artistas.size()][8];

            for (int i = 0; i < artistas.size(); i++) {
                Artista artista = artistas.get(i);

                data[i][0] = artista.getNombre();
                data[i][1] = artista.getNick();
            }
            return data;

        } finally {
            em.close();
        }
    }

    public void registrarAlbumFavorito(String nick, String nombreAlbum) throws Exception {
        UsuarioJpaController aux = new UsuarioJpaController(emf);
        String albumid = nombreAlbum.trim();
        int indicePlay = albumid.indexOf('-');
        String idAlbumString = albumid.substring(0, indicePlay).trim();
        int idAlbum = Integer.parseInt(idAlbumString);
        Album album = auxAlbum.findAlbum(idAlbum);
        Cliente cliente = (Cliente) aux.findUsuario(nick);
        cliente.getAlbumesFavoritos().add(album);
        aux.edit(cliente);

    }
    
     public void registrarAlbumFavoritoWeb(String nick, int id) throws Exception {
        UsuarioJpaController aux = new UsuarioJpaController(emf);
        
        
  
        int idAlbum = id;
        Album album = auxAlbum.findAlbum(idAlbum);
        Cliente cliente = (Cliente) aux.findUsuario(nick);
        cliente.getAlbumesFavoritos().add(album);
        aux.edit(cliente);

    }
    

    public void registrarCancionFavorita(String nick, String nombreCancion) throws Exception {
        UsuarioJpaController aux = new UsuarioJpaController(emf);
        String cancionid = nombreCancion.trim();
        int indicePlay = cancionid.indexOf('-');
        String idPlayString = cancionid.substring(0, indicePlay).trim();
        int idCancion = Integer.parseInt(idPlayString);
        Cancion cancion = auxCan.findCancion(idCancion);
        Cliente cliente = (Cliente) aux.findUsuario(nick);
        cliente.getCancionesFavoritas().add(cancion);
        aux.edit(cliente);

    }

    public void eliminarCancionFavorita(String nick, String nombreCancion) throws Exception {

        UsuarioJpaController aux = new UsuarioJpaController(emf);
        String cancionid = nombreCancion.trim();
        int indicePlay = cancionid.indexOf('-');
        String idPlayString = cancionid.substring(0, indicePlay).trim();
        int idCancion = Integer.parseInt(idPlayString);
        Cancion cancion = auxCan.findCancion(idCancion);
        Cliente cliente = (Cliente) aux.findUsuario(nick);
        cliente.getCancionesFavoritas().remove(cancion);
        aux.edit(cliente);
    }

    public void eliminarAlbumFavorito(String nick, String nombreAlbum) throws Exception {
        UsuarioJpaController aux = new UsuarioJpaController(emf);
        String albumid = nombreAlbum.trim();
        int indicePlay = albumid.indexOf('-');
        String idAlbumString = albumid.substring(0, indicePlay).trim();
        int idAlbum = Integer.parseInt(idAlbumString);
        Album album = auxAlbum.findAlbum(idAlbum);
        Cliente cliente = (Cliente) aux.findUsuario(nick);
        cliente.getAlbumesFavoritos().remove(album);
        aux.edit(cliente);

    }
    
        public void eliminarAlbumFavoritoWeb(String nick, int id) throws Exception {
        UsuarioJpaController aux = new UsuarioJpaController(emf);
        int idAlbum = id;
        Album album = auxAlbum.findAlbum(idAlbum);
        Cliente cliente = (Cliente) aux.findUsuario(nick);
        cliente.getAlbumesFavoritos().remove(album);
        aux.edit(cliente);

    }
    
    
    

    public List<String> obtenerNicknamesseguidos(String usuario) throws Exception {
        List<String> aux = null;
        ClienteJpaController jpa = new ClienteJpaController(emf);
        Query query = jpa.getEntityManager().createNativeQuery("Select usuario_id from cliente_usuariosSeguidos where cliente_id ='" + usuario + "'");
        return query.getResultList();
    }
    
    public List<String> obtenerNicknamesseguidores(String usuario) throws Exception {
        List<String> aux = null;
        ClienteJpaController jpa = new ClienteJpaController(emf);
        Query query = jpa.getEntityManager().createNativeQuery("Select cliente_id from cliente_usuariosSeguidos where usuario_id ='" + usuario + "'");
        return query.getResultList();
    }

    public List<String> obtenerNicknamesDisponiblesASeguir(String usuario, List<String> usuariosSeguidos) throws Exception {

        ClienteJpaController jpa = new ClienteJpaController(emf);
        String jpql = "";
        TypedQuery<String> query = null;

        if (usuariosSeguidos.isEmpty()) {
            jpql = "SELECT u.nick FROM Usuario u";
            query = jpa.getEntityManager().createQuery(jpql, String.class);
        } else {
            jpql = "SELECT u.nick FROM Usuario u WHERE u.nick NOT IN :nickExlcuidos";
            query = jpa.getEntityManager().createQuery(jpql, String.class).setParameter("nickExlcuidos", usuariosSeguidos);;
        }
        return query.getResultList();
    }

    public void seguirUsuario(String usuario, String usuarioASeguir) throws Exception {
        EntityManager em = null;
        EntityTransaction transaction = null;
        try {
            em = emf.createEntityManager();
            transaction = em.getTransaction();
            transaction.begin();

            String sql = "INSERT INTO cliente_usuariosSeguidos (cliente_id, usuario_id) VALUES (?, ?)";
            Query query = em.createNativeQuery(sql);
            query.setParameter(1, usuario);
            query.setParameter(2, usuarioASeguir);
            query.executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            System.out.println(e);
            out.println("No Anda");
        }
    }

    public void dejarSeguirUsuario(String usuario, String usuarioADejarDeSeguir) throws Exception {
        EntityManager em = null;
        EntityTransaction transaction = null;
        try {
            em = emf.createEntityManager();
            transaction = em.getTransaction();
            transaction.begin();

            String sql = "Delete from cliente_usuariosSeguidos where cliente_id ='" + usuario + "' and usuario_id = '" + usuarioADejarDeSeguir + "'";
            Query query = em.createNativeQuery(sql);
            query.executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            System.out.println(e);
            out.println("No Anda");
        }
    }

    public void eliminarPlaylistFavorita(String nick, String nombrePlaylist) throws Exception {
        UsuarioJpaController aux = new UsuarioJpaController(emf);
        String playlistid = nombrePlaylist.trim();
        int indicePlay = playlistid.indexOf('-');
        String idPlayString = playlistid.substring(0, indicePlay).trim();
        int idPlaylist = Integer.parseInt(idPlayString);
        Cliente cliente = (Cliente) aux.findUsuario(nick);

        List<Playlist> playlistsFavoritas = cliente.getPlaylistFavoritos();

        Iterator<Playlist> iterator = playlistsFavoritas.iterator();

        while (iterator.hasNext()) {
            Playlist playlist = iterator.next();

            if (playlist.getId() == idPlaylist) {
                iterator.remove();
                break;
            }
        }

        aux.edit(cliente);
    }

    public List<String> obtenerNombresArtistas() {
        EntityManager em = emf.createEntityManager();
        try {

            List<Artista> artistas = em.createQuery("SELECT a FROM Artista a", Artista.class).getResultList();

            return artistas.stream()
                    .map(artista -> artista.getNick())
                    .collect(Collectors.toList());
        } finally {
            em.close();
        }
    }

    public Boolean inicioSesion(String nick, String password) {
    EntityManager em = emf.createEntityManager();
    Usuario usr = null;
    usr = em.find(Usuario.class, nick); //si no busca por nick
    return usr != null && checkPassword(password, usr.getContraseña());
    }
    public String getNickPorMail(String mail) {
        EntityManager em = emf.createEntityManager();
        String nick;
        try {
            nick = em.createQuery("SELECT u.nick FROM Usuario u WHERE u.mail = :mail", String.class).
                    setParameter("mail", mail).
                    getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
        return nick;
    }
     public boolean esCliente(String nickname) {
        if (auxCliente.findCliente(nickname)instanceof Cliente ) {
            return true;
        } else {
            return false;
        }
    }


//encriptar pw
    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());

    }

    public boolean checkPassword(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }
    
    //obtener datos del usuario almacenables en la sesion
    public Map<String, String> getDatosUsuario(String nick) {     
        Map<String, String> datos = new HashMap<>();
        EntityManager em = emf.createEntityManager();

        try {
            // Ejecutar la consulta JPQL que devuelve un Object[]
            Object[] datosSql = (Object[]) em.createNativeQuery("SELECT nick, apellido, fecNac, imagen, mail, nombre, tipo_usuario FROM usuario WHERE nick ='"+nick+"'").getSingleResult();
            // Convertir cada valor a String y almacenarlo en el mapa
            datos.put("nick", Fabrica.safeToString(datosSql[0]));
            datos.put("apellido", Fabrica.safeToString(datosSql[1]));
            datos.put("fecNac", Fabrica.safeToString(datosSql[2])); // Asegúrate de formatear adecuadamente si es un Date
            datos.put("imagen", Fabrica.safeToString(datosSql[3])); // Si es un byte[], convierte a String apropiadamente
            datos.put("mail", Fabrica.safeToString(datosSql[4]));
            datos.put("nombre", Fabrica.safeToString(datosSql[5]));
            datos.put("tipo_usuario", Fabrica.safeToString(datosSql[6]));
    
        } catch (Exception e) {
            e.printStackTrace(); 
        } finally {
            em.close(); 
        }

        return datos;
    }

    public String artistaNombre(String nick){
       Artista art = (Artista) usrController.findUsuario(nick);
       String nombre = art.getNombre();
       String apellido = art.getApellido();
       
       
        if (apellido == null || apellido.isEmpty()) {
        return nombre; // Retorna solo el nombre si no hay apellido
    } else {
        return nombre + " " + apellido; // Retorna nombre completo con un espacio
    }
        
    }
     public void CambiarEstadosubscripcion(String nick, String estado, Integer tipo, LocalDate fecha) throws Exception {
    // 1. Busca al cliente por su nick
    Cliente cliente = (Cliente) usrController.findUsuario(nick);
    
    if (cliente == null) {
        throw new Exception("Cliente no encontrado con nick: " + nick);
    }

    boolean cambios = false;

    // 2. Actualiza los campos solo si hay cambios
    if (estado != null && !estado.equals(cliente.getEstado())) {
        cliente.setEstado(estado);
        cambios = true;
    }
    if (tipo != null && !tipo.equals(cliente.getTipo())) {
        cliente.setTipo(tipo);
        cambios = true;
    }
    if (fecha != null && !fecha.equals(cliente.getFecSub())) {
        cliente.setFecSub(fecha);
        cambios = true;
    }

    // 3. Solo persiste si hubo cambios
    if (cambios) {
       usrController.edit(cliente);  // Verifica que este método use merge() correctamente.
    }
}
}
