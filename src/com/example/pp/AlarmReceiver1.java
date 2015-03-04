package com.example.pp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AlarmReceiver1 extends BroadcastReceiver
{
	@Override
	public void onReceive(Context arg0, Intent arg1)
	{

		Intent i = new Intent("android.intent.action.MAIN");
		i.putExtras(arg1.getExtras());
		i.setClass(arg0, PopUp.class); 
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
		i.putExtra("AlarmID", i.getIntExtra("AlarmID", -1)); arg0.startActivity(i);

		System.out.println(arg1.getExtras().get("msg"));
		/*Intent i = new Intent();
		i.setClass(arg0, MainActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
		arg0.startActivity(i);*/
	}
	
}
