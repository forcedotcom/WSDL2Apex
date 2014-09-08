/*******************************************************************************
 * Copyright (c) 2014 Salesforce.com, inc.. All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Salesforce.com, inc. - initial API and implementation
 ******************************************************************************/
package com.salesforce.ide.wsdl2apex.core;

/**
 * CalloutConstants
 * 
 * @author cheenath
 * @version 1.0
 */
public interface CalloutConstants {
    public static final String TYPE_INFO_PREFIX = "_type_info";
    public static final String ATTRIBUTE_INFO_PREFIX = "_att_info";
    public static final String NAMESPACE_MAP = "ns_map_type_info";
    public static final String APEX_SCHEMA_INFO = "apex_schema_type_info";
    public static final String FIELD_ORDER_INFO = "field_order_type_info";
    public static final String ENDPOINT = "endpoint_x";
    public static final String CLIENT_CERT_NAME = "clientCertName_x";
    public static final String CLIENT_CERT = "clientCert_x";
    public static final String CLIENT_CERT_PASSWD = "clientCertPasswd_x";
    public static final String INPUT_HTTP_HEADERS = "inputHttpHeaders_x";
    public static final String OUTPUT_HTTP_HEADERS = "outputHttpHeaders_x";
    public static final String TIMEOUT = "timeout_x";

    public static final String LENIENT_PARSING = "lenientParsing_x";
    public static final String PARSER_NAMESPACE_LEVEL = "parserNamespaceLevel_x";

    public static final int ATTRIBUTE_INFO_PREFIX_LENGTH = ATTRIBUTE_INFO_PREFIX.length();
}
