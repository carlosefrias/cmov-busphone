package com.feup.cmov.busphone_passenger;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class BuyTicktsActivity extends Activity implements OnClickListener{
	
	private Bundle bundle;
	private Intent newIntent;
	private String username;
	
	private Spinner ticketTypesSpinner, ticketNumberSpinner;
	private Button buyTicketsButton;
	
	private int numberOfTicketsSelected;
	private String typeSelected;
	private String[] types;
	
	private static boolean ticketsbought;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_buy_ticktes);
		
		// loading extras from the previous activity
		bundle = this.getIntent().getExtras();
		username = (String) bundle.getSerializable("username");
		
		Log.i("Username:", username);
		
		// loading view objects
		ticketTypesSpinner = (Spinner) findViewById(R.id.spinnerTypeofTickts);
		ticketNumberSpinner = (Spinner) findViewById(R.id.spinnerNumberOfTicks);
		buyTicketsButton = (Button) findViewById(R.id.buttonBuyTickets);
		
		buyTicketsButton.setOnClickListener(this);
		ticketNumberSpinner.setOnItemSelectedListener(new NumberOfTicketsItemSelectedListener());
		types = new String[]{"T1", "T2", "T3"};
		ticketTypesSpinner.setOnItemSelectedListener(new TypeOfTicketItemSelectedListener());
		
		bundle = new Bundle();
		newIntent = new Intent(this.getApplicationContext(), ShowTicketsActivity.class);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.buy_ticktes, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		class BuyTickets implements Runnable {
			private String type;
			private int number;
			public BuyTickets(String type, int number) {
				this.number = number;
				this.type = type;
			}
			@Override
			public void run() {
				final boolean ticketsBought = RestAPI.buyTickets(type, number, username);
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						ticketsbought = ticketsBought;
						if (ticketsbought) {
							if(numberOfTicketsSelected != 0){
								// Tickets bought successfully
								Toast.makeText(getApplicationContext(), "" 
												+ number
												+ " tickets of type "
												+ type
												+ " bought successfully!", Toast.LENGTH_LONG).show();
							
								bundle.putSerializable("username", username);
								newIntent.putExtras(bundle);
								startActivity(newIntent);
							}
						} else {
							// Unable to buy tickets
							Toast.makeText(getApplicationContext(), "Unable to buy the tickets", Toast.LENGTH_LONG).show();
						}
					}
				});
			}
		}
		if(v.getId() == R.id.buttonBuyTickets){		
			Log.i("DEBUG", "type: " + typeSelected + " num: " + numberOfTicketsSelected);
			new Thread(new BuyTickets(typeSelected, numberOfTicketsSelected)).start();
		}
	}

	private class NumberOfTicketsItemSelectedListener implements OnItemSelectedListener {
		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {
			if (parent.getId() == ticketNumberSpinner.getId()) {
				numberOfTicketsSelected = pos;
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}

	private class TypeOfTicketItemSelectedListener implements OnItemSelectedListener {		
		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {
			if (parent.getId() == ticketTypesSpinner.getId()) {
				int arg = 0;
				if(pos <= 3 && pos > 0) arg = pos - 1;//to make sure that the argument is only 0, 1 or 2
				typeSelected = types[arg];
			}
		}
		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}
}
