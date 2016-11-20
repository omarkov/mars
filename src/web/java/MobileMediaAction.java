package web.java;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.*;

import net.*;
import web.java.controller.*;


/*
 *NOT YET IMPLEMENTED !!!
 */
public class MobileMediaAction extends Action{
    public void checkAndCall(NetworkFactory net, String submission, String command) throws NetworkException
    {
	    if (submission.equalsIgnoreCase(command)) {
	        net.call("Mediacenter", command, new Object[]{new Integer(1)});
	    }
    }

    public ActionForward execute(
				 ActionMapping mapping,
				 ActionForm form,
				 HttpServletRequest request,
				 HttpServletResponse response)
	throws Exception
    {
		ControllerConnection controllerConnection = (ControllerConnection)
        	request.getSession().getAttribute(Constants.CONNECTION);
	    NetworkFactory net = controllerConnection.getNetworkFactory();
	
    	MobileMediaForm mobileMediaForm = (MobileMediaForm) form;
    	String submission = request.getParameter("dispatch");

        System.out.println(submission);
    
    	try {
    	    checkAndCall(net, submission, "remoteUp");
    	    checkAndCall(net, submission, "remoteDown");
    	    checkAndCall(net, submission, "remoteLeft");
    	    checkAndCall(net, submission, "remoteRight");
    	    checkAndCall(net, submission, "volumeUp");
    	    checkAndCall(net, submission, "volumeDown");
    	    checkAndCall(net, submission, "remoteOK");
    	    checkAndCall(net, submission, "remoteESC");
    	} catch (NetworkException ex) {
    	    ex.printStackTrace();
    	    throw new Exception("Shit happened");
    	}
    
    	return mapping.findForward("success");
        }
}
