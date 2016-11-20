<%@ taglib uri="/tags/struts-bean" prefix="bean"%>
<%@ taglib uri="/tags/struts-html" prefix="html"%>
<%@ taglib uri="/tags/struts-logic" prefix="logic"%>
<%@ taglib uri="/tags/display" prefix="display" %>
<%@ include file="redirectInclude.jsp"%>
<!-- TODO Form Action <% %> -->
<form name="" id="" method="post" action="">
    <h2>Do you really want to assignn the TagId 124 to this User?
        This TagId is currently owned by User schleidl.</h2>
    <!-- TODO der Gruppenname <% %> -->
    <input type="submit" name="assignTagIdToUser" value="Yes, assign 124 to User schleidl" id="assignTagIdToUser">
   <input type="reset" name="assignTagIdToUser" value="No, cancel and back" id="reset" onclick="javascript:history.back();">
</form>
