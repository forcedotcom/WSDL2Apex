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
 * This class contains a list of string constants used while parsing WSDL.
 * 
 * @author http://cheenath.com
 * @version 1.0
 * @since 1.0 Nov 5, 2005
 */
public interface Constants {
    String WSDL_NS = "http://schemas.xmlsoap.org/wsdl/";
    String WSDL_SOAP_NS = "http://schemas.xmlsoap.org/wsdl/soap/";
    String SCHEMA_NS = "http://www.w3.org/2001/XMLSchema";
    String SCHEMA_INSTANCE_NS = "http://www.w3.org/2001/XMLSchema-instance";
    String SOAP_ENVELOPE_NS = "http://schemas.xmlsoap.org/soap/envelope/";
    String ENTERPRISE_NS = "urn:enterprise.soap.sforce.com";
    String ENTERPRISE_SOBJECT_NS = "urn:sobject.enterprise.soap.sforce.com";
    String PARTNER_NS = "urn:partner.soap.sforce.com";
    String PARTNER_SOBJECT_NS = "urn:sobject.partner.soap.sforce.com";

    String META_SFORCE_NS = "http://soap.sforce.com/2006/04/metadata";
    String CROSS_INSTANCE_SFORCE_NS = "http://soap.sforce.com/2006/05/crossinstance";
    String INTERNAL_SFORCE_NS = "http://soap.sforce.com/2007/07/internal";
    String CLIENT_SYNC_SFORCE_NS = "http://soap.sforce.com/2009/10/clientsync";
    String SYNC_API_SFORCE_NS = "http://soap.sforce.com/schemas/class/syncapi/Command";

    String SCHEMA = "schema";
    String TYPES = "types";
    String DOCUMENTATION = "documentation";
    String DEFINITIONS = "definitions";
    String MESSAGE = "message";
    String PORT_TYPE = "portType";
    String PORT = "port";
    String BINDING = "binding";
    String SERVICE = "service";
    String TARGET_NAME_SPACE = "targetNamespace";
    String ELEMENT_FORM_DEFAULT = "elementFormDefault";
    String ATTRIBUTE_FORM_DEFAULT = "attributeFormDefault";
    String COMPLEX_TYPE = "complexType";
    String SIMPLE_TYPE = "simpleType";
    String NAME = "name";
    String VALUE = "value";
    String TYPE = "type";
    String REF = "ref";
    String SEQUENCE = "sequence";
    String RESTRICTION = "restriction";
    String ELEMENT = "element";
    String ENUMERATION = "enumeration";
    String NILLABLE = "nillable";
    String MIN_OCCURS = "minOccurs";
    String MAX_OCCURS = "maxOccurs";
    String EXTENSION = "extension";
    String BASE = "base";
    String INPUT = "input";
    String OUTPUT = "output";
    String FAULT = "fault";
    String OPERATION = "operation";
    String PART = "part";
    String ADDRESS = "address";
    String LOCATION = "location";
    String STYLE = "style";
    String TRANSPORT = "transport";
    String HEADER = "header";
    String BODY = "body";
    String IMPORT = "import";
    String ANNOTATION = "annotation";
    String ATTRIBUTE = "attribute";
    String ALL = "all";
    String CHOICE = "choice";
}
