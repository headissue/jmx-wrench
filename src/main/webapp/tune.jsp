<%@
  page import="com.headissue.wrench.Wrench" %><%@
  page import="java.util.Map" %><%

  Wrench wrench;
  {
    wrench = Wrench.getInstance();
  }

  String returnValue = "";
  Map<String, String[]> parameters = request.getParameterMap();
  if ( parameters != null) {
    returnValue = wrench.tune(parameters);
  }
%>
<h2>>> Execution returned <%=returnValue%> <<</h2>
<a href='./displayInfo.jsp?<%=Wrench.QUERY%>=<%=parameters.get(Wrench.QUERY)[0]%>'>back to bean</a>
