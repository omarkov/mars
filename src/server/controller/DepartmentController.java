package server.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import net.*;

import server.controller.exceptions.*;
import server.domain.*;


/**
 * @author Rajkumar
 * 
 * Change History 
 * Date 		Author 		Change
 * 04/08/05		RDE			created			
 * 
 * 			
 */
/**
 * @author Rajkumar
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DepartmentController implements NetworkEventListener {

	
	private static DepartmentController _instance;
	
	private Logger logger = Logger.getLogger(DepartmentController.class);
		
	public final static String DEPARTMENT_NULL ="Department must not be null";
	public final static String NAME_MIN_LENGTH_UNDERSHOOT ="The name must be filled";
	public final static String NAME_MAX_LENGTH_EXCEEDED = "The name must not exceed "
			+ Department.NAME_MAX_LENGTH;
	
	private DepartmentController() {
	}
	 
	/**
	 * @return Returns the _instance.
	 */
	public static DepartmentController getInstance() {
		if (DepartmentController._instance == null){
			DepartmentController._instance = new DepartmentController() ;
		}
		return DepartmentController._instance;	
	}


	
	public Department createDepartment(String name, String abbreviation,
			String comment) throws ElementCreationException {
		return createDepartmentWithUsers(name,abbreviation,comment,new ArrayList());
	}
	
	/**
	 * 
	 * Generates a new Department Element and stores it in DB.
	 * @param name
	 * @param abbreviation
	 * @param comment
	 * @param users
	 * @throws ElementCreationException
	 */
	//Long
	public Department createDepartmentWithUsers(String name, String abbreviation,
			String comment, List userIDs) throws ElementCreationException {
		
		
		try {
			/* Lädt die User aus der DB */
			List users = UserController.getInstance().getUsers(userIDs);
			/* Erstellt das neue User OBJ */
			Department department = new Department(name, abbreviation, comment,
					users);

			/* validiert und speicher es in DB */ 
			validateDepartment(department);

            department = (Department)DBAccessController.save(department);

			for (Iterator i = users.iterator(); i.hasNext();) {
				User u = (User)i.next();
				if(u != null)
			  	{	
					System.out.println(u.getFirstName());
					u.setDepartment(department);
					DBAccessController.save(u);
				}else
				{
					System.out.println("User is null");
				}
			}

			return department;
			
		} catch ( ControllerException e) {
			throw new ElementCreationException(e.getMessage());
		}
		
	}
	
	/**
	 * Receives the Department with the given id from DB, updates it and stores
	 * it in DB again.
	 * 
	 * @param id
	 * @param name
	 * @param abbreviation
	 * @param comment
	 * @return
	 * @throws ElementUpdateException
	 */
	public Boolean updateDepartment(Long id, String name,
			String abbreviation, String comment) throws ElementUpdateException {
		try {
			
	
			
			/* get department from db */
			Department department = this.getDepartment(id);
			
			/* update values */
			department.setAbbreviation(abbreviation);
            department.setComment(comment);
            System.out.println("ABB: " + department.getAbbreviation());
			// department.setName(name);
			/* validate and save */
			validateDepartment(department);
			DBAccessController.save(department);
		} catch (ControllerException e) {
			throw new ElementUpdateException(e.getMessage());
		}
        return Boolean.TRUE;
	}
	
	/**
	 * Receives the Department with the given id from DB, updates it and stores
	 * it in DB again.
	 * 
	 * @param id
	 * @param name
	 * @param abbreviation
	 * @param comment
	 * @param users
	 * @throws ElementUpdateException
	 */
	//Long
	public Boolean updateDepartmentWithUsers(Long id, String name, String abbreviation, String comment,
			List userIDs) throws ElementUpdateException {
		try {
			
			/* Lädt die User aus der DB */
			List users = UserController.getInstance().getUsers(userIDs);

			
			/* get department from db */
			Department department = this.getDepartment(id);
			
			/* update values */
			department.setAbbreviation(abbreviation);
			department.setName(name);
			Set usrSet = new HashSet();
			usrSet.addAll(users);
			department.setUsers(usrSet);
			
			/* validate and save */
			validateDepartment(department);
			DBAccessController.save(department);
		} catch (ControllerException e) {
			throw new ElementUpdateException(e.getMessage());
		}
		return Boolean.TRUE;
	}
	
	/**
	 * Deletes a department from DB.
	 * @param dept
	 * @throws ElementDeletionException
	 */
	protected void deleteDepartment(Department dept) throws ElementDeletionException {
		DBAccessController.delete(dept);
	}

	public void deleteDepartment(Long deptID) throws ElementDeletionException {
		try {
			Department dept = this.getDepartment(deptID);
			removeDepartmentFromAllUsers(dept);
			DBAccessController.delete(dept);
		} catch (ControllerException e) {
			throw new ElementDeletionException(e.getMessage());
		}
	}

	//List
	public List getUsersNotInDepartment(Long deptId) throws ControllerException {
		/*
		 * Object[] param = new Object[] {deptId}; List result = new
		 * ArrayList(); result.addAll(DBAccessController.getObjectsByAttributes(
		 * DBAccessController.GET_USERS_NOT_IN_DEPT, param)); return result;
		 * TODO: Query überprüfen und fixen. Untere Lösung löschen!
		 */
		
		
		List users = UserController.getInstance().getAllUsers();
		Set usersInDept = DepartmentController.getInstance().getDepartment(
				deptId).getUsers();
		users.removeAll(usersInDept);

		return users;
	}
	
	/**
	 * Valiates a Department. If a validation error occurs, an exception is thrown.
	 * 
	 * @param department
	 * @throws ElementValidationException
	 */
	public void validateDepartment(Department department)
			throws ElementValidationException {
		// TODO validate
		
		if (department == null) {
			throw new ElementValidationException(DEPARTMENT_NULL);
			
		} else if ((department.getName() == null)
				|| (department.getName().length() == 0)) {
			throw new ElementValidationException(NAME_MIN_LENGTH_UNDERSHOOT);
			
		} else if (department.getName().length() > Department.NAME_MAX_LENGTH) {
			throw new ElementValidationException(NAME_MAX_LENGTH_EXCEEDED);
		}
	}
	/**
	 * Adds a User to a Group.
	 * 
	 * @param groupID
	 * @param userID
	 * @throws ElementUpdateException
	 */
	public Department addUserToDepartment(Long deptID, Long userID) throws ElementUpdateException {
		try {
	
			Department dept= this.getDepartment(deptID);
			User user = UserController.getInstance().getUser(userID);
	
		dept.addUser(user);
		validateDepartment(dept);
		return (Department)DBAccessController.save(dept);
	
		} catch (ControllerException e) {
			throw new ElementUpdateException(e.getMessage());
		}

	}

	
	/**
	 * Adds the users with the given IDs to the department with the given id.
	 * @param deptID
	 * @param userIDs
	 * @throws ElementUpdateException
	 */
	//Long
	public Department addUsersToDepartment(Long deptID, List userIDs)
			throws ElementUpdateException {

		try {
			Department d = null;
			for (Iterator i = userIDs.iterator(); i.hasNext();) {
				d = addUserToDepartment(deptID,(Long)i.next());
			}
			return d;
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
	public Department removeUserFromDepartment(Long deptID, Long userID)
			throws ElementUpdateException {

		try {

			Department dept = this.getDepartment(deptID);
			User user = UserController.getInstance().getUser(userID);

			dept.removeUser(user);
			validateDepartment(dept);
			return (Department)DBAccessController.save(dept);

		} catch (ControllerException e) {
			throw new ElementUpdateException(e.getMessage());
		}
	}

	/**
	 * Removes the given users from the department.
	 * 
	 * @param deptID
	 * @param userIDs
	 * @throws ElementUpdateException
	 */
	public void removeUsersFromDepartment(Long deptID, List userIDs)
			throws ElementUpdateException {

		try {

			for (Iterator i = userIDs.iterator(); i.hasNext();) {
				removeUserFromDepartment(deptID, (Long)i.next());
			}

		} catch (ControllerException e) {
			throw new ElementUpdateException(e.getMessage());
		}

	}
	
	
	/**
	 * Returns all users of a Department
	 * 
	 * @param deptID
	 * @return
	 * @throws ControllerException
	 */
	//List
	public List getUsersForDepartment(Long deptID) throws ControllerException {
		ArrayList users = new ArrayList();
		
		Department dept = getDepartment(deptID);
		if (dept != null) {
			users.addAll(dept.getUsers());
		}
		return users;
	}
	
	/**
	 * Every user has zero or one department. If a department is deleted,
	 * the users of the department loose it.
	 * 
	 * @param dept
	 * @throws ElementUpdateException
	 */
	private void removeDepartmentFromAllUsers(Department dept) throws ElementUpdateException {
		Object[] deptID = new Object[] { dept.getId() };
		List users = DBAccessController.getObjectsByAttributes(
					DBAccessController.GET_ALL_USERS_BY_DPTID,deptID);
		if ((users == null) || (users.size() == 0)) {
			return;
		}
		for(Iterator i = users.iterator(); i.hasNext();) {
			User u = (User)i.next(); 
			u.setDepartment(null);
			UserController.getInstance().updateUser(u.getId(),
					u.getIdentifications(), u.getUserLoginID(),
					u.getPassword(), u.getFirstName(), u.getLastName(),
					u.getEmail(), u.getComment(), u.isAdministrator(), u.isSuperDuperRepeat(), u.getExpirationDate(),
					u.getDepartment(), u.getDefaultProfile(), u.getQuota(),
					u.getGroups(), u.getSmartRoomProfiles());
		}
		
	}
	
	/**
	 * Loads a Department from DB
	 * 
	 * @param deptID
	 * @return
	 * @throws ControllerException
	 */
	public Department getDepartment(Long deptID) throws ControllerException {
		return (Department)DBAccessController.getObjectByID(Department.class,deptID);
	}

	/**
	 * Loads departments with given ids from the DB
	 * 
	 * @param deptIDs
	 * @return
	 * @throws ControllerException
	 */
	//List
	public List getDepartments(List deptIDs) throws ControllerException {
		return DBAccessController.getObjectsByIDs(Department.class,deptIDs);
	}

	/**
	 * Loads all departments from DB.
	 * @return
	 */
	//List
	public List getAllDepartments() {
		return DBAccessController.getAllObjectsFromQuery(DBAccessController.GET_ALL_DEPTS);
	}
//START_AUTO_GEN
	public ModuleDescription addCommands(ModuleDescription module) {
		Command createDepartment = new Command("createDepartment", new ObjectParameter(Department.class), this);

		createDepartment.addParameter(new StringParameter("name", null));
		createDepartment.addParameter(new StringParameter("abbreviation", null));
		createDepartment.addParameter(new StringParameter("comment", null));

		module.addInterface(createDepartment);
		Command createDepartmentWithUsers = new Command("createDepartmentWithUsers", new ObjectParameter(Department.class), this);

		createDepartmentWithUsers.addParameter(new StringParameter("name", null));
		createDepartmentWithUsers.addParameter(new StringParameter("abbreviation", null));
		createDepartmentWithUsers.addParameter(new StringParameter("comment", null));
		createDepartmentWithUsers.addParameter(new ListParameter("userIDs", Long.class));

		module.addInterface(createDepartmentWithUsers);
		Command updateDepartment = new Command("updateDepartment", new BooleanParameter(), this);

		updateDepartment.addParameter(new ObjectParameter("id", Long.class));
		updateDepartment.addParameter(new StringParameter("name", null));
		updateDepartment.addParameter(new StringParameter("abbreviation", null));
		updateDepartment.addParameter(new StringParameter("comment", null));

		module.addInterface(updateDepartment);
		Command updateDepartmentWithUsers = new Command("updateDepartmentWithUsers", new BooleanParameter(), this);

		updateDepartmentWithUsers.addParameter(new ObjectParameter("id", Long.class));
		updateDepartmentWithUsers.addParameter(new StringParameter("name", null));
		updateDepartmentWithUsers.addParameter(new StringParameter("abbreviation", null));
		updateDepartmentWithUsers.addParameter(new StringParameter("comment", null));
		updateDepartmentWithUsers.addParameter(new ListParameter("userIDs", Long.class));

		module.addInterface(updateDepartmentWithUsers);
		Command deleteDepartment = new Command("deleteDepartment", null, this);

		deleteDepartment.addParameter(new ObjectParameter("deptID", Long.class));

		module.addInterface(deleteDepartment);
		Command getUsersNotInDepartment = new Command("getUsersNotInDepartment", new ObjectParameter(List.class), this);

		getUsersNotInDepartment.addParameter(new ObjectParameter("deptId", Long.class));

		module.addInterface(getUsersNotInDepartment);
		Command validateDepartment = new Command("validateDepartment", null, this);

		validateDepartment.addParameter(new ObjectParameter("department", Department.class));

		module.addInterface(validateDepartment);
		Command addUserToDepartment = new Command("addUserToDepartment", new ObjectParameter(Department.class), this);

		addUserToDepartment.addParameter(new ObjectParameter("deptID", Long.class));
		addUserToDepartment.addParameter(new ObjectParameter("userID", Long.class));

		module.addInterface(addUserToDepartment);
		Command addUsersToDepartment = new Command("addUsersToDepartment", new ObjectParameter(Department.class), this);

		addUsersToDepartment.addParameter(new ObjectParameter("deptID", Long.class));
		addUsersToDepartment.addParameter(new ListParameter("userIDs", Long.class));

		module.addInterface(addUsersToDepartment);
		Command removeUserFromDepartment = new Command("removeUserFromDepartment", new ObjectParameter(Department.class), this);

		removeUserFromDepartment.addParameter(new ObjectParameter("deptID", Long.class));
		removeUserFromDepartment.addParameter(new ObjectParameter("userID", Long.class));

		module.addInterface(removeUserFromDepartment);
		Command removeUsersFromDepartment = new Command("removeUsersFromDepartment", null, this);

		removeUsersFromDepartment.addParameter(new ObjectParameter("deptID", Long.class));
		removeUsersFromDepartment.addParameter(new ListParameter("userIDs", String.class));

		module.addInterface(removeUsersFromDepartment);
		Command getUsersForDepartment = new Command("getUsersForDepartment", new ObjectParameter(List.class), this);

		getUsersForDepartment.addParameter(new ObjectParameter("deptID", Long.class));

		module.addInterface(getUsersForDepartment);
		Command getDepartment = new Command("getDepartment", new ObjectParameter(Department.class), this);

		getDepartment.addParameter(new ObjectParameter("deptID", Long.class));

		module.addInterface(getDepartment);
		Command getDepartments = new Command("getDepartments", new ObjectParameter(List.class), this);

		getDepartments.addParameter(new ListParameter("deptIDs", String.class));

		module.addInterface(getDepartments);
		Command getAllDepartments = new Command("getAllDepartments", new ObjectParameter(List.class), this);


		module.addInterface(getAllDepartments);
		return module;
	}
//STOP_AUTO_GEN
}
