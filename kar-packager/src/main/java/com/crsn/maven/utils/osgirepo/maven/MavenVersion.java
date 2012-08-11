package com.crsn.maven.utils.osgirepo.maven;

public class MavenVersion implements Comparable<MavenVersion> {

  private final int major;
  private final int minor;
  private final int incremental;
  private final String qualifier;

  public MavenVersion( int major, int minor, int incremental, String qualifier ) {
    if( major < 0 ) {
      throw new IllegalArgumentException( "Negative major version." );
    }
    this.major = major;
    if( minor < -1 ) {
      throw new IllegalArgumentException( "Illegal minor version." );
    }
    this.minor = minor;
    if( incremental < -1 ) {
      throw new IllegalArgumentException( "Illegal incremental version." );
    }
    if( minor < 0 && incremental >= 0 ) {
      throw new IllegalArgumentException( "Specified incremental version without minor version." );
    }
    this.incremental = incremental;
    if( qualifier == null ) {
      throw new NullPointerException( "Null qualifier." );
    }
    this.qualifier = qualifier;
  }

  public MavenVersion( int major ) {
    this( major, -1, -1, "" );
  }

  public MavenVersion( int major, int minor ) {
    this( major, minor, -1, "" );
  }

  public MavenVersion( int major, int minor, int incremental ) {
    this( major, minor, incremental, "" );
  }

  public MavenVersion( int major, String qualifier ) {
    this( major, -1, -1, qualifier );
  }

  public MavenVersion( int major, int minor, String qualifier ) {
    this( major, minor, -1, qualifier );
  }

  @Override
  public int compareTo( MavenVersion that ) {
    if( this.major != that.major ) {
      return this.major < that.major
                                    ? -1
                                    : 1;
    } else if( this.minor != that.minor ) {
      return this.minor < that.minor
                                    ? -1
                                    : 1;
    } else if( this.incremental != that.incremental ) {
      return this.incremental < that.incremental
                                                ? -1
                                                : 1;
    } else {
      return this.qualifier.compareTo( that.qualifier );
    }
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append( major );
    if( minor >= 0 ) {
      builder.append( "." );
      builder.append( minor );
    }
    if( incremental >= 0 ) {
      builder.append( "." );
      builder.append( incremental );
    }
    if( qualifier.length() > 0 ) {
      builder.append( "-" );
      builder.append( qualifier );
    }
    return builder.toString();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + incremental;
    result = prime * result + major;
    result = prime * result + minor;
    result = prime * result + ( ( qualifier == null )
                                                     ? 0
                                                     : qualifier.hashCode() );
    return result;
  }

  @Override
  public boolean equals( Object obj ) {
    if( this == obj )
      return true;
    if( obj == null )
      return false;
    if( getClass() != obj.getClass() )
      return false;
    MavenVersion other = ( MavenVersion )obj;
    if( incremental != other.incremental )
      return false;
    if( major != other.major )
      return false;
    if( minor != other.minor )
      return false;
    if( qualifier == null ) {
      if( other.qualifier != null )
        return false;
    } else if( !qualifier.equals( other.qualifier ) )
      return false;
    return true;
  }
}
