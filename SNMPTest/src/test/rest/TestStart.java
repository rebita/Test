package test.rest;

import java.util.HashMap;
import java.util.Random;

public class TestStart {
	public void startTester(){
		HashMap map = new HashMap();

		for (int i = 0; i <1000;i++){
			map.put("thread_"+i, new RestfulThread(0));
		}
		for (int i = 0; i <1000;i++){
			Runnable doit = (RestfulThread) map.get("thread_"+i);
			doit.run();
		}
	}
	public static void main(String args[]){
		TestStart tester = new TestStart();
		tester.startTester();
		
	}
}
