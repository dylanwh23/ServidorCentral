/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;


/**
 *
 * @author dylan
 */
@Entity
@Table(name="Artista")
@DiscriminatorValue("artista")

public class Artista extends Usuario {
	@Column(name="biografia",length = 1200)
    private String biografia;
	@Column(name="DireccionWeb")
    private String DireccionWeb;
    
    
    public Artista(String nick, String nombre, String apellido, String mail, LocalDate FecNac, String imagen, String biografia, String link, String contraseña){
        this.nick = nick;
        this.nombre = nombre;
        this.apellido = apellido;
        this.contraseñaHash = contraseña;
        this.mail = mail;
        this.fecNac = FecNac;
        this.biografia = biografia;
        this.DireccionWeb = link;
        this.imagen = imagen;
    }

    public Artista() {
       
    }

    public String getBiografia() {
        return biografia;
    }

    public void setBiografia(String biografia) {
        this.biografia = biografia;
    }

    public String getDireccionWeb() {
        return DireccionWeb;
    }

    public void setDireccionWeb(String DireccionWeb) {
        this.DireccionWeb = DireccionWeb;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getMail() {
        return mail;
    }

    public LocalDate getFecNac() {
       return fecNac;
    }

    public String getImagen() {
        return imagen;
    }
    
}

