package com.virus.antiv;

import java.util.ArrayList;
import java.util.List;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class appListdbhandler extends SQLiteOpenHelper {
 
	private static final int DATABASE_VERSION=1;
	private static final String DATABASE_NAME="appList.db";
	private static final String TABLE_NAME="appList";
	
	public static final String COLUMN_ID="id";
	public static final String COLUMN_PKNAME="packageName";
	public static final String COLUMN_APPNAME="appName";
	public static final String COLUMN_APPVERSION="appVersion";
	public static final String COLUMN_APPPATH="appPath";
	public static final String COLUMN_PASS="pass";
	public static final String COLUMN_PERCENT="percent";
	public static final String COLUMN_HASH="apkHash";
	
	public appListdbhandler(Context context, String name, 
		CursorFactory app, int version) {
		super(context, DATABASE_NAME, app, DATABASE_VERSION);
	}
	@Override
	public void onCreate(SQLiteDatabase db){
		String CREATE_APPLIST_TABLE="CREATE TABLE "+TABLE_NAME+"("
				+COLUMN_APPNAME+" TEXT,"
						+ COLUMN_PKNAME+" TEXT UNIQUE,"+COLUMN_APPVERSION+" TEXT,"
								+COLUMN_APPPATH+" TEXT,"+COLUMN_HASH+" TEXT,"+COLUMN_PASS+" TEXT,"
										+ COLUMN_PERCENT+" INTEGER)";
		db.execSQL(CREATE_APPLIST_TABLE);
	
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
		db.execSQL("DROP TABLE IF EXISTS"+ TABLE_NAME);
		onCreate(db);
	}
	public void addApp(String appName, String packName, String appVersion, String appPath, String hash){
		ContentValues values = new ContentValues();
		values.put(COLUMN_APPNAME,appName);
		values.put(COLUMN_PKNAME, packName);
		values.put(COLUMN_APPVERSION, appVersion);
		values.put(COLUMN_APPPATH, appPath);
		values.put(COLUMN_HASH, hash);
		values.put(COLUMN_PASS, "null");
		SQLiteDatabase db =this.getWritableDatabase();
		String query="Select * FROM "+TABLE_NAME+" WHERE "+COLUMN_HASH+" = \""+hash+
				"\"";
		Cursor cursor=db.rawQuery(query,null);
		if (!cursor.moveToFirst()){
			db.replace(TABLE_NAME, null, values);
		}
		db.close();
	}
	public String findApp(String packageName){
		String pass="null";
		String query="Select * FROM "+TABLE_NAME+" WHERE "+COLUMN_PKNAME+" = \""+packageName+
				"\"";
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor=db.rawQuery(query,null);
		appListdb appRow= new appListdb();
		if(cursor.moveToFirst()){
			cursor.moveToFirst();
			 pass=cursor.getString(5);
			cursor.close();
		}
		db.close();
		return pass;
	
	}
	public List<appListdb> getAllNull(){
		List<appListdb> appList=new ArrayList<appListdb>();
		String query="Select *FROM "+TABLE_NAME+" WHERE "+COLUMN_PASS+" = \"null\"";
		SQLiteDatabase db=this.getWritableDatabase();
		Cursor cursor= db.rawQuery(query,null);
		
		if (cursor.moveToFirst()){
			do{
				appListdb app = new appListdb();
				app.setAppList(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3),cursor.getString(4), cursor.getString(5));
			    appList.add(app);
			}while(cursor.moveToNext());
		}
		cursor.close();
		db.close();
		return appList;
	}
	
	public void updatePass(String hash, int percent){
		SQLiteDatabase db =this.getWritableDatabase();
		String query="Select * FROM "+TABLE_NAME+" WHERE "+COLUMN_HASH+" = \""+hash+
				"\"";
		Cursor cursor=db.rawQuery(query,null);
		if (cursor.moveToFirst()){
		ContentValues values = new ContentValues();
		values.put(COLUMN_APPNAME,cursor.getString(0));
		values.put(COLUMN_PKNAME, cursor.getString(1));
		values.put(COLUMN_APPVERSION,cursor.getString(2));
		values.put(COLUMN_APPPATH, cursor.getString(3));
		values.put(COLUMN_HASH, hash);
		values.put(COLUMN_PERCENT, percent);
		if(percent > 0){
			values.put(COLUMN_PASS, "fail");
		}else{
			values.put(COLUMN_PASS, "pass");			
		}
		cursor.close();
		db.replace(TABLE_NAME, null, values);
		db.close();
		}
	}
}
