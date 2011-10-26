/*
 * (c) Copyright 2009 Hewlett-Packard Development Company, LP
 * All rights reserved.
 * [See end of file]
 */

package com.hp.hpl.jena.sparql.mgt;

import java.util.concurrent.atomic.AtomicLong ;

import com.hp.hpl.jena.query.Query ;
import com.hp.hpl.jena.sparql.algebra.Op ;
import com.hp.hpl.jena.sparql.util.Utils ;

/**
 * @author  Mr_Anh
 */
public class QueryEngineInfo implements QueryEngineInfoMBean
{
    // Has to be careful about concurrency.
    // It is possible that the count may be momentarily wrong
    // (reading longs is not atomic).
    
    private AtomicLong count = new AtomicLong(0) ;
    public long getQueryCount()                 { return count.get() ; }
    public void incQueryCount()                 { count.incrementAndGet() ; }
    
    /**
	 * @uml.property  name="query"
	 * @uml.associationEnd  
	 */
    Query query = null ;
    public String getLastQueryString()
    { 
        Query q = query ;    // Get once.
        if ( q != null ) return q.toString() ;
        // Sometimes an alegra expression is executited without a query.
        return getLastAlgebra() ;
    }
    public void setLastQueryString(Query q)     { query = q ; }

    /**
	 * @uml.property  name="op"
	 * @uml.associationEnd  
	 */
    private Op op = null ;
    public String getLastAlgebra()
    {
        Op _op = op ;   // Get once.
        return _op == null ? "none" : _op.toString() ;
    }
    public void setLastOp(Op op)                { this.op = op ; }

    private String timeSeen = "" ;
    public String getLastQueryExecAt()          { return timeSeen ; }
    public void setLastQueryExecAt()            { timeSeen = Utils.nowAsString() ; }

//    private long lastExecTime ;
//    public long getLastQueryExecTime()          { return lastExecTime ; }
//    public void setLastQueryExecTime(long timeMillis)   { lastExecTime = timeMillis ; }
    
    
}

/*
 * (c) Copyright 2009 Hewlett-Packard Development Company, LP
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