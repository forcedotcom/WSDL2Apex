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
 * This class represents WSDL->Definitions->types->schema->simpleType
 * 
 * @author http://cheenath.com
 * @version 1.0
 * @since 1.0 Nov 9, 2005
 */
public class SimpleType implements Constants {
    private String name;
    private Restriction restriction;
    private Schema schema;

    public SimpleType(Schema schema) {
        this.schema = schema;
    }

    public Restriction getRestriction() {
        return restriction;
    }

    public Schema getSchema() {
        return schema;
    }

    @Override
    public String toString() {
        return "SimpleType{" + "name='" + name + '\'' + ", restriction=" + restriction + '}';
    }

    public void read(WsdlParser parser, String elementName) throws WsdlParseException {

        name = parser.getAttributeValue(null, NAME);

        if (name == null) {
            name = elementName;
        }

        if (name == null) {
            throw new WsdlParseException("simpleType->element Name can not be null. " + parser.getPositionDescription());
        }

        int eventType = parser.getEventType();

        while (true) {
            if (eventType == XmlInputStream.START_TAG) {
                String name = parser.getName();
                String namespace = parser.getNamespace();

                if (RESTRICTION.equals(name) && SCHEMA_NS.equals(namespace)) {
                    restriction = new Restriction(schema);
                    restriction.read(parser);
                }
            } else if (eventType == XmlInputStream.END_TAG) {
                String name = parser.getName();
                String namespace = parser.getNamespace();

                if (SIMPLE_TYPE.equals(name) && SCHEMA_NS.equals(namespace)) {
                    return;
                }
            } else if (eventType == XmlInputStream.END_DOCUMENT) {
                throw new WsdlParseException("Failed to find end tag for 'simpleType'");
            }

            eventType = parser.next();
        }
    }

    public String getName() {
        return name;
    }
}
