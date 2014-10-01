/*******************************************************************************
 * Copyright (c) 2014 Salesforce.com, inc.. All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: Salesforce.com, inc. - initial API and implementation
 ******************************************************************************/
package com.salesforce.ide.wsdl2apex.core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import javax.xml.namespace.QName;

import com.google.common.collect.Maps;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.wsdl.*;

/**
 * ApexTypeMapper
 *
 * @author cheenath
 * @version 1.0
 */
public class ApexTypeMapper {

    private static final HashSet<String> reserved = reserved();
    private static final HashSet<String> keywords = keywords();
    private static final HashMap<QName, ApexTypeName> xmlToApexMap = getXmlApexMapping();

    private final HashMap<String, String> namespaceMap = new HashMap<String, String>();

    private static final String STRING = "String";
    private static final String BOOLEAN = "Boolean";
    private static final String DECIMAL = "Decimal";
    private static final String DOUBLE = "Double";
    private static final String DATE_TIME = "DateTime";
    private static final String DATE = "Date";
    private static final String INTEGER = "Integer";
    private static final String LONG = "Long";

    private static final ApexTypeName APEX_STRING = new ApexTypeName(null, STRING, false);

    private static HashSet<String> reserved() {
        HashSet<String> s = new HashSet<String>();
        s.add("array");
        s.add("activate");
        s.add("any");
        s.add("autonomous");
        s.add("begin");
        s.add("bigdecimal");
        s.add("boolean");
        s.add("bulk");
        s.add("byte");
        s.add("case");
        s.add("cast");
        s.add("char");
        s.add("collect");
        s.add("commit");
        s.add("const");
        s.add("default");
        s.add("desc");
        s.add("end");
        s.add("export");
        s.add("exception");
        s.add("exit");
        s.add("float");
        s.add("goto");
        s.add("group");
        s.add("having");
        s.add("hint");
        s.add("int");
        s.add("integer");
        s.add("into");
        s.add("inner");
        s.add("import");
        s.add("join");
        s.add("loop");
        s.add("number");
        s.add("object");
        s.add("outer");
        s.add("of");
        s.add("package");
        s.add("parallel");
        s.add("pragma");
        s.add("retrieve");
        s.add("rollback");
        s.add("sort");
        s.add("short");
        s.add("super");
        s.add("switch");
        s.add("system");
        s.add("synchronized");
        s.add("transaction");
        s.add("this");
        s.add("then");
        s.add("when");
        s.add("apexpages");
        s.add("page");
        s.add("sobject");
        return s;
    }

    private static HashSet<String> keywords() {
        HashSet<String> keywords = new HashSet<String>();
        keywords.add("currency");
        keywords.add("date");
        keywords.add("time");
        keywords.add("datetime");
        keywords.add("double");
        keywords.add("float");
        keywords.add("private");
        keywords.add("public");
        keywords.add("string");
        keywords.add("type");
        keywords.add("user");
        keywords.add("yesterday");
        keywords.add("return");
        keywords.add("abstract");
        keywords.add("and");
        keywords.add("as");
        keywords.add("asc");
        keywords.add("blob");
        keywords.add("break");
        keywords.add("by");
        keywords.add("catch");
        keywords.add("class");
        keywords.add("continue");
        keywords.add("convertcurrency");
        keywords.add("decimal");
        keywords.add("delete");
        keywords.add("do");
        keywords.add("else");
        keywords.add("enum");
        keywords.add("extends");
        keywords.add("false");
        keywords.add("final");
        keywords.add("finally");
        keywords.add("for");
        keywords.add("from");
        keywords.add("future");
        keywords.add("global");
        keywords.add("if");
        keywords.add("implements");
        keywords.add("insert");
        keywords.add("instanceof");
        keywords.add("interface");
        keywords.add("last_90_days");
        keywords.add("last_month");
        keywords.add("last_n_days");
        keywords.add("last_week");
        keywords.add("like");
        keywords.add("limit");
        keywords.add("list");
        keywords.add("long");
        keywords.add("map");
        keywords.add("merge");
        keywords.add("new");
        keywords.add("next_90_days");
        keywords.add("next_month");
        keywords.add("next_n_days");
        keywords.add("next_week");
        keywords.add("not");
        keywords.add("null");
        keywords.add("nulls");
        keywords.add("on");
        keywords.add("or");
        keywords.add("override");
        keywords.add("protected");
        keywords.add("returning");
        keywords.add("savepoint");
        keywords.add("search");
        keywords.add("select");
        keywords.add("set");
        keywords.add("stat");
        keywords.add("testmethod");
        keywords.add("this_month");
        keywords.add("this_week");
        keywords.add("throw");
        keywords.add("today");
        keywords.add("tolabel");
        keywords.add("tomorrow");
        keywords.add("trigger");
        keywords.add("true");
        keywords.add("try");
        keywords.add("undelete");
        keywords.add("update");
        keywords.add("upsert");
        keywords.add("using");
        keywords.add("virtual");
        keywords.add("webservice");
        keywords.add("where");
        keywords.add("while");
        return keywords;
    }

    private static HashMap<QName, ApexTypeName> getXmlApexMapping() {

        String[][] xmltypeMap =
            {

                { "string", STRING }, { "boolean", BOOLEAN }, { "decimal", DECIMAL },

                { "float", DOUBLE }, { "double", DOUBLE },

                { "dateTime", DATE_TIME }, { "time", DATE_TIME }, { "date", DATE },

                { "base64Binary", STRING }, { "anyURI", STRING }, { "QName", STRING }, { "NOTATION", STRING },

                { "normalizedString", STRING }, { "token", STRING }, { "language", STRING }, { "NMTOKEN", STRING },
                { "NMTOKENS", STRING }, { "Name", STRING }, { "NCName", STRING },

                { "integer", INTEGER }, { "nonPositiveInteger", INTEGER }, { "negativeInteger", INTEGER },
                { "long", LONG }, { "int", INTEGER }, { "short", INTEGER },

                { "nonNegativeInteger", INTEGER }, { "unsignedLong", LONG }, { "unsignedInt", INTEGER },
                { "unsignedShort", INTEGER },

                { "positiveInteger", INTEGER }, };

        HashMap<QName, ApexTypeName> map = Maps.newHashMap();

        for (String[] pair : xmltypeMap) {
            map.put(new QName(Constants.SCHEMA_NS, pair[0]), new ApexTypeName(null, pair[1], false));
        }

        return map;
    }

    public void registerNamespace(String namespace, String packageName) {
        namespaceMap.put(namespace.trim(), packageName.trim());
    }

    public String getPackageName(String namespace) throws CalloutException {
        String ns = namespaceMap.get(namespace);
        if (ns == null) {
            throw new CalloutException("Failed to find class name for " + namespace);
        }
        return ns;
    }

    public Iterator<String> getPackageNamespaces() {
        return namespaceMap.keySet().iterator();
    }

    public String getFieldName(String name) throws CalloutException {
        return getSafeName(name);
    }

    public String getSafeName(String name) throws CalloutException {
        if (name == null || name.length() == 0) {
            throw new CalloutException("Unsupported name: " + name);
        }

        char ch = name.charAt(0);

        if (!((ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z'))) {
            //first letter not char
            name = "x" + name;
        }

        StringBuilder sb = new StringBuilder(name.length() + 3);
        char lastChar = name.charAt(0);
        sb.append(lastChar);

        for (int i = 1; i < name.length(); i++) {
            char c = name.charAt(i);
            if ((c >= '0' && c <= '9') || (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')
                || (c >= '\u0080' && c <= '\uFFFE')) {
                lastChar = c;
            } else {
                if (lastChar == '_') {
                    //replace __ by x
                    lastChar = 'x';
                } else {
                    lastChar = '_';
                }
            }
            sb.append(lastChar);
        }

        name = sb.toString();

        if (name.endsWith("_")) {
            name = name + "x";
        }

        if (reserved.contains(name.toLowerCase()) || keywords.contains(name.toLowerCase())) {
            name = name + "_x";
        }
        return name;
    }

    public ApexTypeName getApexType(QName xmlType, Definitions definitions) throws CalloutException {
        ApexTypeName apexType = xmlToApexMap.get(xmlType);
        if (apexType == null) {
            if (Constants.SCHEMA_NS.equals(xmlType.getNamespaceURI())) {
                throw new CalloutException("Unsupported schema type: " + xmlType);
            }
            checkValidType(definitions, xmlType);

            try {
                SimpleType st = definitions.getTypes().getSimpleTypeAllowNull(xmlType);
                if (st != null) {
                    apexType = APEX_STRING;
                } else {
                    apexType =
                        new ApexTypeName(getPackageName(xmlType.getNamespaceURI()),
                            getSafeName(xmlType.getLocalPart()), false);
                }
            } catch (CalloutException e) {
                throw new CalloutException("Failed to find type for " + xmlType, e);
            }
        }
        return apexType;
    }

    private void checkValidType(Definitions definitions, QName xmlType) throws CalloutException {
        try {
            SimpleType st = definitions.getTypes().getSimpleTypeAllowNull(xmlType);
            if (st == null) {
                ComplexType ct = definitions.getTypes().getComplexType(xmlType);
                if (ct == null) {
                    throw new CalloutException("Unable to find type definition for schema type " + xmlType);
                }
            }
        } catch (ConnectionException e) {
            throw new CalloutException("Unable to find type definition for schema type: " + xmlType + " Due to: "
                + e.getMessage(), e);
        }
    }

    public ApexTypeName getApexType(Element element, Definitions definitions) throws CalloutException {
        if (element.getType() == null) {
            throw new CalloutException("No type specified for element " + element.getName());
        }

        ApexTypeName apexType = getApexType(element.getType(), definitions);
        if (element.getMaxOccurs() != 1) {
            apexType = ApexTypeName.arrayOf(apexType);
        }
        return apexType;
    }
}
