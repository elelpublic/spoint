// (C) 1998-2016 Information Desire Software GmbH
// www.infodesire.com

package com.infodesire.spoint.operations;

import com.infodesire.spoint.base.Connection;
import com.infodesire.spoint.base.OperationsBase;
import com.infodesire.spoint.base.Response;
import com.infodesire.spoint.base.SPException;
import com.infodesire.spoint.model.Json;
import com.infodesire.spoint.model.SPFolder;
import com.infodesire.spoint.utils.SpointUtils;

import java.util.List;


/**
 * Operations on sharepoint folders
 *
 */
public class FolderOperations extends OperationsBase {
  

  /**
   * Load all root folders
   * 
   * @param connection Sharepoint server connection
   * @param relativeSiteUri Relative uri of site
   * @return Lists found
   * @throws SPException on system error or configuration problem
   * 
   */
  public static List<SPFolder> getRootFolders( Connection connection,
    String relativeSiteUri ) throws SPException {
    return getFolders( connection, relativeSiteUri, null );
  }
  
  
  /**
   * Load folders
   * 
   * @param connection Sharepoint server connection
   * @param relativeSiteUri Relative uri of site
   * @param parentFolder Path of parent folder
   * @return Lists found
   * @throws SPException on system error or configuration problem
   * 
   */
  public static List<SPFolder> getFolders( Connection connection,
    String relativeSiteUri, String parentFolder ) throws SPException {

    String request = "folders";
    if( parentFolder != null ) {
      request = "getfolderbyserverrelativeurl('"
        + SpointUtils.encodeForUrl( parentFolder ) + "')/folders";
    }
    
    Response response = performGet( connection, relativeSiteUri,
      request );
    return Json.parseFolders( response.getContent() );

  }


  /**
   * Get folder by relative path
   * 
   * @param connection Sharepoint server connection
   * @param relativeSiteUri Relative uri of site
   * @param folderPath Relative path folder
   * @return Lists found
   * @throws SPException on system error or configuration problem
   * 
   */
  public static SPFolder getFolder( Connection connection,
    String relativeSiteUri, String folderPath ) throws SPException {
    
    Response response = performGet( connection, relativeSiteUri,
      "getfolderbyserverrelativeurl('" + enc( folderPath ) + "')" );
    return Json.parseFolder( response.getContent() );

  }
  
  
//  /**
//   * Create folder
//   * 
//   * @param connection Sharepoint server connection
//   * @param relativeSiteUri Relative uri of site
//   * @param folderPath Relative path folder
//   * @return Lists found
//   * @throws SPException on system error or configuration problem
//   * 
//   */
//  public static SPFolder createFolder( Connection connection,
//    String relativeSiteUri, String parentPath, String name ) throws SPException {
//    
//    Response response = performGet( connection, relativeSiteUri,
//      "getfolderbyserverrelativeurl('" + enc( folderPath ) + "')" );
//    return Json.parseFolder( response.getContent() );
//    
//  }
  
  
}


