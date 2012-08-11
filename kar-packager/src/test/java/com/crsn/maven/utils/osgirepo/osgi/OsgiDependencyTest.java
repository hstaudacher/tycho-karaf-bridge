package com.crsn.maven.utils.osgirepo.osgi;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.osgi.framework.Version;

import com.crsn.maven.utils.osgirepo.util.TestUtil;

public class OsgiDependencyTest {

  private OsgiBundle plugin;

  @Before
  public void initialize() throws IsNotBundleException {
    plugin = JarOsgiBundle.createBundle( TestUtil.getFileOfResource( "mockrepo/org.eclipse.xtext.xtend2.lib_2.0.1.v201108020636.jar" ) );
  }

  @Test
  public void willResolvePluginIfMatching() {
    OsgiDependency dependency = new OsgiDependency( "org.eclipse.xtext.xtend2.lib",
                                                    new VersionRange( new Version( "1.0" ),
                                                                      true,
                                                                      null,
                                                                      false ) );
    assertTrue( dependency.isResolvedBy( plugin ) );
  }

  @Test
  public void wontResolvePluginOutsideVersionRange() {
    assertFalse( new OsgiDependency( "org.eclipse.xtext.xtend2.lib",
                                     new VersionRange( new Version( "1.0" ),
                                                       true,
                                                       new Version( "1.0" ),
                                                       true ) ).isResolvedBy( plugin ) );
  }

  @Test
  public void wontResolvePluginWithDifferentName() {
    assertFalse( new OsgiDependency( "unknown", new VersionRange( new Version( "1.0" ),
                                                                  true,
                                                                  null,
                                                                  true ) ).isResolvedBy( plugin ) );
  }
}
