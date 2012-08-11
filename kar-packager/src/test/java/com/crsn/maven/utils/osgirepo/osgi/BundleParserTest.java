package com.crsn.maven.utils.osgirepo.osgi;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.osgi.framework.Version;

public class BundleParserTest {

  private static final String MANIFEST_MF = "/complex-MANIFEST.MF";
  private BundleParser parser;

  @Before
  public void initialize() throws CouldNotParseBundleDescriptorException {
    parser = BundleParser.parse( this.getClass().getResourceAsStream( MANIFEST_MF ),
                                 this.getClass().getResource( MANIFEST_MF ).toString() );
  }

  @Test
  public void canParserBundleName() {
    assertEquals( "org.eclipse.egit.core", parser.getBundleName() );
  }

  @Test
  public void canParseBundleVersion() {
    assertEquals( new Version( 1, 0, 0, "201106090707-r" ), parser.getBundleVersion() );
  }

  @Test
  public void canParseDependencies() {
    assertEquals( 5, parser.getRequiredBundles().size() );
  }
}
