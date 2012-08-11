package com.crsn.maven.utils.osgirepo.osgi;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.osgi.framework.Version;

public class JarOsgiBundle implements OsgiBundle {

  private final File location;
  private final String bundleName;
  private final Version bundleVersion;
  private final List<OsgiDependency> requiredBundles;

  private JarOsgiBundle( File location ) throws IsNotBundleException {
    if( location == null ) {
      throw new NullPointerException( "Null location." );
    }
    this.location = location;
    try {
      ZipFile zipFile = new ZipFile( location );
      InputStream content = zipFile.getInputStream( zipFile.getEntry( "META-INF/MANIFEST.MF" ) );
      BundleParser parser;
      try {
        parser = BundleParser.parse( content, location.getAbsolutePath() );
        this.bundleName = parser.getBundleName();
        this.bundleVersion = parser.getBundleVersion();
        this.requiredBundles = parser.getRequiredBundles();
      } catch( CouldNotParseBundleDescriptorException e ) {
        throw new IsNotBundleException( String.format( "Directory %s does not contain valid OSGI bundle",
                                                       location.getAbsolutePath() ),
                                        e );
      }
    } catch( ZipException e ) {
      throw new RuntimeException( String.format( "Could not open '%s': %s",
                                                 location.getAbsolutePath(),
                                                 e.getMessage() ), e );
    } catch( IOException e ) {
      throw new RuntimeException( String.format( "Could not open '%s': %s",
                                                 location.getAbsolutePath(),
                                                 e.getMessage() ), e );
    }
  }

  @Override
  public String getName() {
    return bundleName;
  }

  @Override
  public Version getVersion() {
    return this.bundleVersion;
  }

  @Override
  public File getLocation() {
    return location;
  }

  @Override
  public List<OsgiDependency> getRequiredBundles() {
    return requiredBundles;
  }

  public static JarOsgiBundle createBundle( File file ) throws IsNotBundleException {
    return new JarOsgiBundle( file );
  }
}
