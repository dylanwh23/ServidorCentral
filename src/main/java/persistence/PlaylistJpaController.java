/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistence;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import models.Playlist;
import persistence.exceptions.NonexistentEntityException;

/**
 *
 * @author dylan
 */
public class PlaylistJpaController implements Serializable {

    public PlaylistJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Playlist playlist) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(playlist);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Playlist playlist) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            playlist = em.merge(playlist);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = playlist.getId();
                if (findPlaylist(id) == null) {
                    throw new NonexistentEntityException("The playlist with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(int id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Playlist playlist;
            try {
                playlist = em.getReference(Playlist.class, id);
                playlist.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The playlist with id " + id + " no longer exists.", enfe);
            }
            em.remove(playlist);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Playlist> findPlaylistEntities() {
        return findPlaylistEntities(true, -1, -1);
    }

    public List<Playlist> findPlaylistEntities(int maxResults, int firstResult) {
        return findPlaylistEntities(false, maxResults, firstResult);
    }

    private List<Playlist> findPlaylistEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Playlist.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Playlist findPlaylist(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Playlist.class, id);
        } finally {
            em.close();
        }
    }

    public int getPlaylistCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Playlist> rt = cq.from(Playlist.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
