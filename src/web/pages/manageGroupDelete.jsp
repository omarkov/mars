<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/tags/display" prefix="display" %>
<%@ page language="java" %>
<%@ page import="server.domain.GroupMars"%>
<%@ page import="java.util.Date" %>
<%@ include file="redirectInclude.jsp"%>
<html:form
	action="/pages/manageGroupDelete-submit">
	<%	GroupMars selectedGroup = (GroupMars)request.getSession().getAttribute("selectedGroup");%>
	<html:hidden name="manageGroupDelete" property="groupIDToDelete" value="<%=selectedGroup.getId().toString() %>" />
	<html:hidden name="manageGroupDelete" property="dispatch" value="error"/>
	<!-- TODO username <% %> -->
	<h2><bean:message key="manageGroup.label.delete"/></h2>
	<table>
        <thead><tr>
                <th><bean:message key="manageGroup.label.groupname" /></th>
                <th><bean:message key="label.expirationdate" /></th>
                <th><bean:message key="label.comment" /></th>
            </tr>
		</thead>	
            <tr>
                <td><%= selectedGroup.getName()%></td>
                <% Date expDate  = selectedGroup.getExpirationDate();
                	if (expDate != null)
                {%>
                	<td><%= selectedGroup.getExpirationDate() %></td>
                <%}
                else
                {%>
                	<td><bean:message key="manageUserNew.unlimitedRuntime" /></td>
                <%
                }%>
                <td><%= selectedGroup.getComment() %></td>
            </tr>
	</table>
	<fieldset>
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
	</fieldset>	
</html:form>

