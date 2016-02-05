// (C) 1998-2016 Information Desire Software GmbH
// www.infodesire.com

package com.infodesire.spoint.operations;


/**
 * Defines versioning when checking in files 
 *
 */
public enum CheckinType {
  
  MinorCheckIn( 0 ),
  
  MajorCheckIn( 1 ),
  
  OverwriteCheckIn( 2 )
  
  ;
  
  private int type;

  private CheckinType( int type ) {
    this.type = type;
  }
  
  public int getType() {
    return type;
  }

}


