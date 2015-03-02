package com.example.pp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class Snooze extends BroadcastReceiver
{
	@Override
	public void onReceive(Context arg0, Intent arg1)
	{
		Toast.makeText(arg0, "Snooooze!!!", Toast.LENGTH_LONG).show();
		
		Intent i = new Intent("android.intent.action.MAIN");
		i.setClass(arg0, PopUp.class);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
		i.putExtra("AlarmID", i.getIntExtra("AlarmID", -1));
		
		arg0.startActivity(i);
		
	}
}
