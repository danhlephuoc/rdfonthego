package deri.org.store;

import com.hp.hpl.jena.util.iterator.ClosableIterator;
import com.sleepycat.bind.tuple.TupleInput;
import com.sleepycat.bind.tuple.TupleOutput;
import com.sleepycat.je.Cursor;
import com.sleepycat.je.CursorConfig;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;

import android.util.Pair;

public class PairIdIterator implements ClosableIterator <Pair<Integer,Integer>>{
	Cursor cursor;
	
	Pair<Integer,Integer> pairId = null;
	
	int key1;
	int key2;
	
	SingleIdIterator value;
	
	public PairIdIterator(Database db, int key){
		key1	=	key;
		key2	=	0;
		
		if(db!=null){
			cursor	=	db.openCursor(null, CursorConfig.DEFAULT);
			TupleOutput	output	=	new TupleOutput();
			output.writeInt(key1);
			output.writeInt(key2);
			
			DatabaseEntry	keySearch	=	new DatabaseEntry(output.getBufferBytes());
			DatabaseEntry	data		=	new DatabaseEntry();
			
			if(cursor.getSearchKeyRange(keySearch, data, LockMode.DEFAULT)== OperationStatus.SUCCESS){
				getData(keySearch, data);
			} 
		}
	}
	
	public void getData(DatabaseEntry key, DatabaseEntry data){
		TupleInput in	=	new TupleInput(key.getData());
		pairId	=	new Pair<Integer, Integer> (in.readInt(),in.readInt());
		
		value	=	new SingleIdIterator(data);
	}

	
	private void goNext(){
		if ((pairId!=null)&&(pairId.first==key1)){
			DatabaseEntry	key		=	new DatabaseEntry();
			DatabaseEntry	data	=	new DatabaseEntry();
			if (cursor.getNext(key, data, LockMode.DEFAULT)==OperationStatus.SUCCESS){
				getData(key,data);
			}else{
				pairId	=	null;
				cursor.close();
			}
		}else{
			cursor.close();
		}
	}
	
	
	
	@Override
	public boolean hasNext() {
		boolean hasNext = (pairId!=null)&&(value!=null)&&(pairId.first==key1)&&(value.hasNext());
		if (!hasNext) close();
		return hasNext;
	}

	@Override
	public Pair<Integer, Integer> next() {
		Pair<Integer, Integer> pair	=	new Pair<Integer, Integer>(pairId.second,value.next());
		if(!value.hasNext()) goNext();
		return pair;
	}

	@Override
	public void remove() {
		pairId	=	null;
		value.remove();
		if(cursor!=null) cursor.close();
	}
	
	public void close(){
		if (cursor!=null) cursor.close();
		
	}
}
