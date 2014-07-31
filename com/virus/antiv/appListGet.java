package com.virus.antiv;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class appListGet extends ListActivity{
	private PackageManager packageManager =null;
	private List<ApplicationInfo> applist =null;
	private ApplicationAdapter listadaptor=null;
	private ResultAdapter listadapter1=null;
	private ProgressBar spinner;
	private Button scan;
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.applist);
		spinner= (ProgressBar)findViewById(R.id.progress1);
		spinner.setVisibility(View.GONE);
		packageManager=getPackageManager();
		new LoadApplications().execute();
		scan =(Button)findViewById(R.id.scan);
		scan.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				System.out.println("Scan pressed!!!");
				v.setVisibility(View.INVISIBLE);
				spinner.setVisibility(View.VISIBLE);
				new ScanAppList().execute();
			}
		});
		
	}
	
	private List<ApplicationInfo> checkForLaunchIntent(List<ApplicationInfo> list){
		ArrayList<ApplicationInfo> applist = new ArrayList<ApplicationInfo>();
		for(ApplicationInfo info : list){
			try{
				if(null !=packageManager.getLaunchIntentForPackage(info.packageName)){
					applist.add(info);
				}
			} catch(Exception e){
				e.printStackTrace();
			}
		}
		return applist;
	}
	private class LoadApplications extends AsyncTask<Void, Void,Void>{
		@Override
		protected Void doInBackground(Void... params){
			applist = checkForLaunchIntent(packageManager.getInstalledApplications(PackageManager.GET_META_DATA));
			listadaptor = new ApplicationAdapter(appListGet.this,
					R.layout.snippetlist2, applist);
 
			return null;
		}
		@Override
		protected void onCancelled() {
			super.onCancelled();
		}
 
		@Override
		protected void onPostExecute(Void result) {
			setListAdapter(listadaptor);
			
		}
	}
	
	private class ScanAppList extends AsyncTask<Void, Void,Void>{
		
		@Override
		protected Void doInBackground(Void... params){
			int count=0;
			UploadHash hashClass= new UploadHash();
			List<appListdb> appList=new ArrayList<appListdb>();
			appListdbhandler appdbhandler= new appListdbhandler(getApplicationContext(), null, null, 1);
			appList=appdbhandler.getAllNull();
			for (appListdb app:appList){
				String hash=app.hash;
				hashClass.uploadhash(getApplicationContext(),hash);
				count=count+1;
				if(hashClass.responseCode== 1){
					appdbhandler.updatePass(hash, hashClass.percent);
				}
				if (count == 4){
					count=0;
					try {
						Thread.sleep(60000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				
			}
			appList=appdbhandler.getAllNull();
			for (appListdb app:appList){
				String apkpath=app.apkPath;
				try{
					File f =new File(apkpath);
				
				hashClass.Uploadfile(new File(apkpath),getApplicationContext());
				count=count+1;
				if (count == 4){
					count=0;
					try {
						Thread.sleep(60000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				}catch(Exception e){
					continue;
				}
				
				}
 
 
			return null;
		}
		@Override
		protected void onCancelled() {
			super.onCancelled();
		}
 
		@Override
		protected void onPostExecute(Void result) {
			spinner.setVisibility(View.INVISIBLE);
			scan.setVisibility(View.VISIBLE);
			new LoadApplications2().execute();
			
		}
	}
	private class LoadApplications2 extends AsyncTask<Void, Void,Void>{
		@Override
		protected Void doInBackground(Void... params){
			applist = checkForLaunchIntent(packageManager.getInstalledApplications(PackageManager.GET_META_DATA));
			listadapter1 = new ResultAdapter(appListGet.this,
					R.layout.snippetlist, applist);
 
			return null;
		}
		@Override
		protected void onCancelled() {
			super.onCancelled();
		}
 
		@Override
		protected void onPostExecute(Void result) {
			setListAdapter(listadapter1);
			
		}
	}
	
	
}