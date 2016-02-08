// (C) 1998-2016 Information Desire Software GmbH
// www.infodesire.com

package com.infodesire.spoint.base;


/**
 * Exception in Sharepoint interface
 *
 */
public class SPException extends Exception {
  
  
  protected static final long serialVersionUID = 1093656393163389613L;
  protected SPCode code;
  protected Response response;
  

  public SPException( SPCode code, Response response, Throwable cause, String message ) {
    super( message, cause );
    this.code = code;
    this.response = response;
  }


  /**
   * @return the code
   */
  public SPCode getCode() {
    return code;
  }
  
  
  /**
   * @return the response
   */
  public Response getResponse() {
    return response;
  }
  
  
  public String toString() {
    return ( code == null ? "" : code.toString() ) //
      + ( response == null ? "" : " " + response.toShortString() ) //
      + " " + super.toString();
  }
  

}
