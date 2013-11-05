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
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class ValidationActivity extends Activity {

	private Bundle bundle;
	private Intent newIntent;
	private ArrayList<Ticket> listOfUnusedTickets;
	private String username;
	private Ticket selectedTicket;


	private Socket socket;
	private String serverIpAddress = "10.0.2.2";
	// AND THAT'S MY DEV'T MACHINE WHERE PACKETS TO
	// PORT 5000 GET REDIRECTED TO THE SERVER EMULATOR'S
	// PORT 6000
	private static final int REDIRECTED_SERVERPORT = 5000;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_itemoption_validation);

		// loading extras from the previous activity
		bundle = this.getIntent().getExtras();
		username = (String) bundle.getSerializable("username");
		listOfUnusedTickets = (ArrayList<Ticket>) bundle
				.getSerializable("listUnusedTickets");
		selectedTicket = (Ticket) bundle.getSerializable("selectedTicket");

		newIntent = new Intent(this.getApplicationContext(), ShowTicketsActivity.class);

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
					String stringToSend = selectedTicket.getIdticket();
					PrintWriter out = new PrintWriter(new BufferedWriter(
							new OutputStreamWriter(socket.getOutputStream())),
							true);
					out.println(stringToSend);
					Log.d("Client", "Client sent message");

				} catch (UnknownHostException e) {
					// tv.setText("Error1");
					e.printStackTrace();
				} catch (IOException e) {
					// tv.setText("Error2");
					e.printStackTrace();
				} catch (Exception e) {
					// tv.setText("Error3");
					e.printStackTrace();
				}
				listOfUnusedTickets.remove(selectedTicket);
				bundle.putSerializable("username", username);
				bundle.putSerializable("listUnusedTickets", listOfUnusedTickets);
				//TODO:Store id of last ticket used
				newIntent.putExtras(bundle);
				startActivity(newIntent);				
			}
		}).start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.item_option_validation, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpTo(this, new Intent(this,
					ShowTicketsActivity.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}