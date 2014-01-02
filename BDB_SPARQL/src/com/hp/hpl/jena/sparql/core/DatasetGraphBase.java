/*
 * (c) Copyright 2009 Hewlett-Packard Development Company, LP
 * (c) Copyright 2010 Talis Information Ltd.
 * All rights reserved.
 * [See end of file]
 */

package com.hp.hpl.jena.sparql.core;


import java.util.Iterator ;
import java.util.List ;

//import org.openjena.atlas.io.IndentedLineBuffer ;
//import org.openjena.atlas.iterator.Iter ;
//import org.openjena.atlas.iterator.Transform ;

import com.hp.hpl.jena.graph.Graph ;
import com.hp.hpl.jena.graph.Node ;
import com.hp.hpl.jena.graph.Triple ;
import com.hp.hpl.jena.shared.Lock ;
import com.hp.hpl.jena.shared.LockMRSW ;
//import com.hp.hpl.jena.sparql.sse.writers.WriterGraph ;
import com.hp.hpl.jena.sparql.util.Context ;

/**
 * <p>DatasetGraph framework : readonly dataset need only provide find(g,s,p,o), getGraph() and getDefaultGraph() although it may wish to override other operations and do better.</p> <p>Other implementations include:</p> <ul> <li>  {@link DatasetGraphBase}   that adds an implementation of find based on default / named graphs.</li> <li>  {@link DatasetGraphCollection}   that adds mutating quad operations mapped to a collection of graphs.</li> <li>  {@link DatasetGraphQuad}   that maps graph operations to a quad view. </ul>
 */
abstract public class DatasetGraphBase implements DatasetGraph
{
    /**
	 * @uml.property  name="lock"
	 * @uml.associationEnd  
	 */
    private final Lock lock = new LockMRSW() ;
    /**
	 * @uml.property  name="context"
	 * @uml.associationEnd  
	 */
    private Context context = new Context() ;
    
    protected DatasetGraphBase() {}
    
//    //@Override
//    public boolean containsGraph(Node graphNode)
//    { return contains(graphNode, Node.ANY, Node.ANY, Node.ANY) ; }
    
    //@Override
    /**
	 * @uml.property  name="defaultGraph"
	 * @uml.associationEnd  
	 */
    public abstract Graph getDefaultGraph() ;

    //@Override
    public abstract Graph getGraph(Node graphNode) ;

    //@Override
//    public void addGraph(Node graphName, Graph graph)
//    { throw new UnsupportedOperationException("DatasetGraph.addGraph") ; }

    //@Override
//    public void removeGraph(Node graphName)
//    { throw new UnsupportedOperationException("DatasetGraph.removeGraph") ; }

    //@Override
    /**
	 * @param  g
	 * @uml.property  name="defaultGraph"
	 */
    public void setDefaultGraph(Graph g)
    { throw new UnsupportedOperationException("DatasetGraph.setDefaultGraph") ; }
    
    //@Override
//    public void add(Quad quad) { throw new UnsupportedOperationException("DatasetGraph.add(Quad)") ; } 
    
    //@Override
//    public void delete(Quad quad) { throw new UnsupportedOperationException("DatasetGraph.delete(Quad)") ; }
    
    //@Override
    /** Simple implementation */
//    public void deleteAny(Node g, Node s, Node p, Node o)
//    { 
//        Iterator<Quad> iter = find(g, s, p, o) ;
//        List<Quad> list = Iter.toList(iter) ;
//        for ( Quad q : list )
//            delete(q) ;
//    }
    
//    //@Override
//    public Iterator<Quad> find(Quad quad)
//    { return find(quad.getGraph(), quad.getSubject(), quad.getPredicate(), quad.getObject()) ; }
    
//    /** Implementation of find based on spltting into triples (default graph) and quads (named graph) */
//    //@Override
//    public Iterator<Quad> find(Node g, Node s, Node p , Node o)
//    {
//        if ( ! isWildcard(g) )
//        {
//            if ( Quad.isDefaultGraph(g))
//                return findInDftGraph(s,p,o) ;
//            Iterator<Quad> qIter = findInNamedGraphs(g, s, p, o) ;
//            if ( qIter == null )
//                return Iter.nullIterator() ;
//            return qIter ;
//        }
//        
//        return findAny(s, p, o) ;
//    }
//    
//    public Iterator<Quad> findAny(Node s, Node p , Node o) 
//    {
//        // Pass g in for symmetry and to "pass" to findInNamedGraphs
//        // Default graph
//        Iterator<Quad> iter1 = findInDftGraph(s, p, o) ;
//        Iterator<Quad> iter2 = findInNamedGraphs(Node.ANY, s, p, o) ;
//        
//        if ( iter1 ==null && iter2 == null )
//            return Iter.nullIterator() ;
//        if ( iter1 == null )
//            return iter2 ;
//        if ( iter2 == null )
//            return iter1 ;
//        return Iter.append(iter1, iter2) ;
//    }
//
//    protected abstract Iterator<Quad> findInDftGraph(Node s, Node p , Node o) ;
//    protected abstract Iterator<Quad> findInNamedGraphs(Node g, Node s, Node p , Node o) ;
//
//    protected static Iterator<Quad> triples2quadsDftGraph(Iterator<Triple> iter)
//    {
//        return triples2quads(Quad.tripleInQuad, iter) ;
//    }
//    
//    protected static Iter<Quad> triples2quads(final Node graphNode, Iterator<Triple> iter)
//    {
//        Transform<Triple, Quad> transformNamedGraph = new Transform<Triple, Quad> () {
//            public Quad convert(Triple triple)
//            {
//                return new Quad(graphNode, triple) ;
//            }
//        } ;
//            
//        return Iter.iter(iter).map(transformNamedGraph) ;
//    }


    //@Override
//    public boolean contains(Quad quad) { return contains(quad.getGraph(), quad.getSubject(), quad.getPredicate(), quad.getObject()) ; }

//    //@Override
//    public boolean contains(Node g, Node s, Node p , Node o)
//    {
//        Iterator<Quad> iter = find(g, s, p, o) ;
//        boolean b = iter.hasNext() ;
//        Iter.close(iter) ;
//        return b ;
//    }
    
    protected static boolean isWildcard(Node g)
    {
        return g == null || g == Node.ANY ;
    }
    
//    //@Override
//    public boolean isEmpty()
//    {
//        return ! contains(Node.ANY, Node.ANY, Node.ANY, Node.ANY) ;
//    }

    //@Override
    public long size() { return -1 ; } 
    
    //@Override
    /**
	 * @return
	 * @uml.property  name="lock"
	 */
    public Lock getLock()
    {
        return lock ;
    }
    
    /**
	 * @return
	 * @uml.property  name="context"
	 */
    public Context getContext()
    {
        return context ;
    }
    
    //@Override
    public void close()
    { }
    
//    @Override
//    public String toString()
//    {
//        // Using the size of the graphs would be better.
//        IndentedLineBuffer out = new IndentedLineBuffer() ;
////        WriterGraph.output(out, this, null) ;
//        return out.asString() ;
//    }

    // Helpers
    
//    protected static Iterator<Quad> triples2quadsDftGraph(Iterator<Triple> iter)
//    {
//        return triples2quads(Quad.tripleInQuad, iter) ;
//    }
//
//    protected static Iter<Quad> triples2quads(final Node graphNode, Iterator<Triple> iter)
//    {
//        Transform<Triple, Quad> transformNamedGraph = new Transform<Triple, Quad> () {
//            public Quad convert(Triple triple)
//            {
//                return new Quad(graphNode, triple) ;
//            }
//        } ;
//
//        return Iter.iter(iter).map(transformNamedGraph) ;
//    }

    

}

/*
 * (c) Copyright 2009 Hewlett-Packard Development Company, LP
 * (c) Copyright 2010 Talis Information Ltd.
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