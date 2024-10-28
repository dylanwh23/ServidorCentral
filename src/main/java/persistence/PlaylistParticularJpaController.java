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
import models.PlaylistParticular;
import persistence.exceptions.NonexistentEntityException;

/**
 *
 * @author dylan
 */
public class PlaylistParticularJpaController implements Serializable {

    public PlaylistParticularJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PlaylistParticular playlistParticular) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(playlistParticular);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PlaylistParticular playlistParticular) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            playlistParticular = em.merge(playlistParticular);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = playlistParticular.getId();
                if (findPlaylistParticular(id) == null) {
                    throw new NonexistentEntityException("The playlistParticular with id " + id + " no longer exists.");
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
            PlaylistParticular playlistParticular;
            try {
                playlistParticular = em.getReference(PlaylistParticular.class, id);
                playlistParticular.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The playlistParticular with id " + id + " no longer exists.", enfe);
            }
            em.remove(playlistParticular);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<PlaylistParticular> findPlaylistParticularEntities() {
        return findPlaylistParticularEntities(true, -1, -1);
    }

    public List<PlaylistParticular> findPlaylistParticularEntities(int maxResults, int firstResult) {
        return findPlaylistParticularEntities(false, maxResults, firstResult);
    }

    private List<PlaylistParticular> findPlaylistParticularEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PlaylistParticular.class));
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

    public PlaylistParticular findPlaylistParticular(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PlaylistParticular.class, id);
        } finally {
            em.close();
        }
    }

    public int getPlaylistParticularCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PlaylistParticular> rt = cq.from(PlaylistParticular.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
