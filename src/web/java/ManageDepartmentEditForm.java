package web.java;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import net.NetworkException;

import org.apache.struts.action.*;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.validator.ValidatorForm;

import server.domain.Department;
import server.domain.GroupMars;
import web.java.controller.ControllerConnection;
import web.java.controller.GroupException;
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

public class ManageDepartmentEditForm extends ValidatorForm{

	private String action = null;
	private String name = null;
	private String comment = null;
	private String dispatch = null;
	private String abbreviation = null;
    private ActionMessages errors = null;
	private Long departmentID = null;


    private Long[] selectedUsers = null; 
	private List users = null;


	public  ManageDepartmentEditForm() {
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
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * Set the members of the department to display in the JSP page. 
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request){
		if (request.getSession().getAttribute("selectedDepartment") != null) {
			// the selected department is saved in the preceding action bean.
			Department selectedDepartment = (Department)request.getSession()
				.getAttribute("selectedDepartment");
			try {
				ControllerConnection controllerConnection = 
					(ControllerConnection)request.getSession().getAttribute(Constants.CONNECTION);
				users = controllerConnection.getUsersForDepartment(selectedDepartment.getId());

			} catch (NetworkException e) {
				//TODO Exception handling			
				e.printStackTrace();
			} 
			comment = selectedDepartment.getComment();
			abbreviation = selectedDepartment.getAbbreviation();
			name = selectedDepartment.getName();
			departmentID = selectedDepartment.getId();
		}
		super.reset(mapping, request);
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List getUsers() {
		return users;
	}
	public void setUsers(List users) {
		this.users = users;
	}
	public String getDispatch() {
		return dispatch;
	}
	public void setDispatch(String dispatch) {
		String test = null;
		this.dispatch = dispatch;
	}
	public ActionMessages getErrors() {
		return errors;
	}
	public void setErrors(ActionMessages errors) {
		this.errors = errors;
	}
	public String getAbbreviation() {
		return abbreviation;
	}
	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	public Long getDepartmentID() {
		return departmentID;
	}

	public void setDepartmentID(Long departmentID) {
		this.departmentID = departmentID;
	}

	public String getAction() {
		return action;
	}
	
}
