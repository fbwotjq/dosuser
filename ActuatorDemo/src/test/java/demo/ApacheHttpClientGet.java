package demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import demo.model.Document;

public class ApacheHttpClientGet {

	public static void main(String[] args) {
		
		long startPoint = System.currentTimeMillis();
		
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpGet getRequest = new HttpGet("http://localhost:38080/");
			getRequest.addHeader("accept", "application/json");
 
			HttpResponse response = httpClient.execute(getRequest);
 
			if (response.getStatusLine().getStatusCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
			}
 
			BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
		
			String contents = "";
			String output;
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				contents += output;
			}
			System.out.println("######################################################");
			System.out.println(contents);
			System.out.println("######################################################");
			httpClient.getConnectionManager().shutdown();
		
			Gson gson = new GsonBuilder().create();
			Document p = gson.fromJson(contents, Document.class);
                
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
 
		
		long endPoint = System.currentTimeMillis();
		System.out.println((endPoint - startPoint) / 1000);
		
	}
	
}
