package com.feup.cmov.busphone_passenger;

import Entities.Passenger;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class SignUpActivity extends Activity implements OnClickListener {

	private EditText usernameText, passwordText, confPassText, fullNameText,
			creditCardNumText, creditCardValText;
	private Spinner creditCardTypeSpinner;
	private Button createAccountButton;
	private static boolean userAdded = false;
	
	private String selectedCreditCardType = "";
	private String[] creditCardTypes;
	private Bundle bundle;
	private Intent newIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);

		// loading view objects
		usernameText = (EditText) findViewById(R.id.newUsernameEditText);
		passwordText = (EditText) findViewById(R.id.newPasswordEditText);
		confPassText = (EditText) findViewById(R.id.confirmPasswordEditText);
		fullNameText = (EditText) findViewById(R.id.newRealnameEditText);
		creditCardNumText = (EditText) findViewById(R.id.newCreditCardNumberEditText);
		creditCardTypeSpinner = (Spinner) findViewById(R.id.newCreditCardTypeSpinner);
		//TODO: Implement a DatePicker for the selection of the validity of the credit card
		//For now
		creditCardValText = (EditText) findViewById(R.id.newCreditCardValidityEditText);
		createAccountButton = (Button) findViewById(R.id.signUpCreateAccountButton);
		
		creditCardTypes = new String [] {"", "VISA", "MasterCard", "American Express", "Maestro"};
		// setting the listeners 
		createAccountButton.setOnClickListener(this);
		
		creditCardTypeSpinner.setOnItemSelectedListener(new MyOnItemSelectedListener());

		bundle = new Bundle();
		newIntent = new Intent(this.getApplicationContext(),
				LoginActivity.class);
	}

	@Override
	public void onClick(View v) {
		class CreateAccount implements Runnable {
			private Passenger passenger;

			public CreateAccount(Passenger p) {
				this.passenger = p;
			}

			@Override
			public void run() {
				final boolean useradded = RestAPI.addUser(passenger);
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						userAdded = useradded;
						if (userAdded) {
							// User account created successfully
							Toast.makeText(getApplicationContext(),
									"User account successfully created!",
									Toast.LENGTH_LONG).show();
							// bundle.putSerializable("key", bus);
							newIntent.putExtras(bundle);
							startActivity(newIntent);
						} else {
							// Unable to create user account
							Toast.makeText(getApplicationContext(),
									"Unable to create user account!",
									Toast.LENGTH_LONG).show();
						}
					}
				});
			}
		}
		if (v.getId() == R.id.signUpCreateAccountButton) {
			if (passwordText.getText().toString().equals(confPassText.getText().toString())) {
				String username = usernameText.getText().toString(), 
						password = passwordText.getText().toString(), 
						fullname = fullNameText.getText().toString(), 
						creditCardVal = creditCardValText.getText().toString();
				Long creditCardNumber = Long.parseLong(creditCardNumText
						.getText().toString());
				String creditCardType = selectedCreditCardType;
				Passenger p = new Passenger(username, password, fullname, creditCardType, creditCardNumber, creditCardVal);
				new Thread(new CreateAccount(p)).start();
			}
		}
	}
	
	private class MyOnItemSelectedListener implements OnItemSelectedListener{
		@Override
		public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
			if(parent.getId() == creditCardTypeSpinner.getId()){
				selectedCreditCardType = creditCardTypes[pos];
			}
		}
		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}
}
