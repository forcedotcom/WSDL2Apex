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
 * ConnectionException is the root of all web service client exceptions
 * 
 * @author http://cheenath.com
 * @version 1.0
 * @since 1.0 Dec 1, 2005
 */
public class ConnectionException extends Exception {

    /**
	 * 
	 */

    public ConnectionException() {}

    public ConnectionException(String message) {
        super(message);
    }

    public ConnectionException(String message, Throwable th) {
        super(message, th);
    }
}
