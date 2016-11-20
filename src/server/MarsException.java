/*
 * Created on 04.08.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package server;

/**
 * @author Rajkumar
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MarsException extends Exception {
	
	private String message;
	private static final String defaultMessage ="";

	
	
	/**
	 * @param message
	 */
	public MarsException(String message) {
		super();
		this.message = defaultMessage + message;
	}
	/**
	 * @return Returns the message.
	 */
	public String getMessage() {
		return message;
	}


}
