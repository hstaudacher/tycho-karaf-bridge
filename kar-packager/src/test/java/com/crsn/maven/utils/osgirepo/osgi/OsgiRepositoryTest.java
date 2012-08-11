package com.crsn.maven.utils.osgirepo.osgi;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.osgi.framework.Version;

import com.crsn.maven.utils.osgirepo.util.TestUtil;

public class OsgiRepositoryTest {

  @Test
  public void canLoadRepository() {
    OsgiRepository repo = OsgiRepository.createRepository( TestUtil.getFileOfResource( "mockrepo" ) );
    assertFalse( repo.getPlugins().isEmpty() );
    OsgiBundle plugin = repo.getPlugins().get( 0 );
    assertFalse( plugin.getRequiredBundles().isEmpty() );
  }

  @Test
  public void canLoadRepositoryWithDirectoryPlugins() {
    OsgiRepository repo = OsgiRepository.createRepository( TestUtil.getFileOfResource( "mockrepo-2" ) );
    assertFalse( repo.getPlugins().isEmpty() );
    OsgiBundle plugin = repo.getPlugins().get( 0 );
    assertFalse( plugin.getRequiredBundles().isEmpty() );
  }

  @Test
  public void canResolveDependencies() {
    OsgiRepository repo = OsgiRepository.createRepository( TestUtil.getFileOfResource( "mockrepo" ) );
    assertNotNull( repo.resolveDependency( new OsgiDependency( "org.eclipse.xtext.xtend2.lib",
                                                               new VersionRange( new Version( "1.0" ),
                                                                                 true,
                                                                                 null,
                                                                                 false ) ) ) );
  }
}
