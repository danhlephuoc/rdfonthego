/*
  (c) Copyright 2003, 2004, 2005, 2006, 2007, 2008, 2009 Hewlett-Packard Development Company, LP
  [See end of file]
  $Id: ModelChangedListener.java,v 1.17 2009/01/28 14:37:08 chris-dollin Exp $
*/

package com.hp.hpl.jena.rdf.model;

import java.util.*;

/**
    The interface for classes that listen for model-changed events. In all cases,
    the argument is [a copy of] the item that has been presented to the model,
    or its underlying graph, for addition or removal. For an add, the item [or parts
    of that item] may have already been present in the model; for remove, the
    item [or parts of it] need not have been absent from the item.
<p>
    NOTE that the listener is supplied with more-or-less faithful copies of the
    original items that were added to, or removed from, the model. In particular,
    graph-level updates to the model appear as statements, not triples. 
    
 	@author kers (design by andy & the team)
*/
public interface ModelChangedListener
    {
		void addedStatement( Statement s );
		void addedStatements( Statement [] statements );
		void addedStatements( List<Statement> statements );
		void addedStatements( StmtIterator statements );
		void addedStatements( Model m );
		void removedStatement( Statement s );
		void removedStatements( Statement [] statements );
		void removedStatements( List<Statement> statements );
		void removedStatements( StmtIterator statements );
		void removedStatements( Model m );
		void notifyEvent( Model m, Object event );
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