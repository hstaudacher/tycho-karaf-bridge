package com.eclipsesource.tycho.karaf.bridge.kar_packager;

import java.io.PrintStream;
import java.util.List;

import com.crsn.maven.utils.osgirepo.maven.MavenArtifact;
import com.crsn.maven.utils.osgirepo.util.OsgiToMavenMapper;

public class Feature {

  private List<MavenArtifact> artifacts;
  private String featureName;
  private String version;
  private List<BundleConfiguration> bundlesConfiguration;

  public Feature( List<MavenArtifact> artifacts,
                  String featureName,
                  String version,
                  List<BundleConfiguration> bundlesConfiguration )
  {
    this.artifacts = artifacts;
    this.featureName = featureName;
    this.version = version;
    this.bundlesConfiguration = bundlesConfiguration;
  }

  public void write( PrintStream out ) {
    out.println( "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" );
    out.println( "<features>" );
    out.println( "  <feature name='" + featureName + "' version='" + version + "'>" );
    for( MavenArtifact artifact : artifacts ) {
      BundleConfiguration configuration = getConfiguration( artifact ); 
      if( configuration != null  ) {
        out.println( getConfiguredBundle( artifact, configuration ) );
      } else {
        out.println( "    <bundle>" + artifact.getURL() + "</bundle>" );
      }
    }
    out.println( "  </feature>" );
    out.println( "</features>" );
    out.close();
  }

  private String getConfiguredBundle( MavenArtifact artifact, BundleConfiguration configuration ) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append( "    <bundle" );
    if( configuration.getStartLevel() != 80 ) {
      stringBuilder.append( " start-level='" + configuration.getStartLevel() + "'" );
    }
    if( !configuration.getAutostart() ) {
      stringBuilder.append( " start='false'" );
    }
    stringBuilder.append( ">" );
    stringBuilder.append( artifact.getURL() );
    stringBuilder.append( "</bundle>" );
    String configuredBundle = stringBuilder.toString();
    return configuredBundle;
  }

  private BundleConfiguration getConfiguration( MavenArtifact artifact ) {
    BundleConfiguration result = null;
    if( bundlesConfiguration != null ) {
      for( BundleConfiguration bundleConfiguration : bundlesConfiguration ) {
        if( isConfigurationForArtifact( artifact, bundleConfiguration.getName() ) ) {
          result = bundleConfiguration;
          break;
        }
      }
    }
    return result;
  }

  private boolean isConfigurationForArtifact( MavenArtifact artifact, String bundleName ) {
    return artifact.getArtifactId().equals( OsgiToMavenMapper.createArtifactName( bundleName ) )
           && artifact.getGroupId().equals( OsgiToMavenMapper.createGroupId( bundleName ) );
  }
}