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
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Taken from axis:
 * <p/>
 * The DateSerializer deserializes a Date. Much of the work is done in the base class.
 * 
 * @author Sam Ruby (rubys@us.ibm.com) Modified for JAX-RPC @author Rich Scheuerle (scheu@us.ibm.com)
 */
public class DateCodec {

    private static final SimpleDateFormat zulu = new SimpleDateFormat("yyyy-MM-dd");

    //  0123456789 0 123456789

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
     *            source
     * @return calendar
     */
    public Calendar deserialize(String source) {
        if (source == null) {
            throw new NumberFormatException("Unable to parse date");
        }

        Date result;
        boolean bc = false;
        Calendar calendar = Calendar.getInstance();

        // validate fixed portion of format
        if (source != null) {
            if (source.length() < 10)
                throw new NumberFormatException("Unable to parse date");

            if (source.charAt(0) == '+')
                source = source.substring(1);

            if (source.charAt(0) == '-') {
                source = source.substring(1);
                bc = true;
            }

            if (source.charAt(4) != '-' || source.charAt(7) != '-')
                throw new NumberFormatException("unable to parse date");
        }

        // convert what we have validated so far
        try {
            synchronized (zulu) {
                result = zulu.parse(source.substring(0, 10));
            }
        } catch (Exception e) {
            throw new NumberFormatException(e.toString());
        }

        // support dates before the Christian era
        if (bc) {
            calendar.setTime(result);
            calendar.set(Calendar.ERA, GregorianCalendar.BC);
        } else {
            calendar.setTime(result);
        }

        return calendar;
    }
}
