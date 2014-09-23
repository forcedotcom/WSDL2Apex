/*******************************************************************************
 * Copyright (c) 2014 Salesforce.com, inc.. All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Salesforce.com, inc. - initial API and implementation
 ******************************************************************************/
package com.salesforce.ide.wsdl2apex.core;

import java.util.*;

import com.google.common.collect.Maps;
import com.sforce.ws.wsdl.Definitions;

/**
 * A collection of packages that are going through code-gen
 * 
 * @author sfell
 */
public class Packages {

    private LinkedHashMap<String, APackage> packages = Maps.newLinkedHashMap();

    public Packages(Definitions definitions, ApexTypeMapper mapper) {
        this.definitions = definitions;
        this.mapper = mapper;
    }

    private final Definitions definitions;
    private final ApexTypeMapper mapper;

    public APackage getPackage(String name) {
        APackage p = packages.get(name);
        if (p == null) {
            p = new APackage(name, definitions, mapper);
            packages.put(name, p);
        }
        return p;
    }

    public LinkedHashMap<String, APackage> getPackageMap() {
        return packages;
    }
}
