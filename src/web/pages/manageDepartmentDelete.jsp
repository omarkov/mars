<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/tags/display" prefix="display" %>
<%@ page language="java" %>
<%@ page import="server.domain.Department"%>
<%@ include file="redirectInclude.jsp"%>

<html:form action="/pages/manageDepartmentDelete-submit">
	<%	Department selectedDepartment = (Department)request.getSession().getAttribute("selectedDepartment");%>
	<html:hidden name="manageDepartmentDelete" property="dispatch" value="error"/>

	<h2><bean:message key="manageDepartment.label.delete" /></h2>
	<table>
        <thead>
			<tr>
                <th><bean:message key="manageDepartment.label.departmentname" /></th>
                <th><bean:message key="label.comment" /></th>
                <th><bean:message key="label.abbreviation" /></th>
            </tr>
		</thead>	
            <tr>
                <td><%= selectedDepartment.getName()%></td>
                <td><%= selectedDepartment.getComment() %></td>
                <td><%=selectedDepartment.getAbbreviation() %></td>
            </tr>
	</table>
	<div class="submit">
		<html:submit 
			property="submit" 
			onclick="setDispatch('delete');">
			<bean:message key="button.yesdelete" />
		</html:submit>
		<html:submit 
			property="submit" 
			onclick="setDispatch('cancel');">
			<bean:message key="button.cancel" />
		</html:submit>
	</div>
</html:form>

