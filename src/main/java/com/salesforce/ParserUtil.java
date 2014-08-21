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
 * This is a util class used by wsdl parser.
 * 
 * @author http://cheenath.com
 * @version 1.0
 * @since 1.0 Nov 9, 2005
 */
public class ParserUtil {
    public static QName toQName(String value, WsdlParser parser) {
        int index = value.indexOf(":");

        if (index != -1) {
            String prefix = value.substring(0, index);
            String n = value.substring(index + 1);

            String namespace = parser.getNamespace(prefix);
            return new QName(namespace, n, prefix);
        } else {
            if (parser.getNamespace(null) != null) {
                return new QName(parser.getNamespace(null), value);
            } else {
                return new QName(value);
            }
        }
    }
}
