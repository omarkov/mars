/*
 * Created on 04.08.2005
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
public class ElementUpdateException extends ControllerException {

	private static final String defaultMessage ="Der Update des Elements konnte nicht durchgeführt werden:";

	public ElementUpdateException(String message) {
		super(defaultMessage+ message);
		// TODO Auto-generated constructor stub
	}

}
