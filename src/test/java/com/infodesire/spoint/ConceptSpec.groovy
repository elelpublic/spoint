// (C) 1998-2016 Information Desire Software GmbH
// www.infodesire.com

package com.infodesire.spoint;

import com.google.common.base.Charsets
import com.google.common.io.Files
import com.infodesire.spoint.base.Config
import com.infodesire.spoint.base.Connection
import com.infodesire.spoint.base.LowLevel
import com.infodesire.spoint.base.Response
import com.infodesire.spoint.model.SPContextInfo
import com.infodesire.spoint.model.SPFileVersion
import com.infodesire.spoint.model.SPFolder
import com.infodesire.spoint.model.SPList
import com.infodesire.spoint.model.SPListItem
import com.infodesire.spoint.operations.FolderOperations
import com.infodesire.spoint.operations.SiteOperations

import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import spock.lang.Specification


/**
 * Proof of concept and technology playground.
 * 
 *
 */
public class ConceptSpec extends Specification {
  
  
  
  
  def tests() {
    
    // again with new API
    
    when:
    
      Properties properties = loadProperties();
      
      Config config = new Config( 
        url: properties.getProperty( "url" ),
        user: properties.getProperty( "user" ),
        password: properties.getProperty( "password" )
      );
    
      Connection connection = LowLevel.connect( config );
      Response response = LowLevel.performGet( connection, "/_api/web/title", null );
      
    then:
    
      assert response.statusCode == 200, response.statusLine
      
    when:
    
      def json = new JsonSlurper().parseText( response.content )
      
    then:
    
      json.d.Title == 'Test'
      
    when:
    
      String pretty
      SPList list
      List<SPListItem> items
      List<SPFolder> folders
      String content
      SPContextInfo contextInfo
      File file
      SPFileVersion fileVersion
      
      contextInfo = SiteOperations.getContextInfo( connection );
      println contextInfo
    
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
//      list = ListOperations.getListByTitle( connection, "/_api/web", "Dokumente" );
//      
//      println list      
//      
//      list = ListOperations.getListById( connection, "/_api/web", "0b345821-bfcf-40d9-84e2-e8a914313490" );
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
//      items = ListOperations.getListItemsByTitle( connection, "/_api/web", "Dokumente" );
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
//      FolderOperations.getFolders( connection, "/_api/web", "Freigegebene Dokumente" ).each {
//        println it.relativeUri
//      }
//      
//      println "## /Lists"      
//      FolderOperations.getFolders( connection, "/_api/web", "Lists" ).each {
//        println it.relativeUri
//      }
//      
//      println "## Get Folder"      
//      println FolderOperations.getFolder( connection, "/_api/web", "Freigegebene Dokumente" )
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
//      println FolderOperations.createFolder( connection, "/_api/web", "Freigegebene Dokumente/test4" )
//      
//      println "## /Freigegebene Dokumente"      
//      FolderOperations.getFolders( connection, "/_api/web", "/Freigegebene Dokumente" ).each {
//        println it.relativeUri
//      }
//      
//      println "## Rename Folder"      
//      println FolderOperations.renameFolder( connection, "/_api/web", "Freigegebene Dokumente/test4", "test4-x" )
//      
//      println "## /Freigegebene Dokumente"      
//      FolderOperations.getFolders( connection, "/_api/web", "/Freigegebene Dokumente" ).each {
//        println it.relativeUri
//      }
//      
//      println "## Delete Folder"      
//      println FolderOperations.deleteFolder( connection, "/_api/web", "Freigegebene Dokumente/test4" )
//      
//      println "## /Freigegebene Dokumente"      
//      FolderOperations.getFolders( connection, "/_api/web", "/Freigegebene Dokumente" ).each {
//        println it.relativeUri
//      }
//  
//  
//      println "## Get Files /Freigegebene Dokumente"
//      FolderOperations.getFiles( connection, "/_api/web", "/Freigegebene Dokumente" ).each { println it }
//  
      println "## Get FileContent /Freigegebene Dokumente/LICENSE.TXT"
      file = File.createTempFile( "LICENSE.TXT", "" );
      file.deleteOnExit();
      FileOutputStream fout = new FileOutputStream( file );
      FolderOperations.getFileContent( connection, "/_api/web", "/Freigegebene Dokumente", "LICENSE.TXT", fout )
      println Files.toString( file, Charsets.UTF_8 );
      file.delete();
//  
//      println "## Upload File /Freigegebene Dokumente/testX"
//      file = createTempFile( "test4.txt", "test4 " + new Date() );
//      FileInputStream fin = new FileInputStream( file );
//      FolderOperations.uploadFile( connection, "/_api/web", "/Freigegebene Dokumente", file.getName(), fin );
//  
//      println "## Upload Large File /Freigegebene Dokumente/testX"
//      File bigfile = new File( "temp/bigfile" );
//      fin = new FileInputStream( bigfile );
//      FolderOperations.uploadFile( connection, "/_api/web", "/Freigegebene Dokumente", bigfile.getName(), fin );
//      
//      println "## Get Files /Freigegebene Dokumente"
//      FolderOperations.getFiles( connection, "/_api/web", "/Freigegebene Dokumente" ).each {
//        println it
//      }
//
//      println "## Delete File /Freigegebene Dokumente/testX"
//      FolderOperations.deleteFile( connection, "/_api/web", "/Freigegebene Dokumente", file.getName() );
//      
//      println "## Get Files /Freigegebene Dokumente"
//      FolderOperations.getFiles( connection, "/_api/web", "/Freigegebene Dokumente" ).each {
//        println it
//      }
//      
//      println "## Check out File /Freigegebene Dokumente/LICENSE.TXT"
//      FolderOperations.checkOutFile( connection, "/_api/web", "/Freigegebene Dokumente", "LICENSE.TXT" );
//      
//      println "## Check in File /Freigegebene Dokumente/LICENSE.TXT"
//      FolderOperations.checkInFile( connection, "/_api/web", "/Freigegebene Dokumente", "LICENSE.TXT", "all is better now", CheckinType.MinorCheckIn );
//      
//      println "## Get Files /Freigegebene Dokumente"
//      FolderOperations.getFiles( connection, "/_api/web", "/Freigegebene Dokumente" ).each {
//        println it
//      }
//      
//      println "## Get FileVersions /Freigegebene Dokumente/LICENSE.TXT"
//      FolderOperations.getFileVersions( connection, "/_api/web", "/Freigegebene Dokumente", "LICENSE.TXT" ).each {
//        println it.id
//      }
//      
//      println "## Get FileVersion /Freigegebene Dokumente/LICENSE.TXT 512"
//      println FolderOperations.getFileVersion( connection, "/_api/web", "/Freigegebene Dokumente", "LICENSE.TXT", 512 )
//      
//      println "## Get FileVersion Content /Freigegebene Dokumente/LICENSE.TXT 1536"
//      file = File.createTempFile( "LICENSE-1536.TXT", "" );
//      file.deleteOnExit();
//      FileOutputStream fout = new FileOutputStream( file );
//      FolderOperations.getFileVersionContent( connection, "/_api/web", "Freigegebene Dokumente", "LICENSE.TXT", 1536, fout )
//      println Files.toString( file, Charsets.UTF_8 );
//      file.delete();
//
//      println "## Delete FileVersion /Freigegebene Dokumente/LICENSE.TXT 1536"
//      FolderOperations.deleteFileVersion( connection, "/_api/web", "/Freigegebene Dokumente", "LICENSE.TXT", 1536 );
//      
//      println "## Get FileVersions /Freigegebene Dokumente/LICENSE.TXT"
//      FolderOperations.getFileVersions( connection, "/_api/web", "/Freigegebene Dokumente", "LICENSE.TXT" ).each {
//        println it.id
//      }

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
    return properties;
    
  }
  

  private void usageSecrets( File file ) {
    
    println "Please create a file " + file.getAbsolutePath() + " containing" +
      " the connection information to the sharepoint server";
      
    println '''For example:
url=https://sharepoint.server.com/path_to_resource
user=username
password=password'''
    
  }
  
  
}
