package com.crsn.maven.utils.osgirepo.osgi;

public class IsNotBundleException extends Exception {

  private static final long serialVersionUID = 1L;

  public IsNotBundleException() {
    super();
  }

  public IsNotBundleException( String message, Throwable nested ) {
    super( message, nested );
  }

  public IsNotBundleException( String message ) {
    super( message );
  }

  public IsNotBundleException( Throwable nested ) {
    super( nested );
  }
}
