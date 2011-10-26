/*
  (c) Copyright 2002, 2003, 2004, 2005, 2006, 2007, 2008, 2009 Hewlett-Packard Development Company, LP
  [See end of file]
  $Id: Model.java,v 1.78 2009/03/27 15:55:05 andy_seaborne Exp $
*/

package com.hp.hpl.jena.rdf.model;

import com.hp.hpl.jena.datatypes.*;

import com.hp.hpl.jena.shared.*;

import java.util.*;

/**
    An RDF Model.
<p>
    An RDF model is a set of Statements.  Methods are provided for creating
    resources, properties and literals and the Statements which link them,
    for adding statements to and removing them from a model, for
    querying a model and set operations for combining models.
<p>
    Models may create Resources [URI nodes and bnodes]. Creating a Resource does
    <i>not</i> make the Resource visible to the model; Resources are only "in" Models
    if Statements about them are added to the Model. Similarly the only way to "remove"
    a Resource from a Model is to remove all the Statements that mention it.
<p>
    When a Resource or Literal is created by a Model, the Model is free to re-use an
    existing Resource or Literal object with the correct values, or it may create a fresh
    one. [All Jena RDFNodes and Statements are immutable, so this is generally safe.]
<p>
    This interface defines a set of primitive methods.  A set of
    convenience methods which extends this interface, e.g. performing
    automatic type conversions and support for enhanced resources,
    is defined in {@link ModelCon}.</P>

 <h2>System Properties</h2>


 <h3>Firewalls and Proxies</h3>

    Some of the methods, e.g. the read methods, may have to traverse a
    firewall.  This can be accomplished using the standard java method
    of setting system properties.  To use a socks proxy, include on the
    java command line:</p>
 * <blockquote>
 *   -DsocksProxyHost=[your-proxy-domain-name-or-ip-address]
 * </blockquote>
 *
 * <p>To use an http proxy, include on the command line:</p>
 * <blockquote>
 * -DproxySet=true -DproxyHost=[your-proxy] -DproxyPort=[your-proxy-port-number]
 * </blockquote>
 *
 * <p>Alternatively, these properties can be set programatically, e.g.</p>
 *
 * <code><pre>
 *   System.getProperties().put("proxySet","true");
 *   System.getProperties().put("proxyHost","proxy.hostname");
 *   System.getProperties().put("proxyPort",port_number);
 * </pre></code>
 *
 * @author bwm
 * @version $Name:  $ $Revision: 1.78 $Date: 2009/03/27 15:55:05 $'
 */
public interface Model
    extends ModelCon, ModelGraphInterface,PrefixMapping, Lock
{
	long size() ;

    boolean isEmpty();

	

	NsIterator 	listNameSpaces() ;

	Resource getResource(String uri) ;

	Property getProperty(String nameSpace, String localName);

	public Resource createResource() ;

    public Resource createResource( AnonId id );

	public Resource createResource( String uri ) ;

	public Property createProperty(String nameSpace, String localName);

	public Literal createLiteral(String v, String language);

	public Literal createTypedLiteral(String lex, RDFDatatype dtype);

    public Literal createTypedLiteral(Object value, RDFDatatype dtype);

    public Literal createTypedLiteral(Object value);
	
    public Statement createStatement( Resource s, Property p, RDFNode o );

    
  

	
    Model add(Statement s) ;

    Model add( Statement [] statements );

    Model remove( Statement [] statements );

    Model add( List<Statement> statements );

    Model remove( List<Statement> statements );

	Model add(StmtIterator iter) ;

	Model add(Model m) ;

    Model add( Model m, boolean suppressReifications );

	
//    public Model read(String url) ;
//
//	public Model read(InputStream in, String base) ;

	
	Model remove(Statement s) ;

	Statement getRequiredProperty(Resource s, Property p) ;

    Statement getProperty( Resource s, Property p );

	


	
	boolean contains(Resource s, Property p) ;

	boolean contains(Resource s, Property p, RDFNode o) ;

    boolean contains(Statement s) ;
    
    boolean containsAny(StmtIterator iter) ;

	boolean containsAll(StmtIterator iter) ;

	boolean containsAny(Model model) ;

	boolean containsAll(Model model) ;

	boolean isReified( Statement s );
	

	Resource getAnyReifiedStatement( Statement s );
	

	void removeAllReifications( Statement s );

    void removeReification( ReifiedStatement rs );

    StmtIterator listStatements() ;

	StmtIterator listStatements(Selector s) ;
  
    StmtIterator listStatements( Resource s, Property p, RDFNode o );

	Model intersection(Model model) ;

	Model difference(Model model) ;

	public boolean equals(Object m);

	
	boolean independent();

	boolean isIsomorphicWith(Model g);

	public void close();

    public Lock getLock() ;

    public Model register( ModelChangedListener listener );

    public Model unregister( ModelChangedListener listener );

	public Model notifyEvent( Object e );

    public Model removeAll();

    public Model removeAll( Resource s, Property p, RDFNode r );

    public boolean isClosed();

}

/*
 *  (c)   Copyright 2000, 2001, 2002, 2003, 2004, 2005, 2006, 2007, 2008, 2009 Hewlett-Packard Development Company, LP
 *   All rights reserved.
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
 * $Id: Model.java,v 1.78 2009/03/27 15:55:05 andy_seaborne Exp $
 */
