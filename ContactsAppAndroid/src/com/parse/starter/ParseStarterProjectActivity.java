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
	TextView skipView;
	String TAG = "Main Activity";
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main); 
		
		username = (EditText) findViewById(R.id.usernameText);
		password = (EditText) findViewById(R.id.passwordText);
		loginButton = (Button) findViewById(R.id.loginButton);
		signupButton = (Button) findViewById(R.id.signupButton);
		skipView = (TextView) findViewById(R.id.skipLink);
		
		 

		ParseAnalytics.trackAppOpened(getIntent());

		/*
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

		LoginFragment loginFragment = new LoginFragment();
		SignUpFragment signupFragment = new SignUpFragment();
		fragmentTransaction.remove(signupFragment);
		fragmentTransaction.add(R.id.login_fragment, loginFragment);
		fragmentTransaction.commit();*/
		
		
		
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
				//Bundle bundle = new Bundle();
				//bundle.putSerializable("details", item);
				//detailActivity.putExtras(bundle);
				//startActivityForResult(detailActivity, 0);
				startActivity(detailActivity);
				// TODO Auto-generated method stub
				//FragmentManager manager = getFragmentManager();
				//SignUpFragment fragment = (SignUpFragment) manager.findFragmentById(R.id.signup_fragment);
				//manager.beginTransaction().attach(fragment).commit();
				/*
				Fragment newFragment = new SignUpFragment();
				FragmentTransaction transaction = getFragmentManager().beginTransaction();

				// Replace whatever is in the fragment_container view with this fragment,
				// and add the transaction to the back stack
				transaction.replace(R.id.signup_fragment, newFragment);
				transaction.addToBackStack(null);

				// Commit the transaction
				transaction.commit();*/
			}
			
		});
		
		skipView.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.i(TAG, "Clicked Skip");
				Intent intent = new Intent(getBaseContext(), ContactsActivity.class);
				startActivity(intent);
			}
			
		});
	}
}