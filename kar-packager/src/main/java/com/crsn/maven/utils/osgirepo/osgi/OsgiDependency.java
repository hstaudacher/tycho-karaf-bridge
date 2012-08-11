package com.crsn.maven.utils.osgirepo.osgi;

public class OsgiDependency {

  private final String name;
  private final VersionRange versionRange;

  public OsgiDependency( String name, VersionRange versionRange ) {
    if( name == null ) {
      throw new NullPointerException( "Null name." );
    }
    if( versionRange == null ) {
      throw new NullPointerException( "Null version." );
    }
    this.name = name;
    this.versionRange = versionRange;
  }

  public String getName() {
    return name;
  }

  public VersionRange getVersionRange() {
    return versionRange;
  }

  public boolean isResolvedBy( OsgiBundle osgiBundle ) {
    if( osgiBundle == null ) {
      throw new NullPointerException( "Null osgi bundle." );
    }
    String pluginName = osgiBundle.getName();
    if( !pluginName.equals( name ) ) {
      return false;
    }
    return versionRange.contains( osgiBundle.getVersion() );
  }

  @Override
  public String toString() {
    return "OsgiDependency [name=" + name + ", versionRange=" + versionRange + "]";
  }
}
