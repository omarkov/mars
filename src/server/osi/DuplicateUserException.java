package server.osi;

public class DuplicateUserException extends UserManagerException
{
    public DuplicateUserException(String msg)
    {
	super(msg);
    }
}
