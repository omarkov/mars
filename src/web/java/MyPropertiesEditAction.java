package web.java;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import server.domain.User;
import web.java.controller.ControllerConnection;
import java.util.ArrayList;
import web.java.LoginForm;

/**
 * Executes the user input of a struts JSP-page. 
 *
 */
public final class MyPropertiesEditAction extends Action  {
    /**
     * Process the specified HTTP request, and create the corresponding HTTP
     * response (or forward to another web component that will create it).
     * Return an <code>ActionForward</code> instance describing where and how
     * control should be forwarded, or <code>null</code> if the response has
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
	
	public ActionForward execute(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response)
			throws Exception 
	{
		ControllerConnection controllerConnection = (ControllerConnection)request.getSession().getAttribute(Constants.CONNECTION);
		MyPropertiesEditForm myPropertiesEditForm = (MyPropertiesEditForm)form;
		
		if (myPropertiesEditForm.getDispatch().equals("changepwd")) {
			
		    // uTU = userToUpdate
			User uTU = (User)controllerConnection.getCurrentUser();
            uTU.setPassword(myPropertiesEditForm.getNewpassword());

            controllerConnection.updateUser(uTU);
//			controllerConnection.updateUser(uTU.getId(),new ArrayList(uTU.getIdentifications()),uTU.getUserLoginID(),myPropertiesEditForm.getNewpassword(),uTU.getFirstName(),uTU.getLastName(), uTU.getEmail(), uTU.getComment(), uTU.isAdministrator(), uTU.getExpirationDate(), uTU.getQuota());
			
			LoginForm loginForm = (LoginForm) request.getSession().getAttribute(Constants.USER_KEY);
			loginForm.setPassword(myPropertiesEditForm.getNewpassword());
			
			request.getSession().setAttribute(Constants.USER_KEY, loginForm);

			return(mapping.findForward("success"));
		}
		
		
		return (mapping.findForward("failure"));
	}
}
