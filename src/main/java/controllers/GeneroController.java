/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import models.Genero;
import persistence.GeneroJpaController;
/**
 *
 * @author Machichu
 */
public class GeneroController implements IGeneroController {
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("grupo6_Spotify");
    GeneroJpaController aux = new GeneroJpaController(emf);
    
    public boolean registrarGenero(String nombre, Genero padre) {
        Genero gen = new Genero();
        gen.setNombre(nombre);
        gen.setPadreNombre(padre);
        try {
            aux.create(gen);
            return true;
        } catch (Exception ex) {
            
            return false;
        }
    }
    
        public boolean registrarGenero2(String nombre) {
        Genero gen = new Genero();
        gen.setNombre(nombre);
        try {
            aux.create(gen);
            return true;
        } catch (Exception ex) {
            
            return false;
        }
    }
    
    public List<String> obtenerNombresGeneros() {
            List<Genero> generos = aux.findGeneroEntities();
            return generos.stream()
                           .map(genero -> genero.getNombre())
                           .collect(Collectors.toList());
        
    }
       
   
    public Genero findGenero(String nombre){
        
        return aux.findGenero(nombre);
        
    }
        
        
 
        
        
    }
    
    
    
    

