package org.alarm;

import java.util.Calendar;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class AlarmClockActivity extends Activity implements View.OnClickListener {
	private final static String tag = "Alarm";
	private final static long NOTIFICATION_DELAY_TIME = 2 * 1000;
	private Button button;
	private Runnable mToastRunnable = new Runnable() {
		public void run() {
			// Do nothing
		}
	};
	private Handler mShowToastHandler = new Handler();
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        button = (Button) findViewById(R.id.start);
        button.setOnClickListener(this);
        }
    public void onClick(View v) {
    	if (v.getId() == R.id.start) {
        mToastRunnable = new Runnable() {
			public void run() {
				setAlarm(21, 43);
			}
		};
		mShowToastHandler.postDelayed(mToastRunnable, NOTIFICATION_DELAY_TIME);
		}
    }
    public void onProceedSetTime() {
		mShowToastHandler.removeCallbacks(mToastRunnable);
	}
    private void setAlarm(int hour, int minute) {

		// Create the Calendar instance for the current system time
		long now = System.currentTimeMillis();
		Calendar current = Calendar.getInstance();
		current.setTimeInMillis(now);

		// Create the Calendar instance for the alarm clock time
		// for the current date
		Calendar alarm = (Calendar) current.clone();  
		alarm.set(Calendar.HOUR_OF_DAY, hour);
		alarm.set(Calendar.MINUTE, minute);
		alarm.set(Calendar.SECOND, 0);

		// If the alarm clock time value in millis is less than 
		// the current time in the same format - then add to the alarm
		// Calendar instance one day
		if (current.getTimeInMillis() > alarm.getTimeInMillis()) {
			alarm.add(Calendar.DAY_OF_MONTH, 1);
		}

		// Calculate time period and when it passes the alarm clock will go off
		long delay = alarm.getTimeInMillis() - current.getTimeInMillis();
		final int hours = (int) ((delay / (1000 * 60 * 60)) % 24);
		final int minutes = (int) ((delay / (1000 * 60)) % 60);

		Log.d(tag, "Alarm is set on "+alarm.get(Calendar.DAY_OF_MONTH)+" at "+
				alarm.get(Calendar.HOUR)+":"+alarm.get(Calendar.MINUTE));

		// And of course don't forget to stop working alarm clock
		stopRepeating();
		// before you start up the new alarm
		startRepeating(alarm);

		// Inform the user when he has to wake up
		mShowToastHandler.removeCallbacks(mToastRunnable);
		mToastRunnable = new Runnable() {
			public void run() {
				Toast.makeText(AlarmClockActivity.this, "Alarm will go off in "
						+hours+" hours and "+minutes+" minutes.",
						Toast.LENGTH_LONG).show();
					
			}
		};
		mShowToastHandler.postDelayed(mToastRunnable, NOTIFICATION_DELAY_TIME);
	}
    
    private void startRepeating(Calendar alarmTime) {
		Log.d(tag, "Start repeating");
		// When the alarm goes off, we want to broadcast an Intent to our
		// BroadcastReceiver.  Here we make an Intent with an explicit class
		// name to have our own receiver (which has been published in
		// AndroidManifest.xml) instantiated and called, and then create an
		// IntentSender to have the intent executed as a broadcast.
		// Note that unlike above, this IntentSender is configured to
		// allow itself to be sent multiple times.
		Intent intent = new Intent(this, AlarmReceiver.class);
		PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent, 0);

		// Schedule the alarm!
		AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
		am.setRepeating(AlarmManager.RTC_WAKEUP, alarmTime.getTimeInMillis(),
				(24 * 60 * 60 * 1000), sender);
	}

    private void stopRepeating() {
		Log.d(tag, "Stop repeating");
		// Create the same intent, and thus a matching IntentSender, for
		// the one that was scheduled
		Intent intent = new Intent(this, AlarmReceiver.class);
		PendingIntent sender = PendingIntent.getBroadcast(this,
				0, intent, 0);

		// And cancel the alarm
		AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
		am.cancel(sender);
	}
}