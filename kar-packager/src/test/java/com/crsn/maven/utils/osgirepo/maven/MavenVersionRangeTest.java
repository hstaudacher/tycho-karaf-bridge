package com.crsn.maven.utils.osgirepo.maven;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MavenVersionRangeTest {

  @Test
  public void supportsSingleVersion() {
    assertEquals( "1.0", new MavenVersionRange( new MavenVersion( 1, 0 ),
                                                false,
                                                new MavenVersion( 1, 0 ),
                                                false ).toString() );
  }

  @Test
  public void supportsLowerBoundExcludingVersion() {
    assertEquals( "(1.0,)",
                  new MavenVersionRange( new MavenVersion( 1, 0 ), false, null, false ).toString() );
  }

  @Test
  public void supportsUpperBoundExcludingVersion() {
    assertEquals( "(,1.0)",
                  new MavenVersionRange( null, false, new MavenVersion( 1, 0 ), false ).toString() );
  }

  @Test
  public void supportsLowerBoundIncludingVersion() {
    assertEquals( "[1.0,)",
                  new MavenVersionRange( new MavenVersion( 1, 0 ), true, null, false ).toString() );
  }

  @Test
  public void supportsUpperBoundIncludingVersion() {
    assertEquals( "(,1.0]",
                  new MavenVersionRange( null, false, new MavenVersion( 1, 0 ), true ).toString() );
  }
}
