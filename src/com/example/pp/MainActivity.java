package com.example.pp;


import java.util.Calendar;



import android.media.AudioManager;
import android.os.Bundle;
import android.os.Vibrator;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;

import android.content.Context;
import android.content.Intent;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import android.app.TimePickerDialog.OnTimeSetListener;
import android.widget.Switch;




public class MainActivity extends Activity
{
	//private TimePicker time;
	private Button btn, cancel;
	private Switch switcher, sound;
	private TimePickerDialog timep;
	//private Date d;
	private PendingIntent pendingIntent;
	private TextView text;
	private Calendar calSet, calNow;
	private String can = "Du har stängt av alarmet!";
	private int k = 0;
	private CheckBox vib;
	private Vibrator v1;
	private Toast toast;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		btn = (Button)findViewById(R.id.button1);
		text = (TextView)findViewById(R.id.textView1);
		
		v1 = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
		
		
		final AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
		k = audioManager.getStreamVolume(AudioManager.STREAM_ALARM);
		
		
		btn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				openTimePickerDialog(true);
			}
		});
		
		switcher = (Switch)findViewById(R.id.switch1);
		switcher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked)
				{
					calNow = Calendar.getInstance();
					if(calSet.compareTo(calNow) <= 0)
					{
						calSet.add(Calendar.DATE,1);
					}
					System.out.println("" + calNow.getTime());
					setAlarm(calSet);
					text.setText("Tiden du har satt: " + calSet.getTime());
				}
				else
				{
					cancelAlarm();
					text.setText(can);
				}
			}
		});
		
		sound = (Switch)findViewById(R.id.switch2);
		sound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked)
				{
					//audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
					audioManager.setStreamVolume(AudioManager.STREAM_ALARM, k, AudioManager.FLAG_PLAY_SOUND);
					//System.out.println(audioManager.getStreamVolume(AudioManager.STREAM_ALARM));
					String message = getString(R.string.volume) + audioManager.getStreamVolume(AudioManager.STREAM_ALARM);
					//toast = Toast.makeText(getApplicationContext(), "Volume: " + audioManager.getStreamVolume(AudioManager.STREAM_ALARM), Toast.LENGTH_SHORT);
					toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
					toast.show();
					System.out.println("ljud på " + audioManager.getRingerMode());
				}
				else
				{
					//audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
					audioManager.setStreamVolume(AudioManager.STREAM_ALARM, 0, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
					//System.out.println(audioManager.getStreamVolume(AudioManager.STREAM_ALARM));
					System.out.println("ljud " + audioManager.getRingerMode());
				}
				
			}
		});
		
		
		cancel = (Button)findViewById(R.id.button2);
		cancel.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				cancelAlarm();
				//text = (TextView)findViewById(R.id.textView1);
				//text.setText(can);
			}
		});
		
		vib = (CheckBox)findViewById(R.id.checkBox1);
		vib.setOnClickListener(new OnClickListener()
		{
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View v) 
			{
				if(((CheckBox)v).isChecked())
				{
					audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
					//audioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION, AudioManager.VIBRATE_SETTING_ON);
					//System.out.println("på" + audioManager.getVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION));
					//System.out.println("Volym " + audioManager.getStreamVolume(AudioManager.STREAM_ALARM));
					System.out.println("på " + audioManager.getRingerMode());
					toast = Toast.makeText(getApplicationContext(), R.string.vibration_on, Toast.LENGTH_SHORT);
					toast.show();
				}
				else
				{
					audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
					//audioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION, AudioManager.VIBRATE_SETTING_OFF);
					//System.out.println("av" + audioManager.getVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION));
					//System.out.println("Volym " + audioManager.getStreamVolume(AudioManager.STREAM_ALARM));
					System.out.println("av " + audioManager.getRingerMode());
					toast = Toast.makeText(getApplicationContext(), R.string.vibration_off, Toast.LENGTH_SHORT);
					toast.show();
				}
			}
			
		});
		
	}
	
	private void openTimePickerDialog(boolean is24r)
	{
		Calendar cal = Calendar.getInstance();
		
		timep = new TimePickerDialog(MainActivity.this, onTimeSetListener, cal.get(Calendar.HOUR_OF_DAY),
				cal.get(Calendar.MINUTE), is24r);
		
		timep.setTitle("Set Alarm Title");
		timep.show();
	}
	
	OnTimeSetListener onTimeSetListener = new OnTimeSetListener()
	{
		@SuppressWarnings("deprecation")
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute)
		{
			calNow = Calendar.getInstance();
			calSet = (Calendar)calNow.clone();
			
			calSet.set(Calendar.HOUR_OF_DAY, hourOfDay);
			calSet.set(Calendar.MINUTE, minute);
			calSet.set(Calendar.SECOND, 0);
			calSet.set(Calendar.MILLISECOND, 0);
			
			if(calSet.compareTo(calNow) <= 0)
			{
				calSet.add(Calendar.DATE,1);
			}
			setAlarm(calSet);
			switcher.setChecked(true);
			vib.setChecked(true);
			sound.setChecked(true);
			text.setText("Tiden du har satt: " + calSet.getTime());
			btn.setText("Time: " + calSet.get(Calendar.HOUR_OF_DAY) + ":" + calSet.get(Calendar.MINUTE));
		}
	};
	
	private void setAlarm(Calendar targetCal)
	{
		Intent intent = new Intent(this, AlarmReceiver1.class);
		pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
		
		AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
		am.setRepeating(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
	}
	
	private void cancelAlarm()
	{
		Intent intent = new Intent(this, AlarmReceiver1.class);
		pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
		
		AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
		am.cancel(pendingIntent);
	}

}