package com.crsn.maven.utils.osgirepo.osgi;

public class CouldNotParseBundleDescriptorException extends Exception {

  private static final long serialVersionUID = 1L;

  public CouldNotParseBundleDescriptorException() {
    super();
  }

  public CouldNotParseBundleDescriptorException( String message, Throwable cause ) {
    super( message, cause );
  }

  public CouldNotParseBundleDescriptorException( String message ) {
    super( message );
  }

  public CouldNotParseBundleDescriptorException( Throwable cause ) {
    super( cause );
  }
}
