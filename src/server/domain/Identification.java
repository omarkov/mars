/*
 * Created on 10.08.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package server.domain;

/**
 * @author Rajkumar
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Identification {
	
	private Long id;
	private LogInSystem logInSystem;
	private String tagID;
	private User user;
	
	public Identification() {
	}

	/**
	 * @param logInSystem
	 * @param tagID
	 */
	public Identification(LogInSystem logInSystem, String tagID) {
		super();
		this.logInSystem = logInSystem;
		this.tagID = tagID;
	}
	
	public Long getId(){
		return this.id;
	}
	
	public void setId(Long id){
		this.id = id;
	}
	/**
	 * @return Returns the logInSystem.
	 */
	public LogInSystem getLogInSystem() {
		return logInSystem;
	}
	/**
	 * @param logInSystem The logInSystem to set.
	 */
	public void setLogInSystem(LogInSystem logInSystem) {
		this.logInSystem = logInSystem;
	}
	/**
	 * @return Returns the tagID.
	 */
	public String getTagID() {
		return tagID;
	}
	/**
	 * @param tagID The tagID to set.
	 */
	public void setTagID(String tagID) {
		this.tagID = tagID;
	}

	
	
	/**
	 * @return Returns the user.
	 */
	public User getUser() {
		return user;
	}
	/**
	 * @param user The user to set.
	 */
	public void setUser(User user) {
		this.user = user;
	}
}
