package web.java;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.ValidatorForm;
import org.apache.struts.action.ActionMapping;

/**
 * Form bean for the user login page.
 */
public final class MobileLoginForm extends ValidatorForm {

// ------------------------------------------------ Instance Variables
    /**
     * The password.
     */
    private String password = null;
    
    /**
     * The username.
     */
    private String username = null;

// ------------------------------------------------------ Properties


    /**
     * Return the password.
     */
    public String getPassword() {
        return (this.password);
    }

    /**
     * Set the password.
     *
     * @param password The new password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Return the username.
     */
    public String getUsername() {
        return (this.username);
    }

    /**
     * Set the username.
     *
     * @param username The new username
     */
    public void setUsername(String username) {
        this.username = username;
    }


// -------------------------------------------------- Public Methods


    /**
     * Reset all properties to their default values.
     *
     */
    public void reset(ActionMapping mapping,
        HttpServletRequest request) {
        setPassword(null);
        setUsername(null);
    }


    /**
     * Ensure that both fields have been input.
     *
     **/
    
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {

        ActionErrors errors = new ActionErrors();
        ActionMessages actionMessages = new ActionMessages();
        
        if (((this.username == null) || (username.length() < 1) & ((this.password == null) || (password.length()<1))) || (password.length() <1) || (username.length() <1) ){
            ActionMessage actionMessage = new ActionMessage("errors.loginError");
            actionMessages.add("", actionMessage);
            errors.add(actionMessages);}
        
        return errors;

    }

} // End LogonForm
