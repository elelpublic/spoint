// (C) 1998-2016 Information Desire Software GmbH
// www.infodesire.com

package com.infodesire.spoint.base;

import com.google.common.io.ByteStreams;
import com.infodesire.spoint.utils.SpointUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.NTCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HttpContext;


/**
 * API for MS Sharepoint
 *
 */
public class LowLevel {
  
  
  public static Connection connect( Config config ) throws MalformedURLException {
    
    Connection connection = new Connection( config );
  
    CredentialsProvider credsProvider = new BasicCredentialsProvider();
    credsProvider.setCredentials(AuthScope.ANY,
      new NTCredentials( config.getUser(), config.getPassword(), config
        .getWorkstation(), config.getDomain() ) );
  
    RequestConfig.Builder requestBuilder = RequestConfig.custom();
    requestBuilder = requestBuilder.setConnectTimeout( config
      .getConnectionTimeoutMs() );
    requestBuilder = requestBuilder.setConnectionRequestTimeout( config
      .getRequestTimeoutMs() );
    
    HttpHost target = new HttpHost( connection.getHost(), connection.getPort(),
      connection.getProtocol() );
    connection.setHttpHost( target );
    
    HttpClientBuilder builder = HttpClientBuilder.create(); 
    builder.setDefaultRequestConfig( requestBuilder.build() );
    
    if( config.getUseJson() ) {
      builder.addInterceptorFirst( new HttpRequestInterceptor() {
        public void process( HttpRequest request, HttpContext context ) throws HttpException, IOException {
          request.setHeader( "Accept", "application/json;odata=verbose" );
        }
      });
    }
    
    HttpClient httpClient = builder.build();
    connection.setHttpClient( httpClient );
    
    // Make sure the same context is used to execute logically related requests
    HttpClientContext context = HttpClientContext.create();
    context.setCredentialsProvider(credsProvider);
    
    connection.setContext( context );
    
    return connection;

  }
  
  
  /**
   * Send GET request to server
   * 
   * @param connection Connection
   * @param path Request uri
   * @param target Optional output stream for reply data
   * @return Response from server
   * @throws ClientProtocolException Http problem
   * @throws IOException Http communication problem
   * 
   */
  public static Response performGet( Connection connection, String path,
    OutputStream target ) throws ClientProtocolException, IOException {
    
    HttpClient httpClient = connection.getHttpClient();
    HttpHost httpHost = connection.getHttpHost();
    HttpContext context = connection.getContext();
    
    HttpGet httpGet = new HttpGet( path );
    CloseableHttpResponse httpResponse = null;
    Response response = new Response();
    InputStream data = null;
    
    try {
      
      httpResponse = (CloseableHttpResponse) httpClient.execute( httpHost, httpGet, context );
      response.setStatusLine( httpResponse.getStatusLine() );
      response.setStatusCode( response.getStatusLine().getStatusCode() );
    
      HttpEntity entity = httpResponse.getEntity();
      data = entity.getContent();
      
      if( target != null && SpointUtils.isHttpOk( response.getStatusCode() ) ) {
        ByteStreams.copy( data, target );
      }
      else {
        String text = new String( ByteStreams.toByteArray( data ), "UTF-8" );
        response.setContent( text );
      }
      
    } 
    finally {
      if( data != null ) {
        data.close();
      }
      if( target != null ) {
        target.close();
      }
      if( httpResponse != null ) {
        httpResponse.close();
      }
    }
    
    return response;
    
  }
  
  
  /**
   * Send POST request to server
   * 
   * @param connection Connection
   * @param path Request uri
   * @param contentAsString Request body as string
   * @param contentAsStream Request body as stream
   * @param xHttpMethod Header value for X-HTTP-Method
   * @return Response from server
   * @throws ClientProtocolException Http problem
   * @throws IOException Http communication problem
   * 
   */
  public static Response performPost( Connection connection, String path,
    String contentAsString, InputStream contentAsStream, String formDigestValue,
    String xHttpMethod ) throws ClientProtocolException, IOException {
    
    HttpClient httpClient = connection.getHttpClient();
    HttpHost httpHost = connection.getHttpHost();
    HttpContext context = connection.getContext();
    
    HttpPost requestObject = new HttpPost( path );
    
    if( contentAsString != null ) {
      requestObject.setEntity( new ByteArrayEntity( contentAsString.toString()
        .getBytes( "UTF8" ) ) );
    }
    else if( contentAsStream != null ) {
      requestObject.setEntity( new ByteArrayEntity( ByteStreams.toByteArray(
        contentAsStream ) ) );
    }
    
    if( xHttpMethod != null ) {
      requestObject.addHeader( "X-HTTP-Method", xHttpMethod );
    }
    if( formDigestValue != null ) {
      requestObject.addHeader( "X-RequestDigest", formDigestValue );
    }
    requestObject.addHeader( "IF-MATCH", "*" );
    requestObject.addHeader( "Content-Type", "application/json;odata=verbose" );
    
    CloseableHttpResponse httpResponse = null;
    Response response = new Response();

    try {

      httpResponse = (CloseableHttpResponse) httpClient.execute( httpHost,
        requestObject, context );
      response.setStatusLine( httpResponse.getStatusLine() );
      response.setStatusCode( response.getStatusLine().getStatusCode() );

      HttpEntity entity = httpResponse.getEntity();
      if( entity != null ) {
        InputStream data = entity.getContent();
        String text = new String( ByteStreams.toByteArray( data ), "UTF-8" );
        response.setContent( text );
        data.close();
      }

    }
    finally {
      if( httpResponse != null ) {
        httpResponse.close();
      }
    }

    return response;
    
  }
  
  

}


