package com.crsn.maven.utils.osgirepo.osgi;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.osgi.util.ManifestElement;
import org.osgi.framework.BundleException;
import org.osgi.framework.Version;

public class BundleParser {

  private static final String BUNDLE_CLASS_PATH = "Bundle-ClassPath";
  private static final String REQUIRE_BUNDLE = "Require-Bundle";
  private static final String BUNDLE_VERSION = "Bundle-Version";
  private static final String BUNDLE_SYMBOLIC_NAME = "Bundle-SymbolicName";
  private final String bundleName;
  private final Version bundleVersion;
  private final LinkedList<OsgiDependency> requiredBundles;
  private final List<String> classPathEntries = new LinkedList<String>();

  private BundleParser( InputStream content ) throws CouldNotParseBundleDescriptorException {
    try {
      HashMap<String, String> headers = new HashMap<String, String>();
      ManifestElement.parseBundleManifest( content, headers );
      String unparsedSymbolicName = headers.get( BUNDLE_SYMBOLIC_NAME );
      if( unparsedSymbolicName == null ) {
        throw new CouldNotParseBundleDescriptorException( String.format( "Could not parse bundle, header %s is missing",
                                                                         BUNDLE_SYMBOLIC_NAME ) );
      }
      ManifestElement[] elements = ManifestElement.parseHeader( BUNDLE_SYMBOLIC_NAME,
                                                                unparsedSymbolicName );
      if( elements.length != 1 ) {
        throw new IllegalArgumentException( "more than one symbolic name" );
      }
      this.bundleName = elements[ 0 ].getValue();
      this.bundleVersion = new Version( headers.get( BUNDLE_VERSION ) );
      this.requiredBundles = new LinkedList<OsgiDependency>();
      ManifestElement[] requireElements = ManifestElement.parseHeader( REQUIRE_BUNDLE,
                                                                       headers.get( REQUIRE_BUNDLE ) );
      if( requireElements != null ) {
        for( ManifestElement manifestElement : requireElements ) {
          String bundleVersion = manifestElement.getAttribute( "bundle-version" );
          String bundleName = manifestElement.getValue();
          requiredBundles.add( new OsgiDependency( bundleName,
                                                   VersionRange.parseVersionRange( bundleVersion ) ) );
        }
      }
      String bundleClassPath = headers.get( BUNDLE_CLASS_PATH );
      if( bundleClassPath != null ) {
        for( String entry : bundleClassPath.split( "," ) ) {
          classPathEntries.add( entry.trim() );
        }
      }
    } catch( IOException e ) {
      throw new RuntimeException( e );
    } catch( BundleException e ) {
      throw new RuntimeException( e );
    }
  }

  public String getBundleName() {
    return bundleName;
  }

  public Version getBundleVersion() {
    return bundleVersion;
  }

  public List<OsgiDependency> getRequiredBundles() {
    return Collections.unmodifiableList( requiredBundles );
  }

  public List<String> getClassPathEntries() {
    return Collections.unmodifiableList( classPathEntries );
  }

  public static BundleParser parse( InputStream content, String absolutePath )
    throws CouldNotParseBundleDescriptorException
  {
    return new BundleParser( content );
  }
}
