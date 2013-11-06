package com.feup.cmov.busphone_inspector;

import java.util.ArrayList;

import Entities.Ticket;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class SelectTicketActivity extends Activity {

	private Bundle newbundle;
	private Intent newIntent;

	private Spinner ticktsSpinner;
	private Button validateButton, receiveButton;
	private TextView selectedTicketLabel;

	private ArrayList<Ticket> ticketList;
	private Ticket selectedTicket;
		
	private static boolean isValidTicket = false;

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

		newbundle = new Bundle();
		newIntent = new Intent(this.getApplicationContext(), SelectTicketActivity.class);
		
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
        			Bundle b = new Bundle();
        			b.putSerializable("key", ticketList);
        			Intent t = new Intent(getApplicationContext(), Inspection_Activity.class);
        			t.putExtras(b);
        			startActivity(t);
        		}
            }
        });
		validateButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				class InspectTicket implements Runnable{
        			@Override
        			public void run() {
        				//gets from server the ticket with the id of the selected ticket
            			final Boolean isAValidTicket = RestAPI.inspectTicket(selectedTicket.getIdticket(), ticketList);
            			runOnUiThread(new Runnable() {
							@Override
							public void run() {
								isValidTicket = isAValidTicket;
								//if the ticket is in the ticket list then its a valid ticket
								if(isValidTicket){
									Toast toast = Toast.makeText(getApplicationContext(), "Valid Ticket!!", Toast.LENGTH_LONG);
									toast.show();
									newbundle.putSerializable("key", ticketList);
									newIntent.putExtras(newbundle);
									startActivity(newIntent);
								}else{
									Toast toast = Toast.makeText(getApplicationContext(), "Invalid Ticket!!", Toast.LENGTH_LONG);
									toast.show();								
								}
							}
						});
        			}
        		}
				if(v.getId() == R.id.ticket_selection_validate_button){
					new Thread(new InspectTicket()).start();
				}
			}
		});
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