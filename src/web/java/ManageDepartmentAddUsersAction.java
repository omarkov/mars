package web.java;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import server.domain.Department;
import web.java.controller.ControllerConnection;
/**
 * Executes the user input of a struts JSP-page. 
 */
public class ManageDepartmentAddUsersAction extends Action{
    /**
     * Process the specified HTTP request, and create the corresponding HTTP
     * response (or forward to another web component that will create it).
     * Return an <code>ActionForward</code> instance describing where and how
     * control should be forwarded, or if the response has
     * already been completed.
     *
     * @param mapping The ActionMapping used to select this instance
     * @param form The optional ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     *
     * @return Action to forward to
     * @exception Exception if an input/output error or servlet exception occurs
     */

	public ActionForward execute(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request, 
			HttpServletResponse response) throws Exception {
		ManageDepartmentAddUsersForm manageDepartmentAddUsersForm = (ManageDepartmentAddUsersForm) form;
		
		if (manageDepartmentAddUsersForm.getDispatch().equals("back")) {
			return (mapping.findForward("back"));
		}
		
		//the users are beeing selected from the jsp page and stored in selectedUsers
		if (manageDepartmentAddUsersForm.getSelectedUsers() != null) {
			//get the ControllerConnection-object that is saved in the session. 
			ControllerConnection controllerConnection = (ControllerConnection)
				request.getSession().getAttribute(Constants.CONNECTION);
			
			List selectedUsersList = new ArrayList();
			Long[] selectedUsers = manageDepartmentAddUsersForm.getSelectedUsers();
				for (int i = 0; i < selectedUsers.length; i++) {
					selectedUsersList.add(selectedUsers[i]);
				}
				controllerConnection.addUsersToDepartment(
					((Department)request.getSession().getAttribute("selectedDepartment")).getId(),
					selectedUsersList);	
			return (mapping.findForward("success"));
		}
		//if no user was selected to add to the department
		return (mapping.findForward("nothingSelected"));
	}

}
