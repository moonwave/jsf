package org.moonwave.jpa.bo;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.moonwave.jpa.model.Schedule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ScheduleBO
 *
 * @author moonwave
 *
 */
public class ScheduleBO extends BaseBO {

    private static final long serialVersionUID = 1L;
    static final Logger LOG = LoggerFactory.getLogger(ScheduleBO.class);

    @SuppressWarnings("unchecked")
    public List<Schedule> getAllRoles() {
        Query query = super.getEntityManager().createNamedQuery("Schedule.findAll", Schedule.class);
        List<Schedule> list = query.getResultList();
        super.release();
        return list;
    }

    public Schedule findById(Integer id) {
        Query query = super.getEntityManager().createNamedQuery("User.findById", Schedule.class);
        query.setParameter("id", id);
        Schedule ret = null;
        try {
            ret = (Schedule) query.getSingleResult();
        } catch (NoResultException e) {

        } catch (Exception e) {
            LOG.error("", e);
        } finally {
            super.release();
        }
        return ret;
    }

    @SuppressWarnings("unchecked")
    public List<Schedule> findByUserId(Integer userId) {
        Query query = super.getEntityManager().createNamedQuery("Schedule.findByUserId", Schedule.class);
        query.setParameter("userId", userId);
        List<Schedule> list = query.getResultList();
        super.release();
        return list;
    }

    @SuppressWarnings("unchecked")
    public List<Schedule>  findByTutorId(Integer tutorId) {
        Query query = super.getEntityManager().createNamedQuery("Schedule.findByTutorId", Schedule.class);
        query.setParameter("tutorId", tutorId);
        List<Schedule> list = query.getResultList();
        super.release();
        return list;
    }
}
