// (C) 1998-2016 Information Desire Software GmbH
// www.infodesire.com

package com.infodesire.spoint.base;

import static com.infodesire.spoint.base.SPCode.HTTP_ERROR;

import com.infodesire.spoint.utils.FilePath;
import com.infodesire.spoint.utils.SpointUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.http.client.ClientProtocolException;


/**
 * Base class with utils for all operations
 *
 */
public class OperationsBase {
  
  
  /**
   * Perform REST GET request to sharepoint, handle problems and return response
   * 
   * @param connection Sharepoint connection
   * @param relativeSiteUri Relative site uri
   * @param requestString Request string
   * @return Response from server
   * @throws SPException when any kind of problem occured. Exception contains details.
   */
  public static Response performGet( Connection connection,
    String relativeSiteUri, String requestString ) throws SPException {

    FilePath path = FilePath.parse( relativeSiteUri );
    path = new FilePath( path, requestString );

    try {
      
      Response response = LowLevel.performGet( connection, path.toString(), null );
      if( !response.isOk() ) {
        throw new SPException( HTTP_ERROR, response, null, null );
      }
      
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
   * @param relativeSiteUri Relative site uri
   * @param requestString Request string
   * @param target Output stream for reply data
   * @throws SPException when any kind of problem occured. Exception contains details.
   */
  public static void performGetRaw( Connection connection,
    String relativeSiteUri, String requestString, OutputStream target ) throws SPException {
    
    FilePath path = FilePath.parse( relativeSiteUri );
    path = new FilePath( path, requestString );
    
    try {
      
      LowLevel.performGet( connection, path.toString(), target );
      
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
   * @param relativeSiteUri Relative site uri
   * @param requestString Request string
   * @param contentAsString Request body as string
   * @param contentAsStream Request body as stream
   * @param formDigestValue Auth value necessary for all writing operations. Can be retrieved via SiteOperations.getContextInfo. 
   * @param xHttpMethod Header value for X-HTTP-Method
   * @return Response from server
   * @throws SPException when any kind of problem occured. Exception contains details.
   * 
   */
  public static Response performPost( Connection connection,
    String relativeSiteUri, String requestString, String contentAsString,
    InputStream contentAsStream, String formDigestValue, String xHttpMethod )
    throws SPException {
    
    FilePath path = FilePath.parse( relativeSiteUri );
    path = new FilePath( path, requestString );
    
    try {
      
      Response response = LowLevel.performPost( connection, path.toString(),
        contentAsString, contentAsStream, formDigestValue, xHttpMethod );
      if( !response.isOk() ) {
        throw new SPException( HTTP_ERROR, response, null, null );
      }
      
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
  

}

