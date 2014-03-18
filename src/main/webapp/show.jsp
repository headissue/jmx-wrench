<%@ page import="com.headissue.wrench.Util" %><%@ page import="javax.management.MBeanAttributeInfo" %><%@ page
  import="javax.management.MBeanInfo" %><%@ page import="javax.management.MBeanOperationInfo" %><%@ page
  import="javax.management.MBeanParameterInfo" %><%@ page import="javax.management.ObjectName" %><%@ page
  import="java.net.URLDecoder" %><%@ page import="java.net.URLEncoder" %><%@ page
  import="java.util.Map" %><%@ page import="static org.apache.commons.lang3.StringEscapeUtils.escapeHtml4" %><%@include file="frag/decl.jspf"%><%

  String path = URLDecoder.decode(Util.removeLeadingSlash(request.getPathInfo()), "UTF-8");
  String error  = null;
  // encoded ObjectName
  Map<String, String[]> parameters = (Map<String, String[]>) request.getParameterMap();
  String fullyQualifiedObjectName = Util.decodeObjectNameQuery(path, parameters, characterEncoding);
  ObjectName objectName = new ObjectName(fullyQualifiedObjectName);
  if (!wrench.isRegistered(objectName)) {
    error = "Unknown object name: " + objectName;
  }
  MBeanInfo info = wrench.getInfo(objectName);
  MBeanAttributeInfo[] mBeanAttributeInfos = info.getAttributes();

  %><%@include file="frag/head.jspf"%><script id="okay-message" type="text/x-handlebars-template">
  <div class="alert alert-success fade-in">
    <strong>Success!</strong> {{message}}
  </div>
</script>
<script id="error-message" type="text/x-handlebars-template">
  <div class="alert alert-danger fade-in">
    <strong>Error:</strong> {{message}}
  </div>
</script><%

  if (error != null) {

%><div class="row">
  <div class="col-md-12">
    <div class="alert alert-danger fade in">
    <button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>
    <h4>Unknown object name</h4>
    <p>The object name <pre><%=objectName%></pre> is not registered within the Managed Bean server</p>
    </div>
  </div>
</div><%
  }
%><div class="row"><div class="col-md-12" id="messages"></div></div>
<div class="row">
  <div class="col-md-12">
    <h2><%=info.getClassName()%> <small><a href="<%=restLink.show + Util.encodeObjectNameQuery(objectName,
    characterEncoding) %>" title="Permalink for this bean"><span class="glyphicon glyphicon-link"> </span></a></small></h2>
    <p><code class="whitespace"><small><%= escapeHtml4(objectName.getKeyPropertyListString())%></small></code></p>
    <%
      if (info.getDescription() != null) {

      %><p><strong>Description: </strong> <%=info.getDescription()%></strong></p><%

    }
    %>

    <h3>Attributes</h3>
    <table class="table table-responsive table-striped" class="attributeTable">
      <thead>
      <tr><th>Type</th><th>Property</th><th>Current value</th><th>New value</th></tr>
      </thead>
      <tbody>
      <%


        for (MBeanAttributeInfo mBeanAttributeInfo : mBeanAttributeInfos) {
      %>
      <tr>
        <td><%= mBeanAttributeInfo.getType() %></td>
        <td><code class="whitespace"><%=mBeanAttributeInfo.getName()%></code></td><%

        String value;
        try {
          value = wrench.getAttributeValue(objectName, mBeanAttributeInfo.getName());
        } catch (Exception e) {
          value = "<p><span class=\"error\">inaccessible </span></p>"+
            "<p>"+e.getLocalizedMessage()+"</p>";
        }
      %><%-- Zero width space hack --%>
        <td class="value"><%=value.toString().replace(",",",&#8203;")%></td><%

        if (mBeanAttributeInfo.isWritable()) {
      %>
          <td>
            <form class="setpropertyform"  method="GET" role="form" class="form-inline" action="<%=restLink.set%><%=URLEncoder.encode(objectName.getCanonicalName(),characterEncoding)%>">
              <div class="input-group">
                <input type="text" class="form-control" name="<%=Wrench.VALUE%>"/>
                <input type="hidden" name="<%=Wrench.ATTRIBUTE%>" value='<%=URLEncoder.encode(mBeanAttributeInfo.getName(), characterEncoding)%>'/>
                <div class="input-group-btn">
                  <input class="btn btn-primary" type="submit" value="set"/>
                </div>
              </div>
            </form>
          </td><%

      } else {
      %><td></td><%
        }
      %>
      </tr><%

        }
      %>  </tbody>
    </table>
    </div>
  </div>
<div class="row">
  <div
    class="col-md-12"><a href="#" class="pull-right"><span class="glyphicon glyphicon-arrow-up"></span> Back to top</a></div>
</div>
<div class="row">
  <div class="col-md-12">
    <h3>Operations</h3>
    <table class="table table-responsive"><thead>
      <thead>
        <tr><th>Type</th><th>Property</th><th>Operations</th></tr>
      </thead><%

        MBeanOperationInfo[] mBeanOperationInfos = info.getOperations();
        for (MBeanOperationInfo mBeanOperationInfo : mBeanOperationInfos) {
          MBeanParameterInfo[] operationParameters = mBeanOperationInfo.getSignature();
          String signatureString = Util.humanReadableSignature(operationParameters);
      %>
        <tr>
          <td><%=mBeanOperationInfo.getReturnType()%></td>
          <td><code><%=mBeanOperationInfo.getName()%><%=signatureString.replace(",",",&#8203;").replace("(","(&#8203;")%></code</td>
          <td>
            <form role="form" action="<%=restLink.invoke%>" method="GET" class="form-horizontal">
                <div class="input-group"><%

                  for (MBeanParameterInfo operationParameter : operationParameters) {
                %>
              <input type="text" class="form-control" name="<%=Wrench.PARAMETER%>"><%

                    }

                    if (operationParameters.length == 0) {
                  %><p>()</p><%
                    }
                  %>
            <input type="hidden" name="<%=Wrench.OPERATION%>" value='<%=mBeanOperationInfo.getName()%>'/>
            <input type="hidden" name="<%=Wrench.SIGNATURE%>" value='<%=Wrench.getSignature(mBeanOperationInfo.getSignature())%>'/>
            <input type="hidden" name="<%=Wrench.QUERY%>" value='<%=objectName%>'/>
              <div class="input-group-btn">
            <input type="submit" class="btn btn-primary" value="execute"/></div>
                  </div>
          </form>
        </td>
      </tr><%

        }
      %>
    </table>
    <div class="row">
  <div class="col-md-12"><a href="#" class="pull-right"><span class="glyphicon glyphicon-arrow-up"></span> Back to
    top</a></div>
</div>
  </div>
</div>
<%@include file="frag/bottom.jspf"%>