package com.parse.starter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

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
	ListView runsList;
	static ArrayList<HashMap<String, ?>> myData = new ArrayList<HashMap<String, ?>>();
	SimpleAdapter adapter;
	Context context;
	String objectToDelete;
	String username;
	TextView usernameView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contacts_layout);
		context = this;
		final ParseUser currentUser = ParseUser.getCurrentUser();
		username = currentUser.getUsername();
		
		//String userID = currentUser.getObjectId();
		objectToDelete = "";
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Runs");
		query.whereEqualTo("userID", currentUser); 
		query.findInBackground(new FindCallback<ParseObject>() { 

			@Override
			public void done(List<ParseObject> objects,
					com.parse.ParseException e) {
				// TODO Auto-generated method stub 
				//List<ArrayList> data =  objects;
				Log.d("score", "retrieved: " +objects);
				String pulledName;
				float pulledNumber;
				String pulledID;
				for (int i = 0; i < objects.size(); i++) {
					pulledName = objects.get(i).getString("date");
					pulledNumber = objects.get(i).getInt("distance");
					pulledID = objects.get(i).getObjectId(); 
					Log.i("test", "test: " +pulledName+ " " +pulledNumber);
					HashMap<String, Object> displayText = new HashMap<String, Object>();
					displayText.put("date", pulledName);
					displayText.put("distance", pulledNumber);
					displayText.put("objectID", pulledID);
					myData.add(displayText);  
				}
			}
		});
		
		usernameView = (TextView) findViewById(R.id.usernameField);
		usernameView.setText(username);
		dateText = (EditText) findViewById(R.id.editText1);
		distanceText = (EditText) findViewById(R.id.editText2);
		runsList = (ListView) findViewById(R.id.listView1);
		adapter = new SimpleAdapter(context, myData, R.layout.listview,
				new String[] {"date", "distance"}, new int[] {R.id.date, R.id.distance});
		runsList.setAdapter(adapter); 
		runsList.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Log.i(TAG, "Clicked: " +position);
				objectToDelete = myData.get(position).get("objectID").toString();
				Log.i(TAG, "To delete: " +objectToDelete); 
			}
			
		});
		
		addRun = (Button) findViewById(R.id.addButton);
		deleteRun = (Button) findViewById(R.id.deleteButton);
		deleteRun.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ParseQuery<ParseObject> query = ParseQuery.getQuery("Runs");
				query.getInBackground(objectToDelete, new GetCallback<ParseObject>() {

					@Override
					public void done(ParseObject object,
							com.parse.ParseException e) {
						// TODO Auto-generated method stub
						object.deleteInBackground();
					}
					});
				ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Runs");
				query2.whereEqualTo("userID", currentUser);
				query2.findInBackground(new FindCallback<ParseObject>() { 

					@Override
					public void done(List<ParseObject> objects,
							com.parse.ParseException e) {
						String pulledName;
						float pulledNumber;
						String pulledID;
						myData.clear();
						for (int i = 0; i < objects.size(); i++) {
							pulledName = objects.get(i).getString("date");
							pulledNumber = objects.get(i).getInt("distance");
							pulledID = objects.get(i).getObjectId();
							Log.i("test", "test: " +pulledName+ " " +pulledNumber);
							HashMap<String, Object> displayText = new HashMap<String, Object>();
							displayText.put("date", pulledName);
							displayText.put("distance", pulledNumber);
							displayText.put("objectID", pulledID); 
							myData.add(displayText);
							
						}
						adapter.notifyDataSetChanged();
					}
				});
			}
			
		});
		signOut = (Button) findViewById(R.id.signoutButton);
		signOut.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ParseUser.logOut();
				myData.clear();
				Intent intent = new Intent(getBaseContext(), ParseStarterProjectActivity.class);
				startActivity(intent);
				
			}
			
		});
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
					runObject.put("userID", currentUser);
					runObject.saveInBackground();
					
					HashMap<String, Object> displayText = new HashMap<String, Object>();
					displayText.put("date", date);   
					displayText.put("distance", distanceNum);
					myData.add(displayText);
					adapter.notifyDataSetChanged();
					dateText.setText("");
					distanceText.setText("");    
					
				}
				//Log.i(TAG, "Name: " +contactName+ " Phone: " +contactPhone); 
				
			}
			
		});
		
		
	}
	

}
