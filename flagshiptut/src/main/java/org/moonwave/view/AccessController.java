package org.moonwave.view;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.moonwave.jpa.model.Role;
import org.moonwave.jpa.model.User;

@ManagedBean
@SessionScoped
public class AccessController implements Serializable {

    private static final long serialVersionUID = 1L;

    static final String student    = "student";
    static final String tutor      = "tutor";
    static final String teacher    = "teacher";
    static final String supervisor = "supervisor";
    static final String admin      = "admin";

    @ManagedProperty("#{loginController}")
    private LoginController loginController;

    public LoginController getLoginController() {
        return loginController;
    }

    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }

    public boolean isStudent() {
        return isInRole(student);
    }
 
    public boolean isTutor() {
        return isInRole(tutor);
    }

    public boolean isTeacher() {
        return isInRole(teacher);
    }

    public boolean isSupervisor() {
        return isInRole(supervisor);
    }

    public boolean isAdmin() {
        return isInRole(admin);
    }

    /**
     * Editor can add contents like announcement, group post, assignment, etc. 
     * @return
     */
    public boolean isEditor() {
        return isTutor() || isTeacher() || isSupervisor() || isAdmin();
    }

    /**
     * Return true if passed in user is the logged in user
     *
     * @param user
     * @return
     */
    public boolean isSelf(User user) {
        User loggedInUser = loginController.getLoggedInUser();
        return (loggedInUser.equals(user));
    }

    public void redirectTo401() throws IOException {
        FacesContext fContext = FacesContext.getCurrentInstance();
        ExternalContext extContext = fContext.getExternalContext();
        extContext.redirect(extContext.getRequestContextPath() + "/error/401.xhtml");
    }

    // ========================================================= Private methods

    private boolean isInRole(String roleAlias) {
        boolean ret = false;
        User user = loginController.getLoggedInUser();
        if ((user != null) && (user.getRoles() != null)) {
            List<Role> roles = user.getRoles();
            for (Role role : roles) {
                if (role.getAlias().equals(roleAlias)) {
                    ret = true;
                    break;
                }
            }
        }
        return ret;
    }
}