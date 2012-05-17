package org.alarm;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AdvancedScheduleActivity extends Activity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.advanced_schedule);
		LinearLayout ll = (LinearLayout) findViewById(R.id.as_container);
		TextView tv = new TextView(this);
		Bundle extras = getIntent().getExtras();
		String[] item = (String[]) extras.get("item");
		tv.append("Начало: " + item[0] + "\n");
		tv.append("Место: " + item[1] + "\n");
		tv.append("Предмет: " + item[2] + "\n");
		tv.append("Тип: " + item[3] + "\n");
		tv.append("Преподаватель: " + item[4]);
		tv.setTextColor(Color.BLACK);
		ll.addView(tv);
	}
}
