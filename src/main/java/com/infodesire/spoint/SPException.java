// (C) 1998-2016 Information Desire Software GmbH
// www.infodesire.com

package com.infodesire.spoint;


/**
 * Exception in Sharepoint interface
 *
 */
public class SPException extends Exception {
  
  
  private static final long serialVersionUID = 1093656393163389613L;
  private SPCode code;
  private Response response;
  

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
