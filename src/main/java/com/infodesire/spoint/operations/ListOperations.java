// (C) 1998-2016 Information Desire Software GmbH
// www.infodesire.com

package com.infodesire.spoint.operations;

import com.infodesire.spoint.base.Connection;
import com.infodesire.spoint.base.OperationsBase;
import com.infodesire.spoint.base.Response;
import com.infodesire.spoint.base.SpointException;
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
   * @return Lists found
   * @throws SpointException on system error or configuration problem
   * 
   */
  public static List<SPList> getLists( Connection connection )
    throws SpointException {

    String description = "All lists";
    Response response = performGet( connection, API + "lists", description );
    return Json.parseLists( response.getContent() );

  }


  /**
   * Load list by title
   * 
   * @param connection Sharepoint server connection
   * @param title List title
   * @return List
   * @throws SpointException on system error or configuration problem
   * 
   */
  public static SPList getListByTitle( Connection connection, String title )
    throws SpointException {

    String description = "List with title " + title;

    Response response = performGet( connection, API + "lists/getbytitle('"
      + enc( title ) + "')", description );
    return Json.parseList( response.getContent() );

  }


  /**
   * Load list by id
   * 
   * @param connection Sharepoint server connection
   * @param id List id (GUID)
   * @return List
   * @throws SpointException on system error or configuration problem
   * 
   */
  public static SPList getListById( Connection connection, String id )
    throws SpointException {

    String description = "List with id " + id;

    Response response = performGet( connection, API + "lists(guid'" + id + "')",
      description );
    return Json.parseList( response.getContent() );

  }


  /**
   * Load list items by list title
   * 
   * @param connection Sharepoint server connection
   * @param title List title
   * @return List
   * @throws SpointException on system error or configuration problem
   * 
   */
  public static List<SPListItem> getListItemsByTitle( Connection connection,
    String title ) throws SpointException {

    String description = "Items of list with title " + title;

    Response response = performGet( connection, API + "lists/getbytitle('"
      + enc( title ) + "')/items", description );
    return Json.parseListItems( response.getContent() );

  }


}
