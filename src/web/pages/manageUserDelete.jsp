<%@ taglib uri="/tags/struts-bean" prefix="bean"%>
<%@ taglib uri="/tags/struts-html" prefix="html"%>
<%@ taglib uri="/tags/struts-logic" prefix="logic"%>
<%@ taglib uri="/tags/display" prefix="display" %>
<%@ page import="server.domain.User"%>
<%@ page import="java.util.Date" %>
<%@ include file="redirectInclude.jsp"%>

<html:form action="/pages/manageUserDelete-submit">
    <%User selectedUser = (User)request.getSession().getAttribute("selectedUser");%>
    <html:hidden name="manageUserDelete" property="userIDToDelete" value="<%=selectedUser.getUserLoginID() %>" />
    <html:hidden name="manageUserDelete" property="dispatch" value="error"/>

	<h2><bean:message key="manageUser.label.delete" /></h2>

    <table>
		<thead>
	        <tr>
	            <th><bean:message key="label.firstname" /></th>
	            <th><bean:message key="label.lastname" /></th>
	            <th><bean:message key="label.expirationdate" /></th>
	            <th><bean:message key="label.comment" /></th>
	        </tr>
        </thead>
		<tr>
            <td><%= selectedUser.getFirstName()%></td>
            <td><%= selectedUser.getLastName() %></td>
            <% Date expDate  = selectedUser.getExpirationDate();
               if (expDate != null){%>
                	<td><%= selectedUser.getExpirationDate() %></td> 
            <%}
               else
          	{%>
                	<td><bean:message key="manageUserNew.unlimitedRuntime" /></td>
            <%}%>

            <td><%= selectedUser.getComment() %></td>
        </tr>
    </table>
    	<div class="submit">
		    <html:submit property="submit" onclick="setDispatch('delete');">
		    	<bean:message key="button.yesdelete"/>
		    </html:submit>
		    <html:submit property="submit" onclick="setDispatch('cancel');">
		    	<bean:message key="button.cancel" />
		    </html:submit>
		</div>
</html:form>
