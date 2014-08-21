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
 * SoapAddress
 * 
 * @author http://cheenath.com
 * @version 1.0
 * @since 1.0 Jan 20, 2006
 */
public class SoapAddress extends WsdlNode {
    private String location;

    public String getLocation() {
        return location;
    }

    void read(WsdlParser parser) throws WsdlParseException {
        location = parser.getAttributeValue(null, LOCATION);

        int eventType = parser.getEventType();

        while (true) {
            if (eventType == XmlInputStream.START_TAG) {
                String name = parser.getName();
                String namespace = parser.getNamespace();

                if (name != null && namespace != null) {

                }
            } else if (eventType == XmlInputStream.END_TAG) {
                String name = parser.getName();
                String namespace = parser.getNamespace();

                if (ADDRESS.equals(name) && WSDL_SOAP_NS.equals(namespace)) {
                    return;
                }
            }

            eventType = parser.next();
        }
    }
}
