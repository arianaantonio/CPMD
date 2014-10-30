package com.parse.starter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class SignUpActivity extends Activity {

	EditText username;
	EditText password;
	Button signupButton;
	String passwordCreated;
	String usernameCreated;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.i("Sign Up Activity", "Test");  
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signup_layout);  
		
		username = (EditText) findViewById(R.id.newUsername);
		password = (EditText) findViewById(R.id.newPassword);
		signupButton = (Button) findViewById(R.id.newSignUp);
		signupButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				usernameCreated = username.getText().toString();
				passwordCreated = password.getText().toString();
				
				Intent intent = new Intent(getBaseContext(), ContactsActivity.class);
				startActivity(intent);
				
			}
			
		});
		
	}

}
