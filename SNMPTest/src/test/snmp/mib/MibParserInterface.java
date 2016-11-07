package test.snmp.mib;

public abstract interface MibParserInterface {
	public abstract void newMibParseToken(DwSnmpMibRecord paramDwSnmpMibRecord);

	public abstract void parseMibError(String paramString);
}