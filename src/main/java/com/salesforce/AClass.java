/*******************************************************************************
 * Copyright (c) 2014 Salesforce.com, inc.. All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Salesforce.com, inc. - initial API and implementation
 ******************************************************************************/
package com.salesforce.ide.wsdl2apex.core;

import java.util.ArrayList;

import com.sforce.ws.wsdl.Definitions;

/**
 * AClass
 * 
 * @author cheenath
 * @version 1.0
 */
abstract class AClass extends ABase {

    protected final String name;
    protected final String extendsClass;
    protected final ArrayList<AField> fields = new ArrayList<AField>();
    protected final ArrayList<AMethod> methods = new ArrayList<AMethod>();

    protected AClass(Definitions definitions, ApexTypeMapper typeMapper, String name, String extendsClass) {
        super(definitions, typeMapper);
        this.name = name;
        this.extendsClass = extendsClass;
    }

    String getName() {
        return name;
    }

    void write(AWriter writer) throws CalloutException {
        writer.startBlock();
        if (extendsClass == null)
            writer.writeLine("public class ", name, " {");
        else
            writer.writeLine("public class ", name, " extends ", extendsClass, " {");

        for (AField field : fields) {
            if (field.isPublic()) {
                field.write(writer);
            }
        }
        for (AField field : fields) {
            if (!field.isPublic()) {
                field.write(writer);
            }
        }
        for (AMethod method : methods) {
            method.write(writer);
        }
        writer.writeLine("}");
        writer.endBlock();
    }
}
