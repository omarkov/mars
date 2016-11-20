package web.java;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.*;
import web.java.LoginForm;
import net.NetworkException;
import web.java.controller.ControllerConnection;
import web.java.controller.UnknownLoginException;
import web.java.controller.ChangePasswordException;
import web.java.controller.NotAuthorizedException;
import web.java.*;
import server.domain.*;
/**
 * Login
 */
public final class LoginAction extends Action {


    /*
     *Controller login only returns true at the moment
     */
    public boolean isUserLogin(HttpSession session, String username, String password) throws Exception
    {

        
        try {
            ControllerConnection controllerConnection = new ControllerConnection(username, password);
            session.setAttribute(Constants.CONNECTION, controllerConnection); 
        }
        catch (Exception e) {
            e.printStackTrace();
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
        LoginForm loginform = (LoginForm) form;
        String username = loginform.getUsername();
        String password = loginform.getPassword();
		
	
       if(!isUserLogin(session, username, password))
	{   
            ActionMessages errors = new ActionMessages();
            ActionMessage error = new ActionMessage("errors.loginError");
            errors.add("errors.loginError", error);
            saveErrors(request, errors);    
           
            return mapping.getInputForward();
            
        }

        // Save our logged-in user in the session,
        // with key
        session.setAttribute(Constants.USER_KEY, form);
        // Return success
        return (mapping.findForward(Constants.SUCCESS));

    }

} // End LogonAction
