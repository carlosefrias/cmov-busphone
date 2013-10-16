package com.feup.cmov.busphone_inspector;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class BusSelectionActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bus_selection);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.bus_selection, menu);
		return true;
	}

}
