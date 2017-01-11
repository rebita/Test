package test.snmp.parse;

public interface DwMibParserInterface
{
	void newMibParseToken(DwSnmpMibRecord rec);
	void parseMibError(String s);
}
