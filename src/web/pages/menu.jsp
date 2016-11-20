<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/tags/struts-tiles" prefix="tiles" %>

<%@ page import="server.domain.User" %>
<%@ page import="web.java.Constants" %>
<%@ page import="web.java.LoginForm" %>
<%@ page import="web.java.controller.ControllerConnection" %>

<%@ page language="java" %>
<%@ include file="redirectInclude.jsp"%>

<%  
    ControllerConnection controllerConnection = (ControllerConnection)
    request.getSession().getAttribute(Constants.CONNECTION);
    User currentUser = (User)controllerConnection.getCurrentUser();    
%>


<% if(currentUser.isAdministrator()) { %>
<ul>
    <li><html:link page="/ManageDepartment.do"><bean:message key="mars.manageDepartment" /></html:link></li>
    <li><html:link page="/ManageUser.do"><bean:message key="mars.manageUser" /></html:link></li>
    <li><html:link page="/ManageGroup.do"><bean:message key="mars.manageGroup" /></html:link></li>
    <li><html:link page="/ManageProfilesShow.do?fromMenue=true"><bean:message key="mars.myProfiles" /></html:link></li>
    <li><html:link page="/MyProperties.do"><bean:message key="mars.myProperties" /></html:link></li>
</ul>
<%
} else { %>
<ul>
    <li><html:link page="/ManageProfilesShow.do"><bean:message key="mars.myProfiles" /></html:link></li>
    <li><html:link page="/MyProperties.do"><bean:message key="mars.myProperties" /></html:link></li>
</ul>
<%} %>