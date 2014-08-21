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

import javax.xml.namespace.QName;

/**
 * This class represents WSDL->Definitions->types->schema->complexType
 * 
 * @author http://cheenath.com
 * @version 1.0
 * @since 1.0 Nov 9, 2005
 */
public class ComplexType implements Constants {
    private String name;
    private Collection content;
    private QName base;
    private Schema schema;
    private boolean isHeader;
    private ArrayList<Attribute> attributes = new ArrayList<Attribute>();

    public ComplexType(Schema schema) {
        this.schema = schema;
    }

    public ComplexType(Schema schema, String name) {
        this.schema = schema;
        this.name = name;
    }

    public Collection getContent() {
        return content;
    }

    public QName getBase() {
        return base;
    }

    public boolean hasBaseClass() {
        return isHeader || base != null;
    }

    public Schema getSchema() {
        return schema;
    }

    public void setHeader(boolean isHeader) {
        this.isHeader = isHeader;
    }

    public boolean isHeader() {
        return isHeader;
    }

    public Iterator<Attribute> getAttributes() {
        return attributes.iterator();
    }

    @Override
    public String toString() {
        return "ComplexType{" + "name='" + name + '\'' + ", content=" + content + ", base=" + base + ", attributes="
                + attributes + '}';
    }

    public void read(WsdlParser parser, String elementName) throws WsdlParseException {

        name = parser.getAttributeValue(null, NAME);

        if (name == null) {
            name = elementName;
        }

        if (name == null) {
            throw new WsdlParseException("complexType->elementName can not be null. " + parser.getPositionDescription());
        }

        int eventType = parser.getEventType();

        while (true) {
            if (eventType == XmlInputStream.START_TAG) {
                String name = parser.getName();
                String namespace = parser.getNamespace();

                if ((SEQUENCE.equals(name) || ALL.equals(name) || CHOICE.equals(name)) && SCHEMA_NS.equals(namespace)) {
                    content = new Collection(schema, name);
                    content.read(parser);
                } else if (EXTENSION.equals(name) && SCHEMA_NS.equals(namespace)) {
                    name = parser.getAttributeValue(null, BASE);

                    if (name != null) {
                        base = ParserUtil.toQName(name, parser);
                    }
                } else if (ATTRIBUTE.equals(name) && SCHEMA_NS.equals(namespace)) {
                    Attribute attribute = new Attribute(schema);
                    attribute.read(parser);
                    addAttribute(attribute);
                }
            } else if (eventType == XmlInputStream.END_TAG) {
                String name = parser.getName();
                String namespace = parser.getNamespace();

                if (COMPLEX_TYPE.equals(name) && SCHEMA_NS.equals(namespace)) {
                    return;
                }
            } else if (eventType == XmlInputStream.END_DOCUMENT) {
                throw new WsdlParseException("Failed to find end tag for 'complexType'");
            }

            eventType = parser.next();
        }
    }

    private void addAttribute(Attribute attribute) throws WsdlParseException {
        String name = attribute.getName();

        for (Attribute att : attributes) {
            if (name.equals(att.getName())) {
                throw new WsdlParseException("Two attributes cannot have the same name: " + name);
            }

        }
        attributes.add(attribute);
    }

    public String getName() {
        return name;
    }
}
