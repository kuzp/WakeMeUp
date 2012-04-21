package ru.trunk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class BroadcastImpl extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		 Toast.makeText(context, "Будильник поставлен", Toast.LENGTH_SHORT).show();
		
	}

}
