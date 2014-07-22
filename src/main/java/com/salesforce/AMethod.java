package com.salesforce.ide.wsdl2apex.core;

import java.util.Iterator;
import java.util.ArrayList;

abstract class AMethod extends ABase {
    
    private static final ApexTypeName VOID = new ApexTypeName(null, "void", false);
    
    protected String name;
    protected ApexTypeName returnType;
    protected final ArrayList<AParameter> parameters = new ArrayList<AParameter>();
    protected final ArrayList<String> statements = new ArrayList<String>();

    AMethod(Definitions definitions, ApexTypeMapper typeMapper) {
        super(definitions, typeMapper);
    }

    private String parametersToString() {
        StringBuilder sb = new StringBuilder();
        Iterator<AParameter> it = parameters.iterator();
        while(it.hasNext()) {
            AParameter parameter = it.next();
            sb.append(parameter.getType().getAsApex()).append(" ");
            sb.append(parameter.getName());
            if (it.hasNext()) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

    boolean isVoid() {
        return VOID.equals(returnType);
    }
    
    void write(AWriter writer) throws CalloutException {
        writer.startBlock();
        writer.writeLine("public ", returnType.getAsApex(), " ", name, "(", parametersToString(), ") {");
        writer.startBlock();
        for (String statement : statements) {
            writer.writeLine(statement);
        }
        writer.endBlock();
        writer.writeLine("}");
        writer.endBlock();
    }
}
