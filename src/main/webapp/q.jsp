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

  String path = Util.removeLeadingSlash(request.getPathInfo());
  if(request.getCharacterEncoding() == null) {
    request.setCharacterEncoding("UTF-8");
  }
  String characterEncoding = request.getCharacterEncoding();
  Set<ObjectName> objectNameSet = new LinkedHashSet<ObjectName>();
  Exception error = null;

  if (StringUtils.isNotBlank(request.getQueryString())) {
    // encoded ObjectName
    String objectClassPath = path;
    Map<String, String[]> parameters = request.getParameterMap();
    String fullyQualifiedObjectName = Util.decodeObjectNameQuery(objectClassPath, parameters, characterEncoding);
    objectNameSet.add(new ObjectName(fullyQualifiedObjectName));
  } else {
    // query for ObjectNames
    try {
      objectNameSet = wrench.queryObjectNames(path);
    } catch (MalformedObjectNameException e) {
      error = e;
    }
  }

  RestLink restLink = new RestLink(request.getContextPath());


%>
<!-- remove when design becomes an issue -->
<head>
  <style>
    .error {
      color: red;
    }
  </style>
</head>
  <form name="search" method="get" action="<%=restLink.query%>" size="100" role="search">
    <input type="text" name="class" placeholder="fully qualified object name" value=""><input type="submit" class="send" value="submit">
  </form><%

  // maybe create dedicated jsp for errors
  if (error != null) {
%>
    <p>Query "<%=escapeHtml(path)%>" löste einen Fehler aus!</p>
    <p>Dies kann bei Sonderzeichen im Objektnamen passieren.</p>
    <p><a href="<%=restLink.query%>">Alle anzeigen</a> oder nach Pfad suchen, z.b.</p>
    <p><a href="<%=restLink.query%>java">java</a></p>
    <p><a href="<%=restLink.query%>java.l">java.l</a></p><%

  } else if (objectNameSet == null || objectNameSet.size() == 0) {
%>
    <p>Query "<%=escapeHtml(path)%>" ohne Ergebnis</p>
    <p>Dies kann bei Sonderzeichen im Objektnamen passieren.</p>
    <p><a href="<%=restLink.query%>">Alle anzeigen</a> oder nach Pfad suchen, z.b.</p>
    <p><a href="<%=restLink.query%>java">java</a></p>
    <p><a href="<%=restLink.query%>java.l">java.l</a></p><%

  } else if (objectNameSet.size() == 1) {

    // single search result; display details
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
        <td><%=mBeanAttributeInfo.getName()%></td><%

        String value;
        try {
          value = wrench.getAttributeValue(objectName, mBeanAttributeInfo.getName());
        } catch (Exception e) {
          value = "<p><span class=\"error\">inaccessible </span></p>"+
            "<p>"+e.getLocalizedMessage()+"</p>";
        }
%>
        <td><%=value%></td><%

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
        MBeanParameterInfo[] operationParameters = mBeanOperationInfo.getSignature();
        String signatureString = Util.humanReadableSignature(operationParameters);
%>
        <tr>
          <td><%=mBeanOperationInfo.getReturnType()%></td>
          <td><%=mBeanOperationInfo.getName()%><%=signatureString%></td>
          <td>
            <form action="<%=restLink.invoke%>" method="GET"><%

            for (int i = 0; i < operationParameters.length; i++) {
%>
              <input type="text" name="<%=Wrench.PARAMETER%>"><%

            }
%>
            <input type="hidden" name="<%=Wrench.OPERATION%>" value='<%=mBeanOperationInfo.getName()%>'/>
            <input type="hidden" name="<%=Wrench.SIGNATURE%>" value='<%=Wrench.getSignature(mBeanOperationInfo.getSignature())%>'/>
            <input type="hidden" name="<%=Wrench.QUERY%>" value='<%=objectName%>'/>
            <input type="submit" value="execute"/>
          </form>
        </td>
      </tr><%

      }
%>
    </table><%
  } else {
    // list all objects found

    for (Iterator<ObjectName> objectNameIterator = objectNameSet.iterator(); objectNameIterator.hasNext(); ) {
      ObjectName name = objectNameIterator.next();
%>
      <p><a href="<%=restLink.query%><%=Util.encodeObjectNameQuery(name, characterEncoding)%>"><%= escapeHtml(name.getCanonicalName())%></a></p><%
    }
  }
%>