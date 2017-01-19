package test.snmp.parse;

import java.util.Enumeration;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

public class DwSnmpOidSupport {

	DwSnmpMibTreeHash oidResolveHashName;
	DwSnmpMibTreeHash oidResolveHashParent;
	DwSnmpMibTreeHash oidResolveHashNumber;
	DwSnmpMibTreeHash oidResolveHashAccess;
	DwSnmpMibTreeHash oidResolveHashSyntax;
	DwSnmpMibTreeHash oidResolveHashStatus;
	DwSnmpMibTreeHash oidResolveHashDescription;

	DwSnmpOidSupport() {

		oidResolveHashName = new DwSnmpMibTreeHash();
		oidResolveHashParent = new DwSnmpMibTreeHash();
		oidResolveHashNumber = new DwSnmpMibTreeHash();
		oidResolveHashAccess = new DwSnmpMibTreeHash();
		oidResolveHashSyntax = new DwSnmpMibTreeHash();
		oidResolveHashStatus = new DwSnmpMibTreeHash();
		oidResolveHashDescription = new DwSnmpMibTreeHash();

		System.out.println("OID Support Library initialized");
	}

	public String getNodeOid(DefaultMutableTreeNode node) {
		String strPath = "";
		DwSnmpMibRecord nodeInfo = (DwSnmpMibRecord) node.getUserObject();
		if (nodeInfo.recordType == nodeInfo.recVariable)
			return (nodeInfo.name + "-" + nodeInfo.syntax);
		try {
			TreeNode[] nodePath = node.getPath();
			DwSnmpMibRecord recTemp;
			if (nodePath.length < 2)
				return (".");
			for (int i = 2; i < nodePath.length; i++) {
				recTemp = (DwSnmpMibRecord) (((DefaultMutableTreeNode) nodePath[i]).getUserObject());
				// System.out.println(recTemp.name + " " + recTemp.number);
				strPath = strPath.concat("." + String.valueOf(recTemp.number));
			}
		} catch (Exception e1) {
			return ("Error in getting path..\n" + e1.toString());
		}
		// Node OID Obtained, now check if it is in a table
		// For Table Element
		// System.out.println("Getting OID Name...");
		if (nodeInfo.recordType == 3) {
			DwSnmpMibRecord recParTemp = (DwSnmpMibRecord) ((DefaultMutableTreeNode) node.getParent()).getUserObject();
			if (recParTemp.tableEntry == -1)
				strPath = strPath.concat(".(" + 1 + " - " + "n" + ")");
			else
				strPath = strPath.concat(".(" + 1 + " - " + String.valueOf(recParTemp.tableEntry) + ")");
		} else if (nodeInfo.recordType == 2) {
			if (nodeInfo.tableEntry == -1)
				strPath = strPath.concat(".(1-" + node.getChildCount() + ")" + ".(1-" + "n)");
			else
				strPath = strPath.concat(
						".(1-" + node.getChildCount() + ")" + ".(1-" + String.valueOf(nodeInfo.tableEntry) + ")");
		} else if (node.isLeaf() == true)
			strPath = strPath.concat(".0");
		else
			strPath = strPath.concat(".*");
		// System.out.println(strPath);
		return strPath;

	}

	/**
	 * END OF getNodeOid
	 */

	/**
	 * getNodeOidQuery : RETURNS OID VALUES SUCH THAT THEY CAN BE STRAIGHTAWAY
	 * USED FOR QUERIES
	 */

	public String getNodeOidQuery(DefaultMutableTreeNode node) {

		String strPath = "";
		DwSnmpMibRecord nodeInfo = (DwSnmpMibRecord) node.getUserObject();
		try {
			TreeNode[] nodePath = node.getPath();
			DwSnmpMibRecord recTemp;
			if (nodePath.length < 2)
				return (".");
			for (int i = 2; i < nodePath.length; i++) {
				recTemp = (DwSnmpMibRecord) (((DefaultMutableTreeNode) nodePath[i]).getUserObject());
				// System.out.println(recTemp.name + " " + recTemp.number);
				strPath = strPath.concat("." + String.valueOf(recTemp.number));
			}
		} catch (Exception e1) {
			System.out.println("Error in getting path..\n" + e1.toString());
			return "";
		}
		// Node OID Obtained, now check if it is in a table
		// For Table Element
		if (nodeInfo.recordType == 3) {
			DwSnmpMibRecord recParTemp = (DwSnmpMibRecord) ((DefaultMutableTreeNode) node.getParent()).getUserObject();
			if (recParTemp.tableEntry == -1)
				strPath = strPath.concat(".65535");
			else
				strPath = strPath.concat("." + String.valueOf(recParTemp.tableEntry));
		} else if (nodeInfo.recordType == 2) {
			if (nodeInfo.tableEntry == -1)
				strPath = strPath.concat(".1.1");
			else
				strPath = strPath.concat(".1." + String.valueOf(nodeInfo.tableEntry));
		} else if (node.isLeaf() == true)
			strPath = strPath.concat(".0");
		else
			strPath = strPath.concat(".0");
		return strPath;
	}

	/**
	 * END OF getNodeOidQuery
	 */

	/**
	 * getNodeOidActual RETURNS THE ACTUAL OID, WITHOUT APPENDING ANYTHING.
	 * MAINLY USED FOR OID TO NAME RESOLVING.
	 *
	 */
	public String getNodeOidActual(DefaultMutableTreeNode node) {

		String strPath = "";
		DwSnmpMibRecord nodeInfo = (DwSnmpMibRecord) node.getUserObject();
		try {
			TreeNode[] nodePath = node.getPath();
			DwSnmpMibRecord recTemp;
			if (nodePath.length < 2)
				return (".");
			for (int i = 2; i < nodePath.length; i++) {
				recTemp = (DwSnmpMibRecord) (((DefaultMutableTreeNode) nodePath[i]).getUserObject());
				// System.out.println(recTemp.name + " " + recTemp.number);
				strPath = strPath.concat("." + String.valueOf(recTemp.number));
			}
		} catch (Exception e1) {
			System.out.println("Error in getting path..\n" + e1.toString());
			return ("Error, cannot resolve OID name");
		}
		return strPath;
	}

	void buildOidToNameResolutionTable(DefaultMutableTreeNode treeRoot) {
		try {
			DefaultMutableTreeNode node;
			DwSnmpMibRecord nodeRec;
			Enumeration enu = treeRoot.breadthFirstEnumeration();
			while (enu.hasMoreElements()) {
				node = (DefaultMutableTreeNode) enu.nextElement();
				nodeRec = (DwSnmpMibRecord) node.getUserObject();
				String sRec = getNodeOidActual(node);

				oidResolveHashName.put(sRec, nodeRec.name);
				oidResolveHashParent.put(sRec, nodeRec.parent);
				oidResolveHashNumber.put(sRec, nodeRec.number);
				oidResolveHashAccess.put(sRec, nodeRec.access);
				oidResolveHashSyntax.put(sRec, nodeRec.syntax);
				oidResolveHashStatus.put(sRec, nodeRec.status);
				oidResolveHashDescription.put(sRec, nodeRec.description);
			}
		} catch (Exception e) {
			System.out.println("Error in building OID table..\n" + e.toString());
		}
	}

	/**
	 * Oid 이름 리턴
	 * 
	 * @param oid
	 * @return
	 */
	public String resolveOidName(String oid) {
		String objName = null;
		String oidCopy;

		if (oid.startsWith("."))
			oidCopy = oid.toString();
		else
			oidCopy = "." + oid.toString();

		try {
			oidCopy = oidCopy.substring(0, oidCopy.lastIndexOf('.'));

			while (objName == null && oidCopy.length() > 2) {
				objName = (String) oidResolveHashName.get(oidCopy);
				oidCopy = oidCopy.substring(0, oidCopy.lastIndexOf('.'));
			}
			if (objName == null)
				return ("***");
		} catch (Exception e) {
			System.out.println("Error in Resolving OID Name :\n " + e.toString());
		}

		return (objName + oid.substring(oid.indexOf(".", oidCopy.length() + 1)));
	}

	/**
	 * oid 부모노드 이름 리턴
	 * 
	 * @param oid
	 * @return
	 */
	public String resolveOidParent(String oid) {
		String objParent = null;
		String oidCopy;

		if (oid.startsWith("."))
			oidCopy = oid.toString();
		else
			oidCopy = "." + oid.toString();

		try {
			oidCopy = oidCopy.substring(0, oidCopy.lastIndexOf('.'));

			while (objParent == null && oidCopy.length() > 2) {
				objParent = (String) oidResolveHashParent.get(oidCopy);
				oidCopy = oidCopy.substring(0, oidCopy.lastIndexOf('.'));
			}
			if (objParent == null)
				return ("***");
		} catch (Exception e) {
			System.out.println("Error in Resolving OID Parent :\n " + e.toString());
		}

		return (objParent);
	}

	/**
	 * oid Number 리턴
	 * 
	 * @param oid
	 * @return
	 */
	public String resolveOidNumber(String oid) {
		String objNumber = "";
		String oidCopy;

		if (oid.startsWith("."))
			oidCopy = oid.toString();
		else
			oidCopy = "." + oid.toString();

		try {
			oidCopy = oidCopy.substring(0, oidCopy.lastIndexOf('.'));

			while (objNumber == "" && oidCopy.length() > 2) {
				objNumber = String.valueOf(""+oidResolveHashNumber.get(oidCopy));
				oidCopy = oidCopy.substring(0, oidCopy.lastIndexOf('.'));
			}
			if (objNumber == null || objNumber.equals("null")) 
				return ("0");
				
		} catch (Exception e) {
			System.out.println("Error in Resolving OID Number :\n " + e.toString());
			e.printStackTrace();
		}

		
		return (objNumber);
	}

	/**
	 * oid Access 리턴
	 * 
	 * @param oid
	 * @return
	 */
	public String resolveOidAccess(String oid) {
		String objAccess = null;
		String oidCopy;

		if (oid.startsWith("."))
			oidCopy = oid.toString();
		else
			oidCopy = "." + oid.toString();

		try {
			oidCopy = oidCopy.substring(0, oidCopy.lastIndexOf('.'));

			while (objAccess == null && oidCopy.length() > 2) {
				objAccess = (String) oidResolveHashAccess.get(oidCopy);
				oidCopy = oidCopy.substring(0, oidCopy.lastIndexOf('.'));
			}
			if (objAccess == null)
				return ("***");
		} catch (Exception e) {
			System.out.println("Error in Resolving OID Access :\n " + e.toString());
		}

		return (objAccess);
	}

	/**
	 * Syntax 리턴
	 * 
	 * @param oid
	 * @return
	 */
	public String resolveOidSyntax(String oid) {
		String objSyntax = null;
		String oidCopy;

		if (oid.startsWith("."))
			oidCopy = oid.toString();
		else
			oidCopy = "." + oid.toString();

		try {
			oidCopy = oidCopy.substring(0, oidCopy.lastIndexOf('.'));

			while (objSyntax == null && oidCopy.length() > 2) {
				objSyntax = (String) oidResolveHashSyntax.get(oidCopy);
				oidCopy = oidCopy.substring(0, oidCopy.lastIndexOf('.'));
			}
			if (objSyntax == null)
				return ("***");
		} catch (Exception e) {
			System.out.println("Error in Resolving OID Syntax :\n " + e.toString());
		}

		return (objSyntax);
	}

	/**
	 * Status 리턴
	 * 
	 * @param oid
	 * @return
	 */
	public String resolveOidStatus(String oid) {
		String objStatus = null;
		String oidCopy;

		if (oid.startsWith("."))
			oidCopy = oid.toString();
		else
			oidCopy = "." + oid.toString();

		try {
			oidCopy = oidCopy.substring(0, oidCopy.lastIndexOf('.'));

			while (objStatus == null && oidCopy.length() > 2) {
				objStatus = (String) oidResolveHashStatus.get(oidCopy);
				oidCopy = oidCopy.substring(0, oidCopy.lastIndexOf('.'));
			}
			if (objStatus == null)
				return ("***");
		} catch (Exception e) {
			System.out.println("Error in Resolving OID Status :\n " + e.toString());
		}

		return (objStatus);
	}

	/**
	 * Oid 설명 리턴
	 * 
	 * @param oid
	 * @return
	 */
	public String resolveOidDescription(String oid) {
		String objDesc = null;
		String oidCopy;

		if (oid.startsWith("."))
			oidCopy = oid.toString();
		else
			oidCopy = "." + oid.toString();

		try {
			oidCopy = oidCopy.substring(0, oidCopy.lastIndexOf('.'));

			while (objDesc == null && oidCopy.length() > 2) {
				objDesc = (String) oidResolveHashDescription.get(oidCopy);
				oidCopy = oidCopy.substring(0, oidCopy.lastIndexOf('.'));
			}
			if (objDesc == null)
				return ("***");
		} catch (Exception e) {
			System.out.println("Error in Resolving OID Description :\n " + e.toString());
		}

		return (objDesc);
	}

}
