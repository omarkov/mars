package web.java;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

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
public final class ManageGroupEditAction extends Action {
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
		ManageGroupEditForm manageGroupEditForm = (ManageGroupEditForm) form;
		ControllerConnection controllerConnection = (ControllerConnection)request
			.getSession().getAttribute(Constants.CONNECTION);

		GroupMars updatedGroup = (GroupMars)request.getSession().getAttribute("selectedGroup");
    	
		//the "save" button was pressed
		if (manageGroupEditForm.getDispatch().equals("save")) {
			
			//get the new data from the action form
			updatedGroup.setComment(manageGroupEditForm.getComment());
			if (manageGroupEditForm.getExpirationLimit().equalsIgnoreCase("limited")) {
				//The return value of January is 0. To parse it we have to increment it. 
				String expirationMonth = String.valueOf(Integer.valueOf(request.getParameter("month")) + 1);
				String expirationDay = request.getParameter("day");
				String expirationYear = request.getParameter("year");
				String expirationDate = expirationMonth + "/" + expirationDay + "/" + expirationYear;
				DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT,  Locale.ENGLISH); 
				updatedGroup.setExpirationDate(df.parse(expirationDate));
			} else {
				updatedGroup.setExpirationDate(null);
			}
			updatedGroup.setName(manageGroupEditForm.getName());
			controllerConnection.updateGroup(updatedGroup);
			return (mapping.findForward("success"));
		}
		//the "reset" button was pressed
		if (manageGroupEditForm.getDispatch().equals("reset")) {
			return (mapping.findForward("reset"));
		}
		//the "delete" button was pressed
		if (manageGroupEditForm.getDispatch().equals("delete")) {
			if (manageGroupEditForm.getSelectedUsers() != null) {
				for (int i = 0; i < manageGroupEditForm.getSelectedUsers().length; i++) {
					controllerConnection.removeUserFromGroup(
							((GroupMars)request.getSession().getAttribute("selectedGroup")).getId(),
							manageGroupEditForm.getSelectedUsers()[i]);				
				}
			}
			/*This is nessecary to show the changes on manageGroupEdit.jsp
			 *immediately
			 */

			manageGroupEditForm.setUsers(controllerConnection
					.getUsersForGroup(((GroupMars)request.getSession()
							.getAttribute("selectedGroup")).getId()));
			return (mapping.findForward("reset"));
		}
		
		//the "Add users to group" button was pressed
		if (manageGroupEditForm.getDispatch().equals("add")) {
			request.setAttribute("currentGroup", updatedGroup.getId());
		return (mapping.findForward("addUsers"));
		}
		
		//the "back" button was pressed
		if (manageGroupEditForm.getDispatch().equals("back")) {
			return (mapping.findForward("back"));
		}
		return (mapping.findForward("nothingSelected"));

    }
}
