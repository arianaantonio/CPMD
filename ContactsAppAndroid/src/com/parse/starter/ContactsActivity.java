package com.parse.starter;

import java.util.List;

import android.app.Activity;
import android.net.ParseException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class ContactsActivity extends Activity {
	
	String distance;
	String date;
	EditText distanceText;
	EditText dateText;
	Button addRun;
	Button deleteRun;
	Button signOut;
	String TAG = "Contacts Activity";
	ParseObject runObject = new ParseObject("Runs");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contacts_layout);
		
		ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Runs");
		query2.getInBackground("gI61lUeUKW", new GetCallback<ParseObject>() {
		  public void done(ParseObject object, ParseException e) {
		    if (e == null) {
		      // object will be your game score
		    	Log.d("score1", "Retrieved " + object + " scores");
		    } else { 
		      // something went wrong
		    	Log.d("score2", "Retrieved " + object + " scores");
		    }
		  }

		@Override
		public void done(ParseObject object, com.parse.ParseException e) {
			// TODO Auto-generated method stub
			Log.d("score3", "Retrieved " + object + " scores");
		}
		});
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Runs");
		//query.whereEqualTo("name", "John Smith");
		query.findInBackground(new FindCallback<ParseObject>() {
		    public void done(List<ParseObject> scoreList, ParseException e) {
		        if (e == null) {
		            Log.d("score", "Retrieved " + scoreList.size() + " scores");
		        } else {
		            Log.d("score", "Error: " + e.getMessage());
		        }
		    }

			@Override
			public void done(List<ParseObject> objects,
					com.parse.ParseException e) {
				// TODO Auto-generated method stub 
				//List<ArrayList> data =  objects;
				Log.d("score", "retrieved: " +objects);
				String pulledName;
				float pulledNumber;
				for (int i = 0; i < objects.size(); i++) {
					pulledName = objects.get(i).getString("date");
					pulledNumber = objects.get(i).getInt("distance");
					Log.i("test", "test: " +pulledName+ " " +pulledNumber);
				}
				
			}
		});
		
		dateText = (EditText) findViewById(R.id.editText1);
		distanceText = (EditText) findViewById(R.id.editText2);
		addRun = (Button) findViewById(R.id.addButton);
		deleteRun = (Button) findViewById(R.id.deleteButton);
		signOut = (Button) findViewById(R.id.signoutButton);
		addRun.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				date = dateText.getText().toString();
				distance = distanceText.getText().toString();
				
				float distanceNum = Float.valueOf(distance);  
				if (distance.isEmpty() || date.isEmpty()) {
					Toast.makeText(getApplicationContext(), "Please enter a name and a valid phone number",
							   Toast.LENGTH_LONG).show(); 
				} else {
					
					runObject.put("date", date);
					runObject.put("distance", distanceNum);
					runObject.saveInBackground();
				}
				//Log.i(TAG, "Name: " +contactName+ " Phone: " +contactPhone); 
				
			}
			
		});
		
		
	}
	

}
