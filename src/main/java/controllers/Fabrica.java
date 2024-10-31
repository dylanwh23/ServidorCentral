/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import persistence.AlbumJpaController;
import persistence.ArtistaJpaController;
import persistence.CancionJpaController;
import persistence.ClienteJpaController;
import persistence.GeneroJpaController;
import persistence.PlaylistJpaController;
import persistence.UsuarioJpaController;


/**
 *
 * @author dylan
 */
public class Fabrica {

    private static Fabrica instancia;
    private final EntityManagerFactory emf;
    private Fabrica() {
        this.emf = Persistence.createEntityManagerFactory("grupo6_Spotify");
    };
    
    public static String safeToString(Object obj) {
        return obj != null ? obj.toString() : "null";
    }

    public static Fabrica getInstance() {
        if (instancia == null) {
            instancia = new Fabrica();
        }
        return instancia;
    }

    public IUsuarioController getIUsuarioController() {
        return new UsuarioController();
    }
    
    public IGeneroController getIGeneroController(){
        return new GeneroController();
    }
    
    public ICancionController getICancionController(){
        return new CancionController();
    }
     
    public IAlbumController getIAlbumController(){
        return new AlbumController();
    }
    
    public IPlaylistController getIPlaylistController(){
        return new PlaylistController();
    }

    CancionJpaController getCancionJpaController() {
        return new CancionJpaController(emf);
    }
    ArtistaJpaController getArtistaJpaController() {
        return new ArtistaJpaController(emf);
    }

    AlbumJpaController getAlbumJpaController() {
        return new AlbumJpaController(emf);
    }

    UsuarioJpaController getUsuarioJpaController() {
        return new UsuarioJpaController(emf);
    }

    GeneroJpaController getGeneroJpaController() {
        return new GeneroJpaController(emf);
    }

    PlaylistJpaController getPlaylistJpaController() {
        return new PlaylistJpaController(emf);
    }

    ClienteJpaController getClienteJpaController() {
        return new ClienteJpaController(emf);
    }

    EntityManagerFactory getEntityManagerFactory() {
        return  this.emf;
    }
    
    
}
