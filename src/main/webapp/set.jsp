<%@
  page import="com.headissue.wrench.Util" %><%@
  page import="java.util.Map" %><%@ include file="frag/decl.jspf"%><%
  System.out.println("here");
  // Never cache
  response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
  response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
  response.setDateHeader("Expires", 0); // Proxies.
  System.out.println("here");

  Map<String, String[]> parameterMap = request.getParameterMap();
  String action ="<span class=\"error\">Attribute to set was not found in the query<span>";
  String result = "ok";
  if (parameterMap.get(Wrench.ATTRIBUTE) != null) {
    try {
      wrench.setBeanAttribute(Util.removeLeadingSlash(request.getPathInfo()), request.getParameterMap());
      action = "Setting parameter " +  parameterMap.get(Wrench.ATTRIBUTE)[0] + "succeeded";
    } catch (Exception e) {
      result = "error";
      response.setStatus(503);
      action = e.getLocalizedMessage();
    }
  }

    response.setContentType("application/json");
    %>{result:'<%= result%>', message: '<%= action %>' }<%
%>