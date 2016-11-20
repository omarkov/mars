package server.osi;

import java.io.*;
import java.util.HashMap;

public class LinuxUserManager implements UserManagerImplementation
{
    // ------------------------------------------------------------------------
    // String constants
    // ------------------------------------------------------------------------

    private static final String adminPath = "/sbin:/usr/sbin:/usr/local/sbin:.";
    private static final String passwdPath = "/etc/passwd";

    private static String groupsDirectory = server.Config.get("fs_groupdir");


    // ------------------------------------------------------------------------
    // Native calls
    // ------------------------------------------------------------------------

    private static native int getUIDForName(String name);
    private static native int getGIDForName(String name);
    private static native int chown(String path, int uid, int gid);
    private static native int chmod(String path, int modemask);
    private static native String crypt(String str);
    private static native int execAsUID(String binary, String[] args, int uid);
    private static native int symlink(String name1, String name2);

    static { System.loadLibrary("LinuxUserManager"); }


    // ------------------------------------------------------------------------
    // Private helpers
    // ------------------------------------------------------------------------

    private static void validateLogin(String str) throws InvalidNameException
    {
        if (str.length() > 8)
            throw new InvalidNameException("Login can't be longer than 8 characters");

        if (!str.matches("[a-zA-Z0-9]*"))
            throw new InvalidNameException("Login must contain only alphanumeric characters");
    }

    private static void validateGroup(String str) throws InvalidNameException
    {
        if (str.length() > 32)
            throw new InvalidNameException("Groupname can't be longer than 32 characters");

        if (!str.matches("[a-zA-Z0-9_]*"))
            throw new InvalidNameException("Groupname must contain only alphanumeric characters");
    }

    private static void validatePassword(String str) throws InvalidPasswordException
    {
        if (str.length() > 32)
            throw new InvalidPasswordException("Password can't be longer than 32 characters");
    }

    private static void exec(String cmd) throws IOException
    {
        // environment
        String env[] = new String[1];
        env[0] = "PATH=" + adminPath;

        Process proc = Runtime.getRuntime().exec(cmd, env);

        // relay output to stdout (for now)
        BufferedReader stream = new BufferedReader(new InputStreamReader(proc.getInputStream()));

        String line;
        while ((line = stream.readLine()) != null)
            System.out.println(line);
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

    private void mkdir(File dir, String user, String group) throws IOException
    {
	// create the directory
	dir.mkdir();

	// re-own the directory
	chown(dir.getCanonicalPath(), getUIDForName(user), getGIDForName(group));

	// make the locking file
	File lockFile = new File(dir, ".lock");
	lockFile.createNewFile();
	chmod(lockFile.getCanonicalPath(), 0);
    }


    // ------------------------------------------------------------------------
    // User management
    // ------------------------------------------------------------------------

    private HashMap userList = new HashMap();
    private boolean isUserListDirty = true;

    private void updateUserList()
    {
        userList.clear();

        // get users by parsing the entries in /etc/passwd
        try {
            BufferedReader reader = new BufferedReader(new FileReader(passwdPath));
            String line;

            System.out.println("Opened passwd");

            while ((line = reader.readLine()) != null) {
                // ignore lines starting with a comment character
                if (line.trim().startsWith("#"))
                    continue;

                // Linux passwd format differs slightly from BSD, so we'll have to
                // differentiate by the number of fields. Nevertheless, we can rely
                // on login being the first and comment the fifth field.
                //
                // Linux: login:password:uid:gid:comment:home:shell
                // BSD:   login:password:uid:gid:class:change:expire:gecos:home:shell

                String[] fields = line.split(":");
                String login = fields[0];
                String comment = fields[4];
                String home = fields[fields.length - 2];

                // Mars users have the string "Mars" embedded into their comment field,
                // so we'll weed out those without it in order to not count system users etc.
                if (comment.matches("Mars"))
                    userList.put(login, home);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        isUserListDirty = false;
    }

    public void addUser(String login, String password)
        throws InvalidNameException, InvalidPasswordException, DuplicateUserException, IOException
    {
        // validate parameters
        validateLogin(login);
        validatePassword(password);

        if (userExists(login))
            throw new DuplicateUserException("User " + login + " already exists");

	if (groupExists(login))
	    throw new DuplicateUserException("Usergroup " + login + " already exists");

	// add the user group
        exec("groupadd " + login);

        // build the command to call useradd
        String cmd = "useradd ";
	cmd += "-g " + login + " ";
        cmd += "-m ";
        cmd += "-c Mars ";
        cmd += "-p " + crypt(password) + " ";
        cmd += "-s /bin/nologin ";
        cmd += login;

        System.out.println("Adding user " + login + ".");

        exec(cmd);

        isUserListDirty = true;

        // try to add user to samba
        if (addUserToSamba(login, password) == -1) {
            System.out.println("Fuck. Couldn't add to samba.");
            removeUser(login);
        }

        setupPersonalDirectories(login);

        System.out.println("done");
    }

    private int addUserToSamba(String name, String password) throws IOException {
        // environment
        String cmd = "smbpasswd -a -s " + name;
        String env[] = new String[1];
        env[0] = "PATH=" + adminPath;

        System.out.println("Adding " + name + " to samba database");

        Process proc = Runtime.getRuntime().exec(cmd, env);

        BufferedReader stdout = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        BufferedWriter stdin = new BufferedWriter(new OutputStreamWriter(proc.getOutputStream()));

        stdin.write(password);
        stdin.newLine();
        stdin.flush();
        stdin.write(password);
        stdin.newLine();
        stdin.flush();

        // relay output to stdout (for now)
        String line;
        while ((line = stdout.readLine()) != null)
            System.out.println(line);

        return 0;
    }

    private void setupPersonalDirectories(String login) throws InvalidNameException, IOException  {
        // initialize home directory
        File home = new File(getHomeOfUser(login));

        File personal = new File(home, FileWalker.personalDirectoryName);
	mkdir(personal, login, login);

        File groups = new File(home, FileWalker.groupsDirectoryName);
	mkdir(groups, login, login);

        File audio = new File(personal, FileWalker.audioDirectoryName);
	mkdir(audio, login, login);

        File video = new File(personal, FileWalker.videoDirectoryName);
	mkdir(video, login, login);

        File images = new File(personal, FileWalker.imagesDirectoryName);
	mkdir(images, login, login);
    }

    public void removeUser(String login) throws InvalidNameException, IOException
    {
        validateLogin(login);

	exec("smbpasswd -x " + login);
        exec("userdel -r " + login);

        isUserListDirty = true;
    }

    public boolean userExists(String login)
    {
        // go go hyper-efficient user searching
        String[] userList = getUserList();

        for (int i = 0; i < userList.length; i++)
            if (login.equals(userList[i]))
                return true;

        return false;
    }

    public String[] getUserList()
    {
        String[] result = null;

        if (isUserListDirty)
            updateUserList();

        // this can't be the "right" way to get a simple string array
        // of all keys.
        Object[] keys = userList.keySet().toArray();
        result = new String[keys.length];

        for (int i = 0; i < keys.length; i++)
            result[i] = (String)keys[i];

        return result;
    }

    public String getHomeOfUser(String login) throws InvalidNameException
    {
        if (isUserListDirty)
            updateUserList();

        if (userList.containsKey(login))
            return (String)userList.get(login);
        else
            throw new InvalidNameException("Unknown user: " + login);
    }

    public void setGroupsForUser(String login, String[] groups) throws InvalidNameException, IOException
    {
	if (!userExists(login))
	    throw new InvalidNameException("Unknown user " + login + ".");

        // generate a comma-separated list from the group array
        // Rant: why the fuck doesn't java.lang.String have a join-operation?
        String groupList = "";

        for (int i = 0; i < groups.length; i++) {
            validateGroup(groups[i]);

	    if (!groupExists(groups[i]))
		throw new InvalidNameException("Unknown group " + groups[i] + ".");

            groupList += groups[i];

            // we don't want a trailing comma now, do we?
            if (i < groups.length - 1)
                groupList += ",";
        }

        // make it so, number 1
        exec("usermod -G " + groupList + " " + login);

	File home = new File(getHomeOfUser(login));
	File groupDirectory = new File(home, FileWalker.groupsDirectoryName);

        // delete all previously linked groups (if any)
	File[] entries = groupDirectory.listFiles();
	for (int i = 0; i < entries.length; i++)
	    entries[i].delete();

        // link new groups
	String group_path = groupDirectory.getCanonicalPath();
	for (int i = 0; i < groups.length; i++) {
	    symlink(groupsDirectory + groups[i], group_path + "/" + groups[i]);
	}
    }


    // ------------------------------------------------------------------------
    // Group Management
    // ------------------------------------------------------------------------

    public void setupGroupDirectories(String name) throws IOException {
        File groups_root = new File(groupsDirectory);

        // create the group directory if it doesn't exist
        if (!groups_root.exists())
            groups_root.mkdir();

	// S_ISGID, S_ISVTX, S_IRGRP, S_IWGRP, S_IXGRP
	int mask = 0x1000 | 0x70;

        File groupDirectory = new File(groups_root, name);
	mkdir(groupDirectory, "root", name);
	chmod(groupDirectory.getCanonicalPath(), mask);

        File audio = new File(groupDirectory, FileWalker.audioDirectoryName);
        mkdir(audio, "root", name);
	chmod(audio.getCanonicalPath(), mask);

        File video = new File(groupDirectory, FileWalker.videoDirectoryName);
        mkdir(video, "root", name);
	chmod(video.getCanonicalPath(), mask);

        File images = new File(groupDirectory, FileWalker.imagesDirectoryName);
        mkdir(images, "root", name);
	chmod(images.getCanonicalPath(), mask);
    }

    public void addGroup(String name)
        throws InvalidNameException, DuplicateGroupException, IOException
    {
        validateGroup(name);

        if (groupExists(name))
            throw new DuplicateGroupException("Group " + name + " already exists.");

        exec("groupadd " + name);

        // make a new directory
        setupGroupDirectories(name);
    }

    public void removeGroup(String name) throws InvalidNameException, IOException
    {
        if (!groupExists(name))
            return;

        // get the directory of this group
        File groupDirectory = new File(groupsDirectory, name);
        if (!groupDirectory.exists())
            throw new IOException("Directory for group " + name + " not found.");

        // recursively remove the group directory
        recursiveDeleteDir(groupDirectory);

        // finally kill the group from the OS records
        exec("groupdel " + name);
    }

    public boolean groupExists(String name)
    {
        File dir = new File(groupsDirectory, name);

        return dir.exists();
    }


    // ------------------------------------------------------------------------
    // Quota management
    // ------------------------------------------------------------------------



    public int runProgram(String program, String[] args, String user)
    {
        return execAsUID(program, args, getUIDForName(user));
    }
}
