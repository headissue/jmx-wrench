package com.headissue.wrench;

import com.headissue.sediments.runtime.JmxUtil;
import com.headissue.sensepitch.proxy.PortalWebshopProxy;

import javax.management.JMX;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;


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

  public void setWatermarkForTestShop (int _sessions) {
    portalWebshopProxy.setSessionOverloadWatermark(_sessions);
  }

  public void setMaxSessionsForTestShop(int _sessions) {
    portalWebshopProxy.setSessionLimit(_sessions);
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



}
