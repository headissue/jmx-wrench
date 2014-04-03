package com.headissue.wrench;

import org.apache.commons.lang3.StringEscapeUtils;

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
 * A servlet which returns JSON. It should be used for more complex tasks, like searching.
 * For simple things like method invocation, a JSP should be preferred.
 * @author tobi
 */
@WebServlet(name="api", urlPatterns = "/api/*")
public class JsonApiServlet extends HttpServlet {
  public static String SEARCH = "/search";
  private final Wrench wrench = Wrench.getInstance();

  @Override
  protected void doGet(HttpServletRequest _request, HttpServletResponse _response) throws ServletException, IOException {

    String _encoding = "UTF-8";
    _response.setCharacterEncoding(_encoding);
    _response.setContentType("text/javascript");
    RestLink _restLink = new RestLink(_request.getServletContext());
    String _pathInfo = _request.getPathInfo();
    if (_pathInfo.startsWith(SEARCH)) {
      search(_request, _response, _encoding, _restLink);
    } else {
      _response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

  }

  private void search(HttpServletRequest _request, HttpServletResponse _response, String _encoding, RestLink _restLink) {
    try {
      String _query = _request.getParameter("q");

      Set<ObjectName> _objectNameList = wrench.queryObjectNamesUsingRegExp(_query);
      StringBuilder sb = new StringBuilder("{\"suggestions\":[");
      Iterator<ObjectName> oit = _objectNameList.iterator();
      while (oit.hasNext()) {
        ObjectName on = oit.next();
        String _name = on.getCanonicalName();
        sb.append("{\"name\":\"");
        sb.append(StringEscapeUtils.escapeJson(_name)).append("\", \"url\": \"").append(_restLink.show)
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
