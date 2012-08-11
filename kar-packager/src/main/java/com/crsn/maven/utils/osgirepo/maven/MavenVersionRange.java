package com.crsn.maven.utils.osgirepo.maven;

public class MavenVersionRange {

  private final MavenVersion fromVersion;
  private final boolean includingForm;
  private final MavenVersion toVersion;
  private final boolean includingTo;

  public MavenVersionRange( MavenVersion fromVersion,
                            boolean includingForm,
                            MavenVersion toVersion,
                            boolean includingTo )
  {
    this.fromVersion = fromVersion;
    this.includingForm = includingForm;
    this.toVersion = toVersion;
    this.includingTo = includingTo;
  }

  public MavenVersion getFromVersion() {
    return fromVersion;
  }

  public MavenVersion getToVersion() {
    return toVersion;
  }

  public boolean isIncludingForm() {
    return includingForm;
  }

  public boolean isIncludingTo() {
    return includingTo;
  }

  @Override
  public String toString() {
    if( fromVersion != null && fromVersion.equals( toVersion ) ) {
      return fromVersion.toString();
    }
    StringBuilder result = new StringBuilder();
    result.append( includingForm
                                ? "["
                                : "(" );
    if( fromVersion != null ) {
      result.append( fromVersion );
    }
    result.append( "," );
    if( toVersion != null ) {
      result.append( toVersion );
    }
    result.append( includingTo
                              ? "]"
                              : ")" );
    return result.toString();
  }

  public boolean isLatestVersion() {
    return ( this.fromVersion == null ) && ( this.toVersion == null );
  }
}
