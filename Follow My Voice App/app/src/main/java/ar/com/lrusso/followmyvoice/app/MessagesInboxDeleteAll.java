package ar.com.lrusso.followmyvoice.app;

import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;
import android.app.Activity;
import android.database.Cursor;

public class MessagesInboxDeleteAll extends Activity
	{
	private TextView delete;
	private TextView goback;
	
    @Override protected void onCreate(Bundle savedInstanceState)
    	{
    	super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
    	setContentView(R.layout.messagesinboxdeleteall);
		GlobalVars.lastActivity = MessagesInboxDeleteAll.class;
		GlobalVars.lastActivityArduino = this;
		delete = (TextView) findViewById(R.id.messagedelete);
		goback = (TextView) findViewById(R.id.goback);
		GlobalVars.messagesInboxWereDeleted = false;
		GlobalVars.activityItemLocation=0;
		GlobalVars.activityItemLimit=2;
    	}
		
	@Override public void onResume()
		{
		super.onResume();
		try{
            GlobalVars.alarmVibrator.cancel();}catch(NullPointerException e){}catch(Exception e){}
		GlobalVars.lastActivity = MessagesInboxDeleteAll.class;
		GlobalVars.lastActivityArduino = this;
		GlobalVars.activityItemLocation=0;
		GlobalVars.activityItemLimit=2;
		GlobalVars.selectTextView(delete,false);
		GlobalVars.selectTextView(goback,false);
		GlobalVars.talk(getResources().getString(R.string.layoutMessagesInboxDeleteAllOnResume));
		}
	
	@Override public String toString()
		{
		int result = GlobalVars.detectArduinoKeyUp();
		switch (result)
			{
			case GlobalVars.ACTION_SELECT:
			select();
			break;

			case GlobalVars.ACTION_EXECUTE:
			execute();
			break;
			}
		return null;
		}

	public void select()
		{
		switch (GlobalVars.activityItemLocation)
			{
			case 1: //CONFIRM DELETE
			GlobalVars.selectTextView(delete, true);
			GlobalVars.selectTextView(goback,false);
			GlobalVars.talk(getResources().getString(R.string.layoutMessagesInboxDeleteAllDelete));
			break;

			case 2: //GO BACK TO THE PREVIOUS MENU
			GlobalVars.selectTextView(goback,true);
			GlobalVars.selectTextView(delete,false);
			GlobalVars.talk(getResources().getString(R.string.backToPreviousMenu));
			break;
			}
		}
		
	public void execute()
		{
		switch (GlobalVars.activityItemLocation)
			{
			case 1: //CONFIRM DELETE
			deleteAllReceivedMessages();
			GlobalVars.messagesInboxWereDeleted = true;
			this.finish();
			break;

			case 2: //GO BACK TO THE PREVIOUS MENU
			this.finish();
			break;
			}
		}

	private void deleteAllReceivedMessages()
		{
		try
			{
			Uri inboxUri = Uri.parse("content://sms/inbox");
			Cursor c = getContentResolver().query(inboxUri , null, null, null, null);
			while (c.moveToNext())
				{
			    try
			    	{
			        // Delete the SMS
			        String pid = c.getString(0); // Get id;
			        String uri = "content://sms/" + pid;
			        getContentResolver().delete(Uri.parse(uri), null, null);
			    	}
			    	catch (Exception e)
			    	{
			    	}
				}
		    c.close();
			}
			catch(NullPointerException e)
			{
			}
			catch(Exception e)
			{
			}
		}
	}