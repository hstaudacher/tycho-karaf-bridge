package com.eclipsesource.tycho.karaf.bridge.kar_packager;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import org.apache.maven.archiver.MavenArchiveConfiguration;
import org.apache.maven.archiver.MavenArchiver;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.archiver.jar.JarArchiver;

import com.crsn.maven.utils.osgirepo.MavenOsgiRepository;
import com.crsn.maven.utils.osgirepo.maven.MavenArtifact;

/**
 * Goal to package a folder containing OSGi bundles into a Apache Karaf Archive (kar).
 * 
 * @goal package
 * @phase verify
 */
public class KarPackagerMojo extends AbstractMojo {
  
  /**
   * The Maven project.
   *
   * @parameter expression="${project}"
   * @required
   * @readonly
   */
  protected MavenProject project; 
  
  /**
   * The maven archive configuration to use.
   * <p/>
   * See <a href="http://maven.apache.org/ref/current/maven-archiver/apidocs/org/apache/maven/archiver/MavenArchiveConfiguration.html">the Javadocs for MavenArchiveConfiguration</a>.
   *
   * @parameter
   */
  private MavenArchiveConfiguration archive = new MavenArchiveConfiguration();  
  
  /**
   * The Jar archiver.
   *
   * @component role="org.codehaus.plexus.archiver.Archiver" roleHint="jar"
   * @required
   * @readonly
   */
  private JarArchiver jarArchiver = null; 
  
  /**
   * Location of the file.
   * @parameter expression="${project.build.directory}"
   * @required
   */
  private File outputDirectory;

  /**
   * Location of the folder containing all bundles to convert.
   * 
   * @parameter expression="${bundles.folder}"
   * @required
   */
  private File bundlesFolder;
  
  /**
   * Name of the kar archive to generate.
   * 
   * @parameter expression="${kar.name}"
   */
  private String karName;
  
  /**
   * Bundle Configuration.
   *
   * @parameter
   */
  private List<BundleConfiguration> bundlesConfiguration;
  
  /**
   * Feature Dependencies.
   *
   * @parameter
   */
  private List<FeatureDependency> featureDependencies;

  public void execute() throws MojoExecutionException {
    getLog().info( "Start building Karaf Archive with bundles in " + bundlesFolder.getAbsolutePath() );
    String workingFolder = createWorkingFolder();
    File repoDir = new File( workingFolder + File.separator + "repository" );
    MavenOsgiRepository mavenOsgiRepository = createMavenRepository( repoDir );
    createFeatureMetaData( createFeature( repoDir, mavenOsgiRepository ) );
    createKarArchive( workingFolder );
  }

  private String createWorkingFolder() {
    String karFolder = outputDirectory.getAbsolutePath() + File.separator + "kar";
    File karFolderFile = new File( karFolder );
    karFolderFile.mkdirs();
    karFolderFile.deleteOnExit();
    return karFolder;
  }

  private MavenOsgiRepository createMavenRepository( File repoDir ) {
    getLog().info( "Creating Maven Repository from bundles." );
    repoDir.mkdirs();
    MavenOsgiRepository mavenOsgiRepository = new MavenOsgiRepository( bundlesFolder, repoDir );
    mavenOsgiRepository.create();
    return mavenOsgiRepository;
  }

  private File createFeature( File repoDir, MavenOsgiRepository mavenOsgiRepository ) throws MojoExecutionException {
    getLog().info( "Creating feature.xml for all bundles in " + bundlesFolder.getAbsolutePath() );
    List<MavenArtifact> artifacts = mavenOsgiRepository.getMavenRepository().getArtifacts();
    File featureFolder = new File( getFeaturePath( repoDir ) );
    featureFolder.mkdirs();
    Feature feature = new Feature( artifacts, project.getArtifactId(), project.getVersion(), bundlesConfiguration, featureDependencies );
    try {
      File featureFile = new File( getFeatureFileName( featureFolder ) );
      featureFile.createNewFile();
      feature.write( new PrintStream( featureFile ) );
    } catch( IOException mee ) {
      throw new MojoExecutionException( "Could not write feature file.", mee );
    }
    return featureFolder;
  }

  private String getFeaturePath( File repoDir ) {
    return repoDir
           + File.separator
           + project.getGroupId().replace( ".", File.separator )
           + File.separator
           + project.getArtifactId().replace( ".", File.separator )
           + File.separator
           + project.getVersion();
  }

  private String getFeatureFileName( File featureFolder ) {
    return featureFolder + File.separator + project.getArtifactId() + "-" + project.getVersion() + "-features.xml";
  }

  private void createKarArchive( String workFolder ) throws MojoExecutionException {
    getLog().info( "Creating kar archive." );
    MavenArchiver archiver = new MavenArchiver();
    archiver.setArchiver( jarArchiver );
    archiver.setOutputFile( new File( outputDirectory.getAbsolutePath() + File.separator + karName + ".kar" ) );
    try {
      archiver.getArchiver().addDirectory( new File( workFolder ) );
      archiver.createArchive( project, archive );
    } catch( Exception e ) {
      throw new MojoExecutionException( "Could not create kar archive.", e );
    }
  }

  private void createFeatureMetaData( File featureFolder ) throws MojoExecutionException {
    getLog().info( "Writing metadata for feature." );
    File metaData = new File( featureFolder.getAbsolutePath() + File.separator + "maven-metadata-local.xml" );
    try {
      metaData.createNewFile();
      PrintStream stream = new PrintStream( metaData );
      FeatureMetaData data = new FeatureMetaData( project.getGroupId(), project.getArtifactId(), project.getVersion() );
      data.write( stream );
    } catch( IOException e ) {
      throw new MojoExecutionException( "Could not write feature metadata." );
    }
  }
}
