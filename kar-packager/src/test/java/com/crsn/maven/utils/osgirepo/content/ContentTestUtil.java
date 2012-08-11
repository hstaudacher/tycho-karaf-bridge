package com.crsn.maven.utils.osgirepo.content;

import static org.custommonkey.xmlunit.XMLAssert.assertXMLEqual;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.stream.StreamResult;

import org.codehaus.plexus.util.IOUtil;
import org.junit.ComparisonFailure;
import org.junit.Ignore;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.crsn.maven.utils.osgirepo.content.Content;
import com.crsn.maven.utils.osgirepo.maven.MavenRepository;
import com.crsn.maven.utils.osgirepo.maven.MavenVersion;
import com.crsn.maven.utils.osgirepo.maven.builder.MavenArtifactBuilder;
import com.crsn.maven.utils.osgirepo.maven.builder.MavenDependencyBuilder;
import com.crsn.maven.utils.osgirepo.maven.builder.MavenRepositoryBuilder;

@Ignore
public class ContentTestUtil {

  static MavenRepository createMockMavenRepository() {
    MavenRepositoryBuilder builder = new MavenRepositoryBuilder();
    MavenArtifactBuilder artefactBuilder = builder.addArtifact();
    artefactBuilder.setArtifactId( "boo" );
    artefactBuilder.setGroup( "com.crsn" );
    artefactBuilder.setVersion( new MavenVersion( 1, 0 ) );
    artefactBuilder.setContent( new File( "." ) );
    MavenDependencyBuilder dependencyBuilder = artefactBuilder.addDependency();
    dependencyBuilder.setArtefactId( "dependency" );
    dependencyBuilder.setGroupId( "com.crsn" );
    dependencyBuilder.setVersionRange( new MavenVersion( 1, 0 ), true, null, false );
    dependencyBuilder.build();
    MavenDependencyBuilder secondDependencyBuilder = artefactBuilder.addDependency();
    secondDependencyBuilder.setArtefactId( "dependency" );
    secondDependencyBuilder.setGroupId( "com.crsn" );
    secondDependencyBuilder.setVersionRange( null, true, null, false );
    secondDependencyBuilder.build();
    artefactBuilder.build();
    MavenRepository repository = builder.build();
    return repository;
  }

  public static void assertGeneratesXMLContent( String expected, Content content )
    throws IOException, SAXException
  {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    content.serializeContent( bos );
    String generated = IOUtil.toString( bos.toByteArray(), "UTF-8" );
    try {
      assertXMLEqual( expected, generated );
    } catch( Error e ) {
      throw new ComparisonFailure( e.getMessage(), formatXml( expected ), generated );
    }
  }

  public static String formatXml( String xml ) {
    try {
      return formatXmlInternal( xml );
    } catch( TransformerConfigurationException e ) {
      throw new RuntimeException( e );
    } catch( TransformerFactoryConfigurationError e ) {
      throw new RuntimeException( e );
    } catch( TransformerException e ) {
      throw new RuntimeException( e );
    } catch( IOException e ) {
      throw new RuntimeException( e );
    }
  }

  private static String formatXmlInternal( String xml )
    throws TransformerConfigurationException, TransformerFactoryConfigurationError,
    TransformerException, IOException
  {
    Transformer transformer = SAXTransformerFactory.newInstance().newTransformer();
    transformer.setOutputProperty( OutputKeys.INDENT, "yes" );
    transformer.setOutputProperty( "{http://xml.apache.org/xslt}indent-amount", "2" );
    Source source = new SAXSource( new InputSource( new ByteArrayInputStream( xml.getBytes() ) ) );
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    StreamResult result = new StreamResult( byteArrayOutputStream );
    transformer.transform( source, result );
    return IOUtil.toString( byteArrayOutputStream.toByteArray(), "UTF-8" );
  }
}
