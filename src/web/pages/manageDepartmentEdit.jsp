<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/tags/display" prefix="display" %>
<%@ page language="java" %>
<%@ page import="server.domain.Department"%>
<%@ include file="redirectInclude.jsp"%>
<html:form action="/pages/manageDepartmentEdit-submit">
    <html:hidden property="action" />
    <html:hidden name="manageDepartmentEdit" property="dispatch" value="error"/>
    <html:errors />
    <fieldset>
        <%Department selectedDepartment = (Department) request.getSession().getAttribute("selectedDepartment"); %>

        <legend style="color:black"><bean:message key="manageDepartment.button.editdepartment"/></legend>
        <div>
            <label for="departmentName"><bean:message key="manageDepartment.label.departmentname" /></label>
            <html:text property="name" size="60" errorStyleClass="error" value="<%=selectedDepartment.getName()%>" disabled="true" />
        </div>
        <div>
            <label for="departmentAbbreviation"><bean:message key="manageDepartment.label.abbreviation" /></label>
            <html:text property="abbreviation" size="60"  errorStyleClass="error" value="<%=selectedDepartment.getAbbreviation()%>" />
        </div>
        <div>
            <label for="comment"><bean:message key="label.comment" /></label>
            <html:textarea property="comment" cols="46" value="<%=selectedDepartment.getComment()%>" />
        </div>
    </fieldset>
    <fieldset>	

        <legend style="color:black"><bean:message key="label.members" /></legend>

        <display:table requestURI="/ManageDepartmentEdit.do" id="user" name="manageDepartmentEditForm.users" decorator="web.java.UserTableDecorator">
            <display:column ><html:multibox property="selectedUsers"><bean:write name="user" property="id"/> </html:multibox></display:column>
            <display:column property="firstName" titleKey="manageDepartmentEdit.table.firstName" />
            <display:column property="lastName" titleKey="manageDepartmentEdit.table.lastName"/>
            <display:column property="userLoginID" titleKey="manageDepartmentEdit.table.userLoginID"/>
            <display:column property="expirationDate" titleKey="manageDepartmentEdit.table.expirationDate"/>
            <display:column property="comment" titleKey="manageDepartmentEdit.table.comment"/>                    
        </display:table>
    </fieldset>
    <fieldset>
    <div class="submit">
	    <html:submit property="submit" onclick="setDispatch('delete');">
	            <bean:message key="button.deleteSelectedUsersDepartment" />
	    </html:submit> 
	            <html:submit property="submit" onclick="setDispatch('add');">
	            <bean:message key="button.addUsersToDepartment" />
	    </html:submit> 
	
	    <html:submit property="submit" onclick="setDispatch('save');">
	            <bean:message key="button.save" />
	    </html:submit> 
	    
		 <html:reset>
				<bean:message key="button.reset" /> 
		 </html:reset> 
		 
		 <html:button property="cancel" onclick="javascript:window.location='/mars/ManageDepartment.do';">
				<bean:message key="button.cancel" />
		 </html:button>

    </div>
    </fieldset>
</html:form>
