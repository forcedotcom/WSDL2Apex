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
 * This represents the begin_x method in the async stub, there's one of these for each operation in the wsdl.
 * 
 */
class BeginAsyncOperationMethod extends OperationMethod {

    BeginAsyncOperationMethod(Definitions definitions, ApexTypeMapper typeMapper, Operation operation, Binding binding)
            throws ConnectionException, CalloutException {
        super(definitions, typeMapper, operation, binding);
    }

    @Override
    protected String getOperationName() throws CalloutException {
        String OperationName = new String(super.getOperationName());
        StringBuilder result = new StringBuilder(OperationName.length()).append(OperationName);
        result.setCharAt(0, Character.toUpperCase(OperationName.charAt(0)));

        return "begin" + result.toString();
    }

    @Override
    protected ApexTypeName getReturnType() throws CalloutException, ConnectionException {
        ApexTypeName responseType = super.getResponseType();

        String packageName = new String(responseType.getPacakageName());
        StringBuilder result = new StringBuilder(packageName.length()).append(packageName);
        result.setCharAt(0, Character.toUpperCase(packageName.charAt(0)));

        String asyncPackageName = responseType.hasPackageName() ? "Async" + result.toString() : null;
        String futureClassName = responseType.getJustTypeName() + "Future";
        return new ApexTypeName(asyncPackageName, futureClassName, false);
    }

    /**
     * @return the return type without the Future wrapper, e.g. the apex mapped version of the WSDL type, possibly
     *         unwrapped
     */
    ApexTypeName getRawReturnType() throws CalloutException, ConnectionException {
        return super.getReturnType();
    }

    @Override
    protected void loadParameters() throws CalloutException, ConnectionException {
        // insert the additional Continuation parameter as the first param to the method
        AParameter cp = new AParameter("continuation", new ApexTypeName("System", "Continuation", false));
        this.parameters.add(cp);
        super.loadParameters();
    }

    @Override
    protected void declareReturnVariables() throws ConnectionException, CalloutException {
        // nothing to do
    }

    @Override
    protected void performInvoke(BindingOperation boperation) throws ConnectionException, CalloutException {
        statements.add("return (" + getReturnType() + ") System.WebServiceCallout.beginInvoke(");
        statements.add("  this,");
        statements.add("  request_x,");
        statements.add("  " + getReturnType() + ".class,");
        statements.add("  continuation,");
        addOperationMetadataInvokeParams(boperation);
        statements.add(");");
    }

    @Override
    protected void mapResult() throws CalloutException, ConnectionException {
        // nothing to do, performInvoke already did the return;
    }
}
