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

  public Object invoke(ObjectName _name, String _operation, String[] _parameters, String[] _signature) throws Exception {
    return mbs.invoke(_name, _operation, _parameters, _signature);
  }

  public Object invoke(Map<String, String[]> _params) throws Exception {

    ObjectName _name;
    String _operation;
    String[] _parameters;
    String[] _signature = null;

    _name = new ObjectName(_params.get(QUERY)[0]);
    _operation = _params.get(OPERATION)[0];
    _parameters = _params.get(PARAMETER);
    if (StringUtils.isNotBlank(_params.get(SIGNATURE)[0])) {
      _signature = _params.get(SIGNATURE)[0].split(SIGNATURE_DELIMITER);
    }
    return invoke(_name,  _operation, _parameters,  _signature);
  }

  /**
   * Returns all Beans registered in the MBeanServer. When an ObjectName is provided, the result is
   * filtered.
   * @param q
   * @return Set of registered Beans
   * @throws Exception
   */
  public Set<ObjectName> queryObjectNames(String q) throws Exception, MalformedObjectNameException{
    ObjectName _query = null;
    if (StringUtils.isNotBlank(q)) {
      if (q.contains(":")) {
        _query =new ObjectName(q + "*");
      } else {
        _query = new ObjectName(q+"*:*");
      }
    }
    return new TreeSet<>(mbs.queryNames(_query, null));
  }

  public Set<ObjectName> getAllObjectNames() throws Exception{
    return queryObjectNames("");
  }


    public MBeanInfo getInfo(ObjectName objectName) throws Exception {
    MBeanInfo info = mbs.getMBeanInfo(objectName);
    return info;
  }

  /**
   * queries attribute values and returns them in their toString() form. When read is denied, an error message is returned
   * @param name
   * @param attribute
   * @return
   * @throws AttributeNotFoundException
   * @throws MBeanException
   * @throws InstanceNotFoundException
   * @throws MalformedObjectNameException
   */
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

  public void setBeanAttribute(String objectName, Map<String, String[]> _params) throws Exception {
    String _attributeToSet = _params.get(ATTRIBUTE)[0];
    String _value = _params.get(VALUE)[0];
    setBeanAttribute(objectName, _attributeToSet, _value);
  }

public void setBeanAttribute(String _objectName, String _attribute, String _value) throws Exception {
    MBeanAttributeInfo[] beanAttributes = mbs.getMBeanInfo(new ObjectName(_objectName)).getAttributes();
    String typeString = getTypeOfAttribute(_attribute, beanAttributes);
    Object v = convertToCorrectlyTypeValue(_value, typeString);
    Attribute attribute = new Attribute(_attribute, v);
    mbs.setAttribute(new ObjectName(_objectName), attribute);
  }

  private static Object convertToCorrectlyTypeValue(String _value, String _type) {
    switch (_type) {
      case "int": return Integer.valueOf(_value);
      case "long": return Integer.valueOf(_value);
      case "Date":  return new Date(Integer.valueOf(_value));
      case "boolean": return Boolean.valueOf(_value);
    }
    return _value;
  }

  private String getTypeOfAttribute(String _attribute, MBeanAttributeInfo[] _beanAttributes) {
    for (MBeanAttributeInfo attributeInfo : _beanAttributes) {
      if (attributeInfo.getName().equals(_attribute)) {
        return attributeInfo.getType();
      }
    }
    return null;
  }

  public static String getSignatureString(MBeanParameterInfo[] _signature) {
    StringBuilder sb = new StringBuilder();
    for (MBeanParameterInfo mBeanParameterInfo : _signature) {
      sb.append(mBeanParameterInfo.getType()).append(SIGNATURE_DELIMITER);
    }
    return sb.toString();
  }

}
