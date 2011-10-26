/*
  (c) Copyright 2003, 2004, 2005, 2006, 2007, 2008, 2009 Hewlett-Packard Development Company, LP
  [See end of file]
  $Id: Graph.java,v 1.34 2009/01/26 08:37:07 chris-dollin Exp $
*/

package com.hp.hpl.jena.graph;

import com.hp.hpl.jena.graph.impl.GraphBase;
import com.hp.hpl.jena.shared.*;
import com.hp.hpl.jena.util.iterator.*;

/**
 * The interface to be satisfied by implementations maintaining collections of RDF triples.
 * The core interface is small (add, delete, find, contains) and is augmented by additional classes to handle 
 * more complicated matters such as reification, query handling, bulk update, event management, and transaction handling. 
 * <p> For <code>add(Triple)</code> see GraphAdd.
 * @author  Jeremy Carroll, Chris Dollin
 */
public interface Graph  extends GraphAdd
    {
    
    public static final Graph emptyGraph = new GraphBase(){ 
    	@Override
        public ExtendedIterator<Triple> graphBaseFind( TripleMatch tm ) 
        { return Triple.None; } 
    };
    	
        boolean dependsOn( Graph other );
        
        BulkUpdateHandler getBulkUpdateHandler();
    
        GraphEventManager getEventManager(); 
        
        Reifier getReifier();
    
        PrefixMapping getPrefixMapping();
        
        void delete(Triple t) throws DeleteDeniedException;
      
        ExtendedIterator<Triple> find(TripleMatch m);
    
        ExtendedIterator<Triple> find(Node s,Node p,Node o);
    
        boolean isIsomorphicWith(Graph g);
        
        boolean contains( Node s, Node p, Node o );
    
        boolean contains( Triple t );
        
        void close();
    
        boolean isEmpty();
        
        int size();

        boolean isClosed();
        
        

		void sync();
        
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
