package com.headissue.wrench;

/**
 * Provides common links for the REST service.
 * The web.xml has to be changed accordingly.
 */
public class RestLink {

  private final String QUERY = "/q/";
  private final String SET = "/set/";
  private static final String INVOKE = "/invoke/";

  public final String query;
  public final String set;
  public final String invoke;

  public RestLink(String contextPath) {
    query = contextPath + QUERY;
    set = contextPath + SET;
    invoke = contextPath + INVOKE;

  }
}
