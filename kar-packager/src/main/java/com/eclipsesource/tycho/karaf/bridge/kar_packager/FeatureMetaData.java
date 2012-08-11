package com.eclipsesource.tycho.karaf.bridge.kar_packager;

import java.io.PrintStream;


public class FeatureMetaData {
  
  private String version;
  private String groupId;
  private String artifactId;

  public FeatureMetaData( String groupId, String artifactId, String version ) {
    this.groupId = groupId;
    this.artifactId = artifactId;
    this.version = version;
  }

  public void write( PrintStream out ) {
    out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    out.println("<metadata modelVersion=\"1.1.0\">");
    out.println("  <groupId>" + groupId + "</groupId>" );
    out.println("  <artifactId>" + artifactId + "</artifactId>" );
    out.println("  <version>" + version + "</version>" );
    out.println( "</metadata>" );
    out.close();
  }
}
