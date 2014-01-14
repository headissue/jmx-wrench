package com.headissue.wrench;

import javax.management.MBeanParameterInfo;
import javax.management.ObjectName;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import static java.net.URLEncoder.encode;

public class Util {

  /**
   * Note: Returns null if null is given as parameter.
   * @param s
   * @return
   */
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

  /**
   * encodes an ObjectName to URL path with query parameters
   * @param name
   * @param encoding
   * @return
   * @throws UnsupportedEncodingException
   */
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

  public static String decodeObjectNameQuery(String objectClassPath, Map<String, String[]> queryParameters,  String encoding ) throws UnsupportedEncodingException {

    // objectName as query parameter
    if (queryParameters.get(Wrench.CLASS) != null) {
      return URLDecoder.decode(queryParameters.get(Wrench.CLASS)[0], encoding).trim();
    }

    // url is like objectClassPath?key=value&key=value
    StringBuilder properties = new StringBuilder();
    for (Iterator iterator = queryParameters.keySet().iterator(); iterator.hasNext(); ) {
      String key = (String) iterator.next();
      properties
        .append(URLDecoder.decode(key, encoding))
        .append('=')
        .append(URLDecoder.decode(queryParameters.get(key)[0],encoding));
      if (iterator.hasNext()) {
        properties.append(',');
      }
    }
    return URLDecoder.decode(objectClassPath, encoding) + ":"  + properties;

  }

  /**
   * the "(Type1 param1, Type2 param2)" kind of signature
   * @param signature
   * @return
   */
  public static String humanReadableSignature(MBeanParameterInfo[] signature) {
    StringBuilder signatureString = new StringBuilder();
    for (int i = 0; i < signature.length; i++) {
      MBeanParameterInfo mBeanParameterInfo = signature[i];
      signatureString.append("(").append(mBeanParameterInfo.getType()).append(" ").append(mBeanParameterInfo.getName()).append(")");
    }
    return signatureString.toString();
  }

}
