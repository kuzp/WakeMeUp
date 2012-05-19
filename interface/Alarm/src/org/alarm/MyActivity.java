package org.alarm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public abstract class MyActivity extends Activity{
	private final static String SERVER_IS_BROKEN_MESSAGE = "Some goes wrong and our server fell , /n Sorry, try to run WMU Later!";

	public void onCreate(Bundle si){
		super.onCreate(si);
	}
    protected void catchServerIsBrokenException(){
		Intent intent = new Intent(this,ExceptionActivity.class);
		intent.putExtra("ExceptionMessage", SERVER_IS_BROKEN_MESSAGE);
		startActivity(intent);
	}
}
