package web.java;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import web.java.controller.ControllerConnection;
import server.domain.SmartRoomProfile;


public class ManageProfilesDeleteAction extends Action{
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
   public ActionForward execute(
	        ActionMapping mapping,
	        ActionForm form,
	        HttpServletRequest request,
	        HttpServletResponse response)
	        throws Exception {
		
		ManageProfilesDeleteForm manageProfilesDeleteForm = (ManageProfilesDeleteForm) form;		
		
		
		if (manageProfilesDeleteForm.getDispatch().equalsIgnoreCase("delete")) 
		{
			ControllerConnection controllerConnection = (ControllerConnection) request.getSession().getAttribute(Constants.CONNECTION);
			controllerConnection.deleteSmartRoomProfile(manageProfilesDeleteForm.getProfilesIDToDelete());
			controllerConnection.refreshUser();
			return (mapping.findForward("delete"));
		}
		return (mapping.findForward("cancel"));
    }

}
