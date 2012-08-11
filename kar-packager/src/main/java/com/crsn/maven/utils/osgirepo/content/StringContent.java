package com.crsn.maven.utils.osgirepo.content;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

public class StringContent implements Content {

  private final String contentType;
  private final byte[] data;

  public StringContent( String value, String encoding, String contentType ) {
    if( value == null ) {
      throw new NullPointerException( "Null value." );
    }
    if( encoding == null ) {
      throw new NullPointerException( "Null encoding." );
    }
    if( contentType == null ) {
      throw new NullPointerException( "Null content type." );
    }
    this.contentType = contentType;
    try {
      this.data = value.getBytes( encoding );
    } catch( UnsupportedEncodingException e ) {
      throw new IllegalArgumentException( e );
    }
  }

  @Override
  public String contentType() {
    return contentType;
  }

  @Override
  public long contentLength() {
    return data.length;
  }

  @Override
  public void serializeContent( OutputStream stream ) throws IOException {
    stream.write( data );
  }
}
