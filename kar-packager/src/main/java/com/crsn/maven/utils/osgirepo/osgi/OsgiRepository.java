package com.crsn.maven.utils.osgirepo.osgi;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

public class OsgiRepository {

  private List<OsgiBundle> bundles;

  private OsgiRepository( List<OsgiBundle> bundles ) {
    this.bundles = bundles;
  }

  public static OsgiRepository createRepository( File directory ) {
    LinkedList<OsgiBundle> bundles = new LinkedList<OsgiBundle>();
    if( directory == null ) {
      throw new NullPointerException( "Null directory." );
    }
    File[] listFiles = directory.listFiles();
    for( File file : listFiles ) {
      addBundleToRepository( bundles, file );
    }
    return new OsgiRepository( bundles );
  }

  private static void addBundleToRepository( LinkedList<OsgiBundle> bundles, File file ) {
    try {
      boolean isJarBundle = file.isFile() && file.getName().toLowerCase().endsWith( "jar" );
      if( isJarBundle ) {
        JarOsgiBundle osgiBundle = JarOsgiBundle.createBundle( file );
        addBundle( bundles, osgiBundle );
      } else if( file.isDirectory() ) {
        DirectoryOsgiBundle osgiBundle;
        osgiBundle = DirectoryOsgiBundle.createBundle( file );
        addBundle( bundles, osgiBundle );
      }
    } catch( IsNotBundleException e ) {
      throw new IllegalStateException( "Could not create bundle.", e );
    }
  }

  private static void addBundle( LinkedList<OsgiBundle> plugins, OsgiBundle osgiPlugin ) {
    plugins.add( osgiPlugin );
  }

  public static OsgiRepository createRepository( List<? extends OsgiBundle> plugins ) {
    return new OsgiRepository( new LinkedList<OsgiBundle>( plugins ) );
  }

  public List<OsgiBundle> getPlugins() {
    return Collections.unmodifiableList( bundles );
  }

  public OsgiBundle resolveDependency( OsgiDependency dependency ) {
    if( dependency == null ) {
      throw new NullPointerException( "Null dependency." );
    }
    TreeSet<OsgiBundle> resolved = new TreeSet<OsgiBundle>( new Comparator<OsgiBundle>() {

      @Override
      public int compare( OsgiBundle o1, OsgiBundle o2 ) {
        return o1.getVersion().compareTo( o2.getVersion() );
      }
    } );
    for( OsgiBundle osgiPlugin : this.bundles ) {
      if( dependency.isResolvedBy( osgiPlugin ) ) {
        resolved.add( osgiPlugin );
      }
    }
    if( resolved.isEmpty() ) {
      return null;
    }
    return resolved.last();
  }
}
