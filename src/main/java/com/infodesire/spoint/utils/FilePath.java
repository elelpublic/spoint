// (C) 1998-2015 Information Desire Software GmbH
// www.infodesire.com

package com.infodesire.spoint.utils;

import com.google.common.base.Joiner;

import java.util.ArrayList;
import java.util.List;


/**
 * A normalized unixlike file path.
 *
 */
public class FilePath implements Comparable<FilePath> {
  
  
  private List<String> elements = new ArrayList<String>();
  private boolean relative = true;
  private Integer hashCode;
  private String toString;
  

  /**
   * Create base path
   * 
   * @param relative This is a relative path. Does not begin with "/"
   * 
   */
  public FilePath( boolean relative ) {
    this.relative = relative;
  }
  
  
  public FilePath( boolean relative,  List<String> elements ) {
    this.relative = relative;
    this.elements.addAll( elements );
  }
  
  
  public FilePath( FilePath parent, String element ) {
    this( parent.relative, parent.elements );
    elements.add( element );
  }


  /**
   * @return String representation
   * 
   */
  public String toString() {
    if( toString == null ) {
      toString = ( relative ? "" : "/" ) + Joiner.on( "/" ).join( elements );
    }
    return toString;
  }


//  public String getLast() {
//    return elements.size() == 0 ? null : elements.get( elements.size() - 1 );
//  }
//
//
//  public String getFirst() {
//    return elements.size() == 0 ? null : elements.get( 0 );
//  }
  
  
  public int size() {
    return elements.size();
  }


  /**
   * @return Route which starts at second element
   * 
   */
  public FilePath removeFirst() {
    return new FilePath( true, elements.subList( 1, elements.size() ) );
  }


  public static FilePath parse( String path ) {
    path = path.trim();
    if( path.length() == 0 ) {
      return new FilePath( true );
    }
    boolean relative = true;
    if( path.charAt( 0 ) == '/' ) {
      relative = false;
      path = path.substring( 1 );
    }
    List<String> elements = new ArrayList<String>();
    for( String routeElement : path.split( "/" ) ) {
      if( routeElement != null ) {
        routeElement = routeElement.trim();
        if( routeElement.length() > 0 ) {
          elements.add( routeElement );
        }
      }
    }
    return new FilePath( relative, elements );
  }


  /**
   * @param childRoute
   * @return This route is the direct parent oth the child route
   * 
   */
  public boolean isDirectParentOf( FilePath childRoute ) {
    
    String childPath = childRoute.toString();
    String path = toString();
    
    if( childPath.startsWith( path ) ) {
      if( childPath.equals( path ) ) {
        return false;
      }
      else {
        String remainder = childPath.substring( path.length() + 1);
        return remainder.indexOf( '/' ) == -1;
      }
    }
    else {
      return false;
    }
    
  }


  /**
   * @return Paths not beginning with "/" are considered relative paths
   * 
   */
  public boolean isRelative() {
    return relative;
  }
  
  
  /**
   * @return For relative paths: "" is the base path, for absolute paths "/" is the base oath
   */
  public boolean isBase() {
    return elements.isEmpty();
  }
  
  
  public int hashCode() {
    if( hashCode == null ) {
      hashCode = toString().hashCode();
    }
    return hashCode;
  }
  
  
  public boolean equals( Object o ) {
    if( o instanceof FilePath ) {
      return toString().equals( o.toString() );
    }
    return false;
  }


  @Override
  public int compareTo( FilePath o ) {
    return toString().compareTo( o.toString() );
  }


  /**
   * @return Parent path or null if this is the base path
   * 
   */
  public FilePath getParent() {
    if( isBase() ) {
      return null;
    }
    else {
      return new FilePath( relative, elements.subList( 0, elements.size() - 1 ) );
    }
  }
  
  
  /**
   * Normalize by parsing and formatting
   * 
   * @param path Path string
   * @return Normalized path string
   * 
   */
  public static String normalize( String path ) {
    return parse( path ).toString();
  }


  /**
   * @param index Index of element
   * @return Element of path
   */
  public String getElement( int index ) {
    return elements.get( index );
  }


  /**
   * @return Last element of path
   * 
   */
  public String getLast() {
    return elements.get( elements.size() - 1 );
  }
  
  
}



