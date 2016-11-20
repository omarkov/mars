<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@taglib uri="/tags/struts-logic" prefix="logic"%>
<%@ page language="java" %>


<html>
    <head>
        <script src="browser_detect.js"></script>
        <link rel="stylesheet" type="text/css" href="css/main.css" title="standard"/>
        <link rel="alternate stylesheet" type="text/css" href="css/bluegray.css" title="bluegray" media="screen">
        <link rel="alternate stylesheet" type="text/css" href="css/nocss.css" title="nocss" media="screen">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
    
		<script LANGUAGE="JavaScript">
			
		if (window.screen) {
			if(screen.availHeight<300||screen.availWidth<250 && screenHeight<600)
		        {window.location='/mars/mobile/index.jsp'}
		        else
		        {window.location='/mars/Login.do'}
		}
		
		</script>

		<div class="javascript">
			<br>
			<br>
			<bean:message key='mars.noJavaScript'/>
		</div>	
		
    </body>
</html>


