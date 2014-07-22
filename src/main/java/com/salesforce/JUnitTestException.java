package com.salesforce.ide.wsdl2apex.core;

public class JUnitTestException extends Exception {

	public JUnitTestException(String message, Throwable throwable)
	{
		super(message, throwable);
	}
	
	public JUnitTestException(String message)
	{
		super(message);
	}
}
