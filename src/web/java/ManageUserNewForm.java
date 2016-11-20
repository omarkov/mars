package web.java;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import net.NetworkException;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.validator.ValidatorForm;

import server.domain.Department;
import server.domain.User;

import web.java.controller.ControllerConnection;
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


public final class ManageUserNewForm extends ValidatorForm implements Serializable {
	private String action = null;
	
	private Long id;
	private String firstName;
	private String lastName;
	private String password;
	private String userLoginId;
	private String opsTagId;
	private String expirationDate;
	private String comment;
	private String expirationDay;
	private String expirationMonth;
	private String expirationYear;
	private String expirationLimit = null;
	private boolean isAdmin = false;
	private String email;
	private String quota;
	private Collection dayCollection = null;
	private Collection monthCollection = null;
	private Collection yearCollection = null;
	private String dispatch = null;
	private List departments;
	private Long selectedDepartmentID = null;

	/**
	 * Needed by the JSP to display a dropdown box. 
	 * @return LabelValueBean(s)
	 */	
	public Collection getYearCollection() {
        if (yearCollection == null) {
            Vector entries = new Vector(10);            
            
            entries.add(new LabelValueBean("2005", "2005"));
            entries.add(new LabelValueBean("2006", "2006"));
            entries.add(new LabelValueBean("2007", "2007"));
            entries.add(new LabelValueBean("2008", "2008"));
            entries.add(new LabelValueBean("2009", "2009"));
            entries.add(new LabelValueBean("2010", "2010"));
            entries.add(new LabelValueBean("2011", "2011"));
            entries.add(new LabelValueBean("2012", "2012"));
            entries.add(new LabelValueBean("2013", "2013"));
            entries.add(new LabelValueBean("2014", "2014"));

            yearCollection = entries;
        }

        return (yearCollection);
    }
    public void setYearCollection(Collection yearCollection) {
        this.yearCollection = yearCollection;
    }
	
	/**
	 * Needed by the JSP to display a dropdown box. 
	 * @return LabelValueBean(s)
	 */	
	public Collection getMonthCollection() {
        if (monthCollection == null) {
            Vector entries = new Vector(12);

            entries.add(new LabelValueBean("January", "01"));
            entries.add(new LabelValueBean("February", "02"));
            entries.add(new LabelValueBean("March", "03"));
            entries.add(new LabelValueBean("April", "04"));
            entries.add(new LabelValueBean("May", "05"));
            entries.add(new LabelValueBean("June", "06"));
            entries.add(new LabelValueBean("July", "07"));
            entries.add(new LabelValueBean("August", "08"));
            entries.add(new LabelValueBean("September", "09"));
            entries.add(new LabelValueBean("October", "10"));
            entries.add(new LabelValueBean("November", "11"));
            entries.add(new LabelValueBean("December", "12"));

            monthCollection = entries;
        }

        return (monthCollection);
    }
	
    public void setMonthCollection(Collection monthCollection) {
        this.monthCollection = monthCollection;
    }


	/**
	 * Needed by the JSP to display a dropdown box. 
	 * @return LabelValueBean(s)
	 */	
    public Collection getDayCollection() {
        if (dayCollection == null) {
            Vector entries = new Vector(31);

            entries.add(new LabelValueBean("1", "01"));
            entries.add(new LabelValueBean("2", "02"));
            entries.add(new LabelValueBean("3", "03"));
            entries.add(new LabelValueBean("4", "04"));
            entries.add(new LabelValueBean("5", "05"));
            entries.add(new LabelValueBean("6", "06"));
            entries.add(new LabelValueBean("7", "07"));
            entries.add(new LabelValueBean("8", "08"));
            entries.add(new LabelValueBean("9", "09"));
            entries.add(new LabelValueBean("10", "10"));
            entries.add(new LabelValueBean("11", "11"));
            entries.add(new LabelValueBean("12", "12"));
            entries.add(new LabelValueBean("13", "13"));
            entries.add(new LabelValueBean("14", "14"));
            entries.add(new LabelValueBean("15", "15"));
            entries.add(new LabelValueBean("16", "16"));
            entries.add(new LabelValueBean("17", "17"));
            entries.add(new LabelValueBean("18", "18"));
            entries.add(new LabelValueBean("19", "19"));
            entries.add(new LabelValueBean("20", "20"));
            entries.add(new LabelValueBean("21", "21"));
            entries.add(new LabelValueBean("22", "22"));
            entries.add(new LabelValueBean("23", "23"));
            entries.add(new LabelValueBean("24", "24"));
            entries.add(new LabelValueBean("25", "25"));
            entries.add(new LabelValueBean("26", "26"));
            entries.add(new LabelValueBean("27", "27"));
            entries.add(new LabelValueBean("28", "28"));
            entries.add(new LabelValueBean("29", "29"));
            entries.add(new LabelValueBean("30", "30"));
            entries.add(new LabelValueBean("31", "31"));
			

            dayCollection = entries;
        }

        return (dayCollection);
    }
	
    public void setDayCollection(Collection dayCollection) {
        this.dayCollection = dayCollection;
    }
	
	public String getQuota() {
		return quota;
	}
	public void setQuota(String quota) {
		this.quota = quota;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public String getExpirationDay() {
		return expirationDay;
	}

	public void setExpirationDay(String expirationDay) {
		this.expirationDay = expirationDay;
	}

	public boolean getIsAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	/**
	 * Build the expirationdate so that the action-class can handle it. 
	 * @return the expirationDate
	 */	
	public String getExpirationDate() {
		if (this.expirationLimit.equalsIgnoreCase(Constants.EXPIRATION_DATE_LIMITED)) {
			return expirationMonth + "/" + expirationDay + "/" + expirationYear;			
		} else {
			return null;
		}
	}

	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}

	public String getOpsTagId() {
		return opsTagId;
	}
	public void setOpsTagId(String opsTagId) {
		this.opsTagId = opsTagId;
	}
	public String getUserLoginId() {
		return userLoginId;
	}
	public void setUserLoginId(String userLoginId) {
		this.userLoginId = userLoginId;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public String getExpirationMonth() {
		return expirationMonth;
	}

	public void setExpirationMonth(String expirationMonth) {
		this.expirationMonth = expirationMonth;
	}

	public String getExpirationYear() {
		return expirationYear;
	}

	public void setExpirationYear(String expirationYear) {
		this.expirationYear = expirationYear;
	}

	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}


	public String getExpirationLimit() {
		return expirationLimit;
	}
	public void setExpirationLimit(String expirationLimit) {
		this.expirationLimit = expirationLimit;
	}
	public String getDispatch() {
		return dispatch;
	}

	public void setDispatch(String dispatch) {
		this.dispatch = dispatch;
	}
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		this.expirationLimit = Constants.EXPIRATION_DATE_UNLIMITED;
		
		ControllerConnection controllerConnection = (ControllerConnection)
			request.getSession().getAttribute(Constants.CONNECTION);
		try {
		List departmentList = controllerConnection.getAllDepartments();
			departments = new ArrayList();
			
			//No department has the ID: 0. => this ID is used if 
			//no department was selected by the user.
			departments.add(new LabelValueBean(Constants.NO_DEPARTMENT_SELECTED, 
					"0"));

			for (int i = 0; i < departmentList.size(); i++) {
				departments.add(new LabelValueBean(
						((Department)departmentList.get(i)).getName(), 
						((Department)departmentList.get(i)).getId().toString()));
			}
		} catch (NetworkException e) {
			e.printStackTrace();
		}
		
		if (request.getParameter("isAdmin") != null)
				isAdmin = true;
			
		if (request.getParameter("isAdmin") == null) 
			isAdmin = false;

		super.reset(mapping, request);
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public List getDepartments() {
		return departments;
	}
	public void setDepartments(List departments) {
		this.departments = departments;
	}
	public Long getSelectedDepartmentID() {
		return selectedDepartmentID;
	}
	public void setSelectedDepartmentID(Long selectedDepartmentID) {
		this.selectedDepartmentID = selectedDepartmentID;
	}
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errors = super.validate(mapping, request);
		ControllerConnection controllerConnection = (ControllerConnection)
		request.getSession().getAttribute(Constants.CONNECTION);
		if(this.expirationLimit.equals("limited")) {
			this.expirationMonth = String.valueOf(Integer.valueOf(request.getParameter("month")) + 1);
			this.expirationDay = request.getParameter("day");
			this.expirationYear = request.getParameter("year");

			this.expirationDate = expirationMonth + "/" + expirationDay + "/" + expirationYear;
			DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT,  Locale.ENGLISH); 
			Date dateToValidate;
			try {
				dateToValidate = df.parse(this.expirationDate);
				if (dateToValidate.before(new Date())) {
					ActionMessage error = new ActionMessage("errors.expirationDate.beforeNow");	
					errors.add("errors.expirationDate.beforeNow", error);

				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		//check if the a user with this LoginId already exists.
		List allUsers;
		try {
			allUsers = controllerConnection.getAllUsers();

			for (int i = 0; i < allUsers.size(); i++) {
				if (((User)allUsers.get(i)).getUserLoginID().equals(this.userLoginId)) {
					ActionMessage error = new ActionMessage("errors.userAlreadyExists");	
					errors.add("errors.userAlreadyExists", error);
					
				}
			}
		} catch (NetworkException e) {
			e.printStackTrace();
		}		

		return errors;
	}
	
}
