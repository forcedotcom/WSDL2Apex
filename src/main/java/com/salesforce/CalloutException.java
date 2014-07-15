
/**
 * CalloutException
 *
 * @author cheenath
 * @version 1.0
 * @since 146  Feb 7, 2007
 */
public class CalloutException extends Exception {

    public CalloutException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public CalloutException(String message) {
        super(message);
    }
}
