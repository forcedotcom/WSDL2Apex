package com.salesforce.ide.wsdl2apex.core;

import javax.xml.namespace.QName;

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
