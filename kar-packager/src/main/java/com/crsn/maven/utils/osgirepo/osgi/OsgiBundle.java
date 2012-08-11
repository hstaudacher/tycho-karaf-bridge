package com.crsn.maven.utils.osgirepo.osgi;

import java.io.File;
import java.util.List;

import org.osgi.framework.Version;

public interface OsgiBundle {

  public abstract String getName();

  public abstract Version getVersion();

  public abstract File getLocation();

  public abstract List<OsgiDependency> getRequiredBundles();
}