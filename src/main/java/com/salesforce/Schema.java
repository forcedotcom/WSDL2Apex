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

import java.util.HashMap;
import java.util.Iterator;

/**
 * This class represents WSDL->definitions->types->schema
 * 
 * @author http://cheenath.com
 * @version 1.0
 * @since 1.0 Nov 9, 2005
 */
public class Schema implements Constants {

    private String targetNamespace;
    private String elementFormDefault;
    private String attributeFormDefault;
    private HashMap<String, ComplexType> complexTypes = new HashMap<String, ComplexType>();
    private HashMap<String, SimpleType> simpleTypes = new HashMap<String, SimpleType>();
    private HashMap<String, Element> elements = new HashMap<String, Element>();

    public String getTargetNamespace() {
        return targetNamespace;
    }

    public boolean isElementFormQualified() {
        return "qualified".equals(elementFormDefault);
    }

    public boolean isAttributeFormQualified() {
        return "qualified".equals(attributeFormDefault);
    }

    public void addComplexType(ComplexType type) {
        complexTypes.put(type.getName(), type);
    }

    public void addSimpleType(SimpleType type) {
        simpleTypes.put(type.getName(), type);
    }

    public java.util.Collection<ComplexType> getComplexTypes() {
        return complexTypes.values();
    }

    public java.util.Collection<SimpleType> getSimpleTypes() {
        return simpleTypes.values();
    }

    public ComplexType getComplexType(String type) {
        return complexTypes.get(type);
    }

    public SimpleType getSimpleType(String type) {
        return simpleTypes.get(type);
    }

    public Element getGlobalElement(String name) {
        return elements.get(name);
    }

    public Iterator<Element> getGlobalElements() {
        return elements.values().iterator();
    }

    @Override
    public String toString() {
        return "Schema{" + "targetNamespace='" + targetNamespace + '\'' + ", elementFormDefault='" + elementFormDefault
                + '\'' + ", attributeFormDefault='" + attributeFormDefault + '\'' + ", complexTypes=" + complexTypes
                + '}';
    }

    public void read(WsdlParser parser) throws WsdlParseException {
        targetNamespace = parser.getAttributeValue(null, TARGET_NAME_SPACE);
        elementFormDefault = parser.getAttributeValue(null, ELEMENT_FORM_DEFAULT);
        attributeFormDefault = parser.getAttributeValue(null, ATTRIBUTE_FORM_DEFAULT);

        int eventType = parser.getEventType();

        while (true) {
            if (eventType == XmlInputStream.START_TAG) {
                String n = parser.getName();
                String ns = parser.getNamespace();

                if (COMPLEX_TYPE.equals(n) && SCHEMA_NS.equals(ns)) {
                    ComplexType complexType = new ComplexType(this);
                    complexType.read(parser, null);
                    complexTypes.put(complexType.getName(), complexType);
                } else if (ELEMENT.equals(n) && SCHEMA_NS.equals(ns)) {
                    Element element = new Element(this);
                    element.read(parser);
                    elements.put(element.getName(), element);
                } else if (SIMPLE_TYPE.equals(n) && SCHEMA_NS.equals(ns)) {
                    SimpleType simpleType = new SimpleType(this);
                    simpleType.read(parser, null);
                    simpleTypes.put(simpleType.getName(), simpleType);
                } else if (SCHEMA.equals(n) && SCHEMA_NS.equals(ns)) {
                    //skip header
                } else if ("import".equals(n) && SCHEMA_NS.equals(ns)) {
                    String location = parser.getAttributeValue(null, "schemaLocation");
                    if (location != null) {
                        throw new WsdlParseException("Found schema import from location " + location
                                + ". External schema import not supported");
                    }
                } else if (ANNOTATION.equals(n) && SCHEMA_NS.equals(ns)) {
                    Annotation annotation = new Annotation();
                    annotation.read(parser);
                } else {
                    throw new WsdlParseException("Unsupported Schema element found " + ns + ":" + n + ". At: "
                            + parser.getPositionDescription());
                }
            } else if (eventType == XmlInputStream.END_TAG) {
                String name = parser.getName();
                String namespace = parser.getNamespace();

                if (SCHEMA.equals(name) && SCHEMA_NS.equals(namespace)) {
                    break;
                }
            } else if (eventType == XmlInputStream.END_DOCUMENT) {
                throw new WsdlParseException("Failed to find end tag for 'schema'");
            }

            eventType = parser.next();
        }

        if (targetNamespace == null) {
            throw new WsdlParseException("schema:targetNamespace can not be null");
        }
    }
}
