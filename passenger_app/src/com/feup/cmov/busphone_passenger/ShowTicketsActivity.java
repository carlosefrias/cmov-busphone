package com.feup.cmov.busphone_passenger;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class ShowTicketsActivity extends Activity {
	
	private static final int NEW_TICKET = 10;
	private static final int SINGLE_TICKET = 20;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_itemoption_show_tickets);
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
						/**
						 * Implement runnable for posting a new ticket.
						 * 
						 * class NewTicketRunnable implements Runnable
						 */
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
		showDialog(SINGLE_TICKET);
	}
}
