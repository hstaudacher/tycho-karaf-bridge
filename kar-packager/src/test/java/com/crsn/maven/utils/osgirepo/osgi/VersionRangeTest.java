package com.crsn.maven.utils.osgirepo.osgi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.osgi.framework.Version;

public class VersionRangeTest {

  @Test
  public void containsVersionInRange() {
    assertTrue( new VersionRange( new Version( 1, 0, 0 ), false, new Version( 2, 0, 0 ), false ).contains( new Version( 1,
                                                                                                                        5,
                                                                                                                        0 ) ) );
    assertTrue( new VersionRange( new Version( 1, 0, 0 ), true, new Version( 2, 0, 0 ), false ).contains( new Version( 1,
                                                                                                                       5,
                                                                                                                       0 ) ) );
    assertTrue( new VersionRange( new Version( 1, 0, 0 ), true, new Version( 2, 0, 0 ), true ).contains( new Version( 1,
                                                                                                                      5,
                                                                                                                      0 ) ) );
  }

  @Test
  public void containsVersionOnLowerBoundary() {
    assertFalse( new VersionRange( new Version( 1, 0, 0 ), false, new Version( 2, 0, 0 ), false ).contains( new Version( 1,
                                                                                                                         0,
                                                                                                                         0 ) ) );
    assertTrue( new VersionRange( new Version( 1, 0, 0 ), true, new Version( 2, 0, 0 ), false ).contains( new Version( 1,
                                                                                                                       0,
                                                                                                                       0 ) ) );
  }

  @Test
  public void containsVersionOnUpperBoundary() {
    assertFalse( new VersionRange( new Version( 1, 0, 0 ), false, new Version( 2, 0, 0 ), false ).contains( new Version( 2,
                                                                                                                         0,
                                                                                                                         0 ) ) );
    assertTrue( new VersionRange( new Version( 1, 0, 0 ), true, new Version( 2, 0, 0 ), true ).contains( new Version( 2,
                                                                                                                      0,
                                                                                                                      0 ) ) );
  }

  @Test
  public void doesNotContainVersionOutsideOfRange() {
    assertFalse( new VersionRange( new Version( 1, 0, 0 ), false, new Version( 2, 0, 0 ), false ).contains( new Version( 2,
                                                                                                                         5,
                                                                                                                         0 ) ) );
    assertFalse( new VersionRange( new Version( 1, 0, 0 ), true, new Version( 2, 0, 0 ), false ).contains( new Version( 2,
                                                                                                                        5,
                                                                                                                        0 ) ) );
    assertFalse( new VersionRange( new Version( 1, 0, 0 ), true, new Version( 2, 0, 0 ), true ).contains( new Version( 2,
                                                                                                                       5,
                                                                                                                       0 ) ) );
    assertFalse( new VersionRange( new Version( 1, 0, 0 ), false, new Version( 2, 0, 0 ), false ).contains( new Version( 0,
                                                                                                                         5,
                                                                                                                         0 ) ) );
    assertFalse( new VersionRange( new Version( 1, 0, 0 ), true, new Version( 2, 0, 0 ), false ).contains( new Version( 0,
                                                                                                                        5,
                                                                                                                        0 ) ) );
    assertFalse( new VersionRange( new Version( 1, 0, 0 ), true, new Version( 2, 0, 0 ), true ).contains( new Version( 0,
                                                                                                                       5,
                                                                                                                       0 ) ) );
  }

  @Test
  public void containsEveryVersionBelowUpperBoundaryWhenLowerBoundaryNotSet() {
    assertTrue( new VersionRange( null, false, new Version( 2, 0, 0 ), false ).contains( new Version( 1,
                                                                                                      0,
                                                                                                      0 ) ) );
    assertFalse( new VersionRange( null, false, new Version( 2, 0, 0 ), false ).contains( new Version( 3,
                                                                                                       0,
                                                                                                       0 ) ) );
  }

  @Test
  public void containsEveryVersionAboveLowerBoundaryWhenUpperBoundaryNotSet() {
    assertTrue( new VersionRange( new Version( 2, 0, 0 ), false, null, false ).contains( new Version( 3,
                                                                                                      0,
                                                                                                      0 ) ) );
    assertFalse( new VersionRange( new Version( 2, 0, 0 ), false, null, false ).contains( new Version( 1,
                                                                                                       0,
                                                                                                       0 ) ) );
  }

  @Test
  public void noBoundaryWillMatchAnyVersion() {
    assertTrue( new VersionRange( null, false, null, false ).contains( new Version( 3, 0, 0 ) ) );
  }

  @Test
  public void canParseIncludingVersionRange() {
    VersionRange range = VersionRange.parseVersionRange( "[1.0.0,2.0.0]" );
    assertEquals( new Version( 1, 0, 0 ), range.getFrom() );
    assertTrue( range.isIncludingFrom() );
    assertEquals( new Version( 2, 0, 0 ), range.getTo() );
    assertTrue( range.isIncludingTo() );
  }

  @Test
  public void canParseExcludingVersionRange() {
    VersionRange range = VersionRange.parseVersionRange( "(1.0.0,2.0.0)" );
    assertEquals( new Version( 1, 0, 0 ), range.getFrom() );
    assertFalse( range.isIncludingFrom() );
    assertEquals( new Version( 2, 0, 0 ), range.getTo() );
    assertFalse( range.isIncludingTo() );
  }

  @Test
  public void canParseFromVersionRange() {
    VersionRange range = VersionRange.parseVersionRange( "(1.0.0,)" );
    assertEquals( new Version( 1, 0, 0 ), range.getFrom() );
    assertFalse( range.isIncludingFrom() );
    assertEquals( null, range.getTo() );
    assertFalse( range.isIncludingTo() );
  }

  @Test
  public void canParseToVersionRange() {
    VersionRange range = VersionRange.parseVersionRange( "(,1.0.0)" );
    assertEquals( null, range.getFrom() );
    assertFalse( range.isIncludingFrom() );
    assertEquals( new Version( 1, 0, 0 ), range.getTo() );
    assertFalse( range.isIncludingTo() );
  }

  @Test
  public void canParseNullRange() {
    VersionRange range = VersionRange.parseVersionRange( null );
    assertEquals( null, range.getFrom() );
    assertFalse( range.isIncludingFrom() );
    assertEquals( null, range.getTo() );
    assertFalse( range.isIncludingTo() );
  }

  @Test
  public void canParseSingleVersion() {
    VersionRange range = VersionRange.parseVersionRange( "1.0.0" );
    assertEquals( new Version( 1, 0, 0 ), range.getFrom() );
    assertTrue( range.isIncludingFrom() );
    assertEquals( null, range.getTo() );
    assertFalse( range.isIncludingTo() );
  }
}
