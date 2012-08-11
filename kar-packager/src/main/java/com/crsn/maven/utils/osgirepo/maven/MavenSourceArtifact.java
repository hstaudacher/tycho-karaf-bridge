package com.crsn.maven.utils.osgirepo.maven;

import java.io.File;
import java.util.Collections;

public class MavenSourceArtifact extends MavenArtifact {

  public MavenSourceArtifact( String groupId, String artifactId, MavenVersion version, File content )
  {
    super( groupId, artifactId, version, Collections.<MavenDependency> emptyList(), content, "", "" );
  }
}
