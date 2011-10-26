/*
	 (c) Copyright 2004, 2005, 2006, 2007, 2008, 2009 Hewlett-Packard Development Company, LP
	 [See end of file]
	 $Id: StatementBase.java,v 1.16 2009/01/16 17:23:48 andy_seaborne Exp $
*/

package com.hp.hpl.jena.rdf.model.impl;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.shared.JenaException;

public abstract class StatementBase{
	
	protected final ModelCom model;

	protected StatementBase( ModelCom model ){
		if (model == null) throw new JenaException( "Statement models must no be null" );
		this.model = model;
	}

	public Model getModel(){ 
		return model; 
	}

	protected abstract StatementImpl replace( RDFNode n );

	public abstract Literal getLiteral();
	
	public abstract Resource getResource();
	
	public abstract Resource getSubject();
	
	public abstract Property getPredicate();
	
	public abstract RDFNode getObject();

	protected StatementImpl stringReplace(String s, String lang){
		return replace(new LiteralImpl(Node.createLiteral(s, lang),model));
	}

	protected StatementImpl stringReplace( String s )
		{ return stringReplace( s, ""); }

	public Statement changeLiteralObject( boolean o )
		{ return changeObject( model.createTypedLiteral( o ) ); }
	
    public Statement changeLiteralObject( long o )
        { return changeObject( model.createTypedLiteral( o ) ); }

	public Statement changeLiteralObject( char o )
		{ return changeObject( model.createTypedLiteral( o ) ); }

    public Statement changeLiteralObject( double o )
        { return changeObject( model.createTypedLiteral( o ) ); }
    
	public Statement changeLiteralObject( float o )
		{ return changeObject( model.createTypedLiteral( o ) ); }
	
    public Statement changeLiteralObject( int o )
        { return changeObject( model.createTypedLiteral( o ) ); }

	public Statement changeObject( String o )
		{ return stringReplace( String.valueOf( o ) ); }

	public Statement changeObject( String o, String l )
		{ return stringReplace( String.valueOf( o ), l); }


	public Statement changeObject( RDFNode o )
		{ return replace( o ); }

	public boolean getBoolean()
		{ return getLiteral().getBoolean(); }

	public byte getByte()
		{ return getLiteral().getByte(); }

	public short getShort()
		{ return getLiteral().getShort(); }

	public int getInt()
		{ return getLiteral().getInt(); }

	public long getLong()
		{ return getLiteral().getLong(); }

	public char getChar()
		{ return getLiteral().getChar(); }

	public float getFloat()
		{ return getLiteral().getFloat(); }

	public double getDouble()
		{ return getLiteral().getDouble(); }

	public String getString()
		{ return getLiteral().getLexicalForm(); }

	/**
	 * utility: check that node is a Resource, throw otherwise
	 */
	protected Resource mustBeResource(RDFNode n){
		if (n instanceof Resource) return (Resource) n;
		else
			throw new ResourceRequiredException(n);
	}

	public String getLanguage()
		{ return getLiteral().getLanguage(); }

//
	/**
	 	Answer a string describing this Statement in a vagely pretty way, with the 
	 	representations of the subject, predicate, and object in that order.
	*/
	@Override
    public String toString()
		{
		return
		    "[" 
		    + getSubject().toString()
		    + ", " + getPredicate().toString() 
		    + ", " + objectString( getObject() )
		    + "]";
		}

	/**
	 	Answer a string describing <code>object</code>, quoting it if it is a literal.
	*/
	protected String objectString( RDFNode object )
		{ return object.asNode().toString( null, true ); }

	}

/*
	 (c) Copyright 2004, 2005, 2006, 2007, 2008, 2009 Hewlett-Packard Development Company, LP All rights
	 reserved. Redistribution and use in source and binary forms, with or without
	 modification, are permitted provided that the following conditions are met:
	 1. Redistributions of source code must retain the above copyright notice,
	 this list of conditions and the following disclaimer. 2. Redistributions in
	 binary form must reproduce the above copyright notice, this list of
	 conditions and the following disclaimer in the documentation and/or other
	 materials provided with the distribution. 3. The name of the author may not
	 be used to endorse or promote products derived from this software without
	 specific prior written permission.
	  
	 THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR IMPLIED
	 WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
	 MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
	 EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
	 SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
	 PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
	 OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
	 WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
	 OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
	 ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
