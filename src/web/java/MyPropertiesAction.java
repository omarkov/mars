package web.java;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import web.java.LoginForm;
import web.java.Constants;
import web.java.controller.ControllerConnection;
import server.domain.User;
/**
 * Executes the user input of a struts JSP-page. 
 *
 */
public final class MyPropertiesAction extends Action  {
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
	
	public ActionForward execute(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response)
			throws Exception {
		
    MyPropertiesForm myPropertiesForm = (MyPropertiesForm) form;
    ControllerConnection controllerConnection =(ControllerConnection)
    request.getSession().getAttribute(Constants.CONNECTION);
	//The "change password" button was pressed. 
    if (myPropertiesForm.getDispatch().equals("changePWD")) {	
		
    	return (mapping.findForward("changePassword"));
    }
    else if(myPropertiesForm.getDispatch().equals("changeProperties")){
        User userToUpdate = (User)controllerConnection.getCurrentUser();
        userToUpdate.setEmail(myPropertiesForm.getEmail());
		userToUpdate.setFirstName(myPropertiesForm.getFirstname());
		userToUpdate.setLastName(myPropertiesForm.getLastname());
		
		boolean superDuperRepeat = true;
		if (myPropertiesForm.getSuperDuperRepeat() == null) {
			superDuperRepeat = false;
		}
		
		if (myPropertiesForm.getSuperDuperRepeat().equalsIgnoreCase("on")) {
			superDuperRepeat = true;
		}
		if (myPropertiesForm.getSuperDuperRepeat().equalsIgnoreCase("off")) {
			superDuperRepeat = false;
		}
		
		userToUpdate.setSuperDuperRepeat(superDuperRepeat);
		
        controllerConnection.updateUser(userToUpdate);
        
	return (mapping.findForward("success"));
	}
    return (mapping.findForward("success"));
    }
}
