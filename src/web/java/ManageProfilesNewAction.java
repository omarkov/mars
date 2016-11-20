package web.java;

import java.util.List;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import server.domain.BooleanType;
import server.domain.BooleanValue;
import server.domain.ComponentAttributeValue;
import server.domain.ComponentSetting;
import server.domain.ListType;
import server.domain.ListValue;
import server.domain.NumericType;
import server.domain.NumericValue;
import server.domain.SmartRoomProfile;
import server.domain.StringType;
import server.domain.StringValue;
import server.domain.User;
import web.java.controller.ControllerConnection;
/**
 * Executes the user input of a struts JSP-page. 
 *
 */
public class ManageProfilesNewAction extends Action{
	
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
	public ActionForward execute(ActionMapping mapping, 
			 ActionForm form, 
			 HttpServletRequest request, 
			 HttpServletResponse response) throws Exception {

	ManageProfilesNewForm manageProfilesNewForm = (ManageProfilesNewForm)form;
	//The "reset" button was pressed.
	if (manageProfilesNewForm.getDispatch().equalsIgnoreCase("reset")) {
		return (mapping.findForward("reset"));
	}
	//the "cancel" button was pressed. 
	if (manageProfilesNewForm.getDispatch().equalsIgnoreCase("back")) {
		return (mapping.findForward("back"));
	}
	
	//The "save" button was pressed. 
	if (manageProfilesNewForm.getDispatch().equalsIgnoreCase("save")) {
		ControllerConnection controllerConnection = (ControllerConnection)
		    request.getSession().getAttribute(Constants.CONNECTION);
		
		User currentUser;
        if (request.getSession().getAttribute("currentUser") != null) {
                currentUser = (User)request.getSession().getAttribute("currentUser");  
	        	System.out.println("selectedUser: " + currentUser.getUserLoginID());
        } else {
                currentUser = (User)controllerConnection.getCurrentUser();
        }

		SmartRoomProfile newProfile = (SmartRoomProfile)request.getSession().getAttribute("selectedProfile");
		Object[] componentSettings = newProfile.componentSettings.toArray();
		
		//Every setting for each component has to be handled. 
		for (int i = 0; i < componentSettings.length; i++) {
			ComponentSetting componentSetting = (ComponentSetting)componentSettings[i];
			
			//Avoid nullPointerException
			if (componentSetting.getComponentAttributeValues() != null) {
				Object[] componentAttributeValues = componentSetting
					.getComponentAttributeValues()
						.toArray();
				
				//Every value of each component has to be handled. 
				for (int j = 0; j < componentAttributeValues.length; j++) {
					ComponentAttributeValue componentAttributeValue = 
						(ComponentAttributeValue)componentAttributeValues[j];
					
					 //Identify the type of the componentAttributeValue
					 if (componentAttributeValue.getType() == NumericType.class) {
						 
							componentAttributeValue.setValue(new Integer(
									(Integer.parseInt(request.getParameter
											(componentAttributeValue
													.getComponentAttribute().getName())))));
					} 
					 //Identify the type of the componentAttributeValue
					 if (componentAttributeValue.getType() == BooleanType.class) {
						 if (request.getParameter(componentAttributeValue
								 .getComponentAttribute().getName()) != null) { 
							componentAttributeValue.setValue(Boolean.TRUE); 
						 }else
                         {
							componentAttributeValue.setValue(Boolean.FALSE); 
                         }
				    }
//					Identify the type of the componentAttributeValue						 
					 if (componentAttributeValue.getType() == ListType.class) {
						 // the array with the values from the client request
                         String componentName = componentAttributeValue.getComponentAttribute().getSmartRoomComponent().getName();
                         String attributeName = componentAttributeValue.getComponentAttribute().getName();
                         String listName = componentName + attributeName;

						 String[] valueStr = request.getParameterValues(listName);
						 // the current array with the values from the database
						 ListValue listValue = (ListValue) componentAttributeValue;
						 List list = listValue.getValue();

                         if(list == null) list = new ArrayList();

						 if (valueStr != null)
						 {
							try {
								 //	delete all old values from the list
								 list.clear();								 
								 // iterate through the request values	
								 for (int f = 0; f < valueStr.length; f++ )
								 {
									 list.add(valueStr[f]);
								 }
								 // set the list with the new values to the componentParameter
								 listValue.setValue(list);
							 }
							 catch (Exception e)
							 {
								 // TODO send an error to the user
							 }
						 }
						 else
						 {
							 // TODO Parameter does not exist in the request object
						 }	 
						 	 
					 }  
					 
					 //Identify the type of the componentAttributeValue
					 if (componentAttributeValue.getType() == StringType.class) {
							componentAttributeValue.setValue(new String
									(request.getParameter(componentAttributeValue.getComponentAttribute().getName())));
					 }						
				}
			}
		}
		newProfile.setComment(manageProfilesNewForm.getComment());
		//newProfile.setLastChange(manageProfilesNewForm.getLastChange());
		newProfile.setName(manageProfilesNewForm.getName());
		
		controllerConnection.createProfile(newProfile, currentUser.getId());
		return (mapping.findForward("save"));
	}
	
	return super.execute(mapping, form, request, response);
	}
	
	
}
