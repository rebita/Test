/**
 * 
 */
package test.snmp.mib;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StreamTokenizer;

/**
 * @author sung053
 *
 */
public class TestMibPaser {
	
	static DwSnmpMibOutputHandler output = null;
	
	public static void main(String args[]){
		
//		BufferedReader bf;
//		try {
//			bf = new BufferedReader(new FileReader("C:\\Users\\sung053\\Desktop\\jmibbrowser\\mibs\\test2.txt"));
//			StreamTokenizer st = new StreamTokenizer(bf);
//			st.resetSyntax();
//			st.eolIsSignificant(true); //라인끝에 도달하면 TT_EOL 리턴
//			st.wordChars(33, 126);
//		    
//			
//			while(st.nextToken()!=StreamTokenizer.TT_EOF){
//				
//				switch(st.ttype){
//					case -3:
//						System.out.println(st.sval);
//						break;
//					case -2:
//						System.out.println(String.valueOf((int) st.nval));
//						break;
//					case 10:
//						System.out.println("\n");
//						break;
//				}
//					
//			}
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} 
		
		
		
		
		TestMibPaser parser = new TestMibPaser();
		output = new DwSnmpMibOutputHandler();
		String fName="C:\\Users\\sung053\\Desktop\\jmibbrowser\\mibs\\test3.my";
		if(parser.loadNewFile(fName)){
			parser.loadFile(fName);
		}else{
			System.out.println("no file");
		}
		
	}
	
	private void loadFile(String fileName) {
		this.output.print("Adding file " + fileName);
		if (this.parseFile(fileName) < 0)
			System.out.println(".. Error");
		else
			this.output.print("..Done\n");
	}
	
	private int parseFile(String fName) {
		System.out.println("parseFile");
		DwSnmpMibTreeBuilder builder = new DwSnmpMibTreeBuilder();
		SnmpMibParser fParser = new SnmpMibParser(fName,builder);
		if (this.output != null)
			fParser.setOutput(this.output);
		return fParser.parseMibFile();
	}
	
	public boolean loadNewFile(String fName) {
		if (fName == null)
			return false;
		File mibFile = new File(fName);
		if (mibFile.exists() != true)
			return false;
		else return true;
	}
}
