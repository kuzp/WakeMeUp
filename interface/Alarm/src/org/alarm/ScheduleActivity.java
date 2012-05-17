package org.alarm;

import java.net.URISyntaxException;
import java.util.List;
import java.util.ListIterator;

import org.json.JSONException;
import org.util.Item;
import org.util.RequestUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class ScheduleActivity extends Activity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		List <Item> items = null;
		final Intent i = new Intent(this, AlarmClockActivity.class);
		Bundle extras = getIntent().getExtras();
    	try {
    		items = RequestUtils.getScedule(extras.get("items").toString());
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
    	
    	setContentView(R.layout.shedule);
    	final ScrollView sv = (ScrollView) findViewById(R.id.scrollView1);
    	final LinearLayout ll = new LinearLayout(this);
    	ll.setOrientation(1);
    	sv.addView(ll);
    	ListIterator<Item> iter = items.listIterator();
    	Item temp;
    	while(iter.hasNext()){
    		Button schedulebt = new Button(this);
    		schedulebt.setTextSize(20);
    		temp = iter.next();
    		schedulebt.setText(temp.time + " " + temp.discipline);
    		ll.addView(schedulebt);
    		final String[] tempAr = new String[] {temp.time, temp.address, temp.discipline, temp.type, temp.tutor};
    		schedulebt.setOnClickListener(new OnClickListener() {
    			public void onClick(View v) {
    				Intent ai = new Intent(ScheduleActivity.this, AdvancedScheduleActivity.class);
    				ai.putExtra("item", tempAr);
    				startActivity(ai);
    			}
    		});
    	}
    	Button btn  = (Button)findViewById(R.id.button1);
    	btn.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
	    		ScheduleActivity.this.startActivity(i);
			}
		});
	}

}
