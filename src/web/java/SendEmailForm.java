package web.java;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.ValidatorForm;

import server.domain.User;

import web.java.controller.ControllerConnection;

public final class SendEmailForm extends ValidatorForm{

    private Long[] selectedGroups = null;	
	private String subject = "";	
	private String messageBody = "";
	
	private ArrayList groups; 	//the users still in the group
	
	private String dispatch = "";
    private ActionMessages errors = null;
	
    public void reset(ActionMapping mapping, HttpServletRequest request){
		try {
			ControllerConnection controllerConnection = (ControllerConnection)request.getSession().getAttribute(Constants.CONNECTION);
			groups = new ArrayList(controllerConnection.getAllGroups());

		} catch (Exception e) {    
			//TODO Additional Exception handling
			errors = new ActionMessages();
			ActionMessage actionMessage = new ActionMessage("errors.network");
			errors.add("errors", actionMessage);	
		
			e.printStackTrace();
		}
		super.reset(mapping, request);
	}
    
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = super.validate(mapping, request); 	
		if (this.selectedGroups == null || this.selectedGroups.length < 1)
		{
			ActionMessage error = new ActionMessage("errors.noRecipients");	
			errors.add("errors.noRecipients", error);
			return errors;
		}
		try {	
			ControllerConnection controllerConnection = (ControllerConnection)request.getSession().getAttribute(Constants.CONNECTION);		
			for (int i = 0; i < this.selectedGroups.length; i++) 
			{
				List userObj  = controllerConnection.getUsersForGroup(selectedGroups[i]);
				// Add all email adresses to a string list
				if (userObj.size()<1 || userObj.equals(null))							
				{
					ActionMessage error = new ActionMessage("errors.noRecipients");	
					errors.add("errors.noRecipients", error);
					return errors;
				}
			}
		}
		catch (Exception e)
		{
			ActionMessage error = new ActionMessage("errors.generalError");	
			errors.add("errors.generalError", error);
			return errors;
		}
	
		return errors;
	}
     
	/* GETTER & SETTER */
	public String getMessageBody() {
		return messageBody;
	}
	
	public void setMessageBody(String messageBody) {
		this.messageBody = messageBody;
	}
	
	public String getSubject() {
		return subject;
	}
	
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	public String getDispatch() {
		return dispatch;
	}
	public void setDispatch(String dispatch) {
		this.dispatch = dispatch;
	}
	
	public ActionMessages getErrors() {
		return errors;
	}
	public void setErrors(ActionMessages errors) {
		this.errors = errors;
	}

	public ArrayList getGroups() {
		return groups;
	}

	public void setGroups(ArrayList groups) {
		this.groups = groups;
	}

	public Long[] getSelectedGroups() {
		return selectedGroups;
	}

	public void setSelectedGroups(Long[] selectedGroups) {
		this.selectedGroups = selectedGroups;
	}

	
}
