// (C) 1998-2016 Information Desire Software GmbH
// www.infodesire.com

package com.infodesire.spoint.model;

import groovy.transform.ToString

/**
 * Sharepoint site context info 
 * (contains RequestDigest which is needed for all writing operations)
 * 
 */
@ToString(includeNames=true,includePackage=false)
public class SPContextInfo {
  
  String formDigestValue
  int formDigestTimeoutSeconds
  String siteFullUrl
  String webFullUrl
  
  /** When was this context info created */
  long clientTimestamp = System.currentTimeMillis()

}


