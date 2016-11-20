package server.controller.exceptions;

public class RoomOccupiedException extends ControllerException{
	
	private static final String defaultMessage ="room occupied by: ";


	public RoomOccupiedException(server.domain.User user) {
		super(defaultMessage + user);
	}

}
