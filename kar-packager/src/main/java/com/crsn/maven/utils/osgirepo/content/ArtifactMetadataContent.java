package com.crsn.maven.utils.osgirepo.content;

import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

import org.apache.maven.artifact.repository.metadata.Metadata;
import org.apache.maven.artifact.repository.metadata.Versioning;
import org.apache.maven.artifact.repository.metadata.io.xpp3.MetadataXpp3Writer;

import com.crsn.maven.utils.osgirepo.maven.MavenArtifactVersions;
import com.crsn.maven.utils.osgirepo.maven.MavenVersion;

public class ArtifactMetadataContent implements Content {

  private final Metadata meta;

  public ArtifactMetadataContent( MavenArtifactVersions versions,
                                  MavenVersion latest,
                                  MavenVersion release )
  {
    meta = new Metadata();
    meta.setGroupId( versions.getGroupId() );
    meta.setArtifactId( versions.getArtifactId() );
    Versioning versioning = new Versioning();
    versioning.setLatest( latest.toString() );
    versioning.setRelease( release.toString() );
    List<String> allVersions = new LinkedList<String>();
    for( MavenVersion mavenVersion : versions.getVersions() ) {
      allVersions.add( mavenVersion.toString() );
    }
    versioning.setVersions( allVersions );
    meta.setVersioning( versioning );
  }

  @Override
  public String contentType() {
    return "text/xml";
  }

  @Override
  public long contentLength() {
    throw new RuntimeException( "Not implemented" );
  }

  @Override
  public void serializeContent( OutputStream stream ) throws IOException {
    MetadataXpp3Writer writer = new MetadataXpp3Writer();
    writer.write( stream, meta );
  }
}
