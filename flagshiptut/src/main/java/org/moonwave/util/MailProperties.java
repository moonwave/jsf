package org.moonwave.util;

public class MailProperties {

    private String mailhost = "localhost";
    private String doNotSendDomain = "";
    private String doNotSendMail = "";
    
    private String from;
    private String to;
    private String replyTo;
    private String cc;
    private String bcc;
    private String subjectPrefix;
    private String subjectSuffix;
    private String bodyPrefix;
    private String boySuffix;
    private String postscript;

    private static MailProperties instance;
    public static MailProperties getInstance() {
        if (instance == null) {
            instance = new MailProperties();
        }
        return instance;
    }

    public String getMailhost() {
        return mailhost;
    }
    public void setMailhost(String mailhost) {
        this.mailhost = mailhost;
    }
    public String getDoNotSendDomain() {
        return doNotSendDomain;
    }
    public void setDoNotSendDomain(String doNotSendDomain) {
        this.doNotSendDomain = doNotSendDomain;
    }
    public String getDoNotSendMail() {
        return doNotSendMail;
    }
    public void setDoNotSendMail(String doNotSendMail) {
        this.doNotSendMail = doNotSendMail;
    }
    public String getFrom() {
        return from;
    }
    public void setFrom(String from) {
        this.from = from;
    }
    public String getTo() {
        return to;
    }
    public void setTo(String to) {
        this.to = to;
    }
    public String getReplyTo() {
        return replyTo;
    }
    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }
    public String getCc() {
        return cc;
    }
    public void setCc(String cc) {
        this.cc = cc;
    }
    public String getBcc() {
        return bcc;
    }
    public void setBcc(String bcc) {
        this.bcc = bcc;
    }
    public String getSubjectPrefix() {
        return subjectPrefix;
    }
    public void setSubjectPrefix(String subjectPrefix) {
        this.subjectPrefix = subjectPrefix;
    }
    public String getSubjectSuffix() {
        return subjectSuffix;
    }
    public void setSubjectSuffix(String subjectSuffix) {
        this.subjectSuffix = subjectSuffix;
    }
    public String getBodyPrefix() {
        return bodyPrefix;
    }
    public void setBodyPrefix(String bodyPrefix) {
        this.bodyPrefix = bodyPrefix;
    }
    public String getBoySuffix() {
        return boySuffix;
    }
    public void setBoySuffix(String boySuffix) {
        this.boySuffix = boySuffix;
    }
    public String getPostscript() {
        return postscript;
    }
    public void setPostscript(String postscript) {
        this.postscript = postscript;
    }

}
