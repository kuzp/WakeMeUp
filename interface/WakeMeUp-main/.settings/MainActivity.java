package ru.trunk;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;

import android.os.Bundle;
import android.provider.AlarmClock;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	final String[] days = {"Понедельник","Вторник","Среда","Четверг","Пятница","Суббота","Воскресенье"};
	
	private OnClickListener myClickListener = new OnClickListener() {
	    public void onClick(View v) {
	    	final TextView mtv = (TextView) v;
	    	List <Item> items =null;
	    	Log.i("clickable! ",mtv.getText().toString());
	    	try {
			items = getScedule(mtv.getText().toString(), "неч");
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
	    		scheduletv.append(temp.time + temp.discipline + "\n");
	    	}
	    	
	    }
	};
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//showMainActivity(null);
		setContentView(R.layout.monday_shedule);
		Button but = (Button)findViewById(R.id.button1);
		but.setOnClickListener(ocl);
		
	}


	OnClickListener ocl = new OnClickListener() {
		
		public void onClick(View v) {
			AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
			Intent intent = new Intent(MainActivity.this, BroadcastImpl.class);
	        PendingIntent sender = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);
	        		
	        Calendar calendar = Calendar.getInstance();
	        calendar.setTimeInMillis(System.currentTimeMillis());
	        calendar.add(Calendar.SECOND, 30);
	        
	        am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);			
		}
	};

	public void showMainActivity(View v) {
		setContentView(R.layout.main);
		LinearLayout ll = (LinearLayout) findViewById(R.id.linLayoutPar);
		
		for (int i = 0; i <6; i++){
			TextView tvDay = new TextView(this);
			tvDay.setText(days[i]);
			TextView tv2 = new TextView(this);
			try {
				tv2.setText(getRings(days[i],"неч")[0]);
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
			ll.addView(tvDay);
			tvDay.setClickable(true);
			tvDay.setOnClickListener(myClickListener);
			ll.addView(tv2);
		}
	}


	
	
	private JSONObject getJson(String userID, String method, String action, String day, String parity, String group) throws URISyntaxException, JSONException{
		final String serverURL = "http://178.130.32.166:8075/";
    	final HttpClient httpclient = new DefaultHttpClient();
    	final HttpGet httpget = new HttpGet();
    	JSONObject jsonObj = null;
    	String jsonString = "Oups!";
    	final String uriS = serverURL+ method + "?action=" + action + "&day=" + day + "&parity=" + parity +"&group=" + group + "&userId=" + userID;
    	httpget.setURI(new URI(uriS));
		System.out.println(uriS);
    	try {
    		HttpResponse response = httpclient.execute(httpget);
    		HttpEntity entity = response.getEntity();
    		jsonString = EntityUtils.toString(entity, "UTF-8");
    		Log.i("json:",jsonString);    		
    		jsonObj = new JSONObject(jsonString);

    		jsonObj.remove("errors"); ///    		
    		
    	} catch (ClientProtocolException e) {
    		Log.e("ClientPortExc", e.getMessage());
    	} catch (IOException e) {
    		Log.e("IOExc:",e.getMessage());
    	}

    	return jsonObj;
    }
	private JSONObject getJson(String action, String day, String parity)throws URISyntaxException, JSONException{
		return this.getJson("1","wakeBolet", action, day, parity, "2742");
	}
	
	public String[] getRings(String day, String parity) throws JSONException, URISyntaxException{
		JSONObject json = getJson("rings", day, parity);
		JSONArray jarray = json.getJSONArray("rings");
		int len = jarray.length();
		String[] result= new String[len];
		for (int i = 0; i < len; i++){
			result[i] = jarray.getString(i);
		}
		return result;
	}
	
	public List<Item> getScedule(String day, String parity) throws JSONException, URISyntaxException{
		List<Item> items = new LinkedList<Item>();
		JSONObject json = getJson("schedule", day, parity);
		int len = json.length();
		for (int i = 0; i < len; i++){
			JSONArray jarray = json.getJSONArray(Integer.toString(i));
			items.add(new Item(jarray.getString(1),jarray.getString(2),jarray.getString(3),jarray.getString(4),jarray.getString(5),jarray.getString(6)));
		}
		return items;
	}
}