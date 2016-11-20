<%@ taglib uri="/tags/struts-bean" prefix="bean"%>
<%@ taglib uri="/tags/struts-html" prefix="html"%>
<%@ taglib uri="/tags/struts-logic" prefix="logic"%>
<%@ taglib uri="/tags/display" prefix="display" %>
<%@ page import="server.domain.SmartRoomProfile"%>
<%@ include file="redirectInclude.jsp"%>

<html:form action="/pages/manageProfilesDelete-submit">
	
	 <%SmartRoomProfile selectedProfile = (SmartRoomProfile)request.getSession().getAttribute("selectedProfile"); %>
	 <html:hidden name="manageGroupDelete" property="profilesIDToDelete" value="<%=selectedProfile.getId().toString() %>" />	
    <html:hidden name="manageProfilesDelete" property="dispatch" value="error"/>

    <h2><bean:message key="manageProfiles.label.delete"/> </h2>

	<table>
		<thead>
	       <tr>
	           <th><bean:message key="manageProfiles.profilename" /></th>
	           <th><bean:message key="label.comment" /></th>
	       </tr>
		</thead>   
       <tr>
           <td><%=selectedProfile.getName() %></td>   
           <td><%=selectedProfile.getComment() %></td>
       </tr>
	</table>
     
    <div class="submit">
      <html:submit property="submit" onclick="setDispatch('delete');">
          <bean:message key="button.yesdelete" />
      </html:submit>   
 
      <html:submit property="submit" onclick="setDispatch('cancel');">
			<bean:message key="button.cancel" />
		</html:submit>
    </div>
</html:form>
