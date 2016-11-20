package web.java;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import server.domain.BooleanType;
import server.domain.BooleanValue;
import server.domain.ComponentAttribute;

import server.domain.ComponentAttributeValue;
import server.domain.ComponentSetting;
import server.domain.User;
import server.domain.ListType;
import server.domain.ListValue;
import server.domain.NumericType;
import server.domain.NumericValue;
import server.domain.SmartRoomProfile;
import server.domain.StringType;
import server.domain.StringValue;
import web.java.controller.ControllerConnection;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
/**
 * Executes the user input of a struts JSP-page. 
 *
 */
public class ManageProfilesEditAction extends Action{
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
	
	@SuppressWarnings("unchecked")
	public ActionForward execute(ActionMapping mapping, 
								 ActionForm form, 
								 HttpServletRequest request, 
								 HttpServletResponse response) throws Exception {
		
		ManageProfilesEditForm manageProfilesEditForm = (ManageProfilesEditForm)form;
		//the "reset" button was pressed
		if (request.getParameter("submit").equalsIgnoreCase("reset")) {
			return (mapping.findForward("reset"));
		}
		
		//the "cancel" button was pressed
		if (manageProfilesEditForm.getDispatch().equalsIgnoreCase("back")) {
			return (mapping.findForward("back"));
		}
		//the "save" button was pressed. 
		if (manageProfilesEditForm.getDispatch().equalsIgnoreCase("save")) {
    		ControllerConnection controllerConnection = (ControllerConnection)
	    	    request.getSession().getAttribute(Constants.CONNECTION);

    		User currentUser;
            if (request.getSession().getAttribute("currentUser") != null) {
                currentUser = (User)request.getSession().getAttribute("currentUser");  
	        	System.out.println("currentUser: " + currentUser.getUserLoginID());
            } else {
                currentUser = (User)controllerConnection.getCurrentUser();
            }

	    	manageProfilesEditForm.fillProfile(request);

		SmartRoomProfile selectedProfile = (SmartRoomProfile)request.getSession().getAttribute("selectedProfile");

            boolean edit = "EDIT".equals(request.getSession().getAttribute("PROFILEACTION"));
           
            if(edit)
            {
		        selectedProfile.setComment(manageProfilesEditForm.getComment());
		        selectedProfile.setLastChange(new Date());
		        //selectedProfile.setName(manageProfilesEditForm.getName());

			    controllerConnection.updateProfile(selectedProfile, currentUser.getId());
            }else
            {
		        selectedProfile.setComment(manageProfilesEditForm.getComment());
		        selectedProfile.setName(manageProfilesEditForm.getName());
		        controllerConnection.createProfile(selectedProfile, currentUser.getId());
            }
	    		request.getSession().setAttribute("PROFILEERROR", null);

			return (mapping.findForward("save"));
		}
		
		return super.execute(mapping, form, request, response);
	}

}
