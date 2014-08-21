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

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import javax.xml.namespace.QName;

/**
 * This class represents WSDL->definitions->types->schema->complexType->attribute
 * 
 * @author http://cheenath.com
 * @version 158
 */
public class Attribute implements Constants {

    private Schema schema;
    private String name;
    private QName type;
    private static final int MAX_LENGTH = 255;

    private static final Pattern pattern = Pattern.compile("[\\s:]");

    public Attribute(Schema schema) {
        this.schema = schema;
    }

    @Override
    public String toString() {
        return "Attribute{" + "name=" + name + '}';
    }

    public String getName() {
        return name;
    }

    public QName getType() {
        return type;
    }

    void read(WsdlParser parser) throws WsdlParseException {
        name = parser.getAttributeValue(null, NAME);

        if (name == null) {
            throw new WsdlParseException("attribute name can not be null at: " + parser.getPositionDescription());
        }

        if ("".equals(name)) {
            throw new WsdlParseException("attribute name can not be empty at: " + parser.getPositionDescription());
        }

        if (name.length() > MAX_LENGTH) {
            throw new WsdlParseException("attribute name '" + name + "' bigger than max length: " + MAX_LENGTH);
        }

        Matcher matcher = pattern.matcher(name);
        if (matcher.find()) {
            throw new WsdlParseException("attribute name '" + name + "' is not a valid attribute name");
        }

        String t = parser.getAttributeValue(null, TYPE);

        if (t != null) {
            type = ParserUtil.toQName(t, parser);
        }

        int eventType = parser.getEventType();

        while (true) {
            if (eventType == XmlInputStream.START_TAG) {
                String name = parser.getName();
                String namespace = parser.getNamespace();

                if (SIMPLE_TYPE.equals(name) && SCHEMA_NS.equals(namespace)) {
                    if (type != null) {
                        throw new WsdlParseException("type should not be specified: " + parser.getPositionDescription());
                    }

                    SimpleType st = new SimpleType(schema);
                    st.read(parser, name);

                    type = new QName(SCHEMA_NS, "string");
                }

            } else if (eventType == XmlInputStream.END_TAG) {
                String name = parser.getName();
                String namespace = parser.getNamespace();

                if (ATTRIBUTE.equals(name) && SCHEMA_NS.equals(namespace)) {
                    break;
                }
            } else if (eventType == XmlInputStream.END_DOCUMENT) {
                throw new WsdlParseException("Failed to find end tag for 'attribute'");
            }

            eventType = parser.next();
        }

        if (type == null) {
            throw new WsdlParseException("type not specified for attribute: " + name);
        }
    }
}
