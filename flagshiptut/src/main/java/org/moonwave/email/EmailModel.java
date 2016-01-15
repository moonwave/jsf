package org.moonwave.email;

import org.moonwave.util.mail.SimpleMail;

/**
 * Email Model
 * 
 * @author moonwave
 */
public class EmailModel extends SimpleMail implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    
    boolean groupMail;
    String sendTo;
    String showCc = "false"; // hidden show CC status
    String showBcc = "false"; // hidden show BCC status
    String showPostscript = "false";

    public boolean isGroupMail() {
        return groupMail;
    }
    public void setGroupMail(boolean groupMail) {
        this.groupMail = groupMail;
    }
    public String getSendTo() {
        return sendTo;
    }
    public void setSendTo(String sendTo) {
        this.sendTo = sendTo;
    }

    public String getShowCc() {
        return showCc;
    }
    public void setShowCc(String showCc) {
        this.showCc = showCc;
    }
    public void setShowCc(boolean showCc) {
        this.showCc = showCc ? "true" : "false";
    }

    public String getShowBcc() {
        return showBcc;
    }
    public void setShowBcc(String showBcc) {
        this.showBcc = showBcc;
    }
    public void setShowBcc(boolean showBcc) {
        this.showBcc = showBcc ? "true" : "false";
    }


    public String getShowPostscript() {
        return showPostscript;
    }
    public void setShowPostscript(String showPostscript) {
        this.showPostscript = showPostscript;
    }
    public void setShowPostscript(boolean showPostscript) {
        this.showPostscript = showPostscript ? "true" : "false";
    }

    public boolean isShowCc() {
        return showCc.equals("true");
    }
    public boolean isShowBcc() {
        return showBcc.equals("true");
    }
    public boolean isShowPostscript() {
        return showPostscript.equals("true");
    }

    public SimpleMail toSimpleMail() {
        SimpleMail sm = new SimpleMail();
        sm.setFrom(from);
        sm.setReplyTo(replyTo);
        sm.setTo(to);
        sm.setCc(cc);
        sm.setBcc(bcc);
        sm.setSubject(subject);
        sm.setBody(body);
        sm.setPostscript(postscript);
        sm.setHtmlMail(htmlMail);
        sm.setAppendPostscript(appendPostscript);
        return sm;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("(from: ").append(from);
        sb.append(", replyTo: ").append(replyTo);
        sb.append(", sendTo: ").append(sendTo);
        sb.append(", to: ").append(to);
        sb.append(", showCc: ").append(showCc);
        sb.append(", showBcc: ").append(showBcc);
        sb.append(", cc: ").append(cc);
        sb.append(", bcc: ").append(bcc);
        sb.append(", subject: ").append(subject);
        sb.append(", body: ").append(body).append(")");
        return sb.toString();
    }
}
