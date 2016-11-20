package web.java;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import web.java.controller.ControllerConnection;
import server.domain.User;

import net.*;

/**
 * Executes the user input of a struts JSP-page. 
 *
 */
public final class ManageProfilesShowAction extends Action{
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
		

		ManageProfilesShowForm manageProfilesShowForm = (ManageProfilesShowForm) form;
        
		//The "use" button was pressed
		if (request.getParameter("submit").equalsIgnoreCase("use")) {
			request.getSession().setAttribute("selectedProfile", manageProfilesShowForm.getSelectedProfile());
			manageProfilesShowForm.setProfile(manageProfilesShowForm.getSelectedProfile());
    		ControllerConnection controllerConnection = (ControllerConnection)request.getSession().getAttribute(Constants.CONNECTION);
	    	try {
		    	controllerConnection.setProfile(manageProfilesShowForm.getSelectedProfile());
		    } catch (NetworkException e) {
			    // TODO Auto-generated catch block
			    e.printStackTrace();
		    }

			return (mapping.findForward("use")); 
		}
                
		//The "edit" button was pressed
		if (request.getParameter("submit").equalsIgnoreCase("edit")) {
			request.getSession().setAttribute("selectedProfile", manageProfilesShowForm.getSelectedProfile());
            request.getSession().setAttribute("PROFILEACTION", "EDIT");
			return (mapping.findForward("edit")); 
		}
		//The "delete" button was pressed
		if (request.getParameter("submit").equalsIgnoreCase("delete")) {
			request.getSession().setAttribute("selectedProfile", manageProfilesShowForm.getSelectedProfile());
			return (mapping.findForward("delete"));
		}
		
		/*The "export" button was pressed
		if (request.getParameter("submit").equalsIgnoreCase("export")) {
			ControllerConnection controllerConnection = (ControllerConnection) request.getSession().getAttribute(Constants.CONNECTION);
			// exportProfile(UserID,ProfileID) 
			LoginForm loginForm = (LoginForm)request.getSession().getAttribute(Constants.USER_KEY);
			// UNCOMMENT
			// TODO FOLDER SELECTION FOR EXPORT 
			//controllerConnection.exportProfile(loginForm.getUsername(),manageProfilesShowForm.getSelectedProfile());
			return (mapping.findForward("export"));
		}
		
		//The "import" button was pressed
		if (request.getParameter("submit").equalsIgnoreCase("import")) {
			ControllerConnection controllerConnection = (ControllerConnection) request.getSession().getAttribute(Constants.CONNECTION);
			// exportProfile(UserID,ProfileID) 
			LoginForm loginForm = (LoginForm)request.getSession().getAttribute(Constants.USER_KEY);
			// UNCOMMENT
			// TODO FOLDER SELECTION FOR IMPORT
			//controllerConnection.exportProfile(loginForm.getUsername(),manageProfilesShowForm.getSelectedProfile());
			return (mapping.findForward("import"));
		}*/
		
		//The "setAsDefaultProfile" button was pressed
		if (request.getParameter("submit").equalsIgnoreCase("setAsDefault")) {

			//Update the User	
			ControllerConnection controllerConnection = (ControllerConnection) request.getSession().getAttribute(Constants.CONNECTION);
			User user = manageProfilesShowForm.getSelectedUser();
			
			// Update user attribut defaultProfile
			user.setDefaultProfile(manageProfilesShowForm.getSelectedProfile());
			controllerConnection.updateUser(user);
			
			// Update the form 
			manageProfilesShowForm.setDefaultProfile(manageProfilesShowForm.getSelectedProfile().getName());
			return (mapping.findForward("setAsDefault"));
		}
		
		//Must be the last otherwise exception will occure
		//The "new" button was pressed
		if (manageProfilesShowForm.getDispatch().equalsIgnoreCase("new")) {
			request.getSession().setAttribute("currentUser", manageProfilesShowForm.getSelectedUser());
            request.getSession().setAttribute("PROFILEACTION", "NEW");
			/*
			 * "selectedUser has to be removed because in manageProfilesShow.jsp
			 *  is a check if "selectedUser" is null or not. 
			 */
			// request.getSession().removeAttribute("selectedUser");
			return (mapping.findForward("edit"));
		}

		return (mapping.findForward("success"));
    }

}
