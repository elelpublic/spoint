// (C) 1998-2016 Information Desire Software GmbH
// www.infodesire.com

package com.infodesire.spoint.operations;

import com.infodesire.spoint.base.Connection;
import com.infodesire.spoint.base.OperationsBase;
import com.infodesire.spoint.base.Response;
import com.infodesire.spoint.base.SPException;
import com.infodesire.spoint.model.Json;
import com.infodesire.spoint.model.SPContextInfo;


/**
 * Operations on sharepoint sites
 *
 */
public class SiteOperations extends OperationsBase {
  

  /**
   * Get Context info. Context info contains RequestDigest value. 
   * This value is needed for all CREATE/UPDATE/DELETE operations
   * 
   * @param connection Sharepoint server connection
   * @return Request digest value
   * @throws SPException on system error or configuration problem
   * 
   */
  public static SPContextInfo getContextInfo( Connection connection ) throws SPException {
    
    Response response = performPost( connection, "/_api", "contextinfo", "",
      null, null, "POST" );
    return Json.parseContextInfo( response.getContent() );
    
  }
  
  
  /**
   * Make sure there is a valid FormDigestValue available with
   * enough (10s) remaining lifetime.
   * 
   * @param connection Current 
   * @throws SPException on system error or configuration problem
   */
  public static String ensureValidDigest( Connection connection ) throws SPException {
    
    SPContextInfo contextInfo = connection.getContextInfo();
    
    if( contextInfo != null ) {
      long now = System.currentTimeMillis();
      if( ( now - contextInfo.getClientTimestamp() ) > ( ( contextInfo
        .getFormDigestTimeoutSeconds() - 10 ) * 1000 ) ) {
        contextInfo = null; // was too old
      }
    }
    
    if( contextInfo == null ) {
      contextInfo = getContextInfo( connection );
      connection.setContextInfo( contextInfo );
    }
    
    return contextInfo.getFormDigestValue();
    
  }
  
  
}


