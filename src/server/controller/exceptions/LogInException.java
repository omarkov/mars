package server.controller.exceptions;

public class LogInException extends ControllerException{
	
	private static final String defaultMessage ="Login failed";


	public LogInException(String message) {
		super(message);
	}

}
