package com.crsn.maven.utils.osgirepo.util;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;

public class TestUtil {

  public static File getFileOfResource( String path ) {
    URL resource = Thread.currentThread().getContextClassLoader().getResource( path );
    String encodedFileName = resource.getFile();
    String decodedFileName;
    try {
      decodedFileName = URLDecoder.decode( encodedFileName, "UTF-8" );
    } catch( UnsupportedEncodingException e ) {
      throw new RuntimeException( e );
    }
    File location = new File( decodedFileName );
    return location;
  }
}
