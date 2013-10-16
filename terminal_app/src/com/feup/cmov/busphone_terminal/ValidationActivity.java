package com.feup.cmov.busphone_terminal;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class ValidationActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_validation);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.validation, menu);
		return true;
	}

	/**
	 * Implement runnable for validation (it must be always available to build a transmission channel).
	 * 
	 * class ValidationRunnable implements Runnable
	 */
	
}
