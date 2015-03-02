package com.example.pp;

import java.io.IOException;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;


public class PopUp extends Activity
{
	private int m_alarmId;
	private PendingIntent pIntent;
	private Vibrator v;
	private MediaPlayer mPlayer;
	private long[] pattern = {0,500,1000};
	private AudioManager audioManager ;
	
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		playSound(this, getAlarmUri());
		playVibrate();
		
		System.out.println(audioManager.getRingerMode());
		
		if(extras != null)
		{
			m_alarmId = extras.getInt("AlarmID", -1);
		}
		else
		{
			m_alarmId = -1;
		}
		
		showDialog(0);
		
	}
	
	@SuppressWarnings({ "deprecation", "deprecation", "deprecation" })
	@Override
	protected Dialog onCreateDialog(int id)
	{
		super.onCreateDialog(id);
		
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		
		alert.setTitle("ALARM!!!");
		alert.setMessage("Höhö");
		alert.setCancelable(false);
		
		alert.setPositiveButton("Stäng Av", new DialogInterface.OnClickListener() 
		{	
			@Override
			public void onClick(DialogInterface dialog, int which) {
				v.cancel();
				mPlayer.stop();
				PopUp.this.finish();
			}
		});
		
		alert.setNegativeButton("Snooze", new DialogInterface.OnClickListener() 
		{		
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				//snooze.set(Calendar.MINUTE, 10);
				v.cancel();
				setAlarm();
				mPlayer.stop();
				PopUp.this.finish();
			}
		});
		
		AlertDialog dialog = alert.create();
		return dialog;
	}
	
	private void setAlarm()
	{
		Intent in = new Intent(this, Snooze.class);
		pIntent = PendingIntent.getBroadcast(getBaseContext(), 2, in, 0);
		
		
		
		AlarmManager am1 = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
		am1.set(AlarmManager.RTC_WAKEUP, 60 * 1000 , pIntent);
	}
	
	private void playVibrate()
	{
		//final AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
		v = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
		
		audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
		
		if(audioManager.getRingerMode() == 1)
		{
			v.vibrate(pattern, 0);
		}
	}
	
	//@SuppressWarnings("deprecation")
	private void playSound(Context context, Uri al)
	{
		mPlayer = new MediaPlayer();
		
		
		audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
		//final AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
		try
		{
			mPlayer.setDataSource(context, al);
			
			
			//spelar om volymen inte är noll
			if(audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0)
			{
				mPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
				mPlayer.prepare();
				mPlayer.start();
			}
			
		}
		catch(IOException e)
		{
			System.out.println("Du faila, HAHA!");
		}
	}
	
	private Uri getAlarmUri()
	{
		Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
		if(alert == null)
		{
			alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
			if(alert == null)
			{
				alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
			}
		}
		return alert;
	}
	
}
