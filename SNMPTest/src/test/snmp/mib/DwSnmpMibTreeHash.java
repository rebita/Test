package test.snmp.mib;

import java.util.Enumeration;
import java.util.Hashtable;

public class DwSnmpMibTreeHash {
	Hashtable treeHash;

	DwSnmpMibTreeHash() {
		this.treeHash = new Hashtable();
	}

	public Object get(String key) {
		return this.treeHash.get(key);
	}

	public void put(String key, Object value) {
		this.treeHash.put(key, value);
	}

	public boolean containsKey(String key) {
		return this.treeHash.containsKey(key);
	}

	public Enumeration elements() {
		return this.treeHash.elements();
	}
}