package server.controller.exceptions;

public class ElementValidationException extends ControllerException {
	
	private static final String defaultMessage ="Das Element ist ungültig.";

	public ElementValidationException(String message) {
		super(defaultMessage+ message);
	}
}



