<%@
  page import="com.headissue.wrench.Wrench" %><%@
  page import="java.util.Iterator" %><%@
  page import="java.util.Map" %><%

  Wrench wrench;
  {
    wrench = new Wrench();
  }
%><h2>Parameters</h2><%

  Map<String, String[]> parameters = request.getParameterMap();

  for (Iterator<String> iterator = parameters.keySet().iterator(); iterator.hasNext(); ) {
    String key = iterator.next();
    String[] values = parameters.get(key);

    try {
      if ("setMax".equals(key)) {
        wrench.setMaxSessionsForTestShop(Integer.valueOf(values[0]));
      } else if ("setWatermark".equals(key)) {
        wrench.setWatermarkForTestShop(Integer.valueOf(values[0]));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    String concatenatedValues = "";
    for (int i = 0; i < values.length; i++) {
      concatenatedValues += values[i] + ", ";
    }

%>
<div><%=key%>:</div>
<div><%=concatenatedValues%></div><%

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

