package test.snmp.trap;

import java.io.IOException;

import org.snmp4j.AbstractTarget;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.ScopedPDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.UserTarget;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.MPv3;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.security.AuthMD5;
import org.snmp4j.security.PrivAES128;
import org.snmp4j.security.PrivAES192;
import org.snmp4j.security.PrivAES256;
import org.snmp4j.security.PrivDES;
import org.snmp4j.security.SecurityLevel;
import org.snmp4j.security.SecurityModels;
import org.snmp4j.security.SecurityProtocols;
import org.snmp4j.security.USM;
import org.snmp4j.security.UsmUser;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

public class SnmpGetTester {

	// private static String ipAddress = "198.152.254.198";
	private String ip = "";
	private String port = "";

	// OID of MIB RFC 1213; Scalar Object =
	// .iso.org.dod.internet.mgmt.mib-2.system.sysDescr.0
	// private static String oidValue = ".1.3.6.1.2.1.25.2.2.0"; // ends with 0
	// for scalar object
	 // ends with 0
																// for scalar
																// object
	private int version = 0x01;

	private String community = "";

	private String securityName = "test"; // 보안암호
	private String authPassphrase = "dadmin01"; // 인증암호
	private String privacyPassphrase = "dadmin01"; // 개인정보암호
	private String userName = "test";
	private String authPassMethod = "AuthMD5";
	private String privPassMethod = "PrivAES128";
	
	private StringBuilder consoleString;

	public static void main(String[] args) throws Exception {
		String vali = "";
		String oidValue = ".1.3.6.1.2.1.1.4.0";
		SnmpGetTester tester = new SnmpGetTester("198.152.254.198","171","test");
		tester.SendSnmp(2,"GetNext",oidValue);

	}

	/**
	 * 생성자
	 * @param version > SnmpConstants.version1,SnmpConstants.version2c, SnmpConstants.version3
	 * @param ip
	 * @param port
	 * @param community
	 */
	public SnmpGetTester(String ip,String port,String community){
		this.ip = ip;
		this.port = port;
		this.community = community;
	}
	
	/**
	 * SNMP3 보안 정보 입력
	 * 
	 * @param securityName
	 * @param authPassphrase
	 * @param privacyPassphrase
	 * @param userName
	 */
	public void setSnmpV3Config(String securityName,String authPassphrase,String privacyPassphrase,String userName,String authPassMethod,String privPassMethod){
		this.securityName = securityName;
		this.authPassphrase = authPassphrase;
		this.privacyPassphrase = privacyPassphrase;
		this.userName = userName;
	}
	
	/**
	 * SNMP VERSION1,2 SETUP
	 * 
	 * @param snmpVersion
	 * @param cummunityName
	 * @param ipAddress
	 * @param portNumber
	 * @return
	 */
	private AbstractTarget SetSnmpVersion_1_2(int snmpVersion, String cummunityName, String ipAddress,
			String portNumber) {
		CommunityTarget comtarget = new CommunityTarget();
		comtarget.setCommunity(new OctetString(cummunityName));
		comtarget.setVersion(snmpVersion);
		comtarget.setAddress(new UdpAddress(ipAddress + "/" + portNumber));
		comtarget.setRetries(2);
		comtarget.setTimeout(1000);
		return comtarget;
	}

	
	/**
	 * 실행 메소드 타입 리턴
	 * 
	 * @param type
	 * @return
	 */
	private int getType(String type){
		if(type!=null && !type.isEmpty()){
			String typeName = type.toUpperCase();
			if(typeName.equals("GET")) return PDU.GET;
			else if(typeName.equals("GETNEXT")) return PDU.GETNEXT;
			else if(typeName.equals("GETBULK")) return PDU.GETBULK;	
		}
		return 0;
	}
	/**
	 * SNMP VERSION3 SETUP
	 * 
	 * @param cummunityName
	 * @param ipAddress
	 * @param portNumber
	 * @return
	 */
	private AbstractTarget SetSnmpVersion_3(String cummunityName, String ipAddress, String portNumber,String authPassMethod,String privPassMethod) {
		UserTarget usertarget = new UserTarget();
		usertarget.setVersion(SnmpConstants.version3);
		usertarget.setAddress(new UdpAddress(ipAddress + "/" + portNumber));
		usertarget.setSecurityLevel(SecurityLevel.AUTH_PRIV);
		usertarget.setSecurityName(new OctetString(cummunityName));
		usertarget.setTimeout(3000); // 3s
		usertarget.setRetries(0);
		this.authPassMethod = authPassMethod;
		this.privPassMethod  = privPassMethod;
		return usertarget;
	}

	public OID getPassMethod(String name){
		if(name.equals("AuthMD5")) return AuthMD5.ID;
		else if(name.equals("PrivDES")) return PrivDES.ID;
		else if(name.equals("PrivAES128")) return PrivAES128.ID;
		else if(name.equals("PrivAES192")) return PrivAES192.ID;
		else if(name.equals("PrivAES256")) return PrivAES256.ID;
		return AuthMD5.ID;
	}
	/**
	 * SNMP 요청 실행
	 * 
	 * @param snmpVersion
	 * @param type
	 * @param oid
	 * @throws IOException
	 */
	public String SendSnmp(int snmpVersion, String type, String oid) throws IOException {

		int pduType = this.getType(type);
		consoleString = new StringBuilder();
		System.out.println("snmpversion :"+snmpVersion + ",ss : " + consoleString.toString());
		if(pduType==0){
			System.out.println("Please input snmp request method(EX : GET,GETNEXT,GETBULK : " + type);
			consoleString.append("Please input snmp request method(EX : GET,GETNEXT,GETBULK : " + type);
			return consoleString.toString();
		}
		System.out.println("SNMP GET Demo");
		consoleString.append("SNMP GET Demo\n");

		// Create TransportMapping and Listen
		TransportMapping transport = new DefaultUdpTransportMapping();

		USM usm = new USM(SecurityProtocols.getInstance(), new OctetString(MPv3.createLocalEngineID()), 0);
		SecurityModels.getInstance().addSecurityModel(usm);

		transport.listen();

		// Create Snmp object for sending data to Agent
		Snmp snmp = new Snmp(transport);
		AbstractTarget target = null;

		// Create Target Address object
		if (version != SnmpConstants.version3) {
			target = this.SetSnmpVersion_1_2(version, community, ip, port);
		} else {
			//AuthMD5, PrivDES, PrivAES, PrivAES128, PrivAES192 and PrivAES256
			UsmUser user = new UsmUser(new OctetString(securityName), this.getPassMethod(authPassMethod), new OctetString(authPassphrase),
					this.getPassMethod(privPassMethod), new OctetString(privacyPassphrase));
			if (user == null){
				System.out.println("User info is null...:(");
				consoleString.append("User info is null...:(\n");
			}
			else{
				System.out.println("User is ok :) " + user.getSecurityName());
				consoleString.append("User is ok :) " + user.getSecurityName()+"\n");
			}
			snmp.getUSM().addUser(new OctetString(userName), user);
			//target = this.SetSnmpVersion_3(community, ip, port);

		}
		if (target != null) {
			// Create the PDU object
			PDU pdu = null;
			if (version != SnmpConstants.version3) pdu = new PDU();
			else pdu = new ScopedPDU();

			pdu.add(new VariableBinding(new OID(oid)));
			pdu.setType(pduType);
			pdu.setRequestID(new Integer32(1));

			System.out.println("Sending Request to Agent...");
			// Type Request
			ResponseEvent response = null;
			if (pdu.getType() == PDU.GET)
				response = snmp.get(pdu, target);
			else if (pdu.getType() == PDU.GETNEXT)
				response = snmp.getNext(pdu, target);
			else if (pdu.getType() == PDU.GETBULK)
				snmp.getBulk(pdu, target);
			snmp.close();

			// Process Agent Response
			if (response != null) {
				System.out.println("Got Response from Agent");

				PDU responsePDU = response.getResponse();

				if (responsePDU != null) {
					int errorStatus = responsePDU.getErrorStatus();
					int errorIndex = responsePDU.getErrorIndex();
					String errorStatusText = responsePDU.getErrorStatusText();

					if (errorStatus == PDU.noError) {
						System.out.println("Snmp Get Response = " + responsePDU.getVariableBindings());
						consoleString.append("Snmp Get Response = " + responsePDU.getVariableBindings());
					} else {
						System.out.println("Error: Request Failed");
						System.out.println("Error Status = " + errorStatus);
						System.out.println("Error Index = " + errorIndex);
						System.out.println("Error Status Text = " + errorStatusText);
						
						consoleString.append("Error: Request Failed\n");
						consoleString.append("Error Status = " + errorStatus+"\n");
						consoleString.append("Error Index = " + errorIndex+"\n");
						consoleString.append("Error Status Text = " + errorStatusText+"\n");
					}
				} else {
					System.out.println("Error: Response PDU is null");
					consoleString.append("Error: Response PDU is null");
				}
			} else {
				System.out.println("Error: Agent Timeout... ");
				consoleString.append("Error: Agent Timeout... ");
			}
		}
		return consoleString.toString();

	}
	
	public String getMessage(){
		return consoleString.toString();
	}

}
