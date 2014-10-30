package com.parse.starter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.ParseAnalytics;

public class ParseStarterProjectActivity extends Activity {
	
	/** Called when the activity is first created. */
	
	EditText username;
	EditText password;
	Button loginButton;
	Button signupButton;
	String usernameEntered = "";
	String passwordEntered = "";
	String TAG = "Main Activity";
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main); 
		
		username = (EditText) findViewById(R.id.usernameText);
		password = (EditText) findViewById(R.id.passwordText);
		loginButton = (Button) findViewById(R.id.loginButton);
		signupButton = (Button) findViewById(R.id.signupButton);

		ParseAnalytics.trackAppOpened(getIntent());

		loginButton.setOnClickListener(new OnClickListener(){ 

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				usernameEntered = username.getText().toString();
				passwordEntered = password.getText().toString();
				Log.i(TAG, "Username: " +usernameEntered+ " Password: " +passwordEntered);
				
				Intent intent = new Intent(getBaseContext(), ContactsActivity.class);
				startActivity(intent);
				
			}
			
		}); 
		
		signupButton.setOnClickListener(new OnClickListener(){
 
			@Override
			public void onClick(View v) {
				
				Intent detailActivity = new Intent(getBaseContext(), SignUpActivity.class);
				startActivity(detailActivity);
	
			}
			
		});
		
	}
}
