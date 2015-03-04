	

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
import android.widget.CompoundButton;
import android.widget.Switch;
     
    public class PopUp extends Activity
    {
            private int m_alarmId;
            private Calendar snooze;
            private PendingIntent pIntent;
            private Vibrator v;
            private MediaPlayer mPlayer;
            private Switch sound;
            private long[] pattern = {0,500,1000};
            private AudioManager audioManager;
           
            //initial setup for the activity
            @Override
            public void onCreate(Bundle savedInstanceState)
            {
                    super.onCreate(savedInstanceState);
                  
            		
                   /* EditText result;
                    result = (EditText)findViewById(R.id.editText);
                    String a = result.getText().toString();
                    
                    AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                    alertDialog.setMessage(a);*/
                    
                    //create a alert dialog with the message retrieved by id form strings.xml
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    Intent the_msg = getIntent();
                    
                    builder.setMessage(R.string.your_message);
                    //builder.setMessage(the_msg.getExtras().getString("msg"));
                    //the stop button. when clicked on, stop vibration, sound and end the activity
                    builder.setPositiveButton(R.string.stop_alarm, new DialogInterface.OnClickListener() 
                    {	
						@Override
						public void onClick(DialogInterface dialog, int which) 
						{	
							v.cancel();
                            mPlayer.stop();
							finish();
						}
					});
                    
                    //the snooze button. when clicked on, (call the set alarm function),
                    //stop vibration, sound and end the activity
                    builder.setNegativeButton(R.string.snooze, new DialogInterface.OnClickListener() 
                    {	
						@Override
						public void onClick(DialogInterface dialog, int which) 
						{
							//snooze.set(Calendar.MINUTE, 10);
                            v.cancel();
                            setAlarm();
                            mPlayer.stop();
                            finish();
							
						}
					});

                    playSound(this, getAlarmUri());
                    playVibrate();
                    
                    //create the alert dialog and show
                    AlertDialog alert = builder.create();
                    alert.show();
                   
            }
        
           
            private void setAlarm()
            {
                    Intent in = new Intent(this, AlarmReceiver1.class);
                    pIntent = PendingIntent.getBroadcast(getBaseContext(), 2, in, 0);
                   
                    AlarmManager am1 = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                    am1.set(AlarmManager.RTC_WAKEUP, (60*1000), pIntent);
            }
            
            private void playVibrate()
            {
            	v = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
            	audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
            	
            	if(audioManager.getRingerMode() == 1)
            	{
            		v.vibrate(pattern, 0);
            	}
            }
           
            private void playSound(Context context, Uri al)
            {
                    mPlayer = new MediaPlayer();
                   
                    try
                    {
                            mPlayer.setDataSource(context, al);
                            audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
                           
                            //spelar om volymen inte Šr noll
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

