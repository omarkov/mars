<%@ taglib uri="/tags/struts-bean" prefix="bean"%>
<%@ taglib uri="/tags/struts-html" prefix="html"%>
<%@ taglib uri="/tags/struts-logic" prefix="logic"%>
<%@ taglib uri="/tags/display" prefix="display" %>

<%@ page import="server.domain.User" %>
<%@ page import="web.java.Constants" %>
<%@ page import="web.java.LoginForm" %>
<%@ page import="web.java.controller.ControllerConnection" %>

<%  
    ControllerConnection controllerConnection = (ControllerConnection)
    request.getSession().getAttribute(Constants.CONNECTION);
    User currentUser = (User)controllerConnection.getCurrentUser();    
    String profileName = "No Default Profile";
    if(currentUser.getDefaultProfile() != null)
        profileName = currentUser.getDefaultProfile().getName();
%>
<br>
<br>
<div align="center">
<p><bean:message key="mobile.welcome" /></p>
<p><b><%=currentUser.getUserLoginID() %></b></p>
<p><bean:message key="mobile.mars" /></p>
<br>
<br>
<p><bean:message key="mobile.currentProfile" /></p> 
<b><%=profileName %><b>
</div>
