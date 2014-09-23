/*******************************************************************************
 * Copyright (c) 2014 Salesforce.com, inc.. All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Salesforce.com, inc. - initial API and implementation
 ******************************************************************************/
package com.salesforce.ide.wsdl2apex.core;

import com.sforce.ws.ConnectionException;
import com.sforce.ws.wsdl.*;

/**
 * This generates the apex class that represents a soap binding (or Stub) that exposes a synchronous programming model
 * 
 */
class SyncBindingClass extends BindingClass {

    SyncBindingClass(APackage pkg, Binding binding, Definitions definitions, ApexTypeMapper typeMapper)
            throws ConnectionException, CalloutException {
        super(definitions, typeMapper, typeMapper.getSafeName(definitions.getService().getPort().getName()));
        loadStub(pkg, binding, CertOptions.IncludeDeprecatedFields, OutputHttpHeadersOptions.IncludeHeaderMap);
    }

    @Override
    protected void addOperation(APackage pkg, Binding binding, Operation operation) throws ConnectionException,
            CalloutException {
        OperationMethod method = new OperationMethod(definitions, typeMapper, operation, binding);
        methods.add(method);
    }
}