/*
 * Copyright, 1999-2013, salesforce.com
 * All Rights Reserved
 * Company Confidential
 */

/**
 * Represents the name of an apex type.
 *
 * @author sfell
 * @since 188
 */

import com.google.common.*;

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
        if (this == obj) return true;
        if ((obj == null) || (!(obj instanceof ApexTypeName))) return false;
        ApexTypeName rhs = (ApexTypeName)obj;
        return  com.google.common.base.Objects.equal(typeName, rhs.typeName) &&
                com.google.common.base.Objects.equal(packageName, rhs.packageName) &&
                (isArray == rhs.isArray);
    }
}
