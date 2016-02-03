// (C) 1998-2016 Information Desire Software GmbH
// www.infodesire.com

package com.infodesire.spoint;

import com.infodesire.spoint.model.SPList
import com.infodesire.spoint.model.SPListItem

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
    
//      response = LowLevel.performGet( connection, "/_api/web/lists" );
//      pretty = JsonOutput.prettyPrint( response.content );
      
      HighLevel.getLists( connection, "/_api/web" ).each {
        //println it.title
      }
      
      response = LowLevel.performGet( connection, "/_api/web/lists/getbytitle('Dokumente')",  );
      pretty = JsonOutput.prettyPrint( response.content );
      
      //println pretty

      list = HighLevel.getListByTitle( connection, "/_api/web", "Dokumente" );
      
      //println list      
      
      list = HighLevel.getListById( connection, "/_api/web", "0b345821-bfcf-40d9-84e2-e8a914313490" );
      
      //println list      
      
      response = LowLevel.performGet( connection, "/_api/web/lists/getbytitle('Dokumente')/items",  );
      pretty = JsonOutput.prettyPrint( response.content );
//      println pretty
      
      response = LowLevel.performGet( connection, "/_api/web/Lists(guid'82f1f8c3-2445-45db-8ad0-0ddd5fa76edf')/Items(1)",  );
      pretty = JsonOutput.prettyPrint( response.content );
//      println pretty
      
      items = HighLevel.getListItemsByTitle( connection, "/_api/web", "Dokumente" );
      items.each {
        println it
      }
      
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
