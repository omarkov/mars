package web.java;

import java.text.DateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.*;

import server.domain.*;
import web.java.controller.ControllerConnection;
/**
 * Executes the user input of a struts JSP-page. 
 *
 */
public final class ManageDepartmentEditAction extends Action {
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

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ManageDepartmentEditForm manageDepartmentEditForm = (ManageDepartmentEditForm) form;
	ControllerConnection controllerConnection = (ControllerConnection)request.getSession().getAttribute(Constants.CONNECTION);
    	Department selectedDepartment = (Department)request.getSession().getAttribute("selectedDepartment");
        
        //the "save" button was pressed
        if (manageDepartmentEditForm.getDispatch().equals("save")) {
            //Save the changed department.
            /*selectedDepartment.setId(manageDepartmentEditForm.getDepartmentID());
            selectedDepartment.setName(manageDepartmentEditForm.getName());
            selectedDepartment.setComment(manageDepartmentEditForm.getComment());
            selectedDepartment.setAbbreviation(manageDepartmentEditForm.getAbbreviation());
            controllerConnection.updateDepartment(selectedDepartment.getId(), selectedDepartment.getName(),selectedDepartment.getAbbreviation(),selectedDepartment.getName());*/
            controllerConnection.updateDepartment(manageDepartmentEditForm.getDepartmentID(),
                                                  manageDepartmentEditForm.getName(),
                                                  manageDepartmentEditForm.getAbbreviation(),
                                                  manageDepartmentEditForm.getComment()
                                                  );         
            return (mapping.findForward("success"));
        }
        //the "reset" button was pressed
        if (manageDepartmentEditForm.getDispatch().equals("reset")) {
            return (mapping.findForward("reset"));
        }
        //the "delete" button was pressed
        if (manageDepartmentEditForm.getDispatch().equals("delete")) {
            //get the users to delete from the actionForm
            if (manageDepartmentEditForm.getSelectedUsers() != null) {
                for (int i = 0; i < manageDepartmentEditForm.getSelectedUsers().length; i++) {
                        controllerConnection.removeUserFromDepartment(
                                        ((Department)request.getSession().getAttribute("selectedDepartment")).getId(),
                                        manageDepartmentEditForm.getSelectedUsers()[i]);				
                }
            }
            /*This is nessecary to show the changes on manageDeparmtentEdit.jsp
             *immediately
             */
            manageDepartmentEditForm.setUsers(controllerConnection
                            .getUsersForDepartment(((Department)request.getSession()
                            .getAttribute("selectedDepartment")).getId()));
            return (mapping.findForward("reset"));
        }
        //the "add" button was pressed
        if (manageDepartmentEditForm.getDispatch().equals("add")) {
            request.setAttribute("currentDepartmentID", manageDepartmentEditForm.getDepartmentID());
            return (mapping.findForward("addUsers"));
        }
        //the "back" button was pressed
        if (manageDepartmentEditForm.getDispatch().equals("back")) {
            return (mapping.findForward("back"));
        }
        
        return (mapping.findForward("nothingSelected"));
    }
}
