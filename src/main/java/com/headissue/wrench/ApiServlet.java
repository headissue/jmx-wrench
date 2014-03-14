package com.headissue.wrench;

import javax.management.MBeanInfo;
import javax.management.ObjectName;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

/**
 * @author tobi
 */
@WebServlet(name="api", urlPatterns = "/api")
public class ApiServlet extends HttpServlet {


  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String _encoding = "UTF-8";
    resp.setCharacterEncoding(_encoding);
    resp.setContentType("text/javascript");

    RestLink _restLink = new RestLink(req.getContextPath());

    Wrench _wrench = Wrench.getInstance();
    Set<ObjectName> o;
    try {
      o = _wrench.queryObjectNames(req.getParameter("q"));

    StringBuilder sb = new StringBuilder("{\"suggestions\":[");
      Iterator<ObjectName> oit = o.iterator();
      while (oit.hasNext()) {
        ObjectName on = oit.next();
        MBeanInfo _info = _wrench.getInfo(on);
        sb.append("{\"name\":\"");

        String _name = on.getCanonicalName();

        sb.append(_name).append("\", \"url\": \"").append(_restLink.query)
          .append(Util.encodeObjectNameQuery(on, _encoding)).append("\"}");
        if (oit.hasNext()) {
          sb.append(",");
        }
        //sb.append("}");
      }
      sb.append("]}");
      resp.getWriter().write(sb.toString());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }


  }
}
