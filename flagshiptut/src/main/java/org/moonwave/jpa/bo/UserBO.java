package org.moonwave.jpa.bo;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.moonwave.jpa.model.Role;
import org.moonwave.jpa.model.User;
import org.moonwave.util.StackTrace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * UserBO
 *
 * @author moonwave
 *
 */
public class UserBO extends BaseBO {

    private static final long serialVersionUID = 1L;
    static final Logger LOG = LoggerFactory.getLogger(UserBO.class);

    @SuppressWarnings("unchecked")
    public List<User> findAllUsers() {
        Query query = super.getEntityManager().createNamedQuery("User.findAll", User.class);
        List<User> list = query.getResultList();
        super.release();
        return list;
    }

    @SuppressWarnings("unchecked")
    public List<User> findActiveUsers() {
        Query query = super.getEntityManager().createNamedQuery("User.findActiveUsers", User.class);
        List<User> list = query.getResultList();
        super.release();
        return list;
    }

    @SuppressWarnings("unchecked")
    public List<User> findActiveGenericUsers() {
        Query query = super.getEntityManager().createNamedQuery("User.findActiveGenericUsers", User.class);
        List<User> list = query.getResultList();
        super.release();
        return list;
    }

    public User findById(Integer id) {
        Query query = super.getEntityManager().createNamedQuery("User.findById", User.class);
        query.setParameter("id", id);
        User ret = null;
        try {
            ret = (User) query.getSingleResult();
        } catch (NoResultException e) {

        } catch (Exception e) {
            LOG.error(StackTrace.toString(e));
        } finally {
            super.release();
        }
        return ret;
    }

    public User findByLoginId(String loginId) {
        Query query = super.getEntityManager().createNamedQuery("User.findByLoginId", User.class);
        query.setParameter("loginId", loginId);
        User ret = null;
        try {
            ret = (User) query.getSingleResult();
        } catch (NoResultException e) {

        } catch (Exception e) {
            LOG.error(StackTrace.toString(e));
        } finally {
            super.release();
        }
        return ret;
    }

    public User findByEmail(String email) {
        Query query = super.getEntityManager().createNamedQuery("User.findByEmail", User.class);
        query.setParameter("email", email);
        User ret = null;
        try {
            ret = (User) query.getSingleResult();
        } catch (NoResultException e) {
            LOG.error(StackTrace.toString(e));
        } catch (Exception e) {
            LOG.error(StackTrace.toString(e));
        } finally {
            super.release();
        }
        return ret;
    }
    public User findByActionCode(String actionCode) {
        Query query = super.getEntityManager().createNamedQuery("User.findByActionCode", User.class);
        query.setParameter("actionCode", actionCode);
        User ret = null;
        try {
            ret = (User) query.getSingleResult();
        } catch (NoResultException e) {

        } catch (Exception e) {
            LOG.error(StackTrace.toString(e));
        } finally {
            super.release();
        }
        return ret;
    }

    /**
     * The method is not being used
     * TODO - remove
     * @param userIds
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<User> findInIds(List<Integer> userIds) {
        String q1 = "SELECT u FROM User u WHERE u.id in (";
        String q2 = ")";
        StringBuilder sb = new StringBuilder();
        sb.append(q1);
        for (int i = 0; i < userIds.size(); i++) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append(userIds.get(i));
        }
        sb.append(q2);
        Query query = super.getEntityManager().createQuery(sb.toString(), User.class);
        List<User> list = query.getResultList();
        super.release();
        return list;
    }

    @SuppressWarnings("unchecked")
    public List<User> findAllStudents() {
        Query query = super.getEntityManager().createNamedQuery("Role.findStudents", Role.class);
        List<Role> roles = query.getResultList();
        List<User> list = roles.get(0).getUsers();
        super.release();
        return list;
    }

    @SuppressWarnings("unchecked")
    public List<User> findAllTutors() {
        Query query = super.getEntityManager().createNamedQuery("Role.findTutors", Role.class);
        List<Role> roles = query.getResultList();
        List<User> list = roles.get(0).getUsers();
        super.release();
        return list;
    }

}
