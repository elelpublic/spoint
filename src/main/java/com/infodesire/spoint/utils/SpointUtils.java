// (C) 1998-2016 Information Desire Software GmbH
// www.infodesire.com

package com.infodesire.spoint.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


/**
 * Common utils
 *
 */
public class SpointUtils {
  
  
  /**
   * Encode url parameter
   * 
   * @param parameter Parameter raw string
   * @return Url encoded parameter
   */
  public static String encodeForUrl( String parameter ) {
    try {
      return URLEncoder.encode( parameter, "UTF-8" ).replaceAll( "\\+", "%20" );
    }
    catch( UnsupportedEncodingException ex ) {
      throw new RuntimeException( ex );
    }
  }

  
  /**
   * Anything considered OK in the HTTP protocol
   * 
   * @param statusCode HTTP status code
   * @return Is the code considered 'OK'
   * 
   */
  public static boolean isHttpOk( int statusCode ) {
    return statusCode >= 200 && statusCode < 300;
  }
  

}
