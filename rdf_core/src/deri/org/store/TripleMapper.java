package deri.org.store;

import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.util.iterator.Map1;

public class TripleMapper implements Map1<TripleId, Triple> {
	NodeStore nodeStore;
	
	public TripleMapper(NodeStore node){
		nodeStore = node;
	}
	
	@Override
	public Triple map1(TripleId o) {
		return new Triple(nodeStore.getNodeById(o.sid),nodeStore.getNodeById(o.pid),nodeStore.getNodeById(o.oid)); 
	}
}
