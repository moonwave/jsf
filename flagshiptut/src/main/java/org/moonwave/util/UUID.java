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
package org.moonwave.util;

import java.rmi.server.UID;

import org.apache.commons.lang.StringUtils;

/**
 * Generate unique uuid
 * 
 * @author moonwave
 */
public class UUID {

    /**
     * Returns a uuid based on java.rmi.server.UID
     * @return
     */
    public static String newId() {
        UID uid = new UID();
        String uuid = uid.toString();
        uuid = StringUtils.replace(uuid, ":", "");
        uuid = StringUtils.replace(uuid, "-", "");
        return uuid;
    }

    /**
     * Creates uuidb based on java.util.UUID, with '-' as separator
     * Sample output: "8e62e364-4296-4b53-9dc3-a894fa5c86a2"
     * @return
     */
    public static String randomUUID() {
        java.util.UUID uid = java.util.UUID.randomUUID();
        String uuid = uid.toString();
        uuid = StringUtils.replace(uuid, ":", "");
        uuid = StringUtils.replace(uuid, "-", "");
        return uuid;
    }

    /**
     * Creates uuidb based on java.util.UUID, with '-' as separator removed
     *
     * Sample output: "8e62e36442964b539dc3a894fa5c86a2"
     * @return
     */
    public static String randomUUIDStripped() {
        java.util.UUID uid = java.util.UUID.randomUUID();
        String uuid = uid.toString();
        uuid = StringUtils.replace(uuid, ":", "");
        uuid = StringUtils.replace(uuid, "-", "");
        return uuid;
    }
}
