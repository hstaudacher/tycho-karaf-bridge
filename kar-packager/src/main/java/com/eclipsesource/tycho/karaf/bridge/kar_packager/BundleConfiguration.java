package com.eclipsesource.tycho.karaf.bridge.kar_packager;


public class BundleConfiguration {
  
  /**
   * @parameter
   */
  private String name;
  
  /**
   * @parameter
   */
  private int startLevel;
  
  /**
   * @parameter
   */
  private boolean autostart;
  
  public BundleConfiguration() {
    autostart = true;
    startLevel = 80;
  }
  
  public String getName() {
    return name;
  }
  
  public int getStartLevel() {
    return startLevel;
  }
  
  public boolean getAutostart() {
    return autostart;
  }
}
