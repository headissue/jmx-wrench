<%@
  page import="com.headissue.wrench.Util" %><%@
  page import="com.headissue.wrench.Wrench" %><%@
  page import="java.util.Map" %><%

 Wrench wrench = Wrench.getInstance();
  Map<String, String[]> parameterMap = request.getParameterMap();
  String action ="<span class=\"error\">Attribute to set was not found in the query<span>";
  if (parameterMap.get(Wrench.ATTRIBUTE) != null) {
    try {
      wrench.setBeanAttribute(Util.removeLeadingSlash(request.getPathInfo()), request.getParameterMap());
      action = "setting succeeded";
    } catch (Exception e) {
      action = "<p><span class=\"error\">set failed</span></p>"+
        "<p>"+e.getLocalizedMessage()+"</p>";
    }
  }
%>
<%=action%>
