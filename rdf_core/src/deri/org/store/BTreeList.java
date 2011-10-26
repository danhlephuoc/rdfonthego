package deri.org.store;

import android.util.Pair;	

import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.hp.hpl.jena.util.iterator.WrappedIterator;
import com.sleepycat.bind.tuple.TupleInput;
import com.sleepycat.bind.tuple.TupleOutput;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;

public class BTreeList {

	Database btree;
	
//	static ArrayList<PairIdIterator> openPairItrs; 
	
	public BTreeList(Database db){
		btree=db;
	}
	public Database getDB(){
		return btree;
	}

	public void put(int key1, int key2, int value){
		TupleOutput pair2key	=	new TupleOutput();
		pair2key.writeInt(key1);
		pair2key.writeInt(key2);
		
		DatabaseEntry keyEnt= new DatabaseEntry(pair2key.getBufferBytes());
		
		DatabaseEntry data=new DatabaseEntry();
		
		TupleOutput output=new TupleOutput();
		
		if(btree.get(null, keyEnt, data, LockMode.DEFAULT)==OperationStatus.SUCCESS){
			
			TupleOutput out=new TupleOutput();
			
			TupleInput input=new TupleInput(data.getData());
			
			int count=0;
			
			int size=input.readInt();
			
			boolean notwritten=true;
			
			for(int i=0;i<size;i++){
				try{
					int val=input.readInt();
					
					if (value == val) {

						return;					
					}
					if(notwritten&&(value==val)) {
					
						notwritten=false;
						
						if(value<val){
						
							out.writeInt(value);						
							
							count++;
						}
					}
					out.writeInt(val);
					
					count++;
				}
				catch(IndexOutOfBoundsException e){
					break;
				}
			}
			
			if(notwritten){
				out.writeInt(value);
				count++;
			}
			
			output.writeInt(count);
			output.write(out.getBufferBytes());
		}else{
			output.writeInt(1);
			output.writeInt(value);	
		}
		btree.put(null,keyEnt,new DatabaseEntry(output.getBufferBytes()));
	}
	
	public void delete(int key1,int key2, int value){
		TupleOutput	valueBuffer		=	new TupleOutput();
		TupleOutput outputBuffer	=	new TupleOutput();
		
		TupleOutput pair2key	=	new TupleOutput();
		pair2key.writeInt(key1);
		pair2key.writeInt(key2);
		DatabaseEntry keyEnt= new DatabaseEntry(pair2key.getBufferBytes());
		DatabaseEntry data	= new DatabaseEntry();
		
		if(btree.get(null, keyEnt, data, LockMode.DEFAULT)==OperationStatus.SUCCESS){
			TupleInput input	=	new TupleInput(data.getData());
			int size;
			size	= input.readInt();
			if (size ==1){
				btree.delete(null, keyEnt);
				}
			else{
				int count = 0;
				
				for (int i = 0; i<size;i++){
					int val=input.readInt();
					if (val!=value){
						count++;
						valueBuffer.writeInt(val);
					}
				}
				outputBuffer.writeInt(count);
				outputBuffer.write(valueBuffer.getBufferBytes());
				btree.put(null, keyEnt, new DatabaseEntry(outputBuffer.getBufferBytes()));
			}
		}
	} 
	
	public ExtendedIterator<Pair<Integer, Integer>> getPair(int key1){
		PairIdIterator itr=new PairIdIterator(btree,key1);
//		if (openPairItrs == null) openPairItrs = new ArrayList<PairIdIterator>();
//		openPairItrs.add(itr);
		return WrappedIterator.create(itr);
	}
	
	public ExtendedIterator<Integer> get(int key1,int key2){
		return  WrappedIterator.create(new SingleIdIterator(btree,key1,key2));
	}
	
	public void close(){
//		for(int i=0;i<openPairItrs.size();i++) openPairItrs.get(i).close();
	
		btree.close();
	}
	
	public void sync(){
		btree.sync();
	}

}
