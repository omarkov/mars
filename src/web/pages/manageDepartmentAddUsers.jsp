<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/tags/display" prefix="display" %>
<%@ page language="java" %>
<%@ include file="redirectInclude.jsp"%>
<html:form action="/pages/manageDepartmentAddUsers-submit">
    <html:hidden property="action" />
    <html:hidden name="manageDepartmentAddUsers" property="dispatch" value="error"/>

    <fieldset>
        <legend style="color:black"><bean:message key="label.user" /></legend>
        <display:table requestURI="/ManageDepartmentAddUsers.do" id="user" name="manageDepartmentAddUsersForm.usersNotInDepartment" >
            <display:column><html:multibox property="selectedUsers"><bean:write name="user" property="id"/></html:multibox></display:column>
            <display:column property="firstName" titleKey="manageDepartmentAddUser.table.fistName" />
            <display:column property="lastName" titleKey="manageDepartmentAddUser.table.lastName"/>
            <display:column property="userLoginID" titleKey="manageGroupEdit.table.userLoginID"/>
            <display:column property="expirationDate" titleKey="manageGroupEdit.table.expirationDate" />
            <display:column property="comment" titleKey="manageDepartmentAddUser.table.comment"/>
        </display:table> 
    </fieldset>
	
    <fieldset>
    <div class="submit">
        <html:submit property="submit" onclick="setDispatch('add');">
            <bean:message key="button.addSelectedUsers" />
        </html:submit> 
        <html:submit property="submit" onclick="setDispatch('back');">
            <bean:message key="button.back" />
        </html:submit>
    </div>
    </fieldset>
</html:form>
