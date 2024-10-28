/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistence;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import models.Genero;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import persistence.exceptions.NonexistentEntityException;
import persistence.exceptions.PreexistingEntityException;

/**
 *
 * @author dylan
 */
public class GeneroJpaController implements Serializable {

    public GeneroJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Genero genero) throws PreexistingEntityException, Exception {
        if (genero.getGeneroCollection() == null) {
            genero.setGeneroCollection(new ArrayList<Genero>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Genero padreNombre = genero.getPadreNombre();
            if (padreNombre != null) {
                padreNombre = em.getReference(padreNombre.getClass(), padreNombre.getNombre());
                genero.setPadreNombre(padreNombre);
            }
            Collection<Genero> attachedGeneroCollection = new ArrayList<Genero>();
            for (Genero generoCollectionGeneroToAttach : genero.getGeneroCollection()) {
                generoCollectionGeneroToAttach = em.getReference(generoCollectionGeneroToAttach.getClass(), generoCollectionGeneroToAttach.getNombre());
                attachedGeneroCollection.add(generoCollectionGeneroToAttach);
            }
            genero.setGeneroCollection(attachedGeneroCollection);
            em.persist(genero);
            if (padreNombre != null) {
                padreNombre.getGeneroCollection().add(genero);
                padreNombre = em.merge(padreNombre);
            }
            for (Genero generoCollectionGenero : genero.getGeneroCollection()) {
                Genero oldPadreNombreOfGeneroCollectionGenero = generoCollectionGenero.getPadreNombre();
                generoCollectionGenero.setPadreNombre(genero);
                generoCollectionGenero = em.merge(generoCollectionGenero);
                if (oldPadreNombreOfGeneroCollectionGenero != null) {
                    oldPadreNombreOfGeneroCollectionGenero.getGeneroCollection().remove(generoCollectionGenero);
                    oldPadreNombreOfGeneroCollectionGenero = em.merge(oldPadreNombreOfGeneroCollectionGenero);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findGenero(genero.getNombre()) != null) {
                throw new PreexistingEntityException("Genero " + genero + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Genero genero) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Genero persistentGenero = em.find(Genero.class, genero.getNombre());
            Genero padreNombreOld = persistentGenero.getPadreNombre();
            Genero padreNombreNew = genero.getPadreNombre();
            Collection<Genero> generoCollectionOld = persistentGenero.getGeneroCollection();
            Collection<Genero> generoCollectionNew = genero.getGeneroCollection();
            if (padreNombreNew != null) {
                padreNombreNew = em.getReference(padreNombreNew.getClass(), padreNombreNew.getNombre());
                genero.setPadreNombre(padreNombreNew);
            }
            Collection<Genero> attachedGeneroCollectionNew = new ArrayList<Genero>();
            for (Genero generoCollectionNewGeneroToAttach : generoCollectionNew) {
                generoCollectionNewGeneroToAttach = em.getReference(generoCollectionNewGeneroToAttach.getClass(), generoCollectionNewGeneroToAttach.getNombre());
                attachedGeneroCollectionNew.add(generoCollectionNewGeneroToAttach);
            }
            generoCollectionNew = attachedGeneroCollectionNew;
            genero.setGeneroCollection(generoCollectionNew);
            genero = em.merge(genero);
            if (padreNombreOld != null && !padreNombreOld.equals(padreNombreNew)) {
                padreNombreOld.getGeneroCollection().remove(genero);
                padreNombreOld = em.merge(padreNombreOld);
            }
            if (padreNombreNew != null && !padreNombreNew.equals(padreNombreOld)) {
                padreNombreNew.getGeneroCollection().add(genero);
                padreNombreNew = em.merge(padreNombreNew);
            }
            for (Genero generoCollectionOldGenero : generoCollectionOld) {
                if (!generoCollectionNew.contains(generoCollectionOldGenero)) {
                    generoCollectionOldGenero.setPadreNombre(null);
                    generoCollectionOldGenero = em.merge(generoCollectionOldGenero);
                }
            }
            for (Genero generoCollectionNewGenero : generoCollectionNew) {
                if (!generoCollectionOld.contains(generoCollectionNewGenero)) {
                    Genero oldPadreNombreOfGeneroCollectionNewGenero = generoCollectionNewGenero.getPadreNombre();
                    generoCollectionNewGenero.setPadreNombre(genero);
                    generoCollectionNewGenero = em.merge(generoCollectionNewGenero);
                    if (oldPadreNombreOfGeneroCollectionNewGenero != null && !oldPadreNombreOfGeneroCollectionNewGenero.equals(genero)) {
                        oldPadreNombreOfGeneroCollectionNewGenero.getGeneroCollection().remove(generoCollectionNewGenero);
                        oldPadreNombreOfGeneroCollectionNewGenero = em.merge(oldPadreNombreOfGeneroCollectionNewGenero);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = genero.getNombre();
                if (findGenero(id) == null) {
                    throw new NonexistentEntityException("The genero with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Genero genero;
            try {
                genero = em.getReference(Genero.class, id);
                genero.getNombre();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The genero with id " + id + " no longer exists.", enfe);
            }
            Genero padreNombre = genero.getPadreNombre();
            if (padreNombre != null) {
                padreNombre.getGeneroCollection().remove(genero);
                padreNombre = em.merge(padreNombre);
            }
            Collection<Genero> generoCollection = genero.getGeneroCollection();
            for (Genero generoCollectionGenero : generoCollection) {
                generoCollectionGenero.setPadreNombre(null);
                generoCollectionGenero = em.merge(generoCollectionGenero);
            }
            em.remove(genero);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Genero> findGeneroEntities() {
        return findGeneroEntities(true, -1, -1);
    }

    public List<Genero> findGeneroEntities(int maxResults, int firstResult) {
        return findGeneroEntities(false, maxResults, firstResult);
    }

    private List<Genero> findGeneroEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Genero.class));
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

    public Genero findGenero(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Genero.class, id);
        } finally {
            em.close();
        }
    }

    public int getGeneroCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Genero> rt = cq.from(Genero.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
