<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/tags/struts-tiles" prefix="tiles" %>
<%@ page language="java" %>
<%@ include file="redirectInclude.jsp"%>
<html:form action="/pages/myProperties-submit">
    <html:hidden name="myPropertiesForm" property="faction" value="showProperties"/>
    <html:hidden property="action"/>
    <html:hidden name="myPropertiesForm" property="dispatch" value="error"/>
    <html:errors/>
    
    <fieldset>
        <legend style="color:black"><bean:message key='mars.myProperties' /></legend>
        <div>
            <label><bean:message key='label.username' /></label>
            <bean:write name="myPropertiesForm" property="username"/>
        </div>
        <div>
            <label><bean:message key='label.department' /></label>
            <bean:write name="myPropertiesForm" property="department"/>
        </div>           
        <div>
            <label><bean:message key='label.firstname' /></label>
            <html:text property="firstname" errorStyleClass="error"  size="60"/>
        </div>
        <div>
            <label><bean:message key='label.lastname' /></label>
            <html:text property="lastname" errorStyleClass="error" size="60" />
        </div>
        <div>
            <label><bean:message key='label.email' /></label>
            <html:text property="email" errorStyleClass="error"  size="60"/>
        </div>
        <div>
            <label for="expirationDate"><bean:message key='label.expirationdate' /></label>
            <bean:write name="myPropertiesForm" property="expirationDate"  />
        </div>
        <div>
            <label for="profile"><bean:message key='label.defaultprofile' /></label>
            <bean:write name="myPropertiesForm" property="defaultProfile"/>
        </div>
        <div>
            <label for="quota"><bean:message key='label.quota' /></label>
            <bean:write name="myPropertiesForm" property="quota" />
        </div>
        <div>
            <label></label>
            <html:radio property="superDuperRepeat" value="on">
            	<bean:message key="myProperiesEdit.label.superDuperRepeatOn" />
           	</html:radio>
        </div>
        <div>
            <label></label>
            <html:radio property="superDuperRepeat" value="off">
            	<bean:message key="myProperiesEdit.label.superDuperRepeatOff" />
            </html:radio>
        </div>
    </fieldset>
	
    <fieldset>
        <div class="submit">
	    <html:submit property="submit" onclick="setDispatch('changeProperties');"><bean:message key="button.save" /></html:submit>
            <html:submit property="submit" onclick="setDispatch('changePWD');"><bean:message key="button.changepwd" /></html:submit>
        </div>
    </fieldset>
   
</html:form>
