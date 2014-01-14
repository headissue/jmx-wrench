<%@
  page import="com.headissue.wrench.RestLink" %><%@
  page import="com.headissue.wrench.Wrench" %><%@
  page import="java.util.Map" %><%@
  page import="java.net.URLEncoder" %>
<%

  Wrench wrench;
  {
    wrench = Wrench.getInstance();
  }
  RestLink restLink = new RestLink(request.getContextPath());
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
%>
<h2><%=returnValue%></h2>
<a href='<%=restLink.query%><%=URLEncoder.encode(parameters.get(Wrench.QUERY)[0], request.getCharacterEncoding())%>'>back to bean</a>
