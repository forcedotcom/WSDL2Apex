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

import java.io.*;
import java.net.URL;

/**
 * @author cheenath.com
 * @version 1.0
 * @since 1.0 Nov 5, 2005
 */
public class WsdlFactory {

    /**
     * @param url
     *            url of the wsdl
     * @return parsed definitions
     * @throws WsdlParseException
     *             failed to parse wsdl
     * @throws
     */
    public static Definitions create(URL url) throws WsdlParseException, IOException {
        InputStream in = url.openStream();
        try {
            return createFromInputStream(in);
        } finally {
            closeQuietly(in);
        }
    }

    public static Definitions createFromString(String wsdl) throws WsdlParseException {
        ByteArrayInputStream in = new ByteArrayInputStream(wsdl.getBytes());
        try {
            return createFromInputStream(in);
        } finally {
            closeQuietly(in);
        }
    }

    private static Definitions createFromInputStream(InputStream in) throws WsdlParseException {
        XmlInputStream parser = new XmlInputStream();
        WsdlParser wsdlParser = new WsdlParser(parser);
        Definitions definitions = new Definitions();
        wsdlParser.setInput(in, "UTF-8");
        definitions.read(wsdlParser);
        return definitions;
    }

    private static void closeQuietly(InputStream in) {
        if (in != null) {
            try {
                in.close();
            } catch (IOException ignored) {
                // ignore IOException while closing stream
            }
        }
    }
}
