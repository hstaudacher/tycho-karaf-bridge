package com.crsn.maven.utils.osgirepo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.crsn.maven.utils.osgirepo.content.Content;
import com.crsn.maven.utils.osgirepo.maven.MavenRepository;
import com.crsn.maven.utils.osgirepo.osgi.OsgiRepository;
import com.crsn.maven.utils.osgirepo.util.MavenRepositoryToContentsMapper;
import com.crsn.maven.utils.osgirepo.util.OsgiToMavenMapper;

public class MavenOsgiRepository {

  private File bundleFolder;
  private File outputDirectory;
  private MavenRepository mavenRepository;

  public MavenOsgiRepository( File bundleFolder, File outputDirectory ) {
    this.bundleFolder = bundleFolder;
    this.outputDirectory = outputDirectory;
  }
  
  public void create() {
    OsgiRepository osgiRepository = OsgiRepository.createRepository( bundleFolder );
    mavenRepository = OsgiToMavenMapper.createRepository( osgiRepository );
    Map<String, Content> contentMap = MavenRepositoryToContentsMapper.createContentForRepository( getMavenRepository() );
    createRepo( contentMap );
  }

  private void createRepo( Map<String, Content> contentMap ) {
    for( Entry<String, Content> entry : contentMap.entrySet() ) {
      File file = new File( outputDirectory.getAbsolutePath() + entry.getKey() );
      createFolders( file );
      try {
        if( file.createNewFile() ) {
          writeContent( file, entry.getValue() );
        } else {
          throw new IllegalStateException( "Could not create file " + file.getAbsolutePath() + ". Repo may be invalid" );
        }
      } catch( IOException ioe ) {
        throw new IllegalStateException( "Could not create file " 
                                         + file.getAbsolutePath() + ". Repo may be invalid", ioe );
      }
    }
  }

  private void createFolders( File file ) {
    String[] segments = file.getAbsolutePath().split( File.separator );
    File folder = new File( StringUtils.join( ArrayUtils.subarray( segments, 0, segments.length - 1 ), File.separator ) );
    folder.mkdirs();
  }

  private void writeContent( File file, Content value ) {
    try {
      FileOutputStream stream = new FileOutputStream( file );
      value.serializeContent( stream );
    } catch( Exception e ) {
      throw new IllegalStateException( "Could not write file " + file.getAbsolutePath() );
    }
  }

  public MavenRepository getMavenRepository() {
    return mavenRepository;
  }

}
