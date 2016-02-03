// (C) 1998-2016 Information Desire Software GmbH
// www.infodesire.com

package com.infodesire.spoint;

import static com.infodesire.spoint.SPCode.HTTP_ERROR;

import com.infodesire.spoint.model.Json;
import com.infodesire.spoint.model.SPList;
import com.infodesire.spoint.model.SPListItem;
import com.infodesire.spoint.utils.FilePath;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;



public class HighLevel {
  

  /**
   * Load all lists
   * 
   * @param connection Sharepoint server connection
   * @param pathName Parent path of lists
   * @return Lists found
   * @throws SPException on system error or configuration problem
   * 
   */
  public static List<SPList> getLists( Connection connection, String pathName ) throws SPException {

    FilePath path = FilePath.parse( pathName );
    path = new FilePath( path, "lists" );
    
    try {
      
      Response response = LowLevel.performGet( connection, path.toString() );
      if( !response.isOk() ) {
        throw new SPException( HTTP_ERROR, response, null, null );
      }
      
      return Json.parseLists( response.getContent() );
      
    }
    catch( ClientProtocolException ex ) {
      throw new SPException( HTTP_ERROR, null, ex, "Client protocol error" );
    }
    catch( IOException ex ) {
      throw new SPException( HTTP_ERROR, null, ex, "Cannot read response text" );
    }

  }
  
  
  /**
   * Load list by title
   * 
   * @param connection Sharepoint server connection
   * @param pathName Parent path of lists
   * @param title List title
   * @return List
   * @throws SPException on system error or configuration problem
   * 
   */
  public static SPList getListByTitle( Connection connection, String pathName, String title )
    throws SPException {
    
    return getList( connection, pathName, "lists/getbytitle('" + title + "')" );

  }
  
  
  /**
   * Load list by id
   * 
   * @param connection Sharepoint server connection
   * @param pathName Parent path of lists
   * @param id List id (GUID)
   * @return List
   * @throws SPException on system error or configuration problem
   * 
   */
  public static SPList getListById( Connection connection, String pathName, String id )
    throws SPException {
    
    return getList( connection, pathName, "lists(guid'" + id + "')" );
    
  }
  
  
  private static SPList getList( Connection connection, String pathName, String uri ) throws SPException {
    
    FilePath path = FilePath.parse( pathName );
    path = new FilePath( path, uri );

    try {
      
      Response response = LowLevel.performGet( connection, path.toString() );
      if( !response.isOk() ) {
        throw new SPException( HTTP_ERROR, response, null, null );
      }
      
      return Json.parseList( response.getContent() );
      
    }
    catch( ClientProtocolException ex ) {
      throw new SPException( HTTP_ERROR, null, ex, "Client protocol error" );
    }
    catch( IOException ex ) {
      throw new SPException( HTTP_ERROR, null, ex, "Cannot read response text" );
    }

  }
  

  /**
   * Load list items by list title
   * 
   * @param connection Sharepoint server connection
   * @param pathName Parent path of lists
   * @param title List title
   * @return List
   * @throws SPException on system error or configuration problem
   * 
   */
  public static List<SPListItem> getListItemsByTitle( Connection connection, String pathName, String title )
    throws SPException {
    
    FilePath path = FilePath.parse( pathName );
    path = new FilePath( path, "lists/getbytitle('" + title + "')/items" );

    try {
      
      Response response = LowLevel.performGet( connection, path.toString() );
      if( !response.isOk() ) {
        throw new SPException( HTTP_ERROR, response, null, null );
      }
      
      return Json.parseListItems( response.getContent() );
      
    }
    catch( ClientProtocolException ex ) {
      throw new SPException( HTTP_ERROR, null, ex, "Client protocol error" );
    }
    catch( IOException ex ) {
      throw new SPException( HTTP_ERROR, null, ex, "Cannot read response text" );
    }

  }


}


