package com.virus.antiv;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import android.content.pm.PackageInfo;

public class InstallReceiver extends BroadcastReceiver {
	
	Context context;
	
	@Override
	public void onReceive(Context context, Intent intent){
		this.context=context;
		PackageInfo pkgInfo;
		String path="";
		PackageManager packageManager=context.getPackageManager(); 
		//when package is installed
		if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")||intent.getAction().equals("android.intent.action.PACKAGE_REPLACED")){
			Log.e("BroadcastReceiver","onReceive called "+"PACKAGE_ADDED");
			Toast.makeText(context," onReceive !!! PACKAGE_ADDDED",Toast.LENGTH_LONG).show();
			NameNotFoundException e;
			System.out.println("Package name= "+intent.getData().toString());
			String scheme = intent.getData().getScheme();
			System.out.println(scheme);
			path= intent.getDataString();
			path=path.substring(path.indexOf(':')+1);
			System.out.println(path);
			//do{	
			try {
				pkgInfo=packageManager.getPackageInfo(path,0);
				String sourceDir=pkgInfo.applicationInfo.sourceDir;
				System.out.println("Source dir= "+sourceDir);
				Intent intentReg = new Intent(context,BroadcastAct.class);
				intentReg.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intentReg.putExtra("SourceDir", sourceDir);
				context.startActivity(intentReg);
				e=null;
			} catch (NameNotFoundException e1) {
				// TODO Auto-generated catch block
				e=e1;
				e.printStackTrace();
			}
				
		}
	}
	
}
