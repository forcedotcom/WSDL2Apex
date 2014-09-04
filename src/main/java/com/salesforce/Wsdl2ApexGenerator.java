package com.salesforce.ide.wsdl2apex.core;

import java.io.IOException;

public class Wsdl2ApexGenerator {

    public static void main(String[] args) throws RuntimeException, CalloutException, IOException {
        Wsdl2Apex w = new Wsdl2Apex();
        w.parseAndGenerate(args);
    }

}
