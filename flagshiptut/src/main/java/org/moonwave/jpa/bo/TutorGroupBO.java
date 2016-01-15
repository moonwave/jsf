package org.moonwave.jpa.bo;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.moonwave.jpa.model.TutorGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TutorGroupBO
 *
 * @author moonwave
 *
 */
public class TutorGroupBO extends BaseBO {

    private static final long serialVersionUID = 1L;
    static final Logger LOG = LoggerFactory.getLogger(TutorGroupBO.class);

    public List<TutorGroup> findAllGroups() {
        Query query = super.getEntityManager().createNamedQuery("TutorGroup.findAll", TutorGroup.class);
        @SuppressWarnings("unchecked")
        List<TutorGroup> list = query.getResultList();
        super.release();
        return list;
    }

    public TutorGroup findById(Short id) {
        Query query = super.getEntityManager().createNamedQuery("TutorGroup.findById", TutorGroup.class);
        query.setParameter("id", id);
        TutorGroup ret = null;
        try {
            ret = (TutorGroup) query.getSingleResult();
        } catch (NoResultException e) {

        } catch (Exception e) {
            LOG.error("", e);
        } finally {
            super.release();
        }
        return ret;
    }

//    public List<TutorGroup> findGroupsByUser() {
//        Query query = super.getEntityManager().createNamedQuery("TutorGroup.findGroupsByUser", TutorGroup.class);
//        query.setParameter("user", user);
//        @SuppressWarnings("unchecked")
//        List<TutorGroup> list = query.getResultList();
//        super.release();
//        return list;
//    }
}
