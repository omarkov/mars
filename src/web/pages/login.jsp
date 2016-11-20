<%@ page language="java" %>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html>
    <head> 
        <title><bean:message key="mars.title" /></title>
        <html:base />
        <link rel="stylesheet" type="text/css" href="../css/main.css" title="standard"/>
        <link rel="alternate stylesheet" type="text/css" href="../css/bluegray.css" title="bluegray" media="screen">
                <link rel="alternate stylesheet" type="text/css" href="../css/nocss.css" title="nocss" media="screen">
    </head>

<body>
        <script src="javaScripts.js"></script>
        <script src="cookies.js"></script>
        <script src="changestyle.js"></script>        
        <div id="main">
                <div id="header"></div>
                <div id="menu"></div>
                <div id="content">

                        <html:form action="/login" focus="username">
                        <html:errors />
                                <div class="login" >

                                <div class="login_row">
                                        <label for="username" ><bean:message key="login.login" /></label>
                                        <html:text property="username"  errorStyleClass="error"> </html:text>
                                </div>
                                <div class="login_row">
                                        <label for="password"><bean:message key="login.password" /></label>
                                        <html:password property="password"  errorStyleClass="error"> </html:password>
                                </div>
                                <div class="login_row">
                                <html:submit>
                                        <bean:message key="login.loginButton" />
                                </html:submit>
							 			  <html:reset>
												<bean:message key="button.reset" /> 
										  </html:reset>              
			                       </div>
                                
                        </html:form>
                                <br>
                                <a href="../pages/login.jsp" title="Standard" onclick="setActiveStyleSheet('standard');"><img src="../images/style_green.png"  alt="Standard:Fraunhofer Green"></a>&nbsp
                                <a href="../pages/login.jsp" title="NO IE" onclick="setActiveStyleSheet('bluegray');"><img src="../images/style_blue.png" alt="Alternative stylesheet: CSS2"></a>&nbsp
                                <a href="../pages/login.jsp" title="NO css" onclick="setActiveStyleSheet('nocss');"><img src="../images/style_no.png"  alt="Alternative stylesheet: no css"></a>&nbsp
                                
                                </div>
                </div>
        </div>
        <div id="footer" />
</body>
</html>