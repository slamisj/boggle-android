package cz.boggle;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.boggle.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.PorterDuff.Mode;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class PlayActivity extends Activity {
	Activity activity;
	int idGameUser;
	String gameString = "";
	Counter counter;
	boolean isResultRunning = false;
	//secs past
	int time = 0;
	//found words separated by ;
	String found = "";
    
	public static String shuffleString(String string)
	{
	  List<String> letters = Arrays.asList(string.split(""));
	  Collections.shuffle(letters);
	  String shuffled = "";
	  for (String letter : letters) {
	    shuffled += letter;
	  }
	  return shuffled;
	}
	
	protected String getLocalGs() {
		String retval = "";
		Random rnd = new Random();
		String[] conf = {
			      "eekyjs",
			      "ltidze",
			      "dvicwu",
			      "madial",
			      "pekrea",
			      "petbsc",
			      "teauob",
			      "aclron",
			      "lunvs#",
			      "sumeao",
			      "oevinq",
			      "yjomit",
			      "ornotz",
			      "yfznvk",
			      "iakrgs",
			      "rxhiop"
				};
		for (String val : conf) {
			retval += val.charAt(rnd.nextInt(6));
		}
		return shuffleString(retval);
	}
	
    protected void colorButtons() {
    	Button buttonDel = (Button) findViewById(R.id.buttonDel);
	    buttonDel.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFF0000AA));
	    
	    Button buttonSend = (Button) findViewById(R.id.buttonSend);
	    buttonSend.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFF00FF00));
	    
	    Button buttonEnd = (Button) findViewById(R.id.buttonEnd);
	    buttonEnd.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFFAA0000));
    }
    
    public void runResult() {
    	Log.d(Settings.DEBUG_TAG, "End...");
    	if (!this.isResultRunning) {
	    	this.isResultRunning = true;
	    	Intent intent = new Intent(this, ResultActivity.class);
	    	intent.putExtra("idGameUser", idGameUser);
	    	intent.putExtra("gameString", gameString);
	    	intent.putExtra("found", found);
		    startActivity(intent);
    	}
    }
    
    public void end(View view) {
    	this.counter.cancel(true);
    	this.runResult();
	}
    
		
	
	private class Counter extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			final ProgressBar progressBar;
    		progressBar = (ProgressBar) findViewById(R.id.progressBar1);
            while (time <= Settings.TIME) {
            	Log.d(Settings.DEBUG_TAG, "time " + time);
            	try {
                	progressBar.setProgress(time);
                	time++;
                	Thread.sleep(1000);
            	} catch (InterruptedException e){
                    e.printStackTrace();
                    break;
            	}
            }
            Log.d(Settings.DEBUG_TAG, "go away");
			return null;
		}
		protected void onPostExecute(Void result) {
			Log.d(Settings.DEBUG_TAG, "counter finished");
			runResult();
	    }
	}
	
	protected void onCreate(Bundle savedInstanceState) {
		Intent intent = this.getIntent();
		String upperGameString = "";
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play);
		this.activity = this;
		Log.d(Settings.DEBUG_TAG, "Create...");	

    	char currentChar;
        super.onNewIntent(intent);
        idGameUser = intent.getIntExtra("idGameUser", 0);
        gameString = intent.getStringExtra("gameString");
        Log.d(Settings.DEBUG_TAG, "Start...");
	    this.colorButtons();
	    upperGameString = gameString.toUpperCase();
		for (int i = 1; i <= 16; i++) {
			int resID = getResources().getIdentifier("button" + i,
				    "id", getPackageName());
			Button button = (Button) findViewById(resID);
			currentChar = upperGameString.charAt(i - 1);
			button.setText(Character.toString(currentChar).equals("#") 
						? "CH" 
						: Character.toString(currentChar));
		}
        this.counter = new Counter();
        this.counter.execute(); 
	}
	
	protected void onStop() {
		super.onStop();
		Log.d(Settings.DEBUG_TAG, "Stop...");
		//this.countdown.sendStop();
	}

	protected void onPause(){
	    super.onPause();
	    this.counter.cancel(true);
    	this.runResult();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.play, menu);
		return true;
	}
	
	public void delWord(View view) {
		TextView text = (TextView) findViewById(R.id.current_word);
		text.setText("");
		this.clearButtons();
	}
	
	public void addLetterCommon(View view) {
		TextView currentWord = (TextView) findViewById(R.id.current_word);
	    Button pressedButton = (Button) view;
	    String addedLetter = pressedButton.getText().toString();
	    currentWord.setText(currentWord.getText() + addedLetter);
	    try {
			JSONObject variants = new JSONObject("{\"A\":[\"\u00c1\"],\"C\":[\"\",\"\u010c\"],\"D\":[\"\",\"\u010e\"],\"E\":[\"\u00c9\",\"\u011a\"],\"I\":[\"\u00cd\"],\"N\":[\"\",\"\u0147\"],\"O\":[\"\u00d3\"],\"R\":[\"\",\"\u0158\"],\"S\":[\"\",\"\u0160\"],\"T\":[\"\",\"\u0164\"],\"U\":[\"\u00da\",\"\u016e\"],\"Y\":[\"\u00dd\"],\"Z\":[\"\",\"\u017d\"]}");

			Button dia1 = (Button) findViewById(R.id.dia1);
			Button dia2 = (Button) findViewById(R.id.dia2);
			dia1.setText("");
			dia2.setText("");
			
			if (variants.has(addedLetter)) {
				JSONArray jArray = new JSONArray(variants.getString(addedLetter));
				dia1.setText(jArray.getString(0));
				if (jArray.length() == 2) { 
					dia2.setText(jArray.getString(1));
				}
			}
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			Log.d(Settings.DEBUG_TAG, e1.getMessage());
			e1.printStackTrace();
		}
	}
	
	/** Called when the user adds std letter */
	public void addLetter(View view) {
		Button pressedButton = (Button) view;
		this.addLetterCommon(view);
	    pressedButton.getBackground().setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0xFF00AA00));
	}
	
	/** Called when the user adds diacritics */
	public void addDia(View view) {
		Button pressedButton = (Button) view;
	    String addedLetter = pressedButton.getText().toString();
	    if (addedLetter.length() > 0) {
			TextView text = (TextView) findViewById(R.id.current_word);
			String curVal = text.getText().toString(); 
			text.setText(curVal.substring(0, curVal.length() - 1));
			this.addLetterCommon(view);
	    }
	}
	
	public void clearButtons() {
		for (int i = 1; i <= 16; i++) {
			int resID = getResources().getIdentifier("button" + i,
				    "id", getPackageName());
			Button button = (Button) findViewById(resID);
			button.getBackground().setColorFilter(null);
		}
		Button dia1 = (Button) findViewById(R.id.dia1);
		Button dia2 = (Button) findViewById(R.id.dia2);
		dia1.setText("");
		dia2.setText("");
	}
	
	public void sendWord(View view) {
	    TextView text = (TextView) findViewById(R.id.current_word);
	    String message = text.getText().toString();
	    this.found += ";" + message;
	    text.setText("");
	    Log.d(Settings.DEBUG_TAG, "Added:" + message);
	    this.clearButtons();
	}

}