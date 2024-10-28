/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package controllers;

import java.util.List;
import models.Genero;

/**
 *
 * @author Machichu
 */
public interface IGeneroController {
    
    public abstract boolean registrarGenero(String nombre, Genero padre);
    
    public abstract boolean registrarGenero2(String nombre);
    public abstract List<String> obtenerNombresGeneros();
    public abstract Genero findGenero(String nombre);
    
   
}
