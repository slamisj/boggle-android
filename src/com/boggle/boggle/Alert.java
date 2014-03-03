package com.boggle.boggle;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

public class Alert {

	public static AlertDialog CreateAlertDialog(final Activity activity) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
		// set title
		alertDialogBuilder.setTitle("Offline");
		// set dialog message
		alertDialogBuilder
			.setMessage("Aplikace potøebuje pøipojení k síti.")
			.setCancelable(false)
			.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();
				}
			  });
		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();
		return alertDialog;
	}

}
