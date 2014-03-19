package com.headissue.wrench;

import org.junit.Test;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import java.util.Set;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;


 /**
 * @author wormi
 * @see <a href="https://to.headissue.net/radar/browse/MTP-4990">MTP-4990</a>
 */
public class WrenchTest {

  Wrench wrench = Wrench.getInstance();

  @Test
  public void testGetInstance() throws Exception {
    assertNotNull("getInstance always returns a new Wrench instance", Wrench.getInstance());
  }

  //  FIXME
  /*
  @Test
  public void testInvokeWithMap() throws Exception {

  }

  @Test
  public void testInvoke() throws Exception {

  }
 */
  @Test
  public void testQueryObjectNames() throws Exception {
    Set<ObjectName> _filtered = wrench.findObjectNames("java.la");
    Set<ObjectName> _allEmptyString = wrench.findObjectNames("");
    Set<ObjectName> _allNull = wrench.findObjectNames(null);
    try {
      wrench.findObjectNames("bullshit*:*");
    } catch (MalformedObjectNameException e) {}

    assertTrue("A list of all objects in the classpath is available", !_allEmptyString.isEmpty());
    assertTrue("'null' and an empty String returns the same amount of objects",
      _allEmptyString.size() == _allNull.size());
    assertTrue("There are objects in the java.la* packages", !_filtered.isEmpty());
    assertTrue("There objects in the java.la* packages are a subset of all packages",_filtered.size() < _allEmptyString.size());

  }

  @Test
  public void testQueryFullObjectNames() throws Exception {
    Set<ObjectName> _result = wrench.findObjectNames("java.lang:type=Runtime");
    assertEquals("There is exactly one java.lang.Runtime object name", 1,_result.size());
  }

  @Test
  public void testGetAllObjectNames() throws Exception{
    Set<ObjectName> _objectNames = wrench.findAllObjectNames();
    assertTrue("At least one object is returned", !_objectNames.isEmpty());
    /*
    // Do not use System.out.println()!
    for (ObjectName _next : _objectNames) {
      System.out.println(_next);
    } */
    assertTrue("The object java.lang.Memory is contained in the result",
      _objectNames.contains(new ObjectName("java.lang:type=Memory")));
  }

 //   FIXME
 /*
  @Test
  public void testGetInfo() throws Exception {

  }

  @Test
  public void testGetAttributeValue() throws Exception {

  }

  @Test
  public void testSetBeanAttributeWithMap() throws Exception {

  }

  @Test
  public void testSetBeanAttribute() throws Exception {

  }

  @Test
  public void testGetSignatureString() throws Exception {

  }  */
}
