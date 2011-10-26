/*
  (c) Copyright 2002, 2002, 2003, 2004, 2005, 2006, 2007, 2008, 2009 Hewlett-Packard Development Company, LP
  [See end of file]
  $Id: EnhGraph.java,v 1.24 2009/01/26 10:28:22 chris-dollin Exp $
*/

package com.hp.hpl.jena.enhanced;

import com.hp.hpl.jena.graph.*;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.util.cache.Cache;
import com.hp.hpl.jena.util.cache.CacheControl;
import com.hp.hpl.jena.util.cache.CacheManager;


/**
 * TODO: remove the polymorphic aspect of EnhGraphs. <p> A specialisation of Polymorphic that models an extended graph - that is, one that  contains {@link EnhNode  Enhanced nodes}  or one that itself exposes additional  capabilities beyond the graph API. <p>    <span style="color:red">WARNING</span>. The polymorphic aspects of EnhGraph  are <span style="color:red">not supported</span> and are not expected to be supported in this way for the indefinite future.
 * @author  <a href="mailto:Jeremy.Carroll@hp.com">Jeremy Carroll</a> (original code)  <br><a href="mailto:Chris.Dollin@hp.com">Chris Dollin</a> (original code)  <br><a href="mailto:Ian.Dickinson@hp.com">Ian Dickinson</a>   (refactoring and commentage)
 */

public class EnhGraph 
//    extends Polymorphic 
{
    protected Graph graph;
    
    /** Counter that helps to ensure that caches are kept distinct */
    static private int cnt = 0;

    protected Cache enhNodes = CacheManager.createCache( CacheManager.ENHNODECACHE, "EnhGraph-" + cnt++, 1000 );
  
    private Personality<RDFNode> personality;
    
    public EnhGraph( Graph g, Personality<RDFNode> p ) {
        super();
        graph = g;
        personality = p;
    }
   
    public Graph asGraph() {
        return graph;
    }
   
    @Override final public int hashCode() {
     	return graph.hashCode();
    }

     
    @Override final public boolean equals(Object o) {
        return 
            this == o 
            || o instanceof EnhGraph && graph.equals(((EnhGraph) o).asGraph());
    }
    
      final public boolean isIsomorphicWith(EnhGraph eg){
        return graph.isIsomorphicWith(eg.graph);
    }
   public <X extends RDFNode> X getNodeAs( Node n, Class<X> interf ) 
        {
        EnhNode eh = (EnhNode) enhNodes.get( n );
        if (eh == null)
            {           
            X constructed = personality.newInstance( interf, n, this );
            enhNodes.put( n, constructed );        
            return constructed;
            }
        else
            return eh.viewAs( interf );
        }
    
     public CacheControl getNodeCacheControl() {
         return enhNodes;
    }
    
      public void setNodeCache(Cache cc) {
         enhNodes = cc;
    }
     
    protected Personality<RDFNode> getPersonality() {
        return personality;
    }
    
}

/*
    (c) Copyright 2002, 2002, 2003, 2004, 2005, 2006, 2007, 2008, 2009 Hewlett-Packard Development Company, LP
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
