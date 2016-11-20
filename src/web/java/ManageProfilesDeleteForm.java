package web.java;

import org.apache.struts.action.ActionForm;

import server.domain.SmartRoomProfile;
import server.domain.User;
/**
 * An ActionForm is a JavaBean optionally associated with 
 * one or more ActionMappings. Such a bean will have had 
 * its properties initialized from the corresponding request 
 * parameters before the corresponding Action.execute method is called.
 * When the properties of this bean have been populated, 
 * but before the execute method of the Action is called, 
 * this bean's validate method will be called, which gives 
 * the bean a chance to verify that the properties submitted 
 * by the user are correct and valid. If this method finds problems, 
 * it returns an error messages object that encapsulates those problems, 
 * and the controller servlet will return control to the corresponding 
 * input form. Otherwise, the validate method returns null, 
 * indicating that everything is acceptable and the corresponding 
 * Action.execute method should be called.
 * 
 * This class must be subclassed in order to be instantiated. 
 * Subclasses should provide property getter and setter 
 * methods for all of the bean properties they wish to expose, 
 * plus override any of the public or protected methods for which 
 * they wish to provide modified functionality.
 * Because ActionForms are JavaBeans, subclasses should also 
 * implement Serializable, as required by the JavaBean specification. 
 * Some containers require that an object meet all JavaBean 
 * requirements in order to use the introspection API upon which ActionForms rely.
 * 
 */

public final class ManageProfilesDeleteForm extends ActionForm{

	private Long profilesIDToDelete = null;
	private String dispatch = null;
	
	private User profileOfUser;
	private SmartRoomProfile profileToDelete; 
	private String profileNameToDelete;
	
	public Long getProfilesIDToDelete() {
		return profilesIDToDelete;
	}
	public void setProfilesIDToDelete(Long profilesIDToDelete) {
		this.profilesIDToDelete = profilesIDToDelete;
	}
	public String getDispatch() {
		return dispatch;
	}
	public void setDispatch(String dispatch) {
		this.dispatch = dispatch;
	}
	public SmartRoomProfile getProfileToDelete() {
		return profileToDelete;
	}
	public void setProfileToDelete(SmartRoomProfile profileToDelete) {
		this.profileToDelete = profileToDelete;
	}
	public String getProfileNameToDelete() {
		return profileNameToDelete;
	}
	public void setProfileNameToDelete(String profileNameToDelete) {
		this.profileNameToDelete = profileNameToDelete;
	}
	public User getProfileOfUser() {
		return profileOfUser;
	}
	public void setProfileOfUser(User profileOfUser) {
		this.profileOfUser = profileOfUser;
	}
}
