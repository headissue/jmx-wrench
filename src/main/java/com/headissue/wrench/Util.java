package com.headissue.wrench;

import javax.management.ObjectName;
import java.io.UnsupportedEncodingException;
import java.util.Hashtable;
import java.util.Iterator;

import static java.net.URLEncoder.encode;

/**
 * Created with IntelliJ IDEA.
 * User: wormi
 * Date: 20.12.13
 * Time: 16:24
 * To change this template use File | Settings | File Templates.
 */
public class Util {
  public static String removeLeadingSlash(String s) {
    if (s != null) {
      if (s.length() > 0 && '/' == s.charAt(0)) {
        if (s.length() > 1) {
          return s.substring(1);
        } else {
          return "";
        }
      }
    }
    return s;
  }

  public static String encodeObjectNameQuery(ObjectName name, String encoding ) throws UnsupportedEncodingException {
    String domain = name.getDomain();
    Hashtable<String, String> keyPropertyMap = name.getKeyPropertyList();
    StringBuilder properties = new StringBuilder();
    for (Iterator iterator = keyPropertyMap.keySet().iterator(); iterator.hasNext(); ) {
      String key = (String) iterator.next();
      properties.append(key).append("=").append(keyPropertyMap.get(key));

    }
    return encode(domain, encoding) + "?" + encode(properties.toString(), encoding);
  }
}
