package server.osi;


import java.io.*;
import java.util.*;

public class DummyUserManager implements UserManagerImplementation
{
    private File usersRoot;
    private File groupsRoot;

    public DummyUserManager()
    {
	// get the home of the running user
	File home = new File(System.getProperty("user.home"));

	// re-create the filesystem if necessary
	if (home.canWrite()) {
	    File root = new File(home, "mars_fs");

	    usersRoot = new File(root, "users");
	    groupsRoot = new File(root, "groups");

	    if (!root.exists()) {
		root.mkdir();
		usersRoot.mkdir();
		groupsRoot.mkdir();
	    }
	} else {
	    System.out.println("*** UAH! DANGER! CAN'T WRITE TO HOME DIRECTORY. ***");
	}
    }

    public void addUser(String login, String password)
	throws InvalidNameException, InvalidPasswordException, DuplicateUserException, IOException
    {
	System.out.println("Adding user " + login + "/" + password);

	// initialize home directory
	File home = new File(usersRoot, login);
	home.mkdir();

	File personal = new File(home, FileWalker.personalDirectoryName);
	personal.mkdir();

	File groups = new File(home, FileWalker.groupsDirectoryName);
	groups.mkdir();

	File audio = new File(personal, FileWalker.audioDirectoryName);
	audio.mkdir();

	File video = new File(personal, FileWalker.videoDirectoryName);
	video.mkdir();

	File images = new File(personal, FileWalker.imagesDirectoryName);
	images.mkdir();
    }

    private boolean recursiveDeleteDir(File dir)
    {
	if (dir.isDirectory()) {
            String[] entries = dir.list();
            for (int i = 0; i < entries.length; i++)
                if (!recursiveDeleteDir(new File(dir, entries[i])))
                    return false;
        }

        return dir.delete();
    }

    public void removeUser(String login)
	throws InvalidNameException, IOException
    {
	System.out.println("Removing user " + login);

	// RANT: ARGH!!! Fucking Java has no fucking recursive delete..
	// fuck them. fuck them up their stupid asses.
	recursiveDeleteDir(new File(usersRoot, login));
    }

    public boolean userExists(String login)
    {
	File home = new File(usersRoot, login);

	return home.exists();
    }

    public String[] getUserList()
    {
	return usersRoot.list();
    }

    public String getHomeOfUser(String login)
	throws InvalidNameException
    {
	File home = new File(usersRoot, login);

	try {
	    return home.getCanonicalPath();
	} catch (IOException ex) {
	    return null;
	}
    }

    public void setGroupsForUser(String login, String[] groups)
	throws InvalidNameException, IOException
    {
	System.out.println("Adding user " + login + " to groups: ");

	for (int i = 0; i < groups.length; i++)
	    System.out.println(" * " + groups[i]);
    }

    public void addGroup(String name)
	throws InvalidNameException, DuplicateGroupException, IOException
    {
	System.out.println("Adding group " + name);

	File group_home = new File(groupsRoot, name);
	group_home.mkdir();
    }

    public void removeGroup(String name)
	throws InvalidNameException, IOException
    {
	System.out.println("Removing group " + name);

	recursiveDeleteDir(new File(groupsRoot, name));
    }

    public boolean groupExists(String name)
    {
	File group_home = new File(groupsRoot, name);

	return group_home.exists();
    }


    public int runProgram(String program, String[] args, String user)
    {
	String cmd = program;

	for (int i = 0; i < args.length; ++i) {
	    cmd += " " + args;
	}

	try {
	    Process proc = Runtime.getRuntime().exec(cmd);

	    return proc.waitFor();
	} catch (IOException ex) {
	    ex.printStackTrace();
	    return 1;
	} catch (InterruptedException ex) {
	    ex.printStackTrace();
	    return 1;
	}
    }
}
