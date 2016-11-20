<%@ taglib uri="/tags/struts-bean" prefix="bean"%>
<%@ taglib uri="/tags/struts-html" prefix="html"%>
<%@ taglib uri="/tags/struts-logic" prefix="logic"%>
<%@ taglib uri="/tags/display" prefix="display" %>
<%@ include file="redirectInclude.jsp"%>
<html:form action="/pages/manageProfiles-submit">
<fieldset>
    <legend style="color:black"><bean:message key="manageProfiles.legend" /></legend>    
    <display:table requestURI="/ManageProfiles.do" id="user" name="manageProfilesForm.users" decorator="web.java.ProfileTableDecorator">
        <display:column property="firstName" titleKey="manageProfiles.table.firstName" />
        <display:column property="lastName" titleKey="manageProfiles.table.lastName" />
        <display:column property="expirationDate" titleKey="manageProfiles.table.expirationDate" />
        <display:column property="link" titleKey="manageProfiles.table.option" />
    </display:table>
</fieldset>
</html:form>