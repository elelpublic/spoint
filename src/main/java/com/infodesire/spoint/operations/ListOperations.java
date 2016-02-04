// (C) 1998-2016 Information Desire Software GmbH
// www.infodesire.com

package com.infodesire.spoint.operations;

import com.infodesire.spoint.base.Connection;
import com.infodesire.spoint.base.OperationsBase;
import com.infodesire.spoint.base.Response;
import com.infodesire.spoint.base.SPException;
import com.infodesire.spoint.model.Json;
import com.infodesire.spoint.model.SPList;
import com.infodesire.spoint.model.SPListItem;

import java.util.List;


/**
 * Operations on sharepoint lists
 *
 */
public class ListOperations extends OperationsBase {
  

  /**
   * Load all lists
   * 
   * @param connection Sharepoint server connection
   * @param relativeSiteUri Relative uri of site
   * @return Lists found
   * @throws SPException on system error or configuration problem
   * 
   */
  public static List<SPList> getLists( Connection connection, String relativeSiteUri ) throws SPException {

    Response response = performGet( connection, relativeSiteUri, "lists" );
    return Json.parseLists( response.getContent() );

  }
  
  
  /**
   * Load list by title
   * 
   * @param connection Sharepoint server connection
   * @param relativeSiteUri Relative uri of site
   * @param title List title
   * @return List
   * @throws SPException on system error or configuration problem
   * 
   */
  public static SPList getListByTitle( Connection connection, String relativeSiteUri, String title )
    throws SPException {
    
    Response response = performGet( connection, relativeSiteUri,
      "lists/getbytitle('" + enc( title ) + "')" );
    return Json.parseList( response.getContent() );

  }
  
  
  /**
   * Load list by id
   * 
   * @param connection Sharepoint server connection
   * @param relativeSiteUri Relative uri of site
   * @param id List id (GUID)
   * @return List
   * @throws SPException on system error or configuration problem
   * 
   */
  public static SPList getListById( Connection connection, String relativeSiteUri, String id )
    throws SPException {
    
    Response response = performGet( connection, relativeSiteUri,
      "lists(guid'" + id + "')" );
    return Json.parseList( response.getContent() );
    
  }
  
  
  /**
   * Load list items by list title
   * 
   * @param connection Sharepoint server connection
   * @param relativeSiteUri Relative uri of site
   * @param title List title
   * @return List
   * @throws SPException on system error or configuration problem
   * 
   */
  public static List<SPListItem> getListItemsByTitle( Connection connection,
    String relativeSiteUri, String title ) throws SPException {
    
    Response response = performGet( connection, relativeSiteUri,
      "lists/getbytitle('" + enc( title ) + "')/items" );
    return Json.parseListItems( response.getContent() );
    
  }


}


