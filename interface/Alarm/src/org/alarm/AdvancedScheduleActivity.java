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
		tv.append("������: " + item[0] + "\n");
		tv.append("�����: " + item[1] + "\n");
		tv.append("�������: " + item[2] + "\n");
		tv.append("���: " + item[3] + "\n");
		tv.append("�������������: " + item[4]);
		tv.setTextColor(Color.BLACK);
		ll.addView(tv);
	}
}
