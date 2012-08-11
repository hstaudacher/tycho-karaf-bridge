package com.crsn.maven.utils.osgirepo.content;

import java.io.IOException;
import java.util.TreeSet;

import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.crsn.maven.utils.osgirepo.content.ArtifactMetadataContent;
import com.crsn.maven.utils.osgirepo.maven.MavenArtifactVersions;
import com.crsn.maven.utils.osgirepo.maven.MavenVersion;

public class ArtifactMetadataContentTest {

  @Before
  public void setUpXmlUnit() {
    XMLUnit.setIgnoreAttributeOrder( true );
    XMLUnit.setIgnoreComments( true );
    XMLUnit.setIgnoreWhitespace( true );
  }

  @Test
  public void canSerializeMetaData() throws IOException, SAXException {
    TreeSet<MavenVersion> versions = new TreeSet<MavenVersion>();
    versions.add( new MavenVersion( 1 ) );
    versions.add( new MavenVersion( 2 ) );
    versions.add( new MavenVersion( 3 ) );
    MavenArtifactVersions data = new MavenArtifactVersions( "com.crsn", "test", versions );
    ArtifactMetadataContent content = new ArtifactMetadataContent( data,
                                                                   versions.last(),
                                                                   versions.last() );
    // assertEquals("a","b");
    ContentTestUtil.assertGeneratesXMLContent( "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                                               + "<metadata>"
                                               + "<groupId>com.crsn</groupId>"
                                               + "<artifactId>test</artifactId>"
                                               + "<versioning><latest>3</latest>"
                                               + "<release>3</release>"
                                               + "<versions>"
                                               + "<version>1</version>"
                                               + "<version>2</version>"
                                               + "<version>3</version>"
                                               + "</versions>"
                                               + "</versioning>"
                                               + "</metadata>", content );
  }
}
