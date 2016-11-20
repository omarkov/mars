package server.domain;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Date;

/**
 * @author Rajkumar
 * 
 * Change History 
 * Date 		Author 		Change
 * 04/08/05		RDE			attribute korrigiert		
 * 09/08/05		RDE			Gruppen haben keine Profile	
 * 
 * 			
 */
public class GroupMars {

    private Long id;
    private String name;
    private String comment;
    private Date expirationDate;
    private Set users = new HashSet();
    
    public GroupMars(){
    	
    }
    

	/**
	 * @param name
	 * @param comment
	 * @param expirationDate
	 * @param users
	 */
	public GroupMars(String name, String comment, Date expirationDate, Set users) {
		this.name = name;
		this.comment = comment;
		this.expirationDate = expirationDate;
		if (users != null) {
			for(Iterator i = users.iterator(); i.hasNext();) {
				User u = (User)i.next();
				System.out.println(u);
				this.addUser(u);
			}
		}
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
	 * @return Returns the expirationDate.
	 */
	public java.util.Date getExpirationDate() {
		return expirationDate;
	}
	/**
	 * @param expirationDate The expirationDate to set.
	 */
	public void setExpirationDate(java.util.Date expirationDate) {
		this.expirationDate = expirationDate;
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
		this.users = users;
	}
	
	public void addUser(User user) {
		this.getUsers().add(user);
        user.getGroups().add(this);
	}
	
	public void removeUser(User user){
		this.getUsers().remove(user);
        user.getGroups().remove(this);
	}
 }
