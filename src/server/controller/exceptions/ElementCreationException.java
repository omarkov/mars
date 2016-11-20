package server.controller.exceptions;

public class ElementCreationException extends ControllerException{
	
	private static final String defaultMessage ="Komponente ist Fehlerhaft";


	public ElementCreationException(String message) {
		super(message);
	}

}
