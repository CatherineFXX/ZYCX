package qcjlibrary.service;

import com.zhiyicx.zycx.R;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

public class AlertService extends Service{

	private MediaPlayer mPlayer;
	
	@Override
	public IBinder onBind(Intent intent) {
		
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.alert_1);
		mPlayer.setLooping(false);
		mPlayer.start();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if(mPlayer != null){
			mPlayer.stop();
		}
	}
	
}
