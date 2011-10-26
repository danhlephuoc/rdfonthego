/******************************************************************
 * File:        XSD.java
 * Created by:  Dave Reynolds
 * Created on:  27-Mar-03
 * 
 * (c) Copyright 2003, 2004, 2005, 2006, 2007, 2008, 2009 Hewlett-Packard Development Company, LP
 * [See end of file]
 * $Id: XSD.java,v 1.15 2009/01/13 13:22:49 der Exp $
 *****************************************************************/
package com.hp.hpl.jena.vocabulary;

import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;

/**
 * Defines Jena resources corresponding to the URIs for  the XSD primitive datatypes which are known to Jena. 
 * @author  <a href="mailto:der@hplb.hpl.hp.com">Dave Reynolds</a>
 * @version  $Revision: 1.15 $ on $Date: 2009/01/13 13:22:49 $
 */
public class XSD {    
    /**
     * The RDF-friendly version of the XSD namespace
     * with trailing # character.
     */
    public static String getURI() { return XSDDatatype.XSD + "#"; }
    
    public static Resource xfloat;
    
    public static Resource xdouble;
    
    public static Resource xint;
    
    public static Resource xlong;
       
    public static Resource xshort;
       
    public static Resource xbyte;
       
    public static Resource xboolean;
    
    public static Resource xstring;
    
    public static Resource unsignedByte;
       
    public static Resource unsignedShort;
       
    public static Resource unsignedInt;
       
    public static Resource unsignedLong;
       
    public static Resource decimal;
       
    public static Resource integer;
       
    public static Resource nonPositiveInteger;
       
    public static Resource nonNegativeInteger;
       
    public static Resource positiveInteger;
       
    public static Resource negativeInteger;
       
    public static Resource normalizedString;
    
    public static Resource anyURI;
    
    public static Resource token;

    public static Resource Name;

    public static Resource QName;

    public static Resource language;

    public static Resource NMTOKEN;

    public static Resource ENTITIES;

    public static Resource NMTOKENS;

    public static Resource ENTITY;

    public static Resource ID;

    public static Resource NCName;

    public static Resource IDREF;

    public static Resource IDREFS;

    public static Resource NOTATION;

    public static Resource hexBinary;

    public static Resource base64Binary;

    public static Resource date;

    public static Resource time;

    public static Resource dateTime;

    public static Resource duration;

    public static Resource gDay;

    public static Resource gMonth;

    public static Resource gYear;

    public static Resource gYearMonth;

    public static Resource gMonthDay;

    // Initializer
    static {
        xfloat = ResourceFactory.createResource(XSDDatatype.XSDfloat.getURI());
        xdouble = ResourceFactory.createResource(XSDDatatype.XSDdouble.getURI());
        xint = ResourceFactory.createResource(XSDDatatype.XSDint.getURI());
        xlong = ResourceFactory.createResource(XSDDatatype.XSDlong.getURI());
        xshort = ResourceFactory.createResource(XSDDatatype.XSDshort.getURI());
        xbyte = ResourceFactory.createResource(XSDDatatype.XSDbyte.getURI());
        unsignedByte = ResourceFactory.createResource(XSDDatatype.XSDunsignedByte.getURI());
        unsignedShort = ResourceFactory.createResource(XSDDatatype.XSDunsignedShort.getURI());
        unsignedInt = ResourceFactory.createResource(XSDDatatype.XSDunsignedInt.getURI());
        unsignedLong = ResourceFactory.createResource(XSDDatatype.XSDunsignedLong.getURI());
        decimal = ResourceFactory.createResource(XSDDatatype.XSDdecimal.getURI());
        integer = ResourceFactory.createResource(XSDDatatype.XSDinteger.getURI());
        nonPositiveInteger = ResourceFactory.createResource(XSDDatatype.XSDnonPositiveInteger.getURI());
        nonNegativeInteger = ResourceFactory.createResource(XSDDatatype.XSDnonNegativeInteger.getURI());
        positiveInteger = ResourceFactory.createResource(XSDDatatype.XSDpositiveInteger.getURI());
        negativeInteger = ResourceFactory.createResource(XSDDatatype.XSDnegativeInteger.getURI());
        xboolean = ResourceFactory.createResource(XSDDatatype.XSDboolean.getURI());
        xstring = ResourceFactory.createResource(XSDDatatype.XSDstring.getURI());
        normalizedString = ResourceFactory.createResource(XSDDatatype.XSDnormalizedString.getURI());
        anyURI = ResourceFactory.createResource(XSDDatatype.XSDanyURI.getURI());
        token = ResourceFactory.createResource(XSDDatatype.XSDtoken.getURI());
        Name = ResourceFactory.createResource(XSDDatatype.XSDName.getURI());
        QName = ResourceFactory.createResource(XSDDatatype.XSDQName.getURI());
        language = ResourceFactory.createResource(XSDDatatype.XSDlanguage.getURI());
        NMTOKEN = ResourceFactory.createResource(XSDDatatype.XSDNMTOKEN.getURI());
        ENTITY = ResourceFactory.createResource(XSDDatatype.XSDENTITY.getURI());
        ID = ResourceFactory.createResource(XSDDatatype.XSDID.getURI());
        NCName = ResourceFactory.createResource(XSDDatatype.XSDNCName.getURI());
        IDREF = ResourceFactory.createResource(XSDDatatype.XSDIDREF.getURI());
        NOTATION = ResourceFactory.createResource(XSDDatatype.XSDNOTATION.getURI());
        hexBinary = ResourceFactory.createResource(XSDDatatype.XSDhexBinary.getURI());
        base64Binary = ResourceFactory.createResource(XSDDatatype.XSDbase64Binary.getURI());
        date = ResourceFactory.createResource(XSDDatatype.XSDdate.getURI());
        time = ResourceFactory.createResource(XSDDatatype.XSDtime.getURI());
        dateTime = ResourceFactory.createResource(XSDDatatype.XSDdateTime.getURI());
        duration = ResourceFactory.createResource(XSDDatatype.XSDduration.getURI());
        gDay = ResourceFactory.createResource(XSDDatatype.XSDgDay.getURI());
        gMonth = ResourceFactory.createResource(XSDDatatype.XSDgMonth.getURI());
        gYear = ResourceFactory.createResource(XSDDatatype.XSDgYear.getURI());
        gYearMonth = ResourceFactory.createResource(XSDDatatype.XSDgYearMonth.getURI());
        gMonthDay = ResourceFactory.createResource(XSDDatatype.XSDgMonthDay.getURI());
    }
}

/*
    (c) Copyright 2003, 2004, 2005, 2006, 2007, 2008, 2009 Hewlett-Packard Development Company, LP
    All rights reserved.

    Redistribution and use in source and binary forms, with or without
    modification, are permitted provided that the following conditions
    are met:

    1. Redistributions of source code must retain the above copyright
       notice, this list of conditions and the following disclaimer.

    2. Redistributions in binary form must reproduce the above copyright
       notice, this list of conditions and the following disclaimer in the
       documentation and/or other materials provided with the distribution.

    3. The name of the author may not be used to endorse or promote products
       derived from this software without specific prior written permission.

    THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
    IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
    OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
    IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
    INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
    NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
    DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
    THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
    (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
    THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
