package qcjlibrary.activity;

import qcjlibrary.activity.base.BaseActivity;
import qcjlibrary.activity.base.Title;
import qcjlibrary.adapter.UseMedicineNotifyAdapter;
import qcjlibrary.adapter.base.BAdapter;
import qcjlibrary.broadcast.AlarmBroadCastReciever;
import qcjlibrary.config.Config;
import qcjlibrary.listview.base.CommonListView;
import qcjlibrary.model.ModelAlertData;
import qcjlibrary.model.base.Model;
import qcjlibrary.util.DisplayUtils;
import qcjlibrary.util.SharedPreferencesUtil;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.umeng.socialize.utils.Log;
import com.zhiyicx.zycx.R;

/**
 * author：tan time：下午5:7:01 类描述：用药提醒，闹钟界面
 */

public class UseMedicineNotifyActivity extends BaseActivity {
	private CommonListView mCommonListView;
	private BAdapter mAdapter;

	private List<Model> mAlertList;
	// 闹钟管理类
	private AlarmManager mManager;

	@Override
	public void onClick(View v) {

	}

	@Override
	public String setCenterTitle() {
		return "用药提醒";
	}

	@Override
	public void initIntent() {

	}

	@Override
	public int getLayoutId() {
		return R.layout.listview_common_layout;
	}

	@Override
	public void initView() {
		titleSetRightImage(R.drawable.tianjia);
		mCommonListView = (CommonListView) findViewById(R.id.mCommonListView);
		mCommonListView.setDividerHeight(DisplayUtils.dp2px(this, 10));

	}

	@Override
	public void initData() {
		Title title = getTitleClass();
		title.iv_title_right1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mApp.startActivity_qcj(UseMedicineNotifyActivity.this, MedicineEditNotifyActivity.class, null);
			}
		});
		mAlertList = new ArrayList<Model>();
		int count = (Integer) SharedPreferencesUtil.getData(this, Config.SHARED_SAVE_KEY, 0);
		if (count < 1) {
			return;
		}
		for (int i = 1; i <= count; i++) {
			String totalData = SharedPreferencesUtil.getData(this, i + "", "null").toString();
			if (!totalData.equals("null")) {
				String[] mDataArr = totalData.split(",");
				ModelAlertData mData = new ModelAlertData();
				boolean isOpen = true;
				if (mDataArr[0].equals("false")) {
					isOpen = false;
				}
				String userName = mDataArr[1];
				String medicineName = mDataArr[2];
				String repeatDaily = mDataArr[3];
				String repeatCount = mDataArr[4];
				String startTime = mDataArr[5];
				String timeList = mDataArr[6];
				mData.setId(i);
				mData.setExit(true);
				mData.setOpen(isOpen);
				mData.setUserName(userName);
				mData.setMedicineName(medicineName);
				mData.setRepeatDaily(repeatDaily);
				mData.setRepeatCount(repeatCount);
				mData.setStartTime(startTime);
				mData.setTimeList(timeList);
				mAlertList.add(mData);
			}
		}
		int len = mAlertList.size();
		// Log.d("Cathy", "len:"+len);
		if (len > 0) {
			mAdapter = new UseMedicineNotifyAdapter(this, mAlertList);
			mCommonListView.setAdapter(mAdapter);
		}

		setAlarm();
	}

	@Override
	public void initListener() {
		mCommonListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				mCommonListView.stepToNextActivity(parent, view, position, MedicineEditNotifyActivity.class);

			}
		});
	}

	// 设置闹钟
	private void setAlarm() {
		Intent mIntent = new Intent(this, AlarmBroadCastReciever.class);
		mIntent.setAction("alarm.alert.short");
		// 循环为每个闹钟设置广播
		if (mAlertList.size() > 0) {
			for (int i = 0; i < mAlertList.size(); i++) {
				ModelAlertData mData = (ModelAlertData) mAlertList.get(i);
				if (mData.isOpen()) {
					String timeList = mData.getTimeList();
					String startTime = mData.getStartTime();
					String repeatDaily = mData.getRepeatDaily();
					int daily = setDailyCount(repeatDaily);
					// 根据重复天数计算出间隔的毫秒数
					long intervalMillis = setIntervalMillis(daily);
					String[] mTimeArr = timeList.split(";");
					// 为每一个时间设置广播
					for (int j = 0; j < mTimeArr.length; j++) {
						if (mTimeArr[j] != null) {
							long currentMillis = System.currentTimeMillis();
							if(getYearMonDay(startTime) < currentMillis){
								startTime = getYearMonDay(currentMillis);
							}
							//Log.d("Cathy", "开始年月日转换long:"+ getYearMonDay(startTime) +
									//"当前时间："+ currentMillis + "转换后："+ startTime);
							long triggerAtMillis = changeStr2Long(startTime + " " + mTimeArr[j] + ":00");
							//Log.d("Cathy", "开始时间："+ startTime + " " + mTimeArr[j] + ":00" + " 换算前："+ triggerAtMillis);
							//如果开始时间已经早于当前时间，则将当前时间设为开始时间
							if(currentMillis > triggerAtMillis){
								triggerAtMillis = triggerAtMillis + intervalMillis;
								//Log.d("Cathy", "下次提醒时间： "+changeLong2Str(triggerAtMillis));
							}
							/**
							 * 三种获取PendingIntent对象的方法 getActivity(Context, int,
							 * Intent, int) 启动一个activity getBroadcast(Context,
							 * int, Intent, int) 发送一个广播 getService(Context, int,
							 * Intent, int) 开启一个服务
							 */
							PendingIntent mPendingIntent = PendingIntent.getBroadcast(this, 0, mIntent, 0);
							mManager = (AlarmManager) getSystemService(ALARM_SERVICE);
							mManager.setRepeating(AlarmManager.RTC_WAKEUP, triggerAtMillis, intervalMillis,
									mPendingIntent);
						}
					}
				} else {
					Log.d("Cathy", "cancel alert");
					PendingIntent mPendingIntent = PendingIntent.getBroadcast(this, 0, mIntent, 0);
					mManager = (AlarmManager) getSystemService(ALARM_SERVICE);
					mManager.cancel(mPendingIntent);
				}
			}
		}
	}

	// 判断重复天数频率
	private int setDailyCount(String repeatDaily) {
		int count = 1;
		if (repeatDaily.equals("每2天")) {
			count = 2;
		} else if (repeatDaily.equals("每3天")) {
			count = 3;
		} else if (repeatDaily.equals("每4天")) {
			count = 4;
		} else if (repeatDaily.equals("每5天")) {
			count = 5;
		} else if (repeatDaily.equals("每6天")) {
			count = 6;
		} else if (repeatDaily.equals("每7天")) {
			count = 7;
		}
		return count;
	}

	/**
	 * @param int
	 *            daily 间隔天数
	 * @return long 将天数转换为毫秒数
	 */
	private long setIntervalMillis(int daily) {
		return daily * 24 * 60 * 60 * 1000;
	}

	/**
	 * 将时间字符串转换为long
	 */
	@SuppressLint("SimpleDateFormat")
	private long changeStr2Long(String time) {
		SimpleDateFormat mFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss",Locale.CHINA);
		Date mDate;
		try {
			mDate = mFormat.parse(time);
			return mDate.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
			Log.d("Cathy", e.toString());
		}
		return 0;
	}
	
	private String changeLong2Str(long time){
		SimpleDateFormat mFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss", Locale.CHINA);
		Date date = new Date(time);
		return mFormat.format(date);
	}
	
	private long getYearMonDay(String time){
		SimpleDateFormat mFormat = new SimpleDateFormat(
				"yyyy-MM-dd", Locale.CHINA);
		Date mDate;
		try {
			mDate = mFormat.parse(time);
			return mDate.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
			Log.d("Cathy", e.toString());
		}
		return 0;
	}

	private String getYearMonDay(long time){
		SimpleDateFormat mFormat = new SimpleDateFormat(
				"yyyy-MM-dd", Locale.CHINA);
		Date date = new Date(time);
		return mFormat.format(date);
	}
}
