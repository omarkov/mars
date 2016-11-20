<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/tags/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/tags/display" prefix="display" %>
<%@ page language="java" %>
<%@ include file="redirectInclude.jsp"%>
<script src="javaScripts.js"></script>

<html:form action="/pages/sendEmail-submit">
<html:hidden name="sendEmailForm" property="dispatch" value="error"/>
<html:hidden name="sendEmailForm" property="recipient" value=""/>

<fieldset>
	<legend style="color:black"><bean:message key="label.sendEmail" /></legend>	
	<div>
		<html:errors />
	</div>
	<div>

        <display:table requestURI="/SendEmail.do" id="group" name="sendEmailForm.groups" defaultsort="1" defaultorder="descending" >
            <display:column><html:multibox property="selectedGroups"><bean:write name="group" property="id"/> </html:multibox></display:column>
            <display:column property="name" titleKey="sendEmail.table.name" sortable="true" headerClass="sortable"/>
            <display:column property="expirationDate" titleKey="sendEmail.table.expirationDate" sortable="true" headerClass="sortable"/>
            <display:column property="comment" titleKey="sendEmail.table.comment"/>
        </display:table >	
	</div>
	<legend style="color:black"><bean:message key='label.composeMessage' /></legend>
	<div>
		<label><bean:message key="label.subject" /></label>
		<html:text property="subject" size="60"/>
	</div>
	<div>
		<label><bean:message key="label.messageBody" /></label>
		<html:textarea property="messageBody" cols="60" />
	</div>
</fieldset>

<fieldset>
	<div class="submit">	
		<html:submit property="submit" onclick="setDispatch('save');">
			<bean:message key="button.sendEmail" />
		</html:submit> 
		
		<html:reset>
			<bean:message key="button.reset" /> 
		</html:reset>

		<html:button property="cancel" onclick="javascript:window.location='/mars/ManageGroup.do';">
				<bean:message key="button.cancel" />
		</html:button>

	</div>
</fieldset>

</html:form>
