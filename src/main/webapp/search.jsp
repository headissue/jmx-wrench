<%@
  page import="com.headissue.wrench.Util" %><%@
  page import="org.apache.commons.lang3.StringUtils" %><%@
  page import="javax.management.MalformedObjectNameException" %><%@
  page import="javax.management.ObjectName" %><%@
  page import="static org.apache.commons.lang3.StringEscapeUtils.escapeHtml4" %><%@
  page import="java.net.URLDecoder" %><%@
  page import="java.util.LinkedHashSet" %><%@
  page import="java.util.Map" %><%@
  page import="java.util.Set" %><%@include file="frag/decl.jspf"%><%

  String path = URLDecoder.decode(Util.removeLeadingSlash(request.getPathInfo()), "UTF-8");


  Set<ObjectName> objectNameSet = new LinkedHashSet<ObjectName>();
  Exception error = null;

  if (request.getParameter("query") != null) {
    path = request.getParameter("query");
    // query for ObjectNames
    try {
      objectNameSet = wrench.queryObjectNamesUsingRegExp(path);
    } catch (MalformedObjectNameException e) {
      error = e;
    }
  } else if (StringUtils.isNotBlank(request.getQueryString())) {
    // encoded ObjectName
    String objectClassPath = path;
    Map<String, String[]> parameters = (Map<String, String[]>) request.getParameterMap();
    String fullyQualifiedObjectName = Util.decodeObjectNameQuery(objectClassPath, parameters, characterEncoding);
    objectNameSet.add(new ObjectName(fullyQualifiedObjectName));
  }
   else   {
    objectNameSet = wrench.findAllObjectNames();
  }

%><%@include file="frag/head.jspf"%>
<div class="row">
  <div class="col-lg-12"><%

  // maybe create dedicated jsp for errors
  if (error != null) {
%><div class="alert alert-danger">
    <p>Query <code>"<%=escapeHtml4(path)%>"</code>Caused an error!</p>
    <p>Please try again. If the problem persists, please file a bug at GitHub.</p>
    <p>Error type: <code><%= error.getClass().getSimpleName()%></code></p>
    <p>Message: <code><%= error.getMessage()%></code></p>
</div><%

  } else if (objectNameSet == null || objectNameSet.isEmpty()) {
%><div class="alert alert-info">
    <h3>Result empty</h3>
    <p>Your search for "<code><%=escapeHtml4(path)%></code>" yielded zero results.</p>
    </div>
    <p>Here are some examples how you can search:</p>
    <ul>
      <li><a href="<%=restLink.query%>">Click here</a> to show all JMX beans</li>
      <li><a href="<%=restLink.query%>?query=lang"><code>lang</code></a>shows anything containing the word "lang"</li>
      <li><a href="<%=restLink.query%>?query=^java.l"><code>^java.l</code></a> lists all items beginning
        with "java.l"</li>
      <li><a href="<%=restLink.query%>?query=(j2ee|java)"><code>(j2ee|java)</code></a> lists all items containing
        "j2ee"
        or
        "java"</li>
      <li><a href="<%=restLink.query%>?query=Connector$"><code>Connector$</code></a> yields all names ending with
        "Connector"</li>
    </ul>
    <p>More precisely, you can use any regular expression. Your query will automatically be wrapped in a regular
      expression.
      <table class="table">
      <thead>
      <tr><th>Query</th><th>Resulting regular expression</th><th>Explanation</th></tr>
      </thead>
      <tbody>
      <tr>
        <td>(empty)</td><td><code>.*</code></td><td>Matches everything</td>
      </tr>
      <tr>
        <td><code>^query</code><td><code>^query.*</code></td><td>All items starting with "query", followed by any
        amount of
        characters</td>
      </tr>
      <tr>
        <td><code>query$</code></td><td><code>.*query$</code></td><td>Zero or more characters, plus the name ends wit
        h "query"</td>
      </tr>
      <tr>
        <td><code>^query$</code></td><td><code>.*query$</code></td><td>Matches only the name "query"</td>
      </tr>
      </tbody>
      </table>
    <small>Note that "query" is a regular expression above. Use this fact if you need to.</small>
    <%

  } else if (objectNameSet.size() == 1) {
    ObjectName name = objectNameSet.iterator().next();
    response.sendRedirect(restLink.show + Util.encodeObjectNameQuery(name, characterEncoding) );
  } else {
    %><h1>Search result</h1>
    <p>Your search for "<code><%= escapeHtml4(path) %></code>" has the
      following <%= objectNameSet.size()%> results:</p>
    <table class="table table-responsive table-striped"><%
    // list all objects found
    for (ObjectName name : objectNameSet) {
%>
      <tr><td><a
        href="<%=restLink.show%><%=Util.encodeObjectNameQuery(name, characterEncoding)%>"
        ><%= escapeHtml4(name.getCanonicalName())%></a></td></tr><%
    }

    %></table><%
  }
%>
</div>
</div><%@include file="frag/bottom.jspf"%>