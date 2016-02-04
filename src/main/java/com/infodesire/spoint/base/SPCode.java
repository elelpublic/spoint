// (C) 1998-2016 Information Desire Software GmbH
// www.infodesire.com

package com.infodesire.spoint.base;


/**
 * Sharepoint interface status codes
 *
 */
public enum SPCode {
  
  
  OK( "Operation was performed successfully" ),
  
  HTTP_ERROR( "Error in HTTP communication with server" ),

  JSON_ERROR( "Error parsing JSON reply from server" )

  ;
  
  
  private String description;

  private SPCode( String description ) {
    this.description = description;
  }
  
  
  public String getDescription() {
    return description;
  } 
  
  
  public String toString() {
    return name() + " " + description;
  }
  
  
}


