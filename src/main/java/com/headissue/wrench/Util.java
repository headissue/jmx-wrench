package com.headissue.wrench;

import javax.management.MBeanParameterInfo;
import javax.management.ObjectName;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Map;

import static java.net.URLDecoder.decode;
import static java.net.URLEncoder.encode;

/**
 * Provides utility functions for URL manipulation and processing
 *
 * @author wormi
 * @see <a href="https://to.headissue.net/radar/browse/MTP-4990">MTP-4990</a>
 */
public class Util {

  /**
   * Note: Returns null if null is given as parameter.
   * @param s
   * @return
   */
  public static String removeLeadingSlash(String s) {
    if (s != null) {
      s = s.replaceFirst("^/","");
    } else {
      s = "";
    }
    return s;
  }

  /**
   * encodes an ObjectName to URL path with query parameters
   * @param _name
   * @param _encoding
   * @return
   * @throws UnsupportedEncodingException
   */
  public static String encodeObjectNameQuery(ObjectName _name, String _encoding ) throws UnsupportedEncodingException {
    String _domain = _name.getDomain();
    Map<String, String> _keyPropertyMap = _name.getKeyPropertyList();
    StringBuilder _encodedProperties = new StringBuilder();
    for (Iterator iterator = _keyPropertyMap.keySet().iterator(); iterator.hasNext(); ) {
      String key = (String) iterator.next();
      _encodedProperties
        .append(encode(key, _encoding))
        .append('=')
        .append(encode(_keyPropertyMap.get(key), _encoding));
      if (iterator.hasNext()) {
        _encodedProperties.append('&');
      }
    }
    return encode(_domain, _encoding) + '?' + _encodedProperties.toString();
  }

  /**
   * Turns the given fully qualified object name and the given query perameters into a decoded URL string
   * @param _objectClassPath
   * @param _queryParameters
   * @param _encoding
   * @return
   * @throws UnsupportedEncodingException
   */
  public static String decodeObjectNameQuery(
    String _objectClassPath, Map<String, String[]> _queryParameters,  String _encoding )
    throws UnsupportedEncodingException {

    // objectName as query parameter
    if (_queryParameters.get(Wrench.CLASS) != null) {
      return decode(_queryParameters.get(Wrench.CLASS)[0], _encoding).trim();
    }

    // url is like _objectClassPath?key=value&key=value
    // When Java 8 is out, we can define a new method which receives either URLDecoder.decode or URLEncoder.encode
    // as argument and unify the rest into that method
    StringBuilder _properties = new StringBuilder();
    for (Iterator iterator = _queryParameters.keySet().iterator(); iterator.hasNext(); ) {
      String key = (String) iterator.next();
      _properties
        .append(decode(key, _encoding))
        .append('=')
        .append(decode(_queryParameters.get(key)[0], _encoding));
      if (iterator.hasNext()) {
        _properties.append(',');
      }
    }
    return decode(_objectClassPath, _encoding) + ":"  + _properties;
  }


  /**
   * the "(Type1 param1, Type2 param2)" kind of _signature
   * @param _signature
   * @return
   */
  public static String humanReadableSignature(MBeanParameterInfo[] _signature) {
    StringBuilder _signatureString = new StringBuilder();
    for (MBeanParameterInfo mBeanParameterInfo : _signature) {
      _signatureString
        .append("(")
        .append(mBeanParameterInfo.getType()).append(" ").append(mBeanParameterInfo.getName())
        .append(")");
    }
    return _signatureString.toString();
  }

  /**
   * Inserts a zero-width character into the input string after each given characters.
   * @param _input
   * @param _characters
   * @return
   */
  public static String breakAtChars(String _input, char... _characters) {

    for (char c : _characters) {
      _input = _input.replace(c+"",c + "&#8203;");
    }
    return _input;
  }

  /**
   * Inserts zero-width spaces as HTML entities after common non-word characters
   * @param _input
   * @return
   */
  public static String breakAtAsciiNonword(String _input) {
    char[] _chars = new char[(47 - 33 + 1) + (64 - 58 + 1) + (96-91 + 1)];
    int idx  = 0;
    for (char c = 33;c<=47;c++) {
      _chars[idx] = c;
      idx++;
    }
    for (char c = 58;c<=64;c++) {
      _chars[idx] = c;
      idx++;
    }
    for (char c = 91;c<=96;c++) {
      _chars[idx] = c;
      idx++;
    }
    return breakAtChars(_input, _chars);
  }
}