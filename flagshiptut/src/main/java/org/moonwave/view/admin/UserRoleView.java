package org.moonwave.view.admin;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;

import org.moonwave.jpa.bo.RoleBO;
import org.moonwave.jpa.bo.UserBO;
import org.moonwave.jpa.model.Role;
import org.moonwave.jpa.model.User;
import org.moonwave.view.BaseView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * UserroleView
 *
 * @author moonwave
 *
 */
@ManagedBean
@ViewScoped
public class UserRoleView extends BaseView {

    private static final long serialVersionUID = 1L;
    static final Logger LOG = LoggerFactory.getLogger(UserRoleView.class);

    private String selectedRoleId;
    private List<Role> roles;

    private String selectedUserId;
    private List<User> users;
    private List<User> usersInRole;

    @PostConstruct
    public void init() {
        // get a list of roles
        roles = new RoleBO().findAllRoles();
        // get a list of all users
        users = new UserBO().findActiveUsers();
        // get a list of users for the first role
        if (!roles.isEmpty()) {
            selectedRoleId = String.valueOf(roles.get(0).getId());
            getUsersByRoleId(selectedRoleId);
        }
    }

    public List<User> getUsers() {
        return users;
    }

    public String getSelectedUserId() {
        return selectedUserId;
    }

    public void setSelectedUserId(String selectedUserId) {
        this.selectedUserId = selectedUserId;
    }

    public String getSelectedRoleId() {
        return selectedRoleId;
    }

    public void setSelectedRoleId(String selectedRoleId) {
        this.selectedRoleId = selectedRoleId;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public List<User> getUsersInRole() {
        return usersInRole;
    }

    /**
     * Handle role change event
     * Search userrole table by using role
     *
     * @param event
     */
    public void changeRole(AjaxBehaviorEvent event) {
        getUsersByRoleId(selectedRoleId);
    }

    /**
     * Handle removing user from a selected role
     * or
     * public String removeUserFromRole()
     * @return
     */
    public void removeUserFromRole() {
        try {
            Role role = new RoleBO().findById(Short.parseShort(selectedRoleId));
            User user = new UserBO().findById(Integer.parseInt(selectedUserId));
            List<User> users = (role.getUsers() != null) ? role.getUsers() : new ArrayList<User>();
            for (User u : users) {
                if (u.getId() == user.getId()) {
                    // work on master object in a many-to-many relationship
                    List<Role> roles = (user.getRoles() != null) ? user.getRoles() : new ArrayList<Role>();
                    roles.remove(role);
                    user.setRoles(roles);
                    super.getBasebo().update(user);
                    break;
                }
            }

            getUsersByRoleId(selectedRoleId);
        } catch (Exception e) {
            super.error("Sorry, an error occurred, please contact your administrator");
            LOG.error("", e);
        }
    }

    /**
     * Handle adding user to a selected role
     * or
     * public void addUserToRole()
     * @return
     */
    public String addUserToRole() {
        try {
            Role role = new RoleBO().findById(Short.parseShort(selectedRoleId));
            List<User> users = (role.getUsers() != null) ? role.getUsers() : new ArrayList<User>();
            for (User user : users) {
                if (user.getId() == Integer.parseInt(selectedUserId)) {
                  super.info("User is already in selected role");
                  return null;
                }
            }
            // work on master object in a many-to-many relationship
            User user = new UserBO().findById(Integer.parseInt(selectedUserId));
            List<Role> roles = (user.getRoles() != null) ? user.getRoles() : new ArrayList<Role>();  
            roles.add(role);
            user.setRoles(roles);
            super.getBasebo().update(user);

            getUsersByRoleId(selectedRoleId);
        } catch (Exception e) {
            super.error("Sorry, an error occurred, please contact your administrator");
            LOG.error("", e);
        }
        return "";
    }

    // --------------------------------------------------------- Private Methods

    /**
     * Find a list of users by selected role id
     *
     * @param selectedRoleId
     */
    private void getUsersByRoleId(String selectedRoleId) {
        try {
            Role role = new RoleBO().findById(Short.parseShort(selectedRoleId));
            usersInRole = role.getUsers();
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
    }
}