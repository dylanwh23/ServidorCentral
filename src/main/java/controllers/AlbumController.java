/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import controllers.IAlbumController;
import java.util.ArrayList;
import models.Album;
import persistence.AlbumJpaController;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.Persistence;
import models.Artista;
import models.Cancion;
import models.Cliente;
import models.Genero;
import persistence.ArtistaJpaController;
import persistence.CancionJpaController;
import persistence.ClienteJpaController;
import persistence.GeneroJpaController;

/**
 *
 * @author Machichu
 */
public class AlbumController implements IAlbumController {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("grupo6_Spotify");

    private ArtistaJpaController art_ctr;
    private ClienteJpaController auxCliente;
    private AlbumJpaController auxAL;
    private GeneroJpaController auxG;
    private CancionJpaController auxCan;
    private ICancionController canCon;

    public AlbumController() {
        Fabrica fabrica = Fabrica.getInstance();
        this.art_ctr = fabrica.getArtistaJpaController();
        this.auxCliente = fabrica.getClienteJpaController();
        this.auxAL = fabrica.getAlbumJpaController();
        this.auxG = fabrica.getGeneroJpaController();
        this.auxCan = fabrica.getCancionJpaController();
        this.canCon = fabrica.getICancionController();
    }

    public AlbumController(EntityManagerFactory emf) {
        this.emf = emf;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    // Función que buscará al artista por su nombre, sin modificar el ArtistaJpaController
    public boolean buscarArtista(String nombreA) {
        EntityManager em = getEntityManager();
        try {

            TypedQuery<Artista> query = em.createQuery("SELECT a FROM Artista a WHERE a.nick = :nick", Artista.class);
            query.setParameter("nick", nombreA);

            List<Artista> resultados = query.getResultList();

            return !resultados.isEmpty();
        } finally {
            em.close();
        }
    }

    public boolean buscarAlbum(String nombreAl) {
        EntityManager em = getEntityManager();
        try {

            TypedQuery<Album> query = em.createQuery("SELECT a FROM Album a WHERE a.nombre = :nombre", Album.class);
            query.setParameter("nombre", nombreAl);

            List<Album> resultados = query.getResultList();
            return !resultados.isEmpty();
        } catch (Exception e) {
            System.out.println("Error al buscar el álbum: " + e.getMessage());
            return false;
        } finally {
            em.close();
        }
    }

    public boolean registrarAlbum(String nombre, int anio, List<Genero> generos) {
        Album al = new Album();
        al.setNombre(nombre);
        al.setAnioo(anio);
        al.setGeneros(generos);

        try {
            auxAL.create(al);
            return true;
        } catch (Exception ex) {

            return false;
        }
    }

    public List<String> obtenerNombresAlbums() {
        List<Album> albums = auxAL.findAlbumEntities();
        return albums.stream()
                .map(album -> album.getId() + " - " + album.getNombre())
                .collect(Collectors.toList());
    }

    public void CrearAlbum(String text, int parseInt, String strArtista, String Direccion_imagen, Object[][] cancionesOBJ, List<String> generos) {
        List<Cancion> canciones = new ArrayList<>();
        for (Object[] fila : cancionesOBJ) {
            Cancion cancion = new Cancion();
            cancion.setNombre((String) fila[1]);  // Nombre
            cancion.setDuracion((Integer) fila[2]);  // Duración
            cancion.setDireccion_archivo_de_audio((String) fila[3]);  // Ruta MP3
            //cancion.setDireccion_imagen((String) fila[4]);  // Imagen
            cancion.setGenero(auxG.findGenero((String) fila[4]));
            canciones.add(cancion);  // Agregar la canción a la lista
        }

        Artista artista = art_ctr.findArtista(strArtista);
        List<Genero> generosSeleccionados = new ArrayList<>();
        for (String nombre : generos) {
            Genero genero = auxG.findGenero(nombre); // Método que busca el Genero por su nombre
            if (genero != null) {
                generosSeleccionados.add(genero); // Añadir a la lista si se encontró
            }
        }
        Album album = new Album(text, parseInt, artista, Direccion_imagen, generosSeleccionados, canciones);
        auxAL.create(album);
    }
    
    public Album findAlbum(int id){
       Album album = auxAL.findAlbum(id);
        return album;
    }

    public List<String> obtenerNombresAlbumsFavoritos(String clienteNick) {
        // Busca al cliente por su nick
        Cliente cliente = auxCliente.findCliente(clienteNick);

        // Si no se encuentra el cliente, retorna una lista vacía
        if (cliente == null) {
            return new ArrayList<>();
        }

        // Obtener los álbumes favoritos del cliente
        List<Album> albumesFavoritos = cliente.getAlbumesFavoritos();

        // Mapear los álbumes favoritos a una lista de nombres
        return albumesFavoritos.stream()
                .map(album -> album.getId() + " - " + album.getNombre())
                .collect(Collectors.toList());
    }

    public List<Integer> obtenerIdAlbumsFavoritos(String clienteNick) {
        // Busca al cliente por su nick
        Cliente cliente = auxCliente.findCliente(clienteNick);

        // Si no se encuentra el cliente, retorna una lista vacía
        if (cliente == null) {
            return new ArrayList<>();
        }

        // Obtener los álbumes favoritos del cliente
        List<Album> albumesFavoritos = cliente.getAlbumesFavoritos();

        // Mapear los álbumes favoritos a una lista de nombres
        return albumesFavoritos.stream()
                .map(album -> album.getId())
                .collect(Collectors.toList());
    }
    
    /*
    public List<Album> BuscarAlbumGenero(String nombreGenero) {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery("SELECT a FROM Album a WHERE a.genero.nombre = :nombreGenero", Album.class)
                    .setParameter("nombreGenero", nombreGenero)
                    .getResultList();
        } finally {
            em.close();
        }
    }
*/
    public Object[][] obtenerAlbumesPorGenero(String nombreGenero) {
        EntityManager em = emf.createEntityManager();
        try {
           List<Album> albumes = em.createQuery(
                    "SELECT a FROM Album a JOIN a.generos g WHERE g.nombre = :nombreGenero", Album.class)
                    .setParameter("nombreGenero", nombreGenero)
                    .getResultList();
        Object[][] data = new Object[albumes.size()][9];

            for (int i = 0; i < albumes.size(); i++) {
                Album album = albumes.get(i);
                data[i][0] = album.getId();
                data[i][1] = album.getNombre();
                data[i][2] = album.getAnioo();
                data[i][3] = album.getArtista().getNombre();
                data[i][4] = album.getGeneros().stream()
                        .map(Genero::getNombre)
                        .collect(Collectors.joining(", "));
                data[i][5] = album.getCanciones().stream()
                        .map(Cancion::getNombre)
                        .collect(Collectors.joining(", "));
                data[i][6] = album.getDireccion_imagen();
                data[i][7] = album.getArtista().getApellido();
                data[i][8] = album.getArtista().getNick();

            }
            return data;
            
        } finally {
            em.close();
        }
    }

    public Object[][] obtenerAlbumArtista(String nickArtista) {
        EntityManager em = emf.createEntityManager();
        try {
           List<Album> albumes = em.createQuery(
                    "SELECT a FROM Album a JOIN a.artista art WHERE art.nick = :nickArtista", Album.class)
                    .setParameter("nickArtista", nickArtista)
                    .getResultList();
        Object[][] data = new Object[albumes.size()][9];

            for (int i = 0; i < albumes.size(); i++) {
                Album album = albumes.get(i);
                data[i][0] = album.getId();
                data[i][1] = album.getNombre();
                data[i][2] = album.getAnioo();
                data[i][3] = album.getArtista().getNombre();
                data[i][4] = album.getGeneros().stream()
                        .map(Genero::getNombre)
                        .collect(Collectors.joining(", "));
                data[i][5] = album.getCanciones().stream()
                        .map(Cancion::getNombre)
                        .collect(Collectors.joining(", "));
                data[i][6] = album.getDireccion_imagen();
                data[i][7] = album.getArtista().getApellido();
                data[i][8] = album.getArtista().getNick();
            }
            return data;
            
        } finally {
            em.close();
        }
    }
    
    public Object[][] obtenerAlbumArtistaNombres(String nickArtista) {
         EntityManager em = emf.createEntityManager();
        try {
            List<Album> albumes = em.createQuery("SELECT a FROM Album a JOIN a.artista art WHERE art.nick = :nickArtista", Album.class).setParameter("nickArtista", nickArtista).getResultList();
            Object[][] data = new Object[albumes.size()][6];

            for (int i = 0; i < albumes.size(); i++) {
                Album album = albumes.get(i);
                data[i][0] = album.getId();
                data[i][1] = album.getNombre();
            }
            return data;

        } finally {
            em.close();
        }
    }
//SELECT a FROM Album a JOIN a.artista art WHERE art.nick = :nickArtista
    public Object[][] obtenerDatosAlbum(int id) {
        EntityManager em = emf.createEntityManager();
        try {
            List<Album> albumes = em.createQuery("SELECT a FROM Album a WHERE a.id = :id", Album.class).setParameter("id", id).getResultList();
            Object[][] data = new Object[albumes.size()][9];

            for (int i = 0; i < albumes.size(); i++) {
                Album album = albumes.get(i);
                data[i][0] = album.getId();
                data[i][1] = album.getNombre();
                data[i][2] = album.getAnioo();
                data[i][3] = album.getArtista().getNombre();
                data[i][4] = album.getGeneros().stream()
                        .map(Genero::getNombre)
                        .collect(Collectors.joining(", "));
                data[i][5] = album.getCanciones().stream()
                        .map(Cancion::getNombre)
                        .collect(Collectors.joining(", "));
                data[i][6] = album.getDireccion_imagen();
                data[i][7] = album.getArtista().getApellido();
                data[i][8] = album.getArtista().getNick();

            }
            return data;

        } finally {
            em.close();
        }

    }

    public String obtenerArtistaPorAlbum(String nombreAlbum) {
        EntityManager em = getEntityManager();
        try {
            // Realiza una consulta para buscar el álbum por su nombre
            TypedQuery<Album> query = em.createQuery("SELECT a FROM Album a WHERE a.nombre = :nombre", Album.class);
            query.setParameter("nombre", nombreAlbum);

            List<Album> resultados = query.getResultList();

            // Si encontramos el álbum, retornamos el nombre del artista
            if (!resultados.isEmpty()) {
                Album album = resultados.get(0); // Tomamos el primer resultado
                return album.getArtista().getNick(); // Suponiendo que el artista tiene un método getNick()
            } else {
                return null; // No se encontró el álbum
            }
        } finally {
            em.close();
        }
    }

    
    public String obtenerArtistaAlbum(int id) {
        EntityManager em = emf.createEntityManager();
           
        Album album = auxAL.findAlbum(id);
        return album.getArtista().getNick();
        
    }
    
    public Object[][] obtenerDatosCancionAlbum(int id) {
        Album album = auxAL.findAlbum(id);
        if (album == null) {
            return new Object[0][0];
           
        }
        List<Cancion> canciones = album.getCanciones();
        Object[][] datos = new Object[canciones.size()][6];

        for (int i = 0; i < canciones.size(); i++) {
            Cancion cancion = canciones.get(i);
            datos[i][0] = cancion.getId();
            datos[i][1] = cancion.getNombre();
            datos[i][4] = album.getDireccion_imagen();
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
    /*
    public Object[] obtenerDatosCompletoCancion(int idCancion) {
    Cancion cancion = auxCan.findCancion(idCancion);
    
    if (cancion == null) {
        return new Object[0]; // Devuelve un arreglo vacío si la canción no existe.
    }

    // Obtener el álbum asociado a la canción.
    List<Album> albumsAux = auxAL.findAlbumEntities();
    Album albumAux = new Album();
   
    
    for (Album album : albumsAux) {
        // Obtener las canciones asociadas al álbum actual
        List<Cancion> canciones = album.getCanciones();
        
        // Recorrer las canciones del álbum
        for (Cancion cancionaux : canciones) {
            // Verificar si la canción tiene el ID que estamos buscando
            if (cancion.getId() == idCancion) {
                // Si la canción es encontrada, devolvemos el álbum
                albumAux = album;
            }
        }
    }
    
    
    
    // Crear el arreglo de datos que contendrá toda la información.
    Object[] datos = new Object[11];

    // Rellenar el arreglo con los datos de la canción y su álbum.
    datos[0] = cancion.getId();
    datos[1] = cancion.getNombre();
    datos[2] = cancion.getDuracion();
    datos[3] = cancion.getDireccion_archivo_de_audio();
    datos[4] = albumAux.getDireccion_imagen();

    // Verificar si la canción tiene un género asociado.
    if (cancion.getGenero() != null) {
        datos[5] = cancion.getGenero().toString();
    } else {
        datos[5] = "Sin Genero asociado";
    }

    // Datos del álbum y el artista
    datos[6] = albumAux.getNombre();
    datos[7] = albumAux.getArtista().getNombre();
    datos[8] = albumAux.getArtista().getNick();
    datos[9] = albumAux.getId();
    datos[10] = albumAux.getArtista().getApellido();

    return datos;
}
*/
 public Object[][] obtenerAlbumesPorGeneroObj(String generoSeleccionado) {
        EntityManager em = emf.createEntityManager();
        try {
            List<Album> albumes = em.createQuery("SELECT a FROM Album a JOIN a.generos g WHERE g.nombre = :generoSeleccionado", Album.class).setParameter("generoSeleccionado", generoSeleccionado).getResultList();
            Object[][] data = new Object[albumes.size()][7];

            for (int i = 0; i < albumes.size(); i++) {
                Album album = albumes.get(i);
               data[i][0] = album.getId();
               data[i][1] = album.getNombre();

            }
            return data;

        } finally {
            em.close();
        }
    }
;    
  public Object[][] obtenerAlbumArtistaObj(String artistaSeleccionado) {
        EntityManager em = emf.createEntityManager();
        try {
            List<Album> albumes = em.createQuery("SELECT a FROM Album a JOIN a.artista art WHERE art.nick = :artistaSeleccionado", Album.class).setParameter("artistaSeleccionado", artistaSeleccionado).getResultList();
            Object[][] data = new Object[albumes.size()][7];

            for (int i = 0; i < albumes.size(); i++) {
                Album album = albumes.get(i);
                data[i][0] = album.getId();
                data[i][1] = album.getNombre();
               
            }
            return data;

        } finally {
            em.close();
        }
    }
  public Object[] obtenerDatosCompletoCancion(int idCancion) {
    Cancion cancion = auxCan.findCancion(idCancion);

    if (cancion == null) {
        return new Object[0]; // Devuelve un arreglo vacío si la canción no existe.
    }

    // Obtener el álbum asociado a la canción.

   Album albumAux = auxAL.findAlbum(canCon.obtenerIdAlbum(cancion.getId()));






    // Crear el arreglo de datos que contendrá toda la información.
    Object[] datos = new Object[11];

    // Rellenar el arreglo con los datos de la canción y su álbum.
    datos[0] = cancion.getId();
    datos[1] = cancion.getNombre();
    datos[2] = cancion.getDuracion();
    datos[3] = cancion.getDireccion_archivo_de_audio();
    datos[4] = albumAux.getDireccion_imagen();

    // Verificar si la canción tiene un género asociado.
    if (cancion.getGenero() != null) {
        datos[5] = cancion.getGenero().toString();
    } else {
        datos[5] = "Sin Genero asociado";
    }

    // Datos del álbum y el artista
    datos[6] = albumAux.getNombre();
    datos[7] = albumAux.getArtista().getNombre();
    datos[8] = albumAux.getArtista().getNick();
    datos[9] = albumAux.getId();
    datos[10] = albumAux.getArtista().getApellido();


    return datos;
}
}
