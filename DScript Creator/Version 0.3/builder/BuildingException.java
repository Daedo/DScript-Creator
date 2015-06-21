package builder;

public class BuildingException extends Exception {

	public BuildingException(String message) {
		super(message);
	}
	
	public BuildingException(String message,Throwable throwable) {
		super(message,throwable);
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
