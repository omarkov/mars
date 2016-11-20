package server.osi;

public class DuplicateGroupException extends UserManagerException
{
    public DuplicateGroupException(String msg)
    {
	super(msg);
    }
}
