package org.moonwave.jpa.bo;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.moonwave.jpa.model.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * RoleBO
 *
 * @author moonwave
 *
 */
public class RoleBO extends BaseBO {

    private static final long serialVersionUID = 1L;
    static final Logger LOG = LoggerFactory.getLogger(RoleBO.class);

    public List<Role> findAllRoles() {
        Query query = super.getEntityManager().createNamedQuery("Role.findAll", Role.class);
        @SuppressWarnings("unchecked")
        List<Role> list = query.getResultList();
        super.release();
        return list;
    }

    public Role findById(Short id) {
        Query query = super.getEntityManager().createNamedQuery("Role.findById", Role.class);
        query.setParameter("id", id);
        Role ret = null;
        try {
            ret = (Role) query.getSingleResult();
        } catch (NoResultException e) {

        } catch (Exception e) {
            LOG.error("", e);
        } finally {
            super.release();
        }
        return ret;
    }
}
