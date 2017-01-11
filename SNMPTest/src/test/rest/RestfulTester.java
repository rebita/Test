/**
 * 
 */
package test.rest;

import java.net.Proxy.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.mail.Header;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * @author sung053
 *
 */
public class RestfulTester {
	public static void main(String[] args) {
		RestfulTester restTest = new RestfulTester();
		restTest.doRestRequest("localhost", 7080, "/TongyangGW/wavmake?ment=test&option=1&ch=1");
	}
	
	public void doSample(){
		DefaultHttpClient httpclient = new DefaultHttpClient();
		String url = "api.github.com";
		// String getBody = "/forecastrss?p=80020&u=f";
		String getBody = "/";
		int port = 443; // https
		// int port = 80; //http

		try {
			// specify the host, protocol, and port
			HttpHost target = new HttpHost(url, port, "https");
			// HttpHost target = new HttpHost(url);
			// specify the get request
			HttpGet getRequest = new HttpGet(getBody);
			/**
			 * Post요청
			 */
			// ArrayList<NameValuePair> postParams = new
			// ArrayList<NameValuePair>();
			// Map<String, String> paramMap = new HashMap<String, String>();
			// Builder builder = RequestConfig.custom();
			// builder.setConnectTimeout(4000);
			// builder.setSocketTimeout(4000);
			// builder.setStaleConnectionCheckEnabled(false);
			// RequestConfig config = builder.build();
			//
			// for (Map.Entry<String, String> entry : paramMap.entrySet()) {
			// postParams.add(new BasicNameValuePair(entry.getKey(),
			// entry.getValue()));
			// }
			//
			// HttpPost getRequest = new HttpPost(url);
			// getRequest.setEntity(new UrlEncodedFormEntity(postParams));
			// getRequest.setConfig(config);
			/** **/

			System.out.println("executing request to " + target);

			HttpResponse httpResponse = httpclient.execute(target, getRequest);
			HttpEntity entity = httpResponse.getEntity();

			System.out.println("----------------------------------------");
			System.out.println(httpResponse.getStatusLine());
			org.apache.http.Header[] headers = httpResponse.getAllHeaders();
			for (int i = 0; i < headers.length; i++) {
				System.out.println(headers[i]);
			}
			System.out.println("----------------------------------------");

			if (entity != null) {
				Gson gson = new Gson();
				String json = EntityUtils.toString(entity);
				System.out.println(json);
				HashMap value = gson.fromJson(json, HashMap.class);
				System.out.println(value);
				System.out.println("GET from Hashmap!! : "+value.get("current_user_url"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// When HttpClient instance is no longer needed,
			// shut down the connection manager to ensure
			// immediate deallocation of all system resources
			httpclient.getConnectionManager().shutdown();
		}
	}
	
	public String doRestRequest(String urlString,int portNum, String body){
		DefaultHttpClient httpclient = new DefaultHttpClient();
		//String url = "api.github.com";
		String url = urlString;
		// String getBody = "/forecastrss?p=80020&u=f";
		//String getBody = "/";
		String getBody = body;
		//int port = 443; // https
		int port = portNum;
		// int port = 80; //http

		try {
			// specify the host, protocol, and port
			HttpHost target = new HttpHost(url, port, "http");
			// HttpHost target = new HttpHost(url);
			// specify the get request
			HttpGet getRequest = new HttpGet(getBody);
			/**
			 * Post요청
			 */
			// ArrayList<NameValuePair> postParams = new
			// ArrayList<NameValuePair>();
			// Map<String, String> paramMap = new HashMap<String, String>();
			// Builder builder = RequestConfig.custom();
			// builder.setConnectTimeout(4000);
			// builder.setSocketTimeout(4000);
			// builder.setStaleConnectionCheckEnabled(false);
			// RequestConfig config = builder.build();
			//
			// for (Map.Entry<String, String> entry : paramMap.entrySet()) {
			// postParams.add(new BasicNameValuePair(entry.getKey(),
			// entry.getValue()));
			// }
			//
			// HttpPost getRequest = new HttpPost(url);
			// getRequest.setEntity(new UrlEncodedFormEntity(postParams));
			// getRequest.setConfig(config);
			/** **/

			//System.out.println("executing request to " + target);

			HttpResponse httpResponse = httpclient.execute(target, getRequest);
			HttpEntity entity = httpResponse.getEntity();

//			System.out.println("----------------------------------------");
//			System.out.println(httpResponse.getStatusLine());
			org.apache.http.Header[] headers = httpResponse.getAllHeaders();
			for (int i = 0; i < headers.length; i++) {
//				System.out.println(headers[i]);
			}
//			System.out.println("----------------------------------------");

			if (entity != null) {
				Gson gson = new Gson();
				String json = EntityUtils.toString(entity);
				System.out.println(json);
				HashMap value = gson.fromJson(json, HashMap.class);
				System.out.println(value);
				System.out.println("GET from Hashmap!! : "+value.get("current_user_url"));
				return json;
			}
			
		} catch (Exception e) {
			//e.printStackTrace();
		} finally {
			// When HttpClient instance is no longer needed,
			// shut down the connection manager to ensure
			// immediate deallocation of all system resources
			httpclient.getConnectionManager().shutdown();
		}
		return "";
	}
}
