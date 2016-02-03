// (C) 1998-2016 Information Desire Software GmbH
// www.infodesire.com

package com.infodesire.spoint.model;

import com.infodesire.spoint.SPCode
import com.infodesire.spoint.SPException

import groovy.json.JsonException
import groovy.json.JsonSlurper


/**
 * Parse and format model object in JSON
 *
 */
public class Json {

  public static List<SPList> parseLists( String content ) throws SPException {
    
    try {
      def json = new JsonSlurper().parseText( content );
      json['d']['results'].collect { list ->
        return createList( list );
      }
    }
    catch( JsonException ex ) {
      throw new SPException( SPCode.JSON_ERROR, null, ex, null ); 
    }
    
  }

  public static SPList parseList( String content ) {
    
    try {
      def json = new JsonSlurper().parseText( content );
      def list = json['d']
      return createList( list );
    }
    catch( JsonException ex ) {
      throw new SPException( SPCode.JSON_ERROR, null, ex, null ); 
    }
    
  }
  
  public static List<SPListItem> parseListItems( String content ) {
    
    try {
      def json = new JsonSlurper().parseText( content );
      json['d']['results'].collect { listItem ->
        return createListItem( listItem );
      }
    }
    catch( JsonException ex ) {
      throw new SPException( SPCode.JSON_ERROR, null, ex, null ); 
    }
    
  }
  
  
  private static SPList createList( Object list ) {
    def metadata = list['__metadata'];
    return new SPList(
      id: list['Id'],
      uri: metadata['uri'],
      title: list['Title'],
      description: list['Description']
    );
  }
  

  private static SPListItem createListItem( Object listItem ) {

//    String guid
//    String uri
//    String title
//    String type
  
    def metadata = listItem['__metadata'];
    return new SPListItem(
      guid: listItem['GUID'],
      uri: metadata['uri'],
      title: listItem['Title'],
      type: metadata['type']
    );

  }
  
  
}


