package web.java;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.NetworkException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import server.domain.Department;
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

public final class ManageDepartmentForm extends ActionForm{
	private List departments;
	private Long currentDepartmentID;
	private String dispatch; //tells the action-class which forward to take
	
	
	public ManageDepartmentForm() {
		
	}

	public List getDepartments() {
		return departments;
	}

	public void setDepartments(List departments) {
		this.departments = departments;
	}


	public String getDispatch() {
		return dispatch;
	}

	public void setDispatch(String dispatch) {
		this.dispatch = dispatch;
	}

	public Department getSelectedDepartment() {
		System.out.println("LOG: >" + dispatch + "<");
		for(int i=0; i < this.departments.size(); i++)
		{
			Department department = (Department)departments.get(i);
			if(department.getId().equals(currentDepartmentID))
			{
				return department;
			}
		}

		return null;
	}
	/**
	 * Get all Departments to display them in the corresponding JSP
	 *
	 */

	public void reset(ActionMapping mapping, HttpServletRequest request) {
		try {
			ControllerConnection controllerConnection = (ControllerConnection)
				request.getSession().getAttribute(Constants.CONNECTION);
			departments = controllerConnection.getAllDepartments();
		} catch (NetworkException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		super.reset(mapping, request);
	}

	public Long getCurrentDepartmentID() {
		return currentDepartmentID;
	}

	public void setCurrentDepartmentID(Long currentDepartmentID) {
		this.currentDepartmentID = currentDepartmentID;
	}


}
