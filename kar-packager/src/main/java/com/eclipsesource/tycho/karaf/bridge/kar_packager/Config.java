package com.eclipsesource.tycho.karaf.bridge.kar_packager;


public class Config {
  
  /**
   * @parameter
   */
  private String name;
  
  /**
   * @parameter
   */
  private String key;
  
  /**
   * @parameter
   */
  private String value;

  
  public String getName() {
    return name;
  }
  
  public String getKey() {
    return key;
  }
  
  public String getValue() {
    return value;
  }
  
}
