package com.headissue.wrench;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: wormi
 * Date: 20.12.13
 * Time: 16:30
 * To change this template use File | Settings | File Templates.
 */
public class UtilTest {
  @Test
  public void testRemoveLeadingSlash() {
    assertEquals("", Util.removeLeadingSlash(""));
    assertEquals(null, Util.removeLeadingSlash(null));
    assertEquals("", Util.removeLeadingSlash("/"));
    assertEquals("/", Util.removeLeadingSlash("//"));
    assertEquals("dd", Util.removeLeadingSlash("dd"));
    assertEquals("d/", Util.removeLeadingSlash("d/"));
    assertEquals("d", Util.removeLeadingSlash("/d"));
  }
}
