<%@ taglib uri="/tags/struts-bean" prefix="bean"%>
<%@ taglib uri="/tags/struts-html" prefix="html"%>
<%@ taglib uri="/tags/struts-logic" prefix="logic"%>
<%@ taglib uri="/tags/display" prefix="display" %>
<%@ include file="redirectInclude.jsp"%>
<form name="" id="" method="post" action="">

    <fieldset>
        <legend>Deleted Users</legend>
        <!-- TODO Anfrage der vorhandenen User -->
        <div>
            <input name="User" type="checkbox" value="user2" />&nbsp; User2  (Martin Max) &nbsp; &nbsp;&nbsp; &nbsp;
            <input type="button" name="back" value="Reactivate user (for 2 Weeks)"/>
        <div>
        </div>	
            <input name="User" type="checkbox" value="user3" />&nbsp; User3  (Martin Min) &nbsp; &nbsp;&nbsp; &nbsp;
            <input type="button" name="back" value="Reactivate user (for 2 Weeks)"/>
        <div>
        </div>	
            <input name="User" type="checkbox" value="user4" />&nbsp; User4  (Martin Avg) &nbsp; &nbsp;&nbsp; &nbsp;
            <input type="button" name="back" value="Reactivate user (for 2 Weeks)"/>
        </div>
    </fieldset>

    <fieldset>
<!-- TODO richtige Verlinkung <% %> -->
        <div class="submit">
            <input type="button" name="back" value="Cancel and Back" id="back" onClick="javascript:history.back();">
            <input type="button" name="purgeUsers" value="Purge Selected Users" id="purgeUsers" onClick="javascript:window.location='manageUser.jsp';">
        </div>
    </fieldset>

</form>