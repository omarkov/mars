<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/tags/struts-tiles" prefix="tiles" %>
<%@ page language="java" %>
<div align="center">
<html:link page="/MobileLighting.do"><bean:message key="mobile.lighting" /></html:link> |
<html:link page="/MobileMedia.do"><bean:message key="mobile.media" /></html:link> |
<html:link page="/MobileProfile.do"><bean:message key="mobile.profile" /></html:link> |
<html:link page="/MobileLogoff.do"><bean:message key="mobile.logoff" /></html:link> 
</div>
