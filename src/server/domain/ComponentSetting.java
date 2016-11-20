/*
 * Created on 09.08.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package server.domain;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Rajkumar
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ComponentSetting {
	
	private Long id;
	private SmartRoomComponent smartRoomComponent;
	private Set componentAttributeValues = new HashSet();	
		
	/**
	 * 
	 */
	public ComponentSetting() {
	}
	
	/**
	 * Creates a Setting for a Component. The Default-Values for the Settings
	 * are automaticaly created.
	 * 
	 * @param smartRoomComponent
	 */
	public ComponentSetting(SmartRoomComponent smartRoomComponent){
		this.smartRoomComponent = smartRoomComponent;
		
		for (Iterator i = smartRoomComponent.getComponentAttributes()
				.iterator(); i.hasNext();) {
			addValueForAttribute((ComponentAttribute)i.next());			
		}
		
	}
	

	
	/**
	 * adds the default Value of the attrinute to the setting
	 * @param attribute
	 */
	private void addValueForAttribute(ComponentAttribute attribute) {
		this.componentAttributeValues.add(
				attribute.getDefaultAttributeValue());
		
	}
	/**
	 * @return Returns the smartRoomComponent.
	 */
	public SmartRoomComponent getSmartRoomComponent() {
		return smartRoomComponent;
	}
	/**
	 * @param smartRoomComponent The smartRoomComponent to set.
	 */
	public void setSmartRoomComponent(SmartRoomComponent smartRoomComponent) {
		this.smartRoomComponent = smartRoomComponent;
	}
	

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return Returns the componentAttributeValues.
	 */
	public Set getComponentAttributeValues() {
		return componentAttributeValues;
	}
	/**
	 * @param componentAttributeValues The componentAttributeValues to set.
	 */
	public void setComponentAttributeValues(Set componentAttributeValues) {
		/*if (componentAttributeValues == null) {
			this.componentAttributeValues = new HashSet();
		} else {*/
			this.componentAttributeValues = componentAttributeValues;
		//}
	}
	
	public void addComponentAttributeValue(ComponentAttributeValue value) {
		this.componentAttributeValues.add(value);
	}
	
	public void removeComponentAttributeValue(ComponentAttributeValue value) {
		this.componentAttributeValues.remove(value);
	}
	
}
