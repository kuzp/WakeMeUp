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
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class WakeMeUpActivity extends Activity {
	final String LOG_TAG = "myLogs";
	  
	  final int DIALOG_ITEMS = 1;
	  final int DIALOG_DAYS = 2;
	  DB db;
	  Cursor cursor;

	  String[] data = {"��� �������", "5 �����", "10 �����", "15 �����", "20 �����", "25 �����", "30 �����", "1 ���"};
	  String[] week = {"�����������", "�������", "�����", "�������", "�������", "�������", "�����������"}; 

	  /** Called when the activity is first created. */
	  public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.main);

	    // ��������� ����������� � ��
	    db = new DB(this);
	    db.open();
	    refreshCursor();
	  }

	  public void onclick(View v) {
	    switch (v.getId()) {
	    		case R.id.btnRepeat :
	      showDialog(DIALOG_ITEMS);
	  break;    
	    		case R.id.btnDay: 
		      showDialog(DIALOG_DAYS);
		      break;
		      default:
		      break;
	    } 
	  }

	  protected Dialog onCreateDialog(int id) {
	    AlertDialog.Builder adb = new AlertDialog.Builder(this);
	    switch (id) {
	    	case DIALOG_ITEMS:
	      adb.setTitle(R.string.repeat);
	      adb.setSingleChoiceItems(data, -1, myClickListener);
	      break;
	    	case DIALOG_DAYS:
		      adb.setTitle(R.string.day);
		      adb.setSingleChoiceItems(week, -1, myClickListener);
		      break;
		}
	    adb.setPositiveButton(R.string.ok, myClickListener);
	    return adb.create();
	  }

	  // ���������� ������� �� ����� ������ ������� ��� ������
	  OnClickListener myClickListener = new OnClickListener() {
	    public void onClick(DialogInterface dialog, int which) {
	      ListView lv = ((AlertDialog) dialog).getListView();
	      if (which == Dialog.BUTTON_POSITIVE) 
	        // ������� � ��� ������� ���������� ��������        
	        Log.d(LOG_TAG, "pos = " + lv.getCheckedItemPosition());
	      else
	        // ������� � ��� ������� �������� ��������
	        Log.d(LOG_TAG, "which = " + which);
	    }
	  };

	  // ��������� ������
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