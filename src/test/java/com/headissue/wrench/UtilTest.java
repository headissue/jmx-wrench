package com.headissue.wrench;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UtilTest {
  @Test
  public void testRemoveLeadingSlash() {
    assertEquals("", Util.removeLeadingSlash(null));
    assertEquals("", Util.removeLeadingSlash(""));
    assertEquals("", Util.removeLeadingSlash("/"));
    assertEquals("/", Util.removeLeadingSlash("//"));
    assertEquals("dd/", Util.removeLeadingSlash("/dd/"));
    assertEquals("dd", Util.removeLeadingSlash("dd"));
    assertEquals("d/", Util.removeLeadingSlash("d/"));
    assertEquals("d", Util.removeLeadingSlash("/d"));
  }

  @Test
  public void testBreakAtNonwords() throws Exception {
    String _input = "aA^°!\"§$%&/()=?{[]}\\*+~'#_-:.;,";
    assertEquals("aA^&#8203;°!&&#8203;&#8203;#&&#8203;&#8203;#8203;&#8203;8203;&#8203;" +
        "\"&&#8203;&#8203;#&&#8203;" +
        "&#8203;#8203;&#8203;8203;&#8203;§$&&#8203;&#8203;#8203;&#8203;%&&#8203;&#8203" +
        ";#8203;&#8203;&&#8203;&#8203;/&#8203;&#8203;(&#8203;&#8203;)&#8203;&#8203;=&#8203;" +
        "?&#8203;{[&#8203;]&#8203;}\\&#8203;*&#8203;&#8203;+&#8203;&#8203;~'&#8203;&#8203;#&&#8203;" +
        "&#8203;#8203;&#8203;_&#8203;-&#8203;&#8203;:&#8203;&#8203;.&#8203;&#8203;;&#8203;,&#8203;&#8203;",
      Util.breakAtAsciiNonword(_input));
  }
}
