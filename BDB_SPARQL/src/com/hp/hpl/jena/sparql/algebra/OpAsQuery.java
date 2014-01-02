/*
 * (c) Copyright 2007, 2008, 2009 Hewlett-Packard Development Company, LP
 * All rights reserved.
 * [See end of file]
 */

package com.hp.hpl.jena.sparql.algebra;

import java.util.Iterator;
import java.util.Stack;

import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.sparql.ARQNotImplemented;
import com.hp.hpl.jena.sparql.algebra.op.*;
import com.hp.hpl.jena.sparql.core.BasicPattern;
import com.hp.hpl.jena.sparql.core.Var;
import com.hp.hpl.jena.sparql.expr.Expr;
import com.hp.hpl.jena.sparql.expr.ExprList;
import com.hp.hpl.jena.sparql.syntax.Element;
import com.hp.hpl.jena.sparql.syntax.ElementFilter;
import com.hp.hpl.jena.sparql.syntax.ElementGroup;
import com.hp.hpl.jena.sparql.syntax.ElementNamedGraph;
import com.hp.hpl.jena.sparql.syntax.ElementOptional;
import com.hp.hpl.jena.sparql.syntax.ElementTriplesBlock;
import com.hp.hpl.jena.sparql.syntax.ElementUnion;

/** Convert an Op expression in SPARQL syntax, that is, the reverse of algebra generation */   
public class OpAsQuery
{
    public static Query asQuery(Op op)
    {
        Query query = QueryFactory.make() ;
        query.setQueryResultStar(true) ;    // SELECT *
        
        Converter v = new Converter(query) ;
        //OpWalker.walk(op, v) ;
        op.visit(v) ;
        
        query.setQueryPattern(v.currentGroup) ;
        query.setQuerySelectType() ;
        
        query.setResultVars() ; 
        return query ; 
    }
    
    /**
	 * @author  Mr_Anh
	 */
    public static class Converter implements OpVisitor
    {
        /**
		 * @uml.property  name="query"
		 * @uml.associationEnd  
		 */
        private Query query ;
        /**
		 * @uml.property  name="currentGroup"
		 * @uml.associationEnd  
		 */
        private ElementGroup currentGroup = null ;
        private Stack<ElementGroup> stack = new Stack<ElementGroup>() ;
        
        public Converter(Query query)
        {
            this.query = query ;
            currentGroup = new ElementGroup() ;
        }

        Element asElement(Op op)
        {
            ElementGroup g = asElementGroup(op) ;
            if ( g.getElements().size() == 1 )
                return g.getElements().get(0) ;
            return g ;
        }
        
        ElementGroup asElementGroup(Op op)
        {
            startSubGroup() ;
            op.visit(this) ;
            return endSubGroup() ;
        }

        public void visit(OpBGP opBGP)
        {
            currentGroup().addElement(process(opBGP.getPattern())) ;
        }

      /*public void visit(OpPropFunc opPropFunc)
       {
           OpBGP opBGP = opPropFunc.getBGP() ;
           currentGroup().addElement(process(opBGP.getPattern())) ;
       }*/
        
        public void visit(OpTriple opTriple)
        { currentGroup().addElement(process(opTriple.getTriple())) ; }

        public void visit(OpProcedure opProcedure)
        {
            throw new ARQNotImplemented("OpProcedure") ;
        }
        
       public void visit(OpPropFunc opPropFunc)
        {
            throw new ARQNotImplemented("OpPropFunc") ;
        }
        
        
        public void visit(OpSequence opSequence)
        {
            throw new ARQNotImplemented("OpSequence") ;
        }
        
        private ElementTriplesBlock process(BasicPattern pattern)
        {
            ElementTriplesBlock e = new ElementTriplesBlock() ;
            for (Triple t : pattern)
                // Leave bNode variables as they are
                // Query serialization will deal with them. 
                e.addTriple(t) ;
            return e ;
        }
        
        private ElementTriplesBlock process(Triple triple)
        {
            // Unsubtle
            ElementTriplesBlock e = new ElementTriplesBlock() ;
            e.addTriple(triple) ;
            return e ;
        }
        
//        public void visit(OpQuadPattern quadPattern)
//        { throw new ARQNotImplemented("OpQuadPattern") ; }

        /*public void visit(OpPath opPath)
        { throw new ARQNotImplemented("OpPath") ; }*/

        public void visit(OpJoin opJoin)
        {
            // Keep things clearly separated.
            Element eLeft = asElement(opJoin.getLeft()) ;
            Element eRight = asElementGroup(opJoin.getRight()) ;
            
            ElementGroup g = currentGroup() ;
            g.addElement(eLeft) ;
            g.addElement(eRight) ;
            return ;
        }

        private static boolean emptyGroup(Element element)
        {
            if ( ! ( element instanceof ElementGroup ) )
                return false ;
            ElementGroup eg = (ElementGroup)element ;
            return eg.isEmpty() ;
        }
        
        public void visit(OpLeftJoin opLeftJoin)
        {
            Element eLeft = asElement(opLeftJoin.getLeft()) ;
            ElementGroup eRight = asElementGroup(opLeftJoin.getRight()) ;
            
            if ( opLeftJoin.getExprs() != null )
            {
                for ( Expr expr : opLeftJoin.getExprs() )
                {
                    ElementFilter f = new ElementFilter(expr) ;
                    eRight.addElement(f) ;
                }
            }
            ElementGroup g = currentGroup() ;
            if ( ! emptyGroup(eLeft) )
                g.addElement(eLeft) ;
            ElementOptional opt = new ElementOptional(eRight) ;
            g.addElement(opt) ;
        }

        public void visit(OpDiff opDiff)
        { throw new ARQNotImplemented("OpDiff") ; }

        public void visit(OpUnion opUnion)
        {
            Element eLeft = asElementGroup(opUnion.getLeft()) ;
            Element eRight = asElementGroup(opUnion.getRight()) ;
            if ( eLeft instanceof ElementUnion )
            {
                ElementUnion elUnion = (ElementUnion)eLeft ;
                elUnion.addElement(eRight) ;
                return ;
            }
            
//            if ( eRight instanceof ElementUnion )
//            {
//                ElementUnion elUnion = (ElementUnion)eRight ;
//                elUnion.getElements().add(0, eLeft) ;
//                return ;
//            }
            
            ElementUnion elUnion = new ElementUnion() ;
            elUnion.addElement(eLeft) ;
            elUnion.addElement(eRight) ;
            currentGroup().addElement(elUnion) ;
        }

        public void visit(OpConditional opCondition)
        { throw new ARQNotImplemented("OpCondition") ; }

        public void visit(OpFilter opFilter)
        {
            // (filter .. (filter ( ... ))   (non-canonicalizing OpFilters)
            // Inner gets Grouped unnecessarily. 
            Element e = asElement(opFilter.getSubOp()) ;
            if ( currentGroup() != e )
                currentGroup().addElement(e) ;
            currentGroup();
            
            ExprList exprs = opFilter.getExprs() ;
            for ( Expr expr : exprs )
            {
                ElementFilter f = new ElementFilter(expr) ;
                currentGroup().addElement(f) ;
            }
        }

        public void visit(OpGraph opGraph)
        {
            startSubGroup() ;
            Element e = asElement(opGraph.getSubOp()) ;
            endSubGroup();
            
            Element graphElt = new ElementNamedGraph(opGraph.getNode(), e) ;
            currentGroup().addElement(graphElt) ;
        }

       /* public void visit(OpService opService)
        { throw new ARQNotImplemented("OpService") ; }*/

//        public void visit(OpDatasetNames dsNames)
//        { throw new ARQNotImplemented("OpDatasetNames") ; }

        public void visit(OpTable opTable)
        { 
            // This will go in a group so simply forget it. 
            if ( opTable.isJoinIdentity() ) return ;
            throw new ARQNotImplemented("OpTable") ;
        }

       /* public void visit(OpExt opExt)
        { throw new ARQNotImplemented("OpExt") ; }*/

        public void visit(OpNull opNull)
        { throw new ARQNotImplemented("OpNull") ; }

        public void visit(OpLabel opLabel)
        { throw new ARQNotImplemented("OpLabel") ; }

        public void visit(OpAssign opAssign)
        { throw new ARQNotImplemented("OpAssign") ; }

    /*    public void visit(OpList opList)
        {  No action  }*/

       /* public void visit(OpOrder opOrder)
        {
            List<SortCondition> x = opOrder.getConditions() ;
            for ( SortCondition sc : x )
                query.addOrderBy(sc);
            opOrder.getSubOp().visit(this) ;
        }*/

        public void visit(OpProject opProject)
        {
            query.setQueryResultStar(false) ;
            Iterator<Var> iter = opProject.getVars().iterator() ;
            for ( ; iter.hasNext() ; )
            {
                Var v = iter.next();
                query.addResultVar(v) ;
            }
            opProject.getSubOp().visit(this) ;
        }

      /*  public void visit(OpReduced opReduced)
        { 
            query.setReduced(true) ;
            opReduced.getSubOp().visit(this) ;
        }*/

        public void visit(OpDistinct opDistinct)
        { 
            query.setDistinct(true) ;
            opDistinct.getSubOp().visit(this) ;
        }

        /*public void visit(OpSlice opSlice)
        {
            if ( opSlice.getStart() != Query.NOLIMIT )
                query.setOffset(opSlice.getStart()) ;
            if ( opSlice.getLength() != Query.NOLIMIT )
                query.setLimit(opSlice.getLength()) ;
            opSlice.getSubOp().visit(this) ;
        }*/

        /*public void visit(OpGroupAgg opGroupAgg)
        { throw new ARQNotImplemented("OpGroupAgg") ; }*/
        
        private void startSubGroup()
        {
            push(currentGroup) ;
            ElementGroup g = new ElementGroup() ;
            currentGroup = g ;
        }
        
        private ElementGroup endSubGroup()
        {
            ElementGroup g = pop() ;
            ElementGroup r = currentGroup ;
            currentGroup = g ;
            return r ;
        }
        
//        private void endCurrentGroup()
//        {
//            currentGroup = null ;
//            element = null ; //??
//        }
        
        private ElementGroup currentGroup()
        {
//            if ( currentGroup == null )
//                startSubGroup() ;
            return currentGroup ;
        }
        
        private ElementGroup pop() { return stack.pop(); }
        private void push(ElementGroup el) { stack.push(el); }
    }
}

/*
 * (c) Copyright 2007, 2008, 2009 Hewlett-Packard Development Company, LP
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