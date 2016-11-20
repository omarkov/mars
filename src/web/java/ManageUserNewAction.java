package web.java;

import java.util.Date;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import server.domain.Identification;
import server.domain.LogInSystem;
import server.domain.User;
import web.java.controller.ControllerConnection;
/**
 * Executes the user input of a struts JSP-page. 
 *
 */
public class ManageUserNewAction extends Action {

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
		ManageUserNewForm manageUserNewForm = (ManageUserNewForm) form;
		System.out.println("expirationDay: " + manageUserNewForm.getExpirationDay());
		System.out.println("expirationMonth: " + manageUserNewForm.getExpirationMonth());

		//The "save" button was pressed.
		if (manageUserNewForm.getDispatch().equals("save")) {
			Date expirationDate;
			if (manageUserNewForm.getExpirationLimit().equalsIgnoreCase("limited")) {
				//The return value of January is 0. To parse it we have to increment it. 
				String expirationMonth = String.valueOf(Integer.valueOf(request.getParameter("month")) + 1);
				String expirationDay = request.getParameter("day");
				String expirationYear = request.getParameter("year");
				DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT,  Locale.ENGLISH); 
				expirationDate = (df.parse(expirationMonth + "/" + expirationDay + "/" + expirationYear));
			} else {
				expirationDate = null;
			}

			
			ControllerConnection controllerConnection = (ControllerConnection)
				request.getSession().getAttribute(Constants.CONNECTION);
			
			List tagIDList = new LinkedList();
			List loginSystemList = new LinkedList();

			List users = controllerConnection.getAllUsers();
			// check if "OPS" is drin
			List systems = controllerConnection.getAllLogInSystems();
			String tagID = null;
			
			for (int i = 0; i < systems.size(); i++) {
				LogInSystem system = (LogInSystem) systems.get(i);

				if (system.getName().equals("PDA")) {
					tagID = manageUserNewForm.getUserLoginId();
					tagIDList.add(tagID);
					loginSystemList.add(system.getId());
					System.out.println(system.getName() + '|' + tagID);	
				} else {
					tagID = request.getParameter(system.getName());
					for (int j = 0; j < users.size(); j++) {
						User user = (User)users.get(j);
						HashSet identifications = (HashSet)user.getIdentifications();
						for (Iterator iterator = identifications.iterator(); iterator.hasNext(); ){
							Identification identification = (Identification)iterator.next();
							if (identification.getTagID().equalsIgnoreCase(((String) tagID).trim())) {
								ActionMessages errors = new ActionMessages();
								ActionMessage error = new ActionMessage(
										"errors.tagIDAlreadyExists");
								errors.add("errors.tagIDAlreadyExists", error);
								saveErrors(request, errors);
								return mapping.getInputForward();
							}
						}
					}
                    if(tagID != null && tagID.length() > 0)
                    {
					    tagIDList.add(tagID);
					    loginSystemList.add(system.getId());
					    System.out.println(system.getName() + '|' + tagID);
                    }
				}
			}

			
			Long selectedDepartmentID = null;
			
			//No department has the ID = 0. So this is the ID if 
			//no department was selected by the user. 
			if (manageUserNewForm.getSelectedDepartmentID() != 0) {
				selectedDepartmentID = manageUserNewForm.getSelectedDepartmentID();
			}
			
			controllerConnection.createUserWithIdentification(tagIDList,
						     loginSystemList,
						     manageUserNewForm.getUserLoginId(),
						     manageUserNewForm.getPassword(),
						     manageUserNewForm.getFirstName(),
						     manageUserNewForm.getLastName(),
						     manageUserNewForm.getEmail(),
						     manageUserNewForm.getComment(),
						     new Boolean(manageUserNewForm.getIsAdmin()),
						     Boolean.FALSE, // TODO:
						     expirationDate,
						     manageUserNewForm.getSelectedDepartmentID(), // department
						     new LinkedList(), // groups
						     Integer.valueOf(manageUserNewForm.getQuota()));
			//controllerConnection.createUser(newUser);
	        return mapping.findForward("success");
		}
		
		//The "reset" button was pressed
		if (manageUserNewForm.getDispatch().equals("reset")) {
			return (mapping.findForward("reset"));
		}
		
		//The "back" button was pressed. 
		if (manageUserNewForm.getDispatch().equals("back")) {
			return (mapping.findForward("back"));
		}
		
		return (mapping.findForward("success"));
    }

    /**
     * Convenience method for removing the obsolete form bean.
     *
     * @param mapping The ActionMapping used to select this instance
     * @param request The HTTP request we are processing
     */
    protected void removeFormBean(
        ActionMapping mapping,
        HttpServletRequest request) {
            
        // Remove the obsolete form bean
        if (mapping.getAttribute() != null) {
            if ("request".equals(mapping.getScope())) {
                request.removeAttribute(mapping.getAttribute());
            } else {
                HttpSession session = request.getSession();
                session.removeAttribute(mapping.getAttribute());
            }
        }
    }
	

	
}

