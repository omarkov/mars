package web.java;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.NetworkException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import server.domain.SmartRoomProfile;
import server.domain.User;
import web.java.controller.ControllerConnection;
import web.java.controller.NotAuthorizedException;
import web.java.controller.UserException;
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

public class ManageProfilesShowForm extends ActionForm{
    private List currentUserProfiles; // holds the profiles for the currently selected user
    private Long currentProfileID;
    private String dispatch = null;
    private User selectedUser;
    private String defaultProfile = null;
	private SmartRoomProfile profile;
	    
    public String getDispatch() {
	return dispatch;
    }

    public void setDispatch(String dispatch) {
	this.dispatch = dispatch;
    }

	public Long getCurrentProfileID() {
		return currentProfileID;
	} 
    
    public void setCurrentProfileID(Long currentProfileID) {
	this.currentProfileID = currentProfileID;
    }

    public List getCurrentUserProfiles() {
	return currentUserProfiles;
    }

    public void setCurrentUserProfiles(List currentUserProfiles) {
	this.currentUserProfiles = currentUserProfiles;
    }

    public User getSelectedUser() {
	return selectedUser;
    }

    public void setSelectedUser(User selectedUser) {
	this.selectedUser = selectedUser;
    }

	public String getDefaultProfile() {
		return defaultProfile;
	}

	public void setDefaultProfile(String defaultProfile) {
		this.defaultProfile = defaultProfile;
	}
    
	public void setProfile(SmartRoomProfile profile) {
		this.profile = profile;
	}
	
	
    public SmartRoomProfile getSelectedProfile() {
    	for(int i=0; i < currentUserProfiles.size(); i++) 
    	{
    		SmartRoomProfile profile = (SmartRoomProfile)currentUserProfiles.get(i);

		    if(profile.getId().equals(currentProfileID))
		    {
		    	return profile;
		    }
    	}

    	return null;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
	try {
	    ControllerConnection controllerConnection = (ControllerConnection)
		request.getSession().getAttribute(Constants.CONNECTION);

        if(controllerConnection == null) return;

	    //If the user clicked on "Show Profiles" in manageUser.jsp
	    if (request.getParameter("fromMenue") != null) 
	    {
            selectedUser = (User)controllerConnection.getCurrentUser();
	    } 
	    else 
	    {
	    	if (request.getSession().getAttribute("selectedUser") == null)
	    	{
	    		selectedUser = (User)controllerConnection.getCurrentUser();
	    	}
	    	else
		    {
	    		selectedUser = ((User)request.getSession().getAttribute("selectedUser"));
		    }
	    }
	    request.getSession().setAttribute("currentUser", selectedUser);
		System.out.println(selectedUser.getLastName());
	    currentUserProfiles = (List) controllerConnection.getProfilesForUser(selectedUser.getId());
	    
		//Why?
	    //set the name of the default profile refresh User before
	    //selectedUser = (User)controllerConnection.getCurrentUser();
	    if (selectedUser.getDefaultProfile() != null)
	    {
	    	defaultProfile = selectedUser.getDefaultProfile().getName();
	    }
	    else
	    {
	    	defaultProfile = "No default profile";
	    }
	    	
	} catch (NetworkException e) {
	    e.printStackTrace();
	}
	super.reset(mapping, request);
    }
}
