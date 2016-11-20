package web.java;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.SimpleTimeZone;
import java.util.TimeZone;
import javax.servlet.http.HttpServletRequest;

import web.java.controller.ControllerConnection;
import net.NetworkException;
import server.domain.*;
import java.util.List;
import java.util.ArrayList;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.*;
import java.lang.Integer;

import server.domain.SmartRoomProfile;
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

public class ManageProfilesEditForm extends ValidatorForm implements Serializable {
	private String name;
	private Date lastChange = new Date();
	private String comment;
	private Set componentSettings = new HashSet();
	private String dispatch = null;
	private Locale locale = null;
	
	
	public String getDispatch() {
		return dispatch;
	}
	public void setDispatch(String dispatch) {
		this.dispatch = dispatch;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getLastChange() {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(lastChange);
		String[] months = new DateFormatSymbols(locale).getMonths();
		return calendar.get(Calendar.DAY_OF_MONTH) + ". " + months[calendar.get(Calendar.MONTH)]
				+ " " + calendar.get(Calendar.YEAR);
	}
	public void setLastChange(String lastChange) {
		DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT,  locale); 
		try {
			this.lastChange = df.parse(lastChange);
		} catch (ParseException e) {
			e.printStackTrace();
		};
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ActionErrors fillProfile(HttpServletRequest request)
	{
        ActionErrors errors = new ActionErrors();
		SmartRoomProfile selectedProfile = (SmartRoomProfile)request
			.getSession().getAttribute("selectedProfile");
		//the current components the smartroom can handle
		Object[] componentSettings = selectedProfile.componentSettings.toArray();
		
		//Every component has to be handled. 
		for (int i = 0; i < componentSettings.length; i++) {
			ComponentSetting componentSetting = (ComponentSetting)componentSettings[i];
			
			//Avoid nullPointerException
			if (componentSetting.getComponentAttributeValues() != null) {
				Object[] componentAttributeValues = componentSetting
					.getComponentAttributeValues()
						.toArray();
				
				//Every value of the current component has to be handled
				for (int j = 0; j < componentAttributeValues.length; j++) {
					ComponentAttributeValue componentAttributeValue = 
						(ComponentAttributeValue)componentAttributeValues[j];

                 			String componentName = componentAttributeValue.getComponentAttribute().getSmartRoomComponent().getName();
                 			String attributeName = componentAttributeValue.getComponentAttribute().getName();
                 			String fieldName     = componentName + attributeName;
					
					 //Identify the type of the componentAttributeValue
					 if (componentAttributeValue.getType() == NumericType.class) {
						 int  numericValue = Integer.parseInt(request.getParameter(fieldName));
					     ComponentAttribute componentAttribute = componentAttributeValue.getComponentAttribute(); 
						 NumericType numericType = (NumericType)componentAttribute.getAttributeType();
                         
                         int max = numericType.getMax().intValue();
                         int min = numericType.getMin().intValue();
                         int step = numericType.getStep().intValue();
                         
                         
                         numericValue -= (numericValue - min) % step;
                         numericValue = numericValue >= min ? 
                            (numericValue <= max ? numericValue: max) : min;
					    
                        componentAttributeValue.setValue(new Integer(numericValue));	 
					 } 
					 
					 //Identify the type of the componentAttributeValue						 
					 if (componentAttributeValue.getType() == BooleanType.class) {
						 if (request.getParameter(fieldName) != null) {
							componentAttributeValue.setValue(Boolean.TRUE);
						 }else
                     {
							componentAttributeValue.setValue(Boolean.FALSE);
                     }
					 }
					 //Identify the type of the componentAttributeValue						 
					 if (componentAttributeValue.getType() == ListType.class) {

						 // the array with the values from the client request
						 String[] valueStr = request.getParameterValues(fieldName);
						 // the current array with the values from the database
						 ListValue listValue = (ListValue) componentAttributeValue;
						 List list = listValue.getValue();
                     				if(list == null) list = new ArrayList();

						//	delete all old values from the list
						list.clear();								 
						 
						 if (valueStr != null)
						 {
							try {
								 // iterate through the request values	
								 for (int f = 0; f < valueStr.length; f++ )
								 {
									 //add the next object to the end of the list	
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
							componentAttributeValue.setValue(request.getParameter(fieldName));
					 }						
				}
			}
		}
		return errors;
	}
	
	/**
	 * Get the data for the selected profile. 
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) {
        boolean edit = "EDIT".equals(request.getSession().getAttribute("PROFILEACTION"));
		System.out.println("RESET");
        if(edit)
        {
		    SmartRoomProfile selectedProfile = (SmartRoomProfile)request.getSession().getAttribute("selectedProfile");
		    this.name = selectedProfile.getName();
		    this.comment = selectedProfile.getComment();
		    if(selectedProfile.getLastChange() != null)
			    this.lastChange = selectedProfile.getLastChange();
		    this.componentSettings = selectedProfile.getComponentSettings(); 
        }
		locale = (Locale)request.getSession().getAttribute("org.apache.struts.action.LOCALE");
		super.reset(mapping, request);
	}
	public Set getComponentSettings() {
		return componentSettings;
	}
	public void setComponentSettings(Set componentSettings) {
		this.componentSettings = componentSettings;
	}

	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errors = super.validate(mapping, request);

		errors.add(fillProfile(request));
		request.getSession().setAttribute("PROFILEERROR", Boolean.TRUE);
		Long userId = ((User)request.getSession().getAttribute("currentUser")).getId();
        boolean edit = "EDIT".equals(request.getSession().getAttribute("PROFILEACTION"));

		ControllerConnection controllerConnection = (ControllerConnection)
			request.getSession().getAttribute(Constants.CONNECTION);

		if(name == null || name.length() == 0)
		{
			ActionMessage error = new ActionMessage("errors.profileName");	
			errors.add("errors.profileName", error);
		}

        if(!edit)
        {
    		try
    		{
    			List<SmartRoomProfile> profiles = controllerConnection.getProfilesForUser(userId);
    			for(SmartRoomProfile p: profiles)
    			{
    				if(p.getName().equalsIgnoreCase(name))
    				{
    					ActionMessage error = new ActionMessage("errors.profileAlreadyExists");	
    					errors.add("errors.profileAlreadyExists", error);
    					break;
    				}
    			}
    
    		}catch(NetworkException e)
    		{
    			ActionMessage error = new ActionMessage("errors.generalError");	
    			errors.add("errors.generalError", error);
    		}
        }
		return errors;
	}

}
