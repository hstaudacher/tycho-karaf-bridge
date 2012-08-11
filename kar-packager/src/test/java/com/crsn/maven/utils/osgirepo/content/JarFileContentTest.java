package com.crsn.maven.utils.osgirepo.content;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import com.crsn.maven.utils.osgirepo.content.JarFileContent;
import com.crsn.maven.utils.osgirepo.util.TestUtil;

public class JarFileContentTest {

  @Test
  public void canDeliverFile() throws IOException {
    File testFile = TestUtil.getFileOfResource( "mockrepo/org.eclipse.xtext.xtend2.lib_2.0.1.v201108020636.jar" );
    JarFileContent content = new JarFileContent( testFile );
    assertEquals( "application/java-archive", content.contentType() );
    assertEquals( 14362, content.contentLength() );
    ByteArrayOutputStream contentStream = new ByteArrayOutputStream();
    content.serializeContent( contentStream );
    byte[] original = FileUtils.readFileToByteArray( testFile );
    assertTrue( Arrays.equals( original, contentStream.toByteArray() ) );
  }
}
