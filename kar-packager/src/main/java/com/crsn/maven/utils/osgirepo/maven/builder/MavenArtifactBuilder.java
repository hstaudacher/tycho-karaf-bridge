package com.crsn.maven.utils.osgirepo.maven.builder;

import java.io.File;

import com.crsn.maven.utils.osgirepo.maven.MavenVersion;

public interface MavenArtifactBuilder {

  void setGroup( String groupId );

  void setArtifactId( String artifactId );

  void setVersion( MavenVersion version );

  MavenDependencyBuilder addDependency();

  void setContent( File content );

  void setName( String name );

  void setOrganization( String organization );

  void build();
}