
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import models.Album;
import models.Cliente;	
import models.Cancion;
import models.Playlist;
import models.PlaylistParticular;
import models.PlaylistPorDefecto;
import persistence.AlbumJpaController;
import persistence.CancionJpaController;
import persistence.GeneroJpaController;
import persistence.PlaylistJpaController;
import persistence.ClienteJpaController;
import persistence.exceptions.NonexistentEntityException;

/**
 *
 * @author Machichu
 */
public class PlaylistController implements IPlaylistController{
    private CancionController canCon = new CancionController() ;
    private PlaylistJpaController auxPlay;
    private GeneroJpaController auxGen;
    private CancionJpaController auxCan;
    private ClienteJpaController usr_ctr;
    private AlbumJpaController albCrl;
    private EntityManagerFactory emf;

    // Constructor que obtiene las dependencias desde la fábrica
    public PlaylistController() {
        // Obtener la instancia de la fábrica
        Fabrica fabrica = Fabrica.getInstance();

        // Obtener los controladores JPA necesarios desde la fábrica
        this.auxPlay = fabrica.getPlaylistJpaController();
        this.auxGen = fabrica.getGeneroJpaController();
        this.auxCan = fabrica.getCancionJpaController();
        this.usr_ctr = fabrica.getClienteJpaController();
        this.albCrl = fabrica.getAlbumJpaController();
        this.emf = fabrica.getEntityManagerFactory();
    }
     
     
    public void crearPlaylistPorDefecto(String nombre ,String genero,String rutaImagen){
            PlaylistPorDefecto playlist = new PlaylistPorDefecto();
            playlist.setGenero(auxGen.findGenero(genero));
            playlist.setNombre(nombre);
            playlist.setRutaImagen(rutaImagen);
        try {
             auxPlay.create(playlist);
            
            } 
            catch (Exception ex) {
             Logger.getLogger(PlaylistController.class.getName()).log(Level.SEVERE, null, ex);
         }
    }

    public int crearPlaylistParticular(String Nombre, String rutaImagen, String nick_usuario,boolean privada) {
        Cliente el_usr = usr_ctr.findCliente(nick_usuario);
        PlaylistParticular la_nueva_lista = new PlaylistParticular(true, Nombre, rutaImagen, new LinkedList<Cancion>(), el_usr);
        la_nueva_lista.setPrivada(privada);
        try {
            auxPlay.create(la_nueva_lista);
            
             
        } catch (Exception ex) {
            Logger.getLogger(PlaylistController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return la_nueva_lista.getId();
    }
    
  
    public Object[][] obtenerPlaylistLista( int id) {
        Playlist playlist = auxPlay.findPlaylist(id);
        if (playlist == null) {
            return new Object[0][0]; 
        }
        Object[][] data = new Object[1][7]; 
            if (playlist instanceof PlaylistPorDefecto) {
                PlaylistPorDefecto PlaylistPorDefecto = (PlaylistPorDefecto) playlist;
                 data[0][0] = PlaylistPorDefecto.getRutaImagen();
                data[0][1] = PlaylistPorDefecto.getId();
                data[0][2] = PlaylistPorDefecto.getNombre();
                data[0][3] = "Por Defecto";
                data[0][4] = "";
                data[0][5] = PlaylistPorDefecto.getGenero().getNombre().toString();

            } else if (playlist instanceof PlaylistParticular) {
                PlaylistParticular PlaylistParticular = (PlaylistParticular) playlist;
                data[0][0] = PlaylistParticular.getRutaImagen();
                data[0][1] = PlaylistParticular.getId();
                data[0][2] = PlaylistParticular.getNombre();
                data[0][3] = "Particular";
                data[0][4] = PlaylistParticular.getPrivada();
                data[0][5] = "";
                data[0][6] = PlaylistParticular.getPropietario().getNick();
            } else {
                data[0][0] = playlist.getRutaImagen();
                data[0][1] = playlist.getId();
                data[0][2] = playlist.getNombre();
                data[0][3] = "???";

            }
        
        
        return data;
     }
    public Object[][] obtenerPlaylistLista() {
        List<Playlist> playlists = auxPlay.findPlaylistEntities();
        Object[][] data = new Object[playlists.size()][7]; 

        for (int i = 0; i < playlists.size(); i++) {
            Playlist playlist = playlists.get(i);

            // Si Playlist tiene subclases, puedes hacer un check del tipo
            if (playlist instanceof PlaylistPorDefecto) {
                PlaylistPorDefecto PlaylistPorDefecto = (PlaylistPorDefecto) playlist;
                 data[i][0] = PlaylistPorDefecto.getRutaImagen();
                data[i][1] = PlaylistPorDefecto.getId();
                data[i][2] = PlaylistPorDefecto.getNombre();
                data[i][3] = "Por Defecto";
                data[i][4] = "";
                data[i][5] = PlaylistPorDefecto.getGenero().getNombre().toString();

            } else if (playlist instanceof PlaylistParticular) {
                PlaylistParticular PlaylistParticular = (PlaylistParticular) playlist;
                data[i][0] = PlaylistParticular.getRutaImagen();
                data[i][1] = PlaylistParticular.getId();
                data[i][2] = PlaylistParticular.getNombre();
                data[i][3] = "Particular";
                data[i][4] = PlaylistParticular.getPrivada();
                data[i][5] = "";
                data[i][6] = PlaylistParticular.getPropietario().getNick();
            } else {
                data[i][0] = playlist.getRutaImagen();
                data[i][1] = playlist.getId();
                data[i][2] = playlist.getNombre();
                data[i][3] = "???";

            }
        }

        return data;
    }
    
    public Object[][] obtenerDatosPlaylist(int id) {
        
        Playlist playlist = auxPlay.findPlaylist(id);
        if (playlist == null) {
            return new Object[0][0]; 
        }
        List<Cancion> canciones = playlist.getCanciones();
        Object[][] datos = new Object[canciones.size()][6];

        for (int i = 0; i < canciones.size(); i++) {
            Cancion cancion = canciones.get(i);
            datos[i][0] = cancion.getId();
            datos[i][1] = cancion.getNombre();
            datos[i][4] = canCon.obtenerFoto(cancion.getId());
            datos[i][3] = cancion.getDireccion_archivo_de_audio();
            datos[i][2] = cancion.getDuracion();
            if(cancion.getGenero()!=null){
            datos[i][5] = cancion.getGenero().toString();
            }else{
            datos[i][5] ="Sin Genero asociado";
            }
            
        }
        return datos;
    }
    public Object[][] obtenerDatosPlaylistGenero(String genero) {
        EntityManager em = emf.createEntityManager();
        try {
            List<Playlist> playlists = em.createQuery("SELECT p FROM PlaylistPorDefecto p WHERE p.genero.nombre = :generoNombre", Playlist.class).setParameter("generoNombre", genero).getResultList();
            Object[][] data = new Object[playlists.size()][6];

            for (int i = 0; i < playlists.size(); i++) {
                Playlist playlist = playlists.get(i);

                // Si Playlist tiene subclases, puedes hacer un check del tipo
                if (playlist instanceof PlaylistPorDefecto) {
                    PlaylistPorDefecto PlaylistPorDefecto = (PlaylistPorDefecto) playlist;
                    data[i][1] = PlaylistPorDefecto.getId();
                    data[i][2] = PlaylistPorDefecto.getNombre();
                    data[i][0] = PlaylistPorDefecto.getRutaImagen();
                    data[i][3] = "Por Defecto";
                    data[i][4] = (PlaylistPorDefecto.getGenero().getNombre().toString());
                }
            }
            return data;

        } finally {
            em.close();
        }
    }
    
    public Playlist findPlaylist(int id){
       Playlist play = auxPlay.findPlaylist(id);
        
        return play;
    }
    
    
    public Object[][] obtenerDatosPlaylistCliente(String nick) {
        EntityManager em = emf.createEntityManager();
        try {
            List<Playlist> playlists = em.createQuery("SELECT p FROM PlaylistParticular p WHERE p.Propietario.nick = :nick", Playlist.class).setParameter("nick", nick).getResultList();
            Object[][] data = new Object[playlists.size()][6];
            for (int i = 0; i < playlists.size(); i++) {
                Playlist playlist = playlists.get(i);
                if (playlist instanceof PlaylistParticular) {
                    PlaylistParticular PlaylistParticular = (PlaylistParticular) playlist;
                    data[i][1] = PlaylistParticular.getId();
                    data[i][2] = PlaylistParticular.getNombre();
                    data[i][0] = PlaylistParticular.getRutaImagen();
                    data[i][3] = "Particular";
                    data[i][4] = PlaylistParticular.getPrivada();
		    data[i][5] = PlaylistParticular.getPropietario().getNick();
                }
            }
            return data;

        } finally {
            em.close();
        }
    }
    
    public void crearPlaylist(Playlist playlist) {
        auxPlay.create(playlist);
    }

    public Object[][] obtenerDatosCancionesPlaylist(int id) {
        
        Playlist playlist = auxPlay.findPlaylist(id);
        if (playlist == null) {
            return new Object[0][0]; 
        }
        List<Cancion> canciones = playlist.getCanciones();
        Object[][] datos = new Object[canciones.size()][11];

        for (int i = 0; i < canciones.size(); i++) {
            Cancion cancion = canciones.get(i);
            Album albumAux = albCrl.findAlbum(canCon.obtenerIdAlbum(cancion.getId()));
            datos[i][0] = cancion.getId();
            datos[i][1] = cancion.getNombre();
            datos[i][4] = albumAux.getDireccion_imagen();
            datos[i][3] = cancion.getDireccion_archivo_de_audio();
            datos[i][2] = cancion.getDuracion();
            if(cancion.getGenero()!=null){
            datos[i][5] = cancion.getGenero().toString();
            }else{
            datos[i][5] ="Sin Genero asociado";
            }
            datos[i][9] =albumAux.getId();
            datos[i][6] =albumAux.getNombre();
            datos[i][7] =albumAux.getArtista().getNombre();
            datos[i][8] =albumAux.getArtista().getNick();
            datos[i][10] =albumAux.getArtista().getApellido();
        }
        return datos;
    }
    public List<String> obtenerNombresPlaylistPorDefecto() {
        List<Playlist> playlists = auxPlay.findPlaylistEntities();
        return playlists.stream()
                .filter(playlist -> playlist instanceof PlaylistPorDefecto) 
                .map(playlist -> playlist.getId()+ " - " + playlist.getNombre()) 
                .collect(Collectors.toList());
    }

    public List<String> obtenerNombresPlaylistParticularCliente(String nick) {
        EntityManager em = emf.createEntityManager();
        try {
            List<Playlist> playlists = em.createQuery("SELECT p FROM PlaylistParticular p WHERE p.Propietario.nick = :nick", Playlist.class).setParameter("nick", nick).getResultList();
            return playlists.stream()
                    .filter(playlist -> playlist instanceof PlaylistParticular) 
                    .map(playlist -> playlist.getId()+ " - " + playlist.getNombre())
                    .collect(Collectors.toList());
        } finally {
            em.close();
        }
    }

    public void crearRelacionPlaylistCancion( String stringPlaylist, String stringCancion) throws Exception {
        stringPlaylist = stringPlaylist.trim();
        int indicePlay = stringPlaylist.indexOf('-');
        String idPlayString = stringPlaylist.substring(0, indicePlay).trim();
        int idPlaylist = Integer.parseInt(idPlayString); 
        Playlist playlist = auxPlay.findPlaylist(idPlaylist);
        stringCancion = stringCancion.trim();
        int indiceCan = stringCancion.indexOf('-');
        String idCanString = stringCancion.substring(0, indiceCan).trim();
        int idCancion = Integer.parseInt(idCanString); 
        playlist.getCanciones().add(auxCan.findCancion(idCancion));
        auxPlay.edit(playlist);
    }

    public List<String> obtenerNombresPlaylistCanciones(String stringPlaylist) {
        stringPlaylist = stringPlaylist.trim();
        int indicePlay = stringPlaylist.indexOf('-');
        String idPlayString = stringPlaylist.substring(0, indicePlay).trim();
        int idPlaylist = Integer.parseInt(idPlayString); 
        Playlist playlist = auxPlay.findPlaylist(idPlaylist);
        List<Cancion> canciones = playlist.getCanciones();
        return canciones.stream()
                           .map(cancion -> cancion.getId()+" - "+cancion.getNombre())
                           .collect(Collectors.toList());
    }

    public void borrarRelacionPlaylistCancion(String stringPlaylist, String stringCancion) throws Exception {
        stringPlaylist = stringPlaylist.trim();
        int indicePlay = stringPlaylist.indexOf('-');
        String idPlayString = stringPlaylist.substring(0, indicePlay).trim();
        int idPlaylist = Integer.parseInt(idPlayString); 
        Playlist playlist = auxPlay.findPlaylist(idPlaylist);
        
        stringCancion = stringCancion.trim();
        int indiceCan = stringCancion.indexOf('-');
        String idCanString = stringCancion.substring(0, indiceCan).trim();
        int idCancion = Integer.parseInt(idCanString); 
        
        playlist.getCanciones().remove(auxCan.findCancion(idCancion));
        auxPlay.edit(playlist);
    }
    public void Publicar_Lista(int id){
	    try{
   	PlaylistParticular la_lista_de_reproduccion = (PlaylistParticular) auxPlay.findPlaylist(id);
	la_lista_de_reproduccion.setPrivada(false);
	auxPlay.edit(la_lista_de_reproduccion);
	    } catch (Exception e){}
    }

    public List<String> obtenerNombresDeCancionesNoPresentesPlaylist(String stringPlaylist) {
        EntityManager em = emf.createEntityManager();
        stringPlaylist = stringPlaylist.trim();
        int indicePlay = stringPlaylist.indexOf('-');
        String idPlayString = stringPlaylist.substring(0, indicePlay).trim();
        int idPlaylist = Integer.parseInt(idPlayString);
        Playlist playlist = auxPlay.findPlaylist(idPlaylist);
        List<Integer> idsCancionesEnPlaylist = playlist.getCanciones().stream()
                .map(Cancion::getId)
                .collect(Collectors.toList());
        List<Cancion> canciones;
        if (idsCancionesEnPlaylist.isEmpty()) {
            canciones = auxCan.findCancionEntities();
        } else {
            canciones = em.createQuery("SELECT c FROM Cancion c WHERE c.id NOT IN :ids", Cancion.class).setParameter("ids", idsCancionesEnPlaylist).getResultList();;
        }
        return canciones.stream()
                .map(cancion -> cancion.getId() + " - " + cancion.getNombre())
                .collect(Collectors.toList());
    }
    
    
   public List<String> obtenerNombresPlaylists() {
    List<Playlist> playlists = auxPlay.findPlaylistEntities();
    return playlists.stream()
                    .map(playlist -> playlist.getNombre())
                    .collect(Collectors.toList());
}

    public List<String> obtenerNombresPlaylistPublicas() {
     EntityManager em = emf.createEntityManager();
        try {
           List<PlaylistParticular> playlists = em.createQuery("SELECT p FROM PlaylistParticular p WHERE p.privada = false", PlaylistParticular.class).getResultList();
            return playlists.stream()
                    .filter(playlist -> playlist instanceof PlaylistParticular) 
                    .map(playlist -> playlist.getId()+ " - " + playlist.getNombre())
                    .collect(Collectors.toList());
        } finally {
            em.close();
        }
    }

  
public List<String> obtenerNombresDePlaylistsFavoritas(String clienteNick) {
  
    Cliente cliente = usr_ctr.findCliente(clienteNick);

   
    if (cliente == null) {
        return new ArrayList<>();
    }

   
    List<Playlist> playlistsFavoritas = cliente.getPlaylistFavoritos();

    
    return playlistsFavoritas.stream()
            .map(playlist -> playlist.getId() + " - " + playlist.getNombre())
            .collect(Collectors.toList());
}

  public List<Integer> obtenerIdPlaylistFavoritos(String clienteNick) {
        // Busca al cliente por su nick
        Cliente cliente = usr_ctr.findCliente(clienteNick);

        // Si no se encuentra el cliente, retorna una lista vacía
        if (cliente == null) {
            return new ArrayList<>();
        }

        // Obtener los álbumes favoritos del cliente
        List<Playlist> playlistsFavoritas = cliente.getPlaylistFavoritos();

        // Mapear los álbumes favoritos a una lista de nombres
        return playlistsFavoritas.stream()
                .map(album -> album.getId())
                .collect(Collectors.toList());
    }
  
  public void eliminarLista(int id) throws NonexistentEntityException{

       auxPlay.destroy(id);
} 
    
}
   
   

