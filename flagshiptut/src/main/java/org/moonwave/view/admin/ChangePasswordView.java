package org.moonwave.view.admin;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.moonwave.jpa.bo.UserBO;
import org.moonwave.jpa.model.User;
import org.moonwave.util.SHAUtil;
import org.moonwave.view.BaseView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ChangePasswordView
 *
 * @author moonwave
 *
 */
@ManagedBean
//@ViewScoped
public class ChangePasswordView extends BaseView {

    private static final long serialVersionUID = 1317717701493291877L;

    static final Logger LOG = LoggerFactory.getLogger(ChangePasswordView.class);

    String password;
    String password2;

    @PostConstruct
    public void init() {
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
            User user = new UserBO().findById(super.getLoggedInUser().getId());
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
