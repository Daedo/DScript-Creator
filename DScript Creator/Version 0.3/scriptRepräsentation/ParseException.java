package scriptRepräsentation;

public class ParseException extends Exception {

	public ParseException(String message) {
		super(message);
	}
	
	public ParseException(String message, Exception exception) {
		super(message, exception);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
