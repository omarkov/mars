<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/tags/struts-tiles" prefix="tiles" %>
<%@ page language="java" %>

<html:html locale="true">
    <head>
        <title><bean:message key="mars.title" /></title>
        <link href="<html:rewrite page="/css/mobile.css" />" rel="stylesheet" type="text/css"  />
        <meta name="HandheldFriendly" content="true"><meta http-equiv="content-type" content="text/html; charset=UTF-8">
        <html:base></html:base> 	
    </head>

    <body>
    <script src="../pages/javaScripts.js"></script>
        
            <div id="menu">
                <tiles:insert attribute="menu" ></tiles:insert>
            </div>
            <logic:present name="user">
	            <div id="content">
    	            <html:errors property="errors"/>
        	        <tiles:insert attribute="content" ></tiles:insert>
            	</div>
			</logic:present>
			<logic:notPresent name="user">
            	<div id="content">
					<h3>
						<bean:message key="errors.notLoggedIn"/>
						<html:link page="/mobile/index.jsp">LOGIN</html:link>
					</h3>
            	</div>
			</logic:notPresent>
			
    </body>
</html:html>
