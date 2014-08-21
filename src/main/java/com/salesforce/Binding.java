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
import java.util.Iterator;
import java.util.HashSet;
import java.util.HashMap;

/**
 * This class represents WSDL->Definitions->Binding
 * 
 * @author http://cheenath.com
 * @version 1.0
 * @since 1.0 Nov 5, 2005
 */
public class Binding extends WsdlNode {
    private Definitions definitions;
    private HashMap<QName, BindingOperation> operations = new HashMap<QName, BindingOperation>();
    private String name;

    public Binding(Definitions definitions) {
        this.definitions = definitions;
    }

    public String getName() {
        return name;
    }

    public Iterator<Part> getAllHeaders() throws ConnectionException {
        HashSet<Part> headers = new HashSet<Part>();

        for (BindingOperation operation : operations.values()) {
            addHeaders(operation.getInput().getHeaders(), headers);
            addHeaders(operation.getOutput().getHeaders(), headers);
        }
        return headers.iterator();
    }

    private void addHeaders(Iterator<SoapHeader> hs, HashSet<Part> headers) throws ConnectionException {
        while (hs.hasNext()) {
            SoapHeader h = hs.next();
            Message message = definitions.getMessage(h.getMessage());
            Part part = message.getPart(h.getPart());
            if (!headers.contains(part)) {
                headers.add(part);
            }
        }
    }

    public BindingOperation getOperation(QName name) throws ConnectionException {
        BindingOperation op = operations.get(name);
        if (op == null) {
            throw new ConnectionException("Unable to find binding operation for " + name);
        }
        return op;
    }

    void read(WsdlParser parser) throws WsdlParseException {
        name = parser.getAttributeValue(null, NAME);

        int eventType = parser.getEventType();

        while (true) {
            if (eventType == XmlInputStream.START_TAG) {
                String name = parser.getName();
                String namespace = parser.getNamespace();

                if (name != null && namespace != null) {
                    parse(name, namespace, parser);
                }
            } else if (eventType == XmlInputStream.END_TAG) {
                String name = parser.getName();
                String namespace = parser.getNamespace();

                if (BINDING.equals(name) && WSDL_NS.equals(namespace)) {
                    return;
                }
            }

            eventType = parser.next();
        }
    }

    private void parse(String name, String namespace, WsdlParser parser) throws WsdlParseException {
        if (WSDL_NS.equals(namespace)) {
            if (OPERATION.equals(name)) {
                BindingOperation operation = new BindingOperation(definitions);
                operation.read(parser);
                operations.put(operation.getQName(), operation);
            }
        } else if (WSDL_SOAP_NS.equals(namespace)) {
            if (BINDING.equals(name)) {
                String style = parser.getAttributeValue(null, STYLE);
                String transport = parser.getAttributeValue(null, TRANSPORT);

                if (style != null && !"document".equals(style)) {
                    throw new WsdlParseException("Unsupported WSDL style '" + style
                            + "'. Only supports Dcoument/literal/wrapped services. " + parser.getPositionDescription());
                }
                if (transport != null && !"http://schemas.xmlsoap.org/soap/http".equals(transport)) {
                    throw new WsdlParseException("Unsupported transport " + transport + " "
                            + parser.getPositionDescription());
                }
            }
        }
    }
}
