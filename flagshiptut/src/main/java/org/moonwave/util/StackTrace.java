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

/**
 * Converts a Throwable object's stack trace information to a printable string. 
 * Works with JDK1.4 or later.
 * 
 * @author moonwave
 */
public class StackTrace {
    public static String toString(Throwable cause) {
        StringBuffer sb = new StringBuffer(1024);
        if (cause != null) {
            sb.append(cause.getMessage());
            sb.append(Constants.NEW_LINE);
            StackTraceElement[] items = cause.getStackTrace();
            if (items != null) {
                for (int i = 0;  i < items.length; i++) {
                    StackTraceElement item = items[i];
                    sb.append(item.toString());
                    sb.append(Constants.NEW_LINE);
                }
            }
        }
        return sb.toString();
    }
}
