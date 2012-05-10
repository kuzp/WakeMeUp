package org.alarm;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;

import android.util.Log;

/** This class is for receiving event when time for sleep is over
 * and alarm should go off */

public class AlarmReceiver extends BroadcastReceiver {
	private final static String tag = "AlarmReceiver";
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(tag, "ALARM GOES OFF!");	

		// Capture wake-lock in order to keep phone waking when alarm will go off
		PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
		PowerManager.WakeLock wl =
			pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK |
					PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE,
					tag);
		// Assume Activity will be started in 5 seconds
		wl.acquire(5000);

		// Start the activity where you can stop alarm
		Intent i = new Intent(context, AlarmActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(i);
	}
}
