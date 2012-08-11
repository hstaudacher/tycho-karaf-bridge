package com.crsn.maven.utils.osgirepo.osgi;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.osgi.framework.Version;

import com.crsn.maven.utils.osgirepo.util.TestUtil;

public class JarOsgiPluginTest {

  private OsgiBundle plugin;

  @Before
  public void initialize() throws IsNotBundleException {
    plugin = JarOsgiBundle.createBundle( TestUtil.getFileOfResource( "mockrepo/org.eclipse.xtext.xtend2.lib_2.0.1.v201108020636.jar" ) );
  }

  @Test
  public void canGetName() {
    assertEquals( "org.eclipse.xtext.xtend2.lib", plugin.getName() );
  }

  @Test
  public void canGetVersion() {
    assertEquals( new Version( 2, 0, 1, "v201108020636" ), plugin.getVersion() );
  }
}
