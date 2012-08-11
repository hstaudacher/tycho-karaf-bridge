package com.crsn.maven.utils.osgirepo.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.osgi.framework.Version;

import com.crsn.maven.utils.osgirepo.maven.MavenArtifact;
import com.crsn.maven.utils.osgirepo.maven.MavenDependency;
import com.crsn.maven.utils.osgirepo.maven.MavenRepository;
import com.crsn.maven.utils.osgirepo.maven.MavenSourceArtifact;
import com.crsn.maven.utils.osgirepo.maven.MavenVersion;
import com.crsn.maven.utils.osgirepo.osgi.OsgiDependency;
import com.crsn.maven.utils.osgirepo.osgi.OsgiRepository;
import com.crsn.maven.utils.osgirepo.osgi.VersionRange;

public class OsgiToMavenMapperTest {

  private MavenRepository mavenRepository;

  @Before
  public void createRepository() {
    MockOsgiPlugin firstPlugin = new MockOsgiPlugin( "a.a.a",
                                                     new Version( "2.0.1" ),
                                                     Collections.singletonList( new OsgiDependency( "a.b.c",
                                                                                                    VersionRange.parseVersionRange( "2.0" ) ) ) );
    MockOsgiPlugin secondPlugin = new MockOsgiPlugin( "a.b.c",
                                                      new Version( "3.0.0" ),
                                                      Collections.<OsgiDependency> emptyList() );
    MockOsgiPlugin secondPluginSource = new MockOsgiPlugin( "a.b.c.source",
                                                            new Version( "3.0.0" ),
                                                            Collections.<OsgiDependency> emptyList() );
    List<MockOsgiPlugin> plugins = Arrays.asList( firstPlugin, secondPlugin, secondPluginSource );
    OsgiRepository osgiRepository = OsgiRepository.createRepository( plugins );
    mavenRepository = OsgiToMavenMapper.createRepository( osgiRepository );
  }

  @Test
  public void wontReturnNull() {
    assertNotNull( mavenRepository );
  }

  @Test
  public void willContainAllArtifacts() {
    assertNotNull( mavenRepository );
    assertEquals( 3, mavenRepository.getArtifacts().size() );
  }

  @Test
  public void willGenerateProperArtifactId() {
    MavenDependency firstDependency = getFirstDependencyOfFirstArtifact();
    assertEquals( "c", firstDependency.getArtefactId() );
  }

  @Test
  public void willGenerateProperGroup() {
    MavenDependency firstDependency = getFirstDependencyOfFirstArtifact();
    assertEquals( "a.b", firstDependency.getGroup().toString() );
  }

  @Test
  public void willGenerateProperVersion() {
    MavenDependency firstDependency = getFirstDependencyOfFirstArtifact();
    assertEquals( "3.0.0", firstDependency.getVersionRange().toString() );
  }

  @Test
  public void willGenerateSourceArtifact() {
    MavenArtifact artifact = getThirdArtifact();
    assertTrue( artifact instanceof MavenSourceArtifact );
  }

  @Test
  public void willGenerateProperSourceArtifactName() {
    MavenArtifact artifact = getThirdArtifact();
    assertEquals( "c", artifact.getArtifactId() );
  }

  @Test
  public void willGenerateProperSourceArtifactVersion() {
    MavenArtifact artifact = getThirdArtifact();
    assertEquals( "3.0.0", artifact.getVersion().toString() );
  }

  @Test
  public void willGenerateProperSourceArtifactGroup() {
    MavenArtifact artifact = getThirdArtifact();
    assertEquals( "a.b", artifact.getGroupId().toString() );
  }

  private MavenDependency getFirstDependencyOfFirstArtifact() {
    MavenArtifact firstArtifact = getFirstArtifact();
    assertEquals( new MavenVersion( 2, 0, 1 ), firstArtifact.getVersion() );
    List<MavenDependency> dependencies = firstArtifact.getDependencies();
    assertFalse( dependencies.isEmpty() );
    MavenDependency firstDependency = dependencies.get( 0 );
    return firstDependency;
  }

  private MavenArtifact getFirstArtifact() {
    MavenArtifact firstArtifact = mavenRepository.getArtifacts().get( 0 );
    return firstArtifact;
  }

  private MavenArtifact getThirdArtifact() {
    MavenArtifact firstArtifact = mavenRepository.getArtifacts().get( 2 );
    return firstArtifact;
  }
}
