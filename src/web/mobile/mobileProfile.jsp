<%@ taglib uri="/tags/struts-bean" prefix="bean"%>
<%@ taglib uri="/tags/struts-html" prefix="html"%>
<%@ taglib uri="/tags/struts-logic" prefix="logic"%>
<%@ taglib uri="/tags/display" prefix="display" %>

<%@ page import="server.domain.*" %>
<%@ page import="web.java.Constants" %>
<%@ page import="web.java.controller.ControllerConnection" %>

<%
        ControllerConnection controllerConnection = (ControllerConnection)
           request.getSession().getAttribute(Constants.CONNECTION);
        User selectedUser = controllerConnection.getCurrentUser();
        String profileName = "none";

        SmartRoomProfile defaultProfile = selectedUser.getDefaultProfile();
        if(defaultProfile != null) profileName = defaultProfile.getName();
        
%>

<html:form action="/pages/manageProfilesShow-submit.do">
    <html:hidden name="manageProfilesShow" property="dispatch" value="error"/>

   
    <br>
    
    <display:table requestURI="/ManageProfilesShow.do" id="profile" name="manageProfilesShowForm.currentUserProfiles" defaultsort="1" defaultorder="descending" decorator="web.java.ProfileTableDecoratorMobile">
        <display:column property="name" titleKey="manageProfilesShow.table.name"/>
        <display:column property="link" title="Actions" />
    </display:table>    

</html:form>
