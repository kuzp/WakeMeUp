package org.alarm;

import java.text.BreakIterator;
import java.util.Calendar;

import org.util.RequestUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.sax.StartElementListener;

public class ScheduleReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Calendar cal = Calendar.getInstance();
		String day = AlarmClockActivity.days[cal.get(Calendar.DAY_OF_WEEK)];
//		if (day.equals("Понедельник")){
//			return;
//		}
		Intent i = new Intent(context, AlarmClockActivity.class);
		try {
			String temp = RequestUtils.getRings(day)[0];
			int hour = RequestUtils.getHour(temp);
			int minute = RequestUtils.getMinute (temp);
			i.putExtra("hour", hour);
			i.putExtra("minute",minute);
			context.startActivity(i);
		} catch (Exception e) {
			context.startActivity(new Intent(context,ExceptionActivity.class));
		}
	}

}
