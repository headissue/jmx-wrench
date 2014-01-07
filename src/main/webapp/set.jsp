<%@
  page import="com.headissue.wrench.Util" %><%@
  page import="com.headissue.wrench.Wrench" %>
<%@ page import="java.util.Map" %><%

 Wrench wrench = Wrench.getInstance();
  Map<String, String[]> parameterMap = request.getParameterMap();
  if (parameterMap.get(Wrench.ATTRIBUTE) != null) {
    try {
      wrench.setBeanAttribute(Util.removeLeadingSlash(request.getPathInfo()), request.getParameterMap());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

  }
%>
