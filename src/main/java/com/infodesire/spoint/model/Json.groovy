// (C) 1998-2016 Information Desire Software GmbH
// www.infodesire.com

package com.infodesire.spoint.model;

import com.infodesire.spoint.base.SPCode
import com.infodesire.spoint.base.SPException

import groovy.json.JsonException
import groovy.json.JsonSlurper

import java.text.DecimalFormat;
import java.text.SimpleDateFormat


/**
 * Parse and format model objects in JSON
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
  
  
  public static List<SPFolder> parseFolders( String content ) throws SPException {
    
    try {
      def json = new JsonSlurper().parseText( content );
      json['d']['results'].collect { folder ->
        return createFolder( folder );
      }
    }
    catch( JsonException ex ) {
      throw new SPException( SPCode.JSON_ERROR, null, ex, null ); 
    }
    
  }
  
  
  public static SPFolder parseFolder( String content ) throws SPException {
    
    try {
      def json = new JsonSlurper().parseText( content );
      def folder = json['d'];
      return createFolder( folder );
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
    def metadata = listItem['__metadata'];
    return new SPListItem(
      guid: listItem['GUID'],
      uri: metadata['uri'],
      title: listItem['Title'],
      type: metadata['type']
    );
  }
  
  
  private static SPFolder createFolder( Object folder ) {
    def metadata = folder['__metadata'];
    return new SPFolder(
      name: folder['Name'],
      relativeUri: folder['ServerRelativeUrl'],
      itemCount: folder['ItemCount']
    );
  }

  private static SPFile createFile( Object file ) {
    def metadata = file['__metadata'];
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    DecimalFormat nf = new DecimalFormat("#");
    return new SPFile(
      name: file['Name'],
      serverRelativeUrl: file['ServerRelativeUrl'],
      timeCreated: df.parse( file['TimeLastModified'] ),
      timeLastModified: df.parse( file['TimeLastModified'] ),
      length: nf.parse( file['Length'] ),
      checkInComment: file['CheckInComment'],
      checkOutType: file['CheckOutType'],
      contentTag: file['ContentTag'],
      exists: file['Exists'],
      title: file['Title']
    );
  }
  
  public static SPContextInfo parseContextInfo( String content ) {
    try {
      def json = new JsonSlurper().parseText( content );
      def info = json['d']['GetContextWebInformation']
      return createContextInfo( info );
    }
    catch( JsonException ex ) {
      throw new SPException( SPCode.JSON_ERROR, null, ex, null ); 
    }
  }
  
  private static SPContextInfo createContextInfo( Object info ) {
    try {
      return new SPContextInfo(
        formDigestTimeoutSeconds: info['FormDigestTimeoutSeconds'],
        formDigestValue: info['FormDigestValue'],
        siteFullUrl: info['SiteFullUrl'],
        webFullUrl: info['WebFullUrl']
      );
    }
    catch( JsonException ex ) {
      throw new SPException( SPCode.JSON_ERROR, null, ex, null ); 
    }
  }

  
  public static List<SPFile> parseFiles( String content ) throws SPException {
    
    try {
      def json = new JsonSlurper().parseText( content );
      json['d']['results'].collect { folder ->
        return createFile( folder );
      }
    }
    catch( JsonException ex ) {
      throw new SPException( SPCode.JSON_ERROR, null, ex, null ); 
    }
    
  }
  
  
}


