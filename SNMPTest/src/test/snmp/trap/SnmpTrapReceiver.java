package test.snmp.trap;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Vector;

import org.snmp4j.CommandResponder;
import org.snmp4j.CommandResponderEvent;
import org.snmp4j.MessageDispatcherImpl;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.mp.MPv1;
import org.snmp4j.mp.MPv2c;
import org.snmp4j.mp.MPv3;
import org.snmp4j.security.SecurityModels;
import org.snmp4j.security.SecurityProtocols;
import org.snmp4j.security.USM;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.TcpAddress;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultTcpTransportMapping;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.MultiThreadedMessageDispatcher;
import org.snmp4j.util.ThreadPool;

import test.snmp.parse.MibParser;

/**
 * 
 */

/**
 * @author sung053
 *
 */

public class SnmpTrapReceiver implements CommandResponder {

	private MultiThreadedMessageDispatcher dispatcher;
	private Snmp snmp = null;
	private org.snmp4j.smi.Address listenAddress;
	private ThreadPool threadPool;
	private MibParser parser;

	private void init() throws UnknownHostException, IOException {
		System.out.println("***** start *****");

		parser = new MibParser();
		// 포트162 바인드
		threadPool = ThreadPool.create("Trap", 2);
		dispatcher = new MultiThreadedMessageDispatcher(threadPool, new MessageDispatcherImpl());
		listenAddress = GenericAddress.parse(System.getProperty("snmp4j.listenAddress", "udp:0.0.0.0/162"));
		TransportMapping transport;
		if (listenAddress instanceof UdpAddress) {
			transport = new DefaultUdpTransportMapping((UdpAddress) listenAddress);
		} else {
			transport = new DefaultTcpTransportMapping((TcpAddress) listenAddress);
		}
		snmp = new Snmp(dispatcher, transport);
		snmp.getMessageDispatcher().addMessageProcessingModel(new MPv1());
		snmp.getMessageDispatcher().addMessageProcessingModel(new MPv2c());
		snmp.getMessageDispatcher().addMessageProcessingModel(new MPv3());
		USM usm = new USM(SecurityProtocols.getInstance(), new OctetString(MPv3.createLocalEngineID()), 0);
		SecurityModels.getInstance().addSecurityModel(usm);
		snmp.listen();
	}

	public void shutdown() throws IOException {
		// 스레드를캔슬하고 snmp를 닫음
		System.out.println("***** shutdown *****");
		threadPool.cancel();
		snmp.close();
	}

	public void run() {
		try {

			init();
			snmp.addCommandResponder(this);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		SnmpTrapReceiver snmpTrapReceiver = new SnmpTrapReceiver();
		snmpTrapReceiver.run();

		try {
			// １분후 종료
			Thread.sleep(60L * 5000L);
		} catch (Exception e) {
		}
		// snmpTrapReceiver.shutdown();

		// 종료처리

	}

	@Override
	public void processPdu(CommandResponderEvent event) {
		// TODO Auto-generated method stub
		// 수신한 내용을 표시
		StringBuffer msg = new StringBuffer();

		Vector vars = event.getPDU().getVariableBindings();
		int size = vars.size();
		for (int i = 0; i < vars.size(); i++) {
			VariableBinding var = (VariableBinding) vars.get(i);
			msg.append(var.getVariable().toString().trim());
			if (i < size - 1) {
				msg.append(", ");
			}
			System.out.println("***************************************************************************");
			System.out.println("** OID : " + var.getOid());
			System.out.println("** variable : " + var.getVariable());
			System.out.println("** syntax : " + var.getSyntax());
			System.out.println("** name : " + parser.getName(var.getOid().toString()));
			System.out.println("** Parent : " + parser.getParent(var.getOid().toString()));
			System.out.println("** Number : " + parser.getNumber(var.getOid().toString()));
			System.out.println("** Access : " + parser.getAccess(var.getOid().toString()));
			System.out.println("** Status : " + parser.getStatus(var.getOid().toString()));
			System.out.println("** Description : " + parser.getDescription(var.getOid().toString()));
			System.out.println("***************************************************************************");

		}
		System.out.println("msg = " + msg.toString());
	}

}
