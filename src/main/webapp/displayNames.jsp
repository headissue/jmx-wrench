<%@
  page import="com.headissue.wrench.Wrench" %><%@
  page import="javax.management.ObjectName" %>
<%@ page import="java.util.Set" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.net.URLEncoder" %>
<%

  Wrench wrench = Wrench.getInstance();

  Set<ObjectName> objectNameSet = wrench.getObjectNames();

  for (Iterator<ObjectName> objectNameIterator = objectNameSet.iterator(); objectNameIterator.hasNext(); ) {
    ObjectName name = objectNameIterator.next();
%>
    <p><a href="./displayInfo.jsp?q=<%= URLEncoder.encode(name.getCanonicalName(), response.getCharacterEncoding()) %>"><%= name.getCanonicalName() %></a></p><%
  }
%>
