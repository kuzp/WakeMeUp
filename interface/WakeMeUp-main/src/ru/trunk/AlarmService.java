package ru.trunk;

//Need the following import to get access to the app resources, since this
//class is in a sub-package.

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.SystemClock;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;


/**
* This demonstrates how you can schedule an alarm that causes a service to
* be started.  This is useful when you want to schedule alarms that initiate
* long-running operations, such as retrieving recent e-mails.
*/
public class AlarmService extends Activity {
 private PendingIntent mAlarmSender;

 @Override
     protected void onCreate(Bundle savedInstanceState) {
     super.onCreate(savedInstanceState);

     // Create an IntentSender that will launch our service, to be scheduled
     // with the alarm manager.
     mAlarmSender = PendingIntent.getService(AlarmService.this,
             0, new Intent(AlarmService.this, AlarmService_Service.class), 0);

     setContentView(R.layout.alarm_service);

     // Watch for button clicks.
     Button button = (Button)findViewById(R.id.start_alarm);
     button.setOnClickListener(mStartAlarmListener);
     button = (Button)findViewById(R.id.stop_alarm);
     button.setOnClickListener(mStopAlarmListener);
 }

 private OnClickListener mStartAlarmListener = new OnClickListener() {
     public void onClick(View v) {
         // We want the alarm to go off 30 seconds from now.
         long firstTime = SystemClock.elapsedRealtime();

         // Schedule the alarm!
         AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
         am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                         firstTime, 30*1000, mAlarmSender);

         // Tell the user about what we did.
         Toast.makeText(AlarmService.this, "repeating_scheduled",
                 Toast.LENGTH_LONG).show();
     }
 };

 private OnClickListener mStopAlarmListener = new OnClickListener() {
     public void onClick(View v) {
         // And cancel the alarm.
         AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
         am.cancel(mAlarmSender);

         // Tell the user about what we did.
         Toast.makeText(AlarmService.this, "repeating_unscheduled",
                 Toast.LENGTH_LONG).show();

     }
 };
}