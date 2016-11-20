<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/tags/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/tags/display" prefix="display" %>
<%@ page language="java" %>
<%@ include file="redirectInclude.jsp"%>
<fieldset style="color:gray">
	<legend style="color:black">
		<bean:message key="mars.legalNotice" />
	</legend>
	<div>
		<h2 style="color:gray"><bean:message key="mars.legalNotice01" /></h2>
		<bean:message key="mars.legalNotice02" />
	</div>
	<div style="font-size:80%">
		<bean:message key="mars.legalNotice03" />
	</div>
	<!-- Contact to OIC-->	
	<div>
		<bean:message key="mars.legalNotice04" />
	</div>

	<!-- Contact person from project mar-s and additional information -->
	<div style="font-size:80%">
		<bean:message key="mars.legalNotice05" />
	</div>
	<div>
		<bean:message key="mars.legalNotice06" />
	</div>	
        <br> <br> <br>
	<div style="font-size:80%">
		<bean:message key="mars.legalNotice07" />
	</div>

	<bean:message key="mars.legalNotice08" />
				
			
</fieldset>