package com.example.pp;

import java.io.IOException;
import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class AlarmReceiver extends Activity
{
	private MediaPlayer mMediaPlayer;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.alarm);
		
		Button stopAlarm = (Button)findViewById(R.id.stopAlarm);
		
		//tryckr p� knappen ska alarmet sluta
		stopAlarm.setOnTouchListener(new OnTouchListener()
		{
			public boolean onTouch(View arg0, MotionEvent arg1)
			{
				mMediaPlayer.stop();
				System.out.println("Stoppat Alarmet");
				finish();
				return false;
			}
		}
		);
		
		playSound(this, getAlarmUri());
	}
	
	private void playSound(Context context, Uri alert)
	{
		mMediaPlayer = new MediaPlayer();
		
		try
		{
			mMediaPlayer.setDataSource(context, alert);
			final AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
			
			//spelar om volymen inte �r noll
			if(audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0)
			{
				mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
				mMediaPlayer.prepare();
				mMediaPlayer.start();
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
