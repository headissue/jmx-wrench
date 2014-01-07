<%@
  page import="com.headissue.wrench.RestLink" %><%@
  page import="com.headissue.wrench.Util" %><%@
  page import="com.headissue.wrench.Wrench" %><%@
  page import="org.apache.commons.lang.StringUtils" %><%@
  page import="static org.apache.commons.lang.StringEscapeUtils.escapeHtml" %><%@
  page import="javax.management.*" %><%@
  page import="java.net.URLEncoder" %><%@
  page import="java.util.Iterator" %><%@
  page import="java.util.LinkedHashSet" %><%@
  page import="java.util.Map" %><%@
  page import="java.util.Set" %><%

  Wrench wrench = Wrench.getInstance();
  String q = Util.removeLeadingSlash(request.getPathInfo());
  Set<ObjectName> objectNameSet = new LinkedHashSet<ObjectName>();
  Exception error = null;
  RestLink restLink = new RestLink(request.getContextPath());
  if(request.getCharacterEncoding() == null) {
    request.setCharacterEncoding("UTF-8");
  }
  String characterEncoding = request.getCharacterEncoding();

  if (StringUtils.isNotBlank(request.getQueryString())) {
    // encoded ObjectName
    Map<String, String[]> parameters = request.getParameterMap();
    ObjectName objectName = null;
    String properties = "";
    try {
      objectName = new ObjectName(Util.decodeObjectNameQuery(q, parameters, characterEncoding));
    } catch (MalformedObjectNameException e) {
      throw new RuntimeException(e);
    }
    objectNameSet.add(objectName);
  } else {
    // query for ObjectNames
    try {
      objectNameSet = wrench.queryObjectNames(q);
    } catch (MalformedObjectNameException e) {
      error = e;
    }
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
    ObjectName objectName = objectNameSet.iterator().next();
    MBeanInfo info = wrench.getInfo(objectName);
%>
  <a href="<%=restLink.query%>">Overview</a>
  <h2><%=info.getClassName()%></h2>
  <p><%=info.getDescription()%></p>
  <h3>Attributes</h3>
  <table><%

  MBeanAttributeInfo[] mBeanAttributeInfos = info.getAttributes();
  for (MBeanAttributeInfo mBeanAttributeInfo : mBeanAttributeInfos) {
%>
    <tr>
      <td><%= mBeanAttributeInfo.getType() %></td>
      <td><%=mBeanAttributeInfo.getName()%></td>
      <td><%=wrench.getAttributeValue(objectName, mBeanAttributeInfo.getName())%></td><%

      if (mBeanAttributeInfo.isWritable()) {
%>
        <td>
          <form method="GET" action="<%=restLink.set%><%=URLEncoder.encode(objectName.getCanonicalName(),characterEncoding)%>">
            <input type="text" name="<%=Wrench.VALUE%>"/>
            <input type="hidden" name="<%=Wrench.ATTRIBUTE%>" value='<%=URLEncoder.encode(mBeanAttributeInfo.getName(), characterEncoding)%>'/>
            <input type="submit" value="set"/>
          </form>
        </td><%

      }
%>
    </tr><%

  }
%>
</table>
<h3>Operations</h3>
<table><%

  MBeanOperationInfo[] mBeanOperationInfos = info.getOperations();
  for (MBeanOperationInfo mBeanOperationInfo : mBeanOperationInfos) {
    MBeanParameterInfo[] signature = mBeanOperationInfo.getSignature();
    StringBuilder signatureString = new StringBuilder();
    for (int i = 0; i < signature.length; i++) {
      MBeanParameterInfo mBeanParameterInfo = signature[i];
      signatureString.append("(").append(mBeanParameterInfo.getType()).append(" ").append(mBeanParameterInfo.getName()).append(")");
    }
%>
  <tr>
    <td><%out.write(mBeanOperationInfo.getReturnType());%></td>
    <td><%out.write(mBeanOperationInfo.getName());%><%=signatureString.toString()%></td>
    <td>
      <form action="tune.jsp" method="GET"><%

        for (int i = 0; i < signature.length; i++) {
%>
          <input type="text" name="<%=Wrench.PARAMETER%>"><%

        }
%>
        <input type="hidden" name="<%=Wrench.OPERATION%>" value='<%=mBeanOperationInfo.getName()%>'/>
        <input type="hidden" name="<%=Wrench.SIGNATURE%>" value='<%=Wrench.getSignatureString(mBeanOperationInfo.getSignature())%>'/>
        <input type="hidden" name="<%=Wrench.QUERY%>" value='<%=objectName%>'/>
        <input type="submit" value="execute"/>
      </form>
    </td>
  </tr><%

    }
%>
</table><%
  } else {
    // list all objects

    for (Iterator<ObjectName> objectNameIterator = objectNameSet.iterator(); objectNameIterator.hasNext(); ) {
      ObjectName name = objectNameIterator.next();
%>
      <p><a href="<%=restLink.query%><%=Util.encodeObjectNameQuery(name, characterEncoding)%>"><%= escapeHtml(name.getCanonicalName())%></a></p><%
    }
  }
%>
