/*
  (c) Copyright 2004, 2005, 2006, 2007, 2008, 2009 Hewlett-Packard Development Company, LP, all rights reserved.
  [See end of file]
  $Id: StoreTripleIterator.java,v 1.12 2009/03/16 16:02:27 chris-dollin Exp $
*/
package com.hp.hpl.jena.mem;

import java.util.Iterator;

import com.hp.hpl.jena.graph.*;

public class StoreTripleIterator extends TrackingTripleIterator
	{
    protected NodeToTriplesMapBase X;
    protected NodeToTriplesMapBase A;
    protected NodeToTriplesMapBase B;
    
    protected Graph toNotify;
    
    public StoreTripleIterator
        ( Graph toNotify, Iterator<Triple> it, 
          NodeToTriplesMapBase X, 
          NodeToTriplesMapBase A, 
          NodeToTriplesMapBase B )
    	{ 
        super( it ); 
        this.X = X;
        this.A = A; 
        this.B = B; 
        this.toNotify = toNotify;
        }

    @Override public void remove()
        {
        super.remove();
        X.removedOneViaIterator();
        A.remove( current );
        B.remove( current );
        toNotify.getEventManager().notifyDeleteTriple( toNotify, current );
        }
	}
