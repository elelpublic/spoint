// (C) 1998-2016 Information Desire Software GmbH
// www.infodesire.com

package com.infodesire.spoint.model;import groovy.transform.ToString;


/**
 * Sharepoint FileVersion object
 * 
 */
@ToString(includeNames=true,includePackage=false)
public class SPFileVersion {

  String checkInComment
  Date created
  String id
  boolean isCurrentVersion
  int size
  String url
  String versionLabel

}


