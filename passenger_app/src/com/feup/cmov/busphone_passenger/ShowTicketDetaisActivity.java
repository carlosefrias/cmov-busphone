package com.feup.cmov.busphone_passenger;

import java.util.ArrayList;

import Entities.Ticket;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ShowTicketDetaisActivity extends Activity implements OnClickListener{

	private TextView detailsText;
	private Button validateButton, backButton;

	private Bundle bundle;
	private Intent validateIntent, backIntent;
	
	private String username;
	private ArrayList<Ticket> listOfUnusedTickets;
	private Ticket selectedTicket;
	
	
	
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_ticket_detais);
		
		// loading extras from the previous activity
		bundle = this.getIntent().getExtras();
		username = (String) bundle.getSerializable("username");
		listOfUnusedTickets = (ArrayList<Ticket>) bundle.getSerializable("listUnusedTickets");
		selectedTicket = (Ticket) bundle.getSerializable("selectedTicket");
		
		// loading view objects
		detailsText = (TextView) findViewById(R.id.ticket_details_text);
		detailsText.setText("" + selectedTicket.toString());
		validateButton = (Button) findViewById(R.id.details_validate_button);
		backButton = (Button) findViewById(R.id.details_back_button);
		
		// setting the listeners
		validateButton.setOnClickListener(this);
		backButton.setOnClickListener(this);
		
		bundle = new Bundle();
		validateIntent = new Intent(this.getApplicationContext(), ValidationActivity.class);
		backIntent = new Intent(this.getApplicationContext(), ShowListOfTicketsActivity.class);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.show_ticket_detais, menu);
		return true;
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
		else if(v.getId() == backButton.getId()){
			bundle.putSerializable("username", username);
			bundle.putSerializable("listUnusedTickets", listOfUnusedTickets);
			backIntent.putExtras(bundle);
			startActivity(backIntent);			
		}
	}
}
