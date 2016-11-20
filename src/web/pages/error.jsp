<%@ page language="java" %>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ page import="org.apache.struts.Globals" %>
<%@ page import="java.lang.Exception" %>

<html:errors />

<% Exception exception = (Exception)request.getAttribute(Globals.EXCEPTION_KEY);%>
Reason: <%=exception.getMessage()%>