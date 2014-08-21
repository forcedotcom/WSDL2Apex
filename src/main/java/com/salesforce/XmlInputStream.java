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
 * This is a minimal pull parser. It currently delegates to XPP parser available at
 * http://www.extreme.indiana.edu/xgws/xsoap/xpp/
 * 
 * @author http://cheenath.com
 * @version 1.0
 * @since 1.0 Nov 5, 2005
 */
public final class XmlInputStream {

    private MXParser parser = new MXParser();

    public static final int END_DOCUMENT = XmlPullParser.END_DOCUMENT;
    public static final int START_DOCUMENT = XmlPullParser.START_DOCUMENT;
    public static final int START_TAG = XmlPullParser.START_TAG;
    public static final int END_TAG = XmlPullParser.END_TAG;
    public static final int TEXT = XmlPullParser.TEXT;

    private static final int EMPTY = -99999;

    private int peekTag = EMPTY;

    public XmlInputStream() {
        try {
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
        } catch (XmlPullParserException e) {
            throw new InternalError("Unable to set feature:" + e);
        }
    }

    public void setInput(InputStream inputStream, String inputEncoding) throws PullParserException {
        parser.setInput(inputStream, inputEncoding);
    }

    public String getNamespace(String prefix) {
        return parser.getNamespace(prefix);
    }

    public String getPositionDescription() {
        return parser.getPositionDescription();
    }

    public int getLineNumber() {
        return parser.getLineNumber();
    }

    public int getColumnNumber() {
        return parser.getColumnNumber();
    }

    public String getNamespace() {
        return parser.getNamespace();
    }

    public String getName() {
        return parser.getName();
    }

    public String getAttributeValue(String namespace, String name) {
        return parser.getAttributeValue(namespace, name);
    }

    public int getAttributeCount() {
        return parser.getAttributeCount();
    }

    public String getAttributeValue(int index) {
        return parser.getAttributeValue(index);
    }

    public String getAttributeName(int index) {
        return parser.getAttributeName(index);
    }

    public String getAttributeNamespace(int index) {
        return parser.getAttributeNamespace(index);
    }

    public void consumePeeked() {
        peekTag = EMPTY;
    }

    public int getEventType() throws ConnectionException {
        if (peekTag != EMPTY) {
            return peekTag;
        } else {
            try {
                return parser.getEventType();
            } catch (XmlPullParserException e) {
                throw new ConnectionException("Failed to get event type", e);
            }
        }
    }

    public int next() throws IOException, ConnectionException {
        if (peekTag != EMPTY) {
            int t = peekTag;
            peekTag = EMPTY;
            return t;
        }

        try {
            return parser.next();
        } catch (XmlPullParserException e) {
            throw new ConnectionException("Found invalid XML. " + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        return parser.getPositionDescription();
    }

    public String nextText() throws IOException, ConnectionException {
        try {
            return parser.nextText();
        } catch (XmlPullParserException e) {
            throw new ConnectionException("Failed to get text", e);
        }
    }

    public String getText() {
        return parser.getText();
    }

    public int nextTag() throws IOException, ConnectionException {
        if (peekTag != EMPTY) {
            int t = peekTag;
            peekTag = EMPTY;
            return t;
        }

        try {
            return parser.nextTag();
        } catch (XmlPullParserException e) {
            throw new ConnectionException("Failed to get next element", e);
        }
    }

    public int peekTag() throws ConnectionException, IOException {
        if (peekTag != EMPTY) {
            return peekTag;
        }

        peekTag = nextTag();
        return peekTag;
    }
}
