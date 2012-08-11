package com.crsn.maven.utils.osgirepo.maven.builder;

import com.crsn.maven.utils.osgirepo.maven.MavenVersion;

public interface MavenDependencyBuilder {

  public void setGroupId( String groupId );

  public void setArtefactId( String artefactId );

  public void setVersionRange( MavenVersion from,
                               boolean includingFrom,
                               MavenVersion to,
                               boolean includingTo );

  public void build();
}
