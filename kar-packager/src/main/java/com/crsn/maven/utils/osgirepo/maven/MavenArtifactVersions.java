package com.crsn.maven.utils.osgirepo.maven;

import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

public class MavenArtifactVersions {

  private final String groupId;
  private final String artifactId;
  private final SortedSet<MavenVersion> versions;

  public MavenArtifactVersions( String groupId, String artifactId, SortedSet<MavenVersion> versions )
  {
    this.groupId = groupId;
    this.artifactId = artifactId;
    this.versions = new TreeSet<MavenVersion>( versions );
  }

  public String getGroupId() {
    return groupId;
  }

  public String getArtifactId() {
    return artifactId;
  }

  public SortedSet<MavenVersion> getVersions() {
    return Collections.unmodifiableSortedSet( versions );
  }
}
