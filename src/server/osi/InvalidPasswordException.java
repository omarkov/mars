package server.osi;

public class InvalidPasswordException extends UserManagerException
{
    public InvalidPasswordException(String msg)
    {
	super(msg);
    }
}
