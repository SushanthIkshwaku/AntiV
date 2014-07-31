package com.virus.antiv;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Register extends Activity {
	EditText editApiKey;
	TextView editTextVirusTotal;
	Button setup;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		editApiKey=(EditText)findViewById(R.id.apikey);
		editTextVirusTotal=(TextView)findViewById(R.id.linkt);
		setup=(Button)findViewById(R.id.setupB);
		setup.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String apiKey=editApiKey.getText().toString();
				File file = new File(getApplicationContext().getFilesDir(),"apikey.txt");
					FileWriter writer;
					try {
						writer = new FileWriter(file,false);
						writer.write(apiKey);
						writer.flush();
						writer.close();
						Toast.makeText(getApplicationContext(), "Account Successfully Created ", Toast.LENGTH_LONG).show();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
									
					Intent intentlist = new Intent(getApplicationContext(),appListGet.class);
					startActivity(intentlist);
				
				
			}
		});
		
		editTextVirusTotal.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.virustotal.com/en/"));
				startActivity(browserIntent);
			}
		});
		
	}
}
