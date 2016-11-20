package web.java;

import java.util.*;
import java.text.DateFormat;
import javax.servlet.http.HttpServletRequest;
import net.NetworkException;
import org.apache.struts.action.*;
import org.apache.struts.validator.ValidatorForm;

import server.domain.User;
import web.java.controller.*;
/**
*Last Change: 05.01.06  
*/

public final class MyPropertiesForm extends ValidatorForm {
	private String action = null;

	private String username = null;
	private String firstname = null;
	private String lastname = null;
	private String email = null;
	private String defaultProfile = null;
	private String quota = null;
	private String expirationDate = null;
	private String department = null;
	private String dispatch = null; //Tells the action-class witch forward to take. 
	private String superDuperRepeat = null;
	

	public String getSuperDuperRepeat() {
		return superDuperRepeat;
	}

	public void setSuperDuperRepeat(String superDuperRepeat) {
		this.superDuperRepeat = superDuperRepeat;
	}

	public String getDispatch() {
		return dispatch;
	}

	public void setDispatch(String dispatch) {
		this.dispatch = dispatch;
	}

	public String getUsername() {
	    return username;
	}
	
	public void setUsername(String tmpwd) {
	    this.username = tmpwd;
	}
	
	public String getFirstname() {
	    return firstname;
	}
	
	public void setFirstname(String tmpwd) {
	    this.firstname = tmpwd;
	}	
	
	public String getLastname() {
	    return lastname;
	}
	
	public void setLastname(String tmpwd) {
	    this.lastname = tmpwd;
	}
	    
	public String getEmail() {
	    return email;
	}
	
	public void setEmail(String tmpwd) {
	    this.email = tmpwd;
	}    
	
	public String getDefaultProfile() {
		return defaultProfile;
	}

	public void setDefaultProfile(String defaultProfile) {
		this.defaultProfile = defaultProfile;
	}  
		
	public String getQuota() {
	    return quota;
	}
	
	public void setQuota(String tmpwd) {
	    this.quota = tmpwd;
	}

	public String getExpirationDate() {
	    return expirationDate;
	}
	
	public void setExpirationDate(String tmped) {
	    this.expirationDate = tmped;
	}
	
	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	
	/**
	 * Get the data for the current user. 
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) {
            
        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.ENGLISH);    
		ControllerConnection controllerConnection = (ControllerConnection)request.getSession().getAttribute(Constants.CONNECTION);
		try 
		{
                    User currentUser = (User)controllerConnection.getCurrentUser();  
                    this.email = currentUser.getEmail();
                    this.firstname = currentUser.getFirstName();
                    this.lastname = currentUser.getLastName();
                    this.username = currentUser.getUserLoginID();
					if (currentUser.isSuperDuperRepeat()) {
						this.superDuperRepeat = "on";						
					} else {
						this.superDuperRepeat = "off";
					}


                    //set correct quota
                    if (currentUser.getQuota() != 0) {
                        this.quota = Integer.toString(currentUser.getQuota());
                    }
                    else {
                        this.quota = "Unlimited quota";
                    }
                    
                    //set the name of the default profile
                    if (currentUser.getDefaultProfile() != null) {
                        this.defaultProfile = currentUser.getDefaultProfile().getName();
                    }
                    else {
                        this.defaultProfile = "No default profile";
                    }
		
                    //set ExpirationDate    
                    if (currentUser.getExpirationDate() != null) {
                        this.expirationDate = df.format(currentUser.getExpirationDate());
                    }
                    else {
                        this.expirationDate = "No expiration.";
                    }
                    //set Department
                    if (currentUser.getDepartment() != null) {
                        this.department = currentUser.getDepartment().getName();
                    }
                    else {
                        this.department = "No Department";
                    }
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

        
		super.reset(mapping, request);
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
	
}
