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
import models.PlaylistPorDefecto;
import persistence.exceptions.NonexistentEntityException;

/**
 *
 * @author dylan
 */
public class PlaylistPorDefectoJpaController implements Serializable {

    public PlaylistPorDefectoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PlaylistPorDefecto playlistPorDefecto) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(playlistPorDefecto);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PlaylistPorDefecto playlistPorDefecto) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            playlistPorDefecto = em.merge(playlistPorDefecto);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = playlistPorDefecto.getId();
                if (findPlaylistPorDefecto(id) == null) {
                    throw new NonexistentEntityException("The playlistPorDefecto with id " + id + " no longer exists.");
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
            PlaylistPorDefecto playlistPorDefecto;
            try {
                playlistPorDefecto = em.getReference(PlaylistPorDefecto.class, id);
                playlistPorDefecto.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The playlistPorDefecto with id " + id + " no longer exists.", enfe);
            }
            em.remove(playlistPorDefecto);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<PlaylistPorDefecto> findPlaylistPorDefectoEntities() {
        return findPlaylistPorDefectoEntities(true, -1, -1);
    }

    public List<PlaylistPorDefecto> findPlaylistPorDefectoEntities(int maxResults, int firstResult) {
        return findPlaylistPorDefectoEntities(false, maxResults, firstResult);
    }

    private List<PlaylistPorDefecto> findPlaylistPorDefectoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PlaylistPorDefecto.class));
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

    public PlaylistPorDefecto findPlaylistPorDefecto(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PlaylistPorDefecto.class, id);
        } finally {
            em.close();
        }
    }

    public int getPlaylistPorDefectoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PlaylistPorDefecto> rt = cq.from(PlaylistPorDefecto.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
