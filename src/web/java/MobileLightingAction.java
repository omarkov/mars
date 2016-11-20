package web.java;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.*;

import net.*;
import web.java.controller.*;


/*
 *NOT YET IMPLEMENTED !!!
 */
public class MobileLightingAction extends Action{
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

    	MobileLightingForm mobileLightingForm = (MobileLightingForm) form;

        String dispatch = mobileLightingForm.getDispatch();

        if(dispatch.startsWith("MOOD"))
        {
            String mood = dispatch.replaceAll("MOOD", ""); 
            net.set("LIGHT", "Red", new Integer(0));
            net.set("LIGHT", "Blue", new Integer(0));
            net.set("LIGHT", "Green", new Integer(0));
            net.set("LIGHT", "Mood", Integer.parseInt(mood));
            mobileLightingForm.setMood(mood);
       }
	else if(dispatch.startsWith("RESET"))
	 
	try{
            net.set("LIGHT", "Mood", new Integer(0));
            net.set("LIGHT", "Red", new Integer(50));
            net.set("LIGHT", "Blue", new Integer(50));
            net.set("LIGHT", "Green", new Integer(50));
            mobileLightingForm.setMood("0");
    	    } catch (NetworkException ex) {
    	        ex.printStackTrace();
    	        throw new Exception("Shit happened");
    	    }
	else
        {
            try{
                net.set("LIGHT", "Mood", new Integer(0));
                net.set("LIGHT", "Red", Integer.parseInt(mobileLightingForm.getRed()));
                net.set("LIGHT", "Blue", Integer.parseInt(mobileLightingForm.getBlue()));
                net.set("LIGHT", "Green", Integer.parseInt(mobileLightingForm.getGreen()));
                mobileLightingForm.setMood("0");
    	    } catch (NetworkException ex) {
    	        ex.printStackTrace();
    	        throw new Exception("Shit happened");
    	    }
        } 
    	return mapping.findForward("success");
        }
}
