/*
 * Copyright, 1999-2013, salesforce.com
 * All Rights Reserved
 * Company Confidential
 */

import java.util.*;

import com.google.common.collect.Maps;


/**
 * A collection of packages that are going through code-gen
 *
 * @author sfell
 * @since 188
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
