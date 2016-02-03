// (C) 1998-2016 Information Desire Software GmbH
// www.infodesire.com

package com.infodesire.spoint.model;

import com.infodesire.spoint.SPCode
import com.infodesire.spoint.SPException

import groovy.json.JsonException
import groovy.json.JsonSlurper


/**
 * Parse and format model object in JSON
 *
 */
public class Json {

  public static List<SPList> parseLists( String content ) throws SPException {
    
    try {
      def json = new JsonSlurper().parseText( content );
      json['d']['results'].collect {
        def metadata = it['__metadata'];
        return new SPList(
          id: it['Id'],
          uri: metadata['uri'],
          title: it['Title'],
          description: it['Description']
        );
      }
    }
    catch( JsonException ex ) {
      throw new SPException( SPCode.JSON_ERROR, null, ex, null ); 
    }
    
  }

}
