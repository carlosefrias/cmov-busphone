package com.feup.cmov.busphone_passenger;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class LoginActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	public void signInAction(View view){
		/**
		 * Implement runnable for sign-in
		 * 
		 * class SignInRunnable implements Runnable
		 */
		
		/**
		 * Implement runnable for getting user's tickets.
		 * 
		 * class ShowTicketsRunnable implements Runnable
		 */
		
		Intent intent = new Intent(this, ShowTicketsActivity.class);
		startActivity(intent);
	}
	
	public void signUpAction(View view){
		/**
		 * Implement runnable for sign-up
		 * 
		 * class SignUpRunnable implements Runnable
		 */
		
		Intent intent = new Intent(this, SignUpActivity.class);
		startActivity(intent);
	}
}
