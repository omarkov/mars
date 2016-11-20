package server.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.List;
import server.osi.InvalidNameException;

import org.apache.log4j.Logger;

import net.*;

import server.controller.exceptions.*;
import server.domain.GroupMars;
import server.domain.User;
import server.osi.UserManager;

import server.*;

/**
 * @author Rajkumar
 *
 * Created on 27.07.2005
 *
 * Date 		Author 		Change
 * 09/08/05		RDE			created
 */
public class GroupController implements NetworkEventListener {

	private static GroupController _instance;
	private Logger logger = Logger.getLogger(GroupController.class);

	private GroupController() {
		
	}
	
	/**
	 * @return Returns the _instance.
	 */
	public static GroupController getInstance() {
		if (GroupController._instance == null){
			GroupController._instance = new GroupController() ;
		}
		return GroupController._instance;	
	}
	
	/**
	 * Creates a Group and stores it in DB
	 * 
	 * @param name
	 * @param comment
	 * @param expirationDate
	 * @param userIDs
	 * @throws ElementCreationException
	 */
	//Long
	public GroupMars createGroup(String name, String comment,
			Date expirationDate, List userIDs) throws ElementCreationException {

		try {
						
			Set users = new HashSet();
            List userlist = UserController.getInstance().getUsers(userIDs);
			users.addAll(userlist);
			
			GroupMars group = new GroupMars(name,comment,expirationDate, users);
			group = createGroup(group);
            addUsersToOSGroups(userlist);
            return group;
			
		} catch (ElementCreationException e) {
			throw e;
		} catch (ControllerException e) {
			throw new ElementCreationException(e.getMessage());
		}
		
	}
	
	/**
	 * Stores the given GroupObject in DB.
	 * 
	 * @param group
	 * @throws ElementCreationException
	 */
	protected GroupMars createGroup(GroupMars group) throws ElementCreationException {
		try {
			validateGroup(group);
			GroupMars g = (GroupMars)DBAccessController.save(group);
			
			/* Gruppe im Betriebsystem anlegen */
			UserManager.addGroup(g.getName());
			return g;
		} catch (MarsException e) {
			throw new ElementCreationException(e.getMessage());
		} catch (IOException e) {
			try {
				DBAccessController.delete(group);
			} catch(ElementDeletionException e1) {
				throw new ElementCreationException(e1.getMessage());
			}
			throw new ElementCreationException(e.getMessage());
		}
	}

	/**
	 * Updates a Group.
	 * @param id
	 * @param name
	 * @param comment
	 * @param expirationDate
	 * @param userIDs
	 * @throws ElementUpdateException
	 */
	public Boolean updateGroup(Long id,String name, String comment,
			Date expirationDate) throws ElementUpdateException {
		try {
			
			System.out.println("GRPID: " + id);
			GroupMars group = getGroup(id);
			group.setComment(comment);
			group.setExpirationDate(expirationDate);
			group.setName(name);
			
			validateGroup(group);
			DBAccessController.save(group);
		
		} catch (ControllerException e) {
			throw new ElementUpdateException(e.getMessage());
		}
        return Boolean.TRUE;
	}

    public static void addUserToOSGroups(User user) 
    {
        ArrayList users = new ArrayList();
        users.add(user);
        addUsersToOSGroups(users);
    }

    public static void addUsersToOSGroups(List users) 
    {
        for(User user: (List<User>)users)
        {
            Set groups = user.getGroups();
            String grpNames[] = new String[groups.size()];
            int i=0;

            for(GroupMars grp: (Set<GroupMars>)groups)
            {
                grpNames[i++] = grp.getName();
            }

            System.out.println("addUserToGroups " + user.getUserLoginID());
    
            try
            {
    	        UserManager.setGroupsForUser(user.getUserLoginID(),grpNames);
            }catch(InvalidNameException e)
            {
                e.printStackTrace();
            }catch(IOException e)
            {
                e.printStackTrace();
            }
        }
    }

	/**
	 * Updates a Group.
	 * @param id
	 * @param name
	 * @param comment
	 * @param expirationDate
	 * @param userIDs
	 * @throws ElementUpdateException
	 */
	//Long
	public Boolean updateGroupWithUsers(Long id,String name, String comment,
			Date expirationDate, List userIDs) throws ElementUpdateException {
		try {
			
			List users = UserController.getInstance().getUsers(userIDs);
			
			GroupMars group = getGroup(id);
			group.setComment(comment);
			group.setExpirationDate(expirationDate);
			group.setName(name);
			Set userSet = new HashSet();
			userSet.addAll(users);
			group.setUsers(userSet);

			validateGroup(group);
            addUsersToOSGroups(users);
			DBAccessController.save(group);
		
		} catch (ControllerException e) {
			throw new ElementUpdateException(e.getMessage());
		}
        return Boolean.TRUE;
		
	}
	
	/**
	 * Deletes a Group in DB.
	 * 
	 * @param id
	 * @throws ElementDeletionException
	 */
	public void deleteGroup(Long id) throws ElementDeletionException {
		try {
			GroupMars group = getGroup(id);
			UserController.getInstance().removeGroupFromAllUsers(group.getId());
			String gName = group.getName(); 
			DBAccessController.delete(group);
			
			/* Gruppe aus OS entfernen */
			UserManager.removeGroup(group.getName());
			
		} catch (MarsException e) {
			throw new ElementDeletionException(e.getMessage());
		} catch (IOException e) {
			throw new ElementDeletionException(e.getMessage() + "\n Bitte löschen sie die Betriebsystemgruppe von Hand");
		}
	}
	
	/**
	 * Adds a User to a Group.
	 * 
	 * @param groupID
	 * @param userID
	 * @throws ElementUpdateException
	 */
	public GroupMars addUserToGroup(Long groupID, Long userID) throws ElementUpdateException {
		try {
	
			GroupMars group = getGroup(groupID);
			User user = UserController.getInstance().getUser(userID);
	
		group.addUser(user);
		validateGroup(group);
        addUserToOSGroups(user);
		return (GroupMars)DBAccessController.save(group);
	
		} catch (ControllerException e) {
			throw new ElementUpdateException(e.getMessage());
		}

	}
	
	/**
	 * Adds the given users to the given group
	 * 
	 * @param groupID
	 * @param userIDs
	 * @throws ElementUpdateException
	 */
	//Long
	public GroupMars addUsersToGroup(Long groupID, List userIDs)
			throws ElementUpdateException {
		
		try {

			GroupMars group = getGroup(groupID);
			
			if (userIDs != null) {
				for(Iterator i = userIDs.iterator(); i.hasNext();) {
					User user = UserController.getInstance().getUser((Long)i.next());
					group.addUser(user);
                    addUserToOSGroups(user);
				}
			}		
			
			validateGroup(group);
			return (GroupMars)DBAccessController.save(group);

		} catch (ControllerException e) {
			throw new ElementUpdateException(e.getMessage());
		}
		
	}
	
	/**
	 * Removes a User from a Group
	 * 
	 * @param groupID
	 * @param userID
	 * @throws ElementUpdateException
	 */
	public GroupMars removeUserFromGroup(Long groupID, Long userID)
			throws ElementUpdateException {

		try {

			GroupMars group = getGroup(groupID);
			User user = UserController.getInstance().getUser(userID);
			
			logger.warn("REMOVE USER: " + userID);
			if (group == null) {
				logger.warn("GRUPPE VERLOREN");
			}
			group.removeUser(user);
			logger.warn("USER REMOVED: " + userID);

			validateGroup(group);
            addUserToOSGroups(user);
			return (GroupMars)DBAccessController.save(group);

		} catch (ControllerException e) {
			throw new ElementUpdateException(e.getMessage());
		}
	}
	
	/**
	 * Removes a List of Users from Group.
	 * 
	 * @param groupID
	 * @param userIDs
	 * @throws ElementUpdateException
	 */
	//Long
	public GroupMars removeUsersFromGroup(Long groupID, List userIDs)
			throws ElementUpdateException {
		try {
			GroupMars g = null;
			if (userIDs != null) {
				for (Iterator i = userIDs.iterator(); i.hasNext();) {
					g = removeUserFromGroup(groupID, (Long) i.next());
				}
			}
			return g;
		} catch (ControllerException e) {
			throw new ElementUpdateException(e.getMessage());
		}

	}
	
	/**
	 * Loads a group from DB
	 * 
	 * @param groupID
	 * @return
	 * @throws ControllerException
	 */
	public GroupMars getGroup(Long groupID) throws ControllerException {
		return (GroupMars)DBAccessController.getObjectByID(GroupMars.class,groupID);
	}
	
	/**
	 * loads a list of groups from DB
	 * @param groupIDs
	 * @return
	 * @throws ControllerException
	 */
	//List
	public List getGroups(List groupIDs) throws ControllerException {
		return DBAccessController.getObjectsByIDs(GroupMars.class,groupIDs);
	}
	
	/**
	 * Loads all groups from DB
	 * @return
	 */
	//List
	public List getAllGroups() {
		return DBAccessController.getAllObjectsFromQuery(DBAccessController.GET_ALL_GROUPS);
	}
	/**
	 * 
	 */
	public void removeUserFromAllGroups(User user) {
		//TODO Verbessern, nicht alle Gruppen durchlaufen
		List grps = DBAccessController.getAllObjectsFromQuery(DBAccessController.GET_ALL_GROUPS);
		for (Iterator i = grps.iterator(); i.hasNext();) {
			GroupMars g = (GroupMars)i.next();
			g.removeUser(user);
		}
	}
	
	
	/**
	 * retuns all users of a given group 
	 * @param groupID
	 * @return
	 * @throws ControllerException
	 */
	//List
	public List getUsersForGroup(Long groupID) throws ControllerException {
		ArrayList users = new ArrayList();
		
		GroupMars group = getGroup(groupID);
		if (group != null) {
				users.addAll(group.getUsers());
		}
		return users;
	
	}

	
	/**
	 * Validates a Group
	 *  
	 * @param group
	 * @throws ElementValidationException
	 */
	public void validateGroup(GroupMars group) throws ElementValidationException {
		// TODO validate
		
	}
//START_AUTO_GEN
	public ModuleDescription addCommands(ModuleDescription module) {
		Command createGroup = new Command("createGroup", new ObjectParameter(GroupMars.class), this);

		createGroup.addParameter(new StringParameter("name", null));
		createGroup.addParameter(new StringParameter("comment", null));
		createGroup.addParameter(new ObjectParameter("expirationDate", Date.class));
		createGroup.addParameter(new ListParameter("userIDs", Long.class));

		module.addInterface(createGroup);
		Command updateGroup = new Command("updateGroup", new BooleanParameter(), this);

		updateGroup.addParameter(new ObjectParameter("id", Long.class));
		updateGroup.addParameter(new StringParameter("name", null));
		updateGroup.addParameter(new StringParameter("comment", null));
		updateGroup.addParameter(new ObjectParameter("expirationDate", Date.class));

		module.addInterface(updateGroup);
		Command updateGroupWithUsers = new Command("updateGroupWithUsers", new BooleanParameter(), this);

		updateGroupWithUsers.addParameter(new ObjectParameter("id", Long.class));
		updateGroupWithUsers.addParameter(new StringParameter("name", null));
		updateGroupWithUsers.addParameter(new StringParameter("comment", null));
		updateGroupWithUsers.addParameter(new ObjectParameter("expirationDate", Date.class));
		updateGroupWithUsers.addParameter(new ListParameter("userIDs", Long.class));

		module.addInterface(updateGroupWithUsers);
		Command deleteGroup = new Command("deleteGroup", null, this);

		deleteGroup.addParameter(new ObjectParameter("id", Long.class));

		module.addInterface(deleteGroup);
		Command addUserToGroup = new Command("addUserToGroup", new ObjectParameter(GroupMars.class), this);

		addUserToGroup.addParameter(new ObjectParameter("groupID", Long.class));
		addUserToGroup.addParameter(new ObjectParameter("userID", Long.class));

		module.addInterface(addUserToGroup);
		Command addUsersToGroup = new Command("addUsersToGroup", new ObjectParameter(GroupMars.class), this);

		addUsersToGroup.addParameter(new ObjectParameter("groupID", Long.class));
		addUsersToGroup.addParameter(new ListParameter("userIDs", Long.class));

		module.addInterface(addUsersToGroup);
		Command removeUserFromGroup = new Command("removeUserFromGroup", new ObjectParameter(GroupMars.class), this);

		removeUserFromGroup.addParameter(new ObjectParameter("groupID", Long.class));
		removeUserFromGroup.addParameter(new ObjectParameter("userID", Long.class));

		module.addInterface(removeUserFromGroup);
		Command removeUsersFromGroup = new Command("removeUsersFromGroup", new ObjectParameter(GroupMars.class), this);

		removeUsersFromGroup.addParameter(new ObjectParameter("groupID", Long.class));
		removeUsersFromGroup.addParameter(new ListParameter("userIDs", Long.class));

		module.addInterface(removeUsersFromGroup);
		Command getGroup = new Command("getGroup", new ObjectParameter(GroupMars.class), this);

		getGroup.addParameter(new ObjectParameter("groupID", Long.class));

		module.addInterface(getGroup);
		Command getGroups = new Command("getGroups", new ObjectParameter(List.class), this);

		getGroups.addParameter(new ListParameter("groupIDs", String.class));

		module.addInterface(getGroups);
		Command getAllGroups = new Command("getAllGroups", new ObjectParameter(List.class), this);


		module.addInterface(getAllGroups);
		Command removeUserFromAllGroups = new Command("removeUserFromAllGroups", null, this);

		removeUserFromAllGroups.addParameter(new ObjectParameter("user", User.class));

		module.addInterface(removeUserFromAllGroups);
		Command getUsersForGroup = new Command("getUsersForGroup", new ObjectParameter(List.class), this);

		getUsersForGroup.addParameter(new ObjectParameter("groupID", Long.class));

		module.addInterface(getUsersForGroup);
		Command validateGroup = new Command("validateGroup", null, this);

		validateGroup.addParameter(new ObjectParameter("group", GroupMars.class));

		module.addInterface(validateGroup);
		return module;
	}
//STOP_AUTO_GEN
}
