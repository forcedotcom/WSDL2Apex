package com.salesforce.ide.wsdl2apex.core;

import static org.junit.Assert.*;

import java.io.File;
import java.nio.file.Files;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.junit.*;

public class Wsdl2ApexOpenSourceTest {
	File directory;
	
	@Before
    public void setUp() {
		directory = new File("../../src/test/testFiles/tempTest");
        directory.mkdir();
	}
	
	@After
	public void tearDown(){
		FileString.recursiveDelete(directory);
	}
	
	@Test
	public void test1() throws IOException, CalloutException //airport
	{
		String[] args = new String[3];
		args[0] = directory.getPath() + "/../goodWsdls/airport.wsdl";
		args[1] = directory.getPath();
		args[2] = "true";
		MainWsdl2ApexOpenSource m = new MainWsdl2ApexOpenSource();
		try
		{
			m.parseAndGenerate(args);
			FileString f = new FileString();
		
			String result1 = f.getStringFromFile(directory.getPath() + "/" + "wwwWebservicexNet.cls");
			String answer1 = f.getStringFromFile(directory.getPath() + "/../answers/wwwWebservicexNet_answer.cls");
			String result2 = f.getStringFromFile(directory.getPath() + "/" + "AsyncWwwWebservicexNet.cls");
			String answer2 = f.getStringFromFile(directory.getPath() + "/../answers/AsyncWwwWebservicexNet_answer.cls");
			assertEquals("Test 1 Passed", result1, answer1);
			assertEquals("Test 1 async passed", result2, answer2);
		}
		catch (FileNotFoundException e) {
			System.err.println("Unable to generate the file");
			e.printStackTrace();
			fail("test1 failed");
		}
	}

	@Test
	public void test2() //should fail
	{
		String[] args = new String[3];
		args[0] = directory.getPath() + "/../badWsdls/airport_Raw.wsdl";
		args[1] = directory.getPath();
		args[2] = "false";
		MainWsdl2ApexOpenSource m = new MainWsdl2ApexOpenSource();
		try
		{
			m.parseAndGenerate(args);
			fail("Test2 failed");
		}
		catch (Exception e)
		{
			System.err.println(e.getMessage());
			assertTrue("Test2 failed", true);
		}
	}
	@Test
	public void test3() throws IOException, CalloutException //off internet
	{
		String[] args = new String[3];
		args[0] = directory.getPath() + "/../goodWsdls/stockQuote.wsdl";
		args[1] = directory.getPath();
		args[2] = "true";
		MainWsdl2ApexOpenSource m = new MainWsdl2ApexOpenSource();
		try
		{
			m.parseAndGenerate(args);
			FileString f = new FileString();
		
			String result1 = f.getStringFromFile(directory.getPath() + "/" + "AsyncExampleComStockquoteWsdl.cls");
			String result2 = f.getStringFromFile(directory.getPath() + "/" + "AsyncExampleComStockquoteXsd.cls");
			String result3 = f.getStringFromFile(directory.getPath() + "/" + "exampleComStockquoteWsdl.cls");
			String result4 = f.getStringFromFile(directory.getPath() + "/" + "exampleComStockquoteXsd.cls");
			
			String answer1 = f.getStringFromFile(directory.getPath() + "/../answers/AsyncExampleComStockquoteWsdl_answer.cls");
			String answer2 = f.getStringFromFile(directory.getPath() + "/../answers/AsyncExampleComStockquoteXsd_answer.cls");
			String answer3 = f.getStringFromFile(directory.getPath() + "/../answers/exampleComStockquoteWsdl_answer.cls");
			String answer4 = f.getStringFromFile(directory.getPath() + "/../answers/exampleComStockquoteXsd_answer.cls");
			assertEquals("Test 3a Passed, Async class", result1, answer1);
			assertEquals("Test 3b passed, Async class", result2, answer2);
			assertEquals("Test 3c Passed", result3, answer3);
			assertEquals("Test 3d passed", result4, answer4);
		}
		catch (FileNotFoundException e) {
			System.err.println("Unable to find the file");
			e.printStackTrace();
			fail("test1 failed");
		}
	}
	@Test
	public void test4() throws IOException, CalloutException //large file
	{
		String[] args = new String[3];
		args[0] = directory.getPath() + "/../goodWsdls/EOLS_PSAPLookupUS.wsdl";
		args[1] = directory.getPath();
		args[2] = "true";
		MainWsdl2ApexOpenSource m = new MainWsdl2ApexOpenSource();
		try
		{
			m.parseAndGenerate(args);
			FileString f = new FileString();
		
			String result1 = f.getStringFromFile(directory.getPath() + "/" + "spectrumPbCom.cls");
			String result2 = f.getStringFromFile(directory.getPath() + "/" + "wwwMapinfoComMidevServiceGeometries.cls");
			String result3 = f.getStringFromFile(directory.getPath() + "/" + "wwwMapinfoComMidevServiceUnitsV1.cls");
			String result4 = f.getStringFromFile(directory.getPath() + "/" + "wwwPbComSpectrumServices.cls");
			String result5 = f.getStringFromFile(directory.getPath() + "/" + "wwwPbComSpectrumServicesEolsPsaplo.cls");
			String result6 = f.getStringFromFile(directory.getPath() + "/" + "AsyncSpectrumPbCom.cls");
			String result7 = f.getStringFromFile(directory.getPath() + "/" + "AsyncWwwPbComSpectrumServicesEolsPsaplo.cls");
			
			String answer1 = f.getStringFromFile(directory.getPath() + "/../answers/spectrumPbCom_answer.cls");
			String answer2 = f.getStringFromFile(directory.getPath() + "/../answers/wwwMapinfoComMidevServiceGeometries_answer.cls");
			String answer3 = f.getStringFromFile(directory.getPath() + "/../answers/wwwMapinfoComMidevServiceUnitsV1_answer.cls");
			String answer4 = f.getStringFromFile(directory.getPath() + "/../answers/wwwPbComSpectrumServices_answer.cls");
			String answer5 = f.getStringFromFile(directory.getPath() + "/../answers/wwwPbComSpectrumServicesEolsPsaplo_answer.cls");
			String answer6 = f.getStringFromFile(directory.getPath() + "/../answers/AsyncSpectrumPbCom_answer.cls");
			String answer7 = f.getStringFromFile(directory.getPath() + "/../answers/AsyncWwwPbComSpectrumServicesEolsPsaplo_answer.cls");
			assertEquals("Test 4a Passed", result1, answer1);
			assertEquals("Test 4b passed", result2, answer2);
			assertEquals("Test 4c passed", result3, answer3);
			assertEquals("Test 4d passed", result4, answer4);
			assertEquals("Test 4e passed", result5, answer5);
			assertEquals("Test 4f passed, Async class", result6, answer6);
			assertEquals("Test 4g passed, Async class", result7, answer7);
		}
		catch (FileNotFoundException e) {
			System.err.println("Unable to find the file");
			e.printStackTrace();
			fail("test1 failed");
		}
	}
	@Test
	public void test5() //should fail
	{
		String[] args = new String[3];
		args[0] = directory.getPath() + "/../badWsdls/endorsementSearch.wsdl";
		args[1] = directory.getPath();
		args[2] = "false";
		MainWsdl2ApexOpenSource m = new MainWsdl2ApexOpenSource();
		try
		{
			m.parseAndGenerate(args);
			fail("Test5 failed");
		}
		catch (Exception e)
		{
			System.err.println(e.getMessage());
			assertTrue("Test5 failed", true);
		}
	}
	@Test
	public void test6() //should fail
	{
		String[] args = new String[3];
		args[0] = directory.getPath() + "/../badWsdls/helloService.wsdl";
		args[1] = directory.getPath();
		args[2] = "false";
		MainWsdl2ApexOpenSource m = new MainWsdl2ApexOpenSource();
		try
		{
			m.parseAndGenerate(args);
			fail("Test6 failed");
		}
		catch (Exception e)
		{
			System.err.println(e.getMessage());
			assertTrue("Test6 failed", true);
		}
	}
	@Test
	public void test7() //should fail
	{
		String[] args = new String[3];
		args[0] = directory.getPath() + "/../badWsdls/ADC_BibCode.wsdl";
		args[1] = directory.getPath();
		args[2] = "false";
		MainWsdl2ApexOpenSource m = new MainWsdl2ApexOpenSource();
		try
		{
			m.parseAndGenerate(args);

			fail("Test7 failed");
		}
		catch (Exception e)
		{
			System.err.println(e.getMessage());
			assertTrue("Test7 failed", true);
		}
	}
	
	@Test
	public void test8()								//test async is false
	{
		String[] args = new String[3];
		args[0] = directory.getPath() + "/../goodWsdls/airport.wsdl";
		args[1] = directory.getPath();
		args[2] = "false";
		MainWsdl2ApexOpenSource m = new MainWsdl2ApexOpenSource();
		try
		{
			m.parseAndGenerate(args);
			FileString f = new FileString();
		
			String result1 = f.getStringFromFile(directory.getPath() + "/" + "wwwWebservicexNet.cls");
			String answer1 = f.getStringFromFile(directory.getPath() + "/../answers/wwwWebservicexNet_answer.cls");
			assertEquals("Test 8 Passed", result1, answer1);
		}
		catch (FileNotFoundException e) {
			System.err.println("Unable to generate the file");
			e.printStackTrace();
			fail("test1 failed");
		} catch (IOException e) {
			System.err.println("Unable to generate the file");
			e.printStackTrace();
			fail("test1 failed");			
		} catch (CalloutException e) {
			System.err.println("Unable to generate the file");
			e.printStackTrace();
			fail("test1 failed");
		}
	}
	
	@Test
	public void test9()                             //test parse and generate methods
	{
		String[] parseArgs = new String[] {directory.getPath() + "/../goodWsdls/airport.wsdl"};
		String[] generateArgs = new String[3];
		generateArgs[0] = "airportName";
		generateArgs[1] = "true";
		generateArgs[2] = directory.getPath();

		MainWsdl2ApexOpenSource m = new MainWsdl2ApexOpenSource();
		try
		{
			m.parse(parseArgs);
			m.generate(generateArgs);
			FileString f = new FileString();
		
			String result1 = f.getStringFromFile(directory.getPath() + "/" + "airportName.cls");
			String answer1 = f.getStringFromFile(directory.getPath() + "/../answers/airportName_answer.cls");
			assertEquals("Test 9 Passed", result1, answer1);
		}
		catch (FileNotFoundException e) {
			System.err.println("Unable to generate the file");
			e.printStackTrace();
			fail("test1 failed");
		} catch (IOException e) {
			System.err.println("Unable to generate the file");
			e.printStackTrace();
			fail("test1 failed");			
		} catch (CalloutException e) {
			System.err.println("Unable to generate the file");
			e.printStackTrace();
			fail("test1 failed");
		}
	}
}

