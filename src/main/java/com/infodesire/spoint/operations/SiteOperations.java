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
    
    Response response = performPost( connection, "/_api", "contextinfo", "", null, "POST" );
    return Json.parseContextInfo( response.getContent() );
    
  }
  
  
}


