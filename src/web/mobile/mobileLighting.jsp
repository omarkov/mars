<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/tags/struts-tiles" prefix="tiles" %>

<%@ page import="server.domain.User" %>
<%@ page import="web.java.Constants" %>
<%@ page import="web.java.LoginForm" %>
<%@ page import="web.java.MobileLightingForm" %>
<%@ page import="web.java.controller.ControllerConnection" %>
<%@ page import="java.security.SecureRandom" %>
<%
    SecureRandom prng = SecureRandom.getInstance("SHA1PRNG");  
    String randomNum = String.valueOf(new Integer(prng.nextInt()).intValue());
	String base = "/mobile/mobileLighting-submit.do?";
	String varString="&var=" + randomNum;
        String link1 = base + "dispatch=MOOD1" + varString;
	String link2 = base + "dispatch=MOOD2" + varString;
	String link3 = base + "dispatch=MOOD3" + varString;
	String link4 = base + "dispatch=MOOD4" + varString;
	String link5 = base + "dispatch=MOOD5" + varString;
	
%>
<br>

<html:form action="/mobile/mobileLighting-submit">
  <html:hidden property="dispatch" value="error"/>
  <bean:message key="mobile.module.LIGHT.Header"/>    
  <table align="left" text-align="center">
    <tr><td align="left"><bean:message key="mobile.module.LIGHT.Red"/></td>
    	<td align="left"><html:text property="red" size="10"/></td></tr>
    <tr><td align="left"><bean:message key="mobile.module.LIGHT.Green"/></td>
    	<td align="left"><html:text property="green" size="10"/></td></tr>
    <tr><td align="left"><bean:message key="mobile.module.LIGHT.Blue"/></td>
    	<td align="left"><html:text property="blue" size="10"/></td></tr>
    <tr><td align="left"><html:image property="submit" src="/mars/images/enter.gif"/></td>
        <td align="left"><html:link page="<%=link5 %>"><html:img src="/mars/images/reset.gif"/></html:link></td>      
    </tr>
    <tr><td align="left"><bean:message key="mobile.module.LIGHT.Mood"/></td>
        <td align="left">
    <% 
      String mood = ((MobileLightingForm)request.getAttribute("mobileLightingForm")).getMood();
    %>
     <html:link page="<%=link1%>">Arctic Winter</html:link><br>
     <html:link page="<%=link2%>">Moulin Rouge</html:link><br>
     <html:link page="<%=link3%>">Saphire Ocean</html:link><br>
     <html:link page="<%=link4%>">Seven of Nine</html:link><br>
     <html:link page="<%=link5%>">Disco Fever</html:link>
    </td></tr>
    
 
  </table>
</html:form>
