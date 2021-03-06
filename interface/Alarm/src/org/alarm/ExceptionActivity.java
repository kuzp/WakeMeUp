package org.alarm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ExceptionActivity extends Activity {
	public void onCreate(Bundle savedInstanceState){

		super.onCreate(savedInstanceState);
		setContentView(R.layout.exception_layout);

		TextView messageTextView = (TextView)findViewById(R.id.ExceptionMessage);
		String textOfExceptionMessage = (String) getIntent().getExtras().get("ExceptionMessage");
		messageTextView.setText(textOfExceptionMessage);

		OnClickListener ocListener = new OnClickListener() {

			public void onClick(View v) {
				finish();
				Intent i = new Intent(ExceptionActivity.this,AlarmClockActivity.class);
				startActivity(i);
				moveTaskToBack(true);
			}
		};
		Button btn = (Button) findViewById(R.id.button1);
		btn.setOnClickListener(ocListener);
		//startActivity(new Intent(this, AlarmClockActivity.class));
	}
}
