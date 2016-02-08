// (C) 1998-2016 Information Desire Software GmbH
// www.infodesire.com

package com.infodesire.spoint.base;

import static com.infodesire.spoint.base.SPCode.*;

import com.google.common.base.Strings;
import com.infodesire.spoint.utils.FilePath;
import com.infodesire.spoint.utils.SpointUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;


/**
 * Base class with utils for all operations
 *
 */
public class OperationsBase {
  
  
  /** Standard URI for API calls */
  public static final String API = "/_api/web/";


  /** Standard URI for accessing file history */
  public static final String API_HISTORY = "/_vti_history/";


  /**
   * Perform REST GET request to sharepoint, handle problems and return response
   * 
   * @param connection Sharepoint connection
   * @param requestString Request string
   * @param entityDescription Describes the entity (usefull for logging and error messages)
   * @return Response from server
   * @throws SPException when any kind of problem occured. Exception contains details.
   */
  public static Response performGet( Connection connection,
    String requestString, String entityDescription ) throws SPException {

    FilePath path = new FilePath( true );
    if( !Strings.isNullOrEmpty( connection.getSite() ) ) {
      path = FilePath.parse( connection.getSite() );
    } 
      
    path = new FilePath( path, requestString );

    try {
      
      Response response = LowLevel.performGet( connection, path.toString(), null );
      handleHttpProblems( response, entityDescription );
      return response;
      
    }
    catch( ClientProtocolException ex ) {
      throw new SPException( HTTP_ERROR, null, ex, "Client protocol error" );
    }
    catch( IOException ex ) {
      throw new SPException( HTTP_ERROR, null, ex, "Cannot read response text" );
    }

  }


  /**
   * Perform REST GET request to sharepoint, handle problems and stream response
   * 
   * @param connection Sharepoint connection
   * @param requestString Request string
   * @param target Output stream for reply data
   * @param entityDescription Describes the entity (usefull for logging and error messages)
   * @throws SPException when any kind of problem occured. Exception contains details.
   * 
   */
  public static void performGetRaw( Connection connection,
    String requestString, OutputStream target, String entityDescription ) throws SPException {
    
    FilePath path = new FilePath( true );
    if( !Strings.isNullOrEmpty( connection.getSite() ) ) {
      path = FilePath.parse( connection.getSite() );
    }
    
    path = new FilePath( path, requestString );
    
    try {
      
      Response response = LowLevel.performGet( connection, path.toString(), target );
      handleHttpProblems( response, entityDescription );
      
    }
    catch( ClientProtocolException ex ) {
      throw new SPException( HTTP_ERROR, null, ex, "Client protocol error" );
    }
    catch( IOException ex ) {
      throw new SPException( HTTP_ERROR, null, ex, "Cannot read response text" );
    }
    
  }
  
  
  /**
   * Perform REST POST request to sharepoint, handle problems and return response
   * 
   * @param connection Sharepoint connection
   * @param requestString Request string
   * @param contentAsString Request body as string
   * @param contentAsStream Request body as stream
   * @param formDigestValue Auth value necessary for all writing operations. Can be retrieved via SiteOperations.getContextInfo. 
   * @param xHttpMethod Header value for X-HTTP-Method
   * @param entityDescription Describes the entity (usefull for logging and error messages)
   * @return Response from server
   * @throws SPException when any kind of problem occured. Exception contains details.
   * 
   */
  public static Response performPost( Connection connection,
    String requestString, String contentAsString, InputStream contentAsStream,
    String formDigestValue, String xHttpMethod, String entityDescription ) throws SPException {
    
    FilePath path = new FilePath( true );
    if( !Strings.isNullOrEmpty( connection.getSite() ) ) {
      path = FilePath.parse( connection.getSite() );
    }
    
    path = new FilePath( path, requestString );
    
    try {
      
      Response response = LowLevel.performPost( connection, path.toString(),
        contentAsString, contentAsStream, formDigestValue, xHttpMethod );
      handleHttpProblems( response, entityDescription );
      
      return response;
      
    }
    catch( ClientProtocolException ex ) {
      throw new SPException( HTTP_ERROR, null, ex, "Client protocol error" );
    }
    catch( IOException ex ) {
      throw new SPException( HTTP_ERROR, null, ex, "Cannot read response text" );
    }
    
  }
  
  
  /**
   * @param parameter Parameter to be used in a url
   * @return URL encoded parameter
   * 
   */
  public static String enc( String parameter ) {
    return SpointUtils.encodeForUrl( parameter );
  }
  

  /**
   * Encode each element of a file path
   * 
   * @param parameter Parameter to be used in a url
   * @return URL encoded parameter
   * 
   */
  public static FilePath encElements( FilePath path ) {
    boolean relative = path.isRelative();
    List<String> elements = new ArrayList<String>();
    for( String element : path ) {
      elements.add( enc( element ) );
    }
    return new FilePath( relative, elements );
  }
  
  
  /**
   * Do the http error checking
   * 
   * @param response Response from server
   * @param entityDescription 
   * @throws SPException If HTTP problems occur
   * @throws SPNotFoundException If the requestst entity was not found
   * 
   */
  private static void handleHttpProblems( Response response, String entityDescription  ) throws SPException, SPNotFoundException {

    int code = response.getStatusCode();
    if( !SpointUtils.isHttpOk( code ) ) {
      if( code == 404 ) {
        throw new SPNotFoundException( response, entityDescription, null,
          response.getContent() );
      }
      else {
        throw new SPException( HTTP_ERROR, response, null, entityDescription
          + " " + response.getContent() );
      }
    }
    
  }


}

