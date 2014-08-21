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

import java.util.ArrayList;
import java.util.Iterator;

/**
 * This class represents WSDL->definitions->types->schema->complexType->sequence
 * 
 * @author http://cheenath.com
 * @version 1.0
 * @since 1.0 Nov 9, 2005
 */
public class Collection implements Constants {

    private String type;
    private ArrayList<Element> elements = new ArrayList<Element>();
    private Schema schema;

    public Collection(Schema schema, String type) {
        this.schema = schema;
        this.type = type;
    }

    public Iterator<Element> getElements() {
        return elements.iterator();
    }

    @Override
    public String toString() {
        return "Collection{" + "type=" + type + "elements=" + elements + '}';
    }

    void read(WsdlParser parser) throws WsdlParseException {
        int eventType = parser.getEventType();

        while (true) {
            if (eventType == XmlInputStream.START_TAG) {
                String name = parser.getName();
                String namespace = parser.getNamespace();

                if (ELEMENT.equals(name) && SCHEMA_NS.equals(namespace)) {
                    Element element = new Element(schema);
                    element.read(parser);
                    elements.add(element);
                } else if (SEQUENCE.equals(name) || CHOICE.equals(name) || ALL.equals(name) || "any".equals(name)) {
                    //consume event
                } else {
                    throw new WsdlParseException("Unexpected element '" + name + "' at: "
                            + parser.getPositionDescription());
                }
            } else if (eventType == XmlInputStream.END_TAG) {
                String name = parser.getName();
                String namespace = parser.getNamespace();

                if (type.equals(name) && SCHEMA_NS.equals(namespace)) {
                    return;
                }
            } else if (eventType == XmlInputStream.END_DOCUMENT) {
                throw new WsdlParseException("Failed to find end tag for 'complexType'");
            }

            eventType = parser.next();
        }
    }
}
