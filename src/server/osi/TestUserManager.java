package server.osi;

public class TestUserManager
{
    private static void testAddUser() throws Exception
    {
	UserManager.addUser("schleidl", "cardassia");
    }

    private static void testRemoveUser() throws Exception
    {
	UserManager.removeUser("schleidl");
    }

    private static void testUserList() throws Exception
    {
	String[] list = UserManager.getUserList();
	
	for (int i = 0; i < list.length; i++) 
	    System.out.println("[" + i + "]: " + list[i] + " - " + UserManager.getHomeOfUser(list[i]));
    }

    private static void testAddGroup() throws Exception
    {
	UserManager.addGroup("romulaner");
	UserManager.addGroup("Klingonen");
	UserManager.addGroup("Species8472");
    }

    private static void testRemoveGroup() throws Exception
    {
	UserManager.removeGroup("romulaner");
	UserManager.removeGroup("Klingonen");
	UserManager.removeGroup("Species8472");
    }
    
    private static void testSetGroups() throws Exception {
	String[] grps = new String[] {"romulaner", "Klingonen", "Species8472"};
	UserManager.setGroupsForUser("schleidl", grps);
    }

    private static void testRunProgram() throws Exception {
	UserManager.runProgram("/root/mars_oli/testy_scripty.sh", new String[] { "foo", "bar", "baz" }, "schleidl");
    }

    public static void main(String[] args)
    {
	try {
	    //testAddUser();
	    //testRemoveUser();
	    //testUserList();
	    //testAddGroup();
	    //testRemoveGroup();
	    //testSetGroups();
	    testRunProgram();
	} catch (Exception ex) {
	    System.out.println("An exception during testing occured:");
	    System.out.println(" * " + ex.getMessage());
	    ex.printStackTrace();
	}
    }
}
