/*
	(c) Copyright 2000-2004, 2005, 2006, 2007, 2008, 2009 Hewlett-Packard Development Company, LP
	[See end of file]
	$Id: Statement.java,v 1.25 2009/04/23 08:47:29 chris-dollin Exp $
*/


package com.hp.hpl.jena.rdf.model;

import com.hp.hpl.jena.graph.*;
import com.hp.hpl.jena.util.iterator.Map1;

/** An RDF Statement.
 *
 * <p>A Statement is not a Resource, but can produce a ReifiedStatement
 * that represents it and from which the Statement can be recovered.</p>
 *
 * <p>A statement instance tracks which model created it, if any. All the
 * Resource components of a Statement are in the same model as the
 * Statement, if it has one, and are in no model if the Statement isn't.</p>
 *
 * <p>This interface provides methods supporting typed literals.  This means
 *    that methods are provided which will translate a built in type, or an
 *    object to an RDF Literal.  This translation is done by invoking the
 *    <CODE>toString()</CODE> method of the object, or its built in equivalent.
 *    The reverse translation is also supported.  This is built in for built
 *    in types.  Factory objects, provided by the application, are used
 *    for application objects.</p>
 
 * @author bwm; additions by kers
 * @version $Name:  $ $Revision: 1.25 $ $Date: 2009/04/23 08:47:29 $
 */

public interface Statement extends FrontsTriple
{
    public boolean equals(Object o);
    public int hashCode();
    
    public Resource getSubject();
    public Property getPredicate();
    public RDFNode getObject();
    
    public Statement getProperty(Property p) ;
    public Statement getStatementProperty(Property p) ;
    
    public Resource getResource() ;
    public Literal getLiteral() ;
    
    public boolean getBoolean() ;
    
    public byte getByte() ;
    public short getShort() ;
    public int getInt() ;
    public long getLong() ;
    public char getChar() ;
    public float getFloat() ;
    public double getDouble() ;
    public String getString() ;
    
    public Resource getResource(ResourceF f) ;
    
        
    public String getLanguage();

    
    
    public Statement changeLiteralObject( boolean o );
    
    public Statement changeLiteralObject( long o );
    
    public Statement changeLiteralObject( int o );
    
    public Statement changeLiteralObject(char o) ;
    
    public Statement changeLiteralObject( float o );
    
    public Statement changeLiteralObject( double o );
    
    public Statement changeObject(String o) ;  
   
    public Statement changeObject(String o, String l) ;
   
    public Statement changeObject(RDFNode o) ;
    
    public Statement remove() ;
    
    boolean isReified();
    
    ReifiedStatement createReifiedStatement();
    
    ReifiedStatement createReifiedStatement( String uri );
        
    RSIterator listReifiedStatements();
    
    Model getModel();
    
    void removeReification();
    
    /**
	 * Utility constants -- in a nested class for namespace reasons.
	 */
    public static class Util{
    	public static final Map1<Statement, Resource> getSubject = new Map1<Statement, Resource>(){
            public Resource map1( Statement o ) { 
            	return o.getSubject(); 
            }
    	};
            
        public static final Map1<Statement, Property> getPredicate = new Map1<Statement, Property>(){
        	public Property map1( Statement o ) { 
        		return o.getPredicate(); 
        	}
        };
            
        public static final Map1<Statement, RDFNode> getObject = new Map1<Statement, RDFNode>(){
        	public RDFNode map1( Statement o ){ 
        		return o.getObject(); 
        	}
        };
    }
}
/*
	  (c) Copyright 2000, 2001, 2002, 2003, 2004, 2005, 2006, 2007, 2008, 2009 Hewlett-Packard Development Company, LP
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