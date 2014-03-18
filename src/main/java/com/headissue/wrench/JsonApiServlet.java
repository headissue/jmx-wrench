package com.headissue.wrench;

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
public class JsonApiServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest _request,
                       HttpServletResponse _response) throws ServletException, IOException {
    String _encoding = "UTF-8";
    _response.setCharacterEncoding(_encoding);
    _response.setContentType("text/javascript");

    RestLink _restLink = new RestLink(_request.getContextPath());

    Wrench _wrench = Wrench.getInstance();
    Set<ObjectName> o;
    try {
      String _query = _request.getParameter("q");
      if (_query == null) {
        _query = ".*";
      } else {
        _query = ".*" + _query.toLowerCase() + ".*";
      }
      Set<ObjectName> _objectNameList = _wrench.filterObjectNames(_query);
      StringBuilder sb = new StringBuilder("{\"suggestions\":[");
      Iterator<ObjectName> oit = _objectNameList.iterator();
      while (oit.hasNext()) {
        ObjectName on = oit.next();
        String _name = on.getCanonicalName();
        if (!_name.toLowerCase().matches(_query)) {
          continue;
        }

        sb.append("{\"name\":\"");
        sb.append(_name).append("\", \"url\": \"").append(_restLink.query)
          .append(Util.encodeObjectNameQuery(on, _encoding)).append("\"}");
        if (oit.hasNext()) {
          sb.append(",");
        }
        //sb.append("}");
      }
      sb.append("]}");
      _response.getWriter().write(sb.toString());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }


  }


}
