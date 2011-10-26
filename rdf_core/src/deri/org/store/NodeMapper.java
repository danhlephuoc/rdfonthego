package deri.org.store;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.util.iterator.Map1;

public class NodeMapper implements Map1<TripleId, Node> {
	NodeStore nodeStore;
	private final int	prefix;
	
	public NodeMapper(NodeStore nodes, int pre){
		nodeStore 	= 	nodes;
		prefix		=	pre;
	}
	
	@Override
	public Node map1(TripleId tid) {
		Node node;
		switch (prefix){
			case 1: 
				node	=	nodeStore.getNodeById(tid.sid);
				return node;
			case 2: 
				node	=	nodeStore.getNodeById(tid.pid);
				return node;
			case 3: 
				node	=	nodeStore.getNodeById(tid.oid);
				return node;
			default:
				return Node.ANY;
		}
	}

}
