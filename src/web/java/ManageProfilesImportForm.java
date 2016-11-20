package web.java;

import java.util.Collection;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;

import net.NetworkException;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.*;

import web.java.controller.ControllerConnection;

public class ManageProfilesImportForm extends ActionForm
{
	private FormFile file;
	private String dispatch = "";

	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
		ActionMessages actionMessages = new ActionMessages();
   	
		if ( (this.file == null || this.file.getFileSize() == 0) ) {							
			// displayed error message in the JSP
			ActionMessage actionMessage = new ActionMessage("errors.FileEmptyOrNull");
			// ErrorTagName for the error message 
			actionMessages.add("FileEmptyOrNull", actionMessage);	
			errors.add(actionMessages);
		} 
		return errors;
	}
	
	public FormFile getFile() {
		return file;
	}

	public void setFile(FormFile file) {
		this.file = file;
	}

	public String getDispatch() {
		return dispatch;
	}

	public void setDispatch(String dispatch) {
		this.dispatch = dispatch;
	}
	

}
