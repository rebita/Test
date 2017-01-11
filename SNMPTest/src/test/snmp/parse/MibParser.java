/**
 * 
 */
package test.snmp.parse;

import javax.swing.JTree;

/**
 * @author sung053
 *
 */
public class MibParser {
	private DwSnmpOidSupport oidDetail;
	private JTree myTree;

	public MibParser(){
		init();
	}
	
	/**
	 * 초기화
	 */
	private void init(){
		oidDetail = createTree();
		if(oidDetail==null){
			oidDetail = new DwSnmpOidSupport();
		}
	}
	
	/**
	 * 이름
	 * @return
	 */
	public String getName(String oid){
		return oidDetail.resolveOidName(oid);
	}
	
	/**
	 * 부모노드
	 * @return
	 */
	public String getParent(String oid){
		return oidDetail.resolveOidParent(oid);
	}
	
	/**
	 * Number
	 * @return
	 */
	public String getNumber(String oid){
		return oidDetail.resolveOidNumber(oid);
	}
	
	/**
	 * Access
	 * @return
	 */
	public String getAccess(String oid){
		return oidDetail.resolveOidAccess(oid);
	}
	
	/**
	 * Syntax
	 * @return
	 */
	public String getSyntex(String oid){
		return oidDetail.resolveOidSyntax(oid);
	}
	
	/**
	 * Status
	 * @return
	 */
	public String getStatus(String oid){
		return oidDetail.resolveOidStatus(oid);
	}
	
	/**
	 * 상세설명
	 * @return
	 */
	public String getDescription(String oid){
		return oidDetail.resolveOidDescription(oid);
	}
	

	public static void main(String args[]) {
		MibParser parser = new MibParser();
		System.out.println("desc : " +parser.getDescription(".1.3.6.1.2.1.1.1.0"));

	}

	private DwSnmpOidSupport createTree() {
		DwSnmpMibTreeBuilder treeSupport = new DwSnmpMibTreeBuilder();
		//String projectdir = System.getProperty("ProjectDir");
		String projectdir = System.getProperty("user.dir");
		if (projectdir == null) {
			projectdir = ".";
			System.out.println("::"+projectdir);
		} else {
			System.out.println("::"+projectdir);
		}
		if (treeSupport.addDirectory(projectdir + "/mibs/") == false) {
			System.out.println("Directory " + projectdir + "/mibs/ not found, or it is an empty directory!");
		}

//		treeSupport.addFile(projectdir + "/mibs/"+"mib_core.txt");
//		treeSupport.addFile(projectdir + "/mibs/"+"root_mib.txt");

		myTree = treeSupport.buildTree();
		if (myTree == null || treeSupport.oidSupport == null) {
			System.out.println("Error in loading MIB tree, quitting");
			System.out.println("--------- JTree is null");
			return treeSupport.oidSupport;
		}

		return treeSupport.oidSupport;
	}
}
