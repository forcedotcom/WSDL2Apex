package com.salesforce.ide.wsdl2apex.core;
/*
 * Copyright, 1999-2013, salesforce.com
 * All Rights Reserved
 * Company Confidential
 */

import java.util.Map;

/**
 * This class describes the options to be used with a wsdl2apex execution.
 *
 */
public class Wsdl2ApexOptions {

    
    public static Wsdl2ApexOptions newDefault(boolean asyncTrue)
    {
    	Wsdl2ApexOptions r = new Wsdl2ApexOptions();
    	r.setGenerateAsyncStub(asyncTrue);
    	return r;
    }
    
    private Map<String, String> packageNamespaceMap;
    private boolean generateAsyncStub;

    public Wsdl2ApexOptions setPackageNamespaceMap(Map<String, String> nsMap) {
        this.packageNamespaceMap = nsMap;
        return this;
    }
    
    public Wsdl2ApexOptions setGenerateAsyncStub(boolean async) {
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
