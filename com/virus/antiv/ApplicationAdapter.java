package com.virus.antiv;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.apache.commons.codec.binary.Hex;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
 
public class ApplicationAdapter extends ArrayAdapter<ApplicationInfo>  
{
	private List<ApplicationInfo> appsList = null;
	private Context context;
	private PackageManager packageManager;
	public ApplicationAdapter(Context context, int textViewResourceId,
			List<ApplicationInfo> appsList) {
		super(context, textViewResourceId, appsList);
		this.context = context;
		this.appsList = appsList;
		packageManager = context.getPackageManager();
	}
 
	@Override
	public int getCount() {
		return ((null != appsList) ? appsList.size() : 0);
	}
 
	@Override
	public ApplicationInfo getItem(int position) {
		return ((null != appsList) ? appsList.get(position) : null);
	}
 
	@Override
	public long getItemId(int position) {
		return position;
	}
 
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (null == view) {
			LayoutInflater layoutInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = layoutInflater.inflate(R.layout.snippetlist, null);
		}
 
		ApplicationInfo data = appsList.get(position);
		if (null != data) {
			appListdbhandler appdbHandler= new appListdbhandler(context,null,null,1);
			TextView appName = (TextView) view.findViewById(R.id.app_name);
			TextView packageName = (TextView) view.findViewById(R.id.app_paackage);
			ImageView iconview = (ImageView) view.findViewById(R.id.app_icon);
 
			//appName.setText(Integer.toString(data.hashCode()));
			packageName.setText(data.loadLabel(packageManager));
			iconview.setImageDrawable(data.loadIcon(packageManager));
			PackageInfo pkgInfo;
			try {
				pkgInfo = packageManager.getPackageInfo((data.packageName).toString(), 0);
				appName.setText(pkgInfo.versionName);
				
				try{
					FileInputStream fis= new FileInputStream(data.sourceDir);
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
					 appdbHandler.addApp(data.loadLabel(packageManager).toString(),(data.packageName),pkgInfo.versionName,data.sourceDir,sb1.toString());
					 

				} catch(NoSuchAlgorithmException e){
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			} catch (PackageManager.NameNotFoundException e) {
				
			}
			
		}
		return view;
	}
}

