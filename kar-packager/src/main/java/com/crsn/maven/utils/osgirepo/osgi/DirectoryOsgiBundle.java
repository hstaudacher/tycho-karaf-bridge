package com.crsn.maven.utils.osgirepo.osgi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.plexus.util.IOUtil;
import org.osgi.framework.Version;

public class DirectoryOsgiBundle implements OsgiBundle {

  private final File location;
  private final String bundleName;
  private final Version bundleVersion;
  private final List<OsgiDependency> requiredBundles;
  private final List<String> contentFileNames;

  private DirectoryOsgiBundle( File location ) throws IsNotBundleException {
    if( location == null ) {
      throw new NullPointerException( "Null location." );
    }
    this.location = location;
    if( !location.isDirectory() ) {
      throw new RuntimeException( String.format( "Location '%s' is not a directory.",
                                                 location.getAbsolutePath() ) );
    }
    try {
      InputStream content = new FileInputStream( new File( location, "META-INF/MANIFEST.MF" ) );
      BundleParser parser;
      try {
        parser = BundleParser.parse( content, location.getAbsolutePath() );
      } catch( CouldNotParseBundleDescriptorException e ) {
        throw new IsNotBundleException( String.format( "Directory %s does not contain valid OSGI bundle",
                                                       location.getAbsolutePath() ) );
      }
      this.bundleName = parser.getBundleName();
      this.bundleVersion = parser.getBundleVersion();
      this.requiredBundles = parser.getRequiredBundles();
      this.contentFileNames = parser.getClassPathEntries();
    } catch( IOException e ) {
      throw new RuntimeException( e );
    }
  }

  public static DirectoryOsgiBundle createBundle( File directory ) throws IsNotBundleException {
    return new DirectoryOsgiBundle( directory );
  }

  /*
   * (non-Javadoc)
   * @see com.crsn.maven.utils.osgirepo.osgi.OsgiPlugin#getName()
   */
  @Override
  public String getName() {
    return bundleName;
  }

  /*
   * (non-Javadoc)
   * @see com.crsn.maven.utils.osgirepo.osgi.OsgiPlugin#getVersion()
   */
  @Override
  public Version getVersion() {
    return this.bundleVersion;
  }

  /*
   * (non-Javadoc)
   * @see com.crsn.maven.utils.osgirepo.osgi.OsgiPlugin#getLocation()
   */
  @Override
  public File getLocation() {
    try {
      File tempFile = File.createTempFile( "repacked", "jar" );
      tempFile.delete();
      tempFile.deleteOnExit();
      JarOutputStream jarStream = new JarOutputStream( new FileOutputStream( tempFile ) );
      Set<String> existing = new HashSet<String>();
      existing.add( "META-INF/MANIFEST.MF" );
      for( String contentFileName : this.contentFileNames ) {
        File contentFile = new File( this.location, contentFileName );
        if( contentFile.isDirectory() ) {
          copyDirectoryContentToJar( contentFile, jarStream, existing );
        } else {
          copyJarContentToJar( contentFile, jarStream, existing );
        }
      }
      jarStream.putNextEntry( new ZipEntry( "META-INF/MANIFEST.MF" ) );
      FileInputStream fileInputStream = new FileInputStream( new File( this.location,
                                                                       "META-INF/MANIFEST.MF" ) );
      IOUtils.copy( fileInputStream, jarStream );
      fileInputStream.close();
      jarStream.close();
      return tempFile;
    } catch( IOException e ) {
      throw new RuntimeException( e );
    }
  }

  private void copyJarContentToJar( File contentFile,
                                    JarOutputStream jarStream,
                                    Set<String> existing )
  {
    if( contentFile.exists() ) {
      try {
        JarFile jarFile = new JarFile( contentFile );
        for( Enumeration<JarEntry> entries = jarFile.entries(); entries.hasMoreElements(); ) {
          JarEntry entry = entries.nextElement();
          if( !existing.contains( entry.getName() ) ) {
            existing.add( entry.getName() );
            InputStream inputStream = jarFile.getInputStream( entry );
            JarEntry copy = new JarEntry( entry.getName() );
            jarStream.putNextEntry( copy );
            IOUtil.copy( inputStream, jarStream );
            inputStream.close();
          }
        }
      } catch( IOException e ) {
        throw new RuntimeException( String.format( "Error opening jar file '%s': %s",
                                                   contentFile.getAbsolutePath(),
                                                   e.getMessage() ), e );
      }
    }
  }

  private void copyDirectoryContentToJar( File parentFile,
                                          JarOutputStream jarStream,
                                          Set<String> existing ) throws IOException
  {
    Iterator<File> iterateFiles = FileUtils.iterateFiles( parentFile,
                                                          TrueFileFilter.INSTANCE,
                                                          TrueFileFilter.INSTANCE );
    while( iterateFiles.hasNext() ) {
      File nextFile = iterateFiles.next();
      if( nextFile.isFile() ) {
        String name = stripParentName( nextFile, parentFile );
        if( !existing.contains( name ) ) {
          existing.add( name );
          JarEntry entry = new JarEntry( name );
          jarStream.putNextEntry( entry );
          FileInputStream input = new FileInputStream( nextFile );
          IOUtil.copy( input, jarStream );
          input.close();
        }
      }
    }
  }

  private static String stripParentName( File file, File parent ) {
    File parentAbs = parent.getAbsoluteFile();
    File fileAbs = file.getAbsoluteFile();
    List<String> elements = new LinkedList<String>();
    while( !fileAbs.equals( parentAbs ) ) {
      elements.add( fileAbs.getName() );
      fileAbs = fileAbs.getParentFile();
    }
    Collections.reverse( elements );
    String joined = StringUtils.join( elements, '/' );
    return joined;
  }

  /*
   * (non-Javadoc)
   * @see com.crsn.maven.utils.osgirepo.osgi.OsgiPlugin#getRequiredBundles()
   */
  @Override
  public List<OsgiDependency> getRequiredBundles() {
    return requiredBundles;
  }
}
