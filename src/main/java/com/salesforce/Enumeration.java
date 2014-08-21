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
 * This class represents WSDL->Definitions->types->schema->element
 * 
 * @author http://cheenath.com
 * @version 1.0
 * @since 1.0 Nov 9, 2005
 */
public class Enumeration implements Constants {

    private String value;
    private Schema schema;

    public Enumeration(Schema schema) {
        this.schema = schema;
    }

    public Schema getSchema() {
        return schema;
    }

    @Override
    public String toString() {
        return "Enumeration{" + "value='" + value + '\'' + '}';
    }

    public void read(WsdlParser parser) throws WsdlParseException {
        value = parser.getAttributeValue(null, VALUE);

        int eventType = parser.getEventType();

        while (true) {
            if (eventType == XmlInputStream.START_TAG) {} else if (eventType == XmlInputStream.END_TAG) {
                String name = parser.getName();
                String namespace = parser.getNamespace();

                if (ENUMERATION.equals(name) && SCHEMA_NS.equals(namespace)) {
                    return;
                }
            } else if (eventType == XmlInputStream.END_DOCUMENT) {
                throw new WsdlParseException("Failed to find end tag for 'enumeration'");
            }

            eventType = parser.next();
        }
    }

    public String getValue() {
        return value;
    }
}
