/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package controllers;

import java.util.List;
import models.Album;
import models.Genero;

/**
 *
 * @author F0liver4
 */
public interface IAlbumController {
    public abstract boolean buscarArtista(String nombreA);
    public abstract boolean buscarAlbum(String nombreAl);
    public abstract boolean registrarAlbum(String nombre, int anio,List<Genero> generos);
    public abstract List<String> obtenerNombresAlbums();
    public abstract void CrearAlbum(String text, int parseInt, String strArtista, String Direccion_imagen,  Object[][] cancionesOBJ, List<String> generos);
    public abstract List<String> obtenerNombresAlbumsFavoritos(String clienteNick);
    public abstract Object[][] obtenerAlbumesPorGenero(String nombreGenero);
    public abstract Object[][] obtenerAlbumArtista(String nickArtista);
    public abstract Object[][] obtenerDatosAlbum(int id);
    public abstract String obtenerArtistaAlbum(int id);
    public abstract List<Integer> obtenerIdAlbumsFavoritos(String clienteNick);
    public abstract Object[][] obtenerAlbumArtistaNombres(String nickArtista);
    public abstract Object[][] obtenerDatosCancionAlbum(int id);
    public abstract Album findAlbum(int id);
    public abstract Object[] obtenerDatosCompletoCancion(int idCancion);
    public abstract Object[][] obtenerAlbumesPorGeneroObj(String generoSeleccionado);
    public abstract Object[][] obtenerAlbumArtistaObj(String artistaSeleccionado);


    
}
