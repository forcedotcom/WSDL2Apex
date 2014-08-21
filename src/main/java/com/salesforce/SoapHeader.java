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

import javax.xml.namespace.QName;

/**
 * SoapHeader
 * 
 * @author cheenath
 * @version 1.0
 * @since 146 Dec 13, 2006
 */
public class SoapHeader extends SoapNode {
    private String use;
    private QName message;
    private String part;

    public String getUse() {
        return use;
    }

    public QName getMessage() {
        return message;
    }

    public String getPart() {
        return part;
    }

    void read(WsdlParser parser) throws WsdlParseException {
        use = parseUse(parser);
        message = parseMessage(parser);
        part = parser.getAttributeValue(null, "part");

        int eventType = parser.getEventType();
        while (true) {
            if (eventType == XmlInputStream.START_TAG) {
                //nothing to do
            } else if (eventType == XmlInputStream.END_TAG) {
                String name = parser.getName();
                String namespace = parser.getNamespace();
                if (HEADER.equals(name) && WSDL_SOAP_NS.equals(namespace)) {
                    return;
                }
            }
            eventType = parser.next();
        }
    }

    @Override
    public String toString() {
        return "SoapHeader{" + "use='" + use + '\'' + ", message=" + message + ", part='" + part + '\'' + '}';
    }
}
