// (C) 1998-2016 Information Desire Software GmbH
// www.infodesire.com

package com.infodesire.spoint.base;

import groovy.transform.ToString;

import org.apache.http.StatusLine;


/**
 * Response from REST request to sharepoint server.
 *
 */
@ToString
public class Response {
  
  StatusLine statusLine
  int statusCode
  String content


  public boolean isOk() {
    return statusCode >= 200 && statusCode <= 200;
  }

  public String toShortString() {
    return ( statusLine == null ? "" : statusLine.toString() ) //
      + ( content == null ? "" : " " //
        + ( content.length() > 100 ? content.substring( 0, 100 ) + "..." : content ) );
  }
  
}

