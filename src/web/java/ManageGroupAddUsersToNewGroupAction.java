package web.java;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ManageGroupAddUsersToNewGroupAction extends Action{

	public ActionForward execute(
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request, 
			HttpServletResponse response) throws Exception {
		ManageGroupAddUsersToNewGroupForm manageGroupAddUsersToNewGroupForm = (ManageGroupAddUsersToNewGroupForm) form;
		
		if (request.getParameter("submit").equalsIgnoreCase("Cancel and back")) {
			return (mapping.findForward("back"));
		}

		if (manageGroupAddUsersToNewGroupForm.getSelectedUsers() != null) {
			request.getSession().setAttribute("selectedUsers", manageGroupAddUsersToNewGroupForm.getSelectedUsers());
			return (mapping.findForward("success"));
		}
		return (mapping.findForward("nothingSelected"));
	}

}
