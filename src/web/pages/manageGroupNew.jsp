<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/tags/display" prefix="display" %>
<%@ page import="server.domain.GroupMars"%>
<%@ page language="java" %>
<%@ include file="redirectInclude.jsp"%>

<html:form action="/pages/manageGroupNew-submit">
    <html:hidden property="action" />
    <html:hidden name="manageGroupNew" property="dispatch" value="error"/>
    <html:errors />
    
    <fieldset>
	<%GroupMars selectedGroup = (GroupMars) request.getSession().getAttribute("selectedGroup"); %>

    <legend style="color:black"><bean:message key="manageGroup.button.newgroup" /></legend>
	<div>
        <label for="groupName"><bean:message key="manageGroup.label.groupname" /></label>
        <html:text property="name" errorStyleClass="error"/>
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
    
    </div>
    <div>
        <label for="comment"><bean:message key="label.comment" /></label>
        <html:textarea property="comment" cols="46" />
    </div>
	</fieldset> 

	<fieldset>
        <legend style="color:black"><bean:message key="label.selecttoaddgroup" /></legend>
        <display:table requestURI="/ManageGroupNew.do" id="user" name="manageGroupNewForm.users" >
            <display:column><html:multibox property="selectedUsers"><bean:write name="user" property="id"/> </html:multibox></display:column>
            <display:column property="firstName" titleKey="manageGroupEdit.table.firstName" />
            <display:column property="lastName" titleKey="manageGroupEdit.table.lastName" />
            <display:column property="department.abbreviation" titleKey="table.department.name" />
            <display:column property="userLoginID" titleKey="manageGroupEdit.table.userLoginID" />
            <display:column property="expirationDate" titleKey="manageGroupEdit.table.expirationDate" /> 
            <display:column property="comment" titleKey="manageGroupEdit.table.comment" />   
        </display:table >
	</fieldset>
	
	<fieldset>
	<div class="submit">	
	<html:submit property="submit" onclick="setDispatch('save');">
		<bean:message key="button.save" />
	</html:submit> 
	
	<html:reset>
		<bean:message key="button.reset" /> 
	</html:reset> 
	
	<html:button property="cancel" onclick="javascript:window.location='/mars/ManageGroup.do';">
		<bean:message key="button.cancel" />
	</html:button>
	
	</div>
	</fieldset>
</html:form>
