package qcjlibrary.broadcast;

import com.umeng.socialize.utils.Log;
import com.zhiyicx.zycx.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import qcjlibrary.activity.UseMedicineNotifyActivity;
import qcjlibrary.broadcast.base.BaseBroadCast;

public class AlarmBroadCastReciever extends BaseBroadCast{

	int count;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		String mAction = intent.getAction();
		//开机广播
		if(mAction.equals(Intent.ACTION_BOOT_COMPLETED)){
		}
		if(mAction.equals("alarm.alert.short")){
			Log.d("Cathy", "short alert: 吃药");
			//发送通知
			sendNotification(context);
			//播放短提示音,由于service只能开启一次，所有需要先stop
			context.stopService(new Intent("alert_music"));
			context.startService(new Intent("alert_music"));
		}
	}

	//通知
	@SuppressWarnings("deprecation")
	private void sendNotification(Context context) {
		count ++;
		NotificationManager mManager = (NotificationManager) context.getSystemService(
				Context.NOTIFICATION_SERVICE);
		Notification mNotification = new Notification(R.drawable.morentouxiang02,"吃药",System.currentTimeMillis());
		mNotification.flags = Notification.FLAG_AUTO_CANCEL;
		mNotification.number = count;
		//跳转到用药提醒界面
		Intent intent = new Intent(context,UseMedicineNotifyActivity.class);
		intent.setComponent(new ComponentName("qcjlibrary.broadcast", 
				"qcjlibrary.activity.UseMedicineNotifyActivity"));
		//关键的一步，设置启动模式
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK| 
        		Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        PendingIntent mPi = PendingIntent.getActivity(
        		context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT); 
        mNotification.setLatestEventInfo(context, "该吃药了", "点击查看详情",mPi); 
        mManager.notify(1, mNotification);
	}
	
}
