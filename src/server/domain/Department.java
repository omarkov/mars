package server.domain;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import server.domain.*;

/**
 * @author Rajkumar
 * 
 * Change History 
 * Date 		Author 		Change
 * 04/08/05		RDE			Users eingebaut
 * 
 * 			
 */
public class Department {

	public final static int NAME_MAX_LENGTH = 255;
	
    private Long id;
    private String name;
    private String abbreviation;
    private String comment;
    private Set users = new HashSet();
    
    public Department(){
    	
    }
    
	/**
	 * @param name
	 * @param abbreviation
	 * @param comment
	 */
	public Department(String name, String abbreviation, String comment) {
		//super();
		this.name = name;
		this.abbreviation = abbreviation;
		this.comment = comment;
	}
	
	
	/**
	 * @param name
	 * @param abbreviation
	 * @param comment
	 * @param users
	 */
	public Department(String name, String abbreviation, String comment,
			List users) {
		//super();
		this.name = name;
		this.abbreviation = abbreviation;
		this.comment = comment;
		this.users = new HashSet();
		// this.users.addAll(users);
	}

	/**
	 * @return Returns the abbreviation.
	 */
	public String getAbbreviation() {
		return abbreviation;
	}
	/**
	 * @param abbreviation The abbreviation to set.
	 */
	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
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
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	
	/**
	 * @return Returns the users.
	 */
	public Set getUsers() {
		return users;
	}
	/**
	 * @param users The users to set.
	 */
	public void setUsers(Set users) {
		
		if (users == null) {
			this.users = new HashSet();
		} else {
			this.users = users;
		}
	}
	
	public void addUser(User user) {
		this.getUsers().add(user);
		if (user.getDepartment() != this) {
			user.setDepartment(this);
		}
	}
	
	public void removeUser(User user) {
		this.getUsers().remove(user);
		if (user.getDepartment() != null) {
			user.setDepartment(null);
		}
	}
	
	public void removeAllUsers() {
		this.setUsers(new HashSet());
	}
	
 }
