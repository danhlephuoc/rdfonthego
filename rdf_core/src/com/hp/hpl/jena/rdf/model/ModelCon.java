/*
 *  (c) Copyright 2000, 2001, 2002, 2002, 2003, 2004, 2005, 2006, 2007, 2008, 2009 Hewlett-Packard Development Company, LP
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
 *
 * ModelCon.java
 *
 * Created on 7 December 2001, 11:00
 */

package com.hp.hpl.jena.rdf.model;

import java.util.Calendar;

import com.hp.hpl.jena.datatypes.RDFDatatype;
import com.hp.hpl.jena.graph.Node;



public interface ModelCon {

	Resource getResource(String uri, ResourceF f) ;

	Property getProperty(String uri) ;
    
	public Resource createResource(Resource type) ;
    
    public RDFNode getRDFNode( Node n );

    public Resource createResource(String uri, Resource type);

    public Resource createResource(ResourceF f) ;
 
    public Resource createResource(String uri, ResourceF f) ;

    public Property createProperty(String uri) ;
    
    public Literal createLiteral( String v );
 
    public Literal createTypedLiteral(boolean v) ; 
    
    public Literal createTypedLiteral(int v) ;
    
    public Literal createTypedLiteral(long v) ;

    public Literal createTypedLiteral(Calendar d);
    
    public Literal createTypedLiteral(char v) ;
    
    public Literal createTypedLiteral(float v) ;
    
    public Literal createTypedLiteral(double v) ;
    
    public Literal createTypedLiteral(String v) ;
    
    public Literal createTypedLiteral(Object v) ;

    public Literal createTypedLiteral(String lex, String typeURI)  ;
    
    public Literal createTypedLiteral(Object value, String typeURI);
    
    
    public Statement createLiteralStatement( Resource s, Property p, boolean o );
    
    public Statement createLiteralStatement( Resource s, Property p, float o );
    
    public Statement createLiteralStatement( Resource s, Property p, double o );
    
    public Statement createLiteralStatement( Resource s, Property p, long o );
    
    public Statement createLiteralStatement( Resource s, Property p, int o );
    
    public Statement createLiteralStatement( Resource s, Property p, char o );

    public Statement createLiteralStatement( Resource s, Property p, Object o );
    
    public Statement createStatement(Resource s, Property p, String o)  ;
    
    public Statement createStatement(Resource s, Property p, String o, String l) ;

   
    Model add(Resource s, Property p, RDFNode o)     ;

    Model addLiteral( Resource s, Property p, boolean o );
    
    Model addLiteral( Resource s, Property p, long o );
    
    Model addLiteral( Resource s, Property p, int o );

    Model addLiteral( Resource s, Property p, char o ) ;
    
    Model addLiteral( Resource s, Property p, float o );

    Model addLiteral( Resource s, Property p, double o ) ;

    @Deprecated Model addLiteral( Resource s, Property p, Object o );

    Model add(Resource s, Property p, String o) ;

    Model add(Resource s, Property p, String lex, RDFDatatype datatype) ;
       
    Model add(Resource s, Property p, String o, String l) ;

    Model remove( Resource s, Property p, RDFNode o );

    Model remove(StmtIterator iter) ;

    Model remove(Model m) ;
    
    Model remove( Model m, boolean suppressReifications );

    StmtIterator listLiteralStatements( Resource subject, Property predicate, boolean object );
    StmtIterator listLiteralStatements( Resource subject, Property predicate, char object );
    StmtIterator listLiteralStatements(Resource subject, Property predicate, long object );
    StmtIterator listLiteralStatements( Resource subject, Property predicate, float object );
    StmtIterator listLiteralStatements(Resource subject, Property predicate, double  object );
    StmtIterator listStatements( Resource subject, Property predicate, String  object );
    StmtIterator listStatements(Resource subject,
                                Property predicate,
                                String   object,
                                String   lang);


  
                                           
    boolean containsLiteral( Resource s, Property p, boolean o );
    boolean containsLiteral( Resource s, Property p, long o );
    boolean containsLiteral( Resource s, Property p, int o );
    boolean containsLiteral( Resource s, Property p, char o );
    boolean containsLiteral( Resource s, Property p, float o );
    boolean containsLiteral( Resource s, Property p, double o );
    boolean containsLiteral( Resource s, Property p, Object o );
    boolean contains( Resource s, Property p, String o );
    boolean contains( Resource s, Property p, String o, String l );
}