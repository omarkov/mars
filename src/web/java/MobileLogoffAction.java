package web.java;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Vector;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.util.MessageResources;
import web.java.*;
import web.java.controller.ControllerConnection;


/*
 *Logoff
 */


public final class MobileLogoffAction extends Action {


// ---------------------------------------------------- Public Methods

    /**
     * Logoff the user.
     *
     */
    public ActionForward execute(ActionMapping mapping,
         ActionForm form,
         HttpServletRequest request,
         HttpServletResponse response)
            throws Exception {
	  ControllerConnection controllerConnection = 
			(ControllerConnection)request.getSession()
				.getAttribute(Constants.CONNECTION);

      // Extract attributes we will need
      HttpSession session = request.getSession();
	  
      if(controllerConnection != null)
      {
	     controllerConnection.exitRoom("PDA", controllerConnection.getCurrentUser().getUserLoginID());
      }

      // Remove user login
      session.removeAttribute(Constants.USER_KEY);
	
      // Return success
      return (mapping.findForward(Constants.SUCCESS));

    }

} // end LogoffAction
