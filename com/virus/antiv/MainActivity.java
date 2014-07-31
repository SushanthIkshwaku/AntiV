package com.virus.antiv;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.app.Activity;
import android.os.Bundle;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	Button btnLogin;
	Button reg;
	LoginDataBaseAdapter loginDataBaseAdapter;
	EditText usr,pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        File file = new File(getApplicationContext().getFilesDir(),"apikey.txt");
        if (file.exists()){
        	Intent intentlist = new Intent(getApplicationContext(),appListGet.class);
			startActivity(intentlist);
        }else{
        	Intent intentReg = new Intent(getApplicationContext(),Register.class);
			startActivity(intentReg);
        }
        
       /* loginDataBaseAdapter=new LoginDataBaseAdapter(this);
        loginDataBaseAdapter=loginDataBaseAdapter.open();
        
        btnLogin=(Button)findViewById(R.id.loginB);
        reg=(Button)findViewById(R.id.Lreg);
        
        reg.setOnClickListener(new View.OnClickListener() {
        	@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intentReg = new Intent(getApplicationContext(),Register.class);
				startActivity(intentReg);
			}
		});
        usr=(EditText)findViewById(R.id.usr);
        pass=(EditText)findViewById(R.id.pass);
        btnLogin.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				String userName=usr.getText().toString();
				String password=pass.getText().toString();
				try{
					MessageDigest md = MessageDigest.getInstance("SHA-256");
					md.update(password.getBytes());
					 
			        byte byteData[] = md.digest();
			 
			        //convert the byte to hex format 
			        StringBuffer sb = new StringBuffer();
			        for (int i = 0; i < byteData.length; i++) {
			         sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
			        }
			        password=sb.toString();
			        
			        md.update(userName.getBytes());
					 
			        byteData = md.digest();
			 
			        //convert the byte to hex format 
			        StringBuffer sb1 = new StringBuffer();
			        for (int i = 0; i < byteData.length; i++) {
			         sb1.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
			        }
			        userName=sb1.toString();
				String storedPass=LoginDataBaseAdapter.getSingleEntry(userName);
				
				if(password.equals(storedPass))
				{
					Toast.makeText(getApplicationContext(), "Account Successfully Login ", Toast.LENGTH_LONG).show();
					Intent intentlist = new Intent(getApplicationContext(),appListGet.class);
					startActivity(intentlist);
				}
				else
				{
					Toast.makeText(getApplicationContext(), "Account invalid ", Toast.LENGTH_LONG).show();				
					}
				}
			catch(NoSuchAlgorithmException e){
				e.printStackTrace();
			}}
		});
    
    }
    @Override
    protected void onDestroy(){
    	super.onDestroy();
    	loginDataBaseAdapter.close();
    }*/
    }
}
