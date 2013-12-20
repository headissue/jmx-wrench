package com.headissue.wrench;

import org.apache.commons.lang.StringUtils;

import javax.management.*;
import java.lang.management.ManagementFactory;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class Wrench{

  private final MBeanServer mbs;
  private final static Wrench instance = new Wrench();
  public static final String ATTRIBUTE = "attr";
  public static final String SIGNATURE = "sig";
  public static final String OPERATION = "op";
  public static final String PARAMETER = "pa";
  public static final String QUERY = "q";
  public static final String SIGNATURE_DELIMITER = ",";
  public static final String VALUE = "val";

  private Wrench() {
    mbs = ManagementFactory.getPlatformMBeanServer();
  }

  public static Wrench getInstance() {
    return instance;
  }

  public String tune(Map<String, String[]> _params) throws MalformedObjectNameException, MBeanException, InstanceNotFoundException, ReflectionException {

    ObjectName _name;
    Object returnValue = null;
    String _operation;
    String[] _parameters;
    String[] _signature = null;

    _name = new ObjectName(_params.get(QUERY)[0]);
    _operation = _params.get(OPERATION)[0];
    _parameters = _params.get(PARAMETER);
    if (StringUtils.isNotBlank(_params.get(SIGNATURE)[0])) {
      _signature = _params.get(SIGNATURE)[0].split(SIGNATURE_DELIMITER);
    }

    if ( _name != null && _operation != null) {
      returnValue = mbs.invoke(_name,  _operation, _parameters,  _signature);
    }

   if (returnValue != null) return returnValue.toString();
    return "nothing";
  }

  public Set<ObjectName> getObjectNames() {
    return new TreeSet<>(mbs.queryNames(null, null));
  }

  public MBeanInfo getInfo(String name) throws MalformedObjectNameException, IntrospectionException, InstanceNotFoundException, ReflectionException {
    MBeanInfo info = mbs.getMBeanInfo(new ObjectName(name));
    return info;
  }

  public String getAttributeValue(String name, String attribute) throws AttributeNotFoundException, MBeanException, InstanceNotFoundException, MalformedObjectNameException {
    Object attr;
    try {
      attr = mbs.getAttribute(new ObjectName(name), attribute);
    } catch (ReflectionException e) {
      e.printStackTrace();
      return "caught ReflectionException";
    }
    if (attr != null) return attr.toString();
    return "null";
  }

  public void setAttribute(String objectName, Map<String, String[]> _params)
    throws MalformedObjectNameException, AttributeNotFoundException, MBeanException, ReflectionException, InstanceNotFoundException, InvalidAttributeValueException, ClassNotFoundException, IntrospectionException {

    String attributeToSet = _params.get(ATTRIBUTE)[0];
    String valueString = _params.get(VALUE)[0];
    MBeanAttributeInfo[] attributes = mbs.getMBeanInfo(new ObjectName(objectName)).getAttributes();
    String typeString = getTypeForAtrribute(attributeToSet, attributes);
    Object value = convertToCorrectlyTypeValue(valueString, typeString);
    Attribute attribute = new Attribute(attributeToSet, value);
    mbs.setAttribute(new ObjectName(objectName), attribute);
  }

  private static Object convertToCorrectlyTypeValue(String value, String type) {
    if ("int".equals(type)) {
      return Integer.valueOf(value);
    } else if ("long".equals(type)) {
      return Long.valueOf(value);
    } else if ("boolean".equals(type)) {
      return Boolean.valueOf(value);
    } else if ("java.util.Date".equals(type)) {
      return new Date(Integer.valueOf(value));
    } else {
      return value;
    }
  }

  private String getTypeForAtrribute(String attributeToSet, MBeanAttributeInfo[] attributes) {
    for (MBeanAttributeInfo attributeInfo : attributes) {
      if (attributeInfo.getName().equals(attributeToSet)) {
        return attributeInfo.getType();
      }
    }
    return null;
  }

  public static String getSignatureString(MBeanParameterInfo[] signature) {
    StringBuilder sb = new StringBuilder();
    for (MBeanParameterInfo mBeanParameterInfo : signature) {
      sb.append(mBeanParameterInfo.getType()).append(SIGNATURE_DELIMITER);
    }
    return sb.toString();
  }

}
