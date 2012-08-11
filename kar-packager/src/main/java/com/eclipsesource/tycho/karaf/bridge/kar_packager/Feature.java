package com.eclipsesource.tycho.karaf.bridge.kar_packager;

import java.io.PrintStream;
import java.util.List;

import com.crsn.maven.utils.osgirepo.maven.MavenArtifact;

public class Feature {

  private List<MavenArtifact> artifacts;
  private String featureName;
  private String version;

  public Feature( List<MavenArtifact> artifacts, String featureName, String version ) {
    this.artifacts = artifacts;
    this.featureName = featureName;
    this.version = version;
  }

  public void write( PrintStream out ) {
    out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    out.println("<features>");
    out.println( "  <feature name='" + featureName + "' version='" + version + "'>" );
    for( MavenArtifact artifact : artifacts ) {
      out.println( "    <bundle>" + artifact.getURL() + "</bundle>" );
    }
    out.println( "  </feature>" );
    out.println( "</features>" );
    out.close();
  }
}