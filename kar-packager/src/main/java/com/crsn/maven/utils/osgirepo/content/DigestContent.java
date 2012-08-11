package com.crsn.maven.utils.osgirepo.content;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.io.output.NullOutputStream;


public class DigestContent implements Content {

  private volatile byte[] asciiEncodedMac;
  private final String type;
  private final Content originalContent;

  public DigestContent( String type, Content originalContent ) {
    this.type = type;
    this.originalContent = originalContent;
    if( type == null ) {
      throw new NullPointerException( "Null type" );
    }
    if( originalContent == null ) {
      throw new NullPointerException( "Null original content." );
    }
    this.asciiEncodedMac = null;
  }

  private void calculateMac() {
    MacOutputStream macStream = new MacOutputStream( type );
    try {
      originalContent.serializeContent( macStream );
    } catch( IOException e ) {
      throw new RuntimeException( e );
    }
    try {
      this.asciiEncodedMac = toAsciiEncodedHexString( macStream.getMac() ).getBytes( "ASCII" );
    } catch( UnsupportedEncodingException e ) {
      throw new RuntimeException( e );
    }
  }

  private String toAsciiEncodedHexString( byte[] mac ) {
    StringBuilder builder = new StringBuilder();
    for( byte b : mac ) {
      builder.append( String.format( "%02x", Byte.valueOf( b ) ) );
    }
    builder.append( "\r\n" );
    return builder.toString();
  }

  @Override
  public String contentType() {
    return "text/plain";
  }

  @Override
  public long contentLength() {
    calculateIfNeeded();
    return asciiEncodedMac.length;
  }

  @Override
  public void serializeContent( OutputStream stream ) throws IOException {
    calculateIfNeeded();
    stream.write( asciiEncodedMac );
  }

  private synchronized void calculateIfNeeded() {
    if( this.asciiEncodedMac == null ) {
      calculateMac();
    }
  }
  private class MacOutputStream extends FilterOutputStream {

    private final MessageDigest digest;

    public MacOutputStream( String macType ) {
      super( new NullOutputStream() );
      try {
        this.digest = MessageDigest.getInstance( macType );
        // DigestUtils.md5Hex(data)
      } catch( NoSuchAlgorithmException e ) {
        throw new IllegalArgumentException( e );
      }
    }

    @Override
    public void write( int b ) throws IOException {
      digest.update( ( byte )b );
    }

    public byte[] getMac() {
      return digest.digest();
    }
  }
}
