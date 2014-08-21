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

/**
 * This exception is thrown when there is an error in parsing WSDL.
 * 
 * @author http://cheenath.com
 * @version 1.0
 * @since 1.0 Nov 5, 2005
 */
public class WsdlParseException extends Exception {

    /**
	 * 
	 */

    public WsdlParseException(Throwable th) {
        super("Parse error: " + th.getMessage(), th);
    }

    public WsdlParseException(String message) {
        super(message);
    }

    public WsdlParseException(String message, Throwable th) {
        super(message, th);
    }
}
