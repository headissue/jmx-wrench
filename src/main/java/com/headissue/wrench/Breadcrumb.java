package com.headissue.wrench;

/**
 * @author tobi
 */
public class Breadcrumb {

  public static final Breadcrumb HOME = new Breadcrumb("Home", "/");

  private final String name;
  private final String url;

  public Breadcrumb(String _name, String _url) {

    name = _name;
    url = _url;
  }


  public String getName() {
    return name;
  }

  public String getUrl() {
    return url;
  }

}
