/*******************************************************************************
 * Copyright (c) 2014 Salesforce.com, inc.. All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Salesforce.com, inc. - initial API and implementation
 ******************************************************************************/
package com.salesforce.ide.wsdl2apex.core;

/**
 * Represents the name of an apex type.
 * 
 * @author sfell
 */

public class ApexTypeName {

    public static ApexTypeName arrayOf(ApexTypeName src) {
        return new ApexTypeName(src.getPacakageName(), src.getJustTypeName(), true);
    }

    public ApexTypeName(String packageName, String typeName, boolean isArray) {
        this.packageName = packageName;
        this.typeName = typeName;
        this.isArray = isArray;
    }

    private final String packageName, typeName;
    private final boolean isArray;
    private String asApex;

    public String getPacakageName() {
        return packageName;
    }

    public boolean hasPackageName() {
        return packageName != null;
    }

    public String getJustTypeName() {
        return typeName;
    }

    public boolean isArray() {
        return isArray;
    }

    public String getAsApex() {
        if (asApex == null) {
            String fqn = packageName == null ? typeName : packageName + "." + typeName;
            asApex = isArray ? fqn + "[]" : fqn;
        }
        return asApex;
    }

    @Override
    public String toString() {
        return getAsApex();
    }

    @Override
    public int hashCode() {
        return com.google.common.base.Objects.hashCode(typeName, packageName, isArray);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if ((obj == null) || (!(obj instanceof ApexTypeName)))
            return false;
        ApexTypeName rhs = (ApexTypeName) obj;
        return com.google.common.base.Objects.equal(typeName, rhs.typeName)
                && com.google.common.base.Objects.equal(packageName, rhs.packageName) && (isArray == rhs.isArray);
    }
}
