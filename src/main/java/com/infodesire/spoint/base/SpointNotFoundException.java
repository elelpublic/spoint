// (C) 1998-2016 Information Desire Software GmbH
// www.infodesire.com

package com.infodesire.spoint.base;


/**
 * A requestet entity was not found on the server
 *
 */
public class SpointNotFoundException extends SpointException {
  
  
  private static final long serialVersionUID = -2992288844512822313L;
  
  
  private String what;
  

  public SpointNotFoundException( Response response, String what, Throwable cause, String message ) {
    super( SpointCode.NOT_FOUND, response, cause, message );
    this.what = what;
  }


  /**
   * @return What was not found
   * 
   */
  public String getWhat() {
    return what;
  }
  
  
  public String toString() {
    return ( what == null ? "" : " " + what.toString() ) //
      + " " + super.toString();
  }
  

}
