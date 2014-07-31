package com.salesforce.ide.wsdl2apex.core;
import java.io.*;
import java.util.*;


public class FileString {

	public String getStringFromFile(String Path) throws FileNotFoundException
	{
		FileInputStream fis = new FileInputStream(Path);
		Scanner s = new Scanner(fis);
		s.useDelimiter("\\Z");
		String file = new String();
		while(s.hasNext())
		{
			file += s.next();
		}
        return file;
	}
	
	public void StringToFile(String path, String content, String fileName) throws FileNotFoundException, UnsupportedEncodingException
	{
		PrintWriter writer;
		String totalName = new String(path + fileName + ".cls");
		try {
			writer = new PrintWriter(totalName, "UTF-8");
			writer.print(content.trim());
			writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			System.err.println("Unable to create the new apex class file");
			e.printStackTrace();
			throw e;
		}

	}
}
