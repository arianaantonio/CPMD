package com.parse.starter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.arianaantonio.networkconnection.NetworkConnect;
import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;

public class ParseStarterProjectActivity extends Activity {
	
	/** Called when the activity is first created. */
	
	EditText username;
	EditText password;
	Button loginButton;
	Button signupButton;
	String usernameEntered = "";
	String passwordEntered = "";
	String TAG = "Main Activity";
	Boolean networkConn;
	NetworkConnect networkConnection;
	Context mContext;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main); 
		mContext = this;
		
		username = (EditText) findViewById(R.id.usernameText);
		password = (EditText) findViewById(R.id.passwordText);
		loginButton = (Button) findViewById(R.id.loginButton);
		signupButton = (Button) findViewById(R.id.signupButton);
		
		networkConnection = new NetworkConnect();
		networkConn = networkConnection.connectionStatus(mContext);
		if (!networkConn) {
			Toast.makeText(mContext, "Please connect to a network", Toast.LENGTH_LONG).show();
		} //else {
			//Toast.makeText(mContext, "You are connected", Toast.LENGTH_LONG).show();
	
			ParseAnalytics.trackAppOpened(getIntent());
			//ParseUser.logOut();  
			ParseUser currentUser = ParseUser.getCurrentUser();
			if (currentUser != null) { 
				// do stuff with the user
				Intent intent = new Intent(getBaseContext(), ContactsActivity.class);
				startActivity(intent); 
			} else {
			}
		//} 

		loginButton.setOnClickListener(new OnClickListener(){ 

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				usernameEntered = username.getText().toString();
				passwordEntered = password.getText().toString();
				Log.i(TAG, "Username: " +usernameEntered+ " Password: " +passwordEntered);
				networkConn = networkConnection.connectionStatus(mContext);
				if (networkConn) {
					ParseUser.logInInBackground(usernameEntered, passwordEntered, new LogInCallback() {
						public void done(ParseUser user, ParseException e) {
							if (user != null) {
								Intent intent = new Intent(getBaseContext(), ContactsActivity.class);
								startActivity(intent);
							} else {
								Log.e("Login", "ERROR: " +e);
							}
						}
					});
				} else {
					Toast.makeText(mContext, "Please connect to a network", Toast.LENGTH_LONG).show();
				}
			}

		});  

		signupButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {

				Intent detailActivity = new Intent(getBaseContext(), SignUpActivity.class);
				startActivity(detailActivity);

			}

		});
		new CountDownTimer(20000, 1000) {
		     public void onTick(long millisUntilFinished) {
		    	 
		     }

		     public void onFinish() {
		    	 networkConn = networkConnection.connectionStatus(mContext);
		    	 if (networkConn) {
		    		 //Log.i("Main", "Connected");
		    		 //Toast.makeText(mContext, "Connected", Toast.LENGTH_SHORT).show();
		    	 } else {
		    		 //Log.i("Main", "Not connected");
		    		 //Toast.makeText(mContext, "Not connected", Toast.LENGTH_SHORT).show();
		    	 }
		    	 start();
		     }
		}.start();
		
		
	}
	@Override
	public void onBackPressed() {
		Toast.makeText(mContext, "Please log in or sign up", Toast.LENGTH_SHORT).show();
	}
}
