package com.feup.cmov.busphone_inspector;

import java.lang.reflect.Field;
import java.util.ArrayList;

import Entities.Ticket;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
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
	
	private int toastDuration = Toast.LENGTH_LONG;
	
	private static Boolean isValidTicket = false;
	
	//used to make an RPC invocation
	private Messenger messenger = null; 
	private boolean isBound = false;
	//receives callbacks from bind and unbind invocations
	private ServiceConnection connection;
	//invocation replies are processed by this Messenger
	private Messenger replyTo = null;
	
	

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//setting up the connection for the message exchange with the passenger application
		this.connection = new RemoteServiceConnection();
		this.replyTo = new Messenger(new IncomingHandler());
		
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
        			//TODO: receive from socket the ticket id from passenger application
        			//For now
        			//selectedTicket = new Ticket();
        			//selectedTicket.setIdticket("12345");
        			//validateButton.setEnabled(true);
        			//selectedTicketLabel.setText("Selected: " + selectedTicket.toStringShortVersion());
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
									Toast toast = Toast.makeText(getApplicationContext(), "Valid Ticket!!", toastDuration);
									toast.show();
									newbundle.putSerializable("key", ticketList);
									newIntent.putExtras(newbundle);
									startActivity(newIntent);
								}else{
									Toast toast = Toast.makeText(getApplicationContext(), "Invalid Ticket!!", toastDuration);
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
	
	@Override
	protected void onStart()
	{
		super.onStart();
		
		//Bind to the remote service
		Intent intent = new Intent();
		intent.setClassName("org.openmobster.remote.service.android.app", "org.openmobster.app.RemoteService");
		
		this.bindService(intent, this.connection, Context.BIND_AUTO_CREATE);
	}
	
	@Override
	protected void onStop() 
	{
		super.onStop();
		//Unbind if it is bound to the service
		if(this.isBound)
		{
			this.unbindService(connection);
			this.isBound = false;
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void onResume()
	{
		try
		{
			super.onResume();
			
			//render the main screen
			String layoutClass = this.getPackageName()+".R$layout";
			String main = "main";
			Class clazz = Class.forName(layoutClass);
			Field field = clazz.getField(main);
			int screenId = field.getInt(clazz);
			this.setContentView(screenId);
			
			//Invoke Remote button
			Button invokeButton = (Button) findViewById(R.id.ticket_selection_receive_button);
			invokeButton.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View button) 
				{
					if(SelectTicketActivity.this.isBound)
					{
						Log.i("Debug", "clica aqui");
						//Setup the message for invocation
						Message message = Message.obtain(null, 1, 0, 0);
						try
						{
							//Set the ReplyTo Messenger for processing the invocation response
							message.replyTo = SelectTicketActivity.this.replyTo;
							
							//Make the invocation
							SelectTicketActivity.this.messenger.send(message);
						}
						catch(RemoteException rme)
						{
							//Show an Error Message
							Toast.makeText(SelectTicketActivity.this, "Invocation Failed!!", Toast.LENGTH_LONG).show();
						}
					}
					else
					{
						Toast.makeText(SelectTicketActivity.this, "Service is Not Bound!!", Toast.LENGTH_LONG).show();
					}
				}
			  }
			);
		}
		catch(Exception e)
		{
			e.printStackTrace(System.out);
		}
	}
	private class RemoteServiceConnection implements ServiceConnection{
		@Override
		public void onServiceConnected(ComponentName component, IBinder binder) 
		{	
			SelectTicketActivity.this.messenger = new Messenger(binder);
			
			SelectTicketActivity.this.isBound = true;
		}

		@Override
		public void onServiceDisconnected(ComponentName component) 
		{	
			SelectTicketActivity.this.messenger = null;
			
			SelectTicketActivity.this.isBound = false;
		}
	}
	
	private class IncomingHandler extends Handler
	{
		@Override
        public void handleMessage(Message msg) 
		{
			System.out.println("*****************************************");
			System.out.println("Return successfully received!!!!!!");
			System.out.println("*****************************************");
			
			int what = msg.what;
			Toast.makeText(SelectTicketActivity.this.getApplicationContext(), "Remote Service replied-("+what+")", Toast.LENGTH_LONG).show();
        }
	}
}