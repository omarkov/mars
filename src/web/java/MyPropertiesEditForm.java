package web.java;

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.*;
import org.apache.struts.validator.ValidatorForm;

import web.java.LoginForm;
/**
 * An ActionForm is a JavaBean optionally associated with 
 * one or more ActionMappings. Such a bean will have had 
 * its properties initialized from the corresponding request 
 * parameters before the corresponding Action.execute method is called.
 * When the properties of this bean have been populated, 
 * but before the execute method of the Action is called, 
 * this bean's validate method will be called, which gives 
 * the bean a chance to verify that the properties submitted 
 * by the user are correct and valid. If this method finds problems, 
 * it returns an error messages object that encapsulates those problems, 
 * and the controller servlet will return control to the corresponding 
 * input form. Otherwise, the validate method returns null, 
 * indicating that everything is acceptable and the corresponding 
 * Action.execute method should be called.
 * 
 * This class must be subclassed in order to be instantiated. 
 * Subclasses should provide property getter and setter 
 * methods for all of the bean properties they wish to expose, 
 * plus override any of the public or protected methods for which 
 * they wish to provide modified functionality.
 * Because ActionForms are JavaBeans, subclasses should also 
 * implement Serializable, as required by the JavaBean specification. 
 * Some containers require that an object meet all JavaBean 
 * requirements in order to use the introspection API upon which ActionForms rely.
 * 
 */

public final class MyPropertiesEditForm extends ValidatorForm{
	
	private String faction = "";  	
    private String oldpassword  = "";
	private String newpassword = "";
	private String retypenewpassword ="";
	private String dispatch = null;
		
	public String getDispatch() {
		return dispatch;
	}

	public void setDispatch(String dispatch) {
		this.dispatch = dispatch;
	}

	public String getFaction() {
	    return faction;
	}
	
	public void setFaction(String tmpAction) {
	    this.faction = tmpAction;
	}	

	public String getOldpassword() {
	    return oldpassword;
	}
	
	public void setOldpassword(String tmpopwd) {
	    this.oldpassword = tmpopwd;
	}
	
	public String getNewpassword() {
	    return newpassword;
	}
	
	public void setNewpassword(String tmpnpwd) {
	    this.newpassword = tmpnpwd;
	}
	public String getRetypenewpassword() {
	    return retypenewpassword;
	}
	
	public void setRetypenewpassword(String tmprnpwd) {
	    this.retypenewpassword = tmprnpwd;
	}

	/**
	 * Validate the user input
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
		ActionMessages actionMessages = new ActionMessages();

		
		if (((this.oldpassword == null) || (oldpassword.length() < 1))
			|| ((this.newpassword == null) || (newpassword.length() < 1))
			|| ((this.retypenewpassword == null) || (retypenewpassword.length() < 1))) {
			ActionMessage actionMessage = new ActionMessage("errors.fillAllFields");
			actionMessages.add("oldpassword", actionMessage);	
			errors.add(actionMessages);
		} else {

			if (!(this.getNewpassword().equals(this.getRetypenewpassword()))){
				ActionMessage actionMessage = new ActionMessage("errors.passwordsNotEqual");
				actionMessages.add("newpassword", actionMessage);
	            errors.add(actionMessages);
			}
			if (!(((LoginForm)request.getSession().getAttribute(Constants.USER_KEY))
					.getPassword().equals(this.getOldpassword()))) {
				ActionMessage actionMessage = new ActionMessage("errors.wrongPassword");
				actionMessages.add("oldpassword", actionMessage);
	            errors.add(actionMessages);
			}
		}
		return errors;
	}
}
