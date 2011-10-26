/*
 * (c) Copyright 2002, 2003, 2004, 2005, 2006, 2007, 2008, 2009 Hewlett-Packard Development Company, LP
 * [See end of file]
 */

package com.hp.hpl.jena.util;

import java.io.*;
import java.nio.charset.Charset;
import com.hp.hpl.jena.JenaRuntime;

public class FileUtils
{
    public static final String langNTriple      = "N-TRIPLE" ;
    public static final String langN3           = "N3" ;
    public static final String langTurtle       = "TURTLE" ;
    
    
    /** Java name for UTF-8 encoding */
    public static final String encodingUTF8     = "utf-8" ;
    
    static Charset utf8 = null ;
    static {
        try {
            utf8 = Charset.forName(encodingUTF8) ;
        } catch (Throwable ex)
        {
          
        }
    }
    
    
    /** Create a reader that uses UTF-8 encoding */ 
    
    static public Reader asUTF8(InputStream in) {
        if ( JenaRuntime.runUnder(JenaRuntime.featureNoCharset) )
            return new InputStreamReader(in) ;
        return new InputStreamReader(in, utf8.newDecoder());
    }

    
    static public BufferedReader asBufferedUTF8(InputStream in) {
        return new BufferedReader(asUTF8(in)) ;
    }
    
    static public Writer asUTF8(OutputStream out) {
        if ( JenaRuntime.runUnder(JenaRuntime.featureNoCharset) )
            return new OutputStreamWriter(out) ;
        return new OutputStreamWriter(out, utf8.newEncoder());
    }
    
    static public PrintWriter asPrintWriterUTF8(OutputStream out) {
        return new PrintWriter(asUTF8(out)); 
    }
    
    public static String guessLang( String name, String otherwise )
    {
        String suffix = getFilenameExt( name );
        if (suffix.equals( "n3" ))   return langN3;
        if (suffix.equals( "nt" ))   return langNTriple;
        if (suffix.equals( "ttl" ))  return langTurtle ;
        return otherwise; 
    }    
   
    public static String toFilename(String filenameOrURI)
    {
        if ( !isFile(filenameOrURI) )
            return null ;
      
        String fn = filenameOrURI ;

        if ( ! fn.startsWith("file:") )
            return fn ;
        
     
        if ( fn.startsWith("file:///") )
            fn = fn.substring("file://".length()) ;
        else if ( fn.startsWith("file://localhost/") )
            // NB Leaves the leading slash on. 
            fn = fn.substring("file://localhost".length()) ;
        else
            // Just trim off the file:
            fn = fn.substring("file:".length()) ;

        return decodeFileName(fn) ;
    }
    
    public static String decodeFileName(String s)
    {
        if ( s.indexOf('%') < 0 ) 
            return s ;
        int len = s.length();
        StringBuffer sbuff = new StringBuffer(len) ;

        // This is URIRef.decode()? Is that code used?
        // Just decode % escapes.
        // Not http://www.daml.org/2001/03/daml+oil
        for ( int i =0 ; i < len ; i++ )
        {
            char c = s.charAt(i);
            switch (c)
            {
                case '%':
                    int codepoint = Integer.parseInt(s.substring(i+1,i+3),16) ;
                    char ch = (char)codepoint ;
                    sbuff.append(ch) ;
                    i = i+2 ;
                    break ;
                default:
                    sbuff.append(c);
            }
        }
        return sbuff.toString();
    }
    
    
    /** Turn a plain filename into a "file:" URL */
    public static String toURL(String filename)
    {
        if ( filename.length()>5
        		&& filename.substring(0,5).equalsIgnoreCase("file:") )
            return filename ;
        
        return "file://" + new File(filename).toURI().toString().substring(5);
    }
    
    /**
     * 
     * @deprecated Broken: use toURL()
     */
    @Deprecated
    public static String encodeFileName(String s)
    {
        int len = s.length();
        StringBuffer sbuff = new StringBuffer(len) ;

        // Convert a few charcaters that occur in filenames into a safe form.
        for ( int i = 0 ; i < len ; i++ )
        {
            char c = s.charAt(i);
            switch (c)
            {
                case ' ': case '~':
                    sbuff.append('%') ;
                    sbuff.append(Integer.toHexString(c).toUpperCase()) ;
                    break ;
                default:
                    sbuff.append(c);
            }
        }
        return sbuff.toString();
    }
   
    public static boolean isFile(String name)
    {
        String scheme = getScheme(name) ;
        
        if ( scheme == null  )
            // No URI scheme - treat as filename
            return true ;
        
        if ( scheme.equals("file") )
            // file: URI scheme
            return true ;
            
        // Windows: "c:" etc
        if ( scheme.length() == 1 )
            // file: URI scheme
            return true ;
        
        return false ;
    }
    
    public static boolean isURI(String name)
    {
        return (getScheme(name) != null) ;
    }

    public static String getScheme(String uri)
    {
        // Find "[^/:]*:.*"
        for ( int i = 0 ; i < uri.length() ; i++ )
        {
            char ch = uri.charAt(i) ;
            if ( ch == ':' )
                return uri.substring(0,i) ;
            if ( ! isASCIILetter(ch) )
                // Some illegal character before the ':' 
                break ;
        }
        return null ;
    }
    
    private static boolean isASCIILetter(char ch)
    {
        return ( ch >= 'a' && ch <= 'z' ) || ( ch >= 'A' && ch <= 'Z' ) ;
    }
   
    public static String getDirname(String filename)
    {
        File f = new File(filename) ;
        return f.getParent() ;
    }

    public static String getBasename(String filename)
    {
        File f = new File(filename) ;
        return f.getName() ;
    }

    /**
     Get the suffix part of a file name or a URL in file-like format.
     */
    public static String getFilenameExt( String filename)
    {
        int iSlash = filename.lastIndexOf( '/' );      
        int iBack = filename.lastIndexOf( '\\' );
        int iExt = filename.lastIndexOf( '.' ); 
        if (iBack > iSlash) iSlash = iBack;
        return iExt > iSlash ? filename.substring( iExt+1 ).toLowerCase() : "";
    }

    /**
     * Open an resource file for reading.
     */
    public static InputStream openResourceFileAsStream(String filename)
    throws FileNotFoundException {
        InputStream is = ClassLoader.getSystemResourceAsStream(filename);
        if (is == null) {
            // Try local loader with absolute path
            is = FileUtils.class.getResourceAsStream("/" + filename);
            if (is == null) {
                // Try local loader, relative, just in case
                is = FileUtils.class.getResourceAsStream(filename);
                if (is == null) {
                    // Can't find it on classpath, so try relative to current directory
                    // Will throw security exception under and applet but there's not other choice left
                    is = new FileInputStream(filename);
                }
            }
        }
        return is;
    }

}

/*
 *  (c) Copyright 2002, 2003, 2004, 2005, 2006, 2007, 2008, 2009 Hewlett-Packard Development Company, LP
 *  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. The name of the author may not be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

