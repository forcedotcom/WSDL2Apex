/*******************************************************************************
 * Copyright (c) 2014 Salesforce.com, inc.. All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Salesforce.com, inc. - initial API and implementation
 ******************************************************************************/
package com.salesforce.ide.wsdl2apex.core;

import java.util.Iterator;

import com.sforce.ws.ConnectionException;
import com.sforce.ws.wsdl.*;

/**
 * This is a base class for generating binding aka client stubs in apex. There are concrete versions of this that
 * generate a traditional synchronous stub and a version that generates an async/continuations based stub.
 */
abstract class BindingClass extends AClass {

    BindingClass(Definitions definitions, ApexTypeMapper typeMapper, String name) {
        super(definitions, typeMapper, name, null);
    }

    protected enum CertOptions {
        IncludeDeprecatedFields, SkipDeprecatedFields;
    }

    protected enum OutputHttpHeadersOptions {
        IncludeHeaderMap, SkipHeaderMap;
    }

    protected void loadStub(APackage pkg, Binding binding, CertOptions certOptions, OutputHttpHeadersOptions httpOptions)
            throws CalloutException, ConnectionException {
        String loc = getLocation(definitions);
        AField endpoint = new AField("public", "String", CalloutConstants.ENDPOINT, "'" + loc + "'");
        fields.add(endpoint);

        AField inputHttpHeaders = new AField("public", "Map<String,String>", CalloutConstants.INPUT_HTTP_HEADERS, null);
        fields.add(inputHttpHeaders);

        if (httpOptions == OutputHttpHeadersOptions.IncludeHeaderMap) {
            AField outputHttpHeaders =
                    new AField("public", "Map<String,String>", CalloutConstants.OUTPUT_HTTP_HEADERS, null);
            fields.add(outputHttpHeaders);
        }

        AField clientCertName = new AField("public", "String", CalloutConstants.CLIENT_CERT_NAME, null);
        fields.add(clientCertName);

        if (certOptions == CertOptions.IncludeDeprecatedFields) {

            AField clientCert = new AField("public", "String", CalloutConstants.CLIENT_CERT, null);
            fields.add(clientCert);
            AField clientCertPasswd = new AField("public", "String", CalloutConstants.CLIENT_CERT_PASSWD, null);
            fields.add(clientCertPasswd);
        }

        AField timeout = new AField("public", "Integer", CalloutConstants.TIMEOUT, null);
        fields.add(timeout);

        addHeaders(binding);

        Iterator<Operation> operations = definitions.getPortType().getOperations();
        while (operations.hasNext()) {
            Operation operation = operations.next();
            addOperation(pkg, binding, operation);
        }

        fields.add(new AField("private", "String[]", "ns_map" + CalloutConstants.TYPE_INFO_PREFIX, nsMap()));
    }

    protected abstract void addOperation(APackage pkg, Binding binding, Operation operation)
            throws ConnectionException, CalloutException;

    private void addHeaders(Binding binding) throws ConnectionException, CalloutException {
        Iterator<Part> it = binding.getAllHeaders();
        while (it.hasNext()) {
            Part part = it.next();
            ComplexType type = getType(part);
            String apextype = getApexType(type).getAsApex();
            fields.add(new AField("public", apextype, typeMapper.getSafeName(part.getName()), null));

            String headerNS = "'" + part.getElement().getLocalPart() + "=" + part.getElement().getNamespaceURI() + "'";

            fields.add(new AField("private", "String", typeMapper.getSafeName(part.getName()) + "_hns", headerNS));
        }
    }

    private String getLocation(Definitions definitions) throws CalloutException {
        Service service = definitions.getService();
        if (service == null)
            throw new CalloutException("Unable to find wsdl:service");
        Port port = service.getPort();
        if (port == null)
            throw new CalloutException("Unable to find wsdl:port");
        SoapAddress address = port.getSoapAddress();
        if (address == null)
            throw new CalloutException("Unable to find soap 1.1 address");
        String loc = address.getLocation();
        if (loc == null)
            throw new CalloutException("Unable to find endpoint location");
        return loc;
    }

    private String nsMap() throws CalloutException {
        Iterator<String> it = typeMapper.getPackageNamespaces();
        StringBuilder sb = new StringBuilder();
        sb.append("new String[]{");
        while (it.hasNext()) {
            String ns = it.next();
            String pkg = typeMapper.getPackageName(ns);
            sb.append("'").append(ns).append("', '").append(pkg).append("'");
            if (it.hasNext()) {
                sb.append(", ");
            }
        }
        sb.append("}");

        return sb.toString();
    }
}
