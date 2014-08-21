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
 * This class represents WSDL->definitions->types->schema->complexType->sequence
 * 
 * @author http://cheenath.com
 * @version 1.0
 * @since 1.0 Nov 9, 2005
 */
public class Restriction implements Constants, Iterable<Enumeration> {

    private ArrayList<Enumeration> enumerations = new ArrayList<Enumeration>();
    private QName base;
    private Schema schema;

    public Restriction(Schema schema) {
        this.schema = schema;
    }

    public Iterator<Enumeration> getElements() {
        return enumerations.iterator();
    }

    @Override
    public Iterator<Enumeration> iterator() {
        return enumerations.iterator();
    }

    public int getNumEnumerations() {
        return enumerations.size();
    }

    @Override
    public String toString() {
        return "Restriction{" + "enumerations=" + enumerations + '}';
    }

    public QName getBase() {
        return this.base;
    }

    void read(WsdlParser parser) throws WsdlParseException {
        String baseLocal = parser.getAttributeValue(null, BASE);
        base = baseLocal != null ? ParserUtil.toQName(baseLocal, parser) : null;

        int eventType = parser.getEventType();

        while (true) {
            if (eventType == XmlInputStream.START_TAG) {
                String name = parser.getName();
                String namespace = parser.getNamespace();

                if (ENUMERATION.equals(name) && SCHEMA_NS.equals(namespace)) {
                    Enumeration enumeration = new Enumeration(schema);
                    enumeration.read(parser);
                    enumerations.add(enumeration);
                }

            } else if (eventType == XmlInputStream.END_TAG) {
                String name = parser.getName();
                String namespace = parser.getNamespace();

                if (RESTRICTION.equals(name) && SCHEMA_NS.equals(namespace)) {
                    return;
                }
            } else if (eventType == XmlInputStream.END_DOCUMENT) {
                throw new WsdlParseException("Failed to find end tag for 'restriction'");
            }

            eventType = parser.next();
        }
    }
}
