package com.boggle.boggle;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.TextView;

public class MyHttpClient {
	String url = "";
	public MyHttpClient(String url, Context context) {
		
		
		SharedPreferences sharedPref = context.getSharedPreferences(Settings.PREFS_FILE, Context.MODE_PRIVATE);  
		String secret = sharedPref.getString("SECRET", Settings.NO_USER);
		
		this.url = url + "&secret=" + secret;
		Log.d(Settings.DEBUG_TAG, this.url);
	}
	public String getResponse() {
		InputStream inputStream = null;
		String contentAsString = "";
		/*if (inputStream == null) {
			return null;
		}*/
		try {
	    	/*ConnectivityManager connMgr = (ConnectivityManager) 
	    	        getSystemService(Context.CONNECTIVITY_SERVICE);
    	    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
    	    if (networkInfo != null && networkInfo.isConnected()) {*/
    	    	int len = 5000;
    	        try {
    	            URL url = new URL(this.url);
    	            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    	            conn.setReadTimeout(10000 /* milliseconds */);
    	            conn.setConnectTimeout(15000 /* milliseconds */);
    	            conn.setRequestMethod("GET");
    	            conn.setDoInput(true);
    	            // Starts the query
    	            conn.connect();
    	            int response = conn.getResponseCode();
    	            Log.d(Settings.DEBUG_TAG, "The response is: " + response);
    	            inputStream = conn.getInputStream();
    	            // Convert the InputStream into a string
    	            Reader reader = null;
    	            reader = new InputStreamReader(inputStream, "UTF-8");        
    	            char[] buffer = new char[len];
    	            reader.read(buffer);
    	            contentAsString = new String(buffer);
    	            return contentAsString;
    	        // Makes sure that the InputStream is closed after the app is
    	        // finished using it.
    	        } catch (Exception e) {
	    	    	Log.d(Settings.DEBUG_TAG, "Ex1:" + e.getMessage() + e.getLocalizedMessage() + e.getClass().toString());
    	        } finally {
    	            if (inputStream != null) {
    	            	inputStream.close();
    	            } 
    	        }
	    } catch (Exception e) {
	    	Log.d(Settings.DEBUG_TAG, "Ex2:" + e.getMessage() + e.getLocalizedMessage());
		}
		return null;
	}
}
