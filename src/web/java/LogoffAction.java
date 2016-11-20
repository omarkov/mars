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

/*
 *Logoff
 */


public final class LogoffAction extends Action {


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

      // Extract attributes we will need
      HttpSession session = request.getSession();


      // Remove user login
      session.removeAttribute(Constants.USER_KEY);
	  request.getSession().removeAttribute("selectedDepartment");
	  request.getSession().removeAttribute("currentDepartmentID");
	  request.getSession().removeAttribute("selectedGroup");
	  request.getSession().removeAttribute("selectedUsers");
	  request.getSession().removeAttribute("currentGroup");
	  request.getSession().removeAttribute("fromManageProfiles");
	  request.getSession().removeAttribute("selectedProfile");
	  request.getSession().removeAttribute("currentUser");
	  request.getSession().removeAttribute("selectedUser");
	  request.getSession().removeAttribute(Constants.CONNECTION);
	  
	  
      // Return success
      return (mapping.findForward(Constants.SUCCESS));

    }

} // end LogoffAction
