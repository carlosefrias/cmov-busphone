package com.feup.cmov.busphone_passenger;

import java.util.ArrayList;

import Entities.Ticket;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class ShowListOfTicketsActivity extends Activity implements OnItemSelectedListener, OnClickListener {

	private Spinner listOfTicketsSpinner;
	private Button validateButton, showDetailsButton, buyticketsButton;
	
	private ArrayList<Ticket> listOfUnusedTickets;
	private Ticket selectedTicket;
	
	private Bundle bundle;
	private Intent validateIntent, showDetailsIntent;
	private String username;
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_list_of_tickets);
		
		// loading extras from the previous activity
		bundle = this.getIntent().getExtras();
		username = (String) bundle.getSerializable("username");
		listOfUnusedTickets = (ArrayList<Ticket>) bundle.getSerializable("listUnusedTickets");
		
		// loading view objects
		listOfTicketsSpinner = (Spinner) findViewById(R.id.spinner_list_of_tickets);
		validateButton = (Button) findViewById(R.id.validateTicketButton);
		showDetailsButton = (Button) findViewById(R.id.show_details_button);
		buyticketsButton = (Button) findViewById(R.id.buy_more_tickets_button);
		
		//Setting the default selected ticket
        if(!listOfUnusedTickets.isEmpty()) selectedTicket = listOfUnusedTickets.get(0);
        
		bundle = new Bundle();
		validateIntent = new Intent(this.getApplicationContext(), ValidationActivity.class);
		showDetailsIntent = new Intent(this.getApplicationContext(), ShowTicketDetaisActivity.class);

		//Loading the items for the Spinner
		String[] itemsOnTheSpinner = new String[listOfUnusedTickets.size()];
    	for(int i = 0; i < listOfUnusedTickets.size(); i++){
    		itemsOnTheSpinner[i] = listOfUnusedTickets.get(i).toStringShortVersion();
    	}
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_dropdown_item, itemsOnTheSpinner);
        listOfTicketsSpinner.setAdapter(adapter);
        
        // setting up the listeners
		listOfTicketsSpinner.setOnItemSelectedListener(this);
		validateButton.setOnClickListener(this);
		showDetailsButton.setOnClickListener(this);
		buyticketsButton.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.show_list_of_tickets, menu);
		return true;
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int pos, long arg3) {
		if(arg0.getId() == listOfTicketsSpinner.getId()){
			selectedTicket = listOfUnusedTickets.get(pos);
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub		
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == validateButton.getId()){
			Toast.makeText(getApplicationContext(), "Sending ticket to validation", Toast.LENGTH_LONG).show();
			bundle.putSerializable("username", username);
			bundle.putSerializable("listUnusedTickets", listOfUnusedTickets);
			bundle.putSerializable("selectedTicket", selectedTicket);
			validateIntent.putExtras(bundle);
			startActivity(validateIntent);
		}
		else if(v.getId() == showDetailsButton.getId()){
			Toast.makeText(getApplicationContext(), "Showing the ticket details", Toast.LENGTH_LONG).show();
			bundle.putSerializable("username", username);
			bundle.putSerializable("listUnusedTickets", listOfUnusedTickets);
			bundle.putSerializable("selectedTicket", selectedTicket);
			showDetailsIntent.putExtras(bundle);
			startActivity(showDetailsIntent);			
		}
		//else if(v.getId())
	}

}
