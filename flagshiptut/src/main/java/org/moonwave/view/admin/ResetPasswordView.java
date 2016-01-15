package org.moonwave.view.admin;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.moonwave.jpa.bo.UserBO;
import org.moonwave.jpa.model.User;
import org.moonwave.util.JSFUtil;
import org.moonwave.util.SHAUtil;
import org.moonwave.util.StackTrace;
import org.moonwave.util.StringUtil;
import org.moonwave.view.BaseView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ResetPasswordView
 *
 * @author moonwave
 *
 */
@ManagedBean
@ViewScoped
public class ResetPasswordView extends BaseView {

    private static final long serialVersionUID = 1317717701493291877L;

    static final Logger LOG = LoggerFactory.getLogger(ResetPasswordView.class);

    User user;
    String password;
    String password2;

    @PostConstruct
    public void init() {
        // search user by action_code
        String actionCode = super.getParameter("actionCode");
        try {
            if (StringUtil.nullOrEmpty(actionCode)) {
                JSFUtil.getFlash().put("title", "Action code");
                JSFUtil.getFlash().put("msg", "Your action code was invalid");
                JSFUtil.navigation("/error/error.xhtml?faces-redirect=true&msgCode=101");
                return;
            }
            user = new UserBO().findByActionCode(actionCode);
            if (user == null) {
                JSFUtil.getFlash().put("title", "Action code");
                JSFUtil.getFlash().put("msg", "Your action code was invalid");
                JSFUtil.redirectTo("/error/error.xhtml?faces-redirect=true&msgCode=101");
                return;
            }
        } catch (Exception e) {
            LOG.error(StackTrace.toString(e));
        }
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public String save() {
        if ((password != null) && !password.equals(password2)) {
            super.error("Password not equal");
            return "";
        }
        if (getPassword().length() < 4) {
            super.error("Password must be at least 4 characters long");
            return "";
        }
        try {
            user.setPassword(SHAUtil.encryptPassword(password));
            super.getBasebo().update(user);
            super.info("Password was successfully changed");

            this.password  = null;
            this.password2 = null;
        } catch (Exception e) {
            LOG.error(e.getLocalizedMessage());
            super.error("An error occurred while saving data");
        }
        return "";
    }
}
