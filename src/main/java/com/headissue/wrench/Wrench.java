package com.headissue.wrench;

import org.apache.commons.lang3.StringUtils;

import javax.management.Attribute;
import javax.management.JMException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanParameterInfo;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

/**
 * Executed operations on a managed bean server.
 *
 * @author wormi
 */
public class Wrench {

  private final MBeanServer managedBeanServer;
  private static final Wrench instance = new Wrench();
  public static final String ATTRIBUTE = "attr";
  public static final String SIGNATURE = "sig";
  public static final String OPERATION = "op";
  public static final String PARAMETER = "pa";
  public static final String QUERY = "q";
  public static final String SIGNATURE_DELIMITER = ",";
  public static final String VALUE = "val";
  public static final String CLASS = "class";

  private Wrench() {
    managedBeanServer = ManagementFactory.getPlatformMBeanServer();
  }

  public static Wrench getInstance() {
    return instance;
  }

  /**
   * Invokes an operation with the given parameters and signature on an object managed by the MBean Server
   * @param _name
   * @param _operation
   * @param _parameters
   * @param _signature
   * @return
   * @throws JMException
   */
  public Object invoke(ObjectName _name, String _operation, String[] _parameters, String[] _signature) throws JMException {
    return managedBeanServer.invoke(_name, _operation, _parameters, _signature);
  }

  public Object invoke(Map<String, String[]> _params) {

    String _operation = _params.get(OPERATION)[0];
    String[] _operationParameters = _params.get(PARAMETER);

    String[] _signature = null;
    if (StringUtils.isNotBlank(_params.get(SIGNATURE)[0])) {
      _signature = _params.get(SIGNATURE)[0].split(SIGNATURE_DELIMITER);
    }

    ObjectName _name;
    try {
      _name = new ObjectName(_params.get(QUERY)[0]);
      Object _result = invoke(_name,  _operation, _operationParameters,  _signature);
      return _result;
    } catch (JMException e) {
      return  "<span class=\"error\">could not execute invoke<span>" + e;
    }
  }

  /**
   * Returns all Beans registered in the MBeanServer. When an objects classPath is provided, the result is
   * filtered.
   * @param _query See {@link javax.management.ObjectName} for valid queries
   * @return Set of registered Beans
   * @throws Exception
   */
  public Set<ObjectName> findObjectNames(String _query) throws Exception{
    ObjectName _queryObjectName = null;
    if (StringUtils.isNotBlank(_query)) {
      if (_query.contains(":")) {
        _queryObjectName =new ObjectName(_query + "*");
      } else {
        _queryObjectName = new ObjectName(_query+"*:*");
      }
    }
    return new TreeSet<>(managedBeanServer.queryNames(_queryObjectName, null));
  }

  public Set<ObjectName> findAllObjectNames() throws Exception{
    return findObjectNames("");
  }

  /**
   * @param _objectName
   * @param _attributeInfo
   * @return
   * @throws Exception
   */
  public Class<?> getAttributeType(ObjectName _objectName, MBeanAttributeInfo _attributeInfo) throws Exception {
    Object _attribute = managedBeanServer.getAttribute(_objectName, _attributeInfo.getName());
    if (_attribute == null) {
      return Class.forName(_attributeInfo.getType());
    }
    if (_attribute instanceof Object[]) {
      return ((Object[]) _attribute).getClass();
    }
    return _attribute.getClass();
  }

  /**
   * Reads a specific value from a Bean
   * @param _objectName
   * @param _attributeName
   * @return
   * @throws Exception
   */
  public String getAttributeValue(ObjectName _objectName, String _attributeName) throws Exception {
    Object _attribute = managedBeanServer.getAttribute(_objectName, _attributeName);
    if (_attribute == null) {
      return "null";
    }
    if (_attribute instanceof Object[]) {
      return Arrays.toString((Object[]) _attribute);
    }
    return _attribute.toString();
  }

  public void setBeanAttribute(String _objectName, Map<String, String[]> _params) throws Exception {
    String _attributeToSet = _params.get(ATTRIBUTE)[0];
    String _value = _params.get(VALUE)[0];
    setBeanAttribute(_objectName, _attributeToSet, _value);
  }

  public void setBeanAttribute(String _objectName, String _attributeName, String _value) throws Exception {
    MBeanAttributeInfo[] _beanAttributes = managedBeanServer.getMBeanInfo(new ObjectName(_objectName)).getAttributes();
    String _typeString = getTypeOfAttribute(_attributeName, _beanAttributes);
    Object v = convertToCorrectlyTypedValue(_value, _typeString);
    Attribute _attribute = new Attribute(_attributeName, v);
    managedBeanServer.setAttribute(new ObjectName(_objectName), _attribute);
  }

  private static Object convertToCorrectlyTypedValue(String _value, String _type) {
    switch (_type) {
      case "int": // Fall through
      case "long":
        return Integer.valueOf(_value);
      case "Date":
        return new Date(Integer.valueOf(_value));
      case "boolean":
        return Boolean.valueOf(_value);
      case "double":
        return Double.valueOf(_value);
    }
    return _value;
  }

  private String getTypeOfAttribute(String _attribute, MBeanAttributeInfo[] _beanAttributes) {
    for (MBeanAttributeInfo _attributeInfo : _beanAttributes) {
      if (_attributeInfo.getName().equals(_attribute)) {
        return _attributeInfo.getType();
      }
    }
    return null;
  }

  public static String getSignature(MBeanParameterInfo[] _signature) {
    StringBuilder sb = new StringBuilder();
    for (MBeanParameterInfo _mBeanParameterInfo : _signature) {
      sb.append(_mBeanParameterInfo.getType()).append(SIGNATURE_DELIMITER);
    }
    return sb.toString();
  }

  public MBeanInfo getInfo(ObjectName _objectName) throws Exception {
    return managedBeanServer.getMBeanInfo(_objectName);
  }

  /**
   * Finds JMX beans where the names matches the given regex query.
   * More specifically, it uses the regular expression {@code ".*" + query + ".*"} to find {@link ObjectName}s
   * @param _query
   * @return
   * @throws Exception
   */
  public Set<ObjectName> queryObjectNamesUsingRegExp(String _query) throws Exception {
    if (_query == null) {
      return findAllObjectNames();
    }
    _query = pimpQueryRegExp(_query);
    Pattern _pattern = Pattern.compile(_query, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CHARACTER_CLASS);
    Set<ObjectName> _allObjectNames = findAllObjectNames();
    Iterator<ObjectName> _nameIterator = _allObjectNames.iterator();
    LinkedHashSet<ObjectName> _objectNameSet = new LinkedHashSet<>();
    while (_nameIterator.hasNext()) {
      ObjectName _objectName = _nameIterator.next();
      String _name = _objectName.getCanonicalName();
      if (!_pattern.matcher(_name).find()) {
        continue;
      }
      _objectNameSet.add(_objectName);
    }
    return _objectNameSet;
  }

  public String pimpQueryRegExp(String _query) {
    StringBuilder _queryBuilder = new StringBuilder();
    if (!_query.startsWith("^")) {
      _queryBuilder.append(".*");
    }
    _queryBuilder.append(_query);
    if (!_query.endsWith("$")) {
      _queryBuilder.append(".*");
    }
    _query = _queryBuilder.toString();
    return _query;
  }

  public boolean isRegistered(ObjectName _objectName) {
    return managedBeanServer.isRegistered(_objectName);
  }
}
