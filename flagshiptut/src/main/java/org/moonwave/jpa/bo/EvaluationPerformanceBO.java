package org.moonwave.jpa.bo;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.moonwave.jpa.model.EvaluationPerformance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * EvaluationPerformanceBO
 *
 * @author moonwave
 *
 */
public class EvaluationPerformanceBO extends BaseBO {

    private static final long serialVersionUID = 1L;
    static final Logger LOG = LoggerFactory.getLogger(EvaluationPerformanceBO.class);

    @SuppressWarnings("unchecked")
    public List<EvaluationPerformance> getAllRoles() {
        Query query = super.getEntityManager().createNamedQuery("EvaluationPerformance.findAll", EvaluationPerformance.class);
        List<EvaluationPerformance> list = query.getResultList();
        super.release();
        return list;
    }

    public EvaluationPerformance findById(Integer id) {
        Query query = super.getEntityManager().createNamedQuery("User.findById", EvaluationPerformance.class);
        query.setParameter("id", id);
        EvaluationPerformance ret = null;
        try {
            ret = (EvaluationPerformance) query.getSingleResult();
        } catch (NoResultException e) {

        } catch (Exception e) {
            LOG.error("", e);
        } finally {
            super.release();
        }
        return ret;
    }

    @SuppressWarnings("unchecked")
    public List<EvaluationPerformance> findByUserId(Integer userId) {
        Query query = super.getEntityManager().createNamedQuery("EvaluationPerformance.findByUserId", EvaluationPerformance.class);
        query.setParameter("userId", userId);
        List<EvaluationPerformance> list = query.getResultList();
        super.release();
        return list;
    }

    @SuppressWarnings("unchecked")
    public List<EvaluationPerformance> findByTutorId(Integer tutorId) {
        Query query = super.getEntityManager().createNamedQuery("EvaluationPerformance.findByTutorId", EvaluationPerformance.class);
        query.setParameter("tutorId", tutorId);
        List<EvaluationPerformance> list = query.getResultList();
        super.release();
        return list;
    }
}
