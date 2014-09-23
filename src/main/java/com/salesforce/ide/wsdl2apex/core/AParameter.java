/*******************************************************************************
 * Copyright (c) 2014 Salesforce.com, inc.. All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Salesforce.com, inc. - initial API and implementation
 ******************************************************************************/
package com.salesforce.ide.wsdl2apex.core;

/**
 * AParameter
 * 
 * @author cheenath
 * @version 1.0
 */
class AParameter {

    private final String name;
    private final ApexTypeName type;

    AParameter(String name, ApexTypeName type) {
        this.name = name;
        this.type = type;
    }

    String getName() {
        return name;
    }

    ApexTypeName getType() {
        return type;
    }
}
