package server.domain;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Date;

import server.controller.DBAccessController;
import server.controller.GroupController;



/**
 * @author Rajkumar
 * 
 * 
 * Date 		Author 		Change
 * 04/08/05		RDE			defaultProfil -> Typkorrektur
 * 								Department eingebaut
 * 								Konstruktoren erstellt
 * 								add/remove SRProfil
 */
public class User {

    private Long id;
    private Set identifications = new HashSet();
    private String userLoginID;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String comment;
    private Date expirationDate;
    private boolean expirationNotified = false;
    private boolean administrator;
    private boolean superDuperRepeat;
    private SmartRoomProfile defaultProfile;
    private Department department;
    private int quota;
    public Set groups = new HashSet();
    public Set smartRoomProfiles = new HashSet();

    
    
    public User() {
    	
    }

	/**
	 * @return Returns the department.
	 */
	public Department getDepartment() {
		return department;
	}
	/**
	 * @param department The department to set.
	 */
    public void setDepartment(Department department) {
    	this.department = department;	
/*    	if (this.department != null && department != null) {
    		if (this.department != department) {
    			this.department.getUsers().remove(this);
    			this.department = department;
    			this.department.addUser(this);
    		}
    	} else if (this.department != null && department == null){
			this.department.getUsers().remove(this);
			this.department = department;
    	} else if (this.department == null && department != null) {
			this.department = department;
			this.department.addUser(this);
    	} else {
    		return;
    	}*/
    	
	}

    
	/**
	 * @param opsTagID
	 * @param userLoginID
	 * @param password
	 * @param firstName
	 * @param lastName
	 * @param email
	 * @param comment
	 * @param administrator
	 * @param quota
	 */
	public User(Set identifications, String userLoginID, String password,
			String firstName, String lastName, String email, String comment,
			boolean administrator, boolean superDuperRepeat, Date expirationDate, Department department, Set groups, int quota) {
		super();
		this.setIdentifications(identifications);
		this.userLoginID = userLoginID;
		this.password = password;
		this.superDuperRepeat = superDuperRepeat;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.comment = comment;
		this.administrator = administrator;
		this.expirationDate = expirationDate;
		this.setDepartment(department);
		this.setGroups(groups);
		this.quota = quota;
	}
	
	
	/**
	 * @param opsTagID
	 * @param userLoginID
	 * @param password
	 * @param firstName
	 * @param lastName
	 * @param email
	 * @param comment
	 * @param administrator
	 * @param defaultProfile
	 * @param quota
	 * @param groups
	 * @param smartRoomProfiles
	 */
	public User(Set identifications, String userLoginID, String password,
			String firstName, String lastName, String email, String comment,
			boolean administrator, SmartRoomProfile defaultProfile,
			Department department, int quota, Set groups, Set smartRoomProfiles) {
		super();
		
		this.setIdentifications( identifications);
		this.userLoginID = userLoginID;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.comment = comment;
		this.administrator = administrator;
		this.department = department;
		this.defaultProfile = defaultProfile;
		this.quota = quota;
		this.groups = groups;
		this.smartRoomProfiles = smartRoomProfiles;
		
	}
	/**
	 * @return Returns the administrator.
	 */
	public boolean isAdministrator() {
		return administrator;
	}
	/**
	 * @param administrator The administrator to set.
	 */
	public void setAdministrator(boolean administrator) {
		this.administrator = administrator;
	}

    public boolean isSuperDuperRepeat() {
        return superDuperRepeat;
    }

    public void setSuperDuperRepeat(boolean _p) {
        superDuperRepeat = _p;
    }
    
	/**
	 * @return Returns the comment.
	 */
	public String getComment() {
		return comment;
	}
	/**
	 * @param comment The comment to set.
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}
	/**
	 * @return Returns the defaultProfile.
	 */
	public SmartRoomProfile getDefaultProfile() {
		return defaultProfile;
	}
	/**
	 * @param defaultProfile The defaultProfile to set.
	 */
	public void setDefaultProfile(SmartRoomProfile defaultProfile) {
		this.defaultProfile = defaultProfile;
	}
	/**
	 * @return Returns the email.
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email The email to set.
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return Returns the firstName.
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * @param firstName The firstName to set.
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/**
	 * @return Returns the groups.
	 */
	public Set getGroups() {
		return groups;
	}
	/**
	 * @param groups The groups to set.
	 */
	public void setGroups(Set groups) {
		this.groups = groups;
		/*if (groups == null) {
			return;
		} else {
			for(Iterator i = groups.iterator(); i.hasNext();){
				GroupMars g = (GroupMars)i.next();
				g.addUser(this);
			}
		}*/
	}
	/**
	 * @return Returns the id.
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id The id to set.
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return Returns the lastName.
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * @param lastName The lastName to set.
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return Returns the password.
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password The password to set.
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return Returns the quota.
	 */
	public int getQuota() {
		return quota;
	}
	/**
	 * @param quota The quota to set.
	 */
	public void setQuota(int quota) {
		this.quota = quota;
	}
	/**
	 * @return Returns the smartRoomProfiles.
	 */
	public Set getSmartRoomProfiles() {
		return smartRoomProfiles;
	}
	/**
	 * @param smartRoomProfiles The smartRoomProfiles to set.
	 */
	public void setSmartRoomProfiles(Set smartRoomProfiles) {
		this.smartRoomProfiles = smartRoomProfiles;
	}
	/**
	 * @return Returns the userLoginID.
	 */
	public String getUserLoginID() {
		return userLoginID;
	}
	/**
	 * @param userLoginID The userLoginID to set.
	 */
	public void setUserLoginID(String userLoginID) {
		this.userLoginID = userLoginID;
	}
	
	public void addSmartRoomProfile(SmartRoomProfile profile) {
		//profile.setUser(this);
		this.smartRoomProfiles.add(profile);
	}
	
	public void removeSmartRoomProfile(SmartRoomProfile profile) {
		this.smartRoomProfiles.remove(profile);
	}
	
	/**
	 * @return Returns the identifications.
	 */
	public Set getIdentifications() {
		return identifications;
	}
	
	public void addGroup(GroupMars group) {
		this.groups.add(group);
        group.getUsers().add(this);
	}

	public void removeGroup(GroupMars group) {
		this.groups.remove(group);
        group.getUsers().remove(this);
	}
	
	
	/**
	 * @param identifications The identifications to set.
	 */
	public void setIdentifications(Set identifications) {
		this.identifications = identifications;
		if (identifications == null) {
			return;
		} 
		else {
			for (Iterator i = identifications.iterator(); i.hasNext();) {
				Identification ident = (Identification)i.next();
				ident.setUser(this);
			}
		}
	}
	
	public void addIdentification(Identification ident) {
		this.identifications.add(ident);
		if (ident.getUser() != this) {
			ident.setUser(this);
		}
	}

	public void addIdentification(LogInSystem logInSystem, String tagID) {
		Identification ident = new Identification(logInSystem,tagID);
		addIdentification(ident);
	}

	public void removeIdentification(Identification ident) {
		this.identifications.remove(ident);
		ident.setUser(null);
	}

	public void removeIdentificationsForLogInSystem(LogInSystem logInSystem) {
		removeIdentificationsForLogInSystemNamed(logInSystem.getName());
	}

	public void removeIdentificationsForLogInSystemNamed(String logInSystemName) {
		Set identificationsToRemove = new HashSet();
		
		/* never change the Set while iterating over it!!! */
		for (Iterator i = this.identifications.iterator(); i.hasNext();) {
			Identification ident = (Identification) i.next();
			if (ident.getLogInSystem().getName().equalsIgnoreCase(logInSystemName)) {
				identificationsToRemove.add(ident);
			}
		}
		this.getIdentifications().removeAll(identificationsToRemove);
	}
	
	public boolean isMemberOfGroup(GroupMars group) {
		return this.getGroups().contains(group);
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

    public void setExpirationNotified(boolean flag) {
	expirationNotified = flag;
    }

    public boolean getExpirationNotified() {
	return expirationNotified;
    }

    public boolean equals(Object obj)
    {
        if(obj instanceof User)
            return this.getId().equals(((User)obj).getId());
        return false;
    }
}
