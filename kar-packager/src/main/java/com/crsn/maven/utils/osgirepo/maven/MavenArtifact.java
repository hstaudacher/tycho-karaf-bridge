package com.crsn.maven.utils.osgirepo.maven;

import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class MavenArtifact {

  private final String groupId;
  private final String artifactId;
  private final MavenVersion version;
  private final File content;
  private final List<MavenDependency> dependencies;
  private final String name;
  private final String organization;

  public MavenArtifact( String groupId,
                        String artifactId,
                        MavenVersion version,
                        List<MavenDependency> dependencies,
                        File content,
                        String name,
                        String organization )
  {
    if( groupId == null ) {
      throw new NullPointerException( "Null groupId." );
    }
    this.groupId = groupId;
    if( artifactId == null ) {
      throw new NullPointerException( "Null artifactId." );
    }
    this.artifactId = artifactId;
    if( version == null ) {
      throw new NullPointerException( "Null version" );
    }
    this.version = version;
    if( dependencies == null ) {
      throw new NullPointerException( "Null dependencies" );
    }
    this.dependencies = new LinkedList<MavenDependency>( dependencies );
    if( content == null ) {
      throw new NullPointerException( "Null content." );
    }
    this.content = content;
    if( name == null ) {
      throw new NullPointerException( "Null name." );
    }
    this.organization = organization;
    if( organization == null ) {
      throw new NullPointerException( "Null organization." );
    }
    this.name = name;
  }

  public String getGroupId() {
    return groupId;
  }

  public String getArtifactId() {
    return artifactId;
  }

  public MavenVersion getVersion() {
    return version;
  }

  public File getContent() {
    return content;
  }

  public List<MavenDependency> getDependencies() {
    return Collections.unmodifiableList( dependencies );
  }

  public String getName() {
    return name;
  }

  public String getOrganization() {
    return organization;
  }

  @Override
  public String toString() {
    return String.format( "%s:%s:%s", groupId, artifactId, version );
  }

  public String getURL() {
    return "mvn:" + groupId + "/" + artifactId + "/" + version.toString();
  }
}
