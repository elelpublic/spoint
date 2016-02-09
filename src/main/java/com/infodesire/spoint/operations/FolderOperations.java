// (C) 1998-2016 Information Desire Software GmbH
// www.infodesire.com

package com.infodesire.spoint.operations;

import com.infodesire.spoint.base.Connection;
import com.infodesire.spoint.base.OperationsBase;
import com.infodesire.spoint.base.Response;
import com.infodesire.spoint.base.SpointException;
import com.infodesire.spoint.model.Json;
import com.infodesire.spoint.model.SPFile;
import com.infodesire.spoint.model.SPFolder;
import com.infodesire.spoint.utils.FilePath;
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
   * @return Lists found
   * @throws SpointException on system error or configuration problem
   * 
   */
  public static List<SPFolder> getRootFolders( Connection connection )
    throws SpointException {
    return getFolders( connection, null );
  }


  /**
   * Load folders
   * 
   * @param connection Sharepoint server connection
   * @param parentFolder Path of parent folder
   * @return Lists found
   * @throws SpointException on system error or configuration problem
   * 
   */
  public static List<SPFolder> getFolders( Connection connection,
    String parentFolder ) throws SpointException {

    String request = "folders";
    if( parentFolder != null ) {
      request = "getfolderbyserverrelativeurl('"
        + SpointUtils.encodeForUrl( parentFolder ) + "')/folders";
    }
    String description = "Subfolders of " + parentFolder;

    Response response = performGet( connection, API + request, description );
    return Json.parseFolders( response.getContent() );

  }


  /**
   * Get folder by relative path
   * 
   * @param connection Sharepoint server connection
   * @param folderPath Relative path of folder
   * @return Lists found
   * @throws SpointException on system error or configuration problem
   * 
   */
  public static SPFolder getFolder( Connection connection, String folderPath )
    throws SpointException {

    String request = "getfolderbyserverrelativeurl('" + enc( folderPath )
      + "')";
    String description = "Folder " + folderPath;

    Response response = performGet( connection, API + request, description );
    return Json.parseFolder( response.getContent() );

  }


  /**
   * Create folder
   * 
   * @param connection Sharepoint server connection
   * @param folderPath Relative path of new folder
   * @return Lists found
   * @throws SpointException on system error or configuration problem
   * 
   */
  public static SPFolder createFolder( Connection connection, String folderPath )
    throws SpointException {

    String formDigestValue = SiteOperations.ensureValidDigest( connection );
    String request = "folders/add('" + enc( folderPath ) + "')";
    String description = "New folder " + folderPath;

    Response response = performPost( connection, API + request, null, null,
      formDigestValue, "POST", description );
    return Json.parseFolder( response.getContent() );

  }


  /**
   * Delete folder
   * 
   * @param connection Sharepoint server connection
   * @param folderPath Relative path of folder
   * @return Lists found
   * @throws SpointException on system error or configuration problem
   * 
   */
  public static void deleteFolder( Connection connection, String folderPath )
    throws SpointException {

    String formDigestValue = SiteOperations.ensureValidDigest( connection );
    String request = "GetFolderByServerRelativeUrl('/" + enc( folderPath )
      + "')";
    String description = "Folder " + folderPath;

    performPost( connection, API + request, null, null, formDigestValue,
      "DELETE", description );

  }


  /**
   * Rename folder
   * 
   * @param connection Sharepoint server connection
   * @param folderPath Relative path of folder
   * @param newName New name of folder
   * @return
   * @return Lists found
   * @throws SpointException on system error or configuration problem
   * 
   */
  public static void renameFolder( Connection connection,
    String folderPath, String newName ) throws SpointException {

    String formDigestValue = SiteOperations.ensureValidDigest( connection );
    String request = "GetFolderByServerRelativeUrl('/"
      + encElements( FilePath.parse( folderPath ) ).toString() + "')";
    String content = "{ \"__metadata\": { \"type\": \"SP.Folder\" }, \"Name\": \"" + newName + "\" }";
    String description = "Folder " + folderPath;

    performPost( connection, API + request, content, null,
      formDigestValue, "MERGE", description );

  }


  /**
   * Get files in a folder
   * 
   * @param connection Sharepoint server connection
   * @param folderPath Relative path of folder
   * @return Files found
   * @throws SpointException on system error or configuration problem
   * 
   */
  public static List<SPFile> getFiles( Connection connection, String folderPath )
    throws SpointException {

    String request = "GetFolderByServerRelativeUrl('/" + enc( folderPath )
      + "')/Files";
    String description = "Files of folder " + folderPath;

    Response response = performGet( connection, API + request, description );
    return Json.parseFiles( response.getContent() );

  }


}
