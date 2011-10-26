package com.hp.hpl.jena.rdf.model;

import com.hp.hpl.jena.graph.Factory;
import com.hp.hpl.jena.graph.Graph;
import com.hp.hpl.jena.rdf.model.impl.ModelCom;
import com.hp.hpl.jena.rdf.model.impl.ModelReifier;

import deri.org.store.BDBGraph;


public class ModelFactory {
	
	public static Model createDefaultModel(  ){
		return createModelForGraph(Factory.createGraphMem()); 
	}
	
	public static Model createModelForGraph( Graph g ) {
        return new ModelCom( g );
    }
	
	public static Model creatModelStore(String storeName){
		return new ModelCom(new BDBGraph(storeName));
	}
	public static Model   withHiddenStatements( Model m )
	{ return ModelReifier.withHiddenStatements( m ); }
}
