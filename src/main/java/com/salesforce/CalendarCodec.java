/*******************************************************************************
 * Copyright (c) 2014 Salesforce.com, inc..
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Salesforce.com, inc. - initial API and implementation
 ******************************************************************************/
package com.salesforce.ide.wsdl2apex.core;

import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * The CalendarSerializer deserializes a dateTime. Much of the work is done in the base class.
 * 
 * @author Sam Ruby (rubys@us.ibm.com) Modified for JAX-RPC @author Rich Scheuerle (scheu@us.ibm.com)
 * @author http://cheenath.com adopted from Apache AXIS
 * @version 1.0
 * @since 1.0 Dec 2, 2005
 */
public class CalendarCodec {

    private static final SimpleDateFormat zulu = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    //  0123456789 0 123456789

    static {
        zulu.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    public String getValueAsString(Object value) {
        Date date = value instanceof Date ? (Date) value : ((Calendar) value).getTime();

        // Serialize including convert to GMT
        synchronized (zulu) {
            // Sun JDK bug http://developer.java.sun.com/developer/bugParade/bugs/4229798.html
            return zulu.format(date);
        }
    }

    /**
     * The simple deserializer provides most of the stuff. We just need to override makeValue().
     * 
     * @param source
     *            source string to deserialize
     * @return calendar created
     */
    public Calendar deserialize(String source) {
        Calendar calendar = Calendar.getInstance();
        Date date;
        boolean bc = false;

        // validate fixed portion of format
        if (source == null || source.length() == 0) {
            throw new NumberFormatException("Unable to parse dateTime");
        }
        if (source.charAt(0) == '+') {
            source = source.substring(1);
        }
        if (source.charAt(0) == '-') {
            source = source.substring(1);
            bc = true;
        }
        if (source.length() < 19) {
            throw new NumberFormatException("Unable to parse dateTime");
        }
        if (source.charAt(4) != '-' || source.charAt(7) != '-' || source.charAt(10) != 'T') {
            throw new NumberFormatException("Unable to parse dateTime");
        }
        if (source.charAt(13) != ':' || source.charAt(16) != ':') {
            throw new NumberFormatException("Unable to parse dateTime");
        }
        // convert what we have validated so far
        try {
            synchronized (zulu) {
                date = zulu.parse(source.substring(0, 19) + ".000Z");
            }
        } catch (Exception e) {
            throw new NumberFormatException(e.toString());
        }
        int pos = 19;

        // parse optional milliseconds
        if (pos < source.length() && source.charAt(pos) == '.') {
            int milliseconds;
            int start = ++pos;
            while (pos < source.length() && Character.isDigit(source.charAt(pos))) {
                pos++;
            }
            String decimal = source.substring(start, pos);
            if (decimal.length() == 3) {
                milliseconds = Integer.parseInt(decimal);
            } else if (decimal.length() < 3) {
                milliseconds = Integer.parseInt((decimal + "000").substring(0, 3));
            } else {
                milliseconds = Integer.parseInt(decimal.substring(0, 3));
                if (decimal.charAt(3) >= '5') {
                    ++milliseconds;
                }
            }

            // add milliseconds to the current date
            date.setTime(date.getTime() + milliseconds);
        }

        // parse optional timezone
        if (pos + 5 < source.length() && (source.charAt(pos) == '+' || (source.charAt(pos) == '-'))) {
            if (!Character.isDigit(source.charAt(pos + 1)) || !Character.isDigit(source.charAt(pos + 2))
                    || source.charAt(pos + 3) != ':' || !Character.isDigit(source.charAt(pos + 4))
                    || !Character.isDigit(source.charAt(pos + 5))) {
                throw new NumberFormatException("Unable to parse dateTime");
            }
            int hours = (source.charAt(pos + 1) - '0') * 10 + source.charAt(pos + 2) - '0';
            int mins = (source.charAt(pos + 4) - '0') * 10 + source.charAt(pos + 5) - '0';
            int milliseconds = (hours * 60 + mins) * 60 * 1000;

            // subtract milliseconds from current date to obtain GMT
            if (source.charAt(pos) == '+') {
                milliseconds = -milliseconds;
            }
            date.setTime(date.getTime() + milliseconds);
            pos += 6;
        }
        if (pos < source.length() && source.charAt(pos) == 'Z') {
            pos++;
            calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
        }
        if (pos < source.length()) {
            throw new NumberFormatException("Unable to parse dateTime");
        }
        calendar.setTime(date);

        // support dates before the Christian era
        if (bc) {
            calendar.set(Calendar.ERA, GregorianCalendar.BC);
        }
        return calendar;
    }
}
