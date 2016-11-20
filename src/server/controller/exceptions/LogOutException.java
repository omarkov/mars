package server.controller.exceptions;

public class LogOutException extends ControllerException{
	
	private static final String defaultMessage ="Logout failed";


	public LogOutException(String message) {
		super(message);
	}

}
