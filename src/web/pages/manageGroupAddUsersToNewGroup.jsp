<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/tags/display" prefix="display" %>
<%@ page language="java" %>
<%@ include file="redirectInclude.jsp"%>
<div id="content">
<html:form action="/pages/manageGroupAddUsersToNewGroup-submit">
	<html:hidden property="action" />

	<fieldset>
		<legend style="color:black"><bean:message key="label.user" /></legend>
		<table>
		<tr>
			<th>
			<th><bean:message key="label.firstname" />
			<th><bean:message key="label.lastname" />
			<th><bean:message key="label.userid" />
			<th><bean:message key="label.emailadress" />
			<th><bean:message key="label.expirationdate" />
			<th><bean:message key="label.comment" />
			<logic:iterate
				name="manageGroupAddUsersToNewGroupForm"
				property="usersNotInGroup"
				id="user"
				indexId="i">
				<tr>
					<td><html:multibox property="selectedUsers"><bean:write name="manageGroupAddUsersToNewGroupForm" property="usersNotInGroup"/> </html:multibox>
					<td><bean:write
						name="user"
						property="firstName" />
					<td><bean:write
						name="user"
						property="lastName" />
					<td><bean:write
						name="user"
						property="userLoginID" />
					<td><bean:write
						name="user"
						property="email" />
					<td><bean:write
						name="user"
						property="expirationDate" />
					<td><bean:write
						name="user"
						property="comment" />
					</td>
					</td>
					</td>
					</td>
					</td>
				</tr>
			</logic:iterate></th>
			</th>
			</th>
			</th>
			</th>
			</th>
			</th>
		</tr>
	</table>
	</fieldset>
	<fieldset>
		<div class="submit">
		<html:submit property="submit">
			<bean:message key="button.addSelectedUsers" />
		</html:submit> 
			<html:submit property="submit">
		<bean:message key="button.back" />
	</html:submit>
		</div>
	
	</fieldset>
</html:form>
