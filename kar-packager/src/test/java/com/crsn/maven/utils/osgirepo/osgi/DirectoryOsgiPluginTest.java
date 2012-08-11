package com.crsn.maven.utils.osgirepo.osgi;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.junit.Before;
import org.junit.Test;

import com.crsn.maven.utils.osgirepo.util.TestUtil;

public class DirectoryOsgiPluginTest {

  private DirectoryOsgiBundle plugin;

  @Before
  public void initialize() throws IsNotBundleException {
    plugin = DirectoryOsgiBundle.createBundle( TestUtil.getFileOfResource( "mockrepo-2/org.maven.ide.eclipse.dependency_tree_0.12.1.20110112-1712" ) );
  }

  @Test
  public void canGetPluginName() {
    assertEquals( "org.maven.ide.eclipse.dependency_tree", plugin.getName() );
  }

  @Test
  public void canGetVersionNumber() {
    assertEquals( "0.12.1.20110112-1712", plugin.getVersion().toString() );
  }

  @Test
  public void canGetRequiredBundles() {
    assertEquals( 1, plugin.getRequiredBundles().size() );
  }

  @Test
  public void canCreatePropperJarFile() throws IOException {
    File location = plugin.getLocation();
    JarFile file = new JarFile( location );
    Enumeration<JarEntry> entries = file.entries();
    List<JarEntry> entryList = Collections.list( entries );
    System.out.println( entryList );
    assertEquals( 40, entryList.size() );
    // assertEquals(1,plugin.getRequiredBundles().size());
  }
}
