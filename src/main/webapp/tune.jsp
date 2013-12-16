<%@
  page import="com.headissue.wrench.Wrench" %><%@
  page import="java.util.Iterator" %><%@
  page import="java.util.Map" %><%

  Wrench wrench;
  {
    wrench = new Wrench();
  }
%><h2>Parameters</h2>
  <%=Wrench.help()%><%


  Map<String, String[]> parameters = request.getParameterMap();
  if ( parameters != null) {
    wrench.tune(parameters);
  }

  int active = wrench.getActiveSessionsOfTestShop();
  int maxSessions = wrench.getSessionLimitOfTestShop();
  int watermark = wrench.getWatermarkOfTestShop();
%>
<h2>Values</h2>
<div>activeSessions</div>
<div class="active"><%=active%></div>
<div>max</div>
<div class="max"><%=maxSessions%></div>
<div>watermark</div>
<div class="watermark"><%=watermark%></div>

