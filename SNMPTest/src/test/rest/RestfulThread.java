/**
 * 
 */
package test.rest;

import java.util.Random;

/**
 * @author sung053
 *
 */
public class RestfulThread implements Runnable{
	private int ch = 0;
	public void setChannel(int channel){
		this.ch = channel;
	}
	public RestfulThread(int channel){
		this.ch = channel;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		Random r = new Random();
		int rand = r.nextInt(5);
		RestfulTester test = new RestfulTester();
		try {
			Thread.sleep(rand);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String result = test.doRestRequest("localhost", 7080, "/TongyangGW/ttsmake?ment=test&option=1&ch="+ch);
		System.out.println(result);
	}

}
