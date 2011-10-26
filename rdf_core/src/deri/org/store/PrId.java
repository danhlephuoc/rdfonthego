package deri.org.store;

import android.util.Pair;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.hp.hpl.jena.util.iterator.Filter;
import com.hp.hpl.jena.util.iterator.FilterIterator;
import com.hp.hpl.jena.util.iterator.Map1;
import com.hp.hpl.jena.util.iterator.Map1Iterator;



public class PrId extends NodeIndexBase{
	private static final String PSO_INDEX	=	"PSO_INDEX";
	private static final String POS_INDEX	=	"POS_INDEX";
	
	BTreeList PSO;
	BTreeList POS;
	
	public PrId(NodeStore nodeStore, Configuration conf){
		super (nodeStore,conf);
		PSO	=	getBTreeList(PSO_INDEX);
		POS	=	getBTreeList(POS_INDEX);
	}
	
	public void add(TripleId tripleId){
		PSO.put(tripleId.pid, tripleId.sid, tripleId.oid);
		POS.put(tripleId.pid, tripleId.oid, tripleId.sid);
	};
	
	public void delete(TripleId tripleId){
		PSO.delete(tripleId.pid, tripleId.sid, tripleId.oid);
		POS.delete(tripleId.pid, tripleId.oid, tripleId.sid);
	};

	public ExtendedIterator<Triple> find(final Triple t) {
		if(!t.getPredicate().equals(Node.ANY)){
			int pid= nodeStore.getIdByNode(t.getPredicate());
			if(pid!=NodeStore.NODE_NOT_EXIST){
				if(t.getSubject().equals(Node.ANY)){
					if(t.getObject().equals(Node.ANY)){//? P ?
						Map1<Pair<Integer,Integer>,Triple> map = new Map1<Pair<Integer,Integer>, Triple>(){
							@Override
							public Triple map1(Pair<Integer,Integer> pair){
								Triple td = new Triple(nodeStore.getNodeById(pair.first),
												  t.getPredicate(),
												  nodeStore.getNodeById(pair.second));
								return td;
							}
						};
						return new Map1Iterator<Pair<Integer,Integer>,Triple>(map,PSO.getPair(pid));
					}else{//? P O
						int oid= nodeStore.getIdByNode(t.getObject());
						if(oid!=NodeStore.NODE_NOT_EXIST){
							Map1<Integer,Triple>	map	=	new Map1<Integer, Triple>(){
								@Override
								public Triple map1(Integer sid) {
									return new Triple(nodeStore.getNodeById(sid),
											  t.getPredicate(),
											  t.getObject());
								}
							};
						return new Map1Iterator<Integer,Triple>(map,POS.get(pid, oid));
						}
					}
				}else{//S P ?
					Filter<Integer> filter;
					if(!t.getObject().equals(Node.ANY)){
						filter	=	new Filter<Integer>(){
							@Override
							public boolean accept(Integer o) {
								return o == nodeStore.getIdByNode(t.getObject());
							}
						};
					}else{
						filter	= Filter.any();
					}
					int sid	=	nodeStore.getIdByNode(t.getSubject());
					if	(sid!=NodeStore.NODE_NOT_EXIST){
						return new Map1Iterator<Integer, Triple>(new Map1<Integer, Triple>(){
							@Override
							public Triple map1(Integer oid) {
								
								Triple td=  new Triple(t.getSubject(),
										          t.getPredicate(),
										          nodeStore.getNodeById(oid));
							
								return td;
							}
						},
		    			new FilterIterator<Integer>(filter,PSO.get(pid, sid)));
					}
				}
			}
		}
		return Triple.None;
	}

	public void close() {
		PSO.close();
		POS.close();
	}

	public void sync() {
		PSO.sync();
		POS.sync();
		
	}
	
}
