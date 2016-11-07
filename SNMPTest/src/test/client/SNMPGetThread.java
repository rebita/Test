/**
 * 
 */
package test.client;

import java.awt.TextArea;

import test.snmp.trap.SnmpGetTester;

/**
 * @author sung053
 *
 */
public class SNMPGetThread extends Thread {
	String ip;
	String port;
	String community;
	String version;
	String oid;

	String securityName;
	String authPassphrase;
	String privacyPassphrase;
	String userName;
	private String authPassMethod;
	private String privPassMethod;
	private String requestMethod;
	
	StringBuilder console;
	
	TextArea consoleArea;

	public SNMPGetThread(String ip, String port, String community, String version, String oid,String requestMethod) {
		this.ip = ip;
		this.port = port;
		this.community = community;
		this.version = version;
		this.oid = oid;
		this.requestMethod = requestMethod;
		console = new StringBuilder();
	}

	public void SNMPv3Config(String securityName, String authPassphrase, String privacyPassphrase, String userName, String authPassMethod, String privPassMethod) {
		this.securityName = securityName;
		this.authPassphrase = authPassphrase;
		this.privacyPassphrase = privacyPassphrase;
		this.userName = userName;
		this.authPassMethod = authPassMethod;
		this.privPassMethod = privPassMethod;
	}
	
	public void run() {
		SnmpGetTester tester = new SnmpGetTester(ip, port, community);
		try {
			if (version.equals("3")) {
				tester.setSnmpV3Config(securityName, authPassphrase, privacyPassphrase, userName,authPassMethod,privPassMethod);
				consoleArea.append(tester.SendSnmp(3, "GETNEXT", oid));
			} else if (version.equals("2")) {
				consoleArea.append("test");
				consoleArea.append(tester.SendSnmp(2, "GETNEXT", oid));
			} else {
				consoleArea.append(tester.SendSnmp(1, "GETNEXT", oid));
			}
			consoleArea.append("\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void setTextConsole(TextArea consoleArea){
		this.consoleArea = consoleArea;
	}
}
