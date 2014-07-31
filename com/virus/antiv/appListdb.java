package com.virus.antiv;

public class appListdb {
	int id;
	String packageName;
	String appVersion;
	String appName;
	String apkPath;
	String hash;
	String pass;
	int percent;
	
	public appListdb(){}
	public appListdb(String appName, String packageName, String appVersion, String apkPath, String pass,int percent){
		super();
		this.packageName = packageName;
		this.appVersion =appVersion;
		this.appName=appName;
		this.apkPath=apkPath;
		this.pass=pass;
		this.percent=percent;
	}
	public void setAppList(String appName, String packageName, String appVersion, String apkPath,String hash, String pass){
		this.packageName = packageName;
		this.appVersion =appVersion;
		this.appName=appName;
		this.apkPath=apkPath;
		this.pass=pass;
		this.hash= hash;
	}
	 

}
