package org.openjena.atlas.lib;

import java.nio.charset.Charset;

public class Chars
{
    private Chars() {}
    
 // So also Bytes.hexDigits to get bytes.
    final public static char[] digits10 = {
        '0' , '1' , '2' , '3' , '4' , '5' , '6' , '7' , '8' , '9'
    } ;
    
    // So also Bytes.hexDigits to get bytes.
    final public static char[] hexDigits = {
        '0' , '1' , '2' , '3' , '4' , '5' , '6' , '7' , '8' ,
        '9' , 'A' , 'B' , 'C' , 'D' , 'E' , 'F' 
//         , 'g' , 'h' ,
//        'i' , 'j' , 'k' , 'l' , 'm' , 'n' ,
//        'o' , 'p' , 'q' , 'r' , 's' , 't' ,
//        'u' , 'v' , 'w' , 'x' , 'y' , 'z'
        };
    
    /** Java name for UTF-8 encoding */
    private static final String encodingUTF8     = "utf-8" ;
    /** Java name for ASCII encoding */
    private static final String encodingASCII    = "ascii" ;
    
    public static final Charset charsetUTF8 = Charset.forName(encodingUTF8) ;
    public static final Charset charsetASCII = Charset.forName(encodingASCII) ;
    
    // Pools for encoders/decoder.  Paolo says that creating an encopder or decoder is not that cheap.
    // Initial pool size. Any additional encoder/decoder are later
    // placed in the pool - it's an infinite, reusing, growing pool.
    
    /*private static Pool<CharsetEncoder> encoders = new PoolSync<CharsetEncoder>() ;
    private static Pool<CharsetDecoder> decoders = new PoolSync<CharsetDecoder>() ;
    
    static {
        // Fill the pool.
        for ( int i = 0 ; i < PoolSize ; i++ )
        {
            putEncoder(createEncoder()) ;
            putDecoder(createDecoder()) ;
        }
    }

    *//** Create a UTF-8 encoder *//*
    public static CharsetEncoder createEncoder() { return charsetUTF8.newEncoder() ; }
    *//** Create a UTF-8 decoder *//*
    public static CharsetDecoder createDecoder() { return charsetUTF8.newDecoder() ; }

    *//** Get a UTF-8 encoder from the pool (null if pool empty) *//* 
    public static CharsetEncoder getEncoder() { return encoders.get() ; }
    *//** Add/return a UTF-8 encoder to the pool *//*
    public static void putEncoder(CharsetEncoder encoder) { encoders.put(encoder) ; }

    *//** Get a UTF-8 decoder from the pool (null if pool empty) *//* 
    public static CharsetDecoder getDecoder() { return decoders.get() ; }
    *//** Add/return a UTF-8 decoder to the pool *//*
    public static void putDecoder(CharsetDecoder decoder) { decoders.put(decoder) ; }*/
    
    public static void encodeAsHex(StringBuilder buff, char marker, char ch)
    {
        buff.append(marker) ;
        int lo = ch & 0xF ;
        int hi = (ch >> 4) & 0xF ;
        buff.append(Chars.hexDigits[hi]) ;                
        buff.append(Chars.hexDigits[lo]) ;
    }
}
