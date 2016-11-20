<%@ taglib uri="/tags/struts-bean" prefix="bean"%>
<%@ taglib uri="/tags/struts-html" prefix="html"%>
<%@ taglib uri="/tags/struts-logic" prefix="logic"%>
<%@ taglib uri="/tags/display" prefix="display" %>
<%@ include file="redirectInclude.jsp"%>
<!-- TODO Form Action <% %> -->
<form name="" id="" method="post" action="">
    <h2>Do you really want to deactivate this User</h2>
    <!-- TODO der Gruppenname <% %> -->
    <input type="submit" name="deactivateUser" value="Yes, deactivate User" id="deactivateUser">
    <input type="reset" name="deleteUser" value="No, cancel and back" id="reset" onclick="javascript:history.back();">
</form>
