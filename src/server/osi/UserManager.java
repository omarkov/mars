package server.osi;

import java.io.*;

public final class UserManager
{
    private static UserManagerImplementation impl;

    static {
	String os = System.getProperty("os.name");

	if (os.equals("Linux"))
	    impl = new LinuxUserManager();
	else
	    impl = new DummyUserManager();
    }

    /**
     * Adds a user to the underlying operating system layer. Semantics like what
     * constitutes a valid username vary from OS to OS, so no cleary defined rules
     * can be given here.
     *
     * @param login username
     * @param password password of user
     * @throws InvalidNameException Username contained invalid characters or was too long.
     * @throws InvalidPasswordException Password contained invalid characters or was too long.
     * @throws DuplicateUserException A user with that name already exists.
     * @throws IOException General error during the process.
     */
    public static void addUser(String login, String password)
	throws InvalidNameException, InvalidPasswordException, DuplicateUserException, IOException
    {
	impl.addUser(login, password);
    }

    /**
     * Removes a user from the operating system and deletes the associated home
     * directory. Files belonging to the user in other places are left unchanged.
     *
     * WARNING! WARNING! WARNING!
     *  This does NOT check if the user is a Mar-S user or not, so uncareful use
     *  of this method can lead to disastrous results.
     * WARNING! WARNING! WARNING!
     *
     * @param login username
     * @throws InvalidNameException No user with that name is known.
     * @throws IOException General error during the process.
     */
    public static void removeUser(String login)
	throws InvalidNameException, IOException
    {
	impl.removeUser(login);
    }

    /**
     * Checks to see if a given username exists.
     *
     * @param login username
     * @returns true if login is designates an existing user, false otherwise
     */
    public static boolean userExists(String login)
    {
	return impl.userExists(login);
    }

    /**
     * Returns a list of all Mar-S users on the system.
     *
     * @returns a list of usernames, or null if there are no Mar-S users
     */
    public static String[] getUserList()
    {
	return impl.getUserList();
    }

    /**
     * Returns the home directory associated with the given username.
     *
     * @param login username
     * @returns String absolute path of the home directory
     * @throws InvalidNameException No user with that name is known.
     */
    public static String getHomeOfUser(String login)
	throws InvalidNameException
    {
	return impl.getHomeOfUser(login);
    }

    /**
     * Adds the user to all groups given. If the user previously was member
     * of a group not given in the array, he will be removed from that group.
     * 
     * @param login username
     * @param groups list of groups, to which the user should be added to
     * @throws InvalidNameException Unknown user or unknown group
     * @throws IOException general error during the process
     */
    public static void setGroupsForUser(String login, String[] groups)
	throws InvalidNameException, IOException
    {
	impl.setGroupsForUser(login, groups);
    }

    /**
     * Adds a group to the underlying operating system.
     *
     * @param name groupname
     * @throws InvalidNameException Invalid groupname
     * @throws DuplicateGroupException A group with that name already exists
     * @throws IOException General error during the process
     **/
    public static void addGroup(String name)
	throws InvalidNameException, DuplicateGroupException, IOException
    {
	impl.addGroup(name);
    }

    /**
     * Removes a group from the operating system and deletes all files in
     * the associated group directory.
     *
     * WARNING! WARNING! WARNING!
     *  This does NOT check if the user is a Mar-S user or not, so uncareful use
     *  of this method can lead to disastrous results.
     * WARNING! WARNING! WARNING!
     *
     * @param name groupname
     * @throws InvalidNameException No such group is known
     * @throws IOException General error during the process
     **/
    public static void removeGroup(String name)
	throws InvalidNameException, IOException
    {
	impl.removeGroup(name);
    }

    /**
     * Checks to see if a given group exists.
     *
     * @param name groupname
     * @returns true if name is designating an existing group, false otherwise
     */
    public static boolean groupExists(String name)
    {
	return impl.groupExists(name);
    }

    /**
     * Returns a instance of a FileWalker to enumerate the files available
     * to the given user.
     *
     * @param name username
     * @returns an instance of a FileWalker
     */
    public static FileWalker getFileWalkerForUser(String name)
    {
	FileWalker walker = null;

	try {
	    if (userExists(name))
		walker = new FileWalker(new File(getHomeOfUser(name)));
	} catch (IOException ex) {
	    ex.printStackTrace();
	} catch (InvalidNameException ex) {
	    ex.printStackTrace();
	}

	return walker;
    }


    public static int runProgram(String program, String[] args, String user)
    {
	return impl.runProgram(program, args, user);
    }
}
