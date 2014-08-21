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
 * This class represents a WSDL definitions->portType
 * 
 * @author http://cheenath.com
 * @version 1.0
 * @since 1.0 Nov 5, 2005
 */
public class PortType extends WsdlNode {
    private HashMap<QName, Operation> operations = new HashMap<QName, Operation>();
    private Definitions definitions;

    public PortType(Definitions definitions) {
        this.definitions = definitions;
    }

    public Iterator<Operation> getOperations() {
        return operations.values().iterator();
    }

    public Operation getOperation(QName name) {
        return operations.get(name);
    }

    void read(WsdlParser parser) throws WsdlParseException {

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

                if (PORT_TYPE.equals(name) && WSDL_NS.equals(namespace)) {
                    return;
                }
            }

            eventType = parser.next();
        }
    }

    private void parse(String name, String namespace, WsdlParser parser) throws WsdlParseException {

        if (WSDL_NS.equals(namespace)) {

            if (OPERATION.equals(name)) {
                Operation operation = new Operation(definitions);
                operation.read(parser);
                operations.put(operation.getName(), operation);
            }
        }
    }

    static class MessageRef extends WsdlNode {
        private QName message;

        public MessageRef(QName message) {
            this.message = message;
        }

        public QName getMessage() {
            return message;
        }
    }
}
