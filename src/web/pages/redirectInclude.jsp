<%@ page import="web.java.controller.ControllerConnection" %>
<%@ page import="web.java.Constants" %>

<%
   ControllerConnection _connection = (ControllerConnection)request.getSession().getAttribute(Constants.CONNECTION);	
   if(_connection == null || !_connection.checkConnection() || _connection.getCurrentUser() == null)
   {
%>
<logic:redirect href="index.jsp" />
<%
   }
%>
