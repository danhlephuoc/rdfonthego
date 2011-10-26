package deri.org.store;

import com.hp.hpl.jena.util.iterator.ClosableIterator;
import com.sleepycat.bind.tuple.TupleInput;
import com.sleepycat.je.Cursor;
import com.sleepycat.je.CursorConfig;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;


public class TripleIdIterator implements ClosableIterator<TripleId>{
		Cursor cursor;
		DatabaseEntry key;
		DatabaseEntry data;
		boolean hasNext=false;
		
		int size 	= 0;
		int count 	= 0;
		int p , s , o;
		TupleInput inData;
		TupleInput inKey;
		
		public TripleIdIterator(Database db){
			key		=	new DatabaseEntry();
			data 	= 	new DatabaseEntry(); 
			if(db!=null){
				cursor=db.openCursor(null, CursorConfig.DEFAULT);
				goNext();
			}
		}
		
		public void goNext(){
			
			if (!(count<size)){
				hasNext	=	(cursor.getNext(key,data, LockMode.DEFAULT) == OperationStatus.SUCCESS);
				if (hasNext){	
					count   = 0;
					inData	=	new TupleInput(data.getData());
					size	=	inData.readInt();
				}
			}
			
		}
	
		@Override
		public boolean hasNext() {		
			if (!hasNext) close();
			return hasNext;
		}

		@Override
		public TripleId next() {
			count ++;
			inKey	=	new TupleInput(key.getData());
			p				=	inKey.readInt();
			s				=	inKey.readInt();
			o				=	inData.readInt();
			goNext();
			return new TripleId( s, p, o);
		}

		@Override
		public void remove() {
		}		
		
		@Override
		public void close(){
			if (cursor!=null) cursor.close();			
		}
	
}
