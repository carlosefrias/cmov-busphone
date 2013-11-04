package com.feup.cmov.busphone_passenger;

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
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class ValidationActivity extends Activity {

	private Messenger messenger = null; //used to make an RPC invocation
    private boolean isBound = false;
    private ServiceConnection connection;//receives callbacks from bind and unbind invocations
    private Messenger replyTo = null; //invocation replies are processed by this Messenger
	
    private Bundle bundle;
    private ArrayList<Ticket> listOfUnusedTickets;
    private Ticket selectedTicket;
    private String username;
    
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_itemoption_validation);
		
		// loading extras from the previous activity
		bundle = this.getIntent().getExtras();
		username = (String) bundle.getSerializable("username");
		listOfUnusedTickets = (ArrayList<Ticket>) bundle.getSerializable("listUnusedTickets");
		selectedTicket = (Ticket) bundle.getSerializable("selectedTicket");
		
		// Show the Up button in the action bar.
		//getActionBar().setDisplayHomeAsUpEnabled(true);
		
		this.connection = new ValidationServiceConnection();
        this.replyTo = new Messenger(new IncomingResponseHandler());
        
        if(ValidationActivity.this.isBound)
        {
                //Setup the message for invocation
                Message message = Message.obtain(null, 1, 0, 0);
                try
                {
                        //Set the ReplyTo Messenger for processing the invocation response
                        message.replyTo = ValidationActivity.this.replyTo;
                        
                        //Make the invocation
                        ValidationActivity.this.messenger.send(message);
                }
                catch(RemoteException rme)
                {
                        //Show an Error Message
                        Toast.makeText(ValidationActivity.this, "Invocation Failed!!", Toast.LENGTH_LONG).show();
                }
        }
        else
        {
                Toast.makeText(ValidationActivity.this, "Service is Not Bound!!", Toast.LENGTH_LONG).show();
        }
	}
	
	@Override
	public void onStart(){
		super.onStart();
        
        //Bind to the remote service
        Intent intent = new Intent();
        intent.setClassName("org.openmobster.remote.service.android.app", "org.openmobster.app.RemoteService");
        
        getApplicationContext();
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
			NavUtils.navigateUpTo(this,
					new Intent(this, ShowTicketsActivity.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * Implement runnable for validation (it must be always available to build a transmission channel).
	 * 
	 * class ValidationRunnable implements Runnable
	 */
	
	private class ValidationServiceConnection implements ServiceConnection
    {
            @Override
            public void onServiceConnected(ComponentName component, IBinder binder) 
            {       
            	ValidationActivity.this.messenger = new Messenger(binder);
                    
            	ValidationActivity.this.isBound = true;
            }

            @Override
            public void onServiceDisconnected(ComponentName component) 
            {       
            	ValidationActivity.this.messenger = null;
                    
            	ValidationActivity.this.isBound = false;
            }
    }
    
    @SuppressLint("HandlerLeak")
	private class IncomingResponseHandler extends Handler
    {
            @Override
            public void handleMessage(Message msg) 
            {
                    System.out.println("*****************************************");
                    System.out.println("Return successfully received!!!!!!");
                    System.out.println("*****************************************");
                    
                    int what = msg.what;
                    
                    Toast.makeText(ValidationActivity.this.getApplicationContext(), "Remote Service replied-("+what+")", Toast.LENGTH_LONG).show();
            }
    }
}
