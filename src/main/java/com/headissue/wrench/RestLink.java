package com.headissue.wrench;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;
import java.util.Collection;
import java.util.Map;

/**
 * Constructs common links for the REST service in respect to the given context path
 * The web.xml has to be changed accordingly.
 *
 * @author wormi
 * @see <a href="https://to.headissue.net/radar/browse/MTP-4990">MTP-4990</a>
 */
public class RestLink {

  // FIXME Use getters here! We obtain a nicely capsuled, immutable object then
  public final String query;
  public final String set;
  public final String invoke;
  public final String show;
  public final String api;

  public RestLink(ServletContext _context) {
    String _contextPath = _context.getContextPath();
    Map<String, ? extends ServletRegistration> nameMap = _context.getServletRegistrations();
    query = _contextPath + getRestContextByName(nameMap, "search");
    show = _contextPath + getRestContextByName(nameMap, "show");
    set = _contextPath + getRestContextByName(nameMap, "set");
    invoke = _contextPath + getRestContextByName(nameMap, "invoke");
    api = _contextPath + getRestContextByName(nameMap, "api");
  }


  /**
   * @param _nameMap
   * @param _name
   * @return the first url mapping which does not end with ".jsp"
   */
  private String getRestContextByName(Map<String, ? extends ServletRegistration> _nameMap, String _name) {
    Collection<String> _mappings = _nameMap.get(_name).getMappings();
    for (String s:_mappings) {
      if (!s.endsWith(".jsp")) {
        if (s.endsWith("*")) {
          s = s.substring(0,s.length() -1);
        }
        return s;
      }
    }
    throw new RuntimeException("Could not find a rest mapping for " + _name);
  }


}
