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
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.provider.AlarmClock;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity{
	final String[] days = {"Понедельник","Вторник","Среда","Четверг","Пятница","Суббота","Воскресенье"};
	private long delay ;
	private List <Item> items =null;
	private final static String tag = "AlarmActivity";
	private MediaPlayer mPlayer;
	private OnClickListener myClickListener = new OnClickListener() {
	    public void onClick(View v) {
	    	final TextView mtv = (TextView) v;
	    	
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
	    	// сюда повесим листенер
	    }
	};
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		showMainActivity(null);
		Calendar cal = Calendar.getInstance();
		try{
			String tempTime = "06:54";//= getRings("Суббота", "неч")[1];

			cal.set(Calendar.HOUR_OF_DAY, getHour(tempTime));
			cal.set(Calendar.MINUTE , getMinute(tempTime));
			cal.set(Calendar.SECOND , 0);
			
			resetCall(System.currentTimeMillis()+100);
		}catch(Exception e){
			//todo smth
		}
		
		SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(this);
        String ringtonePref = prefs.getString("ringtone_pref", 
                        Settings.System.DEFAULT_RINGTONE_URI.toString());
        
        mPlayer = new MediaPlayer();    
        mPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                public boolean onError(MediaPlayer mp, int what, int extra) {
                        Log.d(tag, "Error occurred while playing audio");
                        Log.d(tag, "Error code: " + what);
                        mp.stop();
                        mp.release();
                        return true;
                }       
        });

        try {
                // Player setup is here
                mPlayer.setDataSource(this, Uri.parse(ringtonePref));
        } catch (Exception e) {
                Log.d(tag, "Setup player exception: " + e.toString());
        }

	}
	
	
	public void resetCall(long time){
		AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(this, CallNotification.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0,
		intent, PendingIntent.FLAG_CANCEL_CURRENT );
		am.cancel(pendingIntent);
		am.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
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
				Log.e("JSONException", e.getMessage());
			} catch (URISyntaxException e) {
				Log.e("URISyntaxException", e.getMessage());
			}
			ll.addView(tvDay);
			tvDay.setClickable(true);
			tvDay.setOnClickListener(myClickListener);
			ll.addView(tv2);
		}
	}


	
	protected void onResume() {
        super.onResume();
        Log.d(tag, "onResume");
        
        try {
        	// It never ends..
        	mPlayer.setLooping(true);
        	mPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
        	mPlayer.prepare();
        	mPlayer.start();

        } catch (Exception e) {
                Log.d(tag, "Start player is failed:" + e.toString());
                if (mPlayer.isPlaying()) {
                        mPlayer.stop();
                }
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
	private long convert(String time){
		return Integer.parseInt(time.substring(0, 2))*3600000 + Integer.parseInt(time.substring(3))*60000;
	}
	private int getHour(String time){
		return Integer.parseInt(time.substring(0, 2));
	}
	private int getMinute(String time){
		return Integer.parseInt(time.substring(3));
	}
}