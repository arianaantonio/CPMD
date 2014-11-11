package com.parse.starter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class ContactsActivity extends Activity {
	
	String distance;
	String date;
	EditText distanceText;
	DatePicker dateText;
	Button addRun;
	Button deleteRun;
	Button signOut;
	Button refreshButton;
	String TAG = "Contacts Activity";
	ParseObject runObject = new ParseObject("Runs");
	ListView runsList;
	static ArrayList<HashMap<String, ?>> myData = new ArrayList<HashMap<String, ?>>();
	SimpleAdapter adapter;
	Context context;   
	String objectToDelete;
	String username;
	TextView usernameView;   
	Button updateRunButton;
	
	@Override      
	protected void onCreate(Bundle savedInstanceState) {
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
					adapter.notifyDataSetChanged();
				}
			}
		});
		
		usernameView = (TextView) findViewById(R.id.usernameField);
		usernameView.setText(username);    
		dateText = (DatePicker) findViewById(R.id.datePicker1);
		distanceText = (EditText) findViewById(R.id.editText2);
		runsList = (ListView) findViewById(R.id.listView1);
		adapter = new SimpleAdapter(context, myData, R.layout.listview,
				new String[] {"date", "distance"}, new int[] {R.id.date, R.id.distance});
		runsList.setAdapter(adapter); 
		
		runsList.setOnItemClickListener(new OnItemClickListener(){
  
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
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
				ParseQuery<ParseObject> query = ParseQuery.getQuery("Runs");
				query.getInBackground(objectToDelete, new GetCallback<ParseObject>() {

					@Override
					public void done(ParseObject object,
							com.parse.ParseException e) {
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
							adapter.notifyDataSetChanged(); 
							   
						}
					}
				});
			}
			
		});
		signOut = (Button) findViewById(R.id.signoutButton);
		signOut.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				ParseUser.logOut();
				myData.clear();
				Intent intent = new Intent(getBaseContext(), ParseStarterProjectActivity.class);
				startActivity(intent);
				
			}
			
		});
		addRun.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				distance = distanceText.getText().toString();
				int day = dateText.getDayOfMonth();
				int month = dateText.getMonth()+1;
				int year = dateText.getYear();
				date = month+"/"+day+"/"+year;
				Log.i("TAG", "Date: " +date);
				   
				if (distance.isEmpty()) {
					Toast.makeText(getApplicationContext(), "Please enter a distance",
							   Toast.LENGTH_SHORT).show();  
				} else {
					float distanceNum = Float.valueOf(distance);
					runObject.put("date", date);
					runObject.put("distance", distanceNum);
					runObject.put("userID", currentUser);
					runObject.saveInBackground();
					
					//HashMap<String, Object> displayText = new HashMap<String, Object>();
					//displayText.put("date", date);   
					//displayText.put("distance", distanceNum);
					//myData.add(displayText);
					//adapter.notifyDataSetChanged();
					distanceText.setText(""); 
					
					ParseQuery<ParseObject> query = ParseQuery.getQuery("Runs");
					query.whereEqualTo("userID", currentUser); 
					query.findInBackground(new FindCallback<ParseObject>() { 
			   
						@Override
						public void done(List<ParseObject> objects,
								com.parse.ParseException e) {
							Log.d("score", "retrieved: " +objects);
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
								adapter.notifyDataSetChanged();
							}
						}
					});
					
				}
				 
				
			}
			
		});
		updateRunButton = (Button) findViewById(R.id.updateRunButton);
		updateRunButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) { 
				ParseQuery<ParseObject> query = ParseQuery.getQuery("Runs");
				distance = distanceText.getText().toString();
				int day = dateText.getDayOfMonth();
				int month = dateText.getMonth()+1;
				int year = dateText.getYear();
				date = month+"/"+day+"/"+year;
				if (distance.isEmpty()) {
					Toast.makeText(getApplicationContext(), "Please enter a distance",
							   Toast.LENGTH_SHORT).show();  
				} else {
					final float distanceNum = Float.valueOf(distance);
					query.getInBackground(objectToDelete, new GetCallback<ParseObject>() {

					@Override
					public void done(ParseObject newRun, ParseException e) {
						newRun.put("date", date);
					    newRun.put("distance", distanceNum);
					    newRun.saveInBackground();
					}
					});
					ParseQuery<ParseObject> queryUpdate = ParseQuery.getQuery("Runs");
					queryUpdate.whereEqualTo("userID", currentUser); 
					queryUpdate.findInBackground(new FindCallback<ParseObject>() { 
			   
						@Override
						public void done(List<ParseObject> objects,
								com.parse.ParseException e) {
							Log.d("score", "retrieved: " +objects);
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
								distanceText.setText("");
								adapter.notifyDataSetChanged();
							}
						}
					});
				}
				
			}
			
		});
		refreshButton = (Button) findViewById(R.id.refreshButton);
		refreshButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				ParseQuery<ParseObject> query = ParseQuery.getQuery("Runs");
				query.whereEqualTo("userID", currentUser); 
				query.findInBackground(new FindCallback<ParseObject>() { 
		   
					@Override
					public void done(List<ParseObject> objects,
							com.parse.ParseException e) {
						Log.d("score", "retrieved: " +objects);
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
							adapter.notifyDataSetChanged();
						}
					}
				});
				
			}
			
		});
		new CountDownTimer(20000, 1000) {
			 int runsCount = 0;
		     public void onTick(long millisUntilFinished) {
		    	 
		     }

		     public void onFinish() {
		    	
		    	 ParseQuery<ParseObject> query = ParseQuery.getQuery("Runs");
		    	 query.whereEqualTo("userID", currentUser);
		    	 query.countInBackground(new CountCallback() {
		    	   public void done(int count, ParseException e) {
		    	     if (e == null) {
		    	    	 runsCount = count; 
		    	    	 Log.i("Runs", "App: " +myData.size()+ " Server: " +runsCount);
		    	    	 if (runsCount != myData.size()) {
		    	    		 ParseQuery<ParseObject> query = ParseQuery.getQuery("Runs");
		    					query.whereEqualTo("userID", currentUser); 
		    					query.findInBackground(new FindCallback<ParseObject>() { 
		    			   
		    						@Override
		    						public void done(List<ParseObject> objects,
		    								com.parse.ParseException e) {
		    							Log.d("score", "retrieved: " +objects); 
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
		    								adapter.notifyDataSetChanged();
		    							}
		    						}
		    					});
		    	    	 } else {
		    	    		 Log.i("Runs Activity", "No new runs");
		    	    	 }
		    	       Log.d(TAG, "User has gone on " + count + " runs");
		    	     } else {
		    	       
		    	     }
		    	   }
		    	 });
		    	 start();
		     }
		  }.start();

		
		
	}
	

}
