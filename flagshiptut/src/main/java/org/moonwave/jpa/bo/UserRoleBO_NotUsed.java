package org.moonwave.jpa.bo;

import java.util.List;

import javax.persistence.Query;

import org.moonwave.jpa.model.UserRole;

/**
 * UserRoleBO
 *
 * @author moonwave
 * TODO - remove
 *
 */
public class UserRoleBO_NotUsed extends BaseBO {

    private static final long serialVersionUID = 1L;

    @SuppressWarnings("unchecked")
    public List<UserRole> getAllRoles() {
        Query query = super.getEntityManager().createNamedQuery("UserRole.findAll", UserRole.class);
        List<UserRole> list = query.getResultList();
        super.release();
        return list;
    }

    @SuppressWarnings("unchecked")
    public List<UserRole> findByRole(Integer roleId) {
        Query query = super.getEntityManager().createNamedQuery("UserRole.findByRole", UserRole.class);
        query.setParameter("roleId", roleId);
        List<UserRole> list = query.getResultList();
        super.release();
        return list;
    }

    public UserRole findByRoleUser(Short roleId, Integer userId) {
        Query query = super.getEntityManager().createNamedQuery("UserRole.findByRoleUser", UserRole.class);
        query.setParameter("roleId", roleId);
        query.setParameter("userId", userId);
        UserRole userrole = null;
        try {
            userrole = (UserRole) query.getSingleResult();
        } catch (javax.persistence.NoResultException e) {
        }
        super.release();
        return userrole;
    }
}
