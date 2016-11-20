package web.java;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.NetworkException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import server.domain.GroupMars;
import web.java.controller.ControllerConnection;
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

public final class ManageGroupForm extends ActionForm{
	private List groups; //Holds all available groups set by the reset method.
	private Long currentGroupID; //Holds the selected group set in GroupTableDecorator.
	private String dispatch; //tells the action-class with forward to take. 
	
	
	/**
	 * Get all Groups to display them in the corresponding JSP
	 *
	 */
	public ManageGroupForm() {
		
	}

	public List getGroups() {
		return groups;
	}

	public void setGroups(List groups) {
		this.groups = groups;
	}


	public String getDispatch() {
		return dispatch;
	}

	public void setDispatch(String dispatch) {
		this.dispatch = dispatch;
	}

	public GroupMars getSelectedGroup() {
		System.out.println("LOG: >" + dispatch + "<");
		for(int i=0; i < groups.size(); i++)
		{
			GroupMars group = (GroupMars)groups.get(i);
			if(group.getId().equals(currentGroupID))
			{
				return group;
			}
		}

		return null;
	}

	public void reset(ActionMapping mapping, HttpServletRequest request) {
		try {
			ControllerConnection controllerConnection = (ControllerConnection)request.getSession().getAttribute(Constants.CONNECTION);
			groups = controllerConnection.getAllGroups();
		} catch (NetworkException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		super.reset(mapping, request);
	}

	public Long getCurrentGroupID() {
		return currentGroupID;
	}

	public void setCurrentGroupID(Long currentGroupID) {
		this.currentGroupID = currentGroupID;
	}

}
