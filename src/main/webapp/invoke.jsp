<%@ page contentType="text/html;charset=UTF-8" language="java" %><%@
  page import="org.apache.commons.lang3.StringEscapeUtils" %><%@
  page import="java.util.Arrays" %><%@ page import="java.util.Map" %><%@ page import="java.util.Objects" %><%@ include file="frag/decl.jspf"%><%

  if(request.getCharacterEncoding() == null) {
    request.setCharacterEncoding("UTF-8");
  }

  response.setContentType("application/json");
  // Never cache
  response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate, max-age=0");

  String message;
  String result = "ok";

  Map<String, String[]> parameters = request.getParameterMap();
  if ( parameters != null) {
    try {
      String returnValue;
      Object o = wrench.invoke(parameters);
      if (o == null) {
        returnValue="void";
      } else if (o instanceof Object[]) {
        returnValue = Arrays.toString((Object[]) o);
      } else {
        returnValue = o.toString();
      }
      String _operation = parameters.get(Wrench.OPERATION)[0];
      message = "Operation " + _operation + " invoked successfully. The return value was: " + returnValue;
    } catch (Exception e) {
      e.printStackTrace();
      result = "error";
      response.setStatus(503);
      message = e.fillInStackTrace().toString();
    }
  } else {
    result = "error";
    message = "No parameters specified";
  }

  %>{"result":"<%= result%>", "message": "<%= StringEscapeUtils.escapeJson(message) %>" }<%
%>