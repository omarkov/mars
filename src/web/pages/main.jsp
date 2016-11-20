<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/tags/struts-tiles" prefix="tiles" %>
<%@ page language="java" %>
<%@ include file="redirectInclude.jsp"%>

<%@ page import="server.domain.User" %>
<%@ page import="web.java.Constants" %>
<%@ page import="web.java.LoginForm" %>
<%@ page import="web.java.controller.ControllerConnection" %>

<%  
    ControllerConnection controllerConnection = (ControllerConnection)
    request.getSession().getAttribute(Constants.CONNECTION);
    User currentUser = (User)controllerConnection.getCurrentUser();    
%>


<% if(currentUser.isAdministrator()) { %>
<div text-align="center"> 
<table class="menutb">
	<tr>
		<td class="menutd"> <html:link page="/ManageDepartment.do" title="Manage Profiles" >
							<html:img src="/mars/images/dep.png" />
							</html:link></td>
		<td class="menutd"> <html:link page="/ManageUser.do" title="Manage User" >
							<html:img src="/mars/images/user.png" />
							</html:link></td>
		<td class="menutd"> <html:link page="/ManageGroup.do" title="Manage Group" >
							<html:img src="/mars/images/group.png" />
							</html:link></td>
	</tr>
	<tr>
		<td class="menutd"> <html:link page="/ManageDepartment.do" title="Manage Profiles" >
							<bean:message key="mars.manageDepartment" />
							</html:link></td>
		<td class="menutd"> <html:link page="/ManageUser.do" title="Manage User" >
							<bean:message key="mars.manageUser" />
							</html:link></td>
		<td class="menutd"> <html:link page="/ManageGroup.do" title="Manage Group" >
							<bean:message key="mars.manageGroup" />
							</html:link></td>
	</tr>
</table>
<table class="menutb">
	<tr>
		<td class="menutd"> <html:link page="/ManageProfilesShow.do?fromMenue=true" title="myProfiles" >
							<html:img src="/mars/images/profiles.png" />
							</html:link></td>
		<td class="menutd"> <html:link page="/MyProperties.do" title="myProperties" >
							<html:img src="/mars/images/edit_user.png" />
							</html:link></td>
	</tr>
	<tr>
		<td class="menutd"> <html:link page="/ManageProfilesShow.do?fromMenue=true" title="myProfiles" >
							<bean:message key="mars.myProfiles" />
							</html:link></td>
		<td class="menutd"> <html:link page="/MyProperties.do" title="myProperties" >
							<bean:message key="mars.myProperties" />
							</html:link></td>
	</tr>
</table>
</div>
<%
} else { %>

<table class="menutb">

	<tr>
		<td class="menutd"> <html:link page="/ManageProfilesShow.do" title="myProfiles" >
							<html:img src="/mars/images/profiles.png" />
							</html:link></td>
		<td class="menutd"> <html:link page="/MyProperties.do" title="myProperties" >
							<html:img src="/mars/images/edit_user.png" />
							</html:link></td>
	</tr>
	<tr>
		<td class="menutd"> <html:link page="/ManageProfilesShow.do" title="myProfiles" >
							<bean:message key="mars.myProfiles" />
							</html:link></td>
		<td class="menutd"> <html:link page="/MyProperties.do" title="myProperties" >
							<bean:message key="mars.myProperties" />
							</html:link></td>
	</tr>
        
</table>

<%} %>