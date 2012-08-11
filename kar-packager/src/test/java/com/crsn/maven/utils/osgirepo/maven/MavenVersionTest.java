package com.crsn.maven.utils.osgirepo.maven;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MavenVersionTest {

  @Test
  public void supportsMajor() {
    assertEquals( "5", new MavenVersion( 5 ).toString() );
  }

  @Test
  public void supportsMajorMinor() {
    assertEquals( "5.1", new MavenVersion( 5, 1 ).toString() );
  }

  @Test
  public void supportsMajorMinorIncremental() {
    assertEquals( "5.2.3", new MavenVersion( 5, 2, 3 ).toString() );
  }

  @Test
  public void supportsMajorMinorIncrementalQualifier() {
    assertEquals( "5.2.3-SNAPSHOT", new MavenVersion( 5, 2, 3, "SNAPSHOT" ).toString() );
  }

  @Test
  public void supportsMajorQualifier() {
    assertEquals( "5-SNAPSHOT", new MavenVersion( 5, "SNAPSHOT" ).toString() );
  }

  @Test
  public void supportsMajorMinorQualifier() {
    assertEquals( "5.6-SNAPSHOT", new MavenVersion( 5, 6, "SNAPSHOT" ).toString() );
  }

  @Test(expected = IllegalArgumentException.class)
  public void wontAcceptNegativeMajor() {
    new MavenVersion( -1 );
  }

  @Test(expected = IllegalArgumentException.class)
  public void wontAcceptNegativeMinor() {
    new MavenVersion( 1, -2 );
  }

  @Test(expected = IllegalArgumentException.class)
  public void wontAcceptNegativeIncremental() {
    new MavenVersion( 1, 2, -3 );
  }

  @Test
  public void canCompareVersions() {
    assertEquals( 0, new MavenVersion( 1, 2, 3, "a" ).compareTo( new MavenVersion( 1, 2, 3, "a" ) ) );
    assertEquals( -1, new MavenVersion( 0, 2, 3, "a" ).compareTo( new MavenVersion( 1, 2, 3, "a" ) ) );
    assertEquals( -1, new MavenVersion( 1, 1, 3, "a" ).compareTo( new MavenVersion( 1, 2, 3, "a" ) ) );
    assertEquals( -1, new MavenVersion( 1, 2, 2, "a" ).compareTo( new MavenVersion( 1, 2, 3, "a" ) ) );
    assertEquals( -1, new MavenVersion( 1, 2, 2, "a" ).compareTo( new MavenVersion( 1, 2, 3, "b" ) ) );
  }
}
