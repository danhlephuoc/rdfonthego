package deri.org.store;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;


import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.graph.TripleMatch;
import com.hp.hpl.jena.graph.impl.GraphBase;
import com.hp.hpl.jena.n3.turtle.ParserTurtle;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;


public class BDBGraph extends GraphBase{
	
	private BDBStore store;
	public Configuration conf;
	static int i =0;
	
	public BDBGraph(String appName){
		super();
		conf 		=	new Configuration(appName);
		store		=	new BDBStore(conf);
	}
	
	public void checkOpen(){
		super.checkOpen();
	}
	
	@Override
	public void performAdd(Triple t){
		if(!getReifier().handledAdd(t)){
			store.add(t);
		}
	}
	
	@Override
	public void performDelete(Triple t){
		if(!getReifier().handledRemove(t)){
			store.delete(t);
		}
	}
	
	@Override
	protected ExtendedIterator<Triple> graphBaseFind(TripleMatch m) {
		return  store.find(m.asTriple());
	}

	public void load(File file, String baseURI){
		checkOpen();
		ParserTurtle	parser	=	new ParserTurtle();
		try{
			parser.parse(this, baseURI, new FileInputStream(file));
		}catch (FileNotFoundException e){
			
		}
	}
	
	public void load(String urlStr,String baseURI){
		checkOpen();
		ParserTurtle parser	=	new ParserTurtle();
		try{
			URL	url	=	new URL(urlStr);
			parser.parse(this, baseURI, url.openStream());
		}catch(MalformedURLException e){
			
		}catch(IOException e){
			
		}
	}
	
	@Override
	public int graphBaseSize(){
		return store.size();
	}
	
	@Override
	public void sync(){
		 store.sync();
	 }
	
	@Override
	public void close(){
		store.close();
		conf.close();
		super.close();
	}

	public void clear() {
		if (!this.isClosed()){
			close();
		}
	}
	
	public ExtendedIterator<Node> listPred(){
		return store.listPredicates();
	}

	public ExtendedIterator<Node> listSubject(){
		return store.listSubjects();
	}
	
	public ExtendedIterator<Node> listObjects(){
		return store.listObjects();
	}
	
	public ExtendedIterator<Triple> listTriples(){
		return store.listOfTriple();
	}
	
}
