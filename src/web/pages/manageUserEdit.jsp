<%@ taglib uri="/tags/struts-bean" prefix="bean"%>
<%@ taglib uri="/tags/struts-html" prefix="html"%>
<%@ taglib uri="/tags/struts-logic" prefix="logic"%>
<%@ taglib uri="/tags/display" prefix="display" %>
<%@ page import="server.domain.User"%>
<%@ page import="server.domain.Identification" %>
<%@ page import="server.domain.LogInSystem" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Hashtable" %>
<%@ page import="web.java.controller.ControllerConnection" %>
<%@ page import="web.java.Constants" %>
<%@ page import="web.java.ManageUserEditForm" %>
<%@ include file="redirectInclude.jsp"%>
<html:form action="/pages/manageUserEdit-submit">
    <html:hidden property="action" />
    <html:hidden name="manageUserEdit" property="dispatch" value="error"/>
    <html:errors />
    <fieldset>
        <%User selectedUser = (User) request.getSession().getAttribute("selectedUser"); %>
	<legend style="color:black"><bean:message key="manageUserEdit.legend" /></legend>
	<div>
            <label for="userName"><bean:message key="label.username" /></label>
            <html:text property="userLoginId" size="60" disabled="true" errorStyleClass="error" />
        </div>
	<div>
            <label for="firstName"><bean:message key="label.firstname" /></label>
            <html:text property="firstName" size="60" errorStyleClass="error" />
        </div>
	<div>
            <label for="lastName"><bean:message key="label.lastname" /></label>
            <html:text property="lastName"size="60" errorStyleClass="error" />
        </div>
	<div>
            <label for="email"><bean:message key="label.email" /></label>
            <html:text property="email" size="60" maxlength="60" errorStyleClass="error" />
        </div>
		<%
		ControllerConnection controllerConnection = 
			(ControllerConnection)request.getSession().
				getAttribute(Constants.CONNECTION);
		
		List logInSystems = controllerConnection.getAllLogInSystems();
		List identifications = controllerConnection.getIdentificationsForUser(selectedUser.getId());
        Hashtable idents = new Hashtable();
       
        for(int i=0; i < identifications.size(); i++)
        {
	   Identification ident = (Identification)identifications.get(i);
           idents.put(ident.getLogInSystem().getName(), ident);
        }
        
		LogInSystem logInSystem;
			
		for (int i = 0; i < logInSystems.size(); i++) {
			logInSystem = (LogInSystem)logInSystems.get(i);
			%><div><%
			Identification identification = (Identification)idents.get(logInSystem.getName());
            String tagID = "";
            if(identification != null) tagID = identification.getTagID();
	   		%>
    	    <label><bean:message key="manageUserEdit.label.LoginSystem" /><%=logInSystem.getName()%></label>
	   		<bean:message key="manageUserEdit.label.LoginSystemID" /><input type="text" 
	   				   name="<%=logInSystem.getName()%>" 
                       <%=logInSystem.getName().equalsIgnoreCase("PDA")?"disabled":""%>
	   				   value="<%=tagID%>" /></div>
	   		<%

		}
		%>

        <div>
            <label for="departments"><bean:message key="manageDepartment.label.select.department" /></label>
		<html:select property="selectedDepartmentID" errorStyleClass="error">
            <html:optionsCollection name="manageUserEditForm" property="departments"/>
        </html:select> 
		</div>
        <div>
            <label for="password"><bean:message key="label.password" /></label>
            <html:password property="password" errorStyleClass="error" size="60"/>
        </div>
        
	<div>
            <label for="isAdmin"><bean:message key="label.admin" /></label>
            <html:checkbox property="isAdmin" />
        </div>
	<div>
            <label for="quota"><bean:message key="label.quota"  /></label>
            <html:text property="quota" size="60" errorStyleClass="error" />MB
        </div>
        <div class="date">
            <label for="date"><bean:message key="label.expirationdate" /></label> 
            <html:radio property="expirationLimit" value="unlimited"><bean:message key="manageUserNew.unlimitedRuntime" /></html:radio>
            <html:radio property="expirationLimit" value="limited"></html:radio>
        
        <input type="hidden" id="expirationDate" value="<%=request.getAttribute("expirationDate") %>" />
        <script type="text/javascript">
            var jsDateChooser = new JavaScriptDateChooser;
            jsDateChooser.JavaScriptDateChooser();
        </script>
        </div><br/>
        <div>
            <label for="comment"><bean:message key="label.comment" /></label>
            <html:textarea property="comment" cols="46" />
        </div>
    </fieldset>
    <fieldset>
		<div class="submit">
          <html:submit property="submit" onclick="setDispatch('save');">
              <bean:message key="button.save" />
          </html:submit>
           
			 <html:reset>
					<bean:message key="button.reset" /> 
			 </html:reset> 
			 
			 <html:button property="cancel" onclick="javascript:window.location='/mars/ManageUser.do';">
					<bean:message key="button.cancel" />
			 </html:button>

		</div>
    </fieldset>
</html:form>
