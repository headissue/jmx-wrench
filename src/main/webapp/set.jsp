<%@
  page import="com.headissue.wrench.Util" %><%@
  page import="java.util.Map" %><%@ page import="javax.management.InstanceNotFoundException" %><%@ page
  import="java.net.URLDecoder" %><%@ page import="org.apache.commons.lang3.StringEscapeUtils" %><%@ include file="frag/decl.jspf"%><%

  response.setContentType("application/json");
  // Never cache
  response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate, max-age=0");

  Map<String, String[]> parameterMap = request.getParameterMap();
  String message;
  String result = "ok";

  if (parameterMap.get(Wrench.ATTRIBUTE) != null && parameterMap.get(Wrench.CLASS) != null) {
    try {
      String path = URLDecoder.decode(Util.removeLeadingSlash(request.getParameter(Wrench.CLASS)), "UTF-8");
      wrench.setBeanAttribute(path, request.getParameterMap());
      message = "Setting parameter " +  parameterMap.get(Wrench.ATTRIBUTE)[0] + " succeeded";
    } catch (InstanceNotFoundException e){
      result = "error";
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      message = "Parameter or class is unknown";
    } catch (Exception e) {
      result = "error";
      response.setStatus(503);
      message = e.fillInStackTrace().toString();
    }
  } else {
    result = "error";
    message = "Parameter 'attr' or 'class' not specified";
  }

  %>{"result":"<%= result%>", "message": "<%= StringEscapeUtils.escapeJson(message) %>" }<%
%>