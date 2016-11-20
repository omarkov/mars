<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/tags/struts-tiles" prefix="tiles" %>
<%@ page language="java" %>
<%@ include file="redirectInclude.jsp"%>
<html:html locale="true">
    <head>
        <title><bean:message key="mars.title" /></title>
        <link href="<html:rewrite page="/css/main.css" />" rel="stylesheet" title="standard" type="text/css"  />
        <link rel="alternate stylesheet" type="text/css" href="/mars/css/bluegray.css" title="bluegray" media="screen">
				<link rel="alternate stylesheet" type="text/css" href="/mars/css/nocss.css" title="nocss" media="screen">
        <html:base></html:base>
 	<link rel="SHORTCUT ICON" href="/mars/images/mars.ico">
    <script src="sorttable.js"></script>
    </head>
    <body>
    <script src="javaScripts.js"></script>
    <script src="cookies.js"></script>
    <script src="changestyle.js"></script>
	<script type="text/javascript" src="datechooser.js"></script>

        
        <div id="main">
            <div id="header">
                <tiles:insert attribute="header" ></tiles:insert>
            </div>
            <logic:present name="user">
            	<div id="menu">
                	<tiles:insert attribute="menu" ></tiles:insert>
            	</div>
            	<div id="content">
                	<html:errors property="errors"/>
                	<tiles:insert attribute="content" ></tiles:insert>
            	</div>
	        </logic:present> 
			<logic:notPresent name="user">
            	<div id="content">
					<h3>
						<bean:message key="errors.notLoggedIn"/>
						<html:link page="/index.jsp">LOGIN</html:link>
					</h3>
            	</div>    	
			</logic:notPresent>
        </div>
        <div id="footer">
        		<tiles:insert attribute="footer" ></tiles:insert>
        </div>
    </body>
</html:html>
