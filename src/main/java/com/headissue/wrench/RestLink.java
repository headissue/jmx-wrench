package com.headissue.wrench;

/**
 * Constructs common links for the REST service in respect to the given context path
 * The web.xml has to be changed accordingly.
 *
 * @author wormi
 * @see <a href="https://to.headissue.net/radar/browse/MTP-4990">MTP-4990</a>
 */
public class RestLink {

  private final String QUERY = "/q/";
  private final String SET = "/set/";
  private static final String INVOKE = "/invoke/";

  // FIXME Use getters here! We obtain a nicely capsuled, immutable object then
  public final String query;
  public final String set;
  public final String invoke;

  public RestLink(String _contextPath) {
    query = _contextPath + QUERY;
    set = _contextPath + SET;
    invoke = _contextPath + INVOKE;

  }
}
