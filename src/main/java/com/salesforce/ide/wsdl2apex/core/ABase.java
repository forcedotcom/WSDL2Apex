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

import javax.xml.namespace.QName;

/**
 * ABase
 *
 * @author cheenath
 * @version 1.0
 */
abstract class ABase {

    protected final Definitions definitions;
    protected final ApexTypeMapper typeMapper;

    ABase(Definitions definitions, ApexTypeMapper typeMapper) {
        this.definitions = definitions;
        this.typeMapper = typeMapper;
    }

    protected Element getElement(Part part) throws ConnectionException {
        Types types = getTypes(part);
        return types.getElement(part.getElement());
    }

    private Types getTypes(Part part) throws ConnectionException {
        Types types = definitions.getTypes();

        if (types == null) {
            throw new ConnectionException("Unable to find Schema type for " + part.getName());
        }

        return types;
    }

    protected ComplexType getType(Part part) throws ConnectionException {
        Types types = getTypes(part);

        Element element = types.getElement(part.getElement());
        QName type = element.getType();

        if (type == null) {
            throw new ConnectionException("Unable to find schema type for element " + element.getName());
        }

        return types.getComplexType(type);
    }

    ApexTypeName getApexType(ComplexType ct) throws CalloutException {
        QName type = new QName(ct.getSchema().getTargetNamespace(), ct.getName());
        return typeMapper.getApexType(type, definitions);
    }
}
