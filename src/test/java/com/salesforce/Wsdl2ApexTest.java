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

import static org.junit.Assert.*;

import java.io.File;
import java.util.HashMap;
import java.io.FileNotFoundException;
import java.io.IOException;
import static org.hamcrest.CoreMatchers.containsString;

import org.junit.*;

public class Wsdl2ApexTest {
    File directory;

    @Before
    public void setUp() {
        directory = new File("../../src/test/testFiles/tempTest");
        directory.mkdir();
    }

    @After
    public void tearDown() {
        FileString.recursiveDelete(directory);
    }

    /**
     * Tests the basic functionality of the wsdl2apex program
     * 
     * @throws IOException
     * @throws CalloutException
     */
    @Test
    public void testSimple() throws IOException, CalloutException {
        String[] args = new String[3];
        args[0] = directory.getPath() + "/../goodWsdls/airport.wsdl";
        args[1] = directory.getPath();
        args[2] = "true";
        Wsdl2Apex m = new Wsdl2Apex();
        try {
            m.parseAndGenerate(args);
            FileString f = new FileString();

            String result1 = f.getStringFromFile(directory.getPath() + "/" + "wwwWebservicexNet.cls");
            String answer1 = f.getStringFromFile(directory.getPath() + "/../answers/wwwWebservicexNet_answer.cls");
            String result2 = f.getStringFromFile(directory.getPath() + "/" + "AsyncWwwWebservicexNet.cls");
            String answer2 = f.getStringFromFile(directory.getPath() + "/../answers/AsyncWwwWebservicexNet_answer.cls");
            assertEquals("testSimple doesn't match", result1, answer1);
            assertEquals("testSimple async doesn't match", result2, answer2);
        } catch (FileNotFoundException e) {
            System.err.println("Unable to generate the file");
            e.printStackTrace();
            fail("testSimple unable to generate the file");
        }
    }

    /**
     * Makes sure that the a wsdl that doesn't work on the app doesn't work in this version of wsdl2apex
     */
    @Test
    public void testInvalidXml() {
        String[] args = new String[3];
        args[0] = directory.getPath() + "/../badWsdls/airport_Raw.wsdl";
        args[1] = directory.getPath();
        args[2] = "false";
        Wsdl2Apex m = new Wsdl2Apex();
        try {
            m.parseAndGenerate(args);
            fail("testInvalidXml shouldn't have passed");
        } catch (Exception e) {
            System.err.println(e.getMessage());
            assertTrue("testInvalidXml shouldn't have passed", true);
        }
    }

    /**
     * Tests whether the wsdl2apex program can parse a wsdl with multiple namespaces
     * 
     * @throws IOException
     * @throws CalloutException
     */
    @Test
    public void testTwoNamespaces() throws IOException, CalloutException {
        String[] args = new String[3];
        args[0] = directory.getPath() + "/../goodWsdls/stockQuote.wsdl";
        args[1] = directory.getPath();
        args[2] = "true";
        Wsdl2Apex m = new Wsdl2Apex();
        try {
            m.parseAndGenerate(args);
            FileString f = new FileString();

            String result1 = f.getStringFromFile(directory.getPath() + "/" + "AsyncExampleComStockquoteWsdl.cls");
            String result2 = f.getStringFromFile(directory.getPath() + "/" + "AsyncExampleComStockquoteXsd.cls");
            String result3 = f.getStringFromFile(directory.getPath() + "/" + "exampleComStockquoteWsdl.cls");
            String result4 = f.getStringFromFile(directory.getPath() + "/" + "exampleComStockquoteXsd.cls");

            String answer1 =
                    f.getStringFromFile(directory.getPath() + "/../answers/AsyncExampleComStockquoteWsdl_answer.cls");
            String answer2 =
                    f.getStringFromFile(directory.getPath() + "/../answers/AsyncExampleComStockquoteXsd_answer.cls");
            String answer3 =
                    f.getStringFromFile(directory.getPath() + "/../answers/exampleComStockquoteWsdl_answer.cls");
            String answer4 =
                    f.getStringFromFile(directory.getPath() + "/../answers/exampleComStockquoteXsd_answer.cls");
            assertEquals("testTwoNamespaces a doesn't match, Async class", result1, answer1);
            assertEquals("testTwoNamespaces b doesn't match, Async class", result2, answer2);
            assertEquals("testTwoNamespaces c doesn't match", result3, answer3);
            assertEquals("testTwoNamespaces d doesn't match", result4, answer4);
        } catch (FileNotFoundException e) {
            System.err.println("Unable to find the file");
            e.printStackTrace();
            fail("testTwoNamespaces unable to find the file");
        }
    }

    /**
     * Tests to see if the program can parse a large file with 5 namespaces
     * 
     * @throws IOException
     * @throws CalloutException
     */
    @Test
    public void testLargeFile() throws IOException, CalloutException //large file
    {
        String[] args = new String[3];
        args[0] = directory.getPath() + "/../goodWsdls/EOLS_PSAPLookupUS.wsdl";
        args[1] = directory.getPath();
        args[2] = "true";
        Wsdl2Apex m = new Wsdl2Apex();
        try {
            m.parseAndGenerate(args);
            FileString f = new FileString();

            String result1 = f.getStringFromFile(directory.getPath() + "/" + "spectrumPbCom.cls");
            String result2 = f.getStringFromFile(directory.getPath() + "/" + "wwwMapinfoComMidevServiceGeometries.cls");
            String result3 = f.getStringFromFile(directory.getPath() + "/" + "wwwMapinfoComMidevServiceUnitsV1.cls");
            String result4 = f.getStringFromFile(directory.getPath() + "/" + "wwwPbComSpectrumServices.cls");
            String result5 = f.getStringFromFile(directory.getPath() + "/" + "wwwPbComSpectrumServicesEolsPsaplo.cls");
            String result6 = f.getStringFromFile(directory.getPath() + "/" + "AsyncSpectrumPbCom.cls");
            String result7 =
                    f.getStringFromFile(directory.getPath() + "/" + "AsyncWwwPbComSpectrumServicesEolsPsaplo.cls");

            String answer1 = f.getStringFromFile(directory.getPath() + "/../answers/spectrumPbCom_answer.cls");
            String answer2 =
                    f.getStringFromFile(directory.getPath()
                            + "/../answers/wwwMapinfoComMidevServiceGeometries_answer.cls");
            String answer3 =
                    f.getStringFromFile(directory.getPath() + "/../answers/wwwMapinfoComMidevServiceUnitsV1_answer.cls");
            String answer4 =
                    f.getStringFromFile(directory.getPath() + "/../answers/wwwPbComSpectrumServices_answer.cls");
            String answer5 =
                    f.getStringFromFile(directory.getPath()
                            + "/../answers/wwwPbComSpectrumServicesEolsPsaplo_answer.cls");
            String answer6 = f.getStringFromFile(directory.getPath() + "/../answers/AsyncSpectrumPbCom_answer.cls");
            String answer7 =
                    f.getStringFromFile(directory.getPath()
                            + "/../answers/AsyncWwwPbComSpectrumServicesEolsPsaplo_answer.cls");
            assertEquals("testLargeFile a doesn't match", result1, answer1);
            assertEquals("testLargeFile b doesn't match", result2, answer2);
            assertEquals("testLargeFile c doesn't match", result3, answer3);
            assertEquals("testLargeFile d doesn't match", result4, answer4);
            assertEquals("testLargeFile e doesn't match", result5, answer5);
            assertEquals("testLargeFile f doesn't match, Async class", result6, answer6);
            assertEquals("testLargeFile g doesn't match, Async class", result7, answer7);
        } catch (FileNotFoundException e) {
            System.err.println("Unable to find the file");
            e.printStackTrace();
            fail("testLargeFile unable to generate the file");
        }
    }

    /**
     * Makes sure the program fails if there is a fault without a name
     */
    @Test
    public void testNoNameFault() {
        String[] args = new String[3];
        args[0] = directory.getPath() + "/../badWsdls/endorsementSearch.wsdl";
        args[1] = directory.getPath();
        args[2] = "false";
        Wsdl2Apex m = new Wsdl2Apex();
        try {
            m.parseAndGenerate(args);
            fail("testNoNameFault shouldn't have been able to create the files");
        } catch (Exception e) {
            System.err.println(e.getMessage());
            assertTrue("testNoNameFault shouldn't have been able to create the files", true);
        }
    }

    /**
     * Check to make sure the program failed for rpc style wsdl
     */
    @Test
    public void testRPC() {
        String[] args = new String[3];
        args[0] = directory.getPath() + "/../badWsdls/helloService.wsdl";
        args[1] = directory.getPath();
        args[2] = "false";
        Wsdl2Apex m = new Wsdl2Apex();
        try {
            m.parseAndGenerate(args);
            fail("testRPC shouldn't have been able to create the files");
        } catch (Exception e) {
            System.err.println(e.getMessage());
            assertTrue("testRPC shouldn't have been able to creat the files", true);
        }
    }

    /**
     * Test the program fails when it can't find soap 1.1 address
     */
    @Test
    public void testSoapAddress() {
        String[] args = new String[3];
        args[0] = directory.getPath() + "/../badWsdls/ADC_BibCode.wsdl";
        args[1] = directory.getPath();
        args[2] = "false";
        Wsdl2Apex m = new Wsdl2Apex();
        try {
            m.parseAndGenerate(args);

            fail("testSoapAddress shouldn't have been able to create the files");
        } catch (Exception e) {
            System.err.println(e.getMessage());
            assertTrue("testSoapAddress shouldn't have been able to create the files", true);
        }
    }

    /**
     * Make sure that if the async option is false, then it will still generate the classes correctly
     */
    @Test
    public void testAsyncFalse() {
        String[] args = new String[3];
        args[0] = directory.getPath() + "/../goodWsdls/airport.wsdl";
        args[1] = directory.getPath();
        args[2] = "false";
        Wsdl2Apex m = new Wsdl2Apex();
        try {
            m.parseAndGenerate(args);
            FileString f = new FileString();

            String result1 = f.getStringFromFile(directory.getPath() + "/" + "wwwWebservicexNet.cls");
            String answer1 = f.getStringFromFile(directory.getPath() + "/../answers/wwwWebservicexNet_answer.cls");
            assertEquals("Test 8 doesn't match", result1, answer1);
            String result2 = f.getStringFromFile(directory.getPath() + "/" + "AsyncWwwWebservicexNet.cls");
            fail("testAsyncFalse should not have found the async class");
        } catch (FileNotFoundException e) {
            System.err.println("Unable to generate the file");
            e.printStackTrace();
            assertTrue("File was not found", true);
        } catch (IOException e) {
            System.err.println("Unable to generate the file");
            e.printStackTrace();
            fail("testAsyncFalse unable to generate the file");
        } catch (CalloutException e) {
            System.err.println("Unable to generate the file");
            e.printStackTrace();
            fail("testAsyncFalse unable to generate the file");
        }
    }

    /**
     * Test changing the name of the apex classes in the code will reflect on the output classes
     */
    @Test
    public void testNewClassNames() {
        String[] parseArgs = new String[] { directory.getPath() + "/../goodWsdls/EOLS_PSAPLookupUS.wsdl" };
        String[] generateArgs = new String[2];
        generateArgs[0] = "true";
        generateArgs[1] = directory.getPath();

        Wsdl2Apex m = new Wsdl2Apex();
        try {
            Wsdl2Apex.parse(parseArgs);

            HashMap<String, String> a = m.getResultFromParse();
            a.keySet().iterator();
            for (String ns : a.keySet()) {
                if (ns.equals("http://www.pb.com/spectrum/services/EOLS_PSAPLookupUS")) {
                    a.put(ns, "s1");
                } else if (ns.equals("http://spectrum.pb.com/")) {
                    a.put(ns, "s2");
                } else if (ns.equals("http://www.mapinfo.com/midev/service/geometries/v1")) {
                    a.put(ns, "s3");
                } else if (ns.equals("http://www.mapinfo.com/midev/service/units/v1")) {
                    a.put(ns, "s4");
                } else if (ns.equals("http://www.pb.com/spectrum/services/")) {
                    a.put(ns, "s5");
                }
            }

            Wsdl2Apex.generate(generateArgs);
            FileString f = new FileString();

            String result1 = f.getStringFromFile(directory.getPath() + "/" + "s1.cls");
            String result2 = f.getStringFromFile(directory.getPath() + "/" + "s2.cls");
            String result3 = f.getStringFromFile(directory.getPath() + "/" + "s3.cls");
            String result4 = f.getStringFromFile(directory.getPath() + "/" + "s4.cls");
            String result5 = f.getStringFromFile(directory.getPath() + "/" + "s5.cls");
            String result6 = f.getStringFromFile(directory.getPath() + "/" + "AsyncS1.cls");
            String result7 = f.getStringFromFile(directory.getPath() + "/" + "AsyncS2.cls");

            String answer1 = f.getStringFromFile(directory.getPath() + "/../answers/s1_answer.cls");
            String answer2 = f.getStringFromFile(directory.getPath() + "/../answers/s2_answer.cls");
            String answer3 = f.getStringFromFile(directory.getPath() + "/../answers/s3_answer.cls");
            String answer4 = f.getStringFromFile(directory.getPath() + "/../answers/s4_answer.cls");
            String answer5 = f.getStringFromFile(directory.getPath() + "/../answers/s5_answer.cls");
            String answer6 = f.getStringFromFile(directory.getPath() + "/../answers/AsyncS1_answer.cls");
            String answer7 = f.getStringFromFile(directory.getPath() + "/../answers/AsyncS2_answer.cls");
            assertEquals("testNewClassNames doesn't match", result1, answer1);
            assertEquals("testNewClassNames doesn't match", result2, answer2);
            assertEquals("testNewClassNames doesn't match", result3, answer3);
            assertEquals("testNewClassNames doesn't match", result4, answer4);
            assertEquals("testNewClassNames doesn't match", result5, answer5);
            assertEquals("testNewClassNames doesn't match", result6, answer6);
            assertEquals("testNewClassNames doesn't match", result7, answer7);
        } catch (FileNotFoundException e) {
            System.err.println("Unable to generate the file");
            e.printStackTrace();
            fail("testNewClassNames unable to generate the file");
        } catch (IOException e) {
            System.err.println("Unable to generate the file");
            e.printStackTrace();
            fail("testNewClassNames unable to generate the file");
        } catch (CalloutException e) {
            System.err.println("Unable to generate the file");
            e.printStackTrace();
            fail("testNewClassNames unable to generate the file");
        }
    }

    @Test
    public void testImport() {
        String[] parseArgs = new String[] { directory.getPath() + "/../badWsdls/AlexaWebSearch.wsdl" };
        String[] generateArgs = new String[2];
        generateArgs[0] = "true";
        generateArgs[1] = directory.getPath();

        try {
            Wsdl2Apex.parse(parseArgs);
            Wsdl2Apex.generate(generateArgs);
        } catch (Exception e) {
            assertThat(e.getMessage(), containsString("Found schema import from location AlexaWebSearch.xsd. "
                    + "External schema import not supported"));
        }
    }

    @Test
    public void testIncldues() {
        String[] parseArgs = new String[] { directory.getPath() + "/../badWsdls/amazon-s3-orig.wsdl" };
        String[] generateArgs = new String[2];
        generateArgs[0] = "true";
        generateArgs[1] = directory.getPath();

        try {
            Wsdl2Apex.parse(parseArgs);
            Wsdl2Apex.generate(generateArgs);
        } catch (Exception e) {
            assertThat(e.getMessage(),
                containsString("Unsupported Schema element found http://www.w3.org/2001/XMLSchema:include"));
        }
    }

    @Test
    public void testSimpleType() {
        String[] parseArgs = new String[] { directory.getPath() + "/../badWsdls/AWSECommerceService.wsdl" };
        String[] generateArgs = new String[2];
        generateArgs[0] = "true";
        generateArgs[1] = directory.getPath();

        try {
            Wsdl2Apex.parse(parseArgs);
            Wsdl2Apex.generate(generateArgs);
        } catch (Exception e) {
            assertThat(e.getMessage(), containsString("Failed to parse wsdl"));
        }
    }

    @Test
    public void testMultipleBinding() {
        String[] parseArgs = new String[] { directory.getPath() + "/../badWsdls/BLZService.wsdl" };
        String[] generateArgs = new String[2];
        generateArgs[0] = "true";
        generateArgs[1] = directory.getPath();

        try {
            Wsdl2Apex.parse(parseArgs);
            Wsdl2Apex.generate(generateArgs);
        } catch (Exception e) {
            assertThat(e.getMessage(), containsString("WSDL with multiple binding not supported"));
        }
    }

    @Test
    public void testIncorrectBinding() {
        String[] parseArgs = new String[] { directory.getPath() + "/../badWsdls/incorrect-binding.wsdl" };
        String[] generateArgs = new String[2];
        generateArgs[0] = "true";
        generateArgs[1] = directory.getPath();

        try {
            Wsdl2Apex.parse(parseArgs);
            Wsdl2Apex.generate(generateArgs);
        } catch (Exception e) {
            assertThat(
                e.getMessage(),
                containsString("Failed to parse wsdl: Failed to parse WSDL: Unable to find binding {urn:dotnet.callouttest.soap.sforce.com}IDontExist. Found DotNetInteropTestServiceSoap instead."));
        }
    }

    @Test
    public void testIncorrectMessageName() {
        String[] parseArgs = new String[] { directory.getPath() + "/../badWsdls/incorrect-messagename.wsdl" };
        String[] generateArgs = new String[2];
        generateArgs[0] = "true";
        generateArgs[1] = directory.getPath();

        try {
            Wsdl2Apex.parse(parseArgs);
            Wsdl2Apex.generate(generateArgs);
        } catch (Exception e) {
            assertThat(e.getMessage(),
                containsString("No message found for:{urn:dotnet.callouttest.soap.sforce.com}MeNeither"));
        }
    }

    @Test
    public void testIncorrectXsdType() {
        String[] parseArgs = new String[] { directory.getPath() + "/../badWsdls/incorrect-xsd-type.wsdl" };
        String[] generateArgs = new String[2];
        generateArgs[0] = "true";
        generateArgs[1] = directory.getPath();

        try {
            Wsdl2Apex.parse(parseArgs);
            Wsdl2Apex.generate(generateArgs);
        } catch (Exception e) {
            assertThat(e.getMessage(),
                containsString("Unsupported schema type: {http://www.w3.org/2001/XMLSchema}IDontExist"));
        }
    }

    @Test
    public void testMultiplePortType() {
        String[] parseArgs = new String[] { directory.getPath() + "/../badWsdls/mappoint.wsdl" };
        String[] generateArgs = new String[2];
        generateArgs[0] = "true";
        generateArgs[1] = directory.getPath();

        try {
            Wsdl2Apex.parse(parseArgs);
            Wsdl2Apex.generate(generateArgs);
        } catch (Exception e) {
            assertThat(e.getMessage(), containsString("WSDL with multiple portType not supported"));
        }
    }

    @Test
    public void testPortAndTypeWithSameName() {
        String[] parseArgs = new String[] { directory.getPath() + "/../badWsdls/metadata.wsdl" };
        String[] generateArgs = new String[2];
        generateArgs[0] = "true";
        generateArgs[1] = directory.getPath();

        try {
            Wsdl2Apex.parse(parseArgs);
            Wsdl2Apex.generate(generateArgs);
        } catch (Exception e) {
            ;
            assertThat(e.getMessage(), containsString("Class name 'Metadata' already in use. "
                    + "Please edit WSDL to remove repeated names"));
        }
    }

    @Test
    public void testMissingBinding() {
        String[] parseArgs = new String[] { directory.getPath() + "/../badWsdls/missing-binding.wsdl" };
        String[] generateArgs = new String[2];
        generateArgs[0] = "true";
        generateArgs[1] = directory.getPath();

        try {
            Wsdl2Apex.parse(parseArgs);
            Wsdl2Apex.generate(generateArgs);
        } catch (Exception e) {
            assertThat(e.getMessage(),
                containsString("Failed to parse wsdl: Unable to find wsdl:binding in the specified wsdl"));
        }
    }

    @Test
    public void testMissingNamespace() {
        String[] parseArgs = new String[] { directory.getPath() + "/../badWsdls/missing-namespace.wsdl" };
        String[] generateArgs = new String[2];
        generateArgs[0] = "true";
        generateArgs[1] = directory.getPath();

        try {
            Wsdl2Apex.parse(parseArgs);
            Wsdl2Apex.generate(generateArgs);
        } catch (Exception e) {
            assertThat(e.getMessage(),
                containsString("Failed to parse wsdl: targetNamespace not specified in wsdl:definitions"));
        }
    }

    @Test
    public void testMissingPort() {
        String[] parseArgs = new String[] { directory.getPath() + "/../badWsdls/missing-port.wsdl" };
        String[] generateArgs = new String[2];
        generateArgs[0] = "true";
        generateArgs[1] = directory.getPath();

        try {
            Wsdl2Apex.parse(parseArgs);
            Wsdl2Apex.generate(generateArgs);
        } catch (Exception e) {
            assertThat(e.getMessage(), containsString("Unable to find port in wsdl:service"));
        }
    }

    @Test
    public void testMissingPortType() {
        String[] parseArgs = new String[] { directory.getPath() + "/../badWsdls/missing-porttype.wsdl" };
        String[] generateArgs = new String[2];
        generateArgs[0] = "true";
        generateArgs[1] = directory.getPath();

        try {
            Wsdl2Apex.parse(parseArgs);
            Wsdl2Apex.generate(generateArgs);
        } catch (Exception e) {
            assertThat(e.getMessage(), containsString("Unable to find wsdl:portType in the specified wsdl"));
        }
    }

    @Test
    public void testMissingSchemaForElement() {
        String[] parseArgs = new String[] { directory.getPath() + "/../badWsdls/missing-schema-for-element.wsdl" };
        String[] generateArgs = new String[2];
        generateArgs[0] = "true";
        generateArgs[1] = directory.getPath();

        try {
            Wsdl2Apex.parse(parseArgs);
            Wsdl2Apex.generate(generateArgs);
        } catch (Exception e) {
            assertThat(e.getMessage(), containsString("Unable to find schema type for element ElementWithNoSchemaType"));
        }
    }

    @Test
    public void testMissingService() {
        String[] parseArgs = new String[] { directory.getPath() + "/../badWsdls/missing-service.wsdl" };
        String[] generateArgs = new String[2];
        generateArgs[0] = "true";
        generateArgs[1] = directory.getPath();

        try {
            Wsdl2Apex.parse(parseArgs);
            Wsdl2Apex.generate(generateArgs);
        } catch (Exception e) {
            assertThat(e.getMessage(),
                containsString("Failed to parse wsdl: Unable to find wsdl:service in the specified wsdl"));
        }
    }

    @Test
    public void testMissingTypes() {
        String[] parseArgs = new String[] { directory.getPath() + "/../badWsdls/missing-types.wsdl" };
        String[] generateArgs = new String[2];
        generateArgs[0] = "true";
        generateArgs[1] = directory.getPath();

        try {
            Wsdl2Apex.parse(parseArgs);
            Wsdl2Apex.generate(generateArgs);
        } catch (Exception e) {
            assertThat(e.getMessage(), containsString("Unable to find Schema type for TestHeader"));
        }
    }

    @Test
    public void testNumberOfMessagePartsOutput() {
        String[] parseArgs = new String[] { directory.getPath() + "/../badWsdls/multiple-parts-in-output.wsdl" };
        String[] generateArgs = new String[2];
        generateArgs[0] = "true";
        generateArgs[1] = directory.getPath();

        try {
            Wsdl2Apex.parse(parseArgs);
            Wsdl2Apex.generate(generateArgs);
        } catch (Exception e) {
            assertThat(e.getMessage(),
                containsString("Unsupported WSDL. Found more than one part for message EchoStringSoapOut"));
        }
    }

    @Test
    public void testNumberOfMessagePartsInput() {
        String[] parseArgs = new String[] { directory.getPath() + "/../badWsdls/multiple-parts-in-input.wsdl" };
        String[] generateArgs = new String[2];
        generateArgs[0] = "true";
        generateArgs[1] = directory.getPath();

        try {
            Wsdl2Apex.parse(parseArgs);
            Wsdl2Apex.generate(generateArgs);
        } catch (Exception e) {
            assertThat(e.getMessage(),
                containsString("Unsupported WSDL. Found more than one part for message EchoStringSoapIn"));
        }
    }

    @Test
    public void testNoMessagePartsOutput() {
        String[] parseArgs = new String[] { directory.getPath() + "/../badWsdls/no-parts-in-output.wsdl" };
        String[] generateArgs = new String[2];
        generateArgs[0] = "true";
        generateArgs[1] = directory.getPath();

        try {
            Wsdl2Apex.parse(parseArgs);
            Wsdl2Apex.generate(generateArgs);
        } catch (Exception e) {
            assertThat(e.getMessage(), containsString("Unsupported WSDL. No part found for message: EchoStringSoapOut"));
        }
    }

    @Test
    public void testNoMessagePartsInput() {
        String[] parseArgs = new String[] { directory.getPath() + "/../badWsdls/no-parts-in-input.wsdl" };
        String[] generateArgs = new String[2];
        generateArgs[0] = "true";
        generateArgs[1] = directory.getPath();

        try {
            Wsdl2Apex.parse(parseArgs);
            Wsdl2Apex.generate(generateArgs);
        } catch (Exception e) {
            assertThat(e.getMessage(), containsString("Unsupported WSDL. No part found for message: EchoStringSoapIn"));
        }
    }

    @Test
    public void testNestedComplexType() {
        String[] parseArgs = new String[] { directory.getPath() + "/../badWsdls/nested-complex-type.wsdl" };
        String[] generateArgs = new String[2];
        generateArgs[0] = "true";
        generateArgs[1] = directory.getPath();

        try {
            Wsdl2Apex.parse(parseArgs);
            Wsdl2Apex.generate(generateArgs);
        } catch (Exception e) {
            assertThat(e.getMessage(), containsString("Unexpected element 'complexType'"));
        }
    }

    @Test
    public void testNoBindingInPort() {
        String[] parseArgs = new String[] { directory.getPath() + "/../badWsdls/no-binding-in-port.wsdl" };
        String[] generateArgs = new String[2];
        generateArgs[0] = "true";
        generateArgs[1] = directory.getPath();

        try {
            Wsdl2Apex.parse(parseArgs);
            Wsdl2Apex.generate(generateArgs);
        } catch (Exception e) {
            assertThat(e.getMessage(), containsString("Unable to find binding in port DotNetInteropTestServiceSoap"));
        }
    }

    @Test
    public void testNoElementInPort() {
        String[] parseArgs = new String[] { directory.getPath() + "/../badWsdls/no-element-in-part.wsdl" };
        String[] generateArgs = new String[2];
        generateArgs[0] = "true";
        generateArgs[1] = directory.getPath();

        try {
            Wsdl2Apex.parse(parseArgs);
            Wsdl2Apex.generate(generateArgs);
        } catch (Exception e) {
            assertThat(e.getMessage(), containsString("Element not defined for part"));
        }
    }

    @Test
    public void testNoTypeForElement() {
        String[] parseArgs = new String[] { directory.getPath() + "/../badWsdls/no-type-specified.wsdl" };
        String[] generateArgs = new String[2];
        generateArgs[0] = "true";
        generateArgs[1] = directory.getPath();

        try {
            Wsdl2Apex.parse(parseArgs);
            Wsdl2Apex.generate(generateArgs);
        } catch (Exception e) {
            assertThat(e.getMessage(), containsString("No type specified for element anyType"));
        }
    }

    @Test
    public void testTextFileAsWsdl() {
        String[] parseArgs = new String[] { directory.getPath() + "/../badWsdls/random.txt" };
        String[] generateArgs = new String[2];
        generateArgs[0] = "true";
        generateArgs[1] = directory.getPath();

        try {
            Wsdl2Apex.parse(parseArgs);
            Wsdl2Apex.generate(generateArgs);
        } catch (Exception e) {
            assertThat(
                e.getMessage(),
                containsString("Failed to parse wsdl: Parse error: Found invalid XML. only whitespace content allowed before start tag and not t (position: START_DOCUMENT seen t... @1:1) "));
        }
    }

    @Test
    public void testRpcWsdl() {
        String[] parseArgs = new String[] { directory.getPath() + "/../badWsdls/rpc-service.wsdl" };
        String[] generateArgs = new String[2];
        generateArgs[0] = "true";
        generateArgs[1] = directory.getPath();

        try {
            Wsdl2Apex.parse(parseArgs);
            Wsdl2Apex.generate(generateArgs);
        } catch (Exception e) {
            assertThat(e.getMessage(), containsString("Unsupported WSDL style"));
        }
    }

    @Test
    public void testSomeTypesMissing() {
        String[] parseArgs = new String[] { directory.getPath() + "/../badWsdls/some-types-missing-but-not-all.wsdl" };
        String[] generateArgs = new String[2];
        generateArgs[0] = "true";
        generateArgs[1] = directory.getPath();

        try {
            Wsdl2Apex.parse(parseArgs);
            Wsdl2Apex.generate(generateArgs);
        } catch (Exception e) {
            assertThat(e.getMessage(),
                containsString("Unable to find element for {urn:dotnet.callouttest.soap.sforce.com}EchoStringResponse"));
        }
    }

    @Test
    public void testUndefinedSchemaType() {
        String[] parseArgs = new String[] { directory.getPath() + "/../badWsdls/undefined-schma-type.wsdl" };
        String[] generateArgs = new String[2];
        generateArgs[0] = "true";
        generateArgs[1] = directory.getPath();

        try {
            Wsdl2Apex.parse(parseArgs);
            Wsdl2Apex.generate(generateArgs);
        } catch (Exception e) {

            assertThat(e.getMessage(),
                containsString("Unable to find type definition for schema type: {http://tempuri.org/}DNSInfo-xxx"));
        }
    }

    @Test
    public void testUnsupportedSchemaType() {
        String[] parseArgs = new String[] { directory.getPath() + "/../badWsdls/unsupported-schema-type.wsdl" };
        String[] generateArgs = new String[2];
        generateArgs[0] = "true";
        generateArgs[1] = directory.getPath();

        try {
            Wsdl2Apex.parse(parseArgs);
            Wsdl2Apex.generate(generateArgs);
        } catch (Exception e) {
            assertThat(e.getMessage(),
                containsString("Unsupported schema type: {http://www.w3.org/2001/XMLSchema}gDay"));
        }
    }

    @Test
    public void testAmazonGood() {
        String[] parseArgs = new String[] { directory.getPath() + "/../goodWsdls/amazonS3.wsdl" };
        String[] generateArgs = new String[2];
        generateArgs[0] = "true";
        generateArgs[1] = directory.getPath();

        Wsdl2Apex m = new Wsdl2Apex();
        try {
            Wsdl2Apex.parse(parseArgs);

            HashMap<String, String> a = m.getResultFromParse();
            a.keySet().iterator();
            for (String ns : a.keySet()) {
                if (ns.equals("http://s3.amazonaws.com/doc/2006-03-01/")) {
                    a.put(ns, "s3Amazon");
                }
            }

            Wsdl2Apex.generate(generateArgs);
            FileString f = new FileString();

            String result1 = f.getStringFromFile(directory.getPath() + "/" + "s3Amazon.cls");
            String result2 = f.getStringFromFile(directory.getPath() + "/" + "AsyncS3Amazon.cls");
            String answer1 = f.getStringFromFile(directory.getPath() + "/../answers/s3Amazon.cls");
            String answer2 = f.getStringFromFile(directory.getPath() + "/../answers/AsyncS3Amazon.cls");
            assertEquals("Test testAmazonGood failed", result1, answer1);
            assertEquals("Test testAmazonGood failed", result2, answer2);
        } catch (Exception e) {
            System.err.println("Unable to generate the file");
            e.printStackTrace();
            fail("Test testAmazonGood failed");
        }
    }

    @Test
    public void testDocSample() {
        String[] parseArgs = new String[] { directory.getPath() + "/../goodWsdls/elementStartsWithNonLetter.wsdl" };
        String[] generateArgs = new String[2];
        generateArgs[0] = "true";
        generateArgs[1] = directory.getPath();

        Wsdl2Apex m = new Wsdl2Apex();
        try {
            Wsdl2Apex.parse(parseArgs);

            HashMap<String, String> a = m.getResultFromParse();
            a.keySet().iterator();
            for (String ns : a.keySet()) {
                if (ns.equals("http://doc.sample.com/docSample")) {
                    a.put(ns, "docSample");
                }
            }

            Wsdl2Apex.generate(generateArgs);
            FileString f = new FileString();

            String result1 = f.getStringFromFile(directory.getPath() + "/" + "docSample.cls");
            String result2 = f.getStringFromFile(directory.getPath() + "/" + "AsyncDocSample.cls");
            String answer1 = f.getStringFromFile(directory.getPath() + "/../answers/docSample.cls");
            String answer2 = f.getStringFromFile(directory.getPath() + "/../answers/AsyncDocSample.cls");
            assertEquals("Test testDocSample failed", result1, answer1);
            assertEquals("Test testDocSample failed", result2, answer2);
        } catch (Exception e) {
            System.err.println("Unable to generate the file");
            e.printStackTrace();
            fail("Test testDocSample failed");
        }
    }

    @Test
    public void testExchange() {
        String[] parseArgs = new String[] { directory.getPath() + "/../goodWsdls/ForeignExchangeRate.wsdl" };
        String[] generateArgs = new String[2];
        generateArgs[0] = "true";
        generateArgs[1] = directory.getPath();

        Wsdl2Apex m = new Wsdl2Apex();
        try {
            Wsdl2Apex.parse(parseArgs);

            HashMap<String, String> a = m.getResultFromParse();
            a.keySet().iterator();
            for (String ns : a.keySet()) {
                if (ns.equals("http://ws.strikeiron.com")) {
                    a.put(ns, "strikeiron");
                }
                if (ns.equals("http://www.strikeiron.com")) {
                    a.put(ns, "strikeiron2");
                }
            }

            Wsdl2Apex.generate(generateArgs);
            FileString f = new FileString();

            String result1 = f.getStringFromFile(directory.getPath() + "/" + "strikeiron2.cls");
            String result2 = f.getStringFromFile(directory.getPath() + "/" + "strikeiron.cls");
            String answer1 = f.getStringFromFile(directory.getPath() + "/../answers/strikeiron2.cls");
            String answer2 = f.getStringFromFile(directory.getPath() + "/../answers/strikeiron.cls");
            assertEquals("Test testExchange failed", result1, answer1);
            assertEquals("Test testExchange failed", result2, answer2);
        } catch (Exception e) {
            System.err.println("Unable to generate the file");
            e.printStackTrace();
            fail("Test testExchange failed");
        }
    }

    @Test
    public void testBigFile() {
        String[] parseArgs = new String[] { directory.getPath() + "/../goodWsdls/max-file-1mb.wsdl" };
        String[] generateArgs = new String[2];
        generateArgs[0] = "true";
        generateArgs[1] = directory.getPath();

        Wsdl2Apex m = new Wsdl2Apex();
        try {
            Wsdl2Apex.parse(parseArgs);

            HashMap<String, String> a = m.getResultFromParse();
            a.keySet().iterator();
            for (String ns : a.keySet()) {
                if (ns.equals("http://soap.sforce.com/schemas/class/dogfoodServer")) {
                    a.put(ns, "bigFile");
                }
            }

            Wsdl2Apex.generate(generateArgs);
            FileString f = new FileString();

            String result1 = f.getStringFromFile(directory.getPath() + "/" + "bigFile.cls");
            String result2 = f.getStringFromFile(directory.getPath() + "/" + "AsyncBigFile.cls");
            String answer1 = f.getStringFromFile(directory.getPath() + "/../answers/bigFile.cls");
            String answer2 = f.getStringFromFile(directory.getPath() + "/../answers/AsyncBigFile.cls");
            assertEquals("Test testBigFile failed", result1, answer1);
            assertEquals("Test testBigFile failed", result2, answer2);
        } catch (Exception e) {
            System.err.println("Unable to generate the file");
            e.printStackTrace();
            fail("Test testBigFile failed");
        }
    }

    @Test
    public void testMultipleNamespace() {
        String[] parseArgs = new String[] { directory.getPath() + "/../goodWsdls/multiple-namespaces.wsdl" };
        String[] generateArgs = new String[2];
        generateArgs[0] = "true";
        generateArgs[1] = directory.getPath();

        Wsdl2Apex m = new Wsdl2Apex();
        try {
            Wsdl2Apex.parse(parseArgs);

            HashMap<String, String> a = m.getResultFromParse();
            a.keySet().iterator();
            for (String ns : a.keySet()) {
                if (ns.equals("urn:dotnet.callouttest.soap.sforce.com")) {
                    a.put(ns, "first");
                } else if (ns.equals("urn:otherdotnet.callouttest.soap.sforce.com")) {
                    a.put(ns, "second");
                }
            }

            Wsdl2Apex.generate(generateArgs);
            FileString f = new FileString();

            String result1 = f.getStringFromFile(directory.getPath() + "/" + "first.cls");
            String result2 = f.getStringFromFile(directory.getPath() + "/" + "second.cls");
            String result3 = f.getStringFromFile(directory.getPath() + "/" + "AsyncFirst.cls");
            String answer1 = f.getStringFromFile(directory.getPath() + "/../answers/first.cls");
            String answer2 = f.getStringFromFile(directory.getPath() + "/../answers/second.cls");
            String answer3 = f.getStringFromFile(directory.getPath() + "/../answers/AsyncFirst.cls");
            assertEquals("Test testMultipleNamespace failed", result1, answer1);
            assertEquals("Test testMultipleNamespace failed", result2, answer2);
            assertEquals("Test testMultipleNamespace failed", result3, answer3);
        } catch (Exception e) {
            System.err.println("Unable to generate the file");
            e.printStackTrace();
            fail("Test testMultipleNamespace failed");
        }
    }

    @Test
    public void testNameElement() {
        String[] parseArgs = new String[] { directory.getPath() + "/../goodWsdls/nameElement.wsdl" };
        String[] generateArgs = new String[2];
        generateArgs[0] = "true";
        generateArgs[1] = directory.getPath();

        Wsdl2Apex m = new Wsdl2Apex();
        try {
            Wsdl2Apex.parse(parseArgs);

            HashMap<String, String> a = m.getResultFromParse();
            a.keySet().iterator();
            for (String ns : a.keySet()) {
                if (ns.equals("http://doc.sample.com/docSample")) {
                    a.put(ns, "dSample");
                }
            }

            Wsdl2Apex.generate(generateArgs);
            FileString f = new FileString();

            String result1 = f.getStringFromFile(directory.getPath() + "/" + "dSample.cls");
            String result2 = f.getStringFromFile(directory.getPath() + "/" + "AsyncDSample.cls");
            String answer1 = f.getStringFromFile(directory.getPath() + "/../answers/dSample.cls");
            String answer2 = f.getStringFromFile(directory.getPath() + "/../answers/AsyncDSample.cls");
            assertEquals("Test testNameElement failed", result1, answer1);
            assertEquals("Test testNameElement failed", result2, answer2);
        } catch (Exception e) {
            System.err.println("Unable to generate the file");
            e.printStackTrace();
            fail("Test testNameElement failed");
        }
    }

    @Test
    public void testNamespaceDependencies() {
        String[] parseArgs = new String[] { directory.getPath() + "/../goodWsdls/namespaceDependencies.wsdl" };
        String[] generateArgs = new String[2];
        generateArgs[0] = "true";
        generateArgs[1] = directory.getPath();

        Wsdl2Apex m = new Wsdl2Apex();
        try {
            Wsdl2Apex.parse(parseArgs);

            HashMap<String, String> a = m.getResultFromParse();
            a.keySet().iterator();
            for (String ns : a.keySet()) {
                if (ns.equals("urn:sobject.partner.soap.sforce.com")) {
                    a.put(ns, "sParnterSoap");
                } else if (ns.equals("urn:partner.soap.sforce.com")) {
                    a.put(ns, "partnerSoap");
                }
            }

            Wsdl2Apex.generate(generateArgs);
            FileString f = new FileString();

            String result1 = f.getStringFromFile(directory.getPath() + "/" + "sParnterSoap.cls");
            String result2 = f.getStringFromFile(directory.getPath() + "/" + "partnerSoap.cls");
            String result3 = f.getStringFromFile(directory.getPath() + "/" + "AsyncPartnerSoap.cls");
            String answer1 = f.getStringFromFile(directory.getPath() + "/../answers/sParnterSoap.cls");
            String answer2 = f.getStringFromFile(directory.getPath() + "/../answers/partnerSoap.cls");
            String answer3 = f.getStringFromFile(directory.getPath() + "/../answers/AsyncPartnerSoap.cls");
            assertEquals("Test testNamespaceDependencies failed", result1, answer1);
            assertEquals("Test testNamespaceDependencies failed", result2, answer2);
            assertEquals("Test testNamespaceDependencies failed", result3, answer3);
        } catch (Exception e) {
            System.err.println("Unable to generate the file");
            e.printStackTrace();
            fail("Test testNamespaceDependencies failed");
        }
    }

    @Test
    public void testNoOperation() {
        String[] parseArgs = new String[] { directory.getPath() + "/../goodWsdls/noOperations.wsdl" };
        String[] generateArgs = new String[2];
        generateArgs[0] = "true";
        generateArgs[1] = directory.getPath();

        Wsdl2Apex m = new Wsdl2Apex();
        try {
            Wsdl2Apex.parse(parseArgs);

            HashMap<String, String> a = m.getResultFromParse();
            a.keySet().iterator();
            for (String ns : a.keySet()) {
                if (ns.equals("urn:dotnet.callouttest.soap.sforce.com")) {
                    a.put(ns, "calloutSoap");
                }
            }

            Wsdl2Apex.generate(generateArgs);
            FileString f = new FileString();

            String result1 = f.getStringFromFile(directory.getPath() + "/" + "calloutSoap.cls");
            String result2 = f.getStringFromFile(directory.getPath() + "/" + "AsyncCalloutSoap.cls");
            String answer1 = f.getStringFromFile(directory.getPath() + "/../answers/calloutSoap.cls");
            String answer2 = f.getStringFromFile(directory.getPath() + "/../answers/AsyncCalloutSoap.cls");
            assertEquals("Test noOperation failed", result1, answer1);
            assertEquals("Test noOperation failed", result2, answer2);
        } catch (Exception e) {
            System.err.println("Unable to generate the file");
            e.printStackTrace();
            fail("Test noOperation failed");
        }
    }

    @Test
    public void testSqlMutations() {
        String[] parseArgs = new String[] { directory.getPath() + "/../goodWsdls/sqlMutations.wsdl" };
        String[] generateArgs = new String[2];
        generateArgs[0] = "true";
        generateArgs[1] = directory.getPath();

        Wsdl2Apex m = new Wsdl2Apex();
        try {
            Wsdl2Apex.parse(parseArgs);

            HashMap<String, String> a = m.getResultFromParse();
            a.keySet().iterator();
            for (String ns : a.keySet()) {
                if (ns.equals("http://in2test.lsi.uniovi.es/sqlmutationws")) {
                    a.put(ns, "sqlMutations");
                }
            }

            Wsdl2Apex.generate(generateArgs);
            FileString f = new FileString();

            String result1 = f.getStringFromFile(directory.getPath() + "/" + "sqlMutations.cls");
            String result2 = f.getStringFromFile(directory.getPath() + "/" + "AsyncSqlMutations.cls");
            String answer1 = f.getStringFromFile(directory.getPath() + "/../answers/sqlMutations.cls");
            String answer2 = f.getStringFromFile(directory.getPath() + "/../answers/AsyncSqlMutations.cls");
            assertEquals("Test sqlMutations failed", result1, answer1);
            assertEquals("Test sqlMutations failed", result2, answer2);
        } catch (Exception e) {
            System.err.println("Unable to generate the file");
            e.printStackTrace();
            fail("Test sqlMutations failed");
        }
    }

    @Test
    public void testTestNameMangle() {
        String[] parseArgs = new String[] { directory.getPath() + "/../goodWsdls/testNameMangle.wsdl" };
        String[] generateArgs = new String[2];
        generateArgs[0] = "true";
        generateArgs[1] = directory.getPath();

        Wsdl2Apex m = new Wsdl2Apex();
        try {
            Wsdl2Apex.parse(parseArgs);

            HashMap<String, String> a = m.getResultFromParse();
            a.keySet().iterator();
            for (String ns : a.keySet()) {
                if (ns.equals("http://doc.sample.com/docSample")) {
                    a.put(ns, "testNameMangle");
                }
            }

            Wsdl2Apex.generate(generateArgs);
            FileString f = new FileString();

            String result1 = f.getStringFromFile(directory.getPath() + "/" + "testNameMangle.cls");
            String result2 = f.getStringFromFile(directory.getPath() + "/" + "AsyncTestNameMangle.cls");
            String answer1 = f.getStringFromFile(directory.getPath() + "/../answers/testNameMangle.cls");
            String answer2 = f.getStringFromFile(directory.getPath() + "/../answers/AsyncTestNameMangle.cls");
            assertEquals("Test testNameMangle failed", result1, answer1);
            assertEquals("Test testNameMangle failed", result2, answer2);
        } catch (Exception e) {
            System.err.println("Unable to generate the file");
            e.printStackTrace();
            fail("Test testNameMangle failed");
        }
    }

}
