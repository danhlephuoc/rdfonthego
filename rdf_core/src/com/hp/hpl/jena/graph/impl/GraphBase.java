/*
  (c) Copyright 2002, 2003, 2004, 2005, 2006, 2007, 2008, 2009 Hewlett-Packard Development Company, LP
  [See end of file]
  $Id: GraphBase.java,v 1.55 2009/01/26 08:37:08 chris-dollin Exp $
*/

package com.hp.hpl.jena.graph.impl;

import com.hp.hpl.jena.graph.*;


import com.hp.hpl.jena.util.iterator.*;

import com.hp.hpl.jena.shared.*;
import com.hp.hpl.jena.shared.impl.PrefixMappingImpl;

/**
 * GraphBase is an implementation of Graph that provides some convenient base functionality for Graph implementations. 
 * <p> Subtypes of GraphBase must provide performAdd(Triple), performDelete(Triple), 
 *  graphBaseFind(TripleMatch,TripleAction), and graphBaseSize(). GraphBase  provides default implementations of the other methods,
 *   including the other finds  (on top of that one), a simple-minded prepare, and contains. GraphBase also  handles the event-listening
 *    and registration interfaces. <p> When a GraphBase is closed, future operations on it may throw an exception.
 * @author  kers
 */

public abstract class GraphBase implements GraphWithPerform 
	{
     protected final ReificationStyle style;
    
    protected boolean closed = false;

    public GraphBase(){ 
    	this( ReificationStyle.Minimal );
    }
    
    public GraphBase( ReificationStyle style )
        { this.style = style; }
        
    protected void checkOpen()
        { if (closed) throw new ClosedException( "already closed", this ); }

    public void close() 
        { 
        closed = true;
        if (reifier != null) reifier.close(); 
        }
    
    public boolean isClosed()
        { return closed; }
            
    public boolean dependsOn( Graph other ) 
        { return this == other; }

	public GraphEventManager getEventManager()
        { 
        if (gem == null) gem = new SimpleEventManager( this ); 
        return gem;
        }
    
    protected GraphEventManager gem;

    public void notifyAdd( Triple t )
        { getEventManager().notifyAddTriple( this, t ); }
        
    public void notifyDelete( Triple t )
        { getEventManager().notifyDeleteTriple( this, t ); }
        
    public BulkUpdateHandler getBulkUpdateHandler()
        { 
        if (bulkHandler == null) bulkHandler = new SimpleBulkUpdateHandler( this ); 
        return bulkHandler;
        }

    protected BulkUpdateHandler bulkHandler;
    
    public PrefixMapping getPrefixMapping()
        { return pm; }

    protected PrefixMapping pm = new PrefixMappingImpl();
    
	public void add( Triple t ) 
        {
        checkOpen();
        performAdd( t );
        notifyAdd( t );
        }
    
    public void performAdd( Triple t )
        { throw new AddDeniedException( "GraphBase::performAdd" ); }

	public final void delete( Triple t )
        {
        checkOpen();
        performDelete( t );
        notifyDelete( t );
        }
        
    public void performDelete( Triple t ) 
        { throw new DeleteDeniedException( "GraphBase::delete" ); }

	
    public final ExtendedIterator<Triple> find( TripleMatch m )
        { checkOpen(); 
        return reifierTriples( m ) .andThen( graphBaseFind( m ) ); } 
    
    protected abstract ExtendedIterator<Triple> graphBaseFind( TripleMatch m );

    public final ExtendedIterator<Triple> find( Node s, Node p, Node o ) 
        { checkOpen();
        return graphBaseFind( s, p, o ); }
    
    protected ExtendedIterator<Triple> graphBaseFind( Node s, Node p, Node o )
        { return find( Triple.createMatch( s, p, o ) ); }

	public final boolean contains( Triple t ) 
        { checkOpen();
		return reifierContains( t ) || graphBaseContains( t );	}
   
    protected boolean reifierContains( Triple t )
        { ClosableIterator<Triple> it = getReifier().findExposed( t );
        try { return it.hasNext(); } finally { it.close(); } }

    protected boolean graphBaseContains( Triple t )
        { return containsByFind( t ); }

	public final boolean contains( Node s, Node p, Node o ) {
        checkOpen();
		return contains( Triple.create( s, p, o ) );
	}
    
    final protected boolean containsByFind( Triple t )
        {
        ClosableIterator<Triple> it = find( t );
        try { return it.hasNext(); } finally { it.close(); }
        }
    
    protected ExtendedIterator<Triple> reifierTriples( TripleMatch m )
        { return getReifier().findExposed( m ); }

	public Reifier getReifier() 
        {
		if (reifier == null) reifier = constructReifier();
		return reifier;
	    }

    protected Reifier constructReifier()
        { return new SimpleReifier( this, style ); }
    
    protected Reifier reifier = null;
    
	public final int size() 
        { checkOpen();
        int baseSize = graphBaseSize();
        int reifierSize = reifierSize();
//        String className = leafName( this.getClass().getName() );
//        System.err.println( ">> GB(" + className + ")::size = " + baseSize + "(base) + " + reifierSize + "(reifier)" );
        return baseSize + reifierSize; }
    //return baseSize;  }
	
    
    protected int reifierSize()
        { return getReifier().size(); }

    protected int graphBaseSize()
        {
		ExtendedIterator<Triple> it = GraphUtil.findAll( this );
        try 
            {
            int tripleCount = 0;
            while (it.hasNext()) { it.next(); tripleCount += 1; }
            return tripleCount;
            }
        finally
            { it.close(); }
        }

    public boolean isEmpty()
        { return size() == 0; }

	public boolean isIsomorphicWith( Graph g )
        { checkOpen();
		return g != null && GraphMatcher.equals( this, g ); }

	
	@Override
    public String toString() 
        { return toString( (closed ? "closed " : ""), this ); }
        
   
    public static String toString( String prefix, Graph that )
        {
        PrefixMapping pm = that.getPrefixMapping();
		StringBuffer b = new StringBuffer( prefix + " {" );
		String gap = "";
		ClosableIterator<Triple> it = GraphUtil.findAll( that );
		while (it.hasNext()) 
            {
			b.append( gap );
			gap = "; ";
			b.append( it.next().toString( pm ) );
		    } 
		b.append( "}" );
		return b.toString();
	   }
    
    public void sync() {}
}

/*
    (c) Copyright 2002, 2003, 2004, 2005, 2006, 2007, 2008, 2009 Hewlett-Packard Development Company, LP
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
