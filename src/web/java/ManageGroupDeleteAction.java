package web.java;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.NetworkException;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import web.java.controller.ControllerConnection;
import web.java.controller.NotAuthorizedException;
/**
 * Executes the user input of a struts JSP-page. 
 *
 */
public final class ManageGroupDeleteAction extends Action {
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
		
		ManageGroupDeleteForm manageGroupDeleteForm = (ManageGroupDeleteForm) form;
		if (manageGroupDeleteForm.getDispatch().equals("delete")) {
			ControllerConnection controllerConnection = (ControllerConnection)request.getSession().getAttribute(Constants.CONNECTION);
			controllerConnection.deleteGroup(manageGroupDeleteForm.getGroupIDToDelete());

		}
		if (manageGroupDeleteForm.getDispatch().equals("cancel")) {
			return (mapping.findForward("cancel"));
		}

		return (mapping.findForward("success"));
    }
}
