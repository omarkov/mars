package server.osi;

import java.io.IOException;

public interface UserManagerImplementation
{
    public void addUser(String login, String password)
	throws InvalidNameException, InvalidPasswordException, DuplicateUserException, IOException;

    public void removeUser(String login)
	throws InvalidNameException, IOException;

    public boolean userExists(String login);

    public String[] getUserList();

    public String getHomeOfUser(String login)
	throws InvalidNameException;

    public void setGroupsForUser(String login, String[] groups)
	throws InvalidNameException, IOException;

    public void addGroup(String name)
	throws InvalidNameException, DuplicateGroupException, IOException;

    public void removeGroup(String name)
	throws InvalidNameException, IOException;

    public boolean groupExists(String name);

    public int runProgram(String program, String[] args, String user);
}
