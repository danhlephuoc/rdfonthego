/*
 	(c) Copyright 2005, 2006, 2007, 2008, 2009 Hewlett-Packard Development Company, LP
 	All rights reserved - see end of file.
 	$Id: GraphMemFaster.java,v 1.23 2009/03/17 11:01:48 chris-dollin Exp $
*/

package com.hp.hpl.jena.mem.faster;

import com.hp.hpl.jena.graph.*;
import com.hp.hpl.jena.graph.impl.*;

import com.hp.hpl.jena.mem.*;
import com.hp.hpl.jena.shared.ReificationStyle;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

public class GraphMemFaster extends GraphMemBase
    {
    public GraphMemFaster()
        { this( ReificationStyle.Minimal ); }
    
    public GraphMemFaster( ReificationStyle style )
        { super( style ); }

    @Override protected TripleStore createTripleStore()
        { return new FasterTripleStore( this ); }

    @Override protected void destroy()
        { store.close(); }

    @Override public void performAdd( Triple t )
        { if (!getReifier().handledAdd( t )) store.add( t ); }

    @Override public void performDelete( Triple t )
        { if (!getReifier().handledRemove( t )) store.delete( t ); }

    @Override public int graphBaseSize()  
        { return store.size(); }
    
    @Override public ExtendedIterator<Triple> graphBaseFind( TripleMatch m ) 
        { return store.find( m.asTriple() ); }

    protected boolean hasReifications()
        { return reifier != null && reifier.size() > 0; }

    @Override public boolean graphBaseContains( Triple t )
        { return t.isConcrete() ? store.contains( t ) : super.graphBaseContains( t ); }
    
  
    @Override public void clear(){ 
        store.clear(); 
        ((SimpleReifier) getReifier()).clear();
    }
}


/*
 * (c) Copyright 2005, 2006, 2007, 2008, 2009 Hewlett-Packard Development Company, LP
 * All rights reserved.
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