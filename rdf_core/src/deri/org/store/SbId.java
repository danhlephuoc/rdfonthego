package deri.org.store;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.hp.hpl.jena.util.iterator.Map1;
import com.hp.hpl.jena.util.iterator.Map1Iterator;

import android.util.Pair;

public class SbId extends NodeIndexBase{
	private static final String	SOP_INDEX	=	"SOP_INDEX";
	BTreeList	SOP;
	
	public SbId(NodeStore nodeStore, Configuration conf){
		super(nodeStore,conf);
		SOP	=	getBTreeList(SOP_INDEX);
	}
	
	public void add(TripleId tripleId) {
		SOP.put(tripleId.sid, tripleId.oid, tripleId.pid);
	}
 
	public ExtendedIterator<Triple> find(final Triple t) {
		if(!t.getSubject().equals(Node.ANY)){ 
			int sid=nodeStore.getIdByNode(t.getSubject());
			if(sid!=NodeStore.NODE_NOT_EXIST){
			    if(t.getPredicate().equals(Node.ANY)){	
			    	if(t.getObject().equals(Node.ANY)){	
			    		Map1<Pair<Integer,Integer>,Triple> map = new Map1<Pair<Integer,Integer>, Triple>(){
							@Override
							public Triple map1(Pair<Integer,Integer> pair){
								Triple td	=	new Triple(t.getSubject(),
								          nodeStore.getNodeById(pair.second),
								          nodeStore.getNodeById(pair.first));
					
								return td;
							}
						};
						
						return new Map1Iterator<Pair<Integer,Integer>,Triple>(map,SOP.getPair(sid));
			    	
			    	}
			    	else{ 
			    		int oid= nodeStore.getIdByNode(t.getObject());
			    		if(oid!=NodeStore.NODE_NOT_EXIST){			    			
			    			return new Map1Iterator<Integer, Triple>(new Map1<Integer, Triple>(){

								@Override
								public Triple map1(Integer pid) {
									Triple td = new Triple(t.getSubject(),
											          nodeStore.getNodeById(pid),
											          t.getObject());
									
									return td;
								}
			    			},
			    			SOP.get(sid, oid));
			    		}
			    	}
			    }
			}
		}
		return Triple.None;
	}

	public void delete(TripleId tripleId){
		SOP.delete(tripleId.sid, tripleId.oid, tripleId.pid);
	}
	
	public void close(){
		SOP.close();
	}

	public void sync() {
	
		
	}
}
