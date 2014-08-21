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

import javax.xml.namespace.QName;

/**
 * This class represents Definitions->types.
 * 
 * @author http://cheenath.com
 * @version 1.0
 * @since 1.0 Nov 5, 2005
 */
public class Types extends WsdlNode {

    private HashMap<String, Schema> schemas = new HashMap<String, Schema>();

    @Override
    public String toString() {
        return "Types{" + "schemas=" + schemas + '}';
    }

    public void read(WsdlParser parser) throws WsdlParseException {

        int eventType = parser.getEventType();

        while (true) {
            if (eventType == XmlInputStream.START_TAG) {
                String name = parser.getName();
                String namespace = parser.getNamespace();

                if (SCHEMA.equals(name)) {

                    if (!SCHEMA_NS.equals(namespace)) {
                        throw new WsdlParseException("Unsupport schema version: " + namespace + ". It must be: "
                                + SCHEMA_NS);
                    }

                    Schema schema = new Schema();
                    schema.read(parser);
                    schemas.put(schema.getTargetNamespace(), schema);
                }

            } else if (eventType == XmlInputStream.END_TAG) {
                String name = parser.getName();
                String namespace = parser.getNamespace();

                if (TYPES.equals(name) && WSDL_NS.equals(namespace)) {
                    break;
                }
            } else if (eventType == XmlInputStream.END_DOCUMENT) {
                throw new WsdlParseException("Failed to find end tag for 'types'");
            }

            eventType = parser.next();
        }

        try {
            updateElementRef();
        } catch (ConnectionException e) {
            throw new WsdlParseException(e.getMessage(), e);
        }
    }

    private void updateElementRef() throws WsdlParseException, ConnectionException {
        for (Schema s : getSchemas()) {
            checkGlobalElements(s);

            for (ComplexType ctype : s.getComplexTypes()) {
                if (ctype.getContent() != null) {
                    Iterator<Element> elementIt = ctype.getContent().getElements();

                    while (elementIt.hasNext()) {
                        Element element = elementIt.next();
                        if (element.getRef() != null) {
                            Element targetElement = getElement(element.getRef());
                            element.setName(targetElement.getName());
                            element.setType(targetElement.getType());
                        }
                    }
                }
            }
        }
    }

    private void checkGlobalElements(Schema s) throws WsdlParseException {
        Iterator<Element> elementIt = s.getGlobalElements();
        while (elementIt.hasNext()) {
            Element e = elementIt.next();
            if (e.getRef() != null) {
                throw new WsdlParseException("Global element can not use ref: " + e.getRef());
            }
        }
    }

    public java.util.Collection<Schema> getSchemas() {
        return schemas.values();
    }

    public Element getElement(QName element) throws ConnectionException {
        Schema schema = getSchema(element);
        Element el = schema.getGlobalElement(element.getLocalPart());
        if (el == null)
            throw new ConnectionException("Unable to find element for " + element);
        return el;
    }

    private Schema getSchema(QName element) throws ConnectionException {
        Schema schema = schemas.get(element.getNamespaceURI());
        if (schema == null)
            throw new ConnectionException("Unable to find schema for element; " + element);
        return schema;
    }

    public SimpleType getSimpleTypeAllowNull(QName type) {
        Schema schema = schemas.get(type.getNamespaceURI());
        if (schema == null)
            return null;
        return schema.getSimpleType(type.getLocalPart());
    }

    public ComplexType getComplexType(QName type) throws ConnectionException {
        Schema schema = getSchema(type);
        ComplexType ct = schema.getComplexType(type.getLocalPart());
        if (ct == null)
            throw new ConnectionException("Unable to find complexType for " + type);
        return ct;
    }
}
