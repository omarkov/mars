<%@ taglib uri="/tags/struts-bean" prefix="bean"%>
<%@ taglib uri="/tags/struts-html" prefix="html"%>
<%@ taglib uri="/tags/struts-logic" prefix="logic"%>
<%@ taglib uri="/tags/display" prefix="display" %>
<%@ include file="redirectInclude.jsp"%>
<%@ page import="server.domain.User" %>
<%@ page import="web.java.Constants" %>
<%@ page import="web.java.controller.ControllerConnection" %>

<%
	User selectedUser;
	ControllerConnection controllerConnection = (ControllerConnection)request.getSession().getAttribute(Constants.CONNECTION);
   if (request.getParameter("fromMenue") != null) 
   {
   	selectedUser = (User)controllerConnection.getCurrentUser();
 		request.getSession().setAttribute("selectedUser", selectedUser);  
   } 
   else 
   {	    	
	   if (request.getSession().getAttribute("selectedUser") == null) 
		selectedUser = (User)controllerConnection.getCurrentUser();
	   else
		selectedUser = ((User)request.getSession().getAttribute("selectedUser"));

   }
%>

<html:form action="/pages/manageProfilesShow-submit">

<html:hidden name="manageProfilesShowForm" property="dispatch" value="error"/>

<fieldset>
    <legend style="color:black"><bean:message key='label.profileof' /><%=selectedUser.getUserLoginID() %></legend>
    <div>
        <label><bean:message key='label.defaultProfile' /></label>
        <bean:write name="manageProfilesShowForm" property="defaultProfile"/>
    </div> 
</fieldset>
<fieldset>
    <legend style="color:black"><bean:message key='label.availableProfiles' /></legend>

	<display:table requestURI="/ManageProfilesShow.do" id="profile" name="manageProfilesShowForm.currentUserProfiles" decorator="web.java.ProfileTableDecorator">
		<display:column property="name" titleKey="manageProfilesShow.table.name" />
		<display:column property="comment" titleKey="manageProfilesShow.table.comment"/>
		<display:column property="link" titleKey="manageProfilesShow.table.option" />
	</display:table> 
 
</fieldset>

<fieldset>
	<div class="submit">
        <html:submit property="submit" onclick="setDispatch('new');">
                <bean:message key="button.newProfile" />
        </html:submit>
       <!--   <html:submit property="submit" onclick="setDispatch('import');">
                <bean:message key="button.import" />
        </html:submit>  -->  
	</div>
</fieldset>
        
</html:form>
