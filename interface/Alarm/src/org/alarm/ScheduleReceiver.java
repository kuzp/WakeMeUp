package org.alarm;

import java.text.BreakIterator;
import java.util.Calendar;

import org.util.RequestUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ScheduleReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Calendar cal = Calendar.getInstance();
		String day = AlarmClockActivity.days[cal.get(Calendar.DAY_OF_WEEK)];
		if (day.equals("Понедельник")){
			return;
		}
		Intent i = new Intent(context, AlarmClockActivity.class);
		try {
			String temp = RequestUtils.getRings(day)[0];

		} catch (Exception e) {
			//i.putExtra(ignore, true)
		}
	}

}
