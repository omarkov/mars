package web.java;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
public final class ManageGroupNewAction extends Action {
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
		ManageGroupNewForm manageGroupNewForm = (ManageGroupNewForm) form;
		ControllerConnection controllerConnection = (ControllerConnection)request.getSession().getAttribute(Constants.CONNECTION);
    	
		//the "save" button was pressed
		if (manageGroupNewForm.getDispatch().equals("save")) {
			GroupMars newGroup = new GroupMars();
			newGroup.setComment(manageGroupNewForm.getComment()); 
			/*
			 * If the expirationdate is set to unlimited in the jsp. 
			 * getExpirationDate returns null. 
			 */
			if (manageGroupNewForm.getExpirationLimit().equalsIgnoreCase("limited")) {
				//The return value of January is 0. To parse it we have to increment it. 
				String expirationMonth = String.valueOf(Integer.valueOf(request.getParameter("month")) + 1);
				String expirationDay = request.getParameter("day");
				String expirationYear = request.getParameter("year");
				String expirationDate = expirationMonth + "/" + expirationDay + "/" + expirationYear;
				DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT,  Locale.ENGLISH); 
				newGroup.setExpirationDate(df.parse(expirationDate));
			} else {
				newGroup.setExpirationDate(null);
			}
			newGroup.setName(manageGroupNewForm.getName());
			List usersForNewGroup = new ArrayList();
			Long[] selectedUsers = manageGroupNewForm.getSelectedUsers();
			if (selectedUsers != null) {
				for (int i = 0; i < selectedUsers.length; i++) {
					usersForNewGroup.add(manageGroupNewForm.getSelectedUsers()[i]);
				}
			}
			controllerConnection.createGroup(newGroup, usersForNewGroup);
			return (mapping.findForward("success"));
		}
		//the "reset" button was pressed
		if (manageGroupNewForm.getDispatch().equals("reset")) {
			return (mapping.findForward("reset"));
		}
		//the "back" button was pressed
		if (manageGroupNewForm.getDispatch().equals("back")) {
			return (mapping.findForward("back"));
		}
		return (mapping.findForward("nothingSelected"));

    }
}
