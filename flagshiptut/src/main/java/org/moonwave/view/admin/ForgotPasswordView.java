package org.moonwave.view.admin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;

import org.moonwave.email.EmailModel;
import org.moonwave.email.MailThread;
import org.moonwave.jpa.bo.UserBO;
import org.moonwave.jpa.model.User;
import org.moonwave.util.DateUtil;
import org.moonwave.util.JSFUtil;
import org.moonwave.util.MailProperties;
import org.moonwave.util.SHAUtil;
import org.moonwave.util.mail.SimpleMail;
import org.moonwave.view.BaseView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ForgotPasswordView
 *
 * @author moonwave
 *
 */
@ManagedBean
public class ForgotPasswordView extends BaseView {

    private static final long serialVersionUID = -6980002954656894189L;

    static final Logger LOG = LoggerFactory.getLogger(ForgotPasswordView.class);

    User user;
    String email;
    String email2;

    @PostConstruct
    public void init() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail2() {
        return email2;
    }

    public void setEmail2(String email2) {
        this.email2 = email2;
    }

    public String save() {
        if ((email != null) && !email.equals(email2)) {
            super.error("Email not equal");
            return "";
        }
        user = new UserBO().findByEmail(email);
        if (user == null) {
            super.error("Invalid email address");
            return "";
        }
        String url = null;
        try {
            String actionCode = SHAUtil.sha1(email + DateUtil.getTime().toString());
            user.setActionCode(actionCode);
            super.getBasebo().update(user);
            // TODO - send email to user
            ExternalContext ec = JSFUtil.getExternalContext();
            url = JSFUtil.getApplicationURL() + "/admin/resetPassword.xhtml?actionCode=" + actionCode;

            MailProperties mp = MailProperties.getInstance();
            EmailModel emailModel = new EmailModel();
            emailModel.setFrom(mp.getFrom());
            emailModel.setReplyTo(mp.getReplyTo());
            emailModel.setTo(email);
            emailModel.setSubject("Your account password reset");
            emailModel.setBody("Please follow the link below to complete your password reset process\n\n" + url);
            emailModel.setHtmlMail(true);
            emailModel.setGroupMail(false);
            emailModel.setPostscript(mp.getPostscript());
            emailModel.setAppendPostscript(true);

            performSendMailAction(emailModel);
            super.info("An email with reset link was sent to you, thank you!");
        } catch (Exception e) {
            LOG.error(e.getLocalizedMessage());
            super.error("An error occurred while saving data");
        }
        return "";
    }

    // ========================================================= Private methods
    private void performSendMailAction(EmailModel model) {
        // send attachments
        List<String> attachments = new ArrayList<String>(); // list of full path name

        if (model.getFrom() != null)
            model.setFrom(model.getFrom().replaceAll(";", ","));
        if (model.getTo() != null)
            model.setTo(model.getTo().replaceAll(";", ","));
        if (model.getCc() != null)
            model.setCc(model.getCc().replaceAll(";", ","));
        if (model.getBcc() != null)
            model.setBcc(model.getBcc().replaceAll(";", ","));
        if (model.isAppendPostscript())
            model.setBody(model.getBody() + "\n\n" + model.getPostscript());
        if (model.isHtmlMail()) { // replace \n to <br>
            model.setBody(model.getBody().replaceAll("\n", "<br>"));
            model.setBody(model.getBody().replaceAll("\r\n", "<br>"));
        }

        sendMail(model, attachments, null); // send individual mail w/ attachments
        // log email activity
//        logEmailActivity(model);
    }

    /**
     * Sends individual mail with attachments if any
     *
     * @param target
     * @param model
     */
    private void sendMail(SimpleMail mail,
                          List<String> attachments,
                          List<File> fileList) {
        MailThread mailThread = new MailThread();
        mailThread.setMailInfo(mail, attachments, fileList);
        mailThread.send();
    }
}
