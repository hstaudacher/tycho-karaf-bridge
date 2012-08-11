package com.crsn.maven.utils.osgirepo.util;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.crsn.maven.utils.osgirepo.content.Content;
import com.crsn.maven.utils.osgirepo.maven.MavenRepository;
import com.crsn.maven.utils.osgirepo.maven.MavenVersion;
import com.crsn.maven.utils.osgirepo.maven.builder.MavenArtifactBuilder;
import com.crsn.maven.utils.osgirepo.maven.builder.MavenRepositoryBuilder;

public class MavenRepositoryToContentsMapperTest {

  private Map<String, Content> contentOfRepository;

  @Before
  public void createRepository() {
    MavenRepositoryBuilder builder = new MavenRepositoryBuilder();
    addJarArtifact( builder );
    addSourceArtifact( builder );
    MavenRepository repository = builder.build();
    contentOfRepository = MavenRepositoryToContentsMapper.createContentForRepository( repository );
    // System.out.println(contentOfRepository.keySet());
  }

  private void addJarArtifact( MavenRepositoryBuilder builder ) {
    MavenArtifactBuilder artifactBuilder = builder.addArtifact();
    addArtifactInformation( artifactBuilder );
  }

  private void addSourceArtifact( MavenRepositoryBuilder builder ) {
    MavenArtifactBuilder artifactBuilder = builder.addSourceArtifact();
    addArtifactInformation( artifactBuilder );
  }

  private void addArtifactInformation( MavenArtifactBuilder artifactBuilder ) {
    artifactBuilder.setArtifactId( "art1" );
    artifactBuilder.setGroup( "group1" );
    artifactBuilder.setVersion( new MavenVersion( 1 ) );
    artifactBuilder.setContent( new File( "." ) );
    artifactBuilder.build();
  }

  @Test
  public void willCreateJar() {
    assertNotNull( contentOfRepository.get( "/group1/art1/1/art1-1.jar" ) );
  }

  @Test
  public void willCreateJarSha1() {
    assertNotNull( contentOfRepository.get( "/group1/art1/1/art1-1.jar.sha1" ) );
  }

  @Test
  public void willCreateJarMd5() {
    assertNotNull( contentOfRepository.get( "/group1/art1/1/art1-1.jar.md5" ) );
  }

  @Test
  public void willCreateJarSource() {
    assertNotNull( contentOfRepository.get( "/group1/art1/1/art1-1-sources.jar" ) );
  }

  @Test
  public void willCreateJarSourceSha1() {
    assertNotNull( contentOfRepository.get( "/group1/art1/1/art1-1-sources.jar.sha1" ) );
  }

  @Test
  public void willCreateJarSourceMd5() {
    assertNotNull( contentOfRepository.get( "/group1/art1/1/art1-1-sources.jar.md5" ) );
  }

  @Test
  public void willCreatePom() {
    assertNotNull( contentOfRepository.get( "/group1/art1/1/art1-1.pom" ) );
  }

  @Test
  public void willCreatePomSha1() {
    assertNotNull( contentOfRepository.get( "/group1/art1/1/art1-1.pom.sha1" ) );
  }

  @Test
  public void willCreatePomMd5() {
    assertNotNull( contentOfRepository.get( "/group1/art1/1/art1-1.pom.md5" ) );
  }

  @Test
  public void willCreateArtefactMetaData() {
    assertNotNull( contentOfRepository.get( "/group1/art1/maven-metadata.xml" ) );
  }

  @Test
  public void willCreateArtefactMetaDataSha1() {
    assertNotNull( contentOfRepository.get( "/group1/art1/maven-metadata.xml.sha1" ) );
  }

  @Test
  public void willCreateArtefactMetaDataMd5() {
    assertNotNull( contentOfRepository.get( "/group1/art1/maven-metadata.xml.md5" ) );
  }
}
