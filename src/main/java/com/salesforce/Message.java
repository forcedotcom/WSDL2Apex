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
import java.util.ArrayList;
import java.util.Iterator;

/**
 * This class represents WSDL->definitions->message
 * 
 * @author http://cheenath.com
 * @version 1.0
 * @since 1.0 Nov 5, 2005
 */
public class Message extends WsdlNode {
    private QName name;
    private ArrayList<Part> parts = new ArrayList<Part>();
    private String targetNamespace;

    Message(String targetNamespace) {
        this.targetNamespace = targetNamespace;
    }

    public QName getName() {
        return name;
    }

    public Iterator<Part> getParts() {
        return parts.iterator();
    }

    public Part getPart(String part) throws ConnectionException {
        for (Part p : parts) {
            if (part.equals(p.getName())) {
                return p;
            }
        }
        throw new ConnectionException("Failed to find part " + part);
    }

    public void read(WsdlParser parser) throws WsdlParseException {
        name = new QName(targetNamespace, parser.getAttributeValue(null, NAME));

        int eventType = parser.getEventType();

        while (true) {
            if (eventType == XmlInputStream.START_TAG) {
                String n = parser.getName();
                String ns = parser.getNamespace();

                if (n != null && ns != null) {
                    parse(n, ns, parser);
                }
            } else if (eventType == XmlInputStream.END_TAG) {
                String name = parser.getName();
                String namespace = parser.getNamespace();

                if (MESSAGE.equals(name) && WSDL_NS.equals(namespace)) {
                    return;
                }
            }

            eventType = parser.next();
        }
    }

    private void parse(String name, String namespace, WsdlParser parser) throws WsdlParseException {

        if (WSDL_NS.equals(namespace)) {

            if (PART.equals(name)) {
                Part part = new Part();
                part.read(parser);
                parts.add(part);
            }
        }
    }

}
