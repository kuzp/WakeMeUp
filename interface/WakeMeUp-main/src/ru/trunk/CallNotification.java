package ru.trunk;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;

public class CallNotification extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.e("ЗВОНИТ!!!","ЗВОНИТ!!!");
		//final MediaPlayer mp = new MediaPlayer();
		
        // Start the activity where you can stop alarm
        Intent i = new Intent(context, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);

	}
}