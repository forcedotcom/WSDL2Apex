
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
