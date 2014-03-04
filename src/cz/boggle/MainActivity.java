
package cz.boggle;

import org.json.JSONException;
import org.json.JSONObject;

import cz.boggle.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
	Activity activity;
	
	private class GetGameString extends AsyncTask<Void, Void, String> {
		@Override
		protected String doInBackground(Void... params) {
			MyHttpClient httpClient = new MyHttpClient(Settings.GET_GS_URL, getApplicationContext());
			return httpClient.getResponse();
		}
		
	    protected void onPostExecute(String contentAsString) {
	    	int idGameUser = 0;
	    	boolean isError = false;
	    	String upperGameString;
	    	String gameString = "";
	    	JSONObject jObject;
			try {
				if (contentAsString == null) {
					isError = true;
					/*gameString = getLocalGs();
					idGameUser = 0;*/
				} else {
					jObject = new JSONObject(contentAsString);
					idGameUser = jObject.getInt("idGameUser");
		            gameString = jObject.getString("gameString");
				}
			} catch (JSONException e) {
				e.printStackTrace();
				isError = true;
			}
			if (isError) {
				AlertDialog alertDialog = Alert.CreateAlertDialog(activity);
				// show it
				alertDialog.show();
				return;
			}
            
            Intent intent = new Intent(activity, PlayActivity.class);
            intent.putExtra("idGameUser", idGameUser);
        	intent.putExtra("gameString", gameString);
        	startActivity(intent);
            
	    }
	}
	public void play(View view) {
		GetGameString getGsAsyncTask = new GetGameString();
		getGsAsyncTask.execute();
	}
	
	public void rules(View view) {
	    Intent intent = new Intent(this, RulesActivity.class);
	    startActivity(intent);
	}
	
	public void logout(View view) {
		SharedPreferences sharedPref = getSharedPreferences(Settings.PREFS_FILE, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString("SECRET", Settings.NO_USER);
		editor.commit();
		
		Intent intent = new Intent(this, LoginActivity.class);
	    startActivity(intent);
	}
	
	protected void checkLogin() {
		SharedPreferences sharedPref = getSharedPreferences(Settings.PREFS_FILE, Context.MODE_PRIVATE);
		String secret = sharedPref.getString("SECRET", Settings.NO_USER);
		
		Log.d(Settings.DEBUG_TAG, "Secret:" + secret);
		
		if (secret.equals(Settings.NO_USER)) {
			Intent intent = new Intent(this, LoginActivity.class);
		    startActivity(intent);
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(Settings.DEBUG_TAG, "Create...");
		super.onCreate(savedInstanceState);
		this.activity = this;
		setContentView(R.layout.activity_main);
		this.checkLogin();
	}

	protected void onStart() {
		Log.d(Settings.DEBUG_TAG, "Start...");
		super.onStart();
		this.checkLogin();
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
