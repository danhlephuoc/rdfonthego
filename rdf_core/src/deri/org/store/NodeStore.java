package deri.org.store;

import com.hp.hpl.jena.datatypes.RDFDatatype;
import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.NodeConst;
import com.hp.hpl.jena.rdf.model.AnonId;

import com.sleepycat.bind.tuple.DoubleBinding;
import com.sleepycat.bind.tuple.FloatBinding;
import com.sleepycat.bind.tuple.IntegerBinding;
import com.sleepycat.bind.tuple.LongBinding;
import com.sleepycat.bind.tuple.StringBinding;
import com.sleepycat.bind.tuple.TupleOutput;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;
import com.sleepycat.je.SecondaryDatabase;

public class NodeStore {
	
	
	public static final int NODE_NOT_EXIST 	= 	-1;
	public static final int LEN_NODE_HASH		=	128/8;
	
	static final String LASTIDDB_NAME	=	"LastIdDB";
	static final String INDEXDB_NAME	=	"IndexDB";
	static final String INDEX2NDDB_NAME	=	"Index2ndDB";
	static final String NODESDB_NAME	=	"NodeDB";
	
	Configuration conf;



	private boolean isClosed;
	private boolean isEmpty;
	
	private Database			lastIdDB;										//LastKey collector
	
	private Database			indexDB;										//Id collection
	
	private SecondaryDatabase	index2ndDB;										//Id 
	
	private Database			nodesDB;										//Node collector key base on metaDB
								 
	
	public NodeStore(Configuration conf){
		isClosed	=	false;
		
		this.conf	=	conf;
		
		lastIdDB	=	conf.createDB(LASTIDDB_NAME);
		
		nodesDB		=	conf.createDB(NODESDB_NAME);

		indexDB		=	conf.createDB(INDEXDB_NAME);
		
		index2ndDB	=	conf.create2ndDB(indexDB,INDEX2NDDB_NAME);
		
		
	}
	
	public int getIdByNode(Node node){
		
		DatabaseEntry nodeKey	=	new DatabaseEntry();				
		IntegerBinding.intToEntry(NodeLibrary.createNodeKey(node), nodeKey);											

		DatabaseEntry nodeId	=	new DatabaseEntry();
		
		if(index2ndDB.get(null,nodeKey,nodeId,new DatabaseEntry(), LockMode.DEFAULT)==OperationStatus.SUCCESS){
			return IntegerBinding.entryToInt(nodeId);													
		}
		return NODE_NOT_EXIST;
	}
	
	
	public Node getNodeById(int id){
		DatabaseEntry	nodeId	=	new DatabaseEntry();
		IntegerBinding.intToEntry(id, nodeId);
		DatabaseEntry	value	=	new DatabaseEntry();
		
		int group 		= NodeLibrary.getGroupById(id);
		int subGroup 	= NodeLibrary.getSubGroupById(id);
		int xsdDatatype = NodeLibrary.getTypeById(id);
		//===============================================================================================//
		if (group == NodeLibrary.LITERAL){
			if(nodesDB.get(null, nodeId, value, LockMode.DEFAULT)==OperationStatus.SUCCESS){
				String literal	=	StringBinding.entryToString(value);
				return Node.createLiteral(literal);
			}
		}	
		//===============================================================================================//
		if (group	== NodeLibrary.groupURI_BLANK){
			
			if (subGroup	== NodeLibrary.BLANK){
				if(nodesDB.get(null, nodeId, value, LockMode.DEFAULT)==OperationStatus.SUCCESS){
					String anon	=	StringBinding.entryToString(value);
					return Node.createAnon(new AnonId(anon));
				}
			}
			if (subGroup	== NodeLibrary.URI){
				if(nodesDB.get(null, nodeId, value, LockMode.DEFAULT)==OperationStatus.SUCCESS){
					String uri	=	StringBinding.entryToString(value);
					return Node.createURI(uri);
				}
			}
		}

		//===============================================================================================//
		if (group	==	NodeLibrary.groupNumeric){
			if (subGroup == NodeLibrary.subgrouphigh){
				switch(xsdDatatype){
				case NodeLibrary.XSDnonNegativeInteger:
					if(nodesDB.get(null, nodeId, value, LockMode.DEFAULT)==OperationStatus.SUCCESS){
						int num			=	IntegerBinding.entryToInt(value);
						String literal	=	Integer.toString(num);
						return Node.createLiteral(literal,null,XSDDatatype.XSDnonNegativeInteger);
					}
				case NodeLibrary.XSDunsignedLong:
					if(nodesDB.get(null, nodeId, value, LockMode.DEFAULT)==OperationStatus.SUCCESS){
						long num		=	LongBinding.entryToLong(value);
						String literal	=	Long.toString(num);
						return Node.createLiteral(literal,null,XSDDatatype.XSDunsignedLong);
					}			
				case NodeLibrary.XSDunsignedInt:
					if(nodesDB.get(null, nodeId, value, LockMode.DEFAULT)==OperationStatus.SUCCESS){
						int num			=	IntegerBinding.entryToInt(value);
						String literal	=	Integer.toString(num);
						return Node.createLiteral(literal,null,XSDDatatype.XSDunsignedInt);
					}
				case NodeLibrary.XSDunsignedShort:
						{int num		=	BitsInt.unpack(id, 0, 16);
						return Node.createLiteral(Integer.toString(num),null,XSDDatatype.XSDunsignedShort);}
				case NodeLibrary.XSDdecimal:
					if(nodesDB.get(null, nodeId, value, LockMode.DEFAULT)==OperationStatus.SUCCESS){
						String literal	=	StringBinding.entryToString(value);
						return Node.createLiteral(literal,null,XSDDatatype.XSDdecimal);
					}
				case NodeLibrary.XSDfloat:
					if(nodesDB.get(null, nodeId, value, LockMode.DEFAULT)==OperationStatus.SUCCESS){
						float	num		=	FloatBinding.entryToFloat(value);
						String literal	=	Float.toString(num);
						return Node.createLiteral(literal,null,XSDDatatype.XSDfloat);
					}
				case NodeLibrary.XSDdouble:
					if(nodesDB.get(null, nodeId, value, LockMode.DEFAULT)==OperationStatus.SUCCESS){
						double	num		=	DoubleBinding.entryToDouble(value);
						String literal	=	Double.toString(num);
						return Node.createLiteral(literal,null,XSDDatatype.XSDdouble);
					}
				case NodeLibrary.XSDduration:
					if(nodesDB.get(null, nodeId, value, LockMode.DEFAULT)==OperationStatus.SUCCESS){
						String literal	=	StringBinding.entryToString(value);
						return Node.createLiteral(literal,null,XSDDatatype.XSDduration);
					}
				case NodeLibrary.XSDint:
					if(nodesDB.get(null, nodeId, value, LockMode.DEFAULT)==OperationStatus.SUCCESS){
						int num			=	IntegerBinding.entryToInt(value);
						String literal	=	Integer.toString(num);
						return Node.createLiteral(literal,null,XSDDatatype.XSDint);
					}
				case NodeLibrary.XSDinteger:
					if(nodesDB.get(null, nodeId, value, LockMode.DEFAULT)==OperationStatus.SUCCESS){
						String literal	=	StringBinding.entryToString(value);
						return Node.createLiteral(literal,null,XSDDatatype.XSDinteger);
					}
				case NodeLibrary.XSDshort:
					{
						int num		=	BitsInt.unpack(id, 0, 16);
						int	b		=	BitsInt.unpack(id, 16, 17);
						if (b==0) num	=	0- num;
						return Node.createLiteral(Integer.toString(num),null,XSDDatatype.XSDshort);
					}
					
				case NodeLibrary.XSDbyte:
					{
						int num		=	BitsInt.unpack(id, 0, 16);
						int	b		=	BitsInt.unpack(id, 16, 17);
						if (b==0) num	=	0- num;
						return Node.createLiteral(Integer.toString(num),null,XSDDatatype.XSDbyte);
					}
				case NodeLibrary.XSDhexBinary:
					if(nodesDB.get(null, nodeId, value, LockMode.DEFAULT)==OperationStatus.SUCCESS){
						String literal	=	StringBinding.entryToString(value);
						return Node.createLiteral(literal,null,XSDDatatype.XSDhexBinary);
					}
				case NodeLibrary.XSDnegativeInteger:
					if(nodesDB.get(null, nodeId, value, LockMode.DEFAULT)==OperationStatus.SUCCESS){
						int	num			=	IntegerBinding.entryToInt(value);
						String literal	=	Integer.toString(num);
						return Node.createLiteral(literal,null,XSDDatatype.XSDnegativeInteger);
					}
				case NodeLibrary.XSDlong:
					if(nodesDB.get(null, nodeId, value, LockMode.DEFAULT)==OperationStatus.SUCCESS){
						long num		=	LongBinding.entryToLong(value);
						String literal	=	Long.toString(num);
						return Node.createLiteral(literal,null,XSDDatatype.XSDlong);
					}
				case NodeLibrary.XSDnonPositiveInteger:
					if(nodesDB.get(null, nodeId, value, LockMode.DEFAULT)==OperationStatus.SUCCESS){
						int num			=	IntegerBinding.entryToInt(value);
						String literal	=	Integer.toString(num);
						return Node.createLiteral(literal,null,XSDDatatype.XSDnonPositiveInteger);
					}
				}
			}
			//===============================================================================================//
			if(subGroup == NodeLibrary.subgrouplow){
				int num		=	BitsInt.unpack(id, 0, 27);
				int	b		=	BitsInt.unpack(id, 27, 28);
				if (b==0) num	=	0- num;
				return Node.createLiteral(Integer.toString(num),null,XSDDatatype.XSDint);
			}
		}
		
		//===============================================================================================//
		if(group == NodeLibrary.other){
			//===============================================================================================//
			if(subGroup == NodeLibrary.groupDT){
				switch (xsdDatatype){
				case NodeLibrary.XSDtime:
					if(nodesDB.get(null, nodeId, value, LockMode.DEFAULT)==OperationStatus.SUCCESS){
						String literal	=	StringBinding.entryToString(value);
						return Node.createLiteral(literal,null,XSDDatatype.XSDtime);
					}
				case NodeLibrary.XSDdate:
					if(nodesDB.get(null, nodeId, value, LockMode.DEFAULT)==OperationStatus.SUCCESS){
						String literal	=	StringBinding.entryToString(value);
						return Node.createLiteral(literal,null,XSDDatatype.XSDdate);
					}
				case NodeLibrary.XSDgYearMonth:
					if(nodesDB.get(null, nodeId, value, LockMode.DEFAULT)==OperationStatus.SUCCESS){
						String literal	=	StringBinding.entryToString(value);
						return Node.createLiteral(literal,null,XSDDatatype.XSDgYearMonth);
					}
				case NodeLibrary.XSDgMonthDay:
					if(nodesDB.get(null, nodeId, value, LockMode.DEFAULT)==OperationStatus.SUCCESS){
						String literal	=	StringBinding.entryToString(value);
						return Node.createLiteral(literal,null,XSDDatatype.XSDgMonthDay);
					}
				case NodeLibrary.XSDgYear:
					int year	=	BitsInt.unpack(id, 0, 15);
					return Node.createLiteral(Integer.toString(year),null,XSDDatatype.XSDgYear);
				case NodeLibrary.XSDgDay:
					int day		=	BitsInt.unpack(id, 0, 9);
					return Node.createLiteral(Integer.toString(day),null,XSDDatatype.XSDgDay);
				case NodeLibrary.XSDdateTime:
					if(nodesDB.get(null, nodeId, value, LockMode.DEFAULT)==OperationStatus.SUCCESS){
						String literal	=	StringBinding.entryToString(value);
						return Node.createLiteral(literal,null,XSDDatatype.XSDdateTime);
					}
				case NodeLibrary.XSDboolean:	
					int bool	=	BitsInt.unpack(id,0,1);
					if (bool ==1) return NodeConst.nodeTrue;
					if (bool ==0) return NodeConst.nodeFalse;
				}
			}
			//===============================================================================================//
			if(subGroup == NodeLibrary.groupOtherLiteral){
				switch (xsdDatatype){
				case NodeLibrary.XSDstring:
					if(nodesDB.get(null, nodeId, value, LockMode.DEFAULT)==OperationStatus.SUCCESS){
						String literal	=	StringBinding.entryToString(value);
						return Node.createLiteral(literal,null,XSDDatatype.XSDstring);
					}
				case NodeLibrary.XSDlanguage:
					if(nodesDB.get(null, nodeId, value, LockMode.DEFAULT)==OperationStatus.SUCCESS){
						String literal	=	StringBinding.entryToString(value);
						return Node.createLiteral(literal,null,XSDDatatype.XSDlanguage);
					}
				case NodeLibrary.XSDnormalizedString:
					if(nodesDB.get(null, nodeId, value, LockMode.DEFAULT)==OperationStatus.SUCCESS){
						String literal	=	StringBinding.entryToString(value);
						return Node.createLiteral(literal,null,XSDDatatype.XSDnormalizedString);
					}
				case NodeLibrary.XSDtoken:
					if(nodesDB.get(null, nodeId, value, LockMode.DEFAULT)==OperationStatus.SUCCESS){
						String literal	=	StringBinding.entryToString(value);
						return Node.createLiteral(literal,null,XSDDatatype.XSDtoken);
					}
				case NodeLibrary.XSDName:
					if(nodesDB.get(null, nodeId, value, LockMode.DEFAULT)==OperationStatus.SUCCESS){
						String literal	=	StringBinding.entryToString(value);
						return Node.createLiteral(literal,null,XSDDatatype.XSDName);
					}
				case NodeLibrary.XSDQName:
					if(nodesDB.get(null, nodeId, value, LockMode.DEFAULT)==OperationStatus.SUCCESS){
						String literal	=	StringBinding.entryToString(value);
						return Node.createLiteral(literal,null,XSDDatatype.XSDQName);
					}
				case NodeLibrary.XSDNMTOKEN:
					if(nodesDB.get(null, nodeId, value, LockMode.DEFAULT)==OperationStatus.SUCCESS){
						String literal	=	StringBinding.entryToString(value);
						return Node.createLiteral(literal,null,XSDDatatype.XSDNMTOKEN);
					}
				case NodeLibrary.XSDENTITY:
					if(nodesDB.get(null, nodeId, value, LockMode.DEFAULT)==OperationStatus.SUCCESS){
						String literal	=	StringBinding.entryToString(value);
						return Node.createLiteral(literal,null,XSDDatatype.XSDENTITY);
					}
				case NodeLibrary.XSDID:
					if(nodesDB.get(null, nodeId, value, LockMode.DEFAULT)==OperationStatus.SUCCESS){
						String literal	=	StringBinding.entryToString(value);
						return Node.createLiteral(literal,null,XSDDatatype.XSDID);
					}
				case NodeLibrary.XSDNCName:
					if(nodesDB.get(null, nodeId, value, LockMode.DEFAULT)==OperationStatus.SUCCESS){
						String literal	=	StringBinding.entryToString(value);
						return Node.createLiteral(literal,null,XSDDatatype.XSDNCName);
					}
				case NodeLibrary.XSDIDREF:
					if(nodesDB.get(null, nodeId, value, LockMode.DEFAULT)==OperationStatus.SUCCESS){
						String literal	=	StringBinding.entryToString(value);
						return Node.createLiteral(literal,null,XSDDatatype.XSDIDREF);
					}
					
				case NodeLibrary.XSDbase64Binary:	
					if(nodesDB.get(null, nodeId, value, LockMode.DEFAULT)==OperationStatus.SUCCESS){
						String literal	=	StringBinding.entryToString(value);
						return Node.createLiteral(literal,null,XSDDatatype.XSDbase64Binary);
					}	
				case NodeLibrary.XSDanyURI:
					if(nodesDB.get(null, nodeId, value, LockMode.DEFAULT)==OperationStatus.SUCCESS){
						String literal	=	StringBinding.entryToString(value);
						return Node.createLiteral(literal,null,XSDDatatype.XSDanyURI);
					}
				case NodeLibrary.XSDNOTATION:	
					if(nodesDB.get(null, nodeId, value, LockMode.DEFAULT)==OperationStatus.SUCCESS){
						String literal	=	StringBinding.entryToString(value);
						return Node.createLiteral(literal,null,XSDDatatype.XSDNOTATION);
					}
				case NodeLibrary.XSDpositiveInteger:{	
					int num			=	IntegerBinding.entryToInt(value);
					String literal	=	Integer.toString(num);
					return Node.createLiteral(literal,null,XSDDatatype.XSDunsignedInt);
					}
				case NodeLibrary.XSDunsignedByte:	
					int num		=	BitsInt.unpack(id, 0, 16);
					return Node.createLiteral(Integer.toString(num),null,XSDDatatype.XSDunsignedShort);
				}
			}
		}
		return NodeConst.nodeNil;
	}
	
	public int addLiteralToStore(Node node){
		TupleOutput	output	=	new TupleOutput();
		output.writeString(node.getLiteralLexicalForm());
		DatabaseEntry	value	=	new DatabaseEntry(output.getBufferBytes());

		
		int id		=	NodeLibrary.nodeToId(node);
		if (id==0){
			id	=	getLastId() + 1;
			updateLastId(id);	
			RDFDatatype  rdfDatatype	=	node.getLiteralDatatype();
			if (rdfDatatype == null){		
				id		=	NodeLibrary.setGroupById(NodeLibrary.LITERAL, id);
				output.writeString(node.getLiteralLexicalForm());
				value	= new DatabaseEntry(output.getBufferBytes());
			}else{
				if (rdfDatatype.equals(XSDDatatype.XSDnonNegativeInteger)){ 
					id		=	NodeLibrary.setGroupById(NodeLibrary.groupNumeric, id);
					id		=	NodeLibrary.setSubById(NodeLibrary.subgrouphigh, id);
					id		=	NodeLibrary.setTypeById(NodeLibrary.XSDnonNegativeInteger, id);
					String valueLexical	=	node.getLiteralLexicalForm();
					IntegerBinding.intToEntry(Integer.parseInt(valueLexical), value);
				}
				if (rdfDatatype.equals(XSDDatatype.XSDunsignedLong)){ 
					id		=	NodeLibrary.setGroupById(NodeLibrary.groupNumeric, id);
					id		=	NodeLibrary.setSubById(NodeLibrary.subgrouphigh, id);
					id		=	NodeLibrary.setTypeById(NodeLibrary.XSDunsignedLong, id);
					String valueLexical	=	node.getLiteralLexicalForm();
					LongBinding.longToEntry(Long.parseLong(valueLexical), value);
				}
				if (rdfDatatype.equals(XSDDatatype.XSDunsignedInt)){ 
					id		=	NodeLibrary.setGroupById(NodeLibrary.groupNumeric, id);
					id		=	NodeLibrary.setSubById(NodeLibrary.subgrouphigh, id);
					id		=	NodeLibrary.setTypeById(NodeLibrary.XSDunsignedInt, id);
					String valueLexical	=	node.getLiteralLexicalForm();
					IntegerBinding.intToEntry(Integer.parseInt(valueLexical), value);
				}
				if (rdfDatatype.equals(XSDDatatype.XSDunsignedShort)){ 
					id		=	NodeLibrary.setGroupById(NodeLibrary.groupNumeric, id);
					id		=	NodeLibrary.setSubById(NodeLibrary.subgrouphigh, id);
					id		=	NodeLibrary.setTypeById(NodeLibrary.XSDunsignedShort, id);
				}
				
				if (rdfDatatype.equals(XSDDatatype.XSDdecimal)){ 
					id		=	NodeLibrary.setGroupById(NodeLibrary.groupNumeric, id);
					id		=	NodeLibrary.setSubById(NodeLibrary.subgrouphigh, id);
					id		=	NodeLibrary.setTypeById(NodeLibrary.XSDdecimal, id);
				}
				if (rdfDatatype.equals(XSDDatatype.XSDfloat)){ 
					id		=	NodeLibrary.setGroupById(NodeLibrary.groupNumeric, id);
					id		=	NodeLibrary.setSubById(NodeLibrary.subgrouphigh, id);
					id		=	NodeLibrary.setTypeById(NodeLibrary.XSDfloat, id);
					String valueLexical	=	node.getLiteralLexicalForm();
					FloatBinding.floatToEntry(Float.parseFloat(valueLexical), value);
				}
				if (rdfDatatype.equals(XSDDatatype.XSDdouble)){ 
					id		=	NodeLibrary.setGroupById(NodeLibrary.groupNumeric, id);
					id		=	NodeLibrary.setSubById(NodeLibrary.subgrouphigh, id);
					id		=	NodeLibrary.setTypeById(NodeLibrary.XSDdouble, id);
					String valueLexical	=	node.getLiteralLexicalForm();
					DoubleBinding.doubleToEntry(Double.parseDouble(valueLexical), value);
				}
				if (rdfDatatype.equals(XSDDatatype.XSDduration)){ 
					id		=	NodeLibrary.setGroupById(NodeLibrary.groupNumeric, id);
					id		=	NodeLibrary.setSubById(NodeLibrary.subgrouphigh, id);
					id		=	NodeLibrary.setTypeById(NodeLibrary.XSDduration, id);
				}
				if (rdfDatatype.equals(XSDDatatype.XSDint)){ 
					id		=	NodeLibrary.setGroupById(NodeLibrary.groupNumeric, id);
					id		=	NodeLibrary.setSubById(NodeLibrary.subgrouphigh, id);
					id		=	NodeLibrary.setTypeById(NodeLibrary.XSDint, id);
					String valueLexical	=	node.getLiteralLexicalForm();
					IntegerBinding.intToEntry(Integer.parseInt(valueLexical), value);
				}
				if (rdfDatatype.equals(XSDDatatype.XSDinteger)){ 
					id		=	NodeLibrary.setGroupById(NodeLibrary.groupNumeric, id);
					id		=	NodeLibrary.setSubById(NodeLibrary.subgrouphigh, id);
					id		=	NodeLibrary.setTypeById(NodeLibrary.XSDinteger, id);
				}
				if (rdfDatatype.equals(XSDDatatype.XSDshort)){ 
					id		=	NodeLibrary.setGroupById(NodeLibrary.groupNumeric, id);
					id		=	NodeLibrary.setSubById(NodeLibrary.subgrouphigh, id);
					id		=	NodeLibrary.setTypeById(NodeLibrary.XSDshort, id);
				}
				if (rdfDatatype.equals(XSDDatatype.XSDbyte)){ 
					id		=	NodeLibrary.setGroupById(NodeLibrary.groupNumeric, id);
					id		=	NodeLibrary.setSubById(NodeLibrary.subgrouphigh, id);
					id		=	NodeLibrary.setTypeById(NodeLibrary.XSDbyte, id);
				}
				if (rdfDatatype.equals(XSDDatatype.XSDhexBinary)){ 
					id		=	NodeLibrary.setGroupById(NodeLibrary.groupNumeric, id);
					id		=	NodeLibrary.setSubById(NodeLibrary.subgrouphigh, id);
					id		=	NodeLibrary.setTypeById(NodeLibrary.XSDhexBinary, id);
				}
				if (rdfDatatype.equals(XSDDatatype.XSDnegativeInteger)){ 
					id		=	NodeLibrary.setGroupById(NodeLibrary.groupNumeric, id);
					id		=	NodeLibrary.setSubById(NodeLibrary.subgrouphigh, id);
					id		=	NodeLibrary.setTypeById(NodeLibrary.XSDnegativeInteger, id);
					String valueLexical	=	node.getLiteralLexicalForm();
					IntegerBinding.intToEntry(Integer.parseInt(valueLexical), value);
				}
				if (rdfDatatype.equals(XSDDatatype.XSDlong)){ 
					id		=	NodeLibrary.setGroupById(NodeLibrary.groupNumeric, id);
					id		=	NodeLibrary.setSubById(NodeLibrary.subgrouphigh, id);
					id		=	NodeLibrary.setTypeById(NodeLibrary.XSDlong, id);
					String valueLexical	=	node.getLiteralLexicalForm();
					LongBinding.longToEntry(Long.parseLong(valueLexical), value);
				}
				if (rdfDatatype.equals(XSDDatatype.XSDnonPositiveInteger)){ 
					id		=	NodeLibrary.setGroupById(NodeLibrary.groupNumeric, id);
					id		=	NodeLibrary.setSubById(NodeLibrary.subgrouphigh, id);
					id		=	NodeLibrary.setTypeById(NodeLibrary.XSDnonPositiveInteger, id);
					String valueLexical	=	node.getLiteralLexicalForm();
					IntegerBinding.intToEntry(Integer.parseInt(valueLexical), value);
				}
				if (rdfDatatype.equals(XSDDatatype.XSDnegativeInteger)){
					id		=	NodeLibrary.setGroupById(NodeLibrary.groupNumeric, id);
					id		=	NodeLibrary.setSubById(NodeLibrary.subgrouphigh, id);
					id		=	NodeLibrary.setTypeById(NodeLibrary.XSDnegativeInteger, id);
					String valueLexical	=	node.getLiteralLexicalForm();
					IntegerBinding.intToEntry(Integer.parseInt(valueLexical), value);
				}
				if (rdfDatatype.equals(XSDDatatype.XSDnonPositiveInteger)){ 
					id		=	NodeLibrary.setGroupById(NodeLibrary.groupNumeric, id);
					id		=	NodeLibrary.setSubById(NodeLibrary.subgrouphigh, id);
					id		=	NodeLibrary.setTypeById(NodeLibrary.XSDnonPositiveInteger, id);
					String valueLexical	=	node.getLiteralLexicalForm();
					IntegerBinding.intToEntry(Integer.parseInt(valueLexical), value);
				}
				if (rdfDatatype.equals(XSDDatatype.XSDstring)){ 
					id		=	NodeLibrary.setGroupById(NodeLibrary.other, id);
					id		=	NodeLibrary.setSubById(NodeLibrary.groupOtherLiteral, id);
					id		=	NodeLibrary.setTypeById(NodeLibrary.XSDstring, id);
				}
				if (rdfDatatype.equals(XSDDatatype.XSDlanguage)){ 
					id		=	NodeLibrary.setGroupById(NodeLibrary.other, id);
					id		=	NodeLibrary.setSubById(NodeLibrary.groupOtherLiteral, id);
					id		=	NodeLibrary.setTypeById(NodeLibrary.XSDlanguage, id);
				}
				if (rdfDatatype.equals(XSDDatatype.XSDnormalizedString)){ 
					id		=	NodeLibrary.setGroupById(NodeLibrary.other, id);
					id		=	NodeLibrary.setSubById(NodeLibrary.groupOtherLiteral, id);
					id		=	NodeLibrary.setTypeById(NodeLibrary.XSDnormalizedString, id);
				}
				if (rdfDatatype.equals(XSDDatatype.XSDtoken)){ 
					id		=	NodeLibrary.setGroupById(NodeLibrary.other, id);
					id		=	NodeLibrary.setSubById(NodeLibrary.groupOtherLiteral, id);
					id		=	NodeLibrary.setTypeById(NodeLibrary.XSDtoken, id);
				}
				if (rdfDatatype.equals(XSDDatatype.XSDName)){ 
					id		=	NodeLibrary.setGroupById(NodeLibrary.other, id);
					id		=	NodeLibrary.setSubById(NodeLibrary.groupOtherLiteral, id);
					id		=	NodeLibrary.setTypeById(NodeLibrary.XSDName, id);
				}
				if (rdfDatatype.equals(XSDDatatype.XSDQName)){ 
					id		=	NodeLibrary.setGroupById(NodeLibrary.other, id);
					id		=	NodeLibrary.setSubById(NodeLibrary.groupOtherLiteral, id);
					id		=	NodeLibrary.setTypeById(NodeLibrary.XSDQName, id);
				}
				if (rdfDatatype.equals(XSDDatatype.XSDNMTOKEN)){
					id		=	NodeLibrary.setGroupById(NodeLibrary.other, id);
					id		=	NodeLibrary.setSubById(NodeLibrary.groupOtherLiteral, id);
					id		=	NodeLibrary.setTypeById(NodeLibrary.XSDNMTOKEN, id);
				}
				if (rdfDatatype.equals(XSDDatatype.XSDENTITY)){
					id		=	NodeLibrary.setGroupById(NodeLibrary.other, id);
					id		=	NodeLibrary.setSubById(NodeLibrary.groupOtherLiteral, id);
					id		=	NodeLibrary.setTypeById(NodeLibrary.XSDENTITY, id);
				}
				if (rdfDatatype.equals(XSDDatatype.XSDID)){ 
					id		=	NodeLibrary.setGroupById(NodeLibrary.other, id);
					id		=	NodeLibrary.setSubById(NodeLibrary.groupOtherLiteral, id);
					id		=	NodeLibrary.setTypeById(NodeLibrary.XSDID, id);
				}
				if (rdfDatatype.equals(XSDDatatype.XSDNCName)){ 
					id		=	NodeLibrary.setGroupById(NodeLibrary.other, id);
					id		=	NodeLibrary.setSubById(NodeLibrary.groupOtherLiteral, id);
					id		=	NodeLibrary.setTypeById(NodeLibrary.XSDNCName, id);
				}
				if (rdfDatatype.equals(XSDDatatype.XSDIDREF)){ 
					id		=	NodeLibrary.setGroupById(NodeLibrary.other, id);
					id		=	NodeLibrary.setSubById(NodeLibrary.groupOtherLiteral, id);
					id		=	NodeLibrary.setTypeById(NodeLibrary.XSDIDREF, id);
				}
				if (rdfDatatype.equals(XSDDatatype.XSDbase64Binary)){
					id		=	NodeLibrary.setGroupById(NodeLibrary.other, id);
					id		=	NodeLibrary.setSubById(NodeLibrary.groupOtherLiteral, id);
					id		=	NodeLibrary.setTypeById(NodeLibrary.XSDbase64Binary, id);
				}
				if (rdfDatatype.equals(XSDDatatype.XSDanyURI)){ 
					id		=	NodeLibrary.setGroupById(NodeLibrary.other, id);
					id		=	NodeLibrary.setSubById(NodeLibrary.groupOtherLiteral, id);
					id		=	NodeLibrary.setTypeById(NodeLibrary.XSDanyURI, id);
				}
				if (rdfDatatype.equals(XSDDatatype.XSDNOTATION)){ 
					id		=	NodeLibrary.setGroupById(NodeLibrary.other, id);
					id		=	NodeLibrary.setSubById(NodeLibrary.groupOtherLiteral, id);
					id		=	NodeLibrary.setTypeById(NodeLibrary.XSDNOTATION, id);
				}
				if (rdfDatatype.equals(XSDDatatype.XSDpositiveInteger)){ 
					id		=	NodeLibrary.setGroupById(NodeLibrary.other, id);
					id		=	NodeLibrary.setSubById(NodeLibrary.groupOtherLiteral, id);
					id		=	NodeLibrary.setTypeById(NodeLibrary.XSDpositiveInteger, id);
				}
				if (rdfDatatype.equals(XSDDatatype.XSDunsignedByte)){ 
					id		=	NodeLibrary.setGroupById(NodeLibrary.other, id);
					id		=	NodeLibrary.setSubById(NodeLibrary.groupOtherLiteral, id);
					id		=	NodeLibrary.setTypeById(NodeLibrary.XSDunsignedByte, id);
				}
				if (rdfDatatype.equals(XSDDatatype.XSDtime)){ 
					id		=	NodeLibrary.setGroupById(NodeLibrary.other, id);
					id		=	NodeLibrary.setSubById(NodeLibrary.groupDT, id);
					id		=	NodeLibrary.setTypeById(NodeLibrary.XSDtime, id);
				}
				if (rdfDatatype.equals(XSDDatatype.XSDdate)){ 
					id		=	NodeLibrary.setGroupById(NodeLibrary.other, id);
					id		=	NodeLibrary.setSubById(NodeLibrary.groupDT, id);
					id		=	NodeLibrary.setTypeById(NodeLibrary.XSDdate, id);
				}
				if (rdfDatatype.equals(XSDDatatype.XSDgYearMonth)){ 
					id		=	NodeLibrary.setGroupById(NodeLibrary.other, id);
					id		=	NodeLibrary.setSubById(NodeLibrary.groupDT, id);
					id		=	NodeLibrary.setTypeById(NodeLibrary.XSDgYearMonth, id);
				}
				if (rdfDatatype.equals(XSDDatatype.XSDgMonthDay)){ 
					id		=	NodeLibrary.setGroupById(NodeLibrary.other, id);
					id		=	NodeLibrary.setSubById(NodeLibrary.groupDT, id);
					id		=	NodeLibrary.setTypeById(NodeLibrary.XSDgMonthDay, id);
				}
				if (rdfDatatype.equals(XSDDatatype.XSDdateTime)){ 
					id		=	NodeLibrary.setGroupById(NodeLibrary.other, id);
					id		=	NodeLibrary.setSubById(NodeLibrary.groupDT, id);
					id		=	NodeLibrary.setTypeById(NodeLibrary.XSDdateTime, id);
				}			
			}
			DatabaseEntry	nodeId	=	new DatabaseEntry();
			IntegerBinding.intToEntry(id, nodeId);
			nodesDB.put(null, nodeId, value);
		}
		return id;
	}

	public int addUriToStore(Node node){
		DatabaseEntry	value =	new DatabaseEntry();
		DatabaseEntry	nodeId = new DatabaseEntry();
		
		StringBinding.stringToEntry(node.getURI(), value);
		int id	=	getLastId()	+	1;
		updateLastId(id);
		id		=	NodeLibrary.setGroupById(NodeLibrary.groupURI_BLANK,id);
		id		=	NodeLibrary.setSubById(NodeLibrary.URI, id);
		IntegerBinding.intToEntry(id, nodeId);
		nodesDB.put(null, nodeId, value);
		return id;
	}
	
	public int addAnonIdToStore(Node node){
		DatabaseEntry	value	=	new DatabaseEntry();
		DatabaseEntry	nodeId	=	new DatabaseEntry();
		
		StringBinding.stringToEntry(node.getBlankNodeId().toString(), value);
		int id	=	getLastId()+1;
		updateLastId(id);
		id		=	NodeLibrary.setGroupById(NodeLibrary.groupURI_BLANK,id);
		id		=	NodeLibrary.setSubById(NodeLibrary.BLANK, id);
		IntegerBinding.intToEntry(id, nodeId);
		nodesDB.put(null, nodeId, value);
		return id;
	}
	
	public int addNodeToStore(Node node){
		isEmpty	=	false;

		int nodeId	=	getIdByNode(node);

		if	(nodeId == NODE_NOT_EXIST ){
			if (node.isBlank()){
				nodeId	=	addAnonIdToStore(node);
			}
			if (node.isURI()){
				nodeId	=	addUriToStore(node);
			}
			if (node.isLiteral()){
				nodeId	=	addLiteralToStore(node);
			}
		
			DatabaseEntry nodeIdEnt =	new	DatabaseEntry();
			IntegerBinding.intToEntry(nodeId, nodeIdEnt);
			
			DatabaseEntry	key	=	new DatabaseEntry();
			IntegerBinding.intToEntry(NodeLibrary.createNodeKey(node), key);
			
			
			indexDB.put(null,nodeIdEnt, key);
			return nodeId;
		}else{
			return nodeId;
		}
		
		
	
	}

	private int getLastId(){
		DatabaseEntry	id	=	new DatabaseEntry();
		if(lastIdDB.get(null, new DatabaseEntry((NODESDB_NAME+"LAST").getBytes()), id, LockMode.DEFAULT)==OperationStatus.SUCCESS){
			return IntegerBinding.entryToInt(id);
		}
		return 0;
	}
	
	private void updateLastId(int lastId){
		DatabaseEntry id	=	new DatabaseEntry();
		IntegerBinding.intToEntry(lastId,id);
		lastIdDB.put(null, new DatabaseEntry((NODESDB_NAME+"LAST").getBytes()), id);
	}

	
//=================UNDER CONSTRUCTION PROGRESS================================================================================================	
	public OperationStatus deleteNode(Node node){
		int id = getIdByNode(node);
		if (id == NODE_NOT_EXIST) return OperationStatus.KEYEMPTY;
		else{
			DatabaseEntry delKey = new DatabaseEntry();
			IntegerBinding.intToEntry(id, delKey);
			if   (index2ndDB.delete(null, delKey) == OperationStatus.SUCCESS){
				return nodesDB.delete(null,delKey);
			}else
				return index2ndDB.delete(null, delKey);
		}
	}	

	public void sync(){
		lastIdDB.sync();
		nodesDB.sync();
		indexDB.sync();
		index2ndDB.sync();
	}
	
	public void close() {
		lastIdDB.close();
		nodesDB.close();
		indexDB.close();
		index2ndDB.close();
		isClosed = 	true;
	}
	
	public void clear(){
		conf.remove(LASTIDDB_NAME);
		conf.remove(NODESDB_NAME);
		conf.remove(INDEXDB_NAME);
		conf.remove(INDEX2NDDB_NAME);
		isEmpty	=	true;
	}

	public boolean isClosed(){
		return isClosed;
	}
	
	public boolean isEmpty(){
		return isEmpty;
	}
}
