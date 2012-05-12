package org.alarm;

import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.List;
import java.util.ListIterator;

import org.json.JSONException;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import org.util.*;;

public class AlarmClockActivity extends Activity  {
	private List <Item> items =null;
	public static final String[] days = {"Воскресенье","Понедельник","Вторник","Среда","Четверг","Пятница","Суббота"};
	private final int UPDATE_HOUR = 0;
	private final int UPDATE_MINUTE = 1;

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
        showMainActivity(null);
        }
    OnClickListener ocl = new OnClickListener() {

    	public void onClick(View v) {
    	if (true) {
        mToastRunnable = new Runnable() {
			public void run() {
				setAlarm(7, 15);
			}
		};
		mShowToastHandler.postDelayed(mToastRunnable, NOTIFICATION_DELAY_TIME);
		}
    	}
    };
	private OnClickListener myClickListener = new OnClickListener() {
	    public void onClick(View v) {
	    	final TextView mtv = (TextView) v;

	    	try {
			items = RequestUtils.getScedule(mtv.getText().toString(), "неч");
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
	    	setContentView(R.layout.shedule);
	    	TextView scheduletv = (TextView)findViewById(R.id.scheduleText);
	    	scheduletv.setText("");
	    	ListIterator<Item> iter = items.listIterator();
	    	Item temp;
	    	while(iter.hasNext()){
	    		temp = iter.next();
	    		scheduletv.append(temp.time + " " + temp.discipline + "\n");
	    	}
	    	Button btn  = (Button)findViewById(R.id.button1);
	    	btn.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
		    		showMainActivity(null);
				}
			});
	    }
	};

	private void showMainActivity(View v) {
		setContentView(R.layout.main);
		LinearLayout ll = (LinearLayout) findViewById(R.id.linLayoutPar);

		for (int i = 1; i <7; i++){
			TextView tvDay = new TextView(this);
			tvDay.setText(days[i]);
			TextView tv2 = new TextView(this);
			try {
				tv2.setText(RequestUtils.getRings(days[i],"неч")[0]);
			} catch (JSONException e) {
				Log.e("JSONException", e.getMessage());
			} catch (URISyntaxException e) {
				Log.e("URISyntaxException", e.getMessage());
			}
			ll.addView(tvDay);
			tvDay.setClickable(true);
			tvDay.setOnClickListener(myClickListener);
			ll.addView(tv2);
		}
        button = new Button(this);//(Button) findViewById(R.id.start);
        button.setText("Поставить");
        button.setOnClickListener(ocl);
        ll.addView(button);
	}

	private void updateAlarm(){
		 	AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
		 Intent intent = new Intent(this, ScheduleReceiver.class);
		 PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0,
		 intent, PendingIntent.FLAG_CANCEL_CURRENT );

		 am.cancel(pendingIntent);

		 Calendar calendar = Calendar.getInstance();
		 calendar.set(Calendar.HOUR_OF_DAY, UPDATE_HOUR);
		 calendar.set(Calendar.MINUTE, UPDATE_MINUTE);
		 calendar.add(Calendar.DAY_OF_YEAR, 1);

		 am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY,pendingIntent);
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