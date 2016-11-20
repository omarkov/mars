/*
 * Created on 06.08.2005
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
public class ElementDeletionException extends ControllerException {

	
	private static final String defaultMessage ="Das Element konnte nicht gelöscht werden.";

	/**
	 * @param message
	 */
	public ElementDeletionException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

}
