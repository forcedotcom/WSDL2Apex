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

import java.io.InputStream;
import java.io.IOException;

/**
 * WsdlParser
 * 
 * @author http://cheenath.com
 * @version 1.0
 * @since 1.0 Jan 20, 2006
 */
public class WsdlParser {

    private XmlInputStream in;

    public WsdlParser(XmlInputStream in) {
        this.in = in;
    }

    public void setInput(InputStream inputStream, String inputEncoding) throws WsdlParseException {
        try {
            in.setInput(inputStream, inputEncoding);
        } catch (PullParserException e) {
            throw new WsdlParseException("Failed to set input", e);
        }
    }

    public String getNamespace(String prefix) {
        return in.getNamespace(prefix);
    }

    public String getPositionDescription() {
        return in.getLineNumber() + ":" + in.getColumnNumber();
    }

    public String getNamespace() {
        return in.getNamespace();
    }

    public String getName() {
        return in.getName();
    }

    public String getAttributeValue(String namespace, String name) {
        return in.getAttributeValue(namespace, name);
    }

    public int getEventType() throws WsdlParseException {
        try {
            return in.getEventType();
        } catch (ConnectionException e) {
            throw new WsdlParseException(e);
        }
    }

    public int next() throws WsdlParseException {
        try {
            return in.next();
        } catch (IOException e) {
            throw new WsdlParseException(e);
        } catch (ConnectionException e) {
            throw new WsdlParseException(e);
        }
    }

    @Override
    public String toString() {
        return "WsdlParser: " + in.toString();
    }

    public String nextText() throws WsdlParseException {
        try {
            return in.nextText();
        } catch (IOException e) {
            throw new WsdlParseException(e);
        } catch (ConnectionException e) {
            throw new WsdlParseException(e);
        }
    }

    public int nextTag() throws WsdlParseException {
        try {
            return in.nextTag();
        } catch (IOException e) {
            throw new WsdlParseException(e);
        } catch (ConnectionException e) {
            throw new WsdlParseException(e);
        }
    }

    public int peekTag() throws WsdlParseException {
        try {
            return in.peekTag();
        } catch (ConnectionException e) {
            throw new WsdlParseException(e);
        } catch (IOException e) {
            throw new WsdlParseException(e);
        }
    }
}
