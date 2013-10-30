package com.feup.cmov.busphone_terminal;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.widget.Toast;

public class ValidationService extends Service {
	
	private Messenger messenger;
	
	public ValidationService() {
	}

	@Override
	public IBinder onBind(Intent intent) {
		if(this.messenger == null)
		{
			synchronized(ValidationService.class)
			{
				if(this.messenger == null)
				{
					this.messenger = new Messenger(new ValidationHandler());
				}
			}
		}
		//Return the proper IBinder instance
		return this.messenger.getBinder();
	}
	
	@SuppressLint("HandlerLeak")
	private class ValidationHandler extends Handler{
		@Override
        public void handleMessage(Message msg){
			System.out.println("*****************************************");
			System.out.println("Remote Service successfully invoked!!!!!!");
			System.out.println("*****************************************");
			
			int what = msg.what;
			
			Toast.makeText(ValidationService.this.getApplicationContext(), "Remote Service invoked-("+what+")", Toast.LENGTH_LONG).show();
			
			//Setup the reply message
			Message message = Message.obtain(null, 2, 0, 0);
			try
			{
				//make the RPC invocation
				Messenger replyTo = msg.replyTo;
				replyTo.send(message);
			}
			catch(RemoteException rme)
			{
				//Show an Error Message
				Toast.makeText(ValidationService.this, "Invocation Failed!!", Toast.LENGTH_LONG).show();
			}
		}
	}
}
