package com.crsn.maven.utils.osgirepo.content;

import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.Organization;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;

import com.crsn.maven.utils.osgirepo.maven.MavenArtifact;
import com.crsn.maven.utils.osgirepo.maven.MavenDependency;
import com.crsn.maven.utils.osgirepo.maven.MavenVersionRange;

public class PomContent implements Content {

  // private final Model pomModel;
  private final Model pomModel;

  public PomContent( MavenArtifact artefact ) {
    pomModel = new Model();
    // project = factory.createProject(pomModel);
    pomModel.setModelVersion( "4.0.0" );
    pomModel.setArtifactId( artefact.getArtifactId() );
    pomModel.setGroupId( artefact.getGroupId().toString() );
    pomModel.setVersion( artefact.getVersion().toString() );
    pomModel.setName( artefact.getName() );
    Organization organization = new Organization();
    organization.setName( artefact.getOrganization() );
    pomModel.setOrganization( organization );
    List<MavenDependency> dependencies = artefact.getDependencies();
    if( !dependencies.isEmpty() ) {
      List<Dependency> dependencyList = new LinkedList<Dependency>();
      for( MavenDependency dependency : dependencies ) {
        Dependency dependencyModel = new Dependency();
        dependencyModel.setArtifactId( dependency.getArtefactId() );
        dependencyModel.setGroupId( dependency.getGroup().toString() );
        MavenVersionRange versionRange = dependency.getVersionRange();
        if( versionRange.isLatestVersion() ) {
          dependencyModel.setVersion( "LATEST" );
        } else {
          dependencyModel.setVersion( versionRange.toString() );
        }
        dependencyList.add( dependencyModel );
      }
      pomModel.setDependencies( dependencyList );
    }
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
    MavenXpp3Writer writer = new MavenXpp3Writer();
    writer.write( stream, this.pomModel );
  }
}
