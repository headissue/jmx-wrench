package com.headissue.wrench;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by wormi on 24.03.14.
 */
public class BreadcrumbTest {
  @Test
  public void testBuildCrumbs() throws Exception {
    List<Breadcrumb> breadcrumbList = Breadcrumb.buildCrumbs("com.headissue.crunchy:param=value", Breadcrumb.FORMAT_FQON);
    assertEquals(3, breadcrumbList.size());
    assertEquals(null, breadcrumbList.get(0).getParent());
    assertEquals(breadcrumbList.get(1), breadcrumbList.get(2).getParent());
    assertEquals("crunchy", breadcrumbList.get(2).getName());


  }
}
