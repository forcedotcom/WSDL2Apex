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
 */
public class BindingOperation extends WsdlNode {
    private Definitions definitions;
    private QName name;
    private BindingMessage input;
    private BindingMessage output;
    private String soapAction = "";

    public BindingOperation(Definitions definitions) {
        this.definitions = definitions;
    }

    public QName getName() {
        return name;
    }

    public BindingMessage getInput() {
        return input;
    }

    public BindingMessage getOutput() {
        return output;
    }

    public Operation getOperation() {
        return definitions.getPortType().getOperation(name);
    }

    public String getSoapAction() {
        return soapAction;
    }

    public QName getQName() {
        return name;
    }

    void read(WsdlParser parser) throws WsdlParseException {
        name = new QName(definitions.getTargetNamespace(), parser.getAttributeValue(null, NAME));
        int eventType = parser.getEventType();

        while (true) {
            if (eventType == XmlInputStream.START_TAG) {
                String n = parser.getName();
                String ns = parser.getNamespace();

                if (n != null && ns != null) {
                    parse(n, ns, parser);
                }
            } else if (eventType == XmlInputStream.END_TAG) {
                String name = parser.getName();
                String namespace = parser.getNamespace();

                if (OPERATION.equals(name) && WSDL_NS.equals(namespace)) {
                    break;
                }
            }

            eventType = parser.next();
        }

        if (input == null) {
            throw new WsdlParseException("input not defined in binding operation '" + name + "'");
        }
        if (output == null) {
            throw new WsdlParseException("output not defined in binding operation '" + name + "'");
        }
    }

    private void parse(String name, String namespace, WsdlParser parser) throws WsdlParseException {

        if (WSDL_NS.equals(namespace)) {
            if (INPUT.equals(name)) {
                input = new BindingMessage(definitions, INPUT);
                input.read(parser);
            } else if (OUTPUT.equals(name)) {
                output = new BindingMessage(definitions, OUTPUT);
                output.read(parser);
            } else if (FAULT.equals(name)) {
                //todo: parse binding fault
            }
        } else if (WSDL_SOAP_NS.equals(namespace)) {
            if (OPERATION.equals(name)) {
                String sa = parser.getAttributeValue(null, "soapAction");
                if (sa != null) {
                    soapAction = sa;
                }
                String style = parser.getAttributeValue(null, "style");
                if (style != null) {
                    if (!"document".equals(style)) {
                        throw new WsdlParseException("Unsupported style " + style);
                    }
                }
            }
        }
    }

}
