package com.feup.cmov.busphone_inspector;

import java.util.ArrayList;

import Entities.Ticket;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;

public class TicketSelectionActivity extends Activity {

	private Bundle newbundle;
	private Intent newIntent;

	private Spinner ticktsSpinner;
	private Button validateButton, receiveButton;
	private TextView selectedTicketLabel;

	private ArrayList<Ticket> ticketList;
	private Ticket selectedTicket;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ticket_selection);

		// loading the extras from previous activity
		Bundle bundle = this.getIntent().getExtras();
		ticketList = (ArrayList<Ticket>) bundle.getSerializable("key");
		if (!ticketList.isEmpty())
			selectedTicket = ticketList.get(0);

		// loading the view objects
		ticktsSpinner = (Spinner) findViewById(R.id.ticket_selection_spinner);
		validateButton = (Button) findViewById(R.id.ticket_selection_validate_button);
		receiveButton = (Button) findViewById(R.id.ticket_selection_receive_button);
		selectedTicketLabel = (TextView) findViewById(R.id.ticket_selection_receive_lebel);

		validateButton.setEnabled(false);
		
		// Loading the items for the Spinner
		String[] itemNames = new String[ticketList.size()];
		for (int i = 0; i < ticketList.size(); i++) {
			itemNames[i] = ticketList.get(i).toStringShortVersion();
		}
		ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_dropdown_item, itemNames);
		ticktsSpinner.setAdapter(adapter);
		
		//setting the listeners for the buttons and the spinner
        ticktsSpinner.setOnItemSelectedListener(new MyOnItemSelectedListener());
		receiveButton.setOnClickListener(new View.OnClickListener() 
        {
            @Override
            public void onClick(View v){
        		if(v.getId() == R.id.ticket_selection_receive_button){
        			//TODO: receive from socket the ticket id from passenger application
        			//For now
        			selectedTicket = new Ticket();
        			selectedTicket.setIdticket("12345");
        			validateButton.setEnabled(true);
        			selectedTicketLabel.setText("Selected: " + selectedTicket.toStringShortVersion());
        		}
            }
        });
		validateButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(v.getId() == R.id.ticket_selection_validate_button){
					//TODO: send post validating the ticket
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ticket_selection, menu);
		return true;
	}
	
	private class MyOnItemSelectedListener implements OnItemSelectedListener{
		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
			if(parent.getId() == ticktsSpinner.getId()){
				selectedTicket = ticketList.get(pos);
				selectedTicketLabel.setText("Selected: " + selectedTicket.toStringShortVersion());
				validateButton.setEnabled(true);
			}
		}
		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}
}
