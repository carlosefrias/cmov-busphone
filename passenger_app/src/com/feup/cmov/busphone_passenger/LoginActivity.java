package com.feup.cmov.busphone_passenger;

import java.util.ArrayList;

import com.feup.cmov.busphone_passenger.RestAPI;

import Entities.Ticket;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnClickListener {
	private EditText usernameText, passwordText;
	private Button loginButton, signUpButton;
	private static boolean logedIn = false;
	private static ArrayList<Ticket> listUnusedTickets;
	private Bundle bundle;
	private Intent newIntent, signUpIntent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		//loading the view objects
		usernameText = (EditText) findViewById(R.id.loginUsernameEditText);
		passwordText = (EditText) findViewById(R.id.loginPasswordEditText);
		loginButton = (Button) findViewById(R.id.loginSignInButton);
		signUpButton = (Button) findViewById(R.id.loginSignUpButton);
		loginButton.setOnClickListener(this);
		signUpButton.setOnClickListener(this);
		bundle = new Bundle();
		newIntent = new Intent(this.getApplicationContext(), ShowListOfTicketsActivity.class);
		signUpIntent = new Intent(this.getApplicationContext(), SignUpActivity.class);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	public void onClick(View v) {
		class LoginRunnable implements Runnable{
			String user, pass;
			public LoginRunnable(String u, String p){
				this.user = u;
				this.pass = p;
			}
			@Override
			public void run() {
				final boolean isValidLogin = RestAPI.validateLogin(user, pass);
				final ArrayList<Ticket> unusedTicketList = RestAPI.getPassengerUnusedTickets(usernameText.getText().toString());
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						logedIn = isValidLogin;
						listUnusedTickets = unusedTicketList;
						if(logedIn){
							//Login successful
							Toast.makeText(getApplicationContext(), "Login successful!", Toast.LENGTH_LONG).show();
							bundle.putSerializable("username", usernameText.getText().toString());
							bundle.putSerializable("listUnusedTickets", listUnusedTickets);
							newIntent.putExtras(bundle);
							startActivity(newIntent);
							
						}else{
							//login failed
							Toast.makeText(getApplicationContext(), "Login failed!", Toast.LENGTH_LONG).show();
						}
					}
				});
			}
			
		}
		if (v.getId() == R.id.loginSignInButton) {
			String username = usernameText.getText().toString();
			String password = passwordText.getText().toString();
			new Thread(new LoginRunnable(username, password)).start();
		}
		else if(v.getId() == R.id.loginSignUpButton){
			signUpIntent.putExtras(bundle);
			startActivity(signUpIntent);			
		}
	}

}
