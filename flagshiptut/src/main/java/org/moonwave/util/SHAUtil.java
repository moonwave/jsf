/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * Copyright (C) 2009 - 2015 Jonathan Luo
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
package org.moonwave.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * SHA1Util
 * http://www.sha1-online.com/sha1-java/
 * 
 * @author moonwave
 */
public class SHAUtil {

    /**
     * @param args
     * @throws NoSuchAlgorithmException 
     */
    public static void main(String[] args) throws NoSuchAlgorithmException {
        System.out.println(sha1("test string to sha1"));
        System.out.println("=== aff -sha1 ===");
        System.out.println(sha1("aff"));

        System.out.println("=== aff - sha-2 ===");
        System.out.println(sha2("aff"));
    }

    /**
     * SHA-1 ⇒ 40 digit hexadecimal number
     *
     */
    public static String sha1(String input) {
        try {
            MessageDigest mDigest = MessageDigest.getInstance("SHA1");
            byte[] result = mDigest.digest(input.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < result.length; i++) {
                sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * http://stackoverflow.com/questions/6840206/sha2-password-hashing-in-java
     *
     * SHA-2 ⇒ 64 digit hexadecimal number
     *
     * @param password
     * @return
     */
    public static String sha2(String password) {
        try {
            MessageDigest mDigest = MessageDigest.getInstance("SHA-256");
            String salt = "random_salt_77"; // get from property file
            String passWithSalt = password + salt;
            byte[] passBytes = passWithSalt.getBytes();
            byte[] result = mDigest.digest(passBytes);
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< result.length ;i++) {
                sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Returns encrypted password for a given clear text password
     * 
     * @param password clear text password.
     * @return encrypted password.
     */
    public static String encryptPassword (String password) {
        return sha2(password);
    }

}
