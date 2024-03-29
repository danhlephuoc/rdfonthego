package com.hp.hpl.jena.sparql.vocabulary ;

/* CVS $Id: ListPFunction.java,v 1.2 2007/01/15 09:33:57 andy_seaborne Exp $ */
 
import com.hp.hpl.jena.rdf.model.*;
 
/**
 * Vocabulary definitions from list-pfunction.ttl 
 * @author  Auto-generated by schemagen on 12 Jan 2007 16:58
 */
public class ListPFunction {
    /**
	 * <p>The RDF model that holds the vocabulary terms</p>
	 * @uml.property  name="m_model"
	 * @uml.associationEnd  
	 */
    private static Model m_model = ModelFactory.createDefaultModel();
    
    /** <p>The namespace of the vocabulary as a string</p> */
    public static final String NS = "http://jena.hpl.hp.com/ARQ/list#";
    
    /** <p>The namespace of the vocabulary as a string</p>
     *  @see #NS */
    public static String getURI() {return NS;}
    
    /**
	 * <p>The namespace of the vocabulary as a resource</p>
	 * @uml.property  name="nAMESPACE"
	 * @uml.associationEnd  
	 */
    public static final Resource NAMESPACE = m_model.createResource( NS );
    
    /**
	 * @uml.property  name="listLength"
	 * @uml.associationEnd  
	 */
    public static final Property listLength = m_model.createProperty( "http://jena.hpl.hp.com/ARQ/list#listLength" );
    
    /**
	 * <p>Length relationship of a list [subject] to a length [object]</p>
	 * @uml.property  name="length"
	 * @uml.associationEnd  
	 */
    public static final Property length = m_model.createProperty( "http://jena.hpl.hp.com/ARQ/list#length" );
    
    /**
	 * @uml.property  name="listIndex"
	 * @uml.associationEnd  
	 */
    public static final Property listIndex = m_model.createProperty( "http://jena.hpl.hp.com/ARQ/list#listIndex" );
    
    /**
	 * <p>Index relationship of a list [subject] to a query list (index member)</p>
	 * @uml.property  name="index"
	 * @uml.associationEnd  
	 */
    public static final Property index = m_model.createProperty( "http://jena.hpl.hp.com/ARQ/list#index" );
    
    /**
	 * @uml.property  name="listMember"
	 * @uml.associationEnd  
	 */
    public static final Property listMember = m_model.createProperty( "http://jena.hpl.hp.com/ARQ/list#listMember" );
    
    /**
	 * <p>Membership relationship of a list [subject] to a member [object] c.f. rdfs:member</p>
	 * @uml.property  name="member"
	 * @uml.associationEnd  
	 */
    public static final Property member = m_model.createProperty( "http://jena.hpl.hp.com/ARQ/list#member" );
    
}
