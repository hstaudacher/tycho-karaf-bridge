package com.crsn.maven.utils.osgirepo.maven;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Test;

import com.crsn.maven.utils.osgirepo.maven.builder.MavenArtifactBuilder;
import com.crsn.maven.utils.osgirepo.maven.builder.MavenRepositoryBuilder;

public class MavenRepositoryTest {

  @Test
  public void canBuildArtifactVersions() {
    MavenRepositoryBuilder builder = new MavenRepositoryBuilder();
    buildArtifact( builder, "g1", "a", new MavenVersion( 1 ) );
    buildArtifact( builder, "g1", "a", new MavenVersion( 2 ) );
    MavenRepository repo = builder.build();
    assertEquals( 1, repo.getArtifactVersions().size() );
  }

  private void buildArtifact( MavenRepositoryBuilder builder,
                              String groupId,
                              String artifactId,
                              MavenVersion version )
  {
    MavenArtifactBuilder artifactBuilder = builder.addArtifact();
    artifactBuilder.setGroup( groupId );
    artifactBuilder.setArtifactId( artifactId );
    artifactBuilder.setContent( new File( "." ) );
    artifactBuilder.setVersion( version );
    artifactBuilder.build();
  }
}
