package deri.org.store;

import java.util.ArrayList;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.graph.TripleMatch;
import com.hp.hpl.jena.graph.impl.TripleStore;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.hp.hpl.jena.util.iterator.Map1Iterator;

public class BDBStore implements TripleStore{
	
	
	private boolean isEmpty;
	
	static private ArrayList<TripleIdIterator> openIter;
	
	PrId	P;
	SbId	S;
	ObId	O;
	private NodeStore	nodeStore;
	
	public BDBStore(Configuration conf){
		nodeStore	=	new NodeStore(conf);
		S			=	new SbId(nodeStore,conf);
		P			=	new PrId(nodeStore,conf);
		O			=	new ObId(nodeStore,conf);
	}

	@Override
	public void close() {
		nodeStore.close();
		if (openIter!=null)
		for(int i=0;i<openIter.size();i++) {
			if (openIter.get(i)!=null)
			openIter.get(i).close();
		}
		
		P.close();
		S.close();
		O.close();
	}

	@Override
	public void add(Triple t) {

		TripleId tripleId	=	 new TripleId(nodeStore.addNodeToStore(t.getSubject()),
	  			  							  nodeStore.addNodeToStore(t.getPredicate()),
	  			  							  nodeStore.addNodeToStore(t.getObject()));

		
		S.add(tripleId);
		P.add(tripleId);
		O.add(tripleId);
		
		
	}

	@Override
	public void delete(Triple t) {
		TripleId tripleId	=	new TripleId(nodeStore.getIdByNode(t.getSubject()),
											 nodeStore.getIdByNode(t.getPredicate()),
											 nodeStore.getIdByNode(t.getObject()));
		P.delete(tripleId);
		S.delete(tripleId);
		O.delete(tripleId);
		
	}

	@Override
	public int size() {
		return (int)P.POS.getDB().count();
	}

	@Override
	public boolean isEmpty() {
		return isEmpty;
	}

	@Override
	public boolean contains(Triple t) {
		return find(t).hasNext();
	}

	@Override
	public ExtendedIterator<Node> listSubjects() {
		TripleIdIterator tripleIdIter = new TripleIdIterator(P.PSO.getDB());
		if (openIter == null) openIter = new ArrayList<TripleIdIterator>();
		openIter.add(tripleIdIter);
		return new Map1Iterator<TripleId, Node>(new NodeMapper(nodeStore,1),tripleIdIter);
	}

	@Override
	public ExtendedIterator<Node> listPredicates() {
		TripleIdIterator tripleIdIter = new TripleIdIterator(P.PSO.getDB());
		if (openIter == null) openIter = new ArrayList<TripleIdIterator>();
		openIter.add(tripleIdIter);
		return new Map1Iterator<TripleId, Node>(new NodeMapper(nodeStore,2),tripleIdIter);
	}

	@Override
	public ExtendedIterator<Node> listObjects() {
		TripleIdIterator tripleIdIter = new TripleIdIterator(P.PSO.getDB());
		if (openIter == null) openIter = new ArrayList<TripleIdIterator>();
		openIter.add(tripleIdIter);
		return new Map1Iterator<TripleId, Node>(new NodeMapper(nodeStore,3),tripleIdIter);
	}
	
	public ExtendedIterator<Triple> listOfTriple(){
		TripleIdIterator tripleIdIter = new TripleIdIterator(P.PSO.getDB());
		if (openIter == null) openIter = new ArrayList<TripleIdIterator>();
		openIter.add(tripleIdIter);
		return new Map1Iterator<TripleId, Triple>(new TripleMapper(nodeStore), tripleIdIter);
	}
	
	@Override
	public ExtendedIterator<Triple> find(TripleMatch tm) {
		Triple t = tm.asTriple();
		
		if (!t.getPredicate().equals(Node.ANY)){//(? P ?) (? P 0) (S P ?) (S P O)
			return P.find(t);
		}else{
			if (!t.getSubject().equals(Node.ANY)){//(S ? ?) (S ? 0) 
				return S.find(t);
			}else{//? ? 0
				if(!t.getObject().equals(Node.ANY)){
					return O.find(t);
				}else{
					return listOfTriple();
				}
			}
		}
	}

	@Override
	public void clear() {
//	
		isEmpty = true;
	}

	public void sync() {
		P.sync();
		S.sync();
		O.sync();
		nodeStore.sync();
	}
}
