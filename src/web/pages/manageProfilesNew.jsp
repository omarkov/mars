<%@ taglib uri="/tags/struts-bean" prefix="bean"%>
<%@ taglib uri="/tags/struts-html" prefix="html"%>
<%@ taglib uri="/tags/struts-logic" prefix="logic"%>
<%@ taglib uri="/tags/display" prefix="display" %>

<%@ page import="server.domain.SmartRoomProfile"%>
<%@ page import="server.domain.ComponentSetting" %>
<%@ page import="server.domain.ComponentAttributeValue" %>
<%@ page import="server.domain.SmartRoomComponent" %>
<%@ page import="server.domain.ComponentAttribute" %>
<%@ page import="server.domain.BooleanValue" %>
<%@ page import="server.domain.NumericValue" %>
<%@ page import="server.domain.StringValue" %>
<%@ page import="server.domain.ListType" %>
<%@ page import="server.domain.ListValue" %>
<%@ page import="web.java.controller.ControllerConnection" %>
<%@ page import="web.java.Constants" %>
<%@ page import="java.util.List" %>
<%@ page import="java.io.File" %>
<%@ page import="java.util.Comparator"%>
<%@ page import="java.util.Collections"%>
<%@ page import="java.util.ArrayList" %>

<%
boolean edit = "EDIT".equals(request.getSession().getAttribute("PROFILEACTION"));
%>

<!-- Das Skript selectAllOptionElements muss ggf. lokal in die Seite gespeichert werden, in diesem Fall das nachfolgenden Tag löschen-->
<script src="javaScripts.js"></script>
<!-- Ev. anstatt onsubmit onclick beim Save-Button -->

<html:form action="/pages/manageProfilesEdit-submit" onsubmit="selectAllOptionElements()"> 
    <html:hidden name="manageProfilesShow" property="dispatch" value="error"/>

    <fieldset>
        <legend><bean:message key="manageProfiles.profileoverview" /></legend>
        
        <div>
            <label><bean:message key="manageProfiles.profilename" /></label>
<% 
if(edit){ 
%>
            <html:text property="name" size="60" disabled="true"/>
<% 
}else{
%>
            <html:text property="name" size="60" />
<% 
} 
%>
        </div>
        
        <div>
            <label><bean:message key="manageProfiles.lastchangedon" /></label>
            <html:text property="lastChange" size="60" disabled="true"/>		
        </div>
        
        <div>
            <label><bean:message key="label.comment" /></label>
            <html:textarea property="comment" cols="46" />
        </div>
    </fieldset>

    <fieldset>
        <legend>
            <bean:message key="manageProfiles.prodilesettings" />
        </legend>
<%
ControllerConnection controllerConnection = (ControllerConnection)request.getSession().getAttribute(Constants.CONNECTION);	
SmartRoomProfile selectedProfile;

if(edit)
{
    selectedProfile = (SmartRoomProfile)request.getSession().getAttribute("selectedProfile");
}else
{
    try 
    {
        System.out.println("Create Default Profile");
        selectedProfile = controllerConnection.createDefaultProfileForUser(controllerConnection.getCurrentUser().getId());
    } 
    catch (Exception ex) 
    {
        selectedProfile = new SmartRoomProfile();
    }
}

request.getSession().setAttribute("selectedProfile", selectedProfile);
List componentSettings = new ArrayList(selectedProfile.getComponentSettings()); 
Long userId = controllerConnection.getCurrentUser().getId();
Collections.sort(componentSettings, new Comparator() {
    public int compare(Object o1, Object o2) 
    {
        SmartRoomComponent smartRoomComponent1 = ((ComponentSetting)o1).getSmartRoomComponent(); 
        SmartRoomComponent smartRoomComponent2 = ((ComponentSetting)o2).getSmartRoomComponent(); 
        return smartRoomComponent1.getName().compareTo(smartRoomComponent2.getName());
    }
} );

for (int i = 0; i < componentSettings.size(); i++) 
{
    ComponentSetting componentSetting = (ComponentSetting)componentSettings.get(i);
    SmartRoomComponent smartRoomComponent = componentSetting.getSmartRoomComponent();
    String componentName = smartRoomComponent.getName();
%>
        <h3><%=componentName%></h3>
<%	

    List componentAttributeValues = new ArrayList(componentSetting.getComponentAttributeValues()); 
    Collections.sort(componentAttributeValues, new Comparator() {
        public int compare(Object o1, Object o2)
        {
            ComponentAttribute a1 = ((ComponentAttributeValue)o1).getComponentAttribute();
            ComponentAttribute a2 = ((ComponentAttributeValue)o2).getComponentAttribute();
            Class c1 = a1.getAttributeType().getBasicType();
            Class c2 = a2.getAttributeType().getBasicType();
            String name1 = a1.getName();
            String name2 = a2.getName();

            if(c1 == Boolean.class)
                name1 = "a" + name1;
            else if(c1 == String.class)
                name1 = "b" + name1;
            else if(c1 == Integer.class)
                name1 = "c" + name1;
            else if(c1 == List.class)
                name1 = "d" + name1;

            if(c2 == Boolean.class)
                name2 = "a" + name2;
            else if(c2 == String.class)
                name2 = "b" + name2;
            else if(c2 == Integer.class)
                name2 = "c" + name2;
            else if(c2 == List.class)
                name2 = "d" + name2;

            return name1.compareTo(name2);
        }
    });

    for (int j = 0; j < componentAttributeValues.size(); j++) 
    {
        ComponentAttributeValue componentAttributeValue = (ComponentAttributeValue)componentAttributeValues.get(j);
        ComponentAttribute componentAttribute = componentAttributeValue.getComponentAttribute(); 
        String attributeName = componentAttribute.getName();
%>
        <div>
            <label><%=componentAttribute.getName() %></label>
<%
        if (componentAttributeValue instanceof NumericValue || componentAttributeValue instanceof StringValue) 
        {
            Object defaultValue = componentAttributeValue.getValue();
%>
        	
       		<input type="text" name="<%=attributeName%>" value="<%=defaultValue %>"/>
        </div> 
<%
        }else if (componentAttributeValue instanceof BooleanValue) 
        {
            Boolean defaultValue = ((Boolean)componentAttributeValue.getValue());
            String checked = "";
            if (defaultValue.booleanValue()) 
               checked = "checked";
%>
      	    <input type="checkbox" name="<%=attributeName%>" <%=checked %> value="on"/>
        </div> 
<%
      	}else if (componentAttributeValue instanceof ListValue) 
        {
            ListValue listValue = (ListValue) componentAttributeValue;
            ListType listType   = (ListType) componentAttribute.getAttributeType(); 
            String contentType  = listType.getContentType();
            String listName     = componentName + attributeName;
%>
<!-- was soll das ??? -->
        </div>
        <div id="list">
            <div class="addmember">
            <label><%=attributeName%></label>
            <select name="<%=listName%>" id="<%=listName%>" size="10"  border="5" multiple>
<%
            List list = listValue.getValue();
            for(int k=0; k<list.size(); k++) 
            {
                String value = (String)list.get(k);
                if (value != null && value.length()>0)
                {
                    value = value.substring(0, value.lastIndexOf("</file>"));
%>
                <option value="<%=value%>"><%=value.substring(value.lastIndexOf(File.separator)+1, value.length())%></option>
<%
                }
            }
%>
            </select>
        </div>
	 							
        <!-- ADD and REMOVE - Button -->															
        <div class="arrow">
            <input type="button" name="addToList" value="  +  " onclick="javascript:AddToList('_<%=listName%>_','<%=listName%>')">
            <input type="button" name="removeFromList" value="  --  " onclick="javascript:RemoveFromList('<%=listName%>')">
        </div>

        <!-- Create list with all mediacontent, to which user has access -->	
        <div class="addmember">
            <label><%=contentType%></label> 
            <select name="_<%=listName%>_" id="_<%=listName%>_" size="10" border="5" multiple>
<%
            if(listType.isAudioList().booleanValue())
                list = controllerConnection.getAudioFiles(userId);
            else if(listType.isVideoList().booleanValue())
                list = controllerConnection.getVideoFiles(userId);
            else
                list = controllerConnection.getImageFiles(userId);

            for(int k=0; k < list.size(); k++) 
            {
                String value = (String)list.get(k);
                if (value != null && value.length()>0)
                {
                    value = value.substring(0, value.lastIndexOf("</file>"));
%>
                <option value="<%=value%>"><%=value.substring( value.lastIndexOf(File.separator)+1 , value.length())%></option>
<%	
                }else continue; 
            }
%>
            </select>
        </div>
<%	
 		}
    }
}
%>  
    </fieldset>
    
    <fieldset>
        <div class="submit">	
            <html:submit property="submit" onclick="setDispatch('save');"> 
                <bean:message key="button.save" />
            </html:submit> 
            <!-- selectAllOptionElements(); -->
            <html:reset>
                <bean:message key="button.reset" /> 
            </html:reset> 
            <html:submit property="submit" onclick="setDispatch('back');">
                <bean:message key="button.back" />
            </html:submit>
        </div>
    </fieldset>
</html:form>
