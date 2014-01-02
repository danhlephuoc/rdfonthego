/*
 * (c) Copyright 2007, 2008, 2009 Hewlett-Packard Development Company, LP
 * (c) Copyright 2010 Talis Systems Ltd.
 * All rights reserved.
 * [See end of file]
 */

package com.hp.hpl.jena.sparql.expr.aggregate;

import com.hp.hpl.jena.sparql.engine.Renamer ;
import com.hp.hpl.jena.sparql.expr.Expr ;
//import com.hp.hpl.jena.sparql.sse.writers.WriterExpr ;
import com.hp.hpl.jena.sparql.util.ExprUtils ;

public class AggMinDistinct extends AggMin
{
    // ---- MIN(DISTINCT expr)
    // Same as MIN(expr) but remembers the syntax
    
    public AggMinDistinct(Expr expr) { super(expr) ; }
    @Override
    public Aggregator copy() { return new AggMinDistinct(expr) ; }
    @Override
    public Aggregator copyRename(Renamer renamer) { return new AggMinDistinct(expr.copySubstitute(null, false, renamer)) ; }

//    @Override
//    public String toString() { return "min(distinct "+ExprUtils.fmtSPARQL(getExpr())+")" ; }
    @Override
    public String toPrefixString() { return "(min distinct ";}//+WriterExpr.asString(getExpr())+")" ; }

    @Override
    public boolean equalsAsExpr(Aggregator other)
    {
        if ( ! ( other instanceof AggMinDistinct ) )
            return false ;
        return super.equalsAsExpr(other) ;
    } 
}

/*
 * (c) Copyright 2007, 2008, 2009 Hewlett-Packard Development Company, LP
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