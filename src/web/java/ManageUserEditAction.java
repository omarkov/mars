package web.java;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.text.DateFormat;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import server.domain.Department;
import server.domain.Identification;
import server.domain.LogInSystem;
import server.domain.User;
import web.java.controller.ControllerConnection;
/**
 * Executes the user input of a struts JSP-page. 
 *
 */
public class ManageUserEditAction extends Action{
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
	
    public ActionForward execute(
	        ActionMapping mapping,
	        ActionForm form,
	        HttpServletRequest request,
	        HttpServletResponse response)
	        throws Exception {
		ManageUserEditForm manageUserEditForm = (ManageUserEditForm) form;
		
		//The "save" button was pressed. 
		if (manageUserEditForm.getDispatch().equals("save")) {
				ControllerConnection controllerConnection = 
					(ControllerConnection)request.getSession()
					.getAttribute(Constants.CONNECTION);
				
				User updatedUser = (User)request.getSession().getAttribute("selectedUser");
				
				//Collect the changed user data. 
				//
/*				if(manageUserEditForm.getExpirationDate() != null)
				{
					DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT,  Locale.ENGLISH); 
					updatedUser.setExpirationDate(df.parse(manageUserEditForm.getExpirationDate()));
				}else
				{
					updatedUser.setExpirationDate(null);
				}*/
				
				if (manageUserEditForm.getExpirationLimit().equalsIgnoreCase("limited")) {
					//The return value of January is 0. To parse it we have to increment it. 
					String expirationMonth = String.valueOf(Integer.valueOf(request.getParameter("month")) + 1);
					String expirationDay = request.getParameter("day");
					String expirationYear = request.getParameter("year");
					String expirationDate = expirationMonth + "/" + expirationDay + "/" + expirationYear;
					DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT,  Locale.ENGLISH); 
					updatedUser.setExpirationDate(df.parse(expirationDate));
				} else {
					updatedUser.setExpirationDate(null);
				}
				updatedUser.setAdministrator(manageUserEditForm.getIsAdmin());
				updatedUser.setComment(manageUserEditForm.getComment());
				updatedUser.setEmail(manageUserEditForm.getEmail());
				updatedUser.setFirstName(manageUserEditForm.getFirstName());
				updatedUser.setLastName(manageUserEditForm.getLastName());
				updatedUser.setPassword(manageUserEditForm.getPassword());
				updatedUser.setQuota(Integer.parseInt(manageUserEditForm.getQuota()));
				updatedUser.setUserLoginID(manageUserEditForm.getUserLoginId());
				//updatedUser.setDepartment((Department)controllerConnection.getDepartment
				//		(manageUserEditForm.getSelectedDepartmentID()));
				
				//avoid nullPointerException if the user has no department
				if (updatedUser.getDepartment() != null){
					if (manageUserEditForm.getSelectedDepartmentID() 
						  != updatedUser.getDepartment().getId()) {
				    
						//No department has the ID: 0. => this ID is used if 
						//no department was selected by the user.
						if (manageUserEditForm.getSelectedDepartmentID() == 0) {
							controllerConnection.removeUserFromDepartment(
									updatedUser.getDepartment().getId(), updatedUser.getId());
						} else {
						controllerConnection.addUserToDepartment
						    (manageUserEditForm.getSelectedDepartmentID(), updatedUser.getId());
						}
					} 
				} else {
					if (manageUserEditForm.getSelectedDepartmentID() != 0) {
						controllerConnection.addUserToDepartment
					    (manageUserEditForm.getSelectedDepartmentID(), updatedUser.getId());						
					}
				}
/*				List logInSystems = controllerConnection.getAllLogInSystems();
				LogInSystem logInSystem;
				for (int i = 0; i < logInSystems.size(); i++) {
					Identification identification = new Identification();
					logInSystem = (LogInSystem)logInSystems.get(i);
					identification.setLogInSystem(logInSystem);
					identification.setUser(updatedUser);
					updatedUser.getIdentifications().add(identification);
				}
*/
				List tagIDList = new LinkedList();
				List identList = new ArrayList();
				List users = controllerConnection.getAllUsers();
                List<Identification> identsToDelete = new ArrayList();

				// check if "OPS" is drin
				List systems = controllerConnection.getAllLogInSystems();
				for (int i = 0; i < systems.size(); i++) {
					LogInSystem system = (LogInSystem)systems.get(i);
					Identification identification = (Identification)manageUserEditForm.getIdentifications().get(system.getId());

                    String tagID = request.getParameter(system.getName());

					for (int j = 0; j < users.size(); j++) {
						User user = (User) users.get(j);
						HashSet identifications = (HashSet) user.getIdentifications();
						for (Iterator iterator = identifications.iterator(); iterator.hasNext();) {
							Identification tempIdentification = (Identification)iterator.next();
							// prüfen ob andere Benutzer schon die TagID haben
							if ((tempIdentification.getTagID().equalsIgnoreCase(tagID))
								&& !tempIdentification.getUser().getId()
										.equals(updatedUser.getId())) {
					            ActionMessages errors = new ActionMessages();
					            ActionMessage error = new ActionMessage("errors.tagIDAlreadyExists");
					            errors.add("errors.tagIDAlreadyExists", error);
					            saveErrors(request, errors);  
						        return mapping.getInputForward();
							}
						}
					}
                    
				    if (system.getName().equals("PDA") && (tagID == null || tagID.length() == 0)) {
					    tagID = manageUserEditForm.getUserLoginId();
                    }
	

                    if(identification == null && tagID != null && tagID.length() > 0) {
					    identification = controllerConnection.createIdentification(system.getId(), tagID);
        				identList.add(identification.getId());
                    } else if(identification != null) {
                        if(tagID == null || tagID.length() == 0)
                        {
                            identsToDelete.add(identification);
                        }else
                        {
						    controllerConnection.updateIdentification(identification.getId(), tagID);
        					identList.add(identification.getId());
                        }
                    }
				}

    			controllerConnection.updateUser(updatedUser, identList);
                for(Identification ident: identsToDelete)
                {
                    controllerConnection.deleteIdentification(ident.getId());
                }
	
				return (mapping.findForward("success"));	
		}
		
		//The "reset" button was pressed. 
		if (manageUserEditForm.getDispatch().equals("reset")) {
			return (mapping.findForward("reset"));
		}
		
		//The "back" button was pressed. 
		if (manageUserEditForm.getDispatch().equals("back")) {
			return (mapping.findForward("back"));
		}

		return (mapping.findForward("success"));
    }
}
