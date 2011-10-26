package deri.org.store;

import com.hp.hpl.jena.util.iterator.ClosableIterator;
import com.sleepycat.bind.tuple.TupleInput;
import com.sleepycat.bind.tuple.TupleOutput;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;

public class SingleIdIterator implements ClosableIterator<Integer>{
	
	int size	=	0;
	int count	=	0;
	
	TupleInput	input;
	
	public SingleIdIterator(Database db, int key1, int key2){
		TupleOutput out	=	new TupleOutput();
		out.writeInt(key1);
		out.writeInt(key2);
		
		DatabaseEntry	keyEntry	=	new DatabaseEntry(out.getBufferBytes());
		DatabaseEntry	data 		=	new DatabaseEntry();
		
		if (db.get(null, keyEntry, data, LockMode.DEFAULT)== OperationStatus.SUCCESS){
			read(data);
		}
	}
	
	
	public SingleIdIterator(DatabaseEntry data) {
		read(data);
	}


	private void read(DatabaseEntry data){
		input	=	new TupleInput(data.getData());
		size	=	input.readInt();
	}
	
	@Override
	public boolean hasNext() {
		return count<size;
	}

	@Override
	public Integer next() {
		count ++;
		return input.readInt();
	}

	@Override
	public void remove() {
		size	=	0;
		count	=	0;
	}


	@Override
	public void close() {
		
		
	}

}
