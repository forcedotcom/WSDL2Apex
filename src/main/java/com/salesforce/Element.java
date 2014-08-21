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
 * This class represents WSDL->Definitions->types->schema->element
 * 
 * @author http://cheenath.com
 * @version 1.0
 * @since 1.0 Nov 9, 2005
 */
public class Element implements Constants {
    public static final int UNBOUNDED = -1;

    private String name;
    private QName type;
    private QName ref;
    private String nillable;
    private String minOccurs;
    private String maxOccurs;
    private Schema schema;
    private boolean isComplexType = true;

    public Element(Schema schema) {
        this.schema = schema;
    }

    public boolean isComplexType() {
        return isComplexType;
    }

    public QName getType() {
        return type;
    }

    public QName getRef() {
        return ref;
    }

    public Schema getSchema() {
        return schema;
    }

    public int getMinOccurs() {
        return toRange(minOccurs);
    }

    public int getMaxOccurs() {
        return toRange(maxOccurs);
    }

    public boolean isNillable() {
        return "true".equals(nillable);
    }

    private int toRange(String occurs) {
        if (occurs == null || "".equals(occurs)) {
            return 1;
        } else if ("unbounded".equals(occurs)) {
            return UNBOUNDED;
        } else {
            return Integer.parseInt(occurs);
        }
    }

    @Override
    public String toString() {
        return "Element{" + "name='" + name + '\'' + ", type='" + type + '\'' + ", ref='" + ref + '\'' + ", nillable='"
                + nillable + '\'' + ", minOccurs='" + minOccurs + '\'' + ", maxOccurs='" + maxOccurs + '\'' + '}';
    }

    public void read(WsdlParser parser) throws WsdlParseException {
        name = parser.getAttributeValue(null, NAME);
        nillable = parser.getAttributeValue(null, NILLABLE);
        minOccurs = parser.getAttributeValue(null, MIN_OCCURS);
        maxOccurs = parser.getAttributeValue(null, MAX_OCCURS);
        parseType(parser);
        parseRef(parser);

        if (ref != null) {
            if (name != null) {
                throw new WsdlParseException("Both name and ref can not be specified for element: " + name);
            }
            if (type != null) {
                throw new WsdlParseException("Both type and ref can not be specified for element with ref: " + ref);
            }
        }

        int eventType = parser.getEventType();

        while (true) {
            if (eventType == XmlInputStream.START_TAG) {
                String n = parser.getName();
                String ns = parser.getNamespace();

                if (COMPLEX_TYPE.equals(n) && SCHEMA_NS.equals(ns)) {
                    ComplexType complexType = new ComplexType(schema);
                    String ctn = name + "_element";
                    type = new QName(schema.getTargetNamespace(), ctn);
                    complexType.read(parser, ctn);
                    schema.addComplexType(complexType);
                    isComplexType = true;
                } else if (SIMPLE_TYPE.equals(n) && SCHEMA_NS.equals(ns)) {
                    SimpleType simpleType = new SimpleType(schema);
                    String stn = name + "_element";
                    type = new QName(schema.getTargetNamespace(), stn);
                    simpleType.read(parser, stn);
                    schema.addSimpleType(simpleType);
                    isComplexType = false;
                }
            } else if (eventType == XmlInputStream.END_TAG) {
                String name = parser.getName();
                String namespace = parser.getNamespace();

                if (ELEMENT.equals(name) && SCHEMA_NS.equals(namespace)) {
                    if (type == null) {}
                    return;
                }
            } else if (eventType == XmlInputStream.END_DOCUMENT) {
                throw new WsdlParseException("Failed to find end tag for 'complexType'");
            }

            eventType = parser.next();
        }
    }

    private void parseType(WsdlParser parser) {
        String t = parser.getAttributeValue(null, TYPE);

        if (t != null) {
            type = ParserUtil.toQName(t, parser);
            isComplexType = !SCHEMA_NS.equals(type.getNamespaceURI());
        }
    }

    private void parseRef(WsdlParser parser) throws WsdlParseException {
        String r = parser.getAttributeValue(null, REF);

        if (r != null) {
            if ("".equals(r)) {
                throw new WsdlParseException("Element ref can not be empty, at: " + parser.getPositionDescription());
            }
            ref = ParserUtil.toQName(r, parser);
            if (ref.getNamespaceURI() == null || "".equals(ref.getNamespaceURI())) {
                ref = new QName(schema.getTargetNamespace(), ref.getLocalPart());
            }
        }
    }

    public String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    void setType(QName type) {
        this.type = type;
    }
}
