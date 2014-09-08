/*******************************************************************************
 * Copyright (c) 2014 Salesforce.com, inc.. All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Salesforce.com, inc. - initial API and implementation
 ******************************************************************************/
package com.salesforce.ide.wsdl2apex.core;

import com.sforce.ws.wsdl.*;

/**
 * The represents a specific callout future class, one of these is generated for each return type specified in the WSDL
 * 
 * @author sfell
 */
class CalloutFutureClass extends AClass {

    /**
     * @param definitions
     * @param typeMapper
     * @param operaton
     *            An operation that uses this class as its return type.
     * @param futureName
     *            The name of this future class, e.g. FutureBoolean
     * @param responseType
     *            The name of the type of the response element. this is typically a container type that is unwrapped.
     * @param underlyingTypeName
     *            The name of the wrapped type, e.g. Boolean
     * @param returnElementName
     *            the name of the element within the responseType that has the unwrapped return value.
     */
    CalloutFutureClass(Definitions definitions, ApexTypeMapper typeMapper, Operation operaton, String futureName,
            ApexTypeName responseType, ApexTypeName underlyingTypeName, String returnElementName) {
        super(definitions, typeMapper, futureName, "System.WebServiceCalloutFuture");
        methods.add(new ResultGetter(definitions, typeMapper, responseType, underlyingTypeName, returnElementName));
    }

    private static class ResultGetter extends AMethod {

        public ResultGetter(Definitions definitions, ApexTypeMapper typeMapper, ApexTypeName responseType,
                ApexTypeName underlyingType, String returnElementName) {
            super(definitions, typeMapper);
            this.name = "getValue";
            this.returnType = underlyingType;
            if (isVoid()) {
                statements.add("System.WebServiceCallout.endInvoke(this);");
            } else {
                final String r = responseType.getAsApex();
                statements.add(r + " response = (" + r + ")System.WebServiceCallout.endInvoke(this);");
                if (returnElementName != null) {
                    statements.add("return response." + returnElementName + ";");
                } else {
                    statements.add("return response;");
                }
            }
        }
    }
}
