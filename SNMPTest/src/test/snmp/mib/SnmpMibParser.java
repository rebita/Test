package test.snmp.mib;

/*** Eclipse Class Decompiler plugin, copyright (c) 2016 Chen Chao (cnfree2000@hotmail.com) ***/

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.io.StreamTokenizer;

public class SnmpMibParser {
	String fileName;
	DwSnmpMibRecord currentRec;
	String prevToken;
	String tokenVal = "xx";
	String tokenVal2 = "";
	PipedReader pr;
	PipedWriter pw;
	BufferedReader r;
	FileReader inFile;
	public static int TT_EOL = 10;
	DwSnmpMibOutputHandler output;
	MibParserInterface tokens;

	SnmpMibParser(String fileName, MibParserInterface intf) {
		this.fileName = fileName;
		this.tokens = intf;
	}

	int parseMibFile() {
		BufferedReader in = null;

		String tok1 = " ";
		String tok2 = " ";
		StreamTokenizer st1;
		try {
			this.inFile = new FileReader(new File(this.fileName));
			this.r = new BufferedReader(this.inFile);
			st1 = new StreamTokenizer(this.r);
		} catch (Exception e) {
			outputError("File open error : Cannot open file.\n" + e.toString());
			return -1;
		}
		String line = " ";

		st1.resetSyntax();
		st1.eolIsSignificant(true); // 라인끝에 도달하면 TT_EOL 리턴
		st1.wordChars(33, 126);
		// st1.wordChars(33, 126);
		String t1 = "a";
		int parseStatus = 0;
		int parseStatusTemp = 0;
		try {
			int flag = 0;
			while ((getNextToken(st1).trim().length() > 0) || (st1.ttype == TT_EOL)) {
				// while(st1.nextToken()!=StreamTokenizer.TT_EOF){
				t1 = getTokenVal(st1);

				switch (parseStatus) {
				case 0:
					System.out.println("tttttttttttss" + parseStatus + "  " + t1 + "ss");
					this.currentRec = new DwSnmpMibRecord();
					if (t1.indexOf("IMPORT") != -1) {
						parseStatus = 100;
						System.out.println("IMPORT");
					}

					if (t1.equals("MODULE-IDENTITY") == true) {
						this.currentRec.name = this.prevToken;
						parseStatus = 1;
						System.out.println("MODULE-IDENTITY");
					}

					if (t1.equals("OBJECT-TYPE") == true) {
						String temp = new String(this.prevToken.trim());
						temp = temp.substring(0, 1);
						if (temp.toLowerCase().equals(temp)) {
							parseStatus = 1;
							this.currentRec.name = this.prevToken;
							System.out.println("OBJECT-TYPE");
						}
					}

					if (t1.indexOf("OBJECT-GROUP") != -1) {
						String temp = new String(this.prevToken.trim());
						temp = temp.substring(0, 1);
						System.out.println("OBJECT-GROUP");
						if (temp.toLowerCase().equals(temp)) {
							parseStatus = 1;
							this.currentRec.name = this.prevToken;
						}
					}
					if (t1.equals("OBJECT") == true) {
						this.currentRec.name = this.prevToken;
						parseStatus = 2;
					}
					if (t1.equals("::=") != true)
						continue;
					this.currentRec.init();
					this.currentRec.name = this.prevToken;
					parseStatus = 9;
					break;
				case 1:
					System.out.println("tttttttttttss" + parseStatus + "  " + t1 + "ss");
					if (t1.equals("::=") == true)
						parseStatus = 3;
					if (t1.equals("SYNTAX") == true)
						parseStatus = 5;
					if (t1.indexOf("ACCESS") != -1)
						parseStatus = 6;
					if (t1.equals("STATUS") == true)
						parseStatus = 7;
					if (t1.equals("DESCRIPTION") == true) {
						parseStatus = 8;
					}
					if (t1.equals("INDEX") == true)
						parseStatus = 11;
					if (t1.equals("OBJECTS") == true)
						parseStatus = 14;
					break;
				case 2:
					System.out.println("tttttttttttss" + parseStatus + "  " + t1 + "ss");
					if (t1.equals("IDENTIFIER") == true) {
						parseStatus = 1;
					} else {
						parseStatus = 0;
					}
					break;
				case 3:
					System.out.println("tttttttttttss" + parseStatus + "  " + t1 + "ss");
					if (t1.trim().startsWith("{") == true)
						continue;
					if (t1.trim().length() != 0) {
						System.out.println("********Hello...." + t1);
						this.currentRec.parent = t1;
						parseStatus = 4; /** 괄호안에.. */
					}
					break;
				case 4:
					System.out.println("tttttttttttss" + parseStatus + "  " + t1 + "ss");
					// this.currentRec.number = t1; /** 추가 **/
					// parseStatus = 1;
					// parseStatusTemp = 0;
					// ///////////////
					// this.currentRec.number = Integer.parseInt(t1.trim());
					this.currentRec.number = Integer.parseInt(t1.trim());
					// parseStatus = 1;
					parseStatusTemp = 0;
					parseStatus = 5;
					break;
				case 5:
					System.out.println("tttttttttttss" + parseStatus + "  " + t1 + "ss");
					try {
						if (t1.trim().endsWith(")") == true) {
							String numStr = "";
							DwSnmpMibRecord newRec = new DwSnmpMibRecord();
							numStr = t1.substring(t1.indexOf(40) + 1, t1.indexOf(41));
							System.out.println("-----------SYNTAX : " + numStr);
							try {
								newRec.number = Integer.parseInt(numStr.trim());
							} catch (Exception ne2) {
								outputError("Error in line " + st1.lineno());

							}
							newRec.name = t1.substring(0, t1.indexOf("("));

							newRec.parent = this.currentRec.parent;
							// this.currentRec.parent = newRec.name;

							addToken(newRec);
							//break;
						}
						///////
						
						if (parseStatusTemp != 1)
							continue;
//						if (this.currentRec.syntax.indexOf(123) != -1) {
//							parseStatus = 12;
//						}
//
//						if (this.currentRec.syntax.trim().startsWith("SEQUENCE") == true) {
//							System.out.println("-----------SEQUENCE : ");
//							this.currentRec.recordType = 1;
//							this.currentRec.tableEntry = 1;
//						}

						///////////////
						this.currentRec.number = Integer.parseInt(t1.trim());

						addToken(this.currentRec);
						 parseStatus = 0;
						//parseStatus = 1;
						parseStatusTemp = 0;
					} catch (NumberFormatException ne) {
						outputError("Error in getting number.." + t1 + "\n" + ne.toString());
						System.out.println("***************************exception**************");
						if (t1.indexOf(123) != -1) {
							this.currentRec.syntax = this.currentRec.syntax.concat(" " + t1);
							parseStatus = 12;	
						}
						
						this.currentRec.syntax =  this.currentRec.syntax.concat(t1);
						if ((st1.ttype != TT_EOL) && (st1.ttype != -1)){
							//TT_EOL이 아니고 TT_EOF가 아닐경우
							break;
						}

					}
					if (parseStatusTemp != 1)
						continue;
					if (this.currentRec.syntax.indexOf(123) != -1) {
						parseStatus = 12;
					}

					if (this.currentRec.syntax.trim().startsWith("SEQUENCE") == true) {
						this.currentRec.recordType = 1;
						this.currentRec.tableEntry = 1;
					}
					System.out.println("*****************************************");
					parseStatus = 1;
					parseStatusTemp = 0;
					//continue;
					System.out.println("************************************ss*****"+t1);
					this.currentRec.syntax = this.currentRec.syntax.concat(" " + t1);
					if (this.currentRec.syntax.trim().length() <= 0)
						continue;
					parseStatusTemp = 1;
					break;
				case 6:
					System.out.println("tttttttttttss" + parseStatus + "  " + t1 + "ss");
					if (st1.ttype == TT_EOL) {
						parseStatus = 1;
					}

					this.currentRec.access = this.currentRec.access.concat(" " + t1);
					break;
				case 7:
					System.out.println("tttttttttttss" + parseStatus + "  " + t1 + "ss");
					if (st1.ttype == TT_EOL) {
						parseStatus = 1;
					}

					this.currentRec.status = this.currentRec.status.concat(" " + t1);
					break;
				case 8:
					System.out.println("tttttttttttss" + parseStatus + "  " + t1 + "ss");
					if (st1.ttype != -1) {
						this.currentRec.description = this.currentRec.description.concat(" " + t1);
						if (t1.trim().length() == 0)
							continue;
						parseStatus = 1;
					}
					break;
				case 9:
					System.out.println("tttttttttttss" + parseStatus + "  " + t1 + "ss");
					this.currentRec.recordType = DwSnmpMibRecord.recVariable;
					if (t1.indexOf(123) != -1) {
						parseStatus = 10;
						this.currentRec.syntax = this.currentRec.syntax.concat(" " + t1);
					}

					if ((st1.ttype == TT_EOL) || (st1.ttype == -1)) {
						this.currentRec.syntax = this.currentRec.syntax.concat(t1);
						if (parseStatusTemp != 1)
							continue;
						if (this.currentRec.syntax.indexOf(123) != -1) {
							parseStatus = 10;
						}

						if (this.currentRec.syntax.trim().startsWith("SEQUENCE") == true) {
							this.currentRec.recordType = 1;
						}

						addToken(this.currentRec);
						parseStatus = 0;
						parseStatusTemp = 0;
					}

					this.currentRec.syntax = this.currentRec.syntax.concat(" " + t1);
					if (this.currentRec.syntax.trim().length() <= 0)
						continue;
					parseStatusTemp = 1;
					break;
				case 10:
					System.out.println("tttttttttttss" + parseStatus + "  " + t1 + "ss");
					this.currentRec.syntax = this.currentRec.syntax.concat(t1);
					if (t1.indexOf(125) != -1)
						;
					parseStatus = 0;
					parseStatusTemp = 0;

					if (this.currentRec.syntax.trim().startsWith("SEQUENCE") == true) {
						this.currentRec.recordType = 1;
					}
					addToken(this.currentRec);

					break;
				case 11:
					System.out.println("tttttttttttss" + parseStatus + "  " + t1 + "ss");
					if (t1.trim().startsWith("{") != true)
						;
					if (t1.indexOf(125) != -1) {
						parseStatus = 1;
					}

					this.currentRec.index = this.currentRec.index.concat(t1);
					break;
				case 12:
					System.out.println("tttttttttttss" + parseStatus + "  " + t1 + "ss");
					this.currentRec.syntax = this.currentRec.syntax.concat(t1);
					if (t1.indexOf(125) != -1) {
						parseStatus = 1;
					} else {
						parseStatusTemp = 0;
					}
					if (this.currentRec.syntax.trim().startsWith("SEQUENCE") == true) {
						this.currentRec.recordType = 1;
						this.currentRec.tableEntry = 1;
					}
					break;
				case 14:
					System.out.println("tttttttttttss" + parseStatus + "  " + t1 + "ss");
					this.currentRec.syntax = this.currentRec.syntax.concat(t1);
					if (t1.indexOf(125) != -1)
						;
					parseStatus = 1;
					break;
				case 100:
					System.out.println("tttttttttttss" + parseStatus + "  " + t1 + "ss");
					if (t1.indexOf(59) != -1)
						parseStatus = 0;
					break;
				case 101:
					System.out.println("tttttttttttss" + parseStatus + "  " + t1 + "ss");
					if (t1.indexOf(125) != -1)
						parseStatus = 0;
					break;
				}
			}
			System.out.println("****Result : \n" + currentRec.getCompleteString());

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error in parsing.. \n" + e.toString());
		}
		return 0;
	}

	String getNextToken(StreamTokenizer st) {
		String tok = "";
		this.prevToken = getTokenVal(st);

		while (tok.equals("") == true) {
			try {
				if (this.tokenVal.equals("xx") != true)
					return this.tokenVal;
				if (this.tokenVal2.equals("") != true) {
					setTokenVal(this.tokenVal2);
					this.tokenVal2 = "";
					return this.tokenVal;
				}
				if (st.nextToken() != -1) {
					if (st.ttype == TT_EOL)
						return getTokenVal(st);
					if (st.ttype == -3) {
						tok = st.sval;

						if ((tok.startsWith("{") == true) && (tok.trim().length() != 1)) {
							setTokenVal("{");
							this.tokenVal2 = new String(tok.substring(1));
							return "{";
						}

						if ((tok.endsWith("}") == true) && (tok.trim().length() != 1)) {
							setTokenVal(tok.replace('}', ' '));
							this.tokenVal2 = "}";
							return tok.replace('}', ' ');
						}

						if (tok.startsWith("\"") == true) {
							String strQuote = new String(tok);
							st.nextToken();
							tok = getTokenVal(st);
							while ((tok != null) && (tok.indexOf(34) == -1)) {
								String temp = getTokenVal(st);
								if (temp.trim().length() > 0)
									strQuote = strQuote.concat(" " + temp);
								if (st.nextToken() == -1)
									return tok;
								tok = getTokenVal(st);
							}
							strQuote = strQuote.concat(getTokenVal(st));

							if (strQuote.trim().length() > 0)
								this.tokenVal = strQuote;
						}

						if (tok.equals("--") == true) {
							while (st.ttype != 10) {
								st.nextToken();
							}
							break;
						}

						if (st.ttype == TT_EOL)
							return " ";

					}

					if (st.ttype == -2) {
						tok = String.valueOf(st.nval);
						if (tok.trim().length() > 0) {
							return tok;
						}

					}
					/**  **/
					if (st.ttype == -3) {
						tok = st.sval;
						if (tok.trim().length() > 0) {
							return tok;
						}

					}
					/** **/

					tok = "";
				} else {
					return "";
				}
			} catch (Exception e) {
				if (e.getMessage().startsWith("Write end dead") != true) {
					System.out.println("Error in reading file..." + e.toString());
				}
				return "";
			}
		}
		return tok;
	}

	void setTokenVal(String t) {
		this.tokenVal = t;
	}

	String getTokenVal(StreamTokenizer st) {
		try {

			if (this.tokenVal != "xx") {
				String temp = this.tokenVal.toString();
				this.tokenVal = "xx";
				return temp;
			}
			if (st.ttype == TT_EOL) {
				return String.valueOf('\n');
			}
			if (st.ttype == -3) {
				System.out.println("문자" + st.sval);
				return st.sval;
			}
			if (st.ttype == -2) {
				System.out.println("숫자" + String.valueOf((int) st.nval));
				return String.valueOf((int) st.nval);
			}
			return "";
		} catch (Exception e) {
			System.out.println("Error in retrieving token value..\n" + e.toString());
		}

		return "";
	}

	void setOutput(DwSnmpMibOutputHandler output) {
		this.output = output;
	}

	void outputText(String s) {
		this.output.println(s);
	}

	void outputError(String s) {
		this.output.printError(s);
	}

	void addToken(DwSnmpMibRecord rec) {
		this.tokens.newMibParseToken(rec);
	}
}