package com.salesforce.ide.wsdl2apex.core;

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
