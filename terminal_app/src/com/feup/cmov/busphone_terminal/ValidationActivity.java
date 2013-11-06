package com.feup.cmov.busphone_terminal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import android.widget.Toast;

public class ValidationActivity extends Activity {
	private ServerSocket serverSocket = null;
	private String messageFromPassenger = "";
	
	protected static final int MSG_ID = 0x1337;
	private static final int SERVERPORT = 6000;
	
	private static boolean wasValidated = false;	
	
	private TextView textView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_validation);

		textView = (TextView) findViewById(R.id.validateMessageLabel);
		textView.setText("Validate your ticket");
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
	protected void onStop() {
		super.onStop();
		try {
			// make sure you close the socket upon exiting
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressLint("HandlerLeak")
	Handler myUpdateHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_ID:
				TextView tv = (TextView) findViewById(R.id.validateMessageLabel);
				
				  String[] info;
				  String delimiter = " ";
				  info = messageFromPassenger.split(delimiter);
				  if(!info[1].equals("inspect")){
					  tv.setText(messageFromPassenger);
					  new Thread(new ValidateTicket(info[0], info[1])).start();
				  }
				break;
			default:
				wasValidated = false;
				break;
			}
			super.handleMessage(msg);
		}
	};
	class ValidateTicket implements Runnable{
		private String ticketid;
		private String username;
		ValidateTicket(String tck, String usermane){
			this.ticketid = tck;
			this.username = usermane;
		}
		@Override
		public void run(){
			final boolean validated = RestAPI.useTheTicket(ticketid, username);
			runOnUiThread(new Runnable() {				
				@Override
				public void run() {
					wasValidated = validated;
					if(wasValidated){
						Toast.makeText(getApplicationContext(), "Ticket: " + ticketid + " validated with success!", Toast.LENGTH_LONG).show();
					}else{
						Toast.makeText(getApplicationContext(), "Unable to validate ticket: " + ticketid, Toast.LENGTH_LONG).show();
					}
					textView.setText("Validate your ticket");					
					wasValidated = false;
				}
			});
		}		
	}
}