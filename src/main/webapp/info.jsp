<%@
  page import="com.headissue.wrench.Util" %><%@
  page import="com.headissue.wrench.Wrench" %>
<%@ page import="java.util.Map" %>
<%@ page import="javax.management.*" %><%

    /**
     * Errors:
     * https://localhost:8897/wrench/info.jsp?val=y&attr=objectName&q=Catalina%3AJ2EEApplication%3Dnone%2CJ2EEServer%3Dnone%2CWebModule%3D%2F%2Flocalhost%2F%2Cj2eeType%3DServlet%2Cname%3DDereferer
     */

  Wrench wrench = Wrench.getInstance();
  String domain = Util.removeLeadingSlash(request.getPathInfo());
  String properties = request.getQueryString();
  ObjectName objectName = new ObjectName(domain + ":"  +properties);
  MBeanInfo info = wrench.getInfo(objectName);
  Map<String, String[]> parameterMap = request.getParameterMap();
  //if (parameterMap.get(Wrench.ATTRIBUTE) != null) {
  //  wrench.setBeanAttribute(objectName, request.getParameterMap());
  //}
%>
<a href="./displayNames.jsp">overview</a>
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
     <td><%=wrench.getAttributeValue(info.getC, mBeanAttributeInfo.getName())%></td><%

     if (mBeanAttributeInfo.isWritable()) {
%>
       <td>
         <form method="GET" action="displayInfo.jsp">
           <input type="text" name="<%=Wrench.VALUE%>"/>
           <input type="hidden" name="<%=Wrench.ATTRIBUTE%>" value='<%out.write(mBeanAttributeInfo.getName());%>'/>
           <input type="hidden" name="<%=Wrench.QUERY%>" value='<%=""//out.write(objectName);%>'/>
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
          <input type="hidden" name="<%=Wrench.QUERY%>" value='<%=""//out.write(objectName);%>'/>
          <input type="submit" value="execute"/>
        </form>
      </td>
    </tr><%

  }

%>
</table>
