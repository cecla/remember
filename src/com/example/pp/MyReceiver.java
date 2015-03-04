package com.example.pp;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.Context;

public class MyReceiver extends BroadcastReceiver
{
	@Override
	public void onReceive(Context context, Intent intent)
	{
		System.out.println("Alarm received");
		
		Intent service1 = new Intent(context, MyAlarmService.class);
		context.startService(service1);
	}

}
