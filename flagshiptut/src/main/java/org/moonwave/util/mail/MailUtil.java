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

import org.moonwave.util.MailProperties;

/**
 * Mail utilities
 *
 * @author Jonathan Luo
 */
public class MailUtil {

    static String delimiter = ",";

    public MailTemplate getMailTemplate() {
        MailTemplate mt = new MailTemplate();
        MailProperties mp = MailProperties.getInstance();
        mt.setFrom(mp.getFrom());
        mt.setTo(mp.getTo());
        mt.setCc(mp.getCc());
        mt.setBcc(mp.getBcc());
        mt.setSubjectPrefix(mp.getSubjectPrefix());
        mt.setSubjectSuffix(mp.getSubjectSuffix());
        mt.setBodyPrefix(mp.getBodyPrefix());
        mt.setBodySuffix(mp.getBoySuffix());
        mt.setBodyPs(mp.getPostscript());
        return mt;
    }

    /**
     * Applies mail template to outgoing mail
     *
     * @param mail outgoing mail
     * @param mt mail template
     * @return merged outgoing mail
     */
    public SimpleMail applyTemplate(SimpleMail mail, MailTemplate mt) {
        mail.setFrom(merge(mail.getFrom(), mt.getFrom()));
        mail.setTo(merge(mail.getTo(), mt.getTo()));
        mail.setCc(merge(mail.getCc(), mt.getCc()));
        mail.setBcc(merge(mail.getBcc(), mt.getBcc()));

        StringBuffer sb = new StringBuffer(1024);
        sb.append(mt.getSubjectPrefix()).append(mail.getSubject()).append(mt.getSubjectSuffix());
        mail.setSubject(sb.toString());

        sb.delete(0, sb.length());
        sb.append(mt.getBodyPrefix()).append(mail.getBody()).append(mt.getBodySuffix())
          .append(mt.getBodyPs());
        mail.setBody(sb.toString());

        return mail;
    }

    /**
     * Merges str2 to str1, applies delimiter if str1 has data.
     *
     * @param str1 string 1, the participant and target string
     * @param str2 other string to be merged
     * @param delimiter the delimiter
     * @return merged string
     */
    private String merge(String str1, String str2, String delimiter) {
        if ((str1 == null) && (str2 == null))
            return null;
        if ((str1 == null) && (str2 != null))
            return str2;
        if ((str1 != null) && (str2 == null))
            return str1;
        // str1 and str2 both not null
        if (str1.equals(str2)) // the same string
            return str1;
        if (str2.length() > 0) {
            if (str1.length() > 0)
                str1 += delimiter + str2;
            else
                str1 = str2;
        }
        return str1;
    }

    /**
     * Merges str2 to str1, applies default delimiter if str1 has data.
     *
     * @param str1 string 1, the participant and target string
     * @param str2 other string to be merged
     * @return merged string
     */
    private String merge(String str1, String str2) {
        return merge(str1, str2, delimiter);
    }
}