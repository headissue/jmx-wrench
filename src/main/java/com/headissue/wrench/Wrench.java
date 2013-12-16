package com.headissue.wrench;

import com.headissue.sediments.runtime.JmxUtil;
import com.headissue.sensepitch.proxy.PortalWebshopProxy;

import javax.management.JMX;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.util.Iterator;
import java.util.Map;


/**
 * Created with IntelliJ IDEA.
 * User: wormi
 * Date: 09.12.13
 * Time: 08:57
 * To change this template use File | Settings | File Templates.
 */
public class Wrench{

  private final MBeanServer mbs;


  private final ObjectName portalWebshopProxyName;
  private final PortalWebshopProxy.MgmtMBean portalWebshopProxy;



  public Wrench() {
    mbs = ManagementFactory.getPlatformMBeanServer();
    try {
      portalWebshopProxyName = new ObjectName(JmxUtil.constructName(PortalWebshopProxy.class, "testShopProxy"));
    } catch (MalformedObjectNameException e) {
      throw new RuntimeException(e);
    }
    portalWebshopProxy = JMX.newMBeanProxy(mbs, portalWebshopProxyName, PortalWebshopProxy.MgmtMBean.class);

  }

  public void tune(Map<String, String[]> _params) {
    for (Iterator<String> iterator = _params.keySet().iterator(); iterator.hasNext(); ) {
      String key = iterator.next();
      String[] values = _params.get(key);

      try {
        if ("setMax".equals(key)) {
          setMaxSessionsForTestShop(Integer.valueOf(values[0]));
        } else if ("setWatermark".equals(key)) {
          setWatermarkForTestShop(Integer.valueOf(values[0]));
        }
      } catch (Exception e) {
        e.printStackTrace();
      }

      String concatenatedValues = "";
      for (int i = 0; i < values.length; i++) {
        concatenatedValues += values[i] + ", ";
      }
    }
  }

  public void setWatermarkForTestShop (int _sessions) {
    if (_sessions < getSessionLimitOfTestShop() && _sessions > 0) {
      portalWebshopProxy.setSessionOverloadWatermark(_sessions);
    }
  }

  public void setMaxSessionsForTestShop(int _sessions) {
    if (_sessions > 0 ) {
      portalWebshopProxy.setSessionLimit(_sessions);
    }
  }

  public int getActiveSessionsOfTestShop() {
    return portalWebshopProxy.getActiveSessions();
  }

  public int getSessionLimitOfTestShop() {
    return portalWebshopProxy.getSessionLimit();
  }

  public int getWatermarkOfTestShop() {
    return portalWebshopProxy.getSessionOverloadWatermark();
  }


  public static String help() {
    StringBuilder help = new StringBuilder();
    help.append("<p>").append("valid parameters are:").append("</p>");
    help.append("<p>").append("setMax - to set allowed sessions").append("</p>");
    help.append("<p>").append("setWatermark - to set where the warning kicks in").append("</p>");
    help.append("<p>").append("like <a href=\"\">https://mt.demo.h7e.eu/wrench/tune.jsp?setMax=30&setWatermark=20</a>").append("</p>");

    return help.toString();
  }
}
