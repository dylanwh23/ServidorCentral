/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package controllers;

import java.util.List;
import models.Playlist;
import persistence.exceptions.NonexistentEntityException;

/**
 *
 * @author Machichu
 */
public interface IPlaylistController {
    public abstract void crearPlaylistPorDefecto(String nombre ,String genero,String rutaImagen);
    public abstract int crearPlaylistParticular(String Nombre, String rutaImagen, String nick_usuario,boolean privada);
    public abstract Object[][] obtenerPlaylistLista();
    public abstract Object[][] obtenerPlaylistLista(int id);
    public abstract Object[][] obtenerDatosPlaylistGenero(String genero);
    public abstract Object[][] obtenerDatosPlaylistCliente(String nick);
    public abstract void crearPlaylist(Playlist playlist);
    public abstract Object[][] obtenerDatosCancionesPlaylist(int id);
    public abstract List<String> obtenerNombresPlaylistPorDefecto();
    public abstract List<String> obtenerNombresPlaylistParticularCliente(String nick);
    public abstract void crearRelacionPlaylistCancion( String stringPlaylist, String stringCancion) throws Exception;
    public abstract List<String> obtenerNombresPlaylistCanciones(String stringPlaylist);
    public abstract void borrarRelacionPlaylistCancion(String stringPlaylist, String stringCancion) throws Exception;
    public abstract void Publicar_Lista(int id);
    public abstract List<String> obtenerNombresDeCancionesNoPresentesPlaylist(String stringPlaylist);
    public abstract List<String> obtenerNombresPlaylists();
    public abstract List<String> obtenerNombresPlaylistPublicas();
    public abstract List<String> obtenerNombresDePlaylistsFavoritas(String clienteNick);
    public abstract List<Integer> obtenerIdPlaylistFavoritos(String clienteNick);
    public abstract Playlist findPlaylist(int id);
    public abstract Object[][] obtenerDatosPlaylist(int id);
    public abstract void eliminarLista(int id) throws NonexistentEntityException;

   
   
}
