package deri.org.store;

import java.util.Arrays;

public class Hash {
	
	byte[]	bytes;
	
	public Hash(int len)		{ bytes	=	new byte[len];}
	public int getLen()			{ return bytes.length;}
	public byte [] getBytes() 	{ return bytes;}
	
	@Override
	public int hashCode(){
		return Arrays.hashCode(bytes);
	}
	
	@Override
	public boolean equals(Object other){
		if (this == other) return true;
		if (!(other instanceof Hash)) return false;
		
		boolean b = Arrays.equals(bytes, ((Hash)other).bytes);
		return b;
	}
	
	@Override
	public String toString(){
		return "hash:" + "Bytes.asHex(bytes)";
	}
	
}
