package deri.org.store;

import android.util.Pair;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.hp.hpl.jena.util.iterator.Map1;
import com.hp.hpl.jena.util.iterator.Map1Iterator;

public class ObId extends NodeIndexBase {
	private static final String OPS_INDEX	=	"OPS_INDEX";
	
	BTreeList OPS;
			
	public ObId(NodeStore nodeStore,Configuration conf){
		super(nodeStore,conf);
		OPS	=	getBTreeList(OPS_INDEX);
	}
	
	public void add(TripleId tripleId) {
		OPS.put(tripleId.oid, tripleId.pid, tripleId.sid);
	}

	public void delete(TripleId tripleId){
		OPS.delete(tripleId.oid, tripleId.pid, tripleId.sid);
	}
	
	public void sync(){
		OPS.sync();
	}
	
	public ExtendedIterator<Triple> find(final Triple t) {
		if(!t.getObject().equals(Node.ANY)){ //Fixed Object
			Integer oid=nodeStore.getIdByNode(t.getObject());
			if(oid!=NodeStore.NODE_NOT_EXIST){
			    if(t.getSubject().equals(Node.ANY))	{//variable in Subject
			    	if(t.getPredicate().equals(Node.ANY)){//variable in Predicate (? ? O)
			    		return new Map1Iterator<Pair<Integer,Integer>, Triple>(new Map1<Pair<Integer,Integer>, Triple>(){
			    				@Override
							public Triple map1(Pair<Integer,Integer> pair) {
							
			    				Triple td =  new Triple(nodeStore.getNodeById(pair.second),
										          nodeStore.getNodeById(pair.first),
										          t.getObject());
			    			
			    				return td;
							}
		    			},
		    			OPS.getPair(oid));
			    	}
			    }
			}
		}
		return Triple.None;
	}

	public void close() {
		OPS.close();
		
	}

}
