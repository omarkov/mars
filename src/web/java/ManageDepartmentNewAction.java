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
import server.domain.User;
import web.java.controller.ControllerConnection;

/**
 * Executes the user input of a struts JSP-page. 
 *
 */
public final class ManageDepartmentNewAction extends Action {
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
            HttpServletResponse response)
			throws Exception {
		ManageDepartmentNewForm manageDepartmentNewForm = (ManageDepartmentNewForm) form;
		ControllerConnection controllerConnection = 
			(ControllerConnection)request.getSession().getAttribute(Constants.CONNECTION);
    	//the "save" button was pressed
		if (manageDepartmentNewForm.getDispatch().equals("save")) {
			Department newDepartment= new Department();
			List usersForNewDepartment = new ArrayList();
			Long[] selectedUsers = manageDepartmentNewForm.getSelectedUsers();
			if (selectedUsers != null) {
				for (int i = 0; i < selectedUsers.length; i++) {
					usersForNewDepartment.add(selectedUsers[i]);
				}
			}
			controllerConnection.createDepartmentWithUsers(manageDepartmentNewForm.getName(),
														   manageDepartmentNewForm.getAbbreviation(),
														   manageDepartmentNewForm.getComment(),
														   usersForNewDepartment);
			return (mapping.findForward("success"));
		}
		//the "reset" button was pressed
		if (manageDepartmentNewForm.getDispatch().equals("reset")) {
			return (mapping.findForward("reset"));
		}
		//the "back" button was pressed
		if (manageDepartmentNewForm.getDispatch().equals("back")) {
			return (mapping.findForward("back"));
		}
		
		return (mapping.findForward("nothingSelected"));

    }
}
