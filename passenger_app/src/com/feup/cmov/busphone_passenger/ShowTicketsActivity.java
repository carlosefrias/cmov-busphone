package com.feup.cmov.busphone_passenger;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class ShowTicketsActivity extends Activity {
	
	private static final int NEW_TICKET = 10;
	private static final int SINGLE_TICKET = 20;
	
	private String username;
	private static boolean ticketsbought = false;
	
	private Bundle bundle;
	private Intent newIntent;
	
	//private final Spinner numberOfTickesSpinner, typesOfTicketSpinner;
	private String typeSelected = "";
	private String[] types;
	private int numberOfTicketsSelected = 1;
		
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_itemoption_show_tickets);
		
		
		//loading extras from the previous activity
        Bundle bundle = this.getIntent().getExtras();
        username = (String) bundle.getSerializable("username");
        
        //Creating the intent to next activity
		bundle = new Bundle();
		newIntent = new Intent(this.getApplicationContext(), ShowTicketsActivity.class);		
		
		TicketData tickets = new TicketData(this);
		try{
			Cursor cursor = tickets.getAllTickets();
			startManagingCursor(cursor);
			
			if(cursor.getCount() == 0){
				/*
				 * Connects with server.
				 */
			}
			else{
				/*
				 * Inflate all rows in list view.
				 */
			}
		}
		finally{
			tickets.close();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.item_option_show_tickets, menu);
		return true;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch (item.getItemId()){
		case R.id.new_ticket_dialog:
			showDialog(NEW_TICKET);
			return true;
		case R.id.sign_out_action:
			Intent validationIntent = new Intent(getApplicationContext(), LoginActivity.class);
			startActivity(validationIntent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	@SuppressWarnings("deprecation")
	@Override
	protected Dialog onCreateDialog(int id){
		switch(id){
		case NEW_TICKET:
			Builder ntBuilder = new AlertDialog.Builder(this);
			LayoutInflater ntInflater = getLayoutInflater();
			ntBuilder.setTitle(R.string.title_dialog_new_ticket);
			ntBuilder.setView(ntInflater.inflate(R.layout.dialog_itemoption_new_ticket, null))
				.setPositiveButton(R.string.buy_ticket_label, new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int id) {
						//#################################################################
						class BuyTickets implements Runnable{
							private String type;
							private int number;
							public BuyTickets(String type, int number){
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
										if(ticketsbought){
											// Tickets bought successfully
											Toast.makeText(getApplicationContext(), "" + number + 
													" tickets of type " + type + " bought successfully!", 
													Toast.LENGTH_LONG).show();
											bundle.putSerializable("username", username);
											newIntent.putExtras(bundle);
											startActivity(newIntent);											
										}else{
											// Unable to buy tickets
											Toast.makeText(getApplicationContext(), "Unable to buy the tickets", Toast.LENGTH_LONG).show();										
										}										
									}
								});
							}							
						}
						//loading view objects
						Spinner numberOfTickesSpinner = (Spinner) findViewById(R.id.numberOfTicketsToBuySpinner);
						Spinner typesOfTicketSpinner = (Spinner) findViewById(R.id.ticketTypesSpinner);
						types = new String[] {"T1", "T2", "T3"};
						Log.i("DEBUG", "numberOfTickesSpinner: " + numberOfTickesSpinner + " typesOfTicketSpinner: " + typesOfTicketSpinner);
						if(numberOfTickesSpinner != null && typesOfTicketSpinner != null) {
							//TODO: Os spinners dão sempre null NÃO PERCEBO!!!! RESOLVER ISTO
							//Setting the listeners for the Spinners
							numberOfTickesSpinner.setOnItemSelectedListener(new NumberOfTicketsItemSelectedListener(numberOfTickesSpinner));
							typesOfTicketSpinner.setOnItemSelectedListener(new TypeOfTicketItemSelectedListener(typesOfTicketSpinner));
						}
						Log.i("DEBUG", "type: " + typeSelected + " num: " + numberOfTicketsSelected);
						new Thread(new BuyTickets(typeSelected, numberOfTicketsSelected)).start();
						//##################################################################
					}
				})
				.setNegativeButton(R.string.cancel_buy_ticket_label, new DialogInterface.OnClickListener(){
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
			stBuilder.setView(stInflater.inflate(R.layout.dialog_itemoption_single_ticket, null))
				.setPositiveButton(R.string.validate_ticket_button_label, new DialogInterface.OnClickListener(){
					@Override
		            public void onClick(DialogInterface dialog, int id) {
						/**
						 * Implement runnable for retrieving single ticket information
						 * 
						 * class SingleTicketRunnable implements Runnable
						 */
						Intent validationIntent = new Intent(getApplicationContext(), ValidationActivity.class);
						startActivity(validationIntent);
						dismissDialog(SINGLE_TICKET);
		            }
				})
				.setNegativeButton(R.string.go_back_ticket_label, new DialogInterface.OnClickListener(){
					@Override
		            public void onClick(DialogInterface dialog, int id) {
						dismissDialog(SINGLE_TICKET);
		            }
				});
			return stBuilder.create();
		}
		
		return super.onCreateDialog(id);
	}
	
	@SuppressWarnings("deprecation")
	public void dummyAction(View view){
		showDialog(NEW_TICKET);
	}
	
	private class NumberOfTicketsItemSelectedListener implements OnItemSelectedListener{
		private Spinner numberOfTickesSpinner;
		public NumberOfTicketsItemSelectedListener(Spinner numberOfTickesSpinner) {
			this.numberOfTickesSpinner = numberOfTickesSpinner;
		}
		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
			if(parent.getId() == numberOfTickesSpinner.getId()){
				numberOfTicketsSelected = pos + 1;
			}
		}
		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}
	
	private class TypeOfTicketItemSelectedListener implements OnItemSelectedListener{
		private Spinner spinner;
		public TypeOfTicketItemSelectedListener(Spinner spinner){
			this.spinner = spinner;
		}
		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
			if(parent.getId() == spinner.getId()){
				typeSelected = types[pos];
			}
		}
		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}
}
