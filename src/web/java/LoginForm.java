package web.java;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.*;
import org.apache.struts.validator.ValidatorForm;
import org.apache.struts.action.ActionMapping;

/**
 * Form bean for the user login page.
 */
public final class LoginForm extends ValidatorForm {

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
        this.password = "";
        this.username = "";
        super.reset(mapping, request);
    }


    /**
     * Ensure that both fields have been input.
     *
     **/
    
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        
        if (((this.username == null) || (username.length() < 1) & ((this.password == null) || (password.length()<1))) || (password.length() <1) || (username.length() <1) ){
            ActionMessage error = new ActionMessage("errors.loginError", "Login");
            errors.add("errors.loginError",error);
        }
        return errors;

    }
    
    

} // End LogonForm
