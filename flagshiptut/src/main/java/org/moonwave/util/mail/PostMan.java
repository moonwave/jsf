/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * Copyright (C) 2015 Jonathan Luo
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307, USA.
 *
 */
package org.moonwave.util.mail;

import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;


import javax.mail.internet.MimeMultipart;

import org.moonwave.servlet.InitServlet;
import org.moonwave.util.MailProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Mail delivery agent
 *
 * @author Jonathan Luo
 */
public class PostMan {

    static final Logger LOG = LoggerFactory.getLogger(PostMan.class);

    String mailhost = MailProperties.getInstance().getMailhost();
    String doNotSendDomain = MailProperties.getInstance().getDoNotSendDomain();
    String doNotSendMail = MailProperties.getInstance().getDoNotSendMail();

    /**
     * Sends a simple email w/o attachment
     *
     * @param mail mail to send
     * @return
     */
    public boolean send(SimpleMail mail) {
        boolean ret = false;
        try {
            Properties props = new Properties();
            props.put("mail.smtp.host", mailhost);

            // Get a Session object
            Session session = Session.getInstance(props, null);

            // construct the message
            MimeMessage msg = new MimeMessage(session);

            if (mail.getFrom().indexOf(",") > 0)
                msg.addFrom(InternetAddress.parse(mail.getFrom(), false));
            else
                msg.setFrom(new InternetAddress(mail.getFrom()));

            if ((mail.getReplyTo() != null) &&  (mail.getReplyTo().length() > 0))
                msg.setReplyTo(InternetAddress.parse(mail.getReplyTo(), false));

            if (mail.getTo().indexOf(",") > 0)
                msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mail.getTo(), false));
            else
                msg.setRecipient(Message.RecipientType.TO, new InternetAddress(mail.getTo()));

            if ((mail.getCc() != null) &&  (mail.getCc().length() > 0))
                msg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(mail.getCc(), false));

            if ((mail.getBcc() != null) &&  (mail.getBcc().length() > 0))
                msg.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(mail.getBcc(), false));

            msg.setSubject(mail.getSubject());

            if (mail.isHtmlMail())
                msg.setContent(mail.getBody(), "text/html; charset=UTF-8"); // MIME media type
            else
                msg.setContent(mail.getBody(), "text/plain; charset=UTF-8");

            msg.setSentDate(new Date());
            
            Transport.send(msg);
            ret = true;
        } catch (Exception e) {
            LOG.error("Error sending email to '" + mail.getTo() + "'", e);
        }
        return ret;
    }

    /**
     * Sends mail with attachments
     *
     * @param mail mail to send
     * @param attachments attachment file list
     * @return
     */
    public boolean send(SimpleMail mail, List<String> attachments) {
        if (attachments.isEmpty())
            return send(mail);
        boolean ret = false;
        try {
            Properties props = new Properties();
            props.put("mail.smtp.host", mailhost);

            // Get a Session object
            Session session = Session.getInstance(props, null);

            // construct the message
            Message msg = new MimeMessage(session);

            if (mail.getFrom().indexOf(",") > 0)
                msg.addFrom(InternetAddress.parse(mail.getFrom(), false));
            else
                msg.setFrom(new InternetAddress(mail.getFrom()));

            if ((mail.getReplyTo() != null) &&  (mail.getReplyTo().length() > 0))
                msg.setReplyTo(InternetAddress.parse(mail.getReplyTo(), false));

            if (mail.getTo().indexOf(",") > 0)
                msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mail.getTo(), false));
            else
                msg.setRecipient(Message.RecipientType.TO, new InternetAddress(mail.getTo()));

            if ((mail.getCc() != null) &&  (mail.getCc().length() > 0))
                msg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(mail.getCc(), false));

            if ((mail.getBcc() != null) &&  (mail.getBcc().length() > 0))
                msg.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(mail.getBcc(), false));

            msg.setSubject(mail.getSubject());

            // create and fill the first message part
            MimeBodyPart mainMsg = new MimeBodyPart();
//            mainMsg.setText(mail.getBody());
            if (mail.isHtmlMail())
                mainMsg.setContent(mail.getBody(), "text/html; charset=UTF-8"); // MIME media type
            else
                mainMsg.setContent(mail.getBody(), "text/plain; charset=UTF-8");

            // create the Multipart and add its parts to it
            Multipart mp = new MimeMultipart();
            mp.addBodyPart(mainMsg);

            // create the second message part
            for (String filename: attachments) {
                try {
                    MimeBodyPart attachment = new MimeBodyPart();

                    // attach the file to the message
                    FileDataSource fds = new FileDataSource(filename);
                    attachment.setDataHandler(new DataHandler(fds));
                    attachment.setFileName(fds.getName());

                    mp.addBodyPart(attachment);
                } catch (Exception e) {
                    LOG.error("Failed to load attachment '" + filename + "'", e);
                }
            }

            // add the Multipart to the message
            msg.setContent(mp);
            msg.setSentDate(new Date());

            Transport.send(msg);
            ret = true;
        } catch (MessagingException mex) {
            LOG.error("Error sending email to '" + mail.getTo() + "'", mex);
        } catch (Exception e) {
            LOG.error("Error sending email to '" + mail.getTo() + "'", e);
        }
        return ret;
    }

    /**
     * Sends off a list of outgoing mails
     *
     * @param mailList
     */
    public boolean send(List<SimpleMail> mails){
        if (mails == null || mails.isEmpty())
            return true;
        boolean ret = true;
        for (SimpleMail mail : mails) {
            ret &= send(mail);
        }
        return ret;
    }

    /**
     * Sends off a list of outgoing mails with attachments
     *
     * @param mailList mail to send
     * @param attachments attachment file list
     */
    public boolean send(List<SimpleMail> mails, List<String> attachments){
        if (mails == null || mails.isEmpty())
            return true;
        boolean ret = true;
        for (SimpleMail mail : mails) {
            // log email sended, move logging earlier
            if (needToSend(mail)) {
                if (attachments.size() > 0) // has attachment
                    ret &= send(mail, attachments);
                else
                    ret &= send(mail);
            }
        }
        return ret;
    }

    private boolean needToSend(SimpleMail mail) {
        boolean ret = true;
        if ((mail.getTo() != null) && doNotSendMail.contains(mail.getTo().toLowerCase()))
            ret = false;
        else if ((doNotSendDomain.length() > 0) && doNotSendDomain.contains(getDomain(mail)))
            ret = false;
        return ret;
    }

    private String getDomain(SimpleMail mail) {
        String ret = "";
        if (mail.getTo() != null) {
            int i = mail.getTo().indexOf("@");
            if (i != -1) {
                ret = mail.getTo().substring(i, mail.getTo().length());
            }
        }
        return ret;
    }

}
