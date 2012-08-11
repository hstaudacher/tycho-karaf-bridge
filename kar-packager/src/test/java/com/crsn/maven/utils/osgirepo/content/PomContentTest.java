package com.crsn.maven.utils.osgirepo.content;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.crsn.maven.utils.osgirepo.content.Content;
import com.crsn.maven.utils.osgirepo.content.PomContent;
import com.crsn.maven.utils.osgirepo.maven.MavenArtifact;
import com.crsn.maven.utils.osgirepo.maven.MavenDependency;
import com.crsn.maven.utils.osgirepo.maven.MavenRepository;
import com.crsn.maven.utils.osgirepo.maven.MavenVersion;

public class PomContentTest {

  @Before
  public void setUpXmlUnit() {
    XMLUnit.setIgnoreAttributeOrder( true );
    XMLUnit.setIgnoreComments( true );
    XMLUnit.setIgnoreWhitespace( true );
  }

  @Test
  public void canMarshalPomFile() throws IOException, SAXException {
    MavenArtifact artefact = new MavenArtifact( "com.crsn",
                                                "boo",
                                                new MavenVersion( 1, 0 ),
                                                Collections.<MavenDependency> emptyList(),
                                                new File( "." ),
                                                "name",
                                                "organization" );
    Content content = new PomContent( artefact );
    String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
                      + "<project xmlns=\"http://maven.apache.org/POM/4.0.0\">"
                      + "<modelVersion>4.0.0</modelVersion>"
                      + "<groupId>com.crsn</groupId>"
                      + "<artifactId>boo</artifactId>"
                      + "<version>1.0</version>"
                      + "<name>name</name>"
                      + "<organization>"
                      + "<name>organization</name>"
                      + "</organization>"
                      + "</project>";
    ContentTestUtil.assertGeneratesXMLContent( expected, content );
  }

  @Test
  public void canMarshalPomFileWithDependencies() throws IOException, SAXException {
    MavenRepository repository = ContentTestUtil.createMockMavenRepository();
    List<MavenArtifact> artefacts = repository.getArtifacts();
    MavenArtifact artefact = artefacts.get( 0 );
    PomContent content = new PomContent( artefact );
    String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
                      + "<project xmlns=\"http://maven.apache.org/POM/4.0.0\">"
                      + "<modelVersion>4.0.0</modelVersion>"
                      + "<groupId>com.crsn</groupId>"
                      + "<artifactId>boo</artifactId>"
                      + "<version>1.0</version>"
                      + "<name></name>"
                      + "<organization>"
                      + "<name></name>"
                      + "</organization>"
                      + "<dependencies>"
                      + "<dependency>"
                      + "<artifactId>dependency</artifactId>"
                      + "<groupId>com.crsn</groupId>"
                      + "<version>[1.0,)</version>"
                      + "</dependency>"
                      + "<dependency>"
                      + "<artifactId>dependency</artifactId>"
                      + "<groupId>com.crsn</groupId>"
                      + "<version>LATEST</version>"
                      + "</dependency>"
                      + "</dependencies>"
                      + "</project>";
    ContentTestUtil.assertGeneratesXMLContent( expected, content );
  }
}
