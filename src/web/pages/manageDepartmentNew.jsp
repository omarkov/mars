<%@ taglib uri="/tags/struts-bean" prefix="bean"%>
<%@ taglib uri="/tags/struts-html" prefix="html"%>
<%@ taglib uri="/tags/struts-logic" prefix="logic"%>
<%@ taglib uri="/tags/display" prefix="display"%>
<%@ page language="java"%>
<%@ page import="server.domain.Department"%>
<%@ include file="redirectInclude.jsp"%>
<script src="sorttable.js"></script>
<html:form action="/pages/manageDepartmentNew-submit">
	<html:hidden property="action" />
	<html:hidden name="manageDepartmentNew" property="dispatch"
		value="error" />
	<html:errors />
	<fieldset>
		<%Department selectedDepartment = (Department) request.getSession().getAttribute("selectedDepartment");%>
		<legend style="color:black"> 
			<bean:message key="manageDepartment.button.newdepartment" />
		</legend>

		<div>
			<label for="departmentName"> 
				<bean:message key="manageDepartment.label.departmentname" /> 
			</label> 
			
			<html:text property="name" size="60" errorStyleClass="error" />
		</div>
		
		<div>
			<label for="date">
				<bean:message key="label.abbreviation" />
			</label>
			
			<html:text property="abbreviation" size="6" errorStyleClass="error" />
		</div>
		
		<div>
			<label for="comment">
				<bean:message key="label.comment" />
			</label>
		
			<html:textarea property="comment" cols="46" />
		</div>
	</fieldset>
	
	
	<fieldset>
		<legend style="color:black">
			<bean:message key="label.selecttoadddepartment" />
		</legend>
	

		<display:table requestURI="/ManageDepartmentNew.do" id="user" name="manageDepartmentNewForm.users">
			<display:column>	
				<html:multibox property="selectedUsers"> 
					<bean:write name="user" property="id" />
				</html:multibox>
			</display:column>
			<display:column property="firstName" titleKey="manageDepartmentNew.table.firstName" />
			<display:column property="lastName" titleKey="manageDepartmentNew.table.lastName" />
         <display:column property="userLoginID" titleKey="manageGroupEdit.table.userLoginID" />
         <display:column property="expirationDate" titleKey="manageGroupEdit.table.expirationDate" />
 			<display:column property="comment" titleKey="manageDepartmentNew.table.comment" />
		</display:table>
	</fieldset>

	<fieldset>
		<div class="submit">
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
