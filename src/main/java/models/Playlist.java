/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.io.Serializable;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.JoinTable;

/**
 *
 * @author dylan
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name="Playlist")
public class Playlist implements Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id")
	    protected int id;
    
	@Column(name="Nombre")
	    protected String Nombre;
	@Column(name="rutaImagen")
	    protected String rutaImagen;
    @OneToMany 
    @JoinTable(
        name = "playlist_cancion", 
        joinColumns = @JoinColumn(name = "playlist_id"), 
        inverseJoinColumns = @JoinColumn(name = "canciones_id") 
    )
    protected List<Cancion> canciones;

    public Playlist() {
    }

    public Playlist(String Nombre, String rutaImagen, List<Cancion> canciones) {
        this.Nombre = Nombre;
        this.rutaImagen = rutaImagen;
        this.canciones = canciones;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public String getRutaImagen() {
        return rutaImagen;
    }

    public void setRutaImagen(String rutaImagen) {
        this.rutaImagen = rutaImagen;
    }

    public List<Cancion> getCanciones() {
        return canciones;
    }

    public void setCanciones(List<Cancion> canciones) {
        this.canciones = canciones;
    }
    
}

