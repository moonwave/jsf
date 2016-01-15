package org.moonwave.jpa.bo;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * BaseBO
 *
 * @author moonwave
 *
 */
public class BaseBO implements Serializable {

    private static final long serialVersionUID = 1L;

    transient EntityManager em;

    public EntityManager getEntityManager() {
        if (em == null) {
            EntityManagerFactory emfactory = Persistence.createEntityManagerFactory( "jpa-mysql" );
            em = emfactory.createEntityManager();
        }
        return em;
    }

    public void release() {
        if (em != null) {
            if (em.isOpen()) {
                em.clear();
                em.close();
            }
            em = null;
        }
    }

    /**
     * Make an instance managed and persistent.
     * @param entity  entity instance
     * @throws EntityExistsException if the entity already exists.
     * (If the entity already exists, the <code>EntityExistsException</code> may 
     * be thrown when the persist operation is invoked, or the
     * <code>EntityExistsException</code> or another <code>PersistenceException</code> may be 
     * thrown at flush or commit time.) 
     * @throws IllegalArgumentException if the instance is not an
     *         entity
     * @throws TransactionRequiredException if there is no transaction when
     *         invoked on a container-managed entity manager of that is of type 
     *         <code>PersistenceContextType.TRANSACTION</code>
     */
    public void persist(Object entity) {
        em = getEntityManager();
        em.getTransaction().begin();
        em.persist(entity);
        em.flush();
        em.getTransaction().commit();
    }

    public void persist(List<?> entities) {
        em = getEntityManager();
        em.getTransaction().begin();
        for (Object entity : entities) {
            em.persist(entity);
            em.flush();
        }
        em.getTransaction().commit();
    }

    public void update(Object entity) {
        em = getEntityManager();
        em.getTransaction().begin();
        Object entityToPersist = merge(entity);
        em.persist(entityToPersist);
        em.getTransaction().commit();
    }

    public void update(List<?> entities) {
        em = getEntityManager();
        em.getTransaction().begin();
        for (Object entity : entities) {
            Object entityToPersist = merge(entity);
            em.persist(entityToPersist);
        }
        em.getTransaction().commit();
    }

    /**
     * Merge the state of the given entity into the
     * current persistence context.
     * @param entity  entity instance
     * @return the managed instance that the state was merged to
     * @throws IllegalArgumentException if instance is not an
     *         entity or is a removed entity
     * @throws TransactionRequiredException if there is no transaction when
     *         invoked on a container-managed entity manager of that is of type 
     *         <code>PersistenceContextType.TRANSACTION</code>
     */
    public <T> T merge(T entity) {
        em = getEntityManager();
        entity = em.merge(entity);
        return entity;
    }

    /**
     * Remove the entity instance.
     * @param entity  entity instance
     * @throws IllegalArgumentException if the instance is not an
     *         entity or is a detached entity
     * @throws TransactionRequiredException if invoked on a
     *         container-managed entity manager of type 
     *         <code>PersistenceContextType.TRANSACTION</code> and there is 
     *         no transaction
     */    
    public void remove(Object entity) {
        em = getEntityManager();
        em.getTransaction().begin();
        Object entityToRemove = merge(entity);
        em.remove(entityToRemove);
        em.getTransaction().commit();
    }

    public void remove(List<?> entities) {
        em = getEntityManager();
        em.getTransaction().begin();
        for (Object entity : entities) {
            Object entityToRemove = merge(entity);
            em.remove(entityToRemove);
        }
        em.getTransaction().commit();
    }
}
