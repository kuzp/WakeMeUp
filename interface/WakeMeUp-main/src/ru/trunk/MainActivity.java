package ru.trunk;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.monday_shedule);
    }
    public void showMondaySchedule(View v){
    	setContentView(R.layout.monday_shedule);
    }
    public void showMainActivity(View v){
    	setContentView(R.layout.main);
    }
}