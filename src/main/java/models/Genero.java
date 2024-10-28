/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author diego
 */
@Entity
@Table(name = "Genero")
@NamedQueries({
    @NamedQuery(name = "Genero.findAll", query = "SELECT g FROM Genero g"),
    @NamedQuery(name = "Genero.findByNombre", query = "SELECT g FROM Genero g WHERE g.nombre = :nombre")})
public class Genero implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    @OneToMany(mappedBy = "padreNombre")
    private Collection<Genero> generoCollection;
    @JoinColumn(name = "padre_nombre", referencedColumnName = "nombre")
    @ManyToOne
    private Genero padreNombre;

    public Genero() {
    }

    public Genero(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Collection<Genero> getGeneroCollection() {
        return generoCollection;
    }

    public void setGeneroCollection(Collection<Genero> generoCollection) {
        this.generoCollection = generoCollection;
    }

    public Genero getPadreNombre() {
        return padreNombre;
    }

    public void setPadreNombre(Genero padreNombre) {
        this.padreNombre = padreNombre;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nombre != null ? nombre.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Genero)) {
            return false;
        }
        Genero other = (Genero) object;
        if ((this.nombre == null && other.nombre != null) || (this.nombre != null && !this.nombre.equals(other.nombre))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "models.Genero[ nombre=" + nombre + " ]";
    }
    
}
