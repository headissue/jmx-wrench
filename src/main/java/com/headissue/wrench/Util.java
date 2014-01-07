package com.headissue.wrench;

import javax.management.ObjectName;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

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
    StringBuilder encodedProperties = new StringBuilder();
    for (Iterator iterator = keyPropertyMap.keySet().iterator(); iterator.hasNext(); ) {
      String key = (String) iterator.next();
      encodedProperties
        .append(encode(key, encoding))
        .append('=')
        .append(encode(keyPropertyMap.get(key), encoding));
      if (iterator.hasNext()) {
        encodedProperties.append('&');
      }
    }
    return encode(domain, encoding) + '?' + encodedProperties.toString();
  }

  public static String decodeObjectNameQuery(String name, Map<String, String[]> parameters,  String encoding ) throws UnsupportedEncodingException {

    StringBuilder properties = new StringBuilder();

    for (Iterator iterator = parameters.keySet().iterator(); iterator.hasNext(); ) {
      String key = (String) iterator.next();
      properties
        .append(URLDecoder.decode(key, encoding))
        .append('=')
        .append(URLDecoder.decode(parameters.get(key)[0],encoding));
      if (iterator.hasNext()) {
        properties.append(',');
      }

    }
    return URLDecoder.decode(name, encoding) + ":"  + properties;

  }

}
