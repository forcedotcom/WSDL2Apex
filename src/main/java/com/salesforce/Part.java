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
 * This class represents WSDL->definitions->message
 * 
 * @author http://cheenath.com
 * @version 1.0
 * @since 1.0 Nov 5, 2005
 */
public class Part extends WsdlNode {
    private QName element;
    private String name;

    public String getName() {
        return name;
    }

    public QName getElement() throws ConnectionException {
        if (element == null) {
            throw new ConnectionException("Element not defined for part '" + name + "'");
        }
        return element;
    }

    public void read(WsdlParser parser) throws WsdlParseException {

        name = parser.getAttributeValue(null, NAME);
        if (name == null)
            throw new WsdlParseException("Unable to find name attribute in part");

        String e = parser.getAttributeValue(null, ELEMENT);
        if (e == null) {} else {
            element = ParserUtil.toQName(e, parser);
        }

        int eventType = parser.getEventType();

        while (true) {
            if (eventType == XmlInputStream.START_TAG) {} else if (eventType == XmlInputStream.END_TAG) {
                String name = parser.getName();
                String namespace = parser.getNamespace();

                if (PART.equals(name) && WSDL_NS.equals(namespace)) {
                    return;
                }
            }

            eventType = parser.next();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Part part = (Part) o;

        if (element != null ? !element.equals(part.element) : part.element != null)
            return false;
        if (name != null ? !name.equals(part.name) : part.name != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        result = (element != null ? element.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Part{" + "element=" + element + ", name='" + name + '\'' + '}';
    }
}
