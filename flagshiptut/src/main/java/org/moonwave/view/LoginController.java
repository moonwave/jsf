package org.moonwave.view;

import java.io.IOException;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpSession;

import org.moonwave.jpa.model.User;
import org.moonwave.util.SHAUtil;

@ManagedBean(name = "loginController")
@SessionScoped
public class LoginController extends BaseView {

    private static final long serialVersionUID = 1L;

    private String username;
    private String password;
 
    public String getUsername() {
        return username;
    }
 
    public void setUsername(String username) {
        this.username = username;
    }
 
    public String getPassword() {
        return password;
    }
 
    public void setPassword(String password) {
        this.password = password;
    }

    @SuppressWarnings("unchecked")
    public String login() {
        EntityManager em = super.getBasebo().getEntityManager();
        Query query = em.createQuery("Select u from User u where u.loginId = ?1 and u.password = ?2", User.class);
        query.setParameter(1, username);
        query.setParameter(2, SHAUtil.encryptPassword(password));
        List<User> list = query.getResultList();
        User user;
        if (list.isEmpty()) {
            super.error("Invalid credentials");
            return "";
        }
        // login successful
        user = list.get(0);
        User loggedInUser = new User();
        loggedInUser.setId(user.getId());
        loggedInUser.setFirstName(user.getFirstName());
        loggedInUser.setLastName(user.getLastName());
        loggedInUser.setLoginId(user.getLoginId());
        loggedInUser.setRoles(user.getRoles());
        loggedInUser.setTutorGroups(user.getTutorGroups());
        loggedInUser.setTimezone(user.getTimezone());
        loggedInUser.setEmail(user.getEmail());
        loggedInUser.setPhone(user.getPhone());
        loggedInUser.setTag(user.getTag());
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("loggedInUser", loggedInUser);
        return "/schedule/dashboard?faces-redirect=true";
    }

    /**
     * http://stackoverflow.com/questions/4032825/how-to-make-a-redirection-in-jsf
     * http://stackoverflow.com/questions/12815529/jsf-index-xhtml-and-redirect-to-faces-action
     * http://www.coderanch.com/t/541659/JSF/java/JSF-redirect-page
     * @throws IOException
     */
    public void redirect(ComponentSystemEvent e) throws IOException {
        if (this.isUserLoggedIn()) {
            super.redirectTo("/schedule/dashboard.xhtml");
        } else {
            super.redirectTo("/login.xhtml");
        }
    }

    public void redirectToLogin() {
        try {
            super.redirectTo("/login.xhtml");
        } catch (IOException e) {
        }
    }

    public void logout(ComponentSystemEvent e) throws IOException {
        FacesContext fContext = FacesContext.getCurrentInstance();
        ExternalContext extContext = fContext.getExternalContext();
        extContext.getSessionMap().put("loggedInUser", null);
        HttpSession session = (HttpSession) extContext.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        super.redirectTo("/login.xhtml");
    }

//    /**
//     * http://stackoverflow.com/questions/3841361/jsf-http-session-login
//     *
//     * @return
//     */
//    public String logout() {
//        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
//        return "/login?faces-redirect=true";
//    }
}