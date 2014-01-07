package com.headissue.wrench;

/**
 * Created with IntelliJ IDEA.
 * User: wormi
 * Date: 03.01.14
 * Time: 12:45
 */
public class RestLink {

  private final String QUERY = "/q/";
  private final String SET = "/set/";

  public final String query;
  public final String set;

  public RestLink(String contextPath) {
    query = contextPath + QUERY;
    set = contextPath + SET;
  }
}
