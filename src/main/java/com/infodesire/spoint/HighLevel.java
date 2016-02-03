// (C) 1998-2016 Information Desire Software GmbH
// www.infodesire.com

package com.infodesire.spoint;

import static com.infodesire.spoint.SPCode.HTTP_ERROR;

import com.infodesire.spoint.model.Json;
import com.infodesire.spoint.model.SPList;
import com.infodesire.spoint.utils.FilePath;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;



public class HighLevel {
  
  
  public static List<SPList> getLists( Connection connection, String pathName ) throws SPException {

    FilePath path = FilePath.parse( pathName );
    if( !path.getLast().equals( "lists" ) ) {
      path = new FilePath( path, "lists" );
    }
    
    try {
      
      Response response = LowLevel.performGet( connection, path.toString() );
      if( !response.isOk() ) {
        throw new SPException( HTTP_ERROR, response, null, null );
      }
      
      return Json.parseLists( response.getContent() );
      
    }
    catch( ClientProtocolException ex ) {
      throw new SPException( HTTP_ERROR, null, ex, "Client protocol error" );
    }
    catch( IOException ex ) {
      throw new SPException( HTTP_ERROR, null, ex, "Cannot read response text" );
    }

  }
  
  
//  public static SPList getListByTitle( Connection connection, String title )
//    throws SPException {
//    
//    FilePath path = FilePath.parse( pathName );
//    if( !path.getLast().equals( "lists" ) ) {
//      path = new FilePath( path, "lists" );
//    }
//
//
//  }
  

}
