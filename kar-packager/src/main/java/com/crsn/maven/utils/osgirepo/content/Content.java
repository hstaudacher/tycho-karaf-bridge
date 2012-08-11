package com.crsn.maven.utils.osgirepo.content;

import java.io.IOException;
import java.io.OutputStream;

public interface Content {

  String contentType();

  long contentLength();

  void serializeContent( OutputStream stream ) throws IOException;
}
