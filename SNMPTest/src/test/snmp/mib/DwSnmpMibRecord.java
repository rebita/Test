package test.snmp.mib;

import java.io.Serializable;

public class DwSnmpMibRecord implements Serializable {
	public static final int VALUE_TYPE_NONE = 0;
	public static final int VALUE_TYPE_NULL = 1;
	public static final int VALUE_TYPE_INTEGER32 = 2;
	public static final int VALUE_TYPE_UNSIGNED_INTEGER32 = 3;
	public static final int VALUE_TYPE_OCTET_STRING = 4;
	public static final int VALUE_TYPE_OID = 5;
	public static final int VALUE_TYPE_TIMETICKS = 6;
	public static final int VALUE_TYPE_IP_ADDRESS = 7;
	public static int recNormal = 0;
	public static int recVariable = -1;
	public static int recTable = 1;

	public String name = "";
	public String parent = "";
	public int number = 0;
	public String description = "";
	public String access = "";
	public String status = "";
	public String syntax = "";
	public int recordType = recNormal;
	public int tableEntry = -1;
	public String index = "";

	DwSnmpMibRecord() {
		init();
	}

	public void init() {
		this.name = "";
		this.parent = "";
		this.number = 0;
		this.description = "";
		this.access = "";
		this.status = "";
		this.syntax = "";
		this.recordType = recNormal;
		this.index = "";
	}

	public String getCompleteString() {
		String returnVal = new String("");
		returnVal = returnVal.concat("Name   : " + this.name + "\n");
		returnVal = returnVal.concat("Parent : " + this.parent + "\n");
		returnVal = returnVal.concat("Number : " + this.number + "\n");
		returnVal = returnVal.concat("Access : " + this.access + "\n");
		returnVal = returnVal.concat("Syntax : " + this.syntax + "");
		returnVal = returnVal.concat("Status : " + this.status + "\n");
		if (this.index.equals("") != true)
			returnVal = returnVal.concat("Index : " + this.index + "\n");
		String desc = "";
		int i = 50;
		while (i < desc.length()) {
			desc = desc + this.description.substring(i - 50, i);
			desc = desc + "\n";
			i += 50;
		}

		desc = desc + this.description.substring(i - 50);
		returnVal = returnVal.concat("Description :" + desc + "\n");
		returnVal = returnVal.concat("Type :" + this.recordType + "\n");

		return returnVal;
	}

	public boolean isWritable() {
		return (this.access.toUpperCase().indexOf("WRITE") != -1);
	}

	public String toString() {
		return this.name;
	}

	public boolean checkValidValue(String setValue) {
		return true;
	}

	public int getSyntaxID() {
		String strType = this.syntax.trim().toUpperCase();
		if (strType.indexOf("INTEGER") != -1)
			return 2;
		if (strType.indexOf("COUNTER") != -1)
			return 3;
		if (strType.indexOf("STRING") != -1)
			return 4;
		if (strType.indexOf("OID") != -1)
			return 5;
		if (strType.indexOf("TIMETICK") != -1)
			return 6;
		if (strType.indexOf("IPADDRESS") != -1)
			return 7;
		if (strType.indexOf("NULL") != -1) {
			return 1;
		}

		return 0;
	}

	public String getSyntaxIDString() {
		switch (getSyntaxID()) {
		case 2:
			return "Integer";
		case 3:
			return "Unsigned Integer";
		case 4:
			return "Octet String";
		case 5:
			return "OID";
		case 6:
			return "Time Ticks";
		case 7:
			return "IP Address";
		case 1:
			return "Null";
		}
		return "Unknown";
	}
}