package models;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import java.util.LinkedList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import models.Cancion;
import models.Genero;
import models.Playlist;
import javax.persistence.Table;
import javax.persistence.Column;

@Entity
@Table(name="PlaylistPorDefecto")
public class PlaylistPorDefecto extends Playlist {
    
    @ManyToOne
    @JoinColumn(name = "genero_nombre")
    private Genero genero;

    public PlaylistPorDefecto() {
    }

    public PlaylistPorDefecto(Genero genero, String Nombre, String rutaImagen, LinkedList<Cancion> canciones) {
        super(Nombre, rutaImagen, canciones);
        this.genero = genero;
    }

    public Genero getGenero() {
        return genero;
    }

    public void setGenero(Genero genero) {
        this.genero = genero;
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
