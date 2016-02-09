// (C) 1998-2016 Information Desire Software GmbH
// www.infodesire.com

package com.infodesire.spoint.model;import groovy.transform.ToString;


/**
 * Sharepoint exception object
 * 
 */
@ToString(includeNames=true,includePackage=false)
public class SPException {
  
  String code
  String name
  String lang
  String message

}


