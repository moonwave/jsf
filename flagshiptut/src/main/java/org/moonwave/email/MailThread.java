package org.moonwave.email;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.moonwave.util.mail.PostMan;
import org.moonwave.util.mail.SimpleMail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author moonwave
 */
public class MailThread extends Thread {

	static final Logger LOG = LoggerFactory.getLogger(MailThread.class);
    boolean bGroupMail = false;
    List<SimpleMail> mails;
    SimpleMail mail;
    List<String> attachments;
    List<File> fileList;

    public MailThread() {
        setDaemon(true);
    }

    public MailThread(boolean isDaemon) {
        setDaemon(isDaemon);
    }

    @Override
    public void run() {
        LOG.info("Mail thread starts");
        try {
            new PostMan().send(mail, attachments);
        } catch (Exception e) {
            LOG.error(e.getMessage());
        } finally {
        }
    }

    public void send() {
        start();
    }

    /**
     * Sets group mail info
     *
     * @param mails
     * @param attachments
     */
    public void setGroupMailInfo(List<SimpleMail> mails, List<String> attachments, List<File> fileList) {
        bGroupMail = true;
        this.mails = mails;
        this.attachments = (attachments != null) ? attachments : new ArrayList<String>();
        this.fileList = (fileList != null) ? fileList : new ArrayList<File>();
    }

    /**
     * Sets individual mail info
     *
     * @param mail
     * @param attachments
     */
    public void setMailInfo(SimpleMail mail, List<String> attachments, List<File> fileList) {
        bGroupMail = false;
        this.mail = mail;
        this.attachments = (attachments != null) ? attachments : new ArrayList<String>();
        this.fileList = (fileList != null) ? fileList : new ArrayList<File>();
    }
}
