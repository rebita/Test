package test.snmp.mib;

/*** Eclipse Class Decompiler plugin, copyright (c) 2016 Chen Chao (cnfree2000@hotmail.com) ***/

import java.io.PrintStream;
import java.util.Enumeration;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

public class DwSnmpOidSupport {
	DwSnmpMibTreeHash oidResolveHash;
	DwSnmpMibOutputHandler output = null;

	DwSnmpOidSupport() {
		this.oidResolveHash = new DwSnmpMibTreeHash();
		outputText("OID Support Library initialized");
	}

	public String getNodeOid(DefaultMutableTreeNode node) {
		String strPath = "";
		DwSnmpMibRecord nodeInfo = (DwSnmpMibRecord) node.getUserObject();
		if (nodeInfo.recordType == DwSnmpMibRecord.recVariable)
			return nodeInfo.name + "-" + nodeInfo.syntax;
		try {
			TreeNode[] nodePath = node.getPath();

			if (nodePath.length < 2)
				return ".";
			for (int i = 2; i < nodePath.length; ++i) {
				DwSnmpMibRecord recTemp = (DwSnmpMibRecord) ((DefaultMutableTreeNode) nodePath[i]).getUserObject();

				strPath = strPath.concat("." + String.valueOf(recTemp.number));
			}
		} catch (Exception e1) {
			return "Error in getting path..\n" + e1.toString();
		}

		if (nodeInfo.recordType == 3) {
			DwSnmpMibRecord recParTemp = (DwSnmpMibRecord) ((DefaultMutableTreeNode) node.getParent()).getUserObject();
			if (recParTemp.tableEntry == -1)
				strPath = strPath.concat(".(1 - n)");
			else {
				strPath = strPath.concat(".(1 - " + String.valueOf(recParTemp.tableEntry) + ")");
			}
		} else if (nodeInfo.recordType == 2) {
			if (nodeInfo.tableEntry == -1)
				strPath = strPath.concat(".(1-" + node.getChildCount() + ")" + ".(1-" + "n)");
			else {
				strPath = strPath.concat(
						".(1-" + node.getChildCount() + ")" + ".(1-" + String.valueOf(nodeInfo.tableEntry) + ")");
			}
		} else if (node.isLeaf() == true) {
			strPath = strPath.concat(".0");
		} else {
			strPath = strPath.concat(".*");
		}
		return strPath;
	}

	public String getNodeOidQuery(DefaultMutableTreeNode node) {
		String strPath = "";
		DwSnmpMibRecord nodeInfo = (DwSnmpMibRecord) node.getUserObject();
		try {
			TreeNode[] nodePath = node.getPath();

			if (nodePath.length < 2)
				return ".";
			for (int i = 2; i < nodePath.length; ++i) {
				DwSnmpMibRecord recTemp = (DwSnmpMibRecord) ((DefaultMutableTreeNode) nodePath[i]).getUserObject();

				strPath = strPath.concat("." + String.valueOf(recTemp.number));
			}
		} catch (Exception e1) {
			outputError("Error in getting path..\n" + e1.toString());
			return "";
		}

		if (nodeInfo.recordType == 3) {
			DwSnmpMibRecord recParTemp = (DwSnmpMibRecord) ((DefaultMutableTreeNode) node.getParent()).getUserObject();
			if (recParTemp.tableEntry == -1)
				strPath = strPath.concat(".65535");
			else {
				strPath = strPath.concat("." + String.valueOf(recParTemp.tableEntry));
			}
		} else if (nodeInfo.recordType == 2) {
			if (nodeInfo.tableEntry == -1)
				strPath = strPath.concat(".1.1");
			else {
				strPath = strPath.concat(".1." + String.valueOf(nodeInfo.tableEntry));
			}
		} else if (node.isLeaf() == true) {
			strPath = strPath.concat(".0");
		} else {
			strPath = strPath.concat(".0");
		}
		return strPath;
	}

	public String getNodeOidActual(DefaultMutableTreeNode node) {
		String strPath = "";
		DwSnmpMibRecord nodeInfo = (DwSnmpMibRecord) node.getUserObject();
		try {
			TreeNode[] nodePath = node.getPath();

			if (nodePath.length < 2)
				return ".";
			for (int i = 2; i < nodePath.length; ++i) {
				DwSnmpMibRecord recTemp = (DwSnmpMibRecord) ((DefaultMutableTreeNode) nodePath[i]).getUserObject();

				strPath = strPath.concat("." + String.valueOf(recTemp.number));
			}
		} catch (Exception e1) {
			outputError("Error in getting path..\n" + e1.toString());
			return "Error, cannot resolve OID name";
		}
		return strPath;
	}

	void buildOidToNameResolutionTable(DefaultMutableTreeNode treeRoot) {
		try {
			Enumeration enu = treeRoot.breadthFirstEnumeration();
			while (enu.hasMoreElements()) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) enu.nextElement();
				DwSnmpMibRecord nodeRec = (DwSnmpMibRecord) node.getUserObject();
				String sRec = getNodeOidActual(node);
				this.oidResolveHash.put(sRec, nodeRec.name);
			}
		} catch (Exception e) {
			outputError("Error in building OID table..\n" + e.toString());
		}
	}

	public String resolveOidName(String oid) {
		String objName = null;
		String oidCopy;
		if (oid.startsWith("."))
			oidCopy = oid.toString();
		else
			oidCopy = "." + oid.toString();
		try {
			oidCopy = oidCopy.substring(0, oidCopy.lastIndexOf(46));

			while ((objName == null) && (oidCopy.length() > 2)) {
				objName = (String) this.oidResolveHash.get(oidCopy);
				oidCopy = oidCopy.substring(0, oidCopy.lastIndexOf(46));
			}
			if (objName == null)
				return "***";
		} catch (Exception e) {
			System.out.println("Error in Resolving OID Name :\n " + e.toString());
		}
		return objName + oid.substring(oid.indexOf(".", oidCopy.length() + 1));
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
}