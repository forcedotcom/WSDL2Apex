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
import java.util.HashMap;
import java.util.Iterator;

/**
 * This class represents a WSDL->definitions
 * 
 * @author http://cheenath.com
 * @version 1.0
 * @since 1.0 Nov 5, 2005
 */
public class Definitions extends WsdlNode {

    private Types types;
    private HashMap<QName, Message> messages = new HashMap<QName, Message>();
    private String targetNamespace;
    private PortType portType;
    private Service service;
    private Binding binding;

    public Types getTypes() {
        return types;
    }

    public String getTargetNamespace() {
        return targetNamespace;
    }

    public PortType getPortType() {
        return portType;
    }

    public Binding getBinding() throws ConnectionException {
        QName name = service.getPort().getBinding();
        if (binding.getName().equals(name.getLocalPart()) && targetNamespace.equals(name.getNamespaceURI())) {
            return binding;
        } else {
            throw new ConnectionException("Unable to find binding " + name + ". Found " + binding.getName()
                    + " instead.");
        }
    }

    public Service getService() {
        return service;
    }

    public Message getMessage(QName name) throws ConnectionException {
        Message message = messages.get(name);
        if (message == null) {
            throw new ConnectionException("No message found for:" + name);
        }
        return message;
    }

    void read(WsdlParser parser) throws WsdlParseException {

        int eventType = parser.getEventType();

        while (eventType != XmlInputStream.END_DOCUMENT) {
            if (eventType == XmlInputStream.START_DOCUMENT) {

            } else if (eventType == XmlInputStream.START_TAG) {
                String name = parser.getName();
                String namespace = parser.getNamespace();

                if (name != null && namespace != null) {
                    parse(name, namespace, parser);
                }
            } else if (eventType == XmlInputStream.END_TAG) {} else if (eventType == XmlInputStream.TEXT) {}

            eventType = parser.next();
        }

        if (targetNamespace == null) {
            throw new WsdlParseException("targetNamespace not specified in wsdl:definitions ");
        }

        if (binding == null) {
            throw new WsdlParseException("Unable to find wsdl:binding in the specified wsdl");
        }

        if (portType == null) {
            throw new WsdlParseException("Unable to find wsdl:portType in the specified wsdl");
        }

        if (service == null) {
            throw new WsdlParseException("Unable to find wsdl:service in the specified wsdl");
        }

        try {
            updateHeaderTypes();
        } catch (ConnectionException e) {
            throw new WsdlParseException("Failed to parse WSDL: " + e.getMessage(), e);
        }
    }

    private void updateHeaderTypes() throws ConnectionException {
        Iterator<Part> headers = getBinding().getAllHeaders();

        while (headers.hasNext()) {
            Part part = headers.next();
            QName el = part.getElement();
            if (getTypes() != null) {
                Element element = getTypes().getElement(el);
                if (element.isComplexType()) {
                    ComplexType ct = getTypes().getComplexType(element.getType());
                    ct.setHeader(true);
                } else {
                    //no need to set header type for simple types
                }
            }
        }
    }

    private void parse(String name, String namespace, WsdlParser parser) throws WsdlParseException {

        if (WSDL_NS.equals(namespace)) {
            if (DEFINITIONS.equals(name)) {
                targetNamespace = parser.getAttributeValue(null, TARGET_NAME_SPACE);
            } else if (TYPES.equals(name)) {
                types = new Types();
                types.read(parser);
            } else if (MESSAGE.equals(name)) {
                Message message = new Message(targetNamespace);
                message.read(parser);
                messages.put(message.getName(), message);
            } else if (PORT_TYPE.equals(name)) {
                if (portType != null) {
                    throw new WsdlParseException("Found more than one wsdl:portType. "
                            + "WSDL with multiple portType not supported");
                }
                portType = new PortType(this);
                portType.read(parser);
            } else if (BINDING.equals(name)) {
                if (binding != null) {
                    throw new WsdlParseException("Found more than one wsdl:binding. "
                            + "WSDL with multiple binding not supported");
                }
                binding = new Binding(this);
                binding.read(parser);
            } else if (SERVICE.equals(name)) {
                if (service != null) {
                    throw new WsdlParseException("Found more than one wsdl:service. "
                            + "WSDL with multiple service not supported");
                }
                service = new Service();
                service.read(parser);
            } else if (DOCUMENTATION.equals(name)) {
                new Documentation().read(parser);
            } else {
                throw new WsdlParseException("Unknown element: " + name);
            }
        }
    }

    @Override
    public String toString() {
        return "Definitions{" + "types=" + types + ", messages=" + messages + ", targetNamespace='" + targetNamespace
                + '\'' + ", portType=" + portType + ", service=" + service + '}';
    }
}
