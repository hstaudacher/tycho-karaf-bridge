package com.crsn.maven.utils.osgirepo.content;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;


public abstract class FileContent implements Content {

  private final File file;

  public FileContent( File file ) {
    if( file == null ) {
      throw new NullPointerException( "Null file." );
    }
    this.file = file;
  }

  @Override
  public long contentLength() {
    return file.length();
  }

  @Override
  public void serializeContent( OutputStream stream ) throws IOException {
    FileInputStream fileInputStream = new FileInputStream( file );
    try {
      IOUtils.copy( fileInputStream, stream );
    } finally {
      fileInputStream.close();
    }
  }
}
