package com.salesforce.ide.wsdl2apex.core;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

public class MainWsdl2ApexOpenSource {

	public static void main(String[] args) throws IOException, CalloutException{
		if(args.length != 3)
			System.err.println("Must have 3 arguement containing the path to the wsdl file, the path for the new file, and whether you want an asynchronous class");
		String filePath = args[0];													//wsdl file to parse
		String resultPath = args[1];												//location of the new class
		Boolean async = Boolean.parseBoolean(args[2]);								//async or not
		FileString f = new FileString();
		String wsdlString;
		Wsdl2Apex.AnalysisResult result = null;
		try
		{
			wsdlString = f.getStringFromFile(filePath);								//get the wsdl in a stirng
		}
		catch(FileNotFoundException e)
		{
			System.err.println("Cannot find the file");
			throw e;
		}
		try
		{
			result = new Wsdl2Apex().analyze(wsdlString);							//parse the wsdl
			if(!result.isSuccess())
			{
				ArrayList<String> messages = new ArrayList<String>();
				messages = result.getMessages();
				for(String message : messages)
				{
					System.out.println(message);
				}
				throw new RuntimeException();
			}
		}
		catch (Exception e)
		{
			System.err.println(e.getMessage());
			throw e;
		}
		HashMap<String, String> inputMap = new HashMap<String, String>();
		inputMap = result.getMapping();												//get the targetnamesapces
		Wsdl2Apex wsdl2Apex = new Wsdl2Apex();
		LinkedHashMap<String, APackage> packageMap;
		ArrayList<String>resultApexClass;
		try
		{
			packageMap = wsdl2Apex.generate(wsdlString, Wsdl2ApexOptions.newDefault(async).setPackageNamespaceMap(inputMap)); //generates the class
			resultApexClass = wsdl2Apex.generateApexClass(packageMap);				//creates the class as a string
		}
		catch(CalloutException e)
		{
			System.err.println("Failed to generate code");
			System.out.println(e.getMessage());
			throw e;
		}
		catch(Exception e)
		{
			System.err.println(e.getMessage());
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		try
		{
			Iterator i = resultApexClass.iterator();
			Iterator j = packageMap.keySet().iterator();
			while(i.hasNext() && j.hasNext())
			{
				f.StringToFile(resultPath, i.next().toString(), j.next().toString());		//writes the class into a file
			}
		}
		catch(FileNotFoundException | UnsupportedEncodingException e)
		{
			System.err.println("Cannot find the file");
			throw e;
		}
	}
}

