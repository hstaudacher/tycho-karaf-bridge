package com.crsn.maven.utils.osgirepo.content;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.junit.Test;

import com.crsn.maven.utils.osgirepo.content.Content;
import com.crsn.maven.utils.osgirepo.content.DigestContent;
import com.crsn.maven.utils.osgirepo.content.StringContent;


public class DigestContentTest {

  @Test
  public void canCalculateMd5Digest() throws IOException {
    assertEquals( "008c5926ca861023c1d2a36653fd88e2\r\n", createDigestOfType( "MD5" ) );
  }

  @Test
  public void canCalculateSha1Mac() throws IOException {
    assertEquals( "d869db7fe62fb07c25a0403ecaea55031744b5fb\r\n", createDigestOfType( "SHA1" ) );
  }

  private static String createDigestOfType( String digestType )
    throws IOException, UnsupportedEncodingException
  {
    Content originalContent = createTestContent();
    DigestContent content = new DigestContent( digestType, originalContent );
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    content.serializeContent( baos );
    String contentAsString = baos.toString( "ASCII" );
    return contentAsString;
  }

  private static Content createTestContent() {
    return new StringContent( "whatever", "ASCII", "text/plain" );
  }
}
