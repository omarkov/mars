<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/tags/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/tags/display" prefix="display" %>
<%@ page language="java" %>
<%@ include file="redirectInclude.jsp"%>
<html:form action="/pages/manageGroup-submit">
<fieldset>
    <legend style="color:black"><bean:message key="manageGroup.legend" /></legend>
    <display:table requestURI="/ManageGroup.do" id="group" name="manageGroupForm.groups" decorator="web.java.GroupTableDecorator">
        <display:column property="name" titleKey="manageGroup.table.name"/>
        <display:column property="expirationDate" titleKey="manageGroup.table.expirationDate" />
        <display:column property="comment" titleKey="manageGroup.table.comment" />
        <display:column property="link" titleKey="manageGroup.table.option" />
    </display:table>
</fieldset>
    <html:hidden name="manageGroupForm" property="dispatch" value="error" /> 
    <fieldset>
        <div class="submit">
            <html:submit property="submit" onclick="setDispatch('sendEmail');"><bean:message key="button.sendEmail" />

            </html:submit>
            
            <html:submit property="submit" onclick="setDispatch('new');"><bean:message key="button.newGroup" />
            </html:submit>
        </div>
    </fieldset>
    
</html:form>

