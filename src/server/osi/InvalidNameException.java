package server.osi;

public class InvalidNameException extends UserManagerException
{
    public InvalidNameException(String msg)
    {
	super(msg);
    }
}
