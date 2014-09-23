/*******************************************************************************
 * Copyright (c) 2014 Salesforce.com, inc.. All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Salesforce.com, inc. - initial API and implementation
 ******************************************************************************/
package com.salesforce.ide.wsdl2apex.core;

import java.io.IOException;

public class WSDL2ApexGenerator {

    public static void main(String[] args) throws RuntimeException, CalloutException, IOException {
        WSDL2Apex w = new WSDL2Apex();
        w.parseAndGenerate(args);
    }

}
