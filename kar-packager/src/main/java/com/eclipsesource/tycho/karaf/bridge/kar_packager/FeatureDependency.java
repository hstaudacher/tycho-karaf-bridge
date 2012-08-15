package com.eclipsesource.tycho.karaf.bridge.kar_packager;


public class FeatureDependency {
  
  /**
   * @parameter
   */
  private String name;
  
  /**
   * @parameter
   */
  private String versionRange;

  
  public String getName() {
    return name;
  }

  
  public String getVersionRange() {
    return versionRange;
  }
  
}
