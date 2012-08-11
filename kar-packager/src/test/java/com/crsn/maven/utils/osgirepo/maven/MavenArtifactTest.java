package com.crsn.maven.utils.osgirepo.maven;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import java.io.File;
import java.util.ArrayList;

import org.junit.Test;


public class MavenArtifactTest {
  
  @Test
  public void testMvnUrl() {
    MavenArtifact artifact = new MavenArtifact( "test.group",
                                                "test.artifact",
                                                new MavenVersion( 2, 2 ),
                                                new ArrayList<MavenDependency>(),
                                                mock( File.class ),
                                                "test.name",
                                                "test.organisation" );
    
    String mvnUrl = artifact.getURL();
    
    assertEquals( "mvn:test.group/test.artifact/2.2", mvnUrl );
  }
}
