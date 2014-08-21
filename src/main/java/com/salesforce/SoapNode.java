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

import javax.xml.namespace.QName;

/**
 * SoapNode
 * 
 * @author cheenath
 * @version 1.0
 * @since 146 Dec 13, 2006
 */
public class SoapNode extends WsdlNode {
    protected QName parseMessage(WsdlParser parser) {
        String msg = parser.getAttributeValue(null, "message");
        QName message = null;
        if (msg != null) {
            message = ParserUtil.toQName(msg, parser);
        }
        return message;
    }

    protected String parseUse(WsdlParser parser) throws WsdlParseException {
        String use = parser.getAttributeValue(null, "use");
        if (use == null) {
            use = "literal";
        }
        if (!"literal".equals(use)) {
            throw new WsdlParseException("WSC only supports SOAP Header use='literal' but found " + use);
        }
        return use;
    }
}
