// (C) 1998-2016 Information Desire Software GmbH
// www.infodesire.com

package com.infodesire.spoint.operations;

import com.infodesire.spoint.base.Connection;
import com.infodesire.spoint.base.OperationsBase;
import com.infodesire.spoint.base.Response;
import com.infodesire.spoint.base.SPException;
import com.infodesire.spoint.model.Json;
import com.infodesire.spoint.model.SPContextInfo;
import com.infodesire.spoint.model.SPFile;
import com.infodesire.spoint.model.SPFolder;
import com.infodesire.spoint.utils.FilePath;
import com.infodesire.spoint.utils.SpointUtils;

import java.io.InputStream;
import java.io.OutputStream;
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
   * @param folderPath Relative path of folder
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
  
  
  /**
   * Create folder
   * 
   * @param connection Sharepoint server connection
   * @param relativeSiteUri Relative uri of site
   * @param folderPath Relative path of new folder
   * @return Lists found
   * @throws SPException on system error or configuration problem
   * 
   */
  public static SPFolder createFolder( Connection connection,
    String relativeSiteUri, String folderPath ) throws SPException {
    
    String formDigestValue = ensureValidDigest( connection );
    String request = "folders/add('" + enc( folderPath ) + "')";
    
    Response response = performPost( connection, relativeSiteUri, request, "",
      null, formDigestValue, "POST" );
    return Json.parseFolder( response.getContent() );
    
  }


  /**
   * Delete folder
   * 
   * @param connection Sharepoint server connection
   * @param relativeSiteUri Relative uri of site
   * @param folderPath Relative path of folder
   * @return Lists found
   * @throws SPException on system error or configuration problem
   * 
   */
  public static void deleteFolder( Connection connection,
    String relativeSiteUri, String folderPath ) throws SPException {
    
    String formDigestValue = ensureValidDigest( connection );
    String request = "GetFolderByServerRelativeUrl('/" + enc( folderPath ) + "')";
    
    performPost( connection, relativeSiteUri, request, "", null,
      formDigestValue, "DELETE" );
    
  }
  
  
  /**
   * Rename folder
   * 
   * @param connection Sharepoint server connection
   * @param relativeSiteUri Relative uri of site
   * @param folderPath Relative path of folder
   * @param newName New name of folder
   * @return 
   * @return Lists found
   * @throws SPException on system error or configuration problem
   * 
   */
  public static SPFolder renameFolder( Connection connection,
    String relativeSiteUri, String folderPath, String newName ) throws SPException {
    
    String formDigestValue = ensureValidDigest( connection );
    String request = "GetFolderByServerRelativeUrl('/" + enc( folderPath ) + "')";
    String content = "{ 'Name': '"+ newName +"' }";
    
    Response response = performPost( connection, relativeSiteUri, request,
      content, null, formDigestValue, "MERGE" );
    return Json.parseFolder( response.getContent() );
    
  }
  

  /**
   * Get files in a folder
   * 
   * @param connection Sharepoint server connection
   * @param relativeSiteUri Relative uri of site
   * @param folderPath Relative path of folder
   * @return Files found
   * @throws SPException on system error or configuration problem
   * 
   */
  public static List<SPFile> getFiles( Connection connection,
    String relativeSiteUri, String folderPath ) throws SPException {

    String request = "GetFolderByServerRelativeUrl('/" + enc( folderPath ) + "')/Files";
    Response response = performGet( connection, relativeSiteUri, request );
    return Json.parseFiles( response.getContent() );

  }
  
  
  /**
   * Get file content
   * 
   * @param connection Sharepoint server connection
   * @param relativeSiteUri Relative uri of site
   * @param folderPath Relative path of folder
   * @param fileName Name of file
   * @param target Output stream for file content
   * @throws SPException on system error or configuration problem
   * 
   */
  public static void getFileContent( Connection connection,
    String relativeSiteUri, String folderPath, String fileName,
    OutputStream target ) throws SPException {
    
    FilePath filePath = new FilePath( FilePath.parse( folderPath ), fileName );
    String request = "GetFileByServerRelativeUrl('/"
      + enc( filePath.toString() ) + "')/$value";
    
    performGetRaw( connection, relativeSiteUri, request, target );
    
  }
  
  
  /**
   * Upload file content
   * 
   * @param connection Sharepoint server connection
   * @param relativeSiteUri Relative uri of site
   * @param folderPath Relative path of folder
   * @param fileName Name of file
   * @param content Stream providing file content
   * @throws SPException on system error or configuration problem
   * 
   */
  public static void uploadFile( Connection connection, String relativeSiteUri,
    String folderPath, String fileName, InputStream content ) throws SPException {

    String formDigestValue = ensureValidDigest( connection );
    String request = "GetFolderByServerRelativeUrl('/" + enc( folderPath )
      + "')/Files/add(url='" + fileName + "',overwrite=true)";
    
    performPost( connection, relativeSiteUri, request, null, content,
      formDigestValue, null );
    
  }
  
  
  /**
   * Delete file
   * 
   * @param connection Sharepoint server connection
   * @param relativeSiteUri Relative uri of site
   * @param folderPath Relative path of folder
   * @param fileName
   * @return Lists found
   * @throws SPException on system error or configuration problem
   * 
   */
  public static void deleteFile( Connection connection,
    String relativeSiteUri, String folderPath, String fileName ) throws SPException {
    
    String formDigestValue = ensureValidDigest( connection );
    FilePath filePath = new FilePath( FilePath.parse( folderPath ), fileName );
    String request = "GetFileByServerRelativeUrl('/" + enc( filePath.toString() ) + "')";
    
    performPost( connection, relativeSiteUri, request, "", null,
      formDigestValue, "DELETE" );
    
  }

  
  /**
   * Make sure there is a valid FormDigestValue available with
   * enough (10s) remaining lifetime.
   * 
   * @param connection Current 
   * @throws SPException on system error or configuration problem
   */
  private static String ensureValidDigest( Connection connection ) throws SPException {
    
    SPContextInfo contextInfo = connection.getContextInfo();
    
    if( contextInfo != null ) {
      long now = System.currentTimeMillis();
      if( ( now - contextInfo.getClientTimestamp() ) > ( ( contextInfo
        .getFormDigestTimeoutSeconds() - 10 ) * 1000 ) ) {
        contextInfo = null; // was too old
      }
    }
    
    if( contextInfo == null ) {
      contextInfo = SiteOperations.getContextInfo( connection );
      connection.setContextInfo( contextInfo );
    }
    
    return contextInfo.getFormDigestValue();
    
  }
  
  
}


