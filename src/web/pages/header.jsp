<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/tags/struts-tiles" prefix="tiles" %>
<%@ page language="java" %>
<%@ include file="redirectInclude.jsp"%>

<div><html:link page="/Main.do" onclick="javascript:window.open('http://oic.fhg.de/deutsch/')"><html:img src="/mars/images/spacer2.gif" /></html:link></div>
<div><html:link page="/Main.do"><html:img src="/mars/images/spacer2.gif" /></html:link></div>

<div>
<logic:present name="user">
    <html:link forward="logoff">           
            <bean:message key="mars.logout" />
            <bean:write name="user" property="username"/>
            <html:img src="/mars/images/logout.gif" alt="Logout" title="Logout" />
    </html:link>
</logic:present> 
</div>