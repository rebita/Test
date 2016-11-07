package test.snmp.mib;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;

public class DwSnmpMibOutputHandler {
	JTextArea outputText;
	JTextArea outputError;
	boolean doLog = false;
	boolean autoScroll = true;
	static BufferedWriter outfile;

	public void setAutoScroll(boolean as) {
		this.autoScroll = as;
	}

	public void setOutput(JTextArea o) {
		this.outputText = o;
	}

	public void setOutputError(JTextArea e) {
		this.outputError = e;
	}

	void showErrorMessage(String s) {
		JOptionPane.showMessageDialog(this.outputText, s, "Mib Browser", 0);
	}

	public void setLogging(boolean log) {
		try {
			if (log == true) {
				String strFileName = getLogFileName();
				outfile = new BufferedWriter(new FileWriter(strFileName, true));
				outfile.write("\n**********************************************************\n");
				outfile.write("MIB Browser Started at : " + new Date());
				outfile.write("\n**********************************************************\n");
				System.out.println("Output log file: " + strFileName);
				this.doLog = true;

				Timer tmr = new Timer(true);

				long lFlushTime = getFlushTime();
				System.out.println("Log will be refreshed every " + (lFlushTime / 1000L) + " seconds.");
				tmr.schedule(new TimerTask() {
					public void run() {
						try {
							DwSnmpMibOutputHandler.outfile.flush();
						} catch (Exception e) {
							System.out.println("Error in writing to log file: " + e);
						}
					}
				}, lFlushTime, lFlushTime);

				Thread thrdFlush = new Thread(new Runnable() {
					public void run() {
						try {
							System.out.println("Have a nice day !!");
							DwSnmpMibOutputHandler.outfile
									.write("\n**********************************************************\n");
							DwSnmpMibOutputHandler.outfile.write("MIB Browser Stopped at : " + new Date());
							DwSnmpMibOutputHandler.outfile
									.write("\n**********************************************************\n");
							DwSnmpMibOutputHandler.outfile.flush();
							DwSnmpMibOutputHandler.outfile.close();
						} catch (Exception e) {
							System.out.println("Error while writing to log file: " + e);
						}
					}
				});
				Runtime.getRuntime().addShutdownHook(thrdFlush);
			} else {
				outfile.close();
			}
		} catch (Exception e) {
			System.out.println("Error : Cannot log" + e.toString());

			return;
		}
		this.doLog = true;
	}

	private String getLogFileName() {
		String strFileName = System.getProperty("logfilename");
		if (strFileName == null) {
			strFileName = "mibbrowser.log";
		}
		return strFileName;
	}

	private long getFlushTime() {
		long lTime = 0L;
		String strTime = System.getProperty("logrefreshtime");
		if (strTime != null) {
			try {
				lTime = Long.parseLong(strTime);
				lTime *= 1000L;
			} catch (Exception e) {
				System.out.println("Invalid value for log refresh time. default will be used.");
			}
		}

		if (lTime < 1000L) {
			lTime = 60000L;
		}
		return lTime;
	}

	public void println(String s) {
		if (this.outputText != null) {
			this.outputText.append("\n" + s);
			if (this.autoScroll == true)
				this.outputText.setCaretPosition(this.outputText.getDocument().getLength() - 1);
		}

		try {
			outfile.write(s + "\n");
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	public void print(String s) {
		if (this.outputText != null) {
			this.outputText.append(s);
			if (this.autoScroll == true)
				this.outputText.setCaretPosition(this.outputText.getDocument().getLength() - 1);
		} else {
			System.out.println(s);
		}
		try {
			outfile.write(s);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	public void printError(String e) {
		if (this.outputError != null)
			this.outputError.append("\n" + e);
		else
			System.err.println(e);
		try {
			outfile.write("\n" + e + "\n");
		} catch (Exception ex) {
			System.out.println(e.toString());
		}
	}
}