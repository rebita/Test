package test.client;

import java.awt.Button;
import java.awt.Choice;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

public class SNMPGetClient extends JFrame implements ActionListener {

	private JPanel contentPane;
	private TextField textAdress;
	private TextField textPort;
	private TextField textSecureName;
	private TextArea textAreaConsole;
	private Choice choiceVersion;
	private TextField textUserID;
	private Choice choiceUserPW;
	private TextField textSecurePW;
	private Choice choicePrivPW;
	private TextField textOid;
	private TextField textPrivPW;
	private Choice choiceMethod;

	private HashMap getThread;
	private int i;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SNMPGetClient frame = new SNMPGetClient();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public SNMPGetClient() {
		getThread = new HashMap();
		i = 0;

		setTitle("SNMP \uD638\uCD9C \uD14C\uC2A4\uD2B8");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 534, 692);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 521, 0 };
		gbl_contentPane.rowHeights = new int[] { 35, 40, 40, 0, 0, 0, 51, 306, 45, 0 };
		gbl_contentPane.columnWeights = new double[] { 0.0, Double.MIN_VALUE };
		gbl_contentPane.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		contentPane.setLayout(gbl_contentPane);

		Panel panel = new Panel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.anchor = GridBagConstraints.NORTHWEST;
		gbc_panel.insets = new Insets(0, 0, 5, 0);
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		contentPane.add(panel, gbc_panel);

		Label labelAddress = new Label("\uC8FC\uC18C");
		panel.add(labelAddress);

		textAdress = new TextField();
		textAdress.setText("198.152.254.198");
		panel.add(textAdress);

		Label labelPort = new Label("\uD3EC\uD2B8");
		panel.add(labelPort);

		textPort = new TextField();
		textPort.setText("161");
		panel.add(textPort);
		
		Label labelMethod = new Label("\uD638\uCD9C");
		panel.add(labelMethod);
		
		choiceMethod = new Choice();
		choiceMethod.add("GETNEXT");
		choiceMethod.add("GET");
		choiceMethod.add("GETBULK");
		panel.add(choiceMethod);

		Panel panel_1 = new Panel();
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.anchor = GridBagConstraints.WEST;
		gbc_panel_1.insets = new Insets(0, 0, 5, 0);
		gbc_panel_1.gridx = 0;
		gbc_panel_1.gridy = 1;
		contentPane.add(panel_1, gbc_panel_1);

		Label lableSecureName = new Label("\uBCF4\uC548\uC774\uB984");
		panel_1.add(lableSecureName);

		textSecureName = new TextField();
		textSecureName.setText("public");
		panel_1.add(textSecureName);

		Label labelVersion = new Label("\uBC84\uC804");
		panel_1.add(labelVersion);

		choiceVersion = new Choice();
		choiceVersion.add("1");
		choiceVersion.add("2");
		choiceVersion.add("3");
		panel_1.add(choiceVersion);

		Label labelOid = new Label("OID");
		panel_1.add(labelOid);

		textOid = new TextField();
		textOid.setText(".1.3.6.1.2.1.25.3.2.1.1");
		panel_1.add(textOid);

		Label labelV3Config = new Label("\uBC84\uC8043\uBCF4\uC548\uAC12");
		labelV3Config.setForeground(Color.GRAY);
		GridBagConstraints gbc_labelV3Config = new GridBagConstraints();
		gbc_labelV3Config.insets = new Insets(0, 0, 5, 0);
		gbc_labelV3Config.gridx = 0;
		gbc_labelV3Config.gridy = 2;
		contentPane.add(labelV3Config, gbc_labelV3Config);

		Panel panel_2 = new Panel();
		GridBagConstraints gbc_panel_2 = new GridBagConstraints();
		gbc_panel_2.insets = new Insets(0, 0, 5, 0);
		gbc_panel_2.gridx = 0;
		gbc_panel_2.gridy = 3;
		contentPane.add(panel_2, gbc_panel_2);

		Label labelUserID = new Label("\uC720\uC800ID");
		panel_2.add(labelUserID);

		textUserID = new TextField();
		textUserID.setText("TEST");
		panel_2.add(textUserID);

		Label labelSecureType = new Label("\uC554\uD638\uC885\uB958");
		panel_2.add(labelSecureType);

		choiceUserPW = new Choice();
		choiceUserPW.add("AuthMD5");
		choiceUserPW.add("PrivDES");
		choiceUserPW.add("PrivAES128");
		choiceUserPW.add("PrivAES192");
		choiceUserPW.add("PrivAES256");

		panel_2.add(choiceUserPW);

		Label labelAuthPW = new Label("\uC778\uC99D\uC554\uD638");
		panel_2.add(labelAuthPW);

		textSecurePW = new TextField();
		textSecurePW.setText("dadmin01");
		panel_2.add(textSecurePW);

		Panel panel_3 = new Panel();
		GridBagConstraints gbc_panel_3 = new GridBagConstraints();
		gbc_panel_3.insets = new Insets(0, 0, 5, 0);
		gbc_panel_3.gridx = 0;
		gbc_panel_3.gridy = 4;
		contentPane.add(panel_3, gbc_panel_3);

		Label labelPrivSecureType = new Label("\uAC1C\uC778\uC554\uD638\uC885\uB958");
		panel_3.add(labelPrivSecureType);

		choicePrivPW = new Choice();
		choicePrivPW.add("PrivAES128");
		choicePrivPW.add("PrivAES192");
		choicePrivPW.add("PrivAES256");
		choicePrivPW.add("AuthMD5");
		choicePrivPW.add("PrivDES");
		panel_3.add(choicePrivPW);

		Label labelPrivPW = new Label("\uAC1C\uC778\uC778\uC99D\uC554\uD638");
		panel_3.add(labelPrivPW);

		textPrivPW = new TextField();
		textPrivPW.setText("damin01");
		panel_3.add(textPrivPW);

		Button buttonRequest = new Button("\uC694\uCCAD");

		GridBagConstraints gbc_buttonRequest = new GridBagConstraints();
		gbc_buttonRequest.fill = GridBagConstraints.HORIZONTAL;
		gbc_buttonRequest.insets = new Insets(0, 0, 5, 0);
		gbc_buttonRequest.gridx = 0;
		gbc_buttonRequest.gridy = 6;
		contentPane.add(buttonRequest, gbc_buttonRequest);

		textAreaConsole = new TextArea();
		textAreaConsole.setEnabled(false);
		GridBagConstraints gbc_textAreaConsole = new GridBagConstraints();
		gbc_textAreaConsole.insets = new Insets(0, 0, 5, 0);
		gbc_textAreaConsole.fill = GridBagConstraints.BOTH;
		gbc_textAreaConsole.gridx = 0;
		gbc_textAreaConsole.gridy = 7;
		contentPane.add(textAreaConsole, gbc_textAreaConsole);

		Panel panel_4 = new Panel();
		GridBagConstraints gbc_panel_4 = new GridBagConstraints();
		gbc_panel_4.gridx = 0;
		gbc_panel_4.gridy = 8;
		contentPane.add(panel_4, gbc_panel_4);

		Button buttonClean = new Button("\uCF58\uC194Clean");
		panel_4.add(buttonClean);

		buttonRequest.addActionListener(this);
		buttonClean.addActionListener(this);
		// buttonRequest.addActionListener(new ActionListener() {
		// public void actionPerformed(ActionEvent e) {
		// textAreaConsole.append("ho!"+choiceUserPW.getSelectedItem()+","+choicePrivPW.getSelectedItem()+","+choiceVersion.getSelectedItem());
		// //textAreaConsole.setText("setText!");
		//
		// }
		// });
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String temp = e.getActionCommand();

		if (temp.equals("요청")) {
			getThread.put(i, new SNMPGetThread(textAdress.getText(),textPort.getText(),textSecureName.getText(),choiceVersion.getSelectedItem(),textOid.getText(),choiceMethod.getSelectedItem()));
			SNMPGetThread tempThread = (SNMPGetThread) getThread.get(i);
			tempThread.setTextConsole(textAreaConsole);
			if(choiceVersion.getSelectedItem().equals("3")){
				tempThread.SNMPv3Config(textSecureName.getText(), textSecurePW.getText(), textPrivPW.getText(), textUserID.getText(), choiceUserPW.getSelectedItem(), choicePrivPW.getSelectedItem());
			}
			tempThread.start();
			i++;
		}else if(temp.equals("콘솔Clean")){
			SNMPGetThread tempThread = null;
			for(i=0;i<getThread.size();i++){
			tempThread = (SNMPGetThread) getThread.get(i);
			textAreaConsole.setText("clean success!\n");
			}
		}
	}

}
