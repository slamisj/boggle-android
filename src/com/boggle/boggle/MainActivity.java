
package com.boggle.boggle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {

	public void play(View view) {
	    Intent intent = new Intent(this, PlayActivity.class);
	    startActivity(intent);
	}
	
	public void rules(View view) {
	    Intent intent = new Intent(this, RulesActivity.class);
	    startActivity(intent);
	}
	
	public void logout(View view) {
		SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
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
