<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/tags/struts-tiles" prefix="tiles" %>

<%@ page import="server.domain.User" %>
<%@ page import="web.java.Constants" %>
<%@ page import="web.java.controller.ControllerConnection" %>
<%@ page import="java.security.SecureRandom" %>
<br>
<br>
<html:form action="/mobile/mobileMedia-submit">  
  <%
    SecureRandom prng = SecureRandom.getInstance("SHA1PRNG");  
    String randomNum = String.valueOf(new Integer(prng.nextInt()).intValue());
	String base = "/mobile/mobileMedia-submit.do?";
	String varString="&var=" + randomNum;
    String linkUp = base + "dispatch=remoteUp" + varString;
	String linkDown = base + "dispatch=remoteDown" + varString;
	String linkLeft = base + "dispatch=remoteLeft" + varString;
	String linkRight = base + "dispatch=remoteRight" + varString;
	String linkESC = base + "dispatch=remoteESC" + varString;
	String linkOK = base + "dispatch=remoteOK" + varString;
	String linkVolUp = base + "dispatch=volumeUp" + varString;
	String linkVolDown = base + "dispatch=volumeDown" + varString;
  %>	

  <table align="center" text-align="center">
    <tr>
      <td align="center"> </td><!--blank-->
      <td align="center"><html:link page="<%=linkUp %>"><html:img src="/mars/images/up.gif"/></html:link></td>
      <td align="center"> </td><!--blank-->
      <td align="center"><html:link page="<%=linkVolUp %>"><html:img src="/mars/images/up.gif"/></html:link></td>
    </tr>
    <tr>
      <td align="center"><html:link page="<%=linkLeft %>"><html:img src="/mars/images/left.gif"/></html:link></td>
      <td align="center"><html:link page="<%=linkOK %>"><html:img src="/mars/images/enter.gif"/></html:link><br/>
      <html:link page="<%=linkESC %>"><html:img src="/mars/images/esc.gif"/></html:link></td>
      <td align="center"><html:link page="<%=linkRight %>"><html:img src="/mars/images/right.gif"/></html:link></td>
	  <td align="center"><bean:message key="mobile.media.volume"/></td>
    </tr>
    <tr>
      <td align="center"> </td><!--blank-->
      <td align="center"><html:link page="<%=linkDown %>"><html:img src="/mars/images/down.gif"/></html:link></td>
      <td align="center"> </td><!--blank-->
      <td align="center"><html:link page="<%=linkVolDown %>"><html:img src="/mars/images/down.gif"/></html:link></td>
    </tr>
  </table>
</html:form>
