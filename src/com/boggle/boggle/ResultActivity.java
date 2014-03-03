package com.boggle.boggle;

import java.net.URLEncoder;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.webkit.WebView;

public class ResultActivity extends Activity {

	private class EvalGame extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... params) {
		    try {
		    	String evalUrl = Settings.EVAL_URL 
						+ "&gamestring=" + URLEncoder.encode(params[0], "utf-8") 
						+ "&words=" + URLEncoder.encode(params[1], "utf-8")
						+ "&idgameuser=" + params[2];
		    	Log.d(Settings.DEBUG_TAG, evalUrl);
		    	MyHttpClient httpClient = new MyHttpClient(evalUrl, getApplicationContext());
		    	String response = httpClient.getResponse();
		    	
		    	SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(Settings.PREFS_FILE, Context.MODE_PRIVATE);  
				String secret = sharedPref.getString("SECRET", Settings.NO_USER);
		    	
		    	return Settings.RESULT_URL + "&idGameUser=" + params[2] + "&secret=" + secret;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		protected void onPostExecute(String url) {
			Log.d(Settings.DEBUG_TAG, url);

			WebView myWebView = (WebView) findViewById(R.id.webview);
			myWebView.loadUrl(url);
	    }
	}
	
	private void processIntent(Intent intent) {
		Log.d(Settings.DEBUG_TAG, "New intent...");
	    int idGameUser = intent.getIntExtra("idGameUser", 0);
	    if (idGameUser == 0) {//offline
	    	AlertDialog alertDialog = Alert.CreateAlertDialog(this);
			// show it
			alertDialog.show();
    	} else {
		    String gameString = intent.getStringExtra("gameString");
		    String found = intent.getStringExtra("found");	
		    EvalGame evalGameAsyncTask = new EvalGame();
		    evalGameAsyncTask.execute(gameString, found, Integer.toString(idGameUser));
    	}
	}
	
	protected void onNewIntent(Intent intent) 
    {
		this.processIntent(intent);
    }
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Intent intent = getIntent();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);
		this.processIntent(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.result, menu);
		return true;
	}}
