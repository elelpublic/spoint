// (C) 1998-2016 Information Desire Software GmbH
// www.infodesire.com

package com.infodesire.spoint;

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
    
      response = LowLevel.performGet( connection, "/_api/web/lists" );
      String pretty = JsonOutput.prettyPrint( response.content );
      
      HighLevel.getLists( connection, "/_api/web" ).each {
        println it.title
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
