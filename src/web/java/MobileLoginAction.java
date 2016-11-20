package web.java;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.NetworkException;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import web.java.controller.ControllerConnection;
/**
 * Login
 */
public final class MobileLoginAction extends Action {


    /*
     *Controller login only returns true at the moment
     */
    public boolean isUserLogin(HttpSession session, String username, String password) throws NetworkException
    {

        try {
	    ControllerConnection controllerConnection = new ControllerConnection(username, password);
            session.setAttribute(Constants.CONNECTION, controllerConnection); 
    	    controllerConnection.enterRoom("PDA", username);
        }
        catch (NetworkException e) {
            return false;
        }
        return true;
   }   


    /**
     * Login the user.
     *
     */
    public ActionForward execute(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response)
        throws Exception {

        HttpSession session = request.getSession();

        // Obtain username and password
        MobileLoginForm loginform = (MobileLoginForm) form;
        String username = loginform.getUsername();
        String password = loginform.getPassword();
        
        
        if(!isUserLogin(session, username, password))
	{
            // couldn't connect
            ActionMessages errors = new ActionMessages();
            ActionMessage error = new ActionMessage("errors.loginError");
            errors.add("errors.loginError", error);
            saveErrors(request, errors);    
			
            // return to input page
            return mapping.getInputForward();
        }

        // Save our logged-in user in the session,
        // with key
        session.setAttribute(Constants.USER_KEY, form);
        // Return success
		System.out.println("mobileLoginAction");
        return (mapping.findForward(Constants.SUCCESS));

    }


	@Override
	protected void saveErrors(HttpServletRequest arg0, ActionMessages arg1) {
		// TODO Auto-generated method stub
		super.saveErrors(arg0, arg1);
	}

} // End LogonAction
