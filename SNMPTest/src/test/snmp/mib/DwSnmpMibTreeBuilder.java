package test.snmp.mib;

import java.io.File;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class DwSnmpMibTreeBuilder implements MibParserInterface, Runnable {
	DwSnmpMibOutputHandler output = null;
	private DefaultMutableTreeNode rootNode;
	private DefaultMutableTreeNode treeRootNode;
	private DefaultMutableTreeNode rootOrphan;
	private DefaultMutableTreeNode rootVariable;
	private DefaultMutableTreeNode rootVariableTable;
	JTree tree;
	private Vector fileVect;
	private Vector orphanNodes;
	private String errorMsg = "";

	//public DwSnmpOidSupport oidSupport = new DwSnmpOidSupport();
	DwSnmpMibTreeHash treeHash;
	DwSnmpMibTreeHash variableHash;
	DwSnmpMibTreeHash orphanHash;

	public DwSnmpMibTreeBuilder() {
		DwSnmpMibRecord treeRootRec = new DwSnmpMibRecord();
		treeRootRec.name = "MIB Browser";
		treeRootRec.parent = "MIB Browser";
		treeRootRec.number = 0;
		this.treeRootNode = new DefaultMutableTreeNode(treeRootRec);

		DwSnmpMibRecord rootRec = new DwSnmpMibRecord();
		rootRec.name = "root";
		rootRec.parent = "MIB Browser";
		rootRec.number = 1;
		this.rootNode = new DefaultMutableTreeNode(rootRec);

		DwSnmpMibRecord rootOrphanRec = new DwSnmpMibRecord();
		rootOrphanRec.name = "Orphans";
		rootOrphanRec.parent = "MIB Browser";
		rootOrphanRec.description = "This subtree contains MIB Records whose parent cannot be traced";
		rootOrphanRec.number = 10;
		this.rootOrphan = new DefaultMutableTreeNode(rootOrphanRec);

		DwSnmpMibRecord rootVariableRec = new DwSnmpMibRecord();
		rootVariableRec.name = "Variables/Textual Conventions";
		rootVariableRec.parent = "MIB Browser";
		rootVariableRec.description = "This subtree contains all the variables which map to the standard variables.";
		rootVariableRec.number = 11;
		this.rootVariable = new DefaultMutableTreeNode(rootVariableRec);

		DwSnmpMibRecord rootVariableTableRec = new DwSnmpMibRecord();
		rootVariableTableRec.name = "Table Entries";
		rootVariableTableRec.parent = "Variables/Textual Conventions";
		rootVariableTableRec.description = "This branch contains a list of sequences for all the tables ";
		rootVariableTableRec.number = 12;
		this.rootVariableTable = new DefaultMutableTreeNode(rootVariableTableRec);

		this.treeHash = new DwSnmpMibTreeHash();
		this.treeHash.put(rootRec.name, this.rootNode);

		this.variableHash = new DwSnmpMibTreeHash();
		this.orphanHash = new DwSnmpMibTreeHash();

		this.orphanNodes = new Vector();
		this.fileVect = new Vector();
		clearError();
	}

	public DefaultMutableTreeNode getRootNode() {
		return this.rootNode;
	}

	public boolean addFile(String fName) {
		if (fName == null)
			return false;
		File mibFile = new File(fName);
		if (mibFile.exists() != true)
			return false;
		this.fileVect.add(fName);
		return true;
	}

	public boolean addDirectory(String dirName) {
		System.out.println("Adding directory : " + dirName);
		File dir = new File(dirName);
		if (dir.isDirectory() != true)
			return false;
		File[] files = dir.listFiles();
		if (files == null)
			return false;
		for (int i = 0; i < files.length; ++i) {
			this.fileVect.add(files[i].getPath());
		}
		return true;
	}

	public String[] getFiles() {
		try {
			Enumeration enu = this.fileVect.elements();
			String[] returnStr = new String[this.fileVect.size()];

			int i = 0;
			while (enu.hasMoreElements()) {
				returnStr[(i++)] = ((String) enu.nextElement());
			}
			clearError();
			return returnStr;
		} catch (Exception e) {
			setError("Error in getting filenames..\n" + e.toString());
		}
		return null;
	}

	private void clearError() {
		this.errorMsg = "";
	}

	private void setError(String err) {
		this.errorMsg = err;
	}

	public JTree buildTree() {
		if (this.fileVect.size() == 0) {
			setError("Error : Please add files first");
			return null;
		}

		//this.oidSupport = new DwSnmpOidSupport();
		Thread treeThread = new Thread(this);
		treeThread.setPriority(9);
		treeThread.start();

		this.treeRootNode.add(this.rootNode);
		this.treeRootNode.add(this.rootOrphan);
		this.rootVariable.add(this.rootVariableTable);
		this.treeRootNode.add(this.rootVariable);
		this.tree = new JTree(this.treeRootNode);
		this.tree.putClientProperty("JTree.lineStyle", "Angled");
		this.tree.getSelectionModel().setSelectionMode(1);
		return this.tree;
	}

	public void run() {
		Enumeration enu = this.fileVect.elements();
		String fileName = "";
		JTree newTree = null;
		while (enu.hasMoreElements()) {
			fileName = (String) enu.nextElement();
			loadFile(fileName);
		}
		updateOrphans();
		this.output.println("*****COMPLETED******");
	}

	private void loadFile(String fileName) {
		this.output.print("Adding file " + fileName);
		if (parseFile(fileName) < 0)
			outputError(".. Error");
		else
			this.output.print("..Done\n");
	}

	public boolean loadNewFile(String fName) {
		if (fName == null)
			return false;
		File mibFile = new File(fName);
		if (mibFile.exists() != true)
			return false;
		if (this.fileVect.indexOf(fName) == -1) {
			this.tree.collapsePath(this.tree.getSelectionPath());
			this.fileVect.add(fName);
			loadFile(fName);
			updateOrphans();
			return true;
		}
		return false;
	}

	private void updateOrphans() {
		outputText("Updating orphans.");
		DwSnmpMibRecord orphanRec = null;

		boolean contFlag = true;

		while (contFlag == true) {
			contFlag = false;
			Enumeration orphanEnu = this.orphanNodes.elements();
			while (orphanEnu.hasMoreElements()) {
				DefaultMutableTreeNode orphanNode = (DefaultMutableTreeNode) orphanEnu.nextElement();

				if (addNode(orphanNode) == true) {
					contFlag = true;
					this.orphanNodes.remove(orphanNode);
				}

			}

			this.output.print(".");
		}
		this.output.print("Done");
		this.output.print("\nBuilding OID Name resolution table...");
		//this.oidSupport.buildOidToNameResolutionTable(this.rootNode);

		Enumeration orphanEnu = this.orphanNodes.elements();
		while (orphanEnu.hasMoreElements()) {
			DefaultMutableTreeNode orphanNode = (DefaultMutableTreeNode) orphanEnu.nextElement();
			orphanRec = (DwSnmpMibRecord) orphanNode.getUserObject();
			if (orphanRec.recordType != DwSnmpMibRecord.recVariable)
				;
			if (orphanRec.recordType == DwSnmpMibRecord.recTable) {
				this.rootVariable.add(orphanNode);
			}

			if (this.treeHash.containsKey(orphanRec.name) != true)
				this.rootOrphan.add(orphanNode);

		}

		outputText("Updating variables table..");
		Enumeration enuVar = this.variableHash.elements();

		while (enuVar.hasMoreElements()) {
			DwSnmpMibRecord varRec = (DwSnmpMibRecord) enuVar.nextElement();
			this.rootVariable.add(new DefaultMutableTreeNode(varRec));
		}

		if ((this.tree != null) && (this.tree.getModel() != null)) {
			((DefaultTreeModel) this.tree.getModel()).reload();
			this.tree.revalidate();
			this.tree.repaint();
		}
		outputText("Done");
	}

	private int parseFile(String fName) {
		SnmpMibParser fParser = new SnmpMibParser(fName, this);
		if (this.output != null)
			fParser.setOutput(this.output);
		return fParser.parseMibFile();
	}

	private boolean addRecord(DwSnmpMibRecord childRec) {
		int parseStatus = 0;
		if (childRec == null)
			return false;
		DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(childRec);
		if (!(addNode(newNode))) {
			this.orphanNodes.add(newNode);
			this.orphanHash.put(childRec.name, newNode);

			return false;
		}
		return true;
	}

	private boolean addNode(DefaultMutableTreeNode newNode) {
		DwSnmpMibRecord newRec = (DwSnmpMibRecord) newNode.getUserObject();

		DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) this.treeHash.get(newRec.parent);

		if (parentNode == null)
			return false;

		DwSnmpMibRecord parentRec = (DwSnmpMibRecord) parentNode.getUserObject();
		if (parentRec.recordType > 0)
			newRec.recordType = (parentRec.recordType + 1);

		DefaultMutableTreeNode dupNode = isChildPresent(newNode);
		if (dupNode == null) {
			try {
				parentNode.add(newNode);
				newNode.setParent(parentNode);

				this.treeHash.put(newRec.name, newNode);
				return true;
			} catch (Exception e) {
				System.out.println("Err in Child : " + newRec.name + "Parent : " + newRec.parent);
				return false;
			}
		}

		Enumeration dupChildren = newNode.children();
		while (dupChildren.hasMoreElements()) {
			DefaultMutableTreeNode dupChildNode = (DefaultMutableTreeNode) dupChildren.nextElement();
			if (isChildPresent(dupChildNode) == null)
				dupNode.add(dupChildNode);
		}
		return true;
	}

	DefaultMutableTreeNode isChildPresent(DefaultMutableTreeNode childNode) {
		DwSnmpMibRecord childRec = (DwSnmpMibRecord) childNode.getUserObject();
		return isChildPresent(childRec);
	}

	DefaultMutableTreeNode isChildPresent(DwSnmpMibRecord rec) {
		DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) this.treeHash.get(rec.parent);
		if (parentNode == null)
			parentNode = (DefaultMutableTreeNode) this.orphanHash.get(rec.parent);
		if (parentNode == null)
			return null;
		Enumeration enuChildren = parentNode.children();
		DefaultMutableTreeNode childNode;
		while ((enuChildren != null) && (enuChildren.hasMoreElements())) {
			childNode = (DefaultMutableTreeNode) enuChildren.nextElement();
			DwSnmpMibRecord childRec = (DwSnmpMibRecord) childNode.getUserObject();
			if (childRec.name.equals(rec.name) == true) {
				return childNode;
			}
		}

		return null;
	}

	public void setOutput(DwSnmpMibOutputHandler output) {
		this.output = output;
	}

	void outputText(String s) {
		try {
			this.output.println(s);
		} catch (Exception e) {
			System.out.println(s);
		}
	}

	void outputError(String s) {
		try {
			this.output.printError(s);
		} catch (Exception e) {
			System.out.println(s);
		}
	}

	public void newMibParseToken(DwSnmpMibRecord rec) {
		addRecord(rec);
	}

	public void parseMibError(String s) {
		outputError(s);
	}
}