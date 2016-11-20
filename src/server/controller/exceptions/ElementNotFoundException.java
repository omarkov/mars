package server.controller.exceptions;

public class ElementNotFoundException extends ControllerException{
	
	private static final String defaultMessage ="The element could not me read from DB. ID:";


	public ElementNotFoundException(String message) {
		super(defaultMessage+message);
	}

}
