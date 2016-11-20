package web.java;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import server.domain.User;
import web.java.controller.ControllerConnection;

public final class SendEmailAction extends Action{
	
	public ActionForward execute(
    		ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response)
			throws Exception {
		
		ActionMessages errors = null;
		ActionMessages actionMessages = new ActionMessages();
		SendEmailForm sendEmailForm = (SendEmailForm) form;
		
		String sender = "Mar-S Administrator";
		ControllerConnection controllerConnection = (ControllerConnection)request.getSession().getAttribute(Constants.CONNECTION);				
		//The "Send Email" button was pressed
		if (sendEmailForm.getDispatch().equals("save")) {
			try{
				//List off all recipients 
				List recipients  = new ArrayList();
				//The set of groups which users should recieve an email
				Long[] selectedGroups = sendEmailForm.getSelectedGroups();
				
				if (selectedGroups != null) {
					for (int i = 0; i < selectedGroups.length; i++) 
					{
						List userObj  = controllerConnection.getUsersForGroup((Long)selectedGroups[i]);
						// Add all email adresses to a string list
						if (userObj.size()<1 || userObj.equals(null))							
						{
							ActionMessage actionMessage = new ActionMessage("errors.noRecipients");
							// ErrorTagName for the error message 
							actionMessages.add("FillAllFields", actionMessage);	
							errors.add(actionMessages);	
							return (mapping.findForward("nothingSelected"));
						}
						else
						{							
							for (int k=0; k < userObj.size(); k++)
							{
								// An email adress requires more than four characters
								if ( !((User) userObj.get(k)).getEmail().equals(null) && ((User) userObj.get(i)).getEmail().length()>4)
								{
									if (recipients.contains(((User) userObj.get(k)).getEmail()))
									{
										// user email is already in the list
										continue;
									}
									else
									{	// user email hadn't been added to list
										recipients.add( ((User) userObj.get(k)).getEmail());
									}
								}
							}
						}
					}
				}
				else
				{
					ActionMessage actionMessage = new ActionMessage("errors.noRecipients");
					// ErrorTagName for the error message 
					actionMessages.add("FillAllFields", actionMessage);	
					errors.add(actionMessages);	
					return (mapping.findForward("nothingSelected"));
				}
				
				if (recipients.size() <= 0 || recipients.equals(null))
				{
					ActionMessage actionMessage = new ActionMessage("errors.noRecipients");
					// ErrorTagName for the error message 
					actionMessages.add("FillAllFields", actionMessage);	
					errors.add(actionMessages);	
					return (mapping.findForward("nothingSelected"));
				}		
				controllerConnection.composeMessage(sender, recipients, sendEmailForm.getSubject(), sendEmailForm.getMessageBody());
				return (mapping.findForward("success"));
			}
			catch (Exception e)
			{		
				ActionMessage actionMessage = new ActionMessage("errors.generalError");
				// ErrorTagName for the error message 
				actionMessages.add("FillAllFields", actionMessage);	
				errors.add(actionMessages);	
				return (mapping.findForward("nothingSelected"));
			}
		}

		return (mapping.findForward("nothingSelected"));
    }
}
