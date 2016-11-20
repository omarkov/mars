package web.java.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import net.ModuleDescription;
import net.NetworkException;
import net.NetworkEventListener;
import net.NetworkFactory;
import net.Command;
import net.BooleanParameter;

import org.apache.log4j.Logger;

import server.domain.ComponentAttribute;
import server.domain.AttributeType;
import server.domain.ComponentAttributeValue;
import server.domain.SmartRoomComponent;
import server.domain.Department;
import server.domain.GroupMars;
import server.domain.Identification;
import server.domain.LogInSystem;
import server.domain.SmartRoomProfile;
import server.domain.User;

public class ControllerConnection implements NetworkEventListener
{       
    private static Logger logger = Logger.getLogger(ControllerConnection.class);
    private static NetworkFactory net = null;

    private User user = null;

    public ControllerConnection(String user, String password)
	throws NetworkException
    {
        open(user, password);
    }
    private void open(String user, String password)
	throws NetworkException
    {
        if(net == null || ! net.isConnected())
        {
            net = NetworkFactory.getInstance();
            ModuleDescription myModule = new ModuleDescription("web", "V1.0");
            Command c = new Command("ping", new BooleanParameter(), this);
            myModule.addInterface(c);
            net.setModuleDescription(myModule);
            net.startModule("localhost", 1234);
        }

    	this.user = (User)net.call("system", "getUserByLoginIDAndPasswd", new Object[]{user, password});
    	if(this.user == null)
    	{
    		throw new NetworkException("User or password invalid");
    	}
    }

    public Boolean ping()
    {
        return Boolean.TRUE;
    }
 
    public NetworkFactory getNetworkFactory()
    {
        return net;
    }

    public boolean checkConnection()
    {
        return net != null && net.checkConnection();
    }

    public User getCurrentUser()
    {
	    return user;
    }

    public void refreshUser()
    {
	if(user != null)
	{
	    try
	    {
	        user = getUser(user.getId());
	    }catch(NetworkException e)
	    {
		e.printStackTrace();
            }
	}
    }

    public void close ()
    {
        logger.debug("Closing ControllerConnection");
        // net.stopModule();
    }
    
    public GroupMars createGroup(GroupMars group, List userIds)
    throws NetworkException
    {
	System.out.println("Date: " + group.getExpirationDate());

    	return this.createGroup(
    			group.getName(),
    			group.getComment(),
    			group.getExpirationDate(),userIds);
    }

    public User createUser(User user)
    throws NetworkException
    {
    	return this.createUser(
    			user.getUserLoginID(),
    			user.getPassword(),
    			user.getFirstName(),
    			user.getLastName(),
    			user.getEmail(),
    			user.getComment(),
    			new Boolean(user.isAdministrator()),
                new Boolean(user.isSuperDuperRepeat()),
			    user.getExpirationDate());
    }
    
    public Boolean updateUser(User user)
    throws NetworkException
    {
         List ids = new ArrayList();
         for(Identification i: (Set<Identification>)user.getIdentifications())
         {
            ids.add(i.getId());
         }

         return updateUser(user, ids);
    }

    public Boolean updateUser(User user, List identIds)
    throws NetworkException
    {
        Long pid = null;
        if(user.getDefaultProfile() != null)
        {
            pid = user.getDefaultProfile().getId();
        }
    
    	return this.updateUser(
    			user.getId(),
    			identIds,
                pid,
    			user.getUserLoginID(),
    			user.getPassword(),
    			user.getFirstName(),
    			user.getLastName(),
    			user.getEmail(),
    			user.getComment(),
    			new Boolean(user.isAdministrator()),
    			new Boolean(user.isSuperDuperRepeat()),
			user.getExpirationDate(),
    			new Integer(user.getQuota()));
    }

    public Boolean updateGroup(GroupMars group)
    throws NetworkException
    {
    	return this.updateGroup(
    			group.getId(),
    			group.getName(),
    			group.getComment(),
    			group.getExpirationDate());
    }

//START_AUTO_GEN
	public Department createDepartment(String name,String abbreviation,String comment) throws NetworkException {
		return (Department)net.call("system", "createDepartment", new Object[]{name,abbreviation,comment});
	}

	public Department createDepartmentWithUsers(String name,String abbreviation,String comment,List userIDs) throws NetworkException {
		return (Department)net.call("system", "createDepartmentWithUsers", new Object[]{name,abbreviation,comment,userIDs});
	}

	public Boolean updateDepartment(Long id,String name,String abbreviation,String comment) throws NetworkException {
		return (Boolean)net.call("system", "updateDepartment", new Object[]{id,name,abbreviation,comment});
	}

	public Boolean updateDepartmentWithUsers(Long id,String name,String abbreviation,String comment,List userIDs) throws NetworkException {
		return (Boolean)net.call("system", "updateDepartmentWithUsers", new Object[]{id,name,abbreviation,comment,userIDs});
	}

public void deleteDepartment(Long deptID) throws NetworkException {
		net.call("system", "deleteDepartment", new Object[]{deptID});
	}

	public List getUsersNotInDepartment(Long deptId) throws NetworkException {
		return (List)net.call("system", "getUsersNotInDepartment", new Object[]{deptId});
	}

public void validateDepartment(Department department) throws NetworkException {
		net.call("system", "validateDepartment", new Object[]{department});
	}

	public Department addUserToDepartment(Long deptID,Long userID) throws NetworkException {
		return (Department)net.call("system", "addUserToDepartment", new Object[]{deptID,userID});
	}

	public Department addUsersToDepartment(Long deptID,List userIDs) throws NetworkException {
		return (Department)net.call("system", "addUsersToDepartment", new Object[]{deptID,userIDs});
	}

	public Department removeUserFromDepartment(Long deptID,Long userID) throws NetworkException {
		return (Department)net.call("system", "removeUserFromDepartment", new Object[]{deptID,userID});
	}

public void removeUsersFromDepartment(Long deptID,List userIDs) throws NetworkException {
		net.call("system", "removeUsersFromDepartment", new Object[]{deptID,userIDs});
	}

	public List getUsersForDepartment(Long deptID) throws NetworkException {
		return (List)net.call("system", "getUsersForDepartment", new Object[]{deptID});
	}

	public Department getDepartment(Long deptID) throws NetworkException {
		return (Department)net.call("system", "getDepartment", new Object[]{deptID});
	}

	public List getDepartments(List deptIDs) throws NetworkException {
		return (List)net.call("system", "getDepartments", new Object[]{deptIDs});
	}

	public List getAllDepartments() throws NetworkException {
		return (List)net.call("system", "getAllDepartments", new Object[]{});
	}

	public GroupMars createGroup(String name,String comment,Date expirationDate,List userIDs) throws NetworkException {
		return (GroupMars)net.call("system", "createGroup", new Object[]{name,comment,expirationDate,userIDs});
	}

	public Boolean updateGroup(Long id,String name,String comment,Date expirationDate) throws NetworkException {
		return (Boolean)net.call("system", "updateGroup", new Object[]{id,name,comment,expirationDate});
	}

	public Boolean updateGroupWithUsers(Long id,String name,String comment,Date expirationDate,List userIDs) throws NetworkException {
		return (Boolean)net.call("system", "updateGroupWithUsers", new Object[]{id,name,comment,expirationDate,userIDs});
	}

public void deleteGroup(Long id) throws NetworkException {
		net.call("system", "deleteGroup", new Object[]{id});
	}

	public GroupMars addUserToGroup(Long groupID,Long userID) throws NetworkException {
		return (GroupMars)net.call("system", "addUserToGroup", new Object[]{groupID,userID});
	}

	public GroupMars addUsersToGroup(Long groupID,List userIDs) throws NetworkException {
		return (GroupMars)net.call("system", "addUsersToGroup", new Object[]{groupID,userIDs});
	}

	public GroupMars removeUserFromGroup(Long groupID,Long userID) throws NetworkException {
		return (GroupMars)net.call("system", "removeUserFromGroup", new Object[]{groupID,userID});
	}

	public GroupMars removeUsersFromGroup(Long groupID,List userIDs) throws NetworkException {
		return (GroupMars)net.call("system", "removeUsersFromGroup", new Object[]{groupID,userIDs});
	}

	public GroupMars getGroup(Long groupID) throws NetworkException {
		return (GroupMars)net.call("system", "getGroup", new Object[]{groupID});
	}

	public List getGroups(List groupIDs) throws NetworkException {
		return (List)net.call("system", "getGroups", new Object[]{groupIDs});
	}

	public List getAllGroups() throws NetworkException {
		return (List)net.call("system", "getAllGroups", new Object[]{});
	}

public void removeUserFromAllGroups(User user) throws NetworkException {
		net.call("system", "removeUserFromAllGroups", new Object[]{user});
	}

	public List getUsersForGroup(Long groupID) throws NetworkException {
		return (List)net.call("system", "getUsersForGroup", new Object[]{groupID});
	}

public void validateGroup(GroupMars group) throws NetworkException {
		net.call("system", "validateGroup", new Object[]{group});
	}

	public Boolean updateIdentification(Long idID,String tagID) throws NetworkException {
		return (Boolean)net.call("system", "updateIdentification", new Object[]{idID,tagID});
	}

	public Identification createIdentification(Long lsID,String tagID) throws NetworkException {
		return (Identification)net.call("system", "createIdentification", new Object[]{lsID,tagID});
	}

	public Identification createIdentificationForLogInSystemNamed(String lsName,String tagID) throws NetworkException {
		return (Identification)net.call("system", "createIdentificationForLogInSystemNamed", new Object[]{lsName,tagID});
	}

public void deleteIdentification(Long id) throws NetworkException {
		net.call("system", "deleteIdentification", new Object[]{id});
	}

public void validateIdentification(Identification ident) throws NetworkException {
		net.call("system", "validateIdentification", new Object[]{ident});
	}

	public LogInSystem createLogInSystem(String logInSystemName) throws NetworkException {
		return (LogInSystem)net.call("system", "createLogInSystem", new Object[]{logInSystemName});
	}

public void deleteLogInSystem(Long logInSystemID) throws NetworkException {
		net.call("system", "deleteLogInSystem", new Object[]{logInSystemID});
	}

public void validatLogInSystem(LogInSystem system) throws NetworkException {
		net.call("system", "validatLogInSystem", new Object[]{system});
	}

	public List getAllLogInSystems() throws NetworkException {
		return (List)net.call("system", "getAllLogInSystems", new Object[]{});
	}

	public LogInSystem getLogInSystem(Long lsID) throws NetworkException {
		return (LogInSystem)net.call("system", "getLogInSystem", new Object[]{lsID});
	}

	public LogInSystem getLogInSystemByName(String lsName) throws NetworkException {
		return (LogInSystem)net.call("system", "getLogInSystemByName", new Object[]{lsName});
	}

	public Identification getIdentification(Long id) throws NetworkException {
		return (Identification)net.call("system", "getIdentification", new Object[]{id});
	}

	public List getIdentifications(List ids) throws NetworkException {
		return (List)net.call("system", "getIdentifications", new Object[]{ids});
	}

	public Identification getIdentificationByLSNameAndTag(String loginSystemName,String tagID) throws NetworkException {
		return (Identification)net.call("system", "getIdentificationByLSNameAndTag", new Object[]{loginSystemName,tagID});
	}

	public SmartRoomProfile createProfile(SmartRoomProfile profile,Long userID) throws NetworkException {
		return (SmartRoomProfile)net.call("system", "createProfile", new Object[]{profile,userID});
	}

	public Boolean updateProfile(SmartRoomProfile profile,Long userID) throws NetworkException {
		return (Boolean)net.call("system", "updateProfile", new Object[]{profile,userID});
	}

	public SmartRoomProfile getProfile(Long profileID) throws NetworkException {
		return (SmartRoomProfile)net.call("system", "getProfile", new Object[]{profileID});
	}

	public List getProfiles(List profileIDs) throws NetworkException {
		return (List)net.call("system", "getProfiles", new Object[]{profileIDs});
	}

	public SmartRoomProfile createDefaultProfileForUser(Long userID) throws NetworkException {
		return (SmartRoomProfile)net.call("system", "createDefaultProfileForUser", new Object[]{userID});
	}

	public List getAllComponents() throws NetworkException {
		return (List)net.call("system", "getAllComponents", new Object[]{});
	}

public void deleteSmartRoomProfile(Long profileId) throws NetworkException {
		net.call("system", "deleteSmartRoomProfile", new Object[]{profileId});
	}

	public User createUser(String userLoginID,String password,String firstname,String lastName,String email,String comment,Boolean administrator,Boolean superDuperRepeat,Date expDate) throws NetworkException {
		return (User)net.call("system", "createUser", new Object[]{userLoginID,password,firstname,lastName,email,comment,administrator,superDuperRepeat,expDate});
	}

	public User createUserWithIdentification(List tagIDList,List loginSystemIDList,String userLoginID,String password,String firstname,String lastName,String email,String comment,Boolean administrator,Boolean superDuperRepeat,Date expDate,Long departmentID,List groupIDs,Integer quota) throws NetworkException {
		return (User)net.call("system", "createUserWithIdentification", new Object[]{tagIDList,loginSystemIDList,userLoginID,password,firstname,lastName,email,comment,administrator,superDuperRepeat,expDate,departmentID,groupIDs,quota});
	}

	public Boolean updateUser(Long id,List identificationIDs,Long defaultProfileID,String userLoginID,String password,String firstName,String lastName,String email,String comment,Boolean administrator,Boolean superDuperRepeat,Date expDate,Integer quota) throws NetworkException {
		return (Boolean)net.call("system", "updateUser", new Object[]{id,identificationIDs,defaultProfileID,userLoginID,password,firstName,lastName,email,comment,administrator,superDuperRepeat,expDate,quota});
	}

public void deleteUser(Long id) throws NetworkException {
		net.call("system", "deleteUser", new Object[]{id});
	}

	public Department getDepartmentForUser(Long userID) throws NetworkException {
		return (Department)net.call("system", "getDepartmentForUser", new Object[]{userID});
	}

	public List getGroupsForUser(Long userID) throws NetworkException {
		return (List)net.call("system", "getGroupsForUser", new Object[]{userID});
	}

	public SmartRoomProfile getDefaultProfileForUser(Long userID) throws NetworkException {
		return (SmartRoomProfile)net.call("system", "getDefaultProfileForUser", new Object[]{userID});
	}

	public List getProfileNamesForActiveUser() throws NetworkException {
		return (List)net.call("system", "getProfileNamesForActiveUser", new Object[]{});
	}

	public List getProfilesForUser(Long userID) throws NetworkException {
		return (List)net.call("system", "getProfilesForUser", new Object[]{userID});
	}

	public List getIdentificationsForUser(Long userID) throws NetworkException {
		return (List)net.call("system", "getIdentificationsForUser", new Object[]{userID});
	}

	public User getUser(Long userID) throws NetworkException {
		return (User)net.call("system", "getUser", new Object[]{userID});
	}

	public List getAllUsers() throws NetworkException {
		return (List)net.call("system", "getAllUsers", new Object[]{});
	}

	public List getUsers(List ids) throws NetworkException {
		return (List)net.call("system", "getUsers", new Object[]{ids});
	}

	public List getUsersByUserIDs(List userIDs) throws NetworkException {
		return (List)net.call("system", "getUsersByUserIDs", new Object[]{userIDs});
	}

	public List getAllUsersNotInGroup(Long groupID) throws NetworkException {
		return (List)net.call("system", "getAllUsersNotInGroup", new Object[]{groupID});
	}

	public List getAudioFiles(Long userID) throws NetworkException {
		return (List)net.call("system", "getAudioFiles", new Object[]{userID});
	}

	public List getVideoFiles(Long userID) throws NetworkException {
		return (List)net.call("system", "getVideoFiles", new Object[]{userID});
	}

	public List getImageFiles(Long userID) throws NetworkException {
		return (List)net.call("system", "getImageFiles", new Object[]{userID});
	}

	public User getUserByLoginID(String userLogInID) throws NetworkException {
		return (User)net.call("system", "getUserByLoginID", new Object[]{userLogInID});
	}

	public User getUserByLoginIDAndPasswd(String userLogInID,String passwd) throws NetworkException {
		return (User)net.call("system", "getUserByLoginIDAndPasswd", new Object[]{userLogInID,passwd});
	}

public void deleteAllSmartRoomProfilesFromUser(User u) throws NetworkException {
		net.call("system", "deleteAllSmartRoomProfilesFromUser", new Object[]{u});
	}

//STOP_AUTO_GEN
    public void enterRoom(String loginSystem, String tagID) throws NetworkException {
        net.call("system", "enterRoom", new Object[]{loginSystem, tagID});
    }

    public void exitRoom(String loginSystem, String tagID) throws NetworkException {
        net.call("system", "exitRoom", new Object[]{loginSystem, tagID});
    }

    public void composeMessage(String sender, List recipients, String subject, String body) throws NetworkException {
        net.call("system", "composeMessage", new Object[]{sender, recipients, subject, body});
    }

    public void setProfile(SmartRoomProfile profile) throws NetworkException {
        net.call("system", "setProfile", new Object[]{profile});
    }
}
