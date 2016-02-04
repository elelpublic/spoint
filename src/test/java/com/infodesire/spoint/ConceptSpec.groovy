// (C) 1998-2016 Information Desire Software GmbH
// www.infodesire.com

package com.infodesire.spoint;

import com.infodesire.spoint.base.Config
import com.infodesire.spoint.base.Connection
import com.infodesire.spoint.base.LowLevel
import com.infodesire.spoint.base.Response
import com.infodesire.spoint.model.SPFolder
import com.infodesire.spoint.model.SPList
import com.infodesire.spoint.model.SPListItem
import com.infodesire.spoint.operations.FolderOperations
import com.infodesire.spoint.operations.ListOperations

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
      Response response = LowLevel.performGet( connection, "/_api/web/title" );
      
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
    
//      response = LowLevel.performGet( connection, "/_api/web/lists" );
//      pretty = JsonOutput.prettyPrint( response.content );
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
      println "## root folders"
      FolderOperations.getRootFolders( connection, "/_api/web" ).each {
        println it.relativeUri
      }

      println "## Freigegebene Dokumente"      
      FolderOperations.getFolders( connection, "/_api/web", "Freigegebene Dokumente" ).each {
        println it.relativeUri
      }
      
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
//      println LowLevel.performPost( connection, "/_api/web/folders", content )
      
      println "## Create Folder 2"      
      content = ""
      println LowLevel.performPost( connection, "/_api/web/folders/add('/Freigegebene%20Dokumente/test3')", content )
      
      println "## /Freigegebene Dokumente"      
      FolderOperations.getFolders( connection, "/_api/web", "/Freigegebene Dokumente" ).each {
        println it.relativeUri
      }
      
//      println "## Create Folder"      
//      println FolderOperations.createFolder( connection, "/_api/web", "Freigegebene Dokumente", "test2" )
      
    then:
    
      true
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
