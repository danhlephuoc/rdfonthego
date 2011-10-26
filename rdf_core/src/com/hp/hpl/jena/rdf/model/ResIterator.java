

package com.hp.hpl.jena.rdf.model;

import com.hp.hpl.jena.util.iterator.*;

/** An iterator which returns RDF Resources.
 *
 * <p>RDF iterators are standard Java iterators, except that they
 *    have an extra method that returns a specifically typed object,
 *    in this case RDF Resource, and have a <CODE>close()</CODE> method
 *    that should be called to free resources if the caller does not
 *    complete the iteration.</p>
 * @author bwm
 * @version Release='$Name:  $' Revision='$Revision: 1.10 $' Date='$Date: 2009/01/26 08:37:09 $'
 */
public interface ResIterator extends ExtendedIterator<Resource> 
    {
    public Resource nextResource();
    }