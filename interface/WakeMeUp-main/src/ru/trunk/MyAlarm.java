//package ru.trunk;
//
//import java.util.Calendar;
//
//import android.app.Activity;
//import android.app.AlarmManager;
//import android.app.PendingIntent;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentSender;
//import android.util.Log;
//
//public class MyAlarm{
//	private Calendar cal;
//	private Item item;
//	private AlarmManager am;
//	public MyAlarm(Item item, Calendar cal,AlarmManager am) {
//		this.cal = cal;
//		this.item = item;
//		this.am = am;
//	}
//	public void stop(){
//		Log.i("Stopped","yes");
//		am.cancel(null);		
//	}
//	public void start(){
//		Log.i("Started", "yes");
//		Intent intent = new Intent(null, CallNotification.class);
//		PendingIntent pi = IntentSender.getBroadcast(MainActivity.this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT );
//		 am.set(AlarmManager.RTC_WAKEUP, stamp.getTime(), pi);
//	}
//	public void changeTime(Calendar cal){
//		stop();
//		this.cal = cal;
//		start();
//	}
//	public Calendar getTime(){
//		return cal;
//	}
//}
