package com.wakemeup;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TimePicker;

public class NewScheduleItemActivity extends Activity {
	final String LOG_TAG = "myLogs";
	  
	  final int DIALOG_PARITY = 1;
	  final int DIALOG_DAYS = 2;
	  DB db;
	  Cursor cursor;

	  String[] parity = {"нет", "четная", "нечетная"};
	  String[] week = {"Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота", "Воскресенье"}; 

	  /** Called when the activity is first created. */
	  public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.new_schedule_item);
	    TimePicker tp1 = (TimePicker)this.findViewById(R.id.timePicker1);
		tp1.setIs24HourView(true);
		TimePicker tp2 = (TimePicker)this.findViewById(R.id.timePicker2);
		tp2.setIs24HourView(true);

	    // открываем подключение к БД
	    db = new DB(this);
	    db.open();
	    refreshCursor();
	  }

	  public void onclick(View v) {
	    switch (v.getId()) {
	    		case R.id.btnParity :
	      showDialog(DIALOG_PARITY);
	  break;    
	    		case R.id.btnDay2 : 
		      showDialog(DIALOG_DAYS);
		      break;
		      default:
		      break;
	    } 
	  }

	  protected Dialog onCreateDialog(int id) {
	    AlertDialog.Builder adb = new AlertDialog.Builder(this);
	    switch (id) {
	    	case DIALOG_PARITY:
	      adb.setTitle(R.string.parity);
	      adb.setSingleChoiceItems(parity, -1, myClickListener);
	      break;
	    	case DIALOG_DAYS:
		      adb.setTitle(R.string.day);
		      adb.setSingleChoiceItems(week, -1, myClickListener);
		      break;
		}
	    adb.setPositiveButton(R.string.ok, myClickListener);
	    return adb.create();
	  }

	  // обработчик нажатия на пункт списка диалога или кнопку
	  OnClickListener myClickListener = new OnClickListener() {
	    public void onClick(DialogInterface dialog, int which) {
	      ListView lv = ((AlertDialog) dialog).getListView();
	      if (which == Dialog.BUTTON_POSITIVE) 
	        // выводим в лог позицию выбранного элемента        
	        Log.d(LOG_TAG, "pos = " + lv.getCheckedItemPosition());
	      else
	        // выводим в лог позицию нажатого элемента
	        Log.d(LOG_TAG, "which = " + which);
	    }
	  };

	  // обновляем курсор
	  void refreshCursor() {
	    stopManagingCursor(cursor);
	    cursor = db.getAllData();
	    startManagingCursor(cursor);
	  }

	  @Override
	  protected void onDestroy() {
	    super.onDestroy();
	    db.close();
	  }
}
