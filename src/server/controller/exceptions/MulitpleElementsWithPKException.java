package server.controller.exceptions;

public class MulitpleElementsWithPKException extends ControllerException{
	
	private static final String defaultMessage ="Uneindeutige ID:";


	public MulitpleElementsWithPKException(String message) {
		super(defaultMessage+ message);
	}

}
