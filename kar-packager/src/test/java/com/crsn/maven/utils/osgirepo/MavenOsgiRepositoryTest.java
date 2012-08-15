package com.crsn.maven.utils.osgirepo;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.crsn.maven.utils.osgirepo.util.TestUtil;

public class MavenOsgiRepositoryTest {
  
  @Rule
  public TemporaryFolder folder = new TemporaryFolder();
  private File out;

  @Before
  public void setUp() {
    out = folder.newFolder( "out" );
    MavenOsgiRepository repository = new MavenOsgiRepository( TestUtil.getFileOfResource( "mockrepo" ), out );
    repository.create();
  }

  @Test
  public void repoExistWithPomFile() {
    assertFileExist( "/org/eclipse/xtext/xtend2/lib/2.0.1-v201108020636/lib-2.0.1-v201108020636.pom" );
  }

  @Test
  public void repoExistWithJarFile() {
    assertFileExist( "/org/eclipse/xtext/xtend2/lib/2.0.1-v201108020636/lib-2.0.1-v201108020636.jar" );
  }

  @Test
  public void repoExistWithArtifactMetaDataFile() {
    assertFileExist( "/org/eclipse/xtext/xtend2/lib/maven-metadata.xml.md5" );
  }

  @Test
  public void repoExistWithArtifactMetaDataMd5File() {
    assertFileExist( "/org/eclipse/xtext/xtend2/lib/maven-metadata.xml.md5" );
  }

  @Test
  public void repoExistWithArtifactMetaDataSha1File() {
    assertFileExist( "/org/eclipse/xtext/xtend2/lib/maven-metadata.xml.sha1" );
  }

  @Test
  public void repoExistWithMd5FileForPomDigest() {
    assertFileExist( "/org/eclipse/xtext/xtend2/lib/2.0.1-v201108020636/lib-2.0.1-v201108020636.pom.md5" );
  }

  @Test
  public void repoExistWithSha1FileForPomDigest() {
    assertFileExist( "/org/eclipse/xtext/xtend2/lib/2.0.1-v201108020636/lib-2.0.1-v201108020636.pom.sha1" );
  }

  @Test
  public void repoExistWithMd5FileForFileDigest() {
    assertFileExist( "/org/eclipse/xtext/xtend2/lib/2.0.1-v201108020636/lib-2.0.1-v201108020636.jar.md5" );
  }

  @Test
  public void repoExistWithSha1FileForFileDigest() {
    assertFileExist( "/org/eclipse/xtext/xtend2/lib/2.0.1-v201108020636/lib-2.0.1-v201108020636.jar.sha1" );
  }

  private void assertFileExist( String createdFile ) {
    File file = new File( out.getAbsolutePath() + createdFile );
  
    assertTrue( file.exists() );
  }
}
