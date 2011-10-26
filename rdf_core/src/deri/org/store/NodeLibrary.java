package deri.org.store;

import java.io.UnsupportedEncodingException;
import java.security.DigestException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

//import org.openjena.atlas.lib.BitsIntt// Try to build one own
import com.hp.hpl.jena.datatypes.RDFDatatype;
import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.shared.JenaException;

public class NodeLibrary {
	//group1 Literal
    public static final int LITERAL       			= 0;
    //group2 URI/BLANK
    public static final int groupURI_BLANK     			= 3 ;
    	public static final int URI 	= 0 ;
    	public static final int BLANK   = 1 ;
    //group 3
    public static final int groupNumeric				= 2;
    	public static final int subgrouplow					= 0;
    	public static final int subgrouphigh				= 1;
    		
    	
    		public static final int XSDnonNegativeInteger   	= 0;//
    		public static final int XSDunsignedLong       		= 1;//
    		public static final int XSDunsignedInt       		= 2;//
    		public static final int XSDunsignedShort       		= 3;//
    		public static final int XSDdecimal       			= 4;			
    		public static final int XSDfloat       				= 5;			
    		public static final int XSDdouble       			= 6;			
    		public static final int XSDduration       			= 7;
    		public static final int XSDint       				= 8;//	  
    		public static final int XSDinteger       			= 9;
    		public static final int XSDshort       				= 10;//
    		public static final int XSDbyte       				= 11;//
    		public static final int XSDhexBinary       			= 12;
    	    public static final int XSDnegativeInteger      	= 13;
    	    public static final int XSDlong       				= 14;
    	    public static final int XSDnonPositiveInteger   	= 15;
    	    
    //group 4
    public static final int other						= 1;
     	public static final int groupDT					= 0;	
     		public static final int XSDtime       			= 0;			//numeric
     		public static final int XSDdate       			= 1;			
     		public static final int XSDgYearMonth       	= 2;
     		public static final int XSDgMonthDay       		= 3;
     		public static final int XSDgYear       			= 4;
     		public static final int XSDgDay       			= 5;
     		public static final int XSDdateTime       		= 6;	
     		public static final int XSDboolean       		= 7;
     	public static final int groupOtherLiteral	 	= 1;
     		public static final int XSDstring       		= 0;
        	public static final int XSDlanguage 			= 1;
        	public static final int XSDnormalizedString     = 2;
        	public static final int XSDtoken 				= 3;
        	public static final int XSDName 				= 4;
        	public static final int XSDQName 				= 5;
        	public static final int XSDNMTOKEN 				= 6;
        	public static final int XSDENTITY 				= 7;
        	public static final int XSDID 					= 8;
        	public static final int XSDNCName 				= 9;
        	public static final int XSDIDREF 				= 10;
       	    public static final int XSDbase64Binary       	= 11;
       	    public static final int XSDanyURI       		= 12;
       	    public static final int XSDNOTATION       		= 13;
       	    
       	    public static final int XSDpositiveInteger		= 14;
       	    public static final int XSDunsignedByte			= 15;//
    
    public static int setGroupById(int group, int id){
    	//Set group
    	id		=	BitsInt.pack(id, group, 29, 31);
    	return id;
    }
    
    public static int setSubById(int subGroup, int id){
    	//set subgroup
    	id		=	BitsInt.pack(id, subGroup, 28, 29);
    	return id;
    }
    
    public static int setTypeById(int type,int id){
    	//set type;
    	id		=	BitsInt.pack(id, type, 24, 28);
    	return id;
    }
    
    public static int getGroupById(int id){
    	return BitsInt.unpack(id, 29, 31);
    }
    
    public static int getSubGroupById(int id){
    	return BitsInt.unpack(id, 28, 29);
    }
    
    public static int getTypeById(int id){
    	return BitsInt.unpack(id, 24, 28);
    }
    
    
    
    
    public static final int NODEDOESNOTEXIST	= -8;
    public static final int NODE_ANY			= -9;
    
    	
	
	public static final boolean	isAny(long id)		{return id	 == NODE_ANY;}
	public static final boolean notExist(long id)	{return id	==	NODEDOESNOTEXIST;}
	
	

	public static int nodeToId(Node node){
		
		RDFDatatype  rdfDatatype	=	node.getLiteralDatatype();
		if (rdfDatatype!=null){
			if (rdfDatatype.equals(XSDDatatype.XSDgDay)){ 
				int id=0;
				String date = node.getLiteralLexicalForm();
				int	   intDate =	Integer.parseInt(date);
				id	= 	setGroupById(other,id);
				id	= 	setSubById(groupDT,id);
				id	=	setTypeById(XSDgDay,id);
				id	=	BitsInt.pack(id, intDate, 0, 9);
				return id;
			}
			
			if (rdfDatatype.equals(XSDDatatype.XSDgYear)){ 
				int id=0;
				String date = node.getLiteralLexicalForm();
				int	   intDate =	Integer.parseInt(date);
				id	= 	setGroupById(other,id);
				id	= 	setSubById(groupDT,id);
				id	=	setTypeById(XSDgYear,id);
				id	=	BitsInt.pack(id, intDate, 0, 15);
				return id;
			}
			
			if (rdfDatatype.equals(XSDDatatype.XSDboolean)){ 
				int id		=	0;
				String bool	=	node.getLiteralLexicalForm();
				id	= 	setGroupById(other,id);
				id	= 	setSubById(groupDT,id);
				id	=	setTypeById(XSDboolean,id);
				if (bool.equals("true")){
					id	=	BitsInt.pack(id,1 , 0, 1);
				}else{
					id	=	BitsInt.pack(id,0 , 0, 1);
				}
				return id;
			}
		
			//======================================group Numeric==========================//
			if (rdfDatatype.equals(XSDDatatype.XSDunsignedByte)){
				int id			=	0;
				String lexical	=	node.getLiteralLexicalForm();
				byte value		=	Byte.parseByte(lexical);
				if (value <0) value	=	(byte) Math.abs(value);
					id	=	setGroupById(other,id);
					id	=	setSubById(groupOtherLiteral, id);
					id	=	setTypeById(XSDunsignedByte,id);
					id	=	BitsInt.pack(id, value,0,16);
					id	=	BitsInt.pack(id, 1, 16,17);
				return id;
			}
			if (rdfDatatype.equals(XSDDatatype.XSDunsignedShort)){
				int id			=	0;
				String lexical	=	node.getLiteralLexicalForm();
				short	value		=	Short.parseShort(lexical);
				if (value <0) value	=	(short) Math.abs(value);
					id	=	setGroupById(groupNumeric,id);
					id	=	setSubById(subgrouphigh, id);
					id	=	setTypeById(XSDunsignedShort,id);
					id	=	BitsInt.pack(id, value,0,16);
					id	=	BitsInt.pack(id, 1, 16,17);
				return id;
			}
			
			if (rdfDatatype.equals(XSDDatatype.XSDunsignedInt)){
				int id			=	0;
				String lexical	=	node.getLiteralLexicalForm();
				int	value		=	Integer.parseInt(lexical);
				if (value <0) value	=	Math.abs(value);
				if (value> 268435455) return 0;
					id	=	setGroupById(groupNumeric,id);
					id	=	setSubById(subgrouplow, id);
					id	=	BitsInt.pack(id,1,27,28);
					id	=	BitsInt.pack(id,value,0,27);
				return id;
			}
			
			if (rdfDatatype.equals(XSDDatatype.XSDunsignedLong)){
				int id			=	0;
				String lexical	=	node.getLiteralLexicalForm();
				long	value		=	Long.parseLong(lexical);
				if (value <0) value	=	Math.abs(value);
				if (value> 268435455) return 0;
					id	=	setGroupById(groupNumeric,id);
					id	=	setSubById(subgrouplow, id);
					id	=	BitsInt.pack(id, 1,	27,	28);
					id	=	BitsInt.pack(id, (int)value,0, 27);
				return id;
			}
			
			if (rdfDatatype.equals(XSDDatatype.XSDpositiveInteger)){
				int id			=	0;
				String lexical	=	node.getLiteralLexicalForm();
				int	value		=	Integer.parseInt(lexical);
				if (value <0) value	=	Math.abs(value);
				if (value> 268435455) return 0;
					id	=	setGroupById(groupNumeric,id);
					id	=	setSubById(subgrouplow, id);
					id	=	BitsInt.pack(id, 1,27,28);
					id	=	BitsInt.pack(id, value,0, 27);
				return id;
			}
			
			if (rdfDatatype.equals(XSDDatatype.XSDnonNegativeInteger)){
				int id			=	0;
				String lexical	=	node.getLiteralLexicalForm();
				int	value		=	Integer.parseInt(lexical);
				if (value <0) value	=	Math.abs(value);
				if (value> 268435455) return 0;
					id	=	setGroupById(groupNumeric,id);
					id	=	setSubById(subgrouplow, id);
					id	=	BitsInt.pack(id, 1,27,28);
					id	=	BitsInt.pack(id, value,0, 27);
				return id;
			}
			
			if (rdfDatatype.equals(XSDDatatype.XSDbyte)){
				int id			=	0;
				String lexical	=	node.getLiteralLexicalForm();
				byte value		=	Byte.parseByte(lexical);
				if (value <0) {
					value	=	(byte) Math.abs(value);
					id		=	BitsInt.pack(id, 0, 16,17);
				}else{
					id		=	BitsInt.pack(id, 1, 16,17);
				}
					id	=	setGroupById(groupNumeric,id);
					id	=	setSubById(subgrouphigh, id);
					id	=	setTypeById(XSDbyte,id);
					id	=	BitsInt.pack(id, value,0,16);
				return id;
			}
			
			if (rdfDatatype.equals(XSDDatatype.XSDshort)){
				int id			=	0;
				String lexical	=	node.getLiteralLexicalForm();
				short	value		=	Short.parseShort(lexical);
				if (value <0) {
					value	=	(short)Math.abs(value);
					id		=	BitsInt.pack(id, 0, 16,17);
				}else{
					id		=	BitsInt.pack(id, 1, 16,17);
				}
					id	=	setGroupById(groupNumeric,id);
					id	=	setSubById(subgrouphigh, id);
					id	=	setTypeById(XSDshort,id);
					id	=	BitsInt.pack(id, value,0,16);
				return id;
			}
			
			if (rdfDatatype.equals(XSDDatatype.XSDint)){
				int id			=	0;
				String lexical	=	node.getLiteralLexicalForm();
				int	value		=	Integer.parseInt(lexical);
				if (value <0) {
					value	=	Math.abs(value);
					id		=	BitsInt.pack(id, 0, 27,28);
				}else{
					id		=	BitsInt.pack(id, 1, 27,28);
				}
				if (value> 268435455) return 0;
					id	=	setGroupById(groupNumeric,id);
					id	=	setSubById(subgrouplow, id);
					
					id	=	BitsInt.pack(id, value,0, 27);
				return id;
			}
			
			if (rdfDatatype.equals(XSDDatatype.XSDlong)){
				int id			=	0;
				String lexical	=	node.getLiteralLexicalForm();
				long	value		=	Long.parseLong(lexical);
				if (value <0) {
					value	=	Math.abs(value);
					id		=	BitsInt.pack(id, 0, 27,28);
				}else{
					id		=	BitsInt.pack(id, 1, 27,28);
				}
				if (value> 268435455) return 0;
					id	=	setGroupById(groupNumeric,id);
					id	=	setSubById(subgrouplow, id);
									id	=	BitsInt.pack(id, (int)value,0, 27);
				return id;
			}
			
			if (rdfDatatype.equals(XSDDatatype.XSDnegativeInteger)){
				int id			=	0;
				String lexical	=	node.getLiteralLexicalForm();
				int	value		=	Integer.parseInt(lexical);
				if (value <0) value	=	Math.abs(value);
				if (value> 268435455) return 0;
					id	=	setGroupById(groupNumeric,id);
					id	=	setSubById(subgrouplow, id);
					id	=	BitsInt.pack(id, 0,27,28);
					id	=	BitsInt.pack(id, value,0, 27);
				return id;
			}
			
			if (rdfDatatype.equals(XSDDatatype.XSDnonPositiveInteger)){
				int id			=	0;
				String lexical	=	node.getLiteralLexicalForm();
				int	value		=	Integer.parseInt(lexical);
				if (value <0) value	=	Math.abs(value);
				if (value> 268435455) return 0;
					id	=	setGroupById(groupNumeric,id);
					id	=	setSubById(subgrouplow, id);
					id	=	BitsInt.pack(id, 0,27,28);
					id	=	BitsInt.pack(id, value,0, 27);
				return id;
			}
		}
			
		
		
		
		return 0;
	}
		
	public static int createNodeKey(Node node){
		Hash	hash	=	new Hash(NodeStore.LEN_NODE_HASH);
		setHashForNode(hash,node);
		return hash.hashCode(); 
	}
	
	private static void setHashForNode(Hash hash, Node node){
		
		if (node.isURI()){
				hash(hash, node.getURI(), null, null, "URI");
				return;
		}
		if (node.isBlank()){	
				hash(hash, node.getBlankNodeLabel(), null, null, "BLANK");
				return;
		}
		
		if(node.isLiteral()){
				hash(hash, 
					 node.getLiteralLexicalForm(),
					 node.getLiteralLanguage(),
					 node.getLiteralDatatypeURI(),
					 "Literal");
				return;
		}else
		throw new JenaException("Attempt to hash something strange: " + node);
	}
	
	private static void hash(Hash h, String lex, String lang, String datatype, String nodeType){
		
		if (datatype == null) datatype = "";
		if (lang == null) lang = "";
		
		String toHash	=	lex + "|" + lang + "|" + datatype + "|" + nodeType;
		
		MessageDigest digest;
		
		try{
			digest = MessageDigest.getInstance("MD5");
		
			digest.update(toHash.getBytes("UTF-8"));
			
			if (h.getLen() == 16){
				digest.digest(h.getBytes(), 0, 16);
			}else{
				byte b[] = digest.digest();
				System.arraycopy(b, 0, h.getBytes(), 0, h.getLen());
			}
			return;
		}catch (NoSuchAlgorithmException e)	{
		}catch (UnsupportedEncodingException e){
		}catch (DigestException e){
		}
		return;
	}

}
