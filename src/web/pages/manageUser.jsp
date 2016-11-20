<%@ taglib uri="/tags/struts-bean" prefix="bean"%>
<%@ taglib uri="/tags/struts-html" prefix="html"%>
<%@ taglib uri="/tags/struts-logic" prefix="logic"%>
<%@ taglib uri="/tags/display" prefix="display" %>
<%@ include file="redirectInclude.jsp"%>

<html:form action="/pages/manageUser-submit">
    <fieldset>
    <legend style="color:black"><bean:message key="manageUser.legend" /></legend>
    <html:hidden name="manageUser" property="dispatch" value="error"/>
    <display:table requestURI="/ManageUser.do" id="user" name="manageUserForm.users" decorator="web.java.UserTableDecorator">
        <display:column property="firstName" titleKey="manageUser.table.firstName" />
        <display:column property="lastName" titleKey="manageUser.table.lastName" />
        <display:column property="userLoginID" titleKey="manageGroupEdit.table.userLoginID" />
        <display:column property="expirationDate" titleKey="manageUser.table.expirationDate" /> 
        <display:column property="link" titleKey="manageUser.table.option" />
    </display:table>
    </fieldset>
    <fieldset>
       <div class="submit">
            <html:submit property="submit" onclick="setDispatch('new');"><bean:message key="button.newUser" /></html:submit> 
       </div>     
    </fieldset>
</html:form>
