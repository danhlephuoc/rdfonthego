/*
  (c) Copyright 2004, 2005, 2006, 2007, 2008, 2009 Hewlett-Packard Development Company, LP
  [See end of file]
  $Id: PrefixMapping.java,v 1.42 2009/01/26 10:28:25 chris-dollin Exp $
*/

package com.hp.hpl.jena.shared;

import java.util.*;

//import com.hp.hpl.jena.assembler.JA;
import com.hp.hpl.jena.shared.impl.*;
import com.hp.hpl.jena.vocabulary.*;
/**
 * Methods for recording namepsace prefix mappings and applying and unapplying them to URIs. <p> Note that a Model *is* a PrefixMapping, so all the PrefixMapping operations apply to Models, and a Model can be used to supply the PrefixMapping argument to setNsPrefixes.
 * @author  kers
 */

public interface PrefixMapping
    {
	PrefixMapping setNsPrefix( String prefix, String uri );

    PrefixMapping removeNsPrefix( String prefix );
    
    PrefixMapping setNsPrefixes( PrefixMapping other );

    PrefixMapping setNsPrefixes( Map<String, String> map );

    PrefixMapping withDefaultMappings( PrefixMapping map );

    String getNsPrefixURI( String prefix );

    String getNsURIPrefix( String uri );

    Map<String, String> getNsPrefixMap();

    String expandPrefix( String prefixed );

    String shortForm( String uri );

    String qnameFor( String uri );

    PrefixMapping lock();

    @SuppressWarnings("serial")
	public static class IllegalPrefixException extends JenaException
        {
        public IllegalPrefixException( String prefixName ) { super( prefixName ); }
        }

    @SuppressWarnings("serial")
	public static class JenaLockedException extends JenaException
        {
        public JenaLockedException( PrefixMapping pm ) { super( pm.toString() ); }
        }

    public static class Factory
        { public static PrefixMapping create() { return new PrefixMappingImpl(); } }

    public static final PrefixMapping Standard = PrefixMapping.Factory.create()
        .setNsPrefix( "rdfs", RDFS.getURI() )
        .setNsPrefix( "rdf", RDF.getURI() )
        .setNsPrefix( "dc", DC_11.getURI() )
        .setNsPrefix( "owl", OWL.getURI() )
        .setNsPrefix( "xsd", XSD.getURI() )
        .setNsPrefix("vcard", VCARD.getURI())
        .lock()
        ;

    	boolean samePrefixMappingAs( PrefixMapping other );
    }

/*
    (c) Copyright 2004, 2005, 2006, 2007, 2008, 2009 Hewlett-Packard Development Company, LP
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