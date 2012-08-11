package com.crsn.maven.utils.osgirepo.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.crsn.maven.utils.osgirepo.content.ArtifactMetadataContent;
import com.crsn.maven.utils.osgirepo.content.Content;
import com.crsn.maven.utils.osgirepo.content.DigestContent;
import com.crsn.maven.utils.osgirepo.content.JarFileContent;
import com.crsn.maven.utils.osgirepo.content.PomContent;
import com.crsn.maven.utils.osgirepo.maven.MavenArtifact;
import com.crsn.maven.utils.osgirepo.maven.MavenArtifactVersions;
import com.crsn.maven.utils.osgirepo.maven.MavenRepository;
import com.crsn.maven.utils.osgirepo.maven.MavenSourceArtifact;
import com.crsn.maven.utils.osgirepo.maven.MavenVersion;

public class MavenRepositoryToContentsMapper {

  public static Map<String, Content> createContentForRepository( MavenRepository repository ) {
    Map<String, Content> contentMap = new HashMap<String, Content>();
    List<MavenArtifact> artefacts = repository.getArtifacts();
    for( MavenArtifact mavenArtefact : artefacts ) {
      boolean isSourceArtifact = mavenArtefact instanceof MavenSourceArtifact;
      String directory = String.format( "/%s/%s/%s",
                                        mavenArtefact.getGroupId().replaceAll( "\\.", "/" ),
                                        mavenArtefact.getArtifactId(),
                                        mavenArtefact.getVersion().toString() );
      if( isSourceArtifact ) {
        registerJarSourceContent( contentMap, mavenArtefact, directory );
      } else {
        registerPomContent( contentMap, mavenArtefact, directory );
        registerJarContent( contentMap, mavenArtefact, directory );
      }
    }
    for( MavenArtifactVersions mavenArtifactVersions : repository.getArtifactVersions() ) {
      String contentFile = String.format( "/%s/%s/maven-metadata.xml",
                                          mavenArtifactVersions.getGroupId()
                                            .replaceAll( "\\.", "/" ),
                                          mavenArtifactVersions.getArtifactId() );
      MavenVersion lastVersion = mavenArtifactVersions.getVersions().last();
      ArtifactMetadataContent metadataContent = new ArtifactMetadataContent( mavenArtifactVersions,
                                                                             lastVersion,
                                                                             lastVersion );
      contentMap.put( contentFile, metadataContent );
      registerDigestForContent( metadataContent, contentMap, contentFile, "MD5" );
      registerDigestForContent( metadataContent, contentMap, contentFile, "SHA1" );
    }
    return contentMap;
  }

  private static void registerJarSourceContent( Map<String, Content> contentMap,
                                                MavenArtifact mavenArtefact,
                                                String directory )
  {
    JarFileContent jarContent = new JarFileContent( mavenArtefact.getContent() );
    String contentUrl = String.format( "%s/%s-%s-sources.jar",
                                       directory,
                                       mavenArtefact.getArtifactId(),
                                       mavenArtefact.getVersion() );
    registerContentAndItsDigests( jarContent, contentUrl, contentMap );
  }

  private static void registerJarContent( Map<String, Content> contentMap,
                                          MavenArtifact mavenArtefact,
                                          String directory )
  {
    JarFileContent jarContent = new JarFileContent( mavenArtefact.getContent() );
    String contentUrl = String.format( "%s/%s-%s.jar",
                                       directory,
                                       mavenArtefact.getArtifactId(),
                                       mavenArtefact.getVersion() );
    registerContentAndItsDigests( jarContent, contentUrl, contentMap );
  }

  private static void registerPomContent( Map<String, Content> contentMap,
                                          MavenArtifact mavenArtefact,
                                          String directory )
  {
    PomContent pomContent = new PomContent( mavenArtefact );
    String contentUrl = String.format( "%s/%s-%s.pom",
                                       directory,
                                       mavenArtefact.getArtifactId(),
                                       mavenArtefact.getVersion() );
    registerContentAndItsDigests( pomContent, contentUrl, contentMap );
  }

  private static void registerContentAndItsDigests( Content content,
                                                    String contentUrl,
                                                    Map<String, Content> contentMap )
  {
    contentMap.put( contentUrl, content );
    registerDigestForContent( content, contentMap, contentUrl, "MD5" );
    registerDigestForContent( content, contentMap, contentUrl, "SHA1" );
  }

  private static String registerDigestForContent( Content content,
                                                  Map<String, Content> contentMap,
                                                  String originalUrl,
                                                  String macType )
  {
    DigestContent digestContent = new DigestContent( macType, content );
    String digestUrl = String.format( "%s.%s", originalUrl, macType.toLowerCase() );
    contentMap.put( digestUrl, digestContent );
    return digestUrl;
  }
}
