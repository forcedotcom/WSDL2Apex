/*******************************************************************************
 * Copyright (c) 2014 Salesforce.com, inc..
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Salesforce.com, inc. - initial API and implementation
 ******************************************************************************/
package com.salesforce.ide.wsdl2apex.core;

/**
 * AField
 * 
 * @author cheenath
 * @version 1.0
 */
class AField {

    private final String type;
    private final String name;
    private final String access;
    private final String value;

    AField(String access, String type, String name, String value) {
        this.access = access;
        this.type = type;
        this.name = name;
        this.value = value;
    }

    boolean isPublic() {
        return "public".equals(access);
    }

    void write(AWriter writer) throws CalloutException {
        writer.startBlock();
        if (value == null) {
            writer.writeLine(access, " ", type, " ", name, ";");
        } else {
            writer.writeLine(access, " ", type, " ", name, " = ", value, ";");
        }
        writer.endBlock();
    }
}
