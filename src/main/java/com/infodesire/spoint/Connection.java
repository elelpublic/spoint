// (C) 1998-2016 Information Desire Software GmbH
// www.infodesire.com

package com.infodesire.spoint;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.client.protocol.HttpClientContext;


/**
 * An open sharepoint connection. <p>
 * 
 * Due to expensive NTLM authentication, the sharepoint connection is stateful.
 *
 */
public class Connection {


  private Config config;
  private URL url;
  private String host;
  private int port;
  private String protocol;
  private HttpClientContext context;
  private HttpClient httpClient;
  private HttpHost httpHost;


  public Connection( Config config ) throws MalformedURLException {
    this.config = config;

    url = new URL( config.getUrl() );
    host = url.getHost();
    port = url.getPort();
    protocol = url.getProtocol();

    if( protocol == null ) {
      protocol = "http";
    }

    if( port == -1 ) {
      port = 80;
      if( protocol.toLowerCase().equals( "https" ) ) {
        port = 443;
      }
    }

  }


  /**
   * @return the config
   */
  public Config getConfig() {
    return config;
  }


  /**
   * @return the url
   */
  public URL getUrl() {
    return url;
  }


  /**
   * @return the host
   */
  public String getHost() {
    return host;
  }


  /**
   * @return the port
   */
  public int getPort() {
    return port;
  }


  /**
   * @return the protocol
   */
  public String getProtocol() {
    return protocol;
  }


  /**
   * @return the context
   */
  public HttpClientContext getContext() {
    return context;
  }


  /**
   * @param context the context to set
   */
  public void setContext( HttpClientContext context ) {
    this.context = context;
  }


  /**
   * @return the httpClient
   */
  public HttpClient getHttpClient() {
    return httpClient;
  }


  /**
   * @param httpClient the httpClient to set
   */
  public void setHttpClient( HttpClient httpClient ) {
    this.httpClient = httpClient;
  }


  /**
   * @return the httpHost
   */
  public HttpHost getHttpHost() {
    return httpHost;
  }


  /**
   * @param httpHost the httpHost to set
   */
  public void setHttpHost( HttpHost httpHost ) {
    this.httpHost = httpHost;
  }


}
