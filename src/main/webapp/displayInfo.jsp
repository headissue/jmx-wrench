<%@
  page import="com.headissue.wrench.Wrench" %><%@
  page import="com.jwdt.mticket.URLEncoder" %><%@
  page import="javax.management.MBeanAttributeInfo" %><%@
  page import="javax.management.MBeanInfo" %><%@
  page import="javax.management.MBeanOperationInfo" %><%@
  page import="javax.management.MBeanParameterInfo" %><%@
  page import="java.util.Map" %><%

    /**
     * Errors:
     * https://localhost:8897/wrench/displayInfo.jsp?val=y&attr=objectName&q=Catalina%3AJ2EEApplication%3Dnone%2CJ2EEServer%3Dnone%2CWebModule%3D%2F%2Flocalhost%2F%2Cj2eeType%3DServlet%2Cname%3DDereferer
     */

  Wrench wrench;
  {
    wrench = Wrench.getInstance();
  }
  String objectName = request.getParameter(Wrench.QUERY);
  MBeanInfo info = wrench.getInfo(objectName);
  Map<String, String[]> parameterMap = request.getParameterMap();
  if (parameterMap.get(Wrench.ATTRIBUTE) != null) {
    wrench.setAttribute(objectName, request.getParameterMap());
  }
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
     <td><%out.write(mBeanAttributeInfo.getType());%></td>
     <td><%out.write(mBeanAttributeInfo.getName());%></td>
     <td><%out.write(wrench.getAttributeValue(objectName, mBeanAttributeInfo.getName()));%></td><%

     if (mBeanAttributeInfo.isWritable()) {
%>
       <td>
         <form method="GET" action="displayInfo.jsp">
           <input type="text" name="<%=Wrench.VALUE%>"/>
           <input type="hidden" name="<%=Wrench.ATTRIBUTE%>" value='<%out.write(mBeanAttributeInfo.getName());%>'/>
           <input type="hidden" name="<%=Wrench.QUERY%>" value='<%out.write(objectName);%>'/>
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
          <input type="hidden" name="<%=Wrench.QUERY%>" value='<%out.write(objectName);%>'/>
          <input type="submit" value="execute"/>
        </form>
      </td>
    </tr><%

  }

%>
</table>
