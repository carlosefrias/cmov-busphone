package com.feup.cmov.busphone_terminal;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnClickListener {
	private EditText usernameText, passwordText;
	private Button loginButton;
	private static boolean logedIn = false;
	private Bundle bundle;
	private Intent newIntent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		//loading the view objects
		usernameText = (EditText) findViewById(R.id.loginUsernameEditText);
		passwordText = (EditText) findViewById(R.id.loginPasswordEditText);
		loginButton = (Button) findViewById(R.id.loginSignInButton);
		loginButton.setOnClickListener(this);
		bundle = new Bundle();
		newIntent = new Intent(this.getApplicationContext(), ValidationActivity.class);
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
				Log.i("DEBUG", user + " " + pass);
				final boolean isValidLogin = RestAPI.validateLogin(user, pass);
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						logedIn = isValidLogin;
						if(logedIn){
							//Login successful
							Toast.makeText(getApplicationContext(), "Login successful!", Toast.LENGTH_LONG).show();
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
	}

}