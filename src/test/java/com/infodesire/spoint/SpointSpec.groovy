// (C) 1998-2016 Information Desire Software GmbH
// www.infodesire.com

package com.infodesire.spoint;

import com.google.common.base.Charsets
import com.google.common.base.Strings
import com.google.common.io.Files
import com.infodesire.spoint.base.Config
import com.infodesire.spoint.base.Connection
import com.infodesire.spoint.base.LowLevel
import com.infodesire.spoint.base.Response
import com.infodesire.spoint.base.SpointNotFoundException
import com.infodesire.spoint.model.SPFile
import com.infodesire.spoint.model.SPFileVersion
import com.infodesire.spoint.model.SPFolder
import com.infodesire.spoint.operations.FileOperations
import com.infodesire.spoint.operations.FolderOperations

import groovy.json.JsonSlurper
import spock.lang.Specification


/**
 * Proof of concept and technology playground.
 * 
 *
 */
public class SpointSpec extends Specification {
  
  
  static Properties properties
  static String publicFolder
  static String testFolder
  
  
  def setupSpec() {
    properties = loadProperties();
  }
  
  
  def connectionSpec() {
    
    when:
      Connection connection = getConnection();
      String request = "/_api/web/title";
      if( !Strings.isNullOrEmpty( connection.site ) ) {
        request = "/" + connection.site + request; 
      }
      Response response = LowLevel.performGet( connection, request, null );
    then:
      assert response.statusCode == 200, response.statusLine
    when:
      def json = new JsonSlurper().parseText( response.content )
    then:
      json.d.Title == properties.getProperty( "siteTitle" )

  }
  
  
//  def tests() {
//    
//    setup:
//    
//      Connection connection = getConnection();
//      
//    when:
//    
//      String pretty
//      SPList list
//      List<SPListItem> items
//      List<SPFolder> folders
//      String content
//      SPContextInfo contextInfo
//      File file
//      SPFileVersion fileVersion
//      Response response
//      
//      contextInfo = SiteOperations.getContextInfo( connection );
//      println contextInfo
//    
//      response = LowLevel.performGet( connection, "/_api/web/lists" );
//      pretty = JsonOutput.prettyPrint( response.content );
//      println pretty
//      
//      
//      ListOperations.getLists( connection, "/_api/web" ).each {
//        println it.title
//      }
//      
//      response = LowLevel.performGet( connection, "/_api/web/lists/getbytitle('Dokumente')",  );
//      pretty = JsonOutput.prettyPrint( response.content );
//      
//      println pretty
//
//      list = ListOperations.getListByTitle( connection, "Dokumente" );
//      
//      println list      
//      
//      list = ListOperations.getListById( connection, "0b345821-bfcf-40d9-84e2-e8a914313490" );
//      
//      println list      
//      
//      response = LowLevel.performGet( connection, "/_api/web/lists/getbytitle('Dokumente')/items",  );
//      pretty = JsonOutput.prettyPrint( response.content );
//      println pretty
//      
//      response = LowLevel.performGet( connection, "/_api/web/Lists(guid'82f1f8c3-2445-45db-8ad0-0ddd5fa76edf')/Items(1)",  );
//      pretty = JsonOutput.prettyPrint( response.content );
//      println pretty
//      
//      items = ListOperations.getListItemsByTitle( connection, "Dokumente" );
//      items.each {
//        println it
//      }
//      
//      response = LowLevel.performGet( connection, "/_api/web/folders",  );
//      pretty = JsonOutput.prettyPrint( response.content );
//      println pretty
//      
//      println "## root folders"
//      FolderOperations.getRootFolders( connection, "/_api/web" ).each {
//        println it.relativeUri
//      }
//
//      println "## Freigegebene Dokumente"      
//      FolderOperations.getFolders( connection, "Freigegebene Dokumente" ).each {
//        println it.relativeUri
//      }
//      
//      println "## /Lists"      
//      FolderOperations.getFolders( connection, "Lists" ).each {
//        println it.relativeUri
//      }
//      
//      println "## Get Folder"      
//      println FolderOperations.getFolder( connection, "Freigegebene Dokumente" )
//      
//      println "## Create Folder"      
//      content = "body: { '__metadata': { 'type': 'SP.Folder' }, 'ServerRelativeUrl': '/Freigegebene%20Dokumente/test2'}"
//      println LowLevel.performPost( connection, "/_api/web/folders", content, contextInfo.formDigestValue )
//      
//      println "## Create Folder 2"      
//      content = ""
//      println LowLevel.performPost( connection, "/_api/web/folders/add('/Freigegebene%20Dokumente/test3')", content, contextInfo.formDigestValue )
//      
//
//            
//      println "## Create Folder"      
//      println FolderOperations.createFolder( connection, "Freigegebene Dokumente/test4" )
//      
//      println "## /Freigegebene Dokumente"      
//      FolderOperations.getFolders( connection, "/Freigegebene Dokumente" ).each {
//        println it.relativeUri
//      }
//      
//      println "## Rename Folder"      
//      println FolderOperations.renameFolder( connection, "Freigegebene Dokumente/test4", "test4-x" )
//      
//      println "## /Freigegebene Dokumente"      
//      FolderOperations.getFolders( connection, "/Freigegebene Dokumente" ).each {
//        println it.relativeUri
//      }
//      
//      println "## Delete Folder"      
//      println FolderOperations.deleteFolder( connection, "Freigegebene Dokumente/test4" )
//      
//      println "## /Freigegebene Dokumente"      
//      FolderOperations.getFolders( connection, "/Freigegebene Dokumente" ).each {
//        println it.relativeUri
//      }
//  
//  
//      println "## Get Files /Freigegebene Dokumente"
//      FolderOperations.getFiles( connection, "/Freigegebene Dokumente" ).each { println it }
//  
//      println "## Get FileContent /Freigegebene Dokumente/LICENSE.TXT"
//      file = File.createTempFile( "LICENSE.TXT", "" );
//      file.deleteOnExit();
//      FileOutputStream fout = new FileOutputStream( file );
//      FolderOperations.getFileContent( connection, "/Freigegebene Dokumente", "LICENSE.TXT", fout )
//      println Files.toString( file, Charsets.UTF_8 );
//      file.delete();
//  
//      println "## Upload File /Freigegebene Dokumente/testX"
//      file = createTempFile( "test4.txt", "test4 " + new Date() );
//      FileInputStream fin = new FileInputStream( file );
//      FolderOperations.uploadFile( connection, "/Freigegebene Dokumente", file.getName(), fin );
//  
//      println "## Upload Large File /Freigegebene Dokumente/testX"
//      File bigfile = new File( "temp/bigfile" );
//      fin = new FileInputStream( bigfile );
//      FolderOperations.uploadFile( connection, "/Freigegebene Dokumente", bigfile.getName(), fin );
//      
//      println "## Get Files /Freigegebene Dokumente"
//      FolderOperations.getFiles( connection, "/Freigegebene Dokumente" ).each {
//        println it
//      }
//
//      println "## Delete File /Freigegebene Dokumente/testX"
//      FolderOperations.deleteFile( connection, "/Freigegebene Dokumente", file.getName() );
//      
//      println "## Get Files /Freigegebene Dokumente"
//      FolderOperations.getFiles( connection, "/Freigegebene Dokumente" ).each {
//        println it
//      }
//      
//      println "## Check out File /Freigegebene Dokumente/LICENSE.TXT"
//      FolderOperations.checkOutFile( connection, "/Freigegebene Dokumente", "LICENSE.TXT" );
//      
//      println "## Check in File /Freigegebene Dokumente/LICENSE.TXT"
//      FolderOperations.checkInFile( connection, "/Freigegebene Dokumente", "LICENSE.TXT", "all is better now", CheckinType.MinorCheckIn );
//      
//      println "## Get Files /Freigegebene Dokumente"
//      FolderOperations.getFiles( connection, "/Freigegebene Dokumente" ).each {
//        println it
//      }
//      
//      println "## Get FileVersions /Freigegebene Dokumente/LICENSE.TXT"
//      FolderOperations.getFileVersions( connection, "/Freigegebene Dokumente", "LICENSE.TXT" ).each {
//        println it.id
//      }
//      
//      println "## Get FileVersion /Freigegebene Dokumente/LICENSE.TXT 512"
//      println FolderOperations.getFileVersion( connection, "/Freigegebene Dokumente", "LICENSE.TXT", 512 )
//      
//      println "## Get FileVersion Content /Freigegebene Dokumente/LICENSE.TXT 1536"
//      file = File.createTempFile( "LICENSE-1536.TXT", "" );
//      file.deleteOnExit();
//      fout = new FileOutputStream( file );
//      FolderOperations.getFileVersionContent( connection, "Freigegebene Dokumente", "LICENSE.TXT", 1536, fout )
//      println Files.toString( file, Charsets.UTF_8 );
//      file.delete();
//
//      println "## Delete FileVersion /Freigegebene Dokumente/LICENSE.TXT 1536"
//      FolderOperations.deleteFileVersion( connection, "/Freigegebene Dokumente", "LICENSE.TXT", 1536 );
//      
//      println "## Get FileVersions /Freigegebene Dokumente/LICENSE.TXT"
//      FolderOperations.getFileVersions( connection, "/Freigegebene Dokumente", "LICENSE.TXT" ).each {
//        println it.id
//      }
//
//    then:
//    
//      true
//      
//  }
  
  
  def folderSpec() {
    
    setup:
    
      Connection connection = getConnection()
      ensureTestFolderExists( connection )
    
    when: "create folder"
    
      String newFolder = testFolder + "/new";
      FolderOperations.createFolder( connection, newFolder );
    
    then:
    
      FolderOperations.getFolder( connection, newFolder ).name == "new"
      FolderOperations.getFolders( connection, testFolder ).any { folder ->
        folder.name == "new"
      }
      
    when: "delete folder"
    
      FolderOperations.deleteFolder( connection, newFolder );

    then:
        
      try {
        FolderOperations.getFolder( connection, newFolder ).name
        assert false : "Folder was not deleted"
      }
      catch( SpointNotFoundException ex ) {}
      
      !FolderOperations.getFolders( connection, testFolder ).any { folder ->
        folder.name == "new2"
      }
          
      // TODO renaming folders is not working yet
      
//    when: "rename folder"
//    
//      FolderOperations.createFolder( connection, newFolder );
//      String newFolderName2 = "new2"
//      String newFolder2 = newFolder + "2"
//      FolderOperations.renameFolder( connection, newFolder, newFolderName2 )
//      
//    then:
//     
//      try {
//        FolderOperations.getFolder( connection, newFolder2 )
//        assert false : "Folder was not renamed"
//      }
//      catch( SPNotFoundException ex ) {}
//      
//      !FolderOperations.getFolders( connection, testFolder ).any { folder ->
//        folder.name == "new"
//      }
//      
//      FolderOperations.getFolder( connection, newFolder ).name == "new2"
//      FolderOperations.getFolders( connection, testFolder ).any { folder ->
//      folder.name == "new2"
//      }
      
  }
  
  
  def filesSpec() {
    
    setup:
    
      Connection connection = getConnection()
      ensureTestFolderExists( connection )
      FileInputStream fin
      
      try {
        FileOperations.deleteFile( connection, testFolder, "hello.txt" )
      }
      catch( SpointNotFoundException ex ) {}
      
    when: "create file"
    
      File file = createTempFile( "hello.txt", "Hello World!" );
      fin = new FileInputStream( file );
      
      FileOperations.uploadFile( connection, testFolder, file.getName(), fin );
    
    then:
    
      FolderOperations.getFiles( connection, testFolder ).any { f ->
        f.name == file.getName()
      }
      
    when: "upload new versions"
    
      file.delete();
      file = createTempFile( "hello.txt", "Hello World 2!" );
      fin = new FileInputStream( file );
      FileOperations.uploadFile( connection, testFolder, file.getName(), fin );
      
      file.delete();
      file = createTempFile( "hello.txt", "Hello World 3!" );
      fin = new FileInputStream( file );
      FileOperations.uploadFile( connection, testFolder, file.getName(), fin );
      
      file.delete();
      file = createTempFile( "hello.txt", "Hello World 4!" );
      fin = new FileInputStream( file );
      FileOperations.uploadFile( connection, testFolder, file.getName(), fin );
      
      List<SPFileVersion> versions = FileOperations.getFileVersions( connection, testFolder, file.getName() )
      
    then:
    
      // because the current version will not be listed here!
      versions.size() == 3
    
      "Hello World!" == FileOperations.getFileVersionContent( connection, testFolder, file.getName(), versions.get( 0 ).id )
      "Hello World 2!" == FileOperations.getFileVersionContent( connection, testFolder, file.getName(), versions.get( 1 ).id )
      "Hello World 3!" == FileOperations.getFileVersionContent( connection, testFolder, file.getName(), versions.get( 2 ).id )
      
      // current version:
      "Hello World 4!" == FileOperations.getFileContent( connection, testFolder, file.getName() );
      
     when: "delete file version"
     
       FileOperations.deleteFileVersion( connection, testFolder, file.getName(), versions.get( 1 ).id )
     
       versions = FileOperations.getFileVersions( connection, testFolder, file.getName() )
       
     then:
     
       versions.size() == 2
     
       "Hello World!" == FileOperations.getFileVersionContent( connection, testFolder, file.getName(), versions.get( 0 ).id )
       "Hello World 3!" == FileOperations.getFileVersionContent( connection, testFolder, file.getName(), versions.get( 1 ).id )
       "Hello World 4!" == FileOperations.getFileContent( connection, testFolder, file.getName() );
       
     when: "delete file"
     
       FileOperations.deleteFile( connection, testFolder, file.getName() );
       
       try {
         FileOperations.getFile( connection, testFolder, file.getName() );
         assert false : "File still exists, but should not"
       }
       catch( SpointNotFoundException ex ) {}
       
     then:
     
       true
     
  }
  
  
  private File createTempFile( String name, String content ) {
    File file = new File( "temp/" + name );
    file.getParentFile().mkdirs();
    file.delete();
    Files.write( content, file, Charsets.UTF_8 );
    return file;
  }
  

  private Properties loadProperties() {

    // keep secrets in a separate file, not here in source code
    File file = new File( System.getProperty( "user.home" ) + "/SHAREPOINT.TXT" );
    if( !file.exists() ) {
      usageSecrets( file );
      return null;
    }
    
    Properties properties = new Properties();
    properties.load( new FileReader( file ) );

    publicFolder = properties.getProperty( "publicFolder" );
    if( Strings.isNullOrEmpty( publicFolder ) ) {
      usageSecrets( file );
      return null;
    }
    
    testFolder = properties.getProperty( "testFolder" );
    if( Strings.isNullOrEmpty( testFolder ) ) {
      usageSecrets( file );
      return null;
    }
    
    return properties;
    
  }
  

  private void usageSecrets( File file ) {
    
    println "Please create a file " + file.getAbsolutePath() + " containing" +
      " the connection information to the sharepoint server";
      
    println '''For example:
--------------------------------------------------------------------------------------------
url=https://sharepoint.server.com/path_to_resource
user=username
password=password

# leave empty for default site
site=

# public folder
publicFolder=/Freigegebene Dokumente

# everything below is for tests only ---------------------------------------------------------------

# title of sharepoint web site (will be tested)
siteTitle=Test

# a folder where all the tests will be performed in (no data in this folder is safe)
# (we use a space character in folder name, to test this feature)
testFolder=/Freigegebene Dokumente/play ground
--------------------------------------------------------------------------------------------
'''
    
  }
  
  
  private Connection getConnection() {
    
    Config config = new Config(
      url: properties.getProperty( "url" ),
      user: properties.getProperty( "user" ),
      password: properties.getProperty( "password" )
    );
  
    return LowLevel.connect( config );
    
  }
  
  
  private void ensureTestFolderExists( Connection connection ) {
    
    try {
      FolderOperations.getFolder( connection, testFolder )
    }
    catch( SpointNotFoundException ex ) {
      FolderOperations.createFolder( connection, testFolder )
      FolderOperations.getFolder( connection, testFolder )
    }
    
  }
  
  
}


