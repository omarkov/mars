/*
 * Created on 09.08.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package server.controller.exceptions;

/**
 * @author Rajkumar
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class IllegalArgumentsException extends ControllerException {

	
	private static final String defaultMessage ="Die übergebenen Parameter sind ungültig";

	/**
	 * @param message
	 */
	public IllegalArgumentsException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

}
