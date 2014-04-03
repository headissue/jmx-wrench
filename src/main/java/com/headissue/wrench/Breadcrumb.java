package com.headissue.wrench;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Breadcrumb {

  public static final Breadcrumb ROOT = new Breadcrumb("ROOT", null);

  private final String name;
  private Breadcrumb parent;

  public static final String FORMAT_FQON = "fullQualifiedObjectName";

  public Breadcrumb(String _name, Breadcrumb _parent) {

    name = _name;
    parent = _parent;
  }


  public String getName() {
    return name;
  }

  public Breadcrumb getParent() { return parent; }

  public static List<Breadcrumb> buildCrumbs(String s, String format) throws IOException {

    if (s == null) return null;
    if (FORMAT_FQON.equals(format)) {
      return buildCrumbsFromFQON(s);
    }

    //no format is a problem
    throw new IOException("Format '" + format + "'not supported");

  }

  public static List<Breadcrumb> buildCrumbsFormSortedList( List<String> ar ) {
    List<Breadcrumb> breadcrumbs = new LinkedList<>();
    Breadcrumb parent = null;
    for (Iterator<String> iterator = ar.iterator(); iterator.hasNext(); ) {
      String s = iterator.next();
      Breadcrumb entry = new Breadcrumb(s, parent);
      breadcrumbs.add(entry);
      parent = entry;
    }
    return breadcrumbs;
  }

  /**
   * Expects input like "com.sun.management:type=HotSpotDiagnostics"
   * @param s
   * @return
   */
  private static List<Breadcrumb> buildCrumbsFromFQON(String s) {

    String className = s.split(":")[0];
    String[] entries = className.split("\\.");

    return buildCrumbsFormSortedList(Arrays.asList(entries));
  }

}
