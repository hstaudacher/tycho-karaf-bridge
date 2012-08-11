package com.crsn.maven.utils.osgirepo.osgi;

import org.osgi.framework.Version;

public class VersionRange {

  private final Version from;
  private final boolean includingFrom;
  private final Version to;
  private final boolean includingTo;

  public VersionRange( Version from, boolean includingFrom, Version to, boolean includingTo ) {
    this.from = from;
    this.includingFrom = includingFrom;
    this.to = to;
    this.includingTo = includingTo;
  }

  public Version getFrom() {
    return from;
  }

  public boolean isIncludingFrom() {
    return includingFrom;
  }

  public Version getTo() {
    return to;
  }

  public boolean isIncludingTo() {
    return includingTo;
  }

  public boolean contains( Version version ) {
    if( version == null ) {
      throw new NullPointerException( "Null version." );
    }
    if( from != null ) {
      int res = from.compareTo( version );
      switch( res ) {
        case 1:
          return false;
        case 0:
          if( !includingFrom ) {
            return false;
          }
          break;
        default:
      }
    }
    if( to != null ) {
      int res = version.compareTo( to );
      switch( res ) {
        case -1:
          return true;
        case 0:
          if( includingTo ) {
            return true;
          }
          break;
        default:
      }
      return false;
    }
    return true;
  }

  public static VersionRange parseVersionRange( String definition ) {
    if( definition == null ) {
      return new VersionRange( null, false, null, false );
    } else if( definition.startsWith( "[" ) ) {
      return createRange( definition, true );
    } else if( definition.startsWith( "(" ) ) {
      return createRange( definition, false );
    } else {
      return new VersionRange( new Version( definition ), true, null, false );
    }
  }

  private static VersionRange createRange( String definition, boolean fromIncluding ) {
    String stripped = definition.substring( 1, definition.length() - 1 );
    String parts[] = stripped.split( "," );
    Version from;
    Version to;
    if( parts.length == 1 ) {
      from = new Version( parts[ 0 ] );
      to = null;
    } else if( parts.length == 2 ) {
      from = parts[ 0 ].length() > 0
                                    ? new Version( parts[ 0 ] )
                                    : null;
      to = new Version( parts[ 1 ] );
    } else {
      throw new IllegalArgumentException( "Expected exactly two range parts." );
    }
    boolean toIncluding = definition.endsWith( "]" );
    return new VersionRange( from, fromIncluding, to, toIncluding );
  }

  @Override
  public String toString() {
    return String.format( "%s%s,%s%s", isIncludingFrom()
                                                        ? "["
                                                        : "(", from, to, isIncludingTo()
                                                                                        ? "]"
                                                                                        : ")" );
  }
}
