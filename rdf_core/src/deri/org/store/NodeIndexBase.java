package deri.org.store;

import com.sleepycat.je.Database;

public abstract class NodeIndexBase{
	NodeStore nodeStore;
	Configuration conf;
	
	public NodeIndexBase(NodeStore nodeStore, Configuration conf){
		this.nodeStore	=	nodeStore;
		this.conf		=	conf;
	}
	
	public Database createDB(String dbName){
		return conf.createDB(dbName);//+ stName);
	};
	
	public BTreeList getBTreeList(String dbName){
		return new BTreeList(createDB(dbName));//+ stName));
	};
	
	
	
}
