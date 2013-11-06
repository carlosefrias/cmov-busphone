package com.feup.cmov.busphone_inspector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import Entities.Ticket;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

public class Inspection_Activity extends Activity {
	
	private TextView textView;
	private ArrayList<Ticket> ticketList;
	private Bundle newbundle;
	private Intent newIntent;
	
	private ServerSocket serverSocket = null;
	private String messageFromPassenger = "";
	
	protected static final int MSG_ID = 0x1337;
	private static final int SERVERPORT = 6000;
	
	private static boolean wasInspected = false;
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inspection_);
		
		// loading the extras from previous activity
		Bundle bundle = this.getIntent().getExtras();
		ticketList = (ArrayList<Ticket>) bundle.getSerializable("key");
		
		textView = (TextView) findViewById(R.id.inspect_activity_text_view);
		textView.setText("Waiting for passenger ticket");
		
		newbundle = new Bundle();
		newbundle.putSerializable("key", ticketList);
		newIntent = new Intent(this.getApplicationContext(), SelectTicketActivity.class);
		newIntent.putExtras(newbundle);
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				Socket s = null;
				try {
					serverSocket = new ServerSocket(SERVERPORT);
				} catch (IOException e) {
					e.printStackTrace();
				}
				while (!Thread.currentThread().isInterrupted()) {
					Message m = new Message();
					m.what = MSG_ID;
					try {
						if (s == null)
							s = serverSocket.accept();
						BufferedReader input = new BufferedReader(
								new InputStreamReader(s.getInputStream()));
						String st = null;
						st = input.readLine();
						messageFromPassenger = st;
						myUpdateHandler.sendMessage(m);
						s = null;
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.inspection_, menu);
		return true;
	}


	@SuppressLint("HandlerLeak")
	Handler myUpdateHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_ID:
				TextView tv = (TextView) findViewById(R.id.inspect_activity_text_view);
				
				  String[] info;
				  String delimiter = " ";
				  info = messageFromPassenger.split(delimiter);
				  if(info[1].equals("inspect")){
					  tv.setText(info[0]);
					  new Thread(new InspectTicket(info[0], ticketList)).start();
				  }
				break;
			default:
				wasInspected = false;
				break;
			}
			super.handleMessage(msg);
		}
	};
	class InspectTicket implements Runnable{
		private String ticketid;
		private ArrayList<Ticket> list;
		InspectTicket(String tck, ArrayList<Ticket> list){
			this.ticketid = tck;
			this.list = list;
		}
		@Override
		public void run(){
			final Boolean inspected = RestAPI.inspectTicket(ticketid, list);
			runOnUiThread(new Runnable() {				
				@Override
				public void run() {
					wasInspected = inspected;
					if(wasInspected){
						Toast.makeText(getApplicationContext(), "Ticket: " + ticketid + " is a valid ticket!", Toast.LENGTH_LONG).show();
					}else{
						Toast.makeText(getApplicationContext(), "Ticket: " + ticketid + " is an invalid ticket!", Toast.LENGTH_LONG).show();
					}
					textView.setText("Waiting for passenger ticket");
					wasInspected = false;
					startActivity(newIntent);
				}
			});
		}		
	}
}
