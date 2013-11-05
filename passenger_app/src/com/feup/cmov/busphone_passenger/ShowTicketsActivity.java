package com.feup.cmov.busphone_passenger;

import java.util.ArrayList;

import Entities.Ticket;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ShowTicketsActivity extends Activity{

	private static final int NEW_TICKET = 10;
	private static final int SINGLE_TICKET = 20;

	private String username;
	private static boolean ticketsbought = false;

	private Bundle bundle;
	
	private String typeSelected = "T1";
	private String[] types;
	private int numberOfTicketsSelected = 1;
	
	private Spinner numberOfTickesSpinner, typesOfTicketSpinner;
	private ListView ticketsListView;
	private ArrayList<Ticket> listOfUnusedTickets;
	private Ticket selectedTicket;

	@SuppressWarnings({ "deprecation", "unchecked" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_itemoption_show_tickets);

		// loading extras from the previous activity
		bundle = this.getIntent().getExtras();
		username = (String) bundle.getSerializable("username");
		listOfUnusedTickets = (ArrayList<Ticket>) bundle.getSerializable("listUnusedTickets");
		
		//setting the default selected ticket
		if(!listOfUnusedTickets.isEmpty()) 
			selectedTicket = listOfUnusedTickets.get(0);
		
	
		//Loading the items for the Spinner
		String[] itemsOnListView = new String[listOfUnusedTickets.size()];
    	for(int i = 0; i < listOfUnusedTickets.size(); i++){
    		itemsOnListView[i] = listOfUnusedTickets.get(i).toStringShortVersion();
    	}
		// loading view objects
		ticketsListView = (ListView) findViewById(R.id.Tickets_listView);
		ticketsListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				selectedTicket = listOfUnusedTickets.get(arg2);		
				Log.i("DEBUG", "clicou no "+selectedTicket.toStringShortVersion());
				showDialog(SINGLE_TICKET);
			}
		});
		
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_dropdown_item, itemsOnListView);
        ticketsListView.setAdapter(adapter);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.item_option_show_tickets, menu);
		return true;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.new_ticket_dialog:
			showDialog(NEW_TICKET);
			return true;
		case R.id.sign_out_action:
			Intent validationIntent = new Intent(getApplicationContext(),
					LoginActivity.class);
			startActivity(validationIntent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case NEW_TICKET:
			Builder ntBuilder = new AlertDialog.Builder(this);
			LayoutInflater ntInflater = getLayoutInflater();
			ntBuilder.setTitle(R.string.title_dialog_new_ticket);
			final View layoutView = ntInflater.inflate(R.layout.dialog_itemoption_new_ticket, null);
			ntBuilder.setView(layoutView);
			ntBuilder.setPositiveButton(R.string.buy_ticket_label,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {
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
												// Tickets bought successfully
												Toast.makeText(getApplicationContext(), "" 
																+ number
																+ " tickets of type "
																+ type
																+ " bought successfully!", Toast.LENGTH_LONG)
														.show();
											} else {
												// Unable to buy tickets
												Toast.makeText(getApplicationContext(), "Unable to buy the tickets", Toast.LENGTH_LONG).show();
											}
										}
									});
								}
							}
							// loading view objects
							numberOfTickesSpinner = (Spinner) layoutView.findViewById(R.id.numberOfTicketsToBuySpinner);
							typesOfTicketSpinner = (Spinner) layoutView.findViewById(R.id.ticketTypesSpinner);
							types = new String[] { "T1", "T2", "T3" };
							if (numberOfTickesSpinner != null && typesOfTicketSpinner != null) {
								// Setting the listeners for the Spinners
								numberOfTickesSpinner.setOnItemSelectedListener(new MyOwnItemSelectedListener());
								typesOfTicketSpinner.setOnItemSelectedListener(new MyOwnItemSelectedListener());
							}
							new Thread(new BuyTickets(typeSelected,	numberOfTicketsSelected)).start();
						}
					});

			ntBuilder.setNegativeButton(R.string.cancel_buy_ticket_label,new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int id) {
							dismissDialog(NEW_TICKET);
						}
					});
			return ntBuilder.create();
		case SINGLE_TICKET:
			Builder stBuilder = new AlertDialog.Builder(this);
			LayoutInflater stInflater = getLayoutInflater();
			stBuilder.setTitle(R.string.title_dialog_single_ticket);
			final View layoutView2 = stInflater.inflate(R.layout.dialog_itemoption_single_ticket, null);
			TextView ticketDetailsText = (TextView) layoutView2.findViewById(R.id.dialog_show_ticket_text);
			ticketDetailsText.setText(selectedTicket.toString());
			stBuilder.setView(layoutView2);
			stBuilder.setPositiveButton(R.string.validate_ticket_button_label, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {	
						// setting the listener
						Intent validationIntent = new Intent(getApplicationContext(), ValidationActivity.class);
						bundle.putSerializable("username", username);
						bundle.putSerializable("listUnusedTickets", listOfUnusedTickets);
						bundle.putSerializable("selectedTicket", selectedTicket);
						validationIntent.putExtras(bundle);
						startActivity(validationIntent);
						dismissDialog(SINGLE_TICKET);
					}
				})
				.setNegativeButton(R.string.go_back_ticket_label,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int id) {
								dismissDialog(SINGLE_TICKET);
							}
						});
			return stBuilder.create();
		}

		return super.onCreateDialog(id);
	}

	@SuppressWarnings("deprecation")
	public void dummyAction(View view) {
		showDialog(NEW_TICKET);
	}

	private class MyOwnItemSelectedListener implements OnItemSelectedListener {
		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {
			if (parent.getId() == numberOfTickesSpinner.getId()) {
				if(pos > 0) numberOfTicketsSelected = pos;
				else numberOfTicketsSelected = 1;
			}
			if (parent.getId() == typesOfTicketSpinner.getId()) {
				typeSelected = types[pos];
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}
}
