/*
 * Created on 04.08.2005
 */
package server.controller.exceptions;

import server.*;


/**
 * @author Rajkumar
 */
public class ControllerException extends MarsException {
	
	private String message;
	private static final String defaultMessage ="";

	
	
	/**
	 * @param message
	 */
	public ControllerException(String message) {
		super(defaultMessage + message);
		this.message = defaultMessage + message;
	}
	/**
	 * @return Returns the message.
	 */
	public String getMessage() {
		return message;
	}


}
