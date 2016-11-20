package web.java;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.NetworkException;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import server.domain.Department;
import server.domain.User;
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
public final class ManageDepartmentNewForm extends ValidatorForm{
	
	private String action = null;
	private String name = null;
	private String comment = null;
	private String dispatch = null; //tells the action-class which forward to take
	private String abbreviation = null;

    private Long[] selectedUsers = null; 
	private ArrayList users;

	public  ManageDepartmentNewForm() {
	}
	
	
	
    public Long[] getSelectedUsers() { 
	      return this.selectedUsers; 
    } 
    public void setSelectedUsers(Long[] selectedUsers) { 
	      this.selectedUsers = selectedUsers; 
    }
		
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	/**
	 * Get all Users that can be added to the new department. 
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		try {
			ControllerConnection controllerConnection = (ControllerConnection)request.getSession().getAttribute(Constants.CONNECTION);
			users = new ArrayList(controllerConnection.getAllUsers());
		} catch (NetworkException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.reset(mapping, request);
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ArrayList getUsers() {
		return users;
	}
	public void setUsers(ArrayList users) {
		this.users = users;
	}
	public String getDispatch() {
		return dispatch;
	}
	public void setDispatch(String dispatch) {
		this.dispatch = dispatch;
	}

	public String getAbbreviation() {
		return abbreviation;
	}


	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}



	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errors = super.validate(mapping, request);
		
		ControllerConnection controllerConnection = (ControllerConnection)
		request.getSession().getAttribute(Constants.CONNECTION);

		//check if the a department with this name already exists.
		List allDepartments;
		try {
			allDepartments = controllerConnection.getAllDepartments();

			for (int i = 0; i < allDepartments.size(); i++) {
				if (((Department)allDepartments.get(i)).getName().equals(this.name)) {
					ActionMessage error = new ActionMessage("errors.departmentAlreadyExists");	
					errors.add("errors.departmentAlreadyExists", error);
					
				}
			}
		} catch (NetworkException e) {
			e.printStackTrace();
		}		
	return errors; 
	}
}
