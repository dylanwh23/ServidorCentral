/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.OneToMany;

import java.util.LinkedList;
import javax.persistence.DiscriminatorValue;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

/**
 *
 * @author dylan
 */
@Entity
@Table(name="Cliente")
@DiscriminatorValue("cliente")




public class Cliente extends Usuario {

    @Column(name = "fecSub")
    private LocalDate fecSub;
    @Column(name = "estado")
    private String estado = "Cancelado";
    @Column(name = "tipo")
    private int tipo;
    
    @ManyToMany
    @JoinTable(
            name = "cliente_playlistFavoritas",
            joinColumns = @JoinColumn(name = "cliente_id"), 
            inverseJoinColumns = @JoinColumn(name = "playlist_particular_id") 
    )
    private List<Playlist> PlaylistFavoritos;
    
    @ManyToMany
    @JoinTable(
        name = "cliente_cancionesFavoritas", 
        joinColumns = @JoinColumn(name = "cliente_id"), 
        inverseJoinColumns = @JoinColumn(name = "cancion_id") 
    )
    
    private List<Cancion> cancionesFavoritas;
    
        @ManyToMany
    @JoinTable(
        name = "cliente_albumesFavoritos", 
        joinColumns = @JoinColumn(name = "cliente_id"), 
        inverseJoinColumns = @JoinColumn(name = "album_id") 
    ) 
    private List<Album> albumesFavoritos;

    @ManyToMany
    @JoinTable(
        name = "cliente_usuariosSeguidos", 
        joinColumns = @JoinColumn(name = "cliente_id"), 
        inverseJoinColumns = @JoinColumn(name = "usuario_id") 
    )     
    private List<Usuario> usuariosSeguidos;      
        
    public List<Playlist> getPlaylistFavoritos() {
        return PlaylistFavoritos;
    }

    public List<Cancion> getCancionesFavoritas() {
        return cancionesFavoritas;
    }

    public List<Album> getAlbumesFavoritos() {
        return albumesFavoritos;
    }

    public void setPlaylistFavoritos(List<Playlist> PlaylistFavoritos) {
        this.PlaylistFavoritos = PlaylistFavoritos;
    }

    public void setCancionesFavoritas(List<Cancion> cancionesFavoritas) {
        this.cancionesFavoritas = cancionesFavoritas;
    }

    public void setAlbumesFavoritos(List<Album> albumesFavoritos) {
        this.albumesFavoritos = albumesFavoritos;
    }

    public void setUsuariosSeguidos(List<Usuario> usuariosSeguidos) {
        this.usuariosSeguidos = usuariosSeguidos;
    }
    
    public LocalDate getFecSub() {
        return fecSub;
    }

    public void setFecSub(LocalDate fecCont) {
        this.fecSub = fecSub;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }
    
    
    

    public Cliente() {
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public LocalDate getFecNac() {
        return fecNac;
    }

    public void setFecNac(LocalDate fecNac) {
        this.fecNac = fecNac;
    }
    
    public Cliente(String nick, String nombre, String apellido, String mail, LocalDate FecNac, String imagen, String contraseña){

        this.nick = nick;
        this.nombre = nombre;
        this.apellido = apellido;
        this.contraseñaHash = contraseña;
        this.mail = mail;
        this.fecNac = FecNac;
        this.imagen = imagen;
    }
    
    
    
}

 
