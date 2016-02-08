// (C) 1998-2016 Information Desire Software GmbH
// www.infodesire.com

package com.infodesire.spoint.base;


/**
 * Everything required to connect to a sharepoint server.
 *
 */
public class Config {
  
  String url
  String user
  String password
  boolean useJson = true
  
  String workstation
  String domain
  
  int connectionTimeoutMs = 3000
  
  
  /**
   * How long may a request take. Consider uploading large files too!
   * 0 means indefinite, -1 means none (system default).
   */
  int requestTimeoutMs = -1
  

}


