package web.java;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.NetworkException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import web.java.controller.ControllerConnection;
import web.java.controller.NotAuthorizedException;

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
public class ManageDepartmentAddUsersForm extends ActionForm{

	private String action = null;
    private Long[] selectedUsers = null;
	private List usersNotInDepartment = null;
	private String dispatch = null; //tells the action-class which forward to take
	
	public String getDispatch() {
		return dispatch;
	}

	public void setDispatch(String dispatch) {
		this.dispatch = dispatch;
	}

	/**
	 * get the users that are already not in the department to
	 * be displayed in the jsp. 
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) {
			ControllerConnection controllerConnection = (ControllerConnection)
				request.getSession().getAttribute(Constants.CONNECTION);
			if (request.getAttribute("currentDepartmentID") != null) {
				try {
				usersNotInDepartment = controllerConnection.getUsersNotInDepartment((Long)request.getAttribute("currentDepartmentID"));
				
				} catch (NetworkException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 				
			}
		super.reset(mapping, request);
	}

	public Long[] getSelectedUsers() {
		return this.selectedUsers;
	}

	public void setSelectedUsers(Long[] selectedUsers) {
		this.selectedUsers = selectedUsers;
	}

	public List getUsersNotInDepartment() {
		return usersNotInDepartment;
	}

	public void setUsersNotInDepartment(List usersNotInDepartment) {
		this.usersNotInDepartment = usersNotInDepartment;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	} 

}
