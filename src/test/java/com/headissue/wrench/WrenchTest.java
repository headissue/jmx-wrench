package com.headissue.wrench;

import org.junit.Test;

import javax.management.ObjectName;
import java.util.Iterator;
import java.util.Set;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;


/**
 * Created with IntelliJ IDEA.
 * User: wormi
 * Date: 24.12.13
 * Time: 17:19
 * To change this template use File | Settings | File Templates.
 */
public class WrenchTest {

  Wrench wrench = Wrench.getInstance();

  @Test
  public void testGetInstance() throws Exception {
    assertNotNull(Wrench.getInstance());
  }

  @Test
  public void testInvokeWithMap() throws Exception {

  }

  @Test
  public void testInvoke() throws Exception {

  }

  @Test
  public void testQueryObjectNames() throws Exception {
    Set<ObjectName> filtered = wrench.queryObjectNames("java.la");
    Set<ObjectName> all = wrench.queryObjectNames("");
    Set<ObjectName> allToo = wrench.queryObjectNames(null);
    Set<ObjectName> nothing = wrench.queryObjectNames("bullshit*:*");

    assertTrue(filtered.size() > 0);
    assertTrue(filtered.size() < all.size());
    assertTrue(all.size() > 0 && all.size() == allToo.size());
    assertEquals(0, nothing.size());
  }

  @Test
  public void testQueryFullObjectNames() throws Exception {
    assertEquals(1, wrench.queryObjectNames("java.lang:type=Runtime").size());
  }

  @Test
  public void testGetAllObjectNames() throws Exception{
    Set<ObjectName> objectNames = wrench.getAllObjectNames();
    assertTrue(objectNames.size() > 0);
    for (Iterator<ObjectName> objectNameIterator = objectNames.iterator(); objectNameIterator.hasNext(); ) {
      ObjectName next = objectNameIterator.next();
      System.out.println(next);
    }
    assertTrue(objectNames.contains(new ObjectName("java.lang:type=Memory")));
  }

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

  }
}
