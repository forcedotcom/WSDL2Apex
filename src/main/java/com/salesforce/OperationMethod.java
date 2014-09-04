/*******************************************************************************
 * Copyright (c) 2014 Salesforce.com, inc.. All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Salesforce.com, inc. - initial API and implementation
 ******************************************************************************/
package com.salesforce.ide.wsdl2apex.core;

import java.util.*;

import com.sforce.ws.ConnectionException;
import com.sforce.ws.wsdl.*;
import com.sforce.ws.wsdl.Collection;

/**
 * Represent a method to be generated in a stub that represents an operation (aka call) from the WSDL.
 * 
 */
class OperationMethod extends AMethod {

    OperationMethod(Definitions definitions, ApexTypeMapper typeMapper, Operation operation, Binding binding)
            throws ConnectionException, CalloutException {
        super(definitions, typeMapper);
        this.operation = operation;
        this.binding = binding;
        load();
    }

    protected final Operation operation;
    protected final Binding binding;
    private boolean unWrapOutParams = true;

    /** @return the name of the apex method for this operation */
    protected String getOperationName() throws CalloutException {
        return typeMapper.getSafeName(operation.getName().getLocalPart());
    }

    /** @return the name of the apex type that this operation returns */
    protected ApexTypeName getReturnType() throws CalloutException, ConnectionException {
        return unwrapResponseType();
    }

    /**
     * This maps the wsdl operation & binding into apex types, and generates each segment of the method, the generated
     * method is broken up in segments loadParameters() - generate the apex parameters needed to this method, typically
     * based on the input structure to the operation in the WSDL. mapParameters() - generates the code to map from the
     * apex method parameters into the structure to pass to WebServiceCall.invoke declareReturnVariables() - generates
     * any code need to prepare for a result before the invoke call is made. performInvoke() - generate the
     * WebServiceCallout.invoke() call that passes all the relevant data back into the runtime mapResult() - generate
     * the code that does anything needed to get the result from the runtime and to be the method return value.
     */
    protected void load() throws ConnectionException, CalloutException {
        BindingOperation boperation = binding.getOperation(operation.getName());
        this.name = getOperationName();
        this.returnType = getReturnType();

        loadParameters();
        ApexTypeName requestType = getRequestType();
        mapParameters(requestType);
        declareReturnVariables();
        performInvoke(boperation);
        mapResult();
    }

    /** create any needed apex parameters to this method for this operation */
    protected void loadParameters() throws CalloutException, ConnectionException {
        toArgs(argElements(operation));
    }

    boolean shouldUnwrapOutParams() throws ConnectionException {
        getResponseElement(); // ensure that unWrap has been calculated.
        return unWrapOutParams;
    }

    /**
     * This adds the code to build the request object / types info from the method parameters to what can be passed to
     * WebServiceCallout.invoke*
     */
    protected void mapParameters(ApexTypeName requestType) throws ConnectionException, CalloutException {
        statements.add(requestType.getAsApex() + " request_x = new " + requestType.getAsApex() + "();");

        Iterator<Element> elements = argElements(operation);
        while (elements.hasNext()) {
            Element element = elements.next();
            String argName = typeMapper.getSafeName(element.getName());
            statements.add("request_x." + argName + " = " + argName + ";");
        }
    }

    /** This should add any statements needed to declare/setup return parameters before the invoke call is made */
    protected void declareReturnVariables() throws ConnectionException, CalloutException {
        statements.add(getResponseType() + " response_x;");
        statements.add("Map<String, " + getResponseType() + "> response_map_x = " + "new Map<String, "
                + getResponseType() + ">();");

        statements.add("response_map_x.put('response_x', response_x);");
    }

    protected void performInvoke(BindingOperation boperation) throws ConnectionException, CalloutException {
        statements.add("WebServiceCallout.invoke(");
        statements.add("  this,");
        statements.add("  request_x,");
        statements.add("  response_map_x,");
        addOperationMetadataInvokeParams(boperation);
        statements.add(");");
    }

    /** This should add statements to extract the final return value and return it */
    protected void mapResult() throws CalloutException, ConnectionException {
        statements.add("response_x = response_map_x.get('response_x');");

        if (!isVoid()) {
            if (unWrapOutParams) {
                String returnElementName = typeMapper.getSafeName(getResponseElement().getName());
                statements.add("return response_x." + returnElementName + ";");
            } else {
                statements.add("return response_x;");
            }
        }
    }

    protected void addOperationMetadataInvokeParams(BindingOperation boperation) throws ConnectionException,
            CalloutException {
        Part request = getSinglePart(operation.getInput());
        Part response = getSinglePart(operation.getOutput());

        statements.add("  new String[]{endpoint_x,");
        statements.add("  '" + boperation.getSoapAction() + "',");
        statements.add("  '" + request.getElement().getNamespaceURI() + "',");
        statements.add("  '" + request.getElement().getLocalPart() + "',");
        statements.add("  '" + response.getElement().getNamespaceURI() + "',");
        statements.add("  '" + response.getElement().getLocalPart() + "',");
        statements.add("  '" + getResponseType() + "'}");
    }

    private static final ApexTypeName VOID = new ApexTypeName(null, "void", false);

    protected ApexTypeName unwrapResponseType() throws CalloutException, ConnectionException {
        Element el = getResponseElement();
        return el == null ? VOID : typeMapper.getApexType(el, definitions);
    }

    protected Element getResponseElement() throws ConnectionException {
        ComplexType ct = getType(operation.getOutput());
        Collection sequence = ct.getContent();
        if (sequence == null) { //  <complexType/>
            return null;
        }
        Iterator<Element> eit = sequence.getElements();

        Element result;
        if (eit.hasNext()) {
            result = eit.next();
            if (eit.hasNext()) {
                unWrapOutParams = false;
                return getElement(getSinglePart(operation.getOutput()));
            }
        } else {
            result = null; // <complexType><sequence/><complexType>
        }

        return result;
    }

    private ApexTypeName getRequestType() throws ConnectionException, CalloutException {
        ComplexType ct = getType(operation.getInput());
        return getApexType(ct);
    }

    ApexTypeName getResponseType() throws ConnectionException, CalloutException {
        ComplexType ct = getType(operation.getOutput());
        return getApexType(ct);
    }

    /*move this to wsdl message */
    private ComplexType getType(Message message) throws ConnectionException {
        Part part = getSinglePart(message);
        return getType(part);
    }

    private Part getSinglePart(Message message) throws ConnectionException {
        if (message == null) {
            throw new ConnectionException("Unsupported WSDL. Found operation without input/output message");
        }
        Iterator<Part> it = message.getParts();
        if (!it.hasNext()) {
            throw new ConnectionException("Unsupported WSDL. No part found for message: "
                    + message.getName().getLocalPart());
        }
        Part part = it.next();
        if (it.hasNext()) {
            throw new ConnectionException("Unsupported WSDL. Found more than one part for message "
                    + message.getName().getLocalPart());
        }
        return part;
    }

    private Iterator<Element> argElements(Operation operation) throws CalloutException {
        ComplexType ct;
        try {
            ct = getType(operation.getInput());
        } catch (ConnectionException e) {
            throw new CalloutException("Failed to get type for operation '" + operation.getName() + "' due to "
                    + e.getMessage(), e);
        }
        Collection sequence = ct.getContent();
        return (sequence == null) ? Collections.<Element> emptyList().iterator() : sequence.getElements();
    }

    private void toArgs(Iterator<Element> eit) throws CalloutException, ConnectionException {
        while (eit.hasNext()) {
            Element el = eit.next();
            ApexTypeName type = typeMapper.getApexType(el, definitions);
            String name = typeMapper.getSafeName(el.getName());
            AParameter parameter = new AParameter(name, type);
            parameters.add(parameter);
        }
    }
}
