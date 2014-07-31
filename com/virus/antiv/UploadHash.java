package com.virus.antiv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

public class UploadHash {
	public int percent=0;
	public String hash="";
	int responseCode=100;
	public void uploadhash(Context context, String hash){
		
		try{
			this.hash=hash;
			File file1 = new File(context.getFilesDir(),"apikey.txt");
			BufferedReader reader = new BufferedReader(new FileReader(file1));
			String key = reader.readLine();
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost("https://www.virustotal.com/vtapi/v2/file/report");
			StringBody keyStr= new StringBody(key);
			StringBody resource= new StringBody(hash);
			MultipartEntity builder = new MultipartEntity();
			builder.addPart("apikey", keyStr);
			builder.addPart("resource", resource);
			post.setEntity(builder);
			ResponseHandler<String> responseHandler=new BasicResponseHandler();
	        String responseBody = client.execute(post, responseHandler);
	        JSONObject response=new JSONObject(responseBody);
			this.responseCode= response.getInt("response_code");
			System.out.println(this.responseCode);
			System.out.println(response);
			int positive=0;
			int total=0;
			if(this.responseCode == 1 ){
		        total= response.getInt("total");
		        System.out.println("total : "+total);
		        System.out.println("positive : "+positive);
		        positive=response.getInt("positives");
		        System.out.println("positive : "+positive);
		        this.percent=(positive*100)/total;
		        System.out.println("percent : "+this.percent);
				}
			
			}catch(UnsupportedEncodingException e){
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		
	}
	public void Uploadfile(File file, Context context){
		try{
			//String postUrl="https://www.virustotal.com/vtapi/v2/file/scan";
			HttpClient client= new DefaultHttpClient();
			HttpPost post = new HttpPost ("https://www.virustotal.com/vtapi/v2/file/scan");
			File file1 = new File(context.getFilesDir(),"apikey.txt");
			BufferedReader reader = new BufferedReader(new FileReader(file1));
			String key = reader.readLine();
			FileBody apkfile= new FileBody(file);
			
			try{
				StringBody keyStr= new StringBody(key);
				MultipartEntity builder = new MultipartEntity();
				builder.addPart("apikey", keyStr);
				builder.addPart("file", apkfile);
				post.setEntity(builder);
				HttpResponse response= client.execute(post);
				HttpEntity entity= response.getEntity();
				client.getConnectionManager().shutdown();
			}catch(UnsupportedEncodingException e){
				e.printStackTrace();
			}
			
			
		} catch(Exception e){
			e.printStackTrace();
		}
	
	}

}
