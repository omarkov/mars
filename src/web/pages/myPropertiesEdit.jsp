<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/tags/struts-tiles" prefix="tiles" %>
<%@ page language="java" %>
<%@ include file="redirectInclude.jsp"%>
<html:form  action="/pages/myPropertiesEdit-submit">
    <html:hidden name="myPropertiesEditForm" property="faction" value="default"/>
    <html:hidden name="myPropertiesEditForm" property="dispatch" value="error"/>
    <html:errors />
    <fieldset>
        <legend style="color:black"><bean:message key='button.changepwd' /></legend>
        <bean:message key='myPropertiesEdit.label.enteroldpwd' />
        <div>
            <label><bean:message key='myPropertiesEdit.label.oldpwd' /></label>
            <html:password property="oldpassword" />
        </div>
            <bean:message key='myPropertiesEdit.label.typenewpwd' />
        <div>
            <label><bean:message key='myPropertiesEdit.label.newpwd' /></label>
            <html:password property="newpassword" />
        </div>
        <div>
            <label><bean:message key='myPropertiesEdit.label.retypenewpwd' /></label>
            <html:password property="retypenewpassword" />
        </div>
    </fieldset>

    <fieldset>
        <div class="submit">
            <html:submit property="submit" onclick="setDispatch('changepwd');">
                    <bean:message key="button.changepwd" />
            </html:submit>  
            
            <html:reset>
					<bean:message key="button.reset" /> 
	 			</html:reset> 
	
            <html:button property="cancel" onclick="javascript:window.location='/mars/MyProperties.do';">
					<bean:message key="button.cancel" />
				</html:button>
        </div>
    </fieldset>
</html:form>