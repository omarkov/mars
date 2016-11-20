package web.java;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import server.domain.GroupMars;
import web.java.controller.ControllerConnection;
/**
 * Executes the user input of a struts JSP-page. 
 *
 */
public class ManageGroupAddUsersAction extends Action{
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
		ManageGroupAddUsersForm manageGroupAddUsersForm = (ManageGroupAddUsersForm) form;
		//the "back" button was pressed
		if (manageGroupAddUsersForm.getDispatch().equals("back")) {
			return (mapping.findForward("back"));
		}
		//did the user select anything?
		if (manageGroupAddUsersForm.getSelectedUsers() != null) {
			ArrayList l = new ArrayList();
			Long[] users = manageGroupAddUsersForm.getSelectedUsers();
			for(int i=0; i < users.length; i++)l.add(users[i]);
			ControllerConnection controllerConnection = (ControllerConnection)
				request.getSession().getAttribute(Constants.CONNECTION);
			controllerConnection.addUsersToGroup(((GroupMars)request
					.getSession().getAttribute("selectedGroup")).getId(), l);	
			return (mapping.findForward("success"));
		}
		return (mapping.findForward("nothingSelected"));
	}

}
