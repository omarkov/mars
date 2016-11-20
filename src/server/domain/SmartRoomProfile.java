package server.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

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
public class SmartRoomProfile {

    private Long id;
    private String name;
    private String comment;
    private Date lastChange;
    public User user;
    public Set componentSettings = new HashSet();

    
    public SmartRoomProfile(){
    	
    }
    
	/**
	 * @param name
	 * @param comment
	 * @param lastChange
	 * @param group
	 * @param user
	 */
	public SmartRoomProfile(String name, String comment, User user) {
		super();
		this.name = name;
		this.comment = comment;
		this.lastChange = new Date();
		this.user = user;
		user.addSmartRoomProfile(this);
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
	 * @return Returns the lastChange.
	 */
	public java.util.Date getLastChange() {
		return lastChange;
	}
	/**
	 * @param lastChange The lastChange to set.
	 */
	public void setLastChange(java.util.Date lastChange) {
		this.lastChange = lastChange;
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
		user.addSmartRoomProfile(this);
	}
	
	/**
	 * @return
	 */
	public Set getComponentSettings(){
		return this.componentSettings;
	}
	
	/**
	 * @param componentProperties
	 */
	public void setComponentSettings(Set componentSettings){
		this.componentSettings= componentSettings;
	}
	
	/**
	 * @param setting
	 */
	public void addComponentSetting(ComponentSetting setting) {
		this.componentSettings.add(setting);
	}

	/**
	 * @param setting
	 */
	public void removeComponentSetting(ComponentSetting setting) {
		this.componentSettings.remove(setting);
	}

	/**
	 * @param c
	 */
	public void removeComponentSetting(SmartRoomComponent c) {
		this.getComponentSettings().remove(c);		
	}

	/**
	 * Removes the settings for the Componenent named as the given name
	 * @param componentName
	 */
	public void removeComponentSetting(String componentName) {
		removeComponentSetting(getComponentSettingForComponent(componentName));
	}
	
	
	/**
	 * Checks if there is a ComponentSetting for a given Component. Components
	 * are identified by their name.
	 * 
	 * @param component
	 * @return
	 */
	public boolean hasComponentSettingForComponent(SmartRoomComponent component) {
		return (getComponentSettingForComponent(component) != null);
	}
	

	

	/**
	 * return the Setting for the given Component.
	 * @param component
	 * @return
	 */
	public ComponentSetting getComponentSettingForComponent(SmartRoomComponent component) {
		return getComponentSettingForComponent(component.getName());
	}
	
	
	/**
	 * Return the Setting for the Component with the given name.
	 * @param componentName
	 * @return
	 */
	public ComponentSetting getComponentSettingForComponent(
			String componentName) {
		/*
		 * checks if there is a ComponentSetting which is associated to
		 * Component with the same name as the given name
		 */
		for (Iterator i = this.componentSettings.iterator(); i.hasNext();) {
			ComponentSetting setting = (ComponentSetting) i.next();
			if (setting.getSmartRoomComponent().getName().equalsIgnoreCase(
					componentName)) {
				return setting;
			}
		}

		return null;
	}
	
 }
