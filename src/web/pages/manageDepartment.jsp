<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/tags/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/tags/display" prefix="display" %>
<%@ page language="java" %>
<%@ include file="redirectInclude.jsp"%>


<html:form action="/pages/manageDepartment-submit">
   <html:hidden name="manageDepartmentForm" property="dispatch" value="error" /> 
	
	<fieldset>
	    <legend style="color:black"><bean:message key="manageDepartment.legend" /></legend>
	    <display:table requestURI="/ManageDepartment.do" 
	    			   id="department" 
	    			   name="manageDepartmentForm.departments" 
	    			   decorator="web.java.DepartmentTableDecorator">
	        <display:column property="name" titleKey="table.department.name" />
	        <display:column property="abbreviation" titleKey="table.department.abbrivation"/>
	        <display:column property="link" titleKey="table.department.option" />
	    </display:table>
	</fieldset>
	
    <fieldset>
        <div class="submit">
            <html:submit property="submit" onclick="setDispatch('new');"><bean:message key="button.newDepartment" />
            </html:submit>
        </div>
    </fieldset>  
</html:form>
