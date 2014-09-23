/*******************************************************************************
 * Copyright (c) 2014 Salesforce.com, inc.. All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Salesforce.com, inc. - initial API and implementation
 ******************************************************************************/
package com.salesforce.ide.wsdl2apex.core;

import java.util.Map;

/**
 * This class describes the options to be used with a wsdl2apex execution.
 * 
 */
public class WSDL2ApexOptions {

    public static WSDL2ApexOptions newDefault(boolean asyncTrue) {
        WSDL2ApexOptions r = new WSDL2ApexOptions();
        r.setGenerateAsyncStub(asyncTrue);
        return r;
    }

    private Map<String, String> packageNamespaceMap;
    private boolean generateAsyncStub;

    public WSDL2ApexOptions setPackageNamespaceMap(Map<String, String> nsMap) {
        this.packageNamespaceMap = nsMap;
        return this;
    }

    public WSDL2ApexOptions setGenerateAsyncStub(boolean async) {
        this.generateAsyncStub = async;
        return this;
    }

    public Map<String, String> getPackageNamespaceMap() {
        return this.packageNamespaceMap;
    }

    public boolean shouldGenerateSyncStub() {
        return this.generateAsyncStub;
    }
}
