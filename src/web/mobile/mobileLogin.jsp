<%@ page language="java" %>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html>
    <head> 
        <title><bean:message key="mars.title" /></title>
        <html:base />
        <link rel="stylesheet" type="text/css" href="../css/mobile.css" />
    </head>

	<body>

	<div id="main">
		<div id="content">

			<html:form action="/loginMobile">
			<html:errors />
				<table align="center" text-align="center">
					<tr>
						<td><bean:message key="login.login" /></td>
						<td><html:text property="username"  errorStyleClass="error" size="10" /></td>
					</tr>
					<tr>
						<td><bean:message key="login.password" /></td>
						<td><html:password property="password"  errorStyleClass="error" size="10" /></td>
					</tr>
					<tr>
					    <td><html:submit><bean:message key="login.loginButton" /></html:submit></td>
						<td><html:reset /></td>
					</tr>
				</table>
			</html:form>
		</div>
	</div>
	</body>
</html>

