/*
 * (c) Copyright 2010 Talis Systems Ltd.
 * All rights reserved.
 * [See end of file]
 */

package com.hp.hpl.jena.sparql.expr.aggregate;

import java.util.HashMap ;
import java.util.Map ;

import com.hp.hpl.jena.graph.Node ;
import com.hp.hpl.jena.sparql.ARQInternalErrorException ;
import com.hp.hpl.jena.sparql.engine.binding.Binding ;
import com.hp.hpl.jena.sparql.engine.binding.BindingKey ;
import com.hp.hpl.jena.sparql.expr.NodeValue ;
import com.hp.hpl.jena.sparql.function.FunctionEnv ;

/** Aggregate that does everything except the per-group aggregation that is needed for each operation */  
public abstract class AggregatorBase implements Aggregator 
{
    // Aggregator -- handles one aggregation over one group, and is the syntax unit.
    
    // AggregateFactory -- delays the creating of Aggregator so multiple mentions over the same group gives the same Aggregator
    
    // Accumulator -- the per-group, per-key accumulator for the aggregate
    // queries track their aggregators so if one is used twice, the calculataion is only done once.
    // For distinct, that means only uniquefier. 
    
    // Built-ins: COUNT, SUM, MIN, MAX, AVG, GROUP_CONCAT, and SAMPLE
    // but COUNT(*) and COUNT(Expr) are different beasts
    // each in DISTINCT and non-DISTINCT versions 
    
    protected AggregatorBase() {}
    
    private Map<BindingKey, Accumulator> buckets = new HashMap<BindingKey, Accumulator>() ;   // Bindingkey => Accumulator

    final
    public void accumulate(BindingKey key, Binding binding, FunctionEnv functionEnv)
    {
        Accumulator acc = buckets.get(key) ;
        if ( acc == null )
        {
            acc = createAccumulator() ;
            buckets.put(key, acc) ;
        }
        acc.accumulate(binding, functionEnv) ;
    }

    protected abstract Accumulator createAccumulator() ;
    
    public abstract Node getValueEmpty() ;

    public Node getValue(BindingKey key)
    {
        Accumulator acc = buckets.get(key) ;
        if ( acc == null )
            throw new ARQInternalErrorException("Null for accumulator") ;
        NodeValue nv = acc.getValue();
        if ( nv == null ) 
            return null ;
        return nv.asNode() ;
    }
    
    public String key() {  return toPrefixString() ; }
    
//    @Override
//    public abstract String toString() ;

    public abstract String toPrefixString() ;
}

/*
 * (c) Copyright 2010 Talis Systems Ltd.
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