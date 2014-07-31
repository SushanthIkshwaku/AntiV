package com.virus.antiv;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HttpsURLConnection;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.json.JSONObject;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

public class BroadcastAct extends Activity {
	Button getReport;
	String path,hash;
	Boolean report=false;
	int percent=1;
	private Handler handler = new Handler();
	final int INTERVAL = 1000 * 60 * 2;
	private ProgressBar spinner;
	TextView result,twait;
	ImageView shield;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_broadcast);
		spinner= (ProgressBar)findViewById(R.id.progress2);
		twait=(TextView)findViewById(R.id.broadcastwait);
		result=(TextView)findViewById(R.id.apkresult);
		shield=(ImageView)findViewById(R.id.bshield);
		shield.setVisibility(View.INVISIBLE);
		result.setVisibility(View.INVISIBLE);
		Intent intent =getIntent();
		System.out.println("Broadcast act started");
		path=intent.getExtras().getString("SourceDir");
		ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo= connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()){
			Toast.makeText(this," connected to internet!!",Toast.LENGTH_LONG).show();
		
			new SingleFileScan().execute();
		}else{
			Toast.makeText(this," Not connected to internet try again!!",Toast.LENGTH_LONG).show();
		}
		if (networkInfo != null && networkInfo.isConnected())
			new SingleFileReport().execute();
		
		
	}
	
 
	private class SingleFileScan extends AsyncTask <Void, Void, String>
	{
		@Override
		protected String doInBackground(Void... p){
			String content="";
			File file=new File(path);
			try{
				FileInputStream fis= new FileInputStream(path);
				 MessageDigest md = MessageDigest.getInstance("SHA-256");
				 byte[] dataByte = new byte[1024];
				 int nread=0;
				 while((nread=fis.read(dataByte))!= -1){
					 md.update(dataByte,0,nread);
				 }
				 fis.close();
				 byte[] mdbytes=md.digest();
		        StringBuffer sb1 = new StringBuffer();
		        for (int i = 0; i < mdbytes.length; i++) {
		         sb1.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
		        }
				 hash=sb1.toString();
				 

			} catch(NoSuchAlgorithmException e){
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try{
				//String postUrl="https://www.virustotal.com/vtapi/v2/file/scan";
				HttpClient client= new DefaultHttpClient();
				HttpPost post = new HttpPost ("https://www.virustotal.com/vtapi/v2/file/scan");
				File file1 = new File(getApplicationContext().getFilesDir(),"apikey.txt");
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
					BufferedReader rd=new BufferedReader(new InputStreamReader(entity.getContent()));
					String body="";
					
					while((body = rd.readLine())!=null){
						content+=body+"\n";
					}
					client.getConnectionManager().shutdown();
				}catch(UnsupportedEncodingException e){
					e.printStackTrace();
				}
				
				
			} catch(Exception e){
				e.printStackTrace();
			}
			return content;
		}
		@Override
		protected void onPostExecute(String result){
			System.out.println("Response: "+result);
		}
	}
	
	private class SingleFileReport extends AsyncTask <Void, Void, Void>{
		@Override
		protected Void doInBackground(Void... p){
			int count=0;
			UploadHash hashClass= new UploadHash();
			while(hashClass.responseCode != 1){
				count+=1;
			    hashClass.uploadhash(getApplicationContext(),hash);
				report=true;
				percent=hashClass.percent;
				if(count == 3){
					count=0;
					try {
						Thread.sleep(60000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		return null;
		}
		@Override
		protected void onPostExecute(Void p){
			if (percent==0){
				result.setText("The installed apk passed the scan.");
					shield.setImageResource(R.drawable.gshield);
				}if (percent>0){
					result.setText("The installed apk fail the scan. Please uninstall it.");
					shield.setImageResource(R.drawable.rshield);
				}
				shield.setVisibility(View.VISIBLE);
				result.setVisibility(View.VISIBLE);
				spinner.setVisibility(View.GONE);
				twait.setVisibility(View.GONE);
			

		}
	}


}
