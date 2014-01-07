<%@
  page import="com.headissue.wrench.Util" %><%@
  page import="com.headissue.wrench.Wrench" %><%@
  page import="javax.management.ObjectName" %><%@
  page import="java.util.Iterator" %><%@
  page import="java.util.Set" %><%@
  page import="java.util.Hashtable" %><%@
  page import="javax.management.MalformedObjectNameException" %><%@
  page import="static org.apache.commons.lang.StringEscapeUtils.escapeHtml" %><%@
  page import="static java.net.URLEncoder.encode" %>
<%@ page import="com.headissue.wrench.RestLink" %>
<%

  Wrench wrench = Wrench.getInstance();
  String q = Util.removeLeadingSlash(request.getPathInfo());
  Set<ObjectName> objectNameSet = null;
  Exception error = null;
  RestLink restLink = new RestLink(request.getContextPath());

  try {
    objectNameSet = wrench.queryObjectNames(q);
  } catch (MalformedObjectNameException e) {
   error = e;
  }

  if (error != null) {


%>
    <p>Query "<%=escapeHtml(q)%>" löste einen Fehler aus!</p>
    <p>Dies kann bei Sonderzeichen im Objektnamen passieren.</p>
    <p><a href="<%=restLink.query%>">Alle anzeigen</a> oder nach Pfad suchen, z.b.</p>
    <p><a href="<%=restLink.query%>java">java</a></p>
    <p><a href="<%=restLink.query%>java.l">java.l</a></p><%

  } else if (objectNameSet == null || objectNameSet.size() == 0) {

%>
    <p>Query "<%=escapeHtml(q)%>" ohne Ergebnis</p>
    <p>Dies kann bei Sonderzeichen im Objektnamen passieren.</p>
    <p><a href="<%=restLink.query%>">Alle anzeigen</a> oder nach Pfad suchen, z.b.</p>
    <p><a href="<%=restLink.query%>java">java</a></p>
    <p><a href="<%=restLink.query%>java.l">java.l</a></p><%

  } else if (objectNameSet.size() == 1) {
    response.sendRedirect(restLink.info + Util.encodeObjectNameQuery(objectNameSet.iterator().next(), response.getCharacterEncoding()));
  } else {





    for (Iterator<ObjectName> objectNameIterator = objectNameSet.iterator(); objectNameIterator.hasNext(); ) {
      ObjectName name = objectNameIterator.next();
      String domain = name.getDomain();
      Hashtable<String, String> keyPropertyMap = name.getKeyPropertyList();
      StringBuilder properties = new StringBuilder();
      for (Iterator iterator = keyPropertyMap.keySet().iterator(); iterator.hasNext(); ) {
        String key = (String) iterator.next();
        properties.append(key).append("=").append(keyPropertyMap.get(key));

      }
%>
      <p><a href="<%=restLink.query%><%=Util.encodeObjectNameQuery(name, response.getCharacterEncoding())%>"><%= escapeHtml(name.getCanonicalName())%></a></p><%
    }
  }
%>
