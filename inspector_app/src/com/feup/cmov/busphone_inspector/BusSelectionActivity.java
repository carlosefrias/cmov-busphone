package com.feup.cmov.busphone_inspector;


import java.util.ArrayList;

import Entities.Bus;
import Entities.Ticket;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class BusSelectionActivity extends Activity {
	private Spinner busSpinner;
	private Button getTicketsButton;
	private ArrayList<Bus> busList;
	private ArrayList<Ticket> tickets;
	private Bus selectedBus;
	private Bundle bundleNext;
	private Intent newIntent;
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bus_selection);
		
		//loading extras(busList) from previous activity
        Bundle bundle = this.getIntent().getExtras();
        busList = (ArrayList<Bus>) bundle.getSerializable("key");
        //Setting the default selected bus
        if(!busList.isEmpty()) selectedBus = busList.get(0);
        
		bundleNext = new Bundle();
		newIntent = new Intent(this.getApplicationContext(), TicketSelectionActivity.class);
		
		//loading the view objects
		busSpinner = (Spinner) findViewById(R.id.bus_selection_BusList);
		getTicketsButton = (Button) findViewById(R.id.bus_selection_Button);
		getTicketsButton.setEnabled(false);
		
		//Loading the items for the Spinner
		String[] itemNames = new String[busList.size()];
    	for(int i = 0; i < busList.size(); i++){
    		itemNames[i] = busList.get(i).toString();
    	}
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_dropdown_item, itemNames);
        busSpinner.setAdapter(adapter);
        
        //setting the listeners for the spinner and the button
        busSpinner.setOnItemSelectedListener(new MyOnItemSelectedListener());
        getTicketsButton.setOnClickListener(new View.OnClickListener() 
        {
            @Override
            public void onClick(View v){
        		class GetTickets implements Runnable{
        			@Override
        			public void run() {
        				int idBus = selectedBus.getId();
            			final ArrayList<Ticket> tcks = RestAPI.loadTicketListInBus(idBus);
            			runOnUiThread(new Runnable() {
							@Override
							public void run() {
								tickets = tcks;
								if(!tickets.isEmpty()){
									//Successful loading of the tickets
									//passing the tickets to next activity
									bundleNext.putSerializable("key", tickets);
									newIntent.putExtras(bundleNext);
									startActivity(newIntent);
								}
							}
						});
        			}
        		}
        		if(v.getId() == R.id.bus_selection_Button){
        			new Thread(new GetTickets()).start();
        		}
            }
        });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.bus_selection, menu);
		return true;
	}
	
	private class MyOnItemSelectedListener implements OnItemSelectedListener{
		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
			if(parent.getId() == busSpinner.getId()){
				selectedBus = busList.get(pos);
				getTicketsButton.setEnabled(true);
			}
		}
		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			getTicketsButton.setEnabled(false);
		}
	}

}
