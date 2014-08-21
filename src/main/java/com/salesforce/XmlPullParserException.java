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

/* -*- c-basic-offset: 4; indent-tabs-mode: nil; -*- //------100-columns-wide------>| */
// for license please see accompanying LICENSE.txt file (available also at http://www.xmlpull.org/)

/**
 * This exception is thrown to signal XML Pull Parser related faults.
 * 
 * @author <a href="http://www.extreme.indiana.edu/~aslom/">Aleksander Slominski</a>
 */
public class XmlPullParserException extends PullParserException {
    /**
	 * 
	 */
    protected int row = -1;
    protected int column = -1;

    public XmlPullParserException(String s) {
        super(s);
    }

    public XmlPullParserException(String msg, XmlPullParser parser, Throwable chain) {
        super((msg == null ? "" : msg + " ")
                + (parser == null ? "" : "(position:" + parser.getPositionDescription() + ") ")
                + (chain == null ? "" : "caused by: " + chain), chain);

        if (parser != null) {
            this.row = parser.getLineNumber();
            this.column = parser.getColumnNumber();
        }
    }

    public int getLineNumber() {
        return row;
    }

    public int getColumnNumber() {
        return column;
    }
}
