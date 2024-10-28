/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package controllers;

import java.util.List;

/**
 *
 * @author lilia
 */
public interface ICancionController {
    public abstract boolean CrearCancion(String nombre, int duracion);
    public abstract List<String> obtenerNombresCanciones();
    public abstract List<String> obtenerNombresCancionesFavoritas(String clienteNick);
    public abstract List<Integer> obtenerIdsCancionesFavoritas(String clienteNick);
    public abstract Object[] obtenerDatosCancion(int id);
    public abstract String obtenerFoto(int id);
    public abstract int obtenerIdAlbum(int id);
    public Object[][] obtenerDatosCancionSwing(int id);
}
