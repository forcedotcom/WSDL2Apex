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

/**
 * This class represents WSDL definitions->service.
 * 
 * @author http://cheenath.com
 * @version 1.0
 * @since 1.0 Nov 5, 2005
 */
public class Service extends WsdlNode {
    private Port port;

    public Port getPort() {
        return port;
    }

    void read(WsdlParser parser) throws WsdlParseException {

        int eventType = parser.getEventType();

        while (true) {
            if (eventType == XmlInputStream.START_TAG) {
                String name = parser.getName();
                String namespace = parser.getNamespace();

                if (name != null && namespace != null) {
                    parse(name, namespace, parser);
                }
            } else if (eventType == XmlInputStream.END_TAG) {
                String name = parser.getName();
                String namespace = parser.getNamespace();

                if (SERVICE.equals(name) && WSDL_NS.equals(namespace)) {
                    break;
                }
            }

            eventType = parser.next();
        }

        if (port == null) {
            throw new WsdlParseException("Unable to find port in wsdl:service");
        }
    }

    private void parse(String name, String namespace, WsdlParser parser) throws WsdlParseException {

        if (WSDL_NS.equals(namespace)) {
            if (PORT.equals(name)) {
                port = new Port();
                port.read(parser);
            }
        }
    }
}
