package com.feup.cmov.busphone_passenger;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
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
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ShowTicketsActivity extends Activity implements OnClickListener{

	private static final int NEW_TICKET = 10;
	private static final int SINGLE_TICKET = 20;

	private String username;
	private static boolean ticketsbought = false;
	private static Ticket lastTicketUsed = null;

	private Bundle bundle;
	
	private String typeSelected = "T1";
	private String[] types = new String[] {"", "T1", "T2", "T3" };
	private int numberOfTicketsSelected = 1;
	
	private Spinner numberOfTickesSpinner, typesOfTicketSpinner;
	private Button inspectButton;
	private ListView ticketsListView;
	private ArrayList<Ticket> listOfUnusedTickets;
	private Ticket selectedTicket;
	
	private Socket socket;
	private String serverIpAddress = "10.0.2.2";
	// AND THAT'S MY DEV'T MACHINE WHERE PACKETS TO
	// PORT 5000 GET REDIRECTED TO THE SERVER EMULATOR'S
	// PORT 6000
	private static final int REDIRECTED_SERVERPORT = 5000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_itemoption_show_tickets);

		// loading extras from the previous activity
		bundle = this.getIntent().getExtras();
		username = (String) bundle.getSerializable("username");
		
		// loading view objects
		inspectButton = (Button) findViewById(R.id.inspectTicket);
		inspectButton.setOnClickListener(this);

	}
	@SuppressWarnings("unchecked")
	@Override
	protected void onStart(){
		super.onStart();
		listOfUnusedTickets = (ArrayList<Ticket>) bundle.getSerializable("listUnusedTickets");
		Log.i("Show", ""+listOfUnusedTickets.size());
		
		//setting the default selected ticket
		if(!listOfUnusedTickets.isEmpty()) 
			selectedTicket = listOfUnusedTickets.get(0);
		
		//Loading the items for the listView
		String[] itemsOnListView = new String[listOfUnusedTickets.size()];
    	for(int i = 0; i < listOfUnusedTickets.size(); i++){
    		itemsOnListView[i] = listOfUnusedTickets.get(i).toStringShortVersion();
    	}
		// loading view objects
		ticketsListView = (ListView) findViewById(R.id.tickets_list_view);
		ticketsListView.setOnItemClickListener(new OnItemClickListener() {

			@SuppressWarnings("deprecation")
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
			Intent validationIntent = new Intent(getApplicationContext(), LoginActivity.class);
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
							// Setting the listeners for the Spinners
							numberOfTickesSpinner.setOnItemSelectedListener(new MyOwnItemSelectedListener(1));
							typesOfTicketSpinner.setOnItemSelectedListener(new MyOwnItemSelectedListener(2));
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
		private int x;
		MyOwnItemSelectedListener(int x){
			this.x=x;
		}
		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {
			if (x==1) {
				//Nao entra aqui...?????
				System.out.println("*************************************************************");
				if(pos !=0 ) numberOfTicketsSelected = pos;
			}
			if (x==2) {
				System.out.println("************##############################*******************");
				if(pos !=0 ) typeSelected = types[pos];
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}

	@Override
	
	public void onClick(View arg0) {
		if(arg0.getId() == inspectButton.getId()){
			new Thread(new Runnable() {
				@Override
				public void run() {
					final Ticket ticket = RestAPI.getLastUsedTicket(username);
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							lastTicketUsed = ticket;
							if(lastTicketUsed != null){
								new Thread(new Runnable() {
									@Override
									public void run() {
										try {
											InetAddress serverAddr = InetAddress.getByName(serverIpAddress);
											socket = new Socket(serverAddr, REDIRECTED_SERVERPORT);
										} catch (UnknownHostException e1) {
											e1.printStackTrace();
										} catch (IOException e1) {
											e1.printStackTrace();
										}
										try {
											String stringToSend = lastTicketUsed.getIdticket() + " " + "inspect";
											PrintWriter out = new PrintWriter(new BufferedWriter(
													new OutputStreamWriter(socket.getOutputStream())),
													true);
											out.println(stringToSend); 
											Log.d("Client", "Client sent message");

										} catch (UnknownHostException e) {
											e.printStackTrace();
										} catch (IOException e) {
											e.printStackTrace();
										} catch (Exception e) {
											e.printStackTrace();
										}			
									}
								}).start();
								// Ticket sent for inspection successfully
								Toast.makeText(getApplicationContext(), "Ticket: " + lastTicketUsed.getIdticket() + " sent for inspection" , Toast.LENGTH_LONG).show();
							}
						}
					});
				}
			}).start();
		};
	}
}