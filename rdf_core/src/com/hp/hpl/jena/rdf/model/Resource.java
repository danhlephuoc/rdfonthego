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
 * Resource.java
 *
 * Created on 25 July 2000, 13:14
 */

package com.hp.hpl.jena.rdf.model;

import com.hp.hpl.jena.datatypes.RDFDatatype;


/** An RDF Resource.

    Resource instances when created may be associated with a specific model.
    Resources created <i>by</i> a model will refer to that model, and support
    a range of methods, such as <code>getProperty()</code> and
    <code>addProperty()</code> which will access or modify that model.  This
    enables the programmer to write code in a compact and easy style.
    
<p>
    Resources created by ResourceFactory will not refer to any model, and will 
    not permit operations which require a model. Such resources are useful
    as general constants.
 
  <p>This interface provides methods supporting typed literals.  This means
     that methods are provided which will translate a built in type, or an
     object to an RDF Literal.  This translation is done by invoking the
     <CODE>toString()</CODE> method of the object, or its built in equivalent.
     The reverse translation is also supported.  This is built in for built
     in types.  Factory objects, provided by the application, are used
     for application objects.</p>
  <p>This interface provides methods for supporting enhanced resources.  An
     enhanced resource is a resource to which the application has added
     behaviour.  RDF containers are examples of enhanced resources built in
     to this package.  Enhanced resources are supported by encapsulating a
     resource created by an implementation in another class which adds
     the extra behaviour.  Factory objects are used to construct such
     enhanced resources.</p>
  @author bwm
  @version Release='$Name:  $' Revision='$Revision: 1.32 $' Date='$Date: 2009/01/25 20:06:46 $'
*/
public interface Resource extends RDFNode {

    public AnonId getId();

    public boolean hasURI( String uri );

    public String getURI();

    public String getNameSpace();

    public String getLocalName();

    public String toString();

    public boolean equals( Object o );

    public Statement getRequiredProperty( Property p );

    public Statement getProperty( Property p );

    public StmtIterator listProperties( Property p );

    public StmtIterator listProperties();

    public Resource addLiteral( Property p, boolean o );
    
    public Resource addLiteral( Property p, long o );
    
    public Resource addLiteral( Property p, char o );

    public Resource addLiteral( Property value, double d );

    public Resource addLiteral( Property value, float d );

    public Resource addLiteral( Property p, Object o );
    
    public Resource addProperty( Property p, String o );

    public Resource addProperty( Property p, String o, String l );

    public Resource addProperty( Property p, String lexicalForm, RDFDatatype datatype );

    public Resource addProperty( Property p, RDFNode o );

    public boolean hasProperty( Property p );

    public boolean hasLiteral( Property p, boolean o );
    
    public boolean hasLiteral( Property p, long o );

    public boolean hasLiteral( Property p, char o );
    
    public boolean hasLiteral( Property p, double o );
    
    public boolean hasLiteral( Property p, float o );

    public boolean hasLiteral( Property p, Object o );

    public boolean hasProperty( Property p, String o );

    public boolean hasProperty( Property p, String o, String l );

    public boolean hasProperty( Property p, RDFNode o );

    public Resource removeProperties();

    public Resource removeAll( Property p );

    public Model getModel();
}

