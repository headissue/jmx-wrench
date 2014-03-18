<%@ page contentType="text/html;charset=UTF-8" language="java" %><%@
  page import="java.net.URLEncoder" %><%@
  page import="java.util.Map" %><%@ include file="frag/decl.jspf"%><%

  if(request.getCharacterEncoding() == null) {
    request.setCharacterEncoding("UTF-8");
  }


  String returnValue = "Did not invoke, nothing to return";
  Map<String, String[]> parameters = request.getParameterMap();
  if ( parameters != null) {
    try {
      returnValue = (String) wrench.invoke(parameters);
    } catch (Exception e) {
      returnValue = "<p><span class=\"error\">invoke failed</span></p>"+
        "<p>"+e.getLocalizedMessage()+"</p>";
    }
  }



%><%@include file="frag/head.jspf"%>
<h2><%=returnValue%></h2>
<a href='<%=restLink.query%><%=URLEncoder.encode(parameters.get(Wrench.QUERY)[0], request.getCharacterEncoding())%>'>back to bean</a>
<%@include file="frag/bottom.jspf"%>