package web.java;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
/**
 * Executes the user input of a struts JSP-page. 
 *
 */

public class ManageUserAction extends Action{
	
    /**
     * Process the specified HTTP request, and create the corresponding HTTP
     * response (or forward to another web component that will create it).
     * Return an <code>ActionForward</code> instance describing where and how
     * control should be forwarded, if the response has
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
		ManageUserForm manageUserForm = (ManageUserForm) form;
		
		//The "edit" button was pressed
		if (request.getParameter("submit").equalsIgnoreCase("edit")) {
			request.getSession().setAttribute("selectedUser", manageUserForm.getSelectedUser());
			return (mapping.findForward("edit")); 
		}
		
		//The "delete" button was pressed
		if (request.getParameter("submit").equalsIgnoreCase("delete")) {
			request.getSession().setAttribute("selectedUser", manageUserForm.getSelectedUser());
			return (mapping.findForward("delete"));
		}
		
		//The "Show Profiles" button was pressed. 
		if (request.getParameter("submit").equalsIgnoreCase("showProfiles")) {
			request.getSession().setAttribute("selectedUser", manageUserForm.getSelectedUser());
			return (mapping.findForward("showProfiles"));
		}
		//The "new" button was pressed. 
		if (manageUserForm.getDispatch().equals("new")) {
			return (mapping.findForward("new"));
		}
		
		//The "deletions" button was pressed. 
		if (manageUserForm.getDispatch().equals("deletions")) {
			return (mapping.findForward("deletions"));
		}
		return (mapping.findForward("success"));
    }

}
