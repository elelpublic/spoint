// (C) 1998-2016 Information Desire Software GmbH
// www.infodesire.com

package com.infodesire.spoint.model;


/**
 * Code numbers returned by sharepoint server
 *
 */
public enum SPCodes {
  
  FILE_NOT_FOUND( -2146232832 )
  
  ;
  
  private int codeNumber;

  private SPCodes( int codeNumber ) {
    this.codeNumber = codeNumber;
  }
  
  public int getCodeNumber() {
    return codeNumber;
  }
  
  public String toString() {
    return name() + " (" + codeNumber + ")";
  }

}
