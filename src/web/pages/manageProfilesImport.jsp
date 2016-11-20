<%@ taglib uri="/tags/struts-bean" prefix="bean"%>
<%@ taglib uri="/tags/struts-html" prefix="html"%>
<%@ taglib uri="/tags/struts-logic" prefix="logic"%>
<%@ taglib uri="/tags/display" prefix="display" %>
<%@ include file="redirectInclude.jsp"%>
<script src="javaScripts.js"></script>

<html:form action="/pages/manageProfilesImport-submit" enctype="multiformpart/form-data">
<html:hidden name="manageProfilesImportForm" property="dispatch" value="error"/>
    <fieldset>
    	<div>
    		<legend>
    			<bean:message key='label.import'/>
    			<html:errors property="FileEmptyOrNull" />
    		</legend>
    	</div>
        <div>
        	<html:file name="manageProfilesImportForm" property="file" />
        </div> 
        <div>
        	<br>
        </div>
        <div>
		    <html:submit property="submit" onclick="setDispatch('import');">
				<bean:message key="button.import" />
			</html:submit>
			<html:button property="cancel" onclick="javascript:history.back();">
				<bean:message key="button.cancel" />
			</html:button>
        </div>
	</fieldset>
</html:form>