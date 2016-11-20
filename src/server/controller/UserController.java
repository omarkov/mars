package server.controller;

//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Method;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Date;
import java.io.File;

import org.apache.log4j.Logger;

import net.*;

import server.ServerMain;
import server.controller.exceptions.*;
import server.domain.*;
import server.*;
import server.osi.FileWalker;
import server.osi.UserManager;

/**
 * @author Rajkumar
 *
 * Created on 27.07.2005
 *
 * Date 		Author 		Change
 * 04/08/05		RDE			created
 */
public class UserController implements NetworkEventListener {
	
	private Logger logger = Logger.getLogger(UserController.class);

	private static UserController _instance;

	private UserController() {
		
	}
	/**
	 * @return Returns the _instance.
	 */
	public static UserController getInstance() {
		if (UserController._instance == null){
			UserController._instance = new UserController() ;
		}
		return UserController._instance;	
	}	

	public User createUser(String userLoginID,
			String password, String firstname, String lastName, String email,
			String comment, Boolean administrator, Boolean superDuperRepeat, Date expDate)
			throws ElementCreationException {
		
		 return createUser(new HashSet(), userLoginID,
				password, firstname, lastName, email,
				comment, administrator, superDuperRepeat, expDate,null,new ArrayList(), new Integer(0));
	}

    //String,Long,Long
	public User createUserWithIdentification(List tagIDList, List loginSystemIDList, String userLoginID,
			String password, String firstname, String lastName, String email,
			String comment, Boolean administrator, Boolean superDuperRepeat, Date expDate, Long departmentID, List groupIDs,
			Integer quota) throws ElementCreationException {
		try {
			List grps = new ArrayList();
			Set identifications = new HashSet();
			grps.addAll(GroupController.getInstance().getGroups(groupIDs));
			
			if (tagIDList.size() != loginSystemIDList.size()) {
				throw new ControllerException("Ungleiche Anzahl von LogInSystems und Tags");
			}
			
			Iterator tagIds = tagIDList.iterator();
			Iterator loginSystemsIDs = loginSystemIDList.iterator();
		
			
			while(tagIds.hasNext()) {
				Long lsID = (Long)loginSystemsIDs.next();
				String tagID = (String)tagIds.next();
				// logger.warn("LS: " + lsID +"| TagID: "+ tagID);
			    LogInSystem ls = IdentificationController.getInstance().getLogInSystem(lsID);
			    identifications.add(
			    	//new Identification(ls,tagID));
			    	IdentificationController.getInstance().createIdentification(ls,tagID));
			    
			}
			
			
			
			return createUser(identifications, userLoginID, password,
					firstname, lastName, email, comment, administrator, superDuperRepeat, expDate,
					departmentID, grps, quota);
			
		} catch (ControllerException e) {
			throw new ElementCreationException(e.getMessage());
		}
	}
	
	/**
	 * Creates a new User, an equivalent OperatingSystem User and stores it in DB. 
	 * 
	 * @param opsTagID
	 * @param userLoginID
	 * @param password
	 * @param firstname
	 * @param lastName
	 * @param email
	 * @param comment
	 * @param administrator
	 * @param superDuperRepeat
	 * @param quota
	 * @throws UserCreationException
	 */
	protected User createUser(Set identifications, String userLoginID,
			String password, String firstname, String lastName, String email,
			String comment, Boolean administrator, Boolean superDuperRepeat,Date expDate, Long departmentID, List groups,
			Integer quota) throws ElementCreationException {
		
		User user = null;
		
		try {
			Department dept = null;
			if (departmentID != null) {
				dept = DepartmentController.getInstance().getDepartment(departmentID);
			}
			
			Set groupSet = new HashSet();
			groupSet.addAll(groups);
					
			user = new User(identifications, userLoginID, password, firstname,
				lastName, email, comment, administrator.booleanValue(), superDuperRepeat.booleanValue(), expDate, dept, groupSet, quota.intValue());
		
			validateUser(user);
			logger.warn("Saving User: " + user);
			User u = (User)DBAccessController.save(user);

            String[] grpNames = new String[groups.size()];

            int i=0;

            for(GroupMars group: (List<GroupMars>)groups)
            {
                grpNames[i++] = group.getName();
            }
			
			/* User im OS anlegen */
			UserManager.addUser(userLoginID,password);
			UserManager.setGroupsForUser(userLoginID,grpNames);
			return u;
        } catch (MarsException e) {
			throw new ElementCreationException(e.getMessage());
		} catch (IOException e) {
			try {
				DBAccessController.delete(user);
				return null;
			} catch (ElementDeletionException e1) {
				throw new ElementCreationException(e.getMessage() + "\n " + e1.getMessage() + " \n Bitte löschen sie den Benutzer aus der DB.");
			}
		}
	}
	//Long	
	public Boolean updateUser(Long id, List identificationIDs, Long defaultProfileID,
			String userLoginID, String password, String firstName,
			String lastName, String email, String comment,
			Boolean administrator, Boolean superDuperRepeat, Date expDate, Integer quota) throws ElementUpdateException {
		
		try {
			User user = this.getUserByLoginID(userLoginID);// getUser(id);

            SmartRoomProfile profile = null;
            if(defaultProfileID != null)
                profile = SmartRoomProfileController.getInstance().getProfile(defaultProfileID);
                
            user.setDefaultProfile(profile);

			Set identifications = new HashSet();

			for(Long iid: (List<Long>)identificationIDs)
			{
				System.out.println("IID: " + iid);
			}

			
			identifications.addAll(IdentificationController.getInstance().getIdentifications(identificationIDs));
	
			// Set identifications = user.getIdentifications();
			Set groups = user.getGroups();
			Set smartRoomProfiles = user.getSmartRoomProfiles();
			Department department = user.getDepartment();
			SmartRoomProfile defaultProfile = user.getDefaultProfile();
			
			updateUser(id, identifications, userLoginID, password, firstName,
					lastName, email, comment,
					administrator, superDuperRepeat, expDate, department,
					defaultProfile, quota, groups,
					smartRoomProfiles);
					
		} catch (ControllerException e) {
			throw new ElementUpdateException(e.getMessage());
		}
        return Boolean.TRUE;
	}
	//Long,Long,Long
	protected User updateUser(Long id, List identificationIDs,
			String userLoginID, String password, String firstName,
			String lastName, String email, String comment,
			Boolean administrator, Boolean superDuperRepeat, Date expDate, Long departmentID,
			Long defaultProfileID, Integer quota, List groupIDs,
			List smartRoomProfileIDs) throws ElementUpdateException {
		
		try {
			
			Set identifications = new HashSet();
			Set groups = new HashSet();
			Set smartRoomProfiles = new HashSet();
			
			identifications.addAll(IdentificationController.getInstance().getIdentifications(identificationIDs));
			
			Department department = DepartmentController.getInstance()
					.getDepartment(departmentID);
			SmartRoomProfile defaultProfile = SmartRoomProfileController
					.getInstance().getProfile(defaultProfileID);
			
			groups.add(GroupController.getInstance().getGroups(groupIDs));
			smartRoomProfiles.add(SmartRoomProfileController.getInstance().getProfiles(smartRoomProfileIDs));
			
			return updateUser(id, identifications, userLoginID, password, firstName,
					lastName, email, comment,
					administrator, superDuperRepeat, expDate, department,
					defaultProfile, quota, groups,
					smartRoomProfiles);
					
		} catch (ControllerException e) {
			throw new ElementUpdateException(e.getMessage());
		}
		
		
	}
	
	/**
	 * Updates a User.
	 * 
	 * @param id
	 * @param opsTagID
	 * @param userLoginID
	 * @param password
	 * @param firstName
	 * @param lastName
	 * @param email
	 * @param comment
	 * @param administrator
	 * @param superDuperRepeat
	 * @param department
	 * @param defaultProfile
	 * @param quota
	 * @param groups
	 * @param smartRoomProfiles
	 * @throws ElementUpdateException
	 */
	protected User updateUser(Long id, Set identifications,
		String userLoginID, String password, String firstName,
		String lastName, String email, String comment,
		Boolean administrator, Boolean superDuperRepeat, Date expDate, Department department,
		SmartRoomProfile defaultProfile, Integer quota, Set groups,
		Set smartRoomProfiles) throws ElementUpdateException {
			
		try {
			
			/* Receive User from DB */
			// TODO userLoginID durch ID ersetzen
			User user = this.getUserByLoginID(userLoginID);// this.getUser(id);
/*
			for(Identification i: (Set<Identification>)identifications)
			{
			    i.setUser(user);
			    DBAccessController.save(i);
			}
*/			
			/* update values */
			user.setAdministrator(administrator.booleanValue());
			user.setSuperDuperRepeat(superDuperRepeat.booleanValue());
			user.setExpirationDate(expDate);
			user.setComment(comment);
			user.setDefaultProfile(defaultProfile);
			user.setDepartment(department);
			user.setEmail(email);
			user.setFirstName(firstName);
			user.setGroups(groups);
			user.setLastName(lastName);
			user.setIdentifications(identifications);
			user.setPassword(password);
			user.setQuota(quota.intValue());
			user.setSmartRoomProfiles(smartRoomProfiles);
			user.setUserLoginID(userLoginID);

			/* validate and update */
			validateUser(user);
			User u = (User) DBAccessController.save(user);
			//TODO: speichert das Speichern auch die Abhängigen Objekte? 
			//TODO: Hier das update des Users im Betriebsystem einbauen
			return u;
  			} catch (ControllerException e) {
  				throw new ElementUpdateException(e.getMessage());
			}
	
		}

	public void deleteUser(Long id) throws ElementDeletionException {
		try {
			User user = this.getUser(id);
			
			GroupController.getInstance().removeUserFromAllGroups(user);
			user.setDepartment(null);
			
			String userLogInID = user.getUserLoginID();
			DBAccessController.delete(user);

			/* Benutzer aus OS löschen */
			UserManager.removeUser(userLogInID);
			
		} catch (MarsException e) {
			throw new ElementDeletionException(e.getMessage());
		} catch (IOException e) {
			throw new ElementDeletionException(e.getMessage() + "\n Bitte löschen sie den Benutzer aus dem OS");
		}
	}


	/**
	 * Adds a Profile to the Users Profiles.
	 * 
	 * @param user
	 * @param profile
	 * @throws ElementUpdateException
	 */
	protected User addSmartRoomProfile(User user, SmartRoomProfile profile)
			throws ElementUpdateException {
		user.addSmartRoomProfile(profile);
		return (User) DBAccessController.save(user);		
	}

	
	public Department getDepartmentForUser(Long userID) throws ControllerException {		
		User user = this.getUser(userID);
		if (user != null) {
			return user.getDepartment();
		}
		return null;
	}
	//GroupMars
	public List getGroupsForUser(Long userID) throws ControllerException {
		ArrayList groups = new ArrayList();
		
		User user = this.getUser(userID);
		if (user != null) {
				groups.addAll(user.getGroups());
		}
		return groups;
	
	}
	
	public SmartRoomProfile getDefaultProfileForUser(Long userID) throws ControllerException {
		User user = this.getUser(userID);
		if (user != null) {
			return user.getDefaultProfile();
		} else {
			return null;
		}
	}
    
    //String
    public List getProfileNamesForActiveUser() throws ControllerException {
        ArrayList array = new ArrayList();

        Long id = SmartRoomController.getInstance().getActiveUser();
        User user = getUser(id);
        if(user != null) {
            Set<SmartRoomProfile> profiles = user.getSmartRoomProfiles();
            for(SmartRoomProfile p: profiles)
            {
                array.add(p.getName());
            }
        }
        return array;
    }
    
	//List
	public List getProfilesForUser(Long userID) throws ControllerException {
		ArrayList profiles = new ArrayList();
		
		User user = this.getUser(userID);
		if (user != null) {
				profiles.addAll(user.getSmartRoomProfiles());
		}
		return profiles;	
	}
	//List
	public List getIdentificationsForUser(Long userID) throws ControllerException {
		ArrayList idents = new ArrayList();
		
		User user = this.getUser(userID);
		if (user != null) {
			idents.addAll(user.getIdentifications());
		}
		return idents;	
	}
	
	public User getUser(Long userID) throws ControllerException {
		return (User)DBAccessController.getObjectByID(User.class,userID);	
	}
	//List	
	public List getAllUsers() {
		return DBAccessController.getAllObjectsFromQuery(DBAccessController.GET_ALL_USERS);
	}
	//List
	public List getUsers(List ids) throws ControllerException {
		return DBAccessController.getObjectsByIDs(DBAccessController.GET_USER_BY_ID,ids);
	}
	//List,Long
	public List getUsersByUserIDs(List userIDs) throws ControllerException {
		return DBAccessController.getObjectsByIDs(DBAccessController.GET_USER_BY_USRID,userIDs);
	}
	//List	
	public List getAllUsersNotInGroup(Long groupID) throws ControllerException {
		List users = getAllUsers();
		List result = new ArrayList();
		GroupMars group = GroupController.getInstance().getGroup(groupID);
		for (Iterator i = users.iterator(); i.hasNext();) {
			User user = (User) i.next();
			if (!user.isMemberOfGroup(group)) {
				result.add(user);
			}
		}
		return result;
	}
	/**
	 * Validates a User
	 * 
	 * @param user
	 * @return
	 */
	public static void validateUser(User user) throws ElementValidationException {
		
		if (user == null) {
			throw new ElementValidationException("Benutzer darf nicht null sein");
		}
		
	}
	
	protected void removeGroupFromAllUsers(Long grpID) throws ControllerException {
		GroupMars group = GroupController.getInstance().getGroup(grpID);
		List l = this.getAllUsers();
		for (Iterator i = l.iterator(); i.hasNext();) {
			User u = (User)i.next();
			u.removeGroup(group);
			DBAccessController.save(u);
		}
	}
	//String	
	public List getAudioFiles(Long userID) throws ControllerException {
		User user = getUser(userID);
		FileWalker f = UserManager.getFileWalkerForUser(user.getUserLoginID());
        List<File> files = f.getAudioFiles();
        List path = new ArrayList();
        for(File file: files)
        {
            path.add(file.getAbsolutePath()); 
        }
		return path;
	}

	//String	
	public List getVideoFiles(Long userID) throws ControllerException {
		User user = getUser(userID);
		FileWalker f = UserManager.getFileWalkerForUser(user.getUserLoginID());
        List<File> files = f.getVideoFiles();
        List path = new ArrayList();
        for(File file: files)
        {
            path.add(file.getAbsolutePath()); 
        }
		return path;

	}

	//String	
	public List getImageFiles(Long userID) throws ControllerException {
		User user = getUser(userID);
		FileWalker f = UserManager.getFileWalkerForUser(user.getUserLoginID());
        List<File> files = f.getImageFiles();
        List path = new ArrayList();
        for(File file: files)
        {
            path.add(file.getAbsolutePath()); 
        }
		return path;
	}
	
	public User getUserByLoginID(String userLogInID) throws MulitpleElementsWithPKException {
		Object[] o = new Object[] {userLogInID};
		return (User) DBAccessController.getObjectByAttributes(DBAccessController.GET_USER_BY_USRID,o);
	}

	public User getUserByLoginIDAndPasswd(String userLogInID, String passwd) throws MulitpleElementsWithPKException {
		Object[] o = new Object[] {userLogInID,passwd };
		return (User) DBAccessController.getObjectByAttributes(DBAccessController.GET_USER_BY_USRID_AND_PASSWD,o);
	}

	public void deleteAllSmartRoomProfilesFromUser(User u) throws ControllerException {
		Set profiles = u.getSmartRoomProfiles();
		logger.warn("liste grösse:" + profiles.size());
		Iterator it = profiles.iterator();

		while(it.hasNext()){
			SmartRoomProfile srp = (SmartRoomProfile)it.next();
			SmartRoomProfileController.getInstance().deleteSmartRoomProfile(srp.getId());
		}
	}
//START_AUTO_GEN
	public ModuleDescription addCommands(ModuleDescription module) {
		Command createUser = new Command("createUser", new ObjectParameter(User.class), this);

		createUser.addParameter(new StringParameter("userLoginID", null));
		createUser.addParameter(new StringParameter("password", null));
		createUser.addParameter(new StringParameter("firstname", null));
		createUser.addParameter(new StringParameter("lastName", null));
		createUser.addParameter(new StringParameter("email", null));
		createUser.addParameter(new StringParameter("comment", null));
		createUser.addParameter(new BooleanParameter("administrator", null));
		createUser.addParameter(new BooleanParameter("superDuperRepeat", null));
		createUser.addParameter(new ObjectParameter("expDate", Date.class));

		module.addInterface(createUser);
		Command createUserWithIdentification = new Command("createUserWithIdentification", new ObjectParameter(User.class), this);

		createUserWithIdentification.addParameter(new ListParameter("tagIDList", String.class));
		createUserWithIdentification.addParameter(new ListParameter("loginSystemIDList", Long.class));
		createUserWithIdentification.addParameter(new StringParameter("userLoginID", null));
		createUserWithIdentification.addParameter(new StringParameter("password", null));
		createUserWithIdentification.addParameter(new StringParameter("firstname", null));
		createUserWithIdentification.addParameter(new StringParameter("lastName", null));
		createUserWithIdentification.addParameter(new StringParameter("email", null));
		createUserWithIdentification.addParameter(new StringParameter("comment", null));
		createUserWithIdentification.addParameter(new BooleanParameter("administrator", null));
		createUserWithIdentification.addParameter(new BooleanParameter("superDuperRepeat", null));
		createUserWithIdentification.addParameter(new ObjectParameter("expDate", Date.class));
		createUserWithIdentification.addParameter(new ObjectParameter("departmentID", Long.class));
		createUserWithIdentification.addParameter(new ListParameter("groupIDs", Long.class));
		createUserWithIdentification.addParameter(new NumericParameter("quota", null));

		module.addInterface(createUserWithIdentification);
		Command updateUser = new Command("updateUser", new BooleanParameter(), this);

		updateUser.addParameter(new ObjectParameter("id", Long.class));
		updateUser.addParameter(new ListParameter("identificationIDs", Long.class));
		updateUser.addParameter(new ObjectParameter("defaultProfileID", Long.class));
		updateUser.addParameter(new StringParameter("userLoginID", null));
		updateUser.addParameter(new StringParameter("password", null));
		updateUser.addParameter(new StringParameter("firstName", null));
		updateUser.addParameter(new StringParameter("lastName", null));
		updateUser.addParameter(new StringParameter("email", null));
		updateUser.addParameter(new StringParameter("comment", null));
		updateUser.addParameter(new BooleanParameter("administrator", null));
		updateUser.addParameter(new BooleanParameter("superDuperRepeat", null));
		updateUser.addParameter(new ObjectParameter("expDate", Date.class));
		updateUser.addParameter(new NumericParameter("quota", null));

		module.addInterface(updateUser);
		Command deleteUser = new Command("deleteUser", null, this);

		deleteUser.addParameter(new ObjectParameter("id", Long.class));

		module.addInterface(deleteUser);
		Command getDepartmentForUser = new Command("getDepartmentForUser", new ObjectParameter(Department.class), this);

		getDepartmentForUser.addParameter(new ObjectParameter("userID", Long.class));

		module.addInterface(getDepartmentForUser);
		Command getGroupsForUser = new Command("getGroupsForUser", new ListParameter(GroupMars.class), this);

		getGroupsForUser.addParameter(new ObjectParameter("userID", Long.class));

		module.addInterface(getGroupsForUser);
		Command getDefaultProfileForUser = new Command("getDefaultProfileForUser", new ObjectParameter(SmartRoomProfile.class), this);

		getDefaultProfileForUser.addParameter(new ObjectParameter("userID", Long.class));

		module.addInterface(getDefaultProfileForUser);
		Command getProfileNamesForActiveUser = new Command("getProfileNamesForActiveUser", new ListParameter(String.class), this);


		module.addInterface(getProfileNamesForActiveUser);
		Command getProfilesForUser = new Command("getProfilesForUser", new ObjectParameter(List.class), this);

		getProfilesForUser.addParameter(new ObjectParameter("userID", Long.class));

		module.addInterface(getProfilesForUser);
		Command getIdentificationsForUser = new Command("getIdentificationsForUser", new ObjectParameter(List.class), this);

		getIdentificationsForUser.addParameter(new ObjectParameter("userID", Long.class));

		module.addInterface(getIdentificationsForUser);
		Command getUser = new Command("getUser", new ObjectParameter(User.class), this);

		getUser.addParameter(new ObjectParameter("userID", Long.class));

		module.addInterface(getUser);
		Command getAllUsers = new Command("getAllUsers", new ObjectParameter(List.class), this);


		module.addInterface(getAllUsers);
		Command getUsers = new Command("getUsers", new ObjectParameter(List.class), this);

		getUsers.addParameter(new ListParameter("ids", String.class));

		module.addInterface(getUsers);
		Command getUsersByUserIDs = new Command("getUsersByUserIDs", new ObjectParameter(List.class), this);

		getUsersByUserIDs.addParameter(new ListParameter("userIDs", Long.class));

		module.addInterface(getUsersByUserIDs);
		Command getAllUsersNotInGroup = new Command("getAllUsersNotInGroup", new ObjectParameter(List.class), this);

		getAllUsersNotInGroup.addParameter(new ObjectParameter("groupID", Long.class));

		module.addInterface(getAllUsersNotInGroup);
		Command getAudioFiles = new Command("getAudioFiles", new ListParameter(String.class), this);

		getAudioFiles.addParameter(new ObjectParameter("userID", Long.class));

		module.addInterface(getAudioFiles);
		Command getVideoFiles = new Command("getVideoFiles", new ListParameter(String.class), this);

		getVideoFiles.addParameter(new ObjectParameter("userID", Long.class));

		module.addInterface(getVideoFiles);
		Command getImageFiles = new Command("getImageFiles", new ListParameter(String.class), this);

		getImageFiles.addParameter(new ObjectParameter("userID", Long.class));

		module.addInterface(getImageFiles);
		Command getUserByLoginID = new Command("getUserByLoginID", new ObjectParameter(User.class), this);

		getUserByLoginID.addParameter(new StringParameter("userLogInID", null));

		module.addInterface(getUserByLoginID);
		Command getUserByLoginIDAndPasswd = new Command("getUserByLoginIDAndPasswd", new ObjectParameter(User.class), this);

		getUserByLoginIDAndPasswd.addParameter(new StringParameter("userLogInID", null));
		getUserByLoginIDAndPasswd.addParameter(new StringParameter("passwd", null));

		module.addInterface(getUserByLoginIDAndPasswd);
		Command deleteAllSmartRoomProfilesFromUser = new Command("deleteAllSmartRoomProfilesFromUser", null, this);

		deleteAllSmartRoomProfilesFromUser.addParameter(new ObjectParameter("u", User.class));

		module.addInterface(deleteAllSmartRoomProfilesFromUser);
		return module;
	}
//STOP_AUTO_GEN
}
