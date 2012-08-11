package com.crsn.maven.utils.osgirepo.content;

import java.io.File;

public class JarFileContent extends FileContent {

  public JarFileContent( File file ) {
    super( file );
  }

  @Override
  public String contentType() {
    return "application/java-archive";
  }
}
