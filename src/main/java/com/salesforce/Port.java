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
 * Port
 * 
 * @author http://cheenath.com
 * @version 1.0
 * @since 1.0 Jan 20, 2006
 */
public class Port extends WsdlNode {

    private String name;
    private QName binding;
    private SoapAddress soapAddress;

    public String getName() {
        return name;
    }

    public QName getBinding() {
        return binding;
    }

    public boolean isSoapBinding() {
        return soapAddress != null;
    }

    public SoapAddress getSoapAddress() {
        return soapAddress;
    }

    void read(WsdlParser parser) throws WsdlParseException {
        name = parser.getAttributeValue(null, NAME);
        String bn = parser.getAttributeValue(null, BINDING);
        if (bn == null) {
            throw new WsdlParseException("Unable to find binding in port " + name);
        }
        binding = ParserUtil.toQName(bn, parser);

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

                if (PORT.equals(name) && WSDL_NS.equals(namespace)) {
                    return;
                }
            }

            eventType = parser.next();
        }
    }

    private void parse(String name, String namespace, WsdlParser parser) throws WsdlParseException {

        if (WSDL_SOAP_NS.equals(namespace)) {
            if (ADDRESS.equals(name)) {
                soapAddress = new SoapAddress();
                soapAddress.read(parser);
            }
        }
    }
}
