package com.crsn.maven.utils.osgirepo.maven;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeSet;

import org.apache.commons.lang3.tuple.Pair;

public class MavenRepository {

  private final List<MavenArtifact> artifacts;
  private final List<MavenArtifactVersions> artifactVersions;

  public MavenRepository( List<MavenArtifact> artifacts ) {
    if( artifacts == null ) {
      throw new NullPointerException( "Null artefacts." );
    }
    this.artifacts = new LinkedList<MavenArtifact>( artifacts );
    this.artifactVersions = aggregateMavenVersionInformation( artifacts );
  }

  private LinkedList<MavenArtifactVersions> aggregateMavenVersionInformation( List<MavenArtifact> artifacts )
  {
    HashMap<Pair<String, String>, TreeSet<MavenVersion>> versions = groupByGroupAndArtifactId( artifacts );
    LinkedList<MavenArtifactVersions> artifactVersions = new LinkedList<MavenArtifactVersions>();
    for( Entry<Pair<String, String>, TreeSet<MavenVersion>> entry : versions.entrySet() ) {
      MavenArtifactVersions vers = new MavenArtifactVersions( entry.getKey().getLeft(),
                                                              entry.getKey().getRight(),
                                                              entry.getValue() );
      artifactVersions.add( vers );
    }
    return artifactVersions;
  }

  private HashMap<Pair<String, String>, TreeSet<MavenVersion>> groupByGroupAndArtifactId( List<MavenArtifact> artifacts )
  {
    HashMap<Pair<String, String>, TreeSet<MavenVersion>> versions = new HashMap<Pair<String, String>, TreeSet<MavenVersion>>();
    for( MavenArtifact mavenArtifact : artifacts ) {
      Pair<String, String> groupAndId = Pair.of( mavenArtifact.getGroupId(),
                                                 mavenArtifact.getArtifactId() );
      TreeSet<MavenVersion> existingVersions = versions.remove( groupAndId );
      if( existingVersions == null ) {
        existingVersions = new TreeSet<MavenVersion>();
      }
      existingVersions.add( mavenArtifact.getVersion() );
      versions.put( groupAndId, existingVersions );
    }
    return versions;
  }

  public List<MavenArtifact> getArtifacts() {
    return Collections.unmodifiableList( artifacts );
  }

  public List<MavenArtifactVersions> getArtifactVersions() {
    return Collections.unmodifiableList( artifactVersions );
  }
}
