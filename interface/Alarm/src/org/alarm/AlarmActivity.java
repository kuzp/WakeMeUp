package org.alarm;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import android.media.MediaPlayer;
import android.provider.Settings;
import android.net.Uri;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import android.os.Bundle;
import android.os.PowerManager;
import android.os.Vibrator;
import android.os.Handler;

import android.util.Log;
import android.media.AudioManager;

public class AlarmActivity extends Activity implements View.OnClickListener{
	private final static String tag = "AlarmActivity";
	// Time period between two vibration events
	private final static int VIBRATE_DELAY_TIME = 2000;
	// Vibrate for 1000 milliseconds
	private final static int DURATION_OF_VIBRATION = 1000;

	private MediaPlayer mPlayer;
	private PowerManager.WakeLock mWakeLock;

	private boolean mEnableVibration;
	private Button button;

	private Handler mPerformVibrationHandler = new Handler();
	private Runnable mVibrationRunnable = new Runnable() {
		public void run() {
			// Get instance of the Vibrator from current Context
			Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

			v.vibrate(DURATION_OF_VIBRATION);
			// Provide loop for vibration
			mPerformVibrationHandler.postDelayed(mVibrationRunnable, 
					DURATION_OF_VIBRATION+VIBRATE_DELAY_TIME);
		}
	};

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Log.d(tag, "onCreate");

		// Wake up the phone to show AlarmActivity
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		mWakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK |
				PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, tag);
		mWakeLock.acquire();

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
		setContentView(R.layout.act_popout);

		button = (Button) findViewById(R.id.tv_stop_player);
		button.setOnClickListener(this);

		// Retrieve shared preferences to set alarm alert
		SharedPreferences prefs = PreferenceManager
			.getDefaultSharedPreferences(this);
		String ringtonePref = prefs.getString("ringtone_pref", 
				Settings.System.DEFAULT_RINGTONE_URI.toString());
		if (prefs.getBoolean("vibration_pref", false)) {
			Log.d(tag, "Vibration is set");
			mEnableVibration = true; 
		}

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

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(tag, "onResume");
		
				try {
		
			// It never ends..
			mPlayer.setLooping(true);
			mPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
			mPlayer.prepare();
			mPlayer.start();

			// Want more noise? Here is a start point of the phone vibration
			if (mEnableVibration) {
				mPerformVibrationHandler.post(mVibrationRunnable);
			}
		} catch (Exception e) {
			Log.d(tag, "Start player is failed:" + e.toString());
			if (mPlayer.isPlaying()) {
				mPlayer.stop();
			}
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.d(tag, "onStart");
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.d(tag, "onPause");
		dismissAlarm();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d(tag, "onDestroy");
		mWakeLock.release();
	}
	
	/** Called when you tap on screen to stop alarm.
	 * Callback for click on TextView that fill the whole display.
	 * Set in xml file of the current layout.
	 */
	public void onClick(View v) {
  		if (v.getId() == R.id.tv_stop_player){
		dismissAlarm();
		finish();
		}
	}

	private void dismissAlarm() {
		if (mPlayer.isPlaying()) {
			mPlayer.stop();
		}
		mPerformVibrationHandler.removeCallbacks(mVibrationRunnable);	
	}
}

