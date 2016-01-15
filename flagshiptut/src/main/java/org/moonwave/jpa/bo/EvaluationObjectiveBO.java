package org.moonwave.jpa.bo;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.moonwave.jpa.model.EvaluationObjective;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * EvaluationObjectiveBO
 *
 * @author moonwave
 *
 */
public class EvaluationObjectiveBO extends BaseBO {

    private static final long serialVersionUID = 1L;
    static final Logger LOG = LoggerFactory.getLogger(EvaluationObjectiveBO.class);

    @SuppressWarnings("unchecked")
    public List<EvaluationObjective> getAllRoles() {
        Query query = super.getEntityManager().createNamedQuery("EvaluationObjective.findAll", EvaluationObjective.class);
        List<EvaluationObjective> list = query.getResultList();
        super.release();
        return list;
    }

    public EvaluationObjective findById(Integer id) {
        Query query = super.getEntityManager().createNamedQuery("User.findById", EvaluationObjective.class);
        query.setParameter("id", id);
        EvaluationObjective ret = null;
        try {
            ret = (EvaluationObjective) query.getSingleResult();
        } catch (NoResultException e) {

        } catch (Exception e) {
            LOG.error("", e);
        } finally {
            super.release();
        }
        return ret;
    }

    @SuppressWarnings("unchecked")
    public List<EvaluationObjective> findByUserId(Integer userId) {
        Query query = super.getEntityManager().createNamedQuery("EvaluationObjective.findByUserId", EvaluationObjective.class);
        query.setParameter("userId", userId);
        List<EvaluationObjective> list = query.getResultList();
        super.release();
        return list;
    }

    @SuppressWarnings("unchecked")
    public List<EvaluationObjective> findByTutorId(Integer tutorId) {
        Query query = super.getEntityManager().createNamedQuery("EvaluationObjective.findByTutorId", EvaluationObjective.class);
        query.setParameter("tutorId", tutorId);
        List<EvaluationObjective> list = query.getResultList();
        super.release();
        return list;
    }

    @SuppressWarnings("unchecked")
    public List<EvaluationObjective> findByParentEvaluationId(Integer parentEvaluationId) {
        Query query = super.getEntityManager().createNamedQuery("EvaluationObjective.findByParentEvaluationId", EvaluationObjective.class);
        query.setParameter("parentEvaluationId", parentEvaluationId);
        List<EvaluationObjective> list = query.getResultList();
        super.release();
        return list;
    }
}
