package test.snmp.parse;

import java.util.Enumeration;
import java.util.Hashtable;

public class DwSnmpMibTreeHash {
	
	Hashtable treeHash;
	
	DwSnmpMibTreeHash() {
		treeHash=new Hashtable();
	}
	
	public Object  get(String key) {
		return (treeHash.get(key));
	}
	
	public void put(String key,Object value) {
		treeHash.put(key,value);
	}
	
	public boolean containsKey(String key) {
		return (treeHash.containsKey(key));
	}
	
	public Enumeration elements() {
		return (treeHash.elements());
	}
}


