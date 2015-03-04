package com.example.pp;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.R.string;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.ClipData.Item;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.widget.Switch;

import java.lang.Object;
import android.view.Menu;
import android.view.inputmethod.EditorInfo;


public class MainActivity extends Activity
{
	private static final String[] INDIAN_STATE = null;
	//private TimePicker time;
	private CheckBox checkbox1, checkbox2;
	private Button btn, cancel, buttonText;
	private Switch switcher;
	private TimePickerDialog timep;
	//private Date d;
	private PendingIntent pendingIntent;
	private TextView text, text2, text3, alarmtext, alarmtext2;
	//private EditText edit;
	private Calendar calSet, calNow;
	private String can = "Du har stängt av alarmet!";
	private Spinner spinner, spinner2;
	private int k = 0;
	private EditText editText;
    private String result;
    
	public final static int CREATE_DIALOG  = -1;
    public final static int THEME_HOLO_LIGHT  = 0;
    public final static int THEME_BLACK  = 1;

    int position;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{	
		super.onCreate(savedInstanceState);
		//this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		btn = (Button)findViewById(R.id.button1);
		buttonText = (Button)findViewById(R.id.button2);
		text = (TextView)findViewById(R.id.textView1);
		text2 = (TextView)findViewById(R.id.textView2);
		editText = (EditText)findViewById(R.id.editText);
		checkbox1 = (CheckBox) findViewById(R.id.checkbox_vibration);
		checkbox2 = (CheckBox) findViewById(R.id.checkbox_sound);
		//spinner = (Spinner)findViewById(R.id.spinner);
		alarmtext = (TextView) findViewById(R.id.alarmText);
		alarmtext2 = (TextView) findViewById(R.id.alarmText2);
		
		//spinner.setOnItemSelectedListener((OnItemSelectedListener) this);
		
		
		final AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
		k = audioManager.getStreamVolume(AudioManager.STREAM_ALARM);
		
		checkbox1.setOnClickListener(new OnClickListener()
		{
            @Override
            public void onClick(View v) {
 
               if(((CheckBox)v).isChecked())
               {
            	   audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
               }
               
               else
               {
            	   audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
               }
              
                
            }
		});
		
		
		
		checkbox2.setOnClickListener(new OnClickListener()
		{
            @Override
            public void onClick(View v) {
 
            	if(((CheckBox)v).isChecked())
                {
            		audioManager.setStreamVolume(AudioManager.STREAM_ALARM, k,
            				AudioManager.FLAG_PLAY_SOUND);
            		System.out.println(audioManager.getStreamVolume(AudioManager.STREAM_ALARM));
             	   	System.out.println("checkt");
                }
                
                else
                {
                	audioManager.setStreamVolume(AudioManager.STREAM_ALARM, 0,
                			AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
                	System.out.println(audioManager.getStreamVolume(AudioManager.STREAM_ALARM));
             	   	System.out.println("inte checkt");
                }
              
             }
		});
		
		buttonText.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				result = editText.getText().toString();
				editText.setText("");
				editText.setHint(R.string.change_msg);
				
				alarmtext2.setText(getResources().getString(R.string.your_msg) + " " + result);
				//System.out.println(text.getText());
			}
		});
		
		btn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				openTimePickerDialog(true);
			}
		});
		
		switcher = (Switch)findViewById(R.id.switch1);
		//switcher.setTextColor(Color.WHITE);
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
					//text.setText("Tiden du har satt: " + calSet.getTime());
					
				}
				else
				{
					cancelAlarm();
					//text.setText(can);
				}
			}
		});
		
	}
	
	private void openTimePickerDialog(boolean is24r)
	{
		Calendar cal = Calendar.getInstance();
		
		timep = new TimePickerDialog(MainActivity.this, onTimeSetListener, cal.get(Calendar.HOUR_OF_DAY),
				cal.get(Calendar.MINUTE), is24r);
		
		timep.setTitle(R.string.time_picker);
		timep.show();
	}
	
	OnTimeSetListener onTimeSetListener = new OnTimeSetListener()
	{
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
			
			SimpleDateFormat format = new SimpleDateFormat("HH:mm");
			String format1 = format.format(calSet.getTime());
			btn.setText(R.string.changeAlarm);
			alarmtext.setText(getResources().getString(R.string.alarmText) + " " + format1);
			//text.setText(" " + calSet.getTime());

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