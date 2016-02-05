// (C) 1998-2016 Information Desire Software GmbH
// www.infodesire.com

package com.infodesire.spoint.model;import groovy.transform.ToString;


/**
 * Sharepoint File object
 * 
 */
@ToString(includeNames=true,includePackage=false)
public class SPFile {
  

  String name
  String serverRelativeUrl
  Date timeCreated
  Date timeLastModified
  int length
  String checkInComment
  int checkOutType
  String contentTag
  boolean exists
  String title
  

}


