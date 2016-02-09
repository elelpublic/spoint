// (C) 1998-2016 Information Desire Software GmbH
// www.infodesire.com

package com.infodesire.spoint.operations;

import com.infodesire.spoint.base.Connection;
import com.infodesire.spoint.base.OperationsBase;
import com.infodesire.spoint.base.Response;
import com.infodesire.spoint.base.SPException;
import com.infodesire.spoint.model.Json;
import com.infodesire.spoint.model.SPFile;
import com.infodesire.spoint.model.SPFileVersion;
import com.infodesire.spoint.model.SPFolder;
import com.infodesire.spoint.utils.FilePath;
import com.infodesire.spoint.utils.SpointUtils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
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
   * @throws SPException on system error or configuration problem
   * 
   */
  public static List<SPFolder> getRootFolders( Connection connection )
    throws SPException {
    return getFolders( connection, null );
  }


  /**
   * Load folders
   * 
   * @param connection Sharepoint server connection
   * @param parentFolder Path of parent folder
   * @return Lists found
   * @throws SPException on system error or configuration problem
   * 
   */
  public static List<SPFolder> getFolders( Connection connection,
    String parentFolder ) throws SPException {

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
   * @throws SPException on system error or configuration problem
   * 
   */
  public static SPFolder getFolder( Connection connection, String folderPath )
    throws SPException {

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
   * @throws SPException on system error or configuration problem
   * 
   */
  public static SPFolder createFolder( Connection connection, String folderPath )
    throws SPException {

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
   * @throws SPException on system error or configuration problem
   * 
   */
  public static void deleteFolder( Connection connection, String folderPath )
    throws SPException {

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
   * @throws SPException on system error or configuration problem
   * 
   */
  public static void renameFolder( Connection connection,
    String folderPath, String newName ) throws SPException {

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
   * @throws SPException on system error or configuration problem
   * 
   */
  public static List<SPFile> getFiles( Connection connection, String folderPath )
    throws SPException {

    String request = "GetFolderByServerRelativeUrl('/" + enc( folderPath )
      + "')/Files";
    String description = "Files of folder " + folderPath;

    Response response = performGet( connection, API + request, description );
    return Json.parseFiles( response.getContent() );

  }


  /**
   * Get file content
   * 
   * @param connection Sharepoint server connection
   * @param folderPath Relative path of folder
   * @param fileName Name of file
   * @param target Output stream for file content
   * @throws SPException on system error or configuration problem
   * 
   */
  public static void getFileContent( Connection connection, String folderPath,
    String fileName, OutputStream target ) throws SPException {

    FilePath filePath = new FilePath( FilePath.parse( folderPath ), fileName );
    String request = "GetFileByServerRelativeUrl('/"
      + enc( filePath.toString() ) + "')/$value";
    String description = "Content of file " + fileName + " in folder "
      + folderPath;

    performGetRaw( connection, API + request, target, description );

  }


  /**
   * Get file content as a string
   * 
   * @param connection Sharepoint server connection
   * @param folderPath Relative path of folder
   * @param fileName Name of file
   * @param target Output stream for file content
   * @throws SPException on system error or configuration problem
   * 
   */
  public static String getFileContent( Connection connection, String folderPath,
    String fileName ) throws SPException {

    ByteArrayOutputStream bout = new ByteArrayOutputStream();
    getFileContent( connection, folderPath, fileName, bout );
    try {
      return new String( bout.toByteArray(), "UTF-8" );
    }
    catch( UnsupportedEncodingException ex ) {
      throw new RuntimeException( ex );
    }
    
  }
  
  
  /**
   * Upload file content
   * 
   * @param connection Sharepoint server connection
   * @param folderPath Relative path of folder
   * @param fileName Name of file
   * @param content Stream providing file content
   * @throws SPException on system error or configuration problem
   * 
   */
  public static void uploadFile( Connection connection, String folderPath,
    String fileName, InputStream content ) throws SPException {

    String formDigestValue = SiteOperations.ensureValidDigest( connection );
    String request = "GetFolderByServerRelativeUrl('/" + enc( folderPath )
      + "')/Files/add(url='" + fileName + "',overwrite=true)";
    String description = "File " + fileName + " in folder " + folderPath;

    performPost( connection, API + request, null, content, formDigestValue,
      null, description );

  }


  /**
   * Delete file
   * 
   * @param connection Sharepoint server connection
   * @param folderPath Relative path of folder
   * @param fileName Name of file
   * @throws SPException on system error or configuration problem
   * 
   */
  public static void deleteFile( Connection connection, String folderPath,
    String fileName ) throws SPException {

    String formDigestValue = SiteOperations.ensureValidDigest( connection );
    FilePath filePath = new FilePath( FilePath.parse( folderPath ), fileName );
    String request = "GetFileByServerRelativeUrl('/"
      + enc( filePath.toString() ) + "')";
    String description = "File " + fileName + " in folder " + folderPath;

    performPost( connection, API + request, null, null, formDigestValue,
      "DELETE", description );

  }


  /**
   * Check out file
   * 
   * @param connection Sharepoint server connection
   * @param folderPath Relative path of folder
   * @param fileName Name of file
   * @return Lists found
   * @throws SPException on system error or configuration problem
   * 
   */
  public static void checkOutFile( Connection connection, String folderPath,
    String fileName ) throws SPException {

    String formDigestValue = SiteOperations.ensureValidDigest( connection );
    FilePath filePath = new FilePath( FilePath.parse( folderPath ), fileName );
    String request = "GetFileByServerRelativeUrl('/"
      + enc( filePath.toString() ) + "')/CheckOut()";
    String description = "File " + fileName + " in folder " + folderPath;

    performPost( connection, API + request, null, null, formDigestValue, null,
      description );

  }


  /**
   * Check in file
   * 
   * @param connection Sharepoint server connection
   * @param folderPath Relative path of folder
   * @param fileName Name of file
   * @param comment Check in comment
   * @return Lists found
   * @throws SPException on system error or configuration problem
   * 
   */
  public static void checkInFile( Connection connection, String folderPath,
    String fileName, String comment, CheckinType checkInType )
    throws SPException {

    String formDigestValue = SiteOperations.ensureValidDigest( connection );
    FilePath filePath = new FilePath( FilePath.parse( folderPath ), fileName );
    String request = "GetFileByServerRelativeUrl('/"
      + enc( filePath.toString() ) + "')/CheckIn(comment='" + enc( comment )
      + "',checkintype=" + checkInType.getType() + ")";
    String description = "File " + fileName + " in folder " + folderPath;

    performPost( connection, API + request, null, null, formDigestValue, null,
      description );

  }


  /**
   * Get versions of a file
   * 
   * @param connection Sharepoint server connection
   * @param folderPath Relative path of folder
   * @param fileName Name of file
   * @return Files found
   * @throws SPException on system error or configuration problem
   * 
   */
  public static List<SPFileVersion> getFileVersions( Connection connection,
    String folderPath, String fileName ) throws SPException {

    FilePath filePath = new FilePath( FilePath.parse( folderPath ), fileName );
    String request = "GetFileByServerRelativeUrl('/"
      + enc( filePath.toString() ) + "')/Versions";
    String description = "Versions of file " + fileName + " in folder "
      + folderPath;

    Response response = performGet( connection, API + request, description );
    return Json.parseFileVersions( response.getContent() );

  }


  /**
   * Get version of a file
   * 
   * @param connection Sharepoint server connection
   * @param folderPath Relative path of folder
   * @param fileName Name of file
   * @param id File version id
   * @return Files found
   * @throws SPException on system error or configuration problem
   * 
   */
  public static SPFileVersion getFileVersion( Connection connection,
    String folderPath, String fileName, String id ) throws SPException {

    FilePath filePath = new FilePath( FilePath.parse( folderPath ), fileName );
    String request = "GetFileByServerRelativeUrl('/"
      + enc( filePath.toString() ) + "')/Versions(" + id + ")";
    String description = "Version " + id + " of file " + fileName
      + " in folder " + folderPath;

    Response response = performGet( connection, API + request, description );
    return Json.parseFileVersion( response.getContent() );

  }


  /**
   * Get file version content
   * 
   * @param connection Sharepoint server connection
   * @param folderPath Relative path of folder
   * @param fileName Name of file
   * @param id File version id
   * @param target Output stream for file content
   * @throws SPException on system error or configuration problem
   * 
   */
  public static void getFileVersionContent( Connection connection,
    String folderPath, String fileName, String id, OutputStream target )
    throws SPException {

    FilePath filePath = new FilePath( FilePath.parse( folderPath ), fileName );
    String request = id + "/" + encElements( filePath ).toString();
    String description = "Content of version " + id + " of file " + fileName
      + " in folder " + folderPath;

    performGetRaw( connection, API_HISTORY + request, target, description );

  }


  /**
   * Get file version content as string
   * 
   * @param connection Sharepoint server connection
   * @param folderPath Relative path of folder
   * @param fileName Name of file
   * @param id File version id
   * @param target Output stream for file content
   * @throws SPException on system error or configuration problem
   * 
   */
  public static String getFileVersionContent( Connection connection,
    String folderPath, String fileName, String id )
      throws SPException {
    
    ByteArrayOutputStream bout = new ByteArrayOutputStream();
    getFileVersionContent( connection, folderPath, fileName, id, bout );
    try {
      return new String( bout.toByteArray(), "UTF-8" );
    }
    catch( UnsupportedEncodingException ex ) {
      throw new RuntimeException( ex );
    }
    
  }
  
  
  /**
   * Delete file version
   * 
   * @param connection Sharepoint server connection
   * @param folderPath Relative path of folder
   * @param fileName Name of file
   * @param id Id of file version
   * @throws SPException on system error or configuration problem
   * 
   */
  public static void deleteFileVersion( Connection connection,
    String folderPath, String fileName, String id ) throws SPException {

    String formDigestValue = SiteOperations.ensureValidDigest( connection );
    FilePath filePath = new FilePath( FilePath.parse( folderPath ), fileName );
    String request = "GetFileByServerRelativeUrl('" + enc( filePath.toString() )
      + "')/Versions/DeleteById(vid=" + id + ")";
    String description = "Version " + id + " of file " + fileName
      + " in folder " + folderPath;

    performPost( connection, API + request, null, null, formDigestValue, null,
      description );

  }


}
