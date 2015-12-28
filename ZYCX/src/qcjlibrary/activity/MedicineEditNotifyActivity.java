package qcjlibrary.activity;

import qcjlibrary.activity.base.BaseActivity;
import qcjlibrary.config.Config;
import qcjlibrary.model.ModelAlertData;
import qcjlibrary.model.ModelPop;
import qcjlibrary.util.SharedPreferencesUtil;
import qcjlibrary.util.ToastUtils;
import qcjlibrary.widget.popupview.PopAlertDaily;
import qcjlibrary.widget.popupview.PopAlertStartTime;
import qcjlibrary.widget.popupview.PopAlertTime;
import qcjlibrary.widget.popupview.PopAlertTimeList;
import qcjlibrary.widget.popupview.PopDatePicker;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.socialize.utils.Log;
import com.zhiyicx.zycx.R;
import com.zhiyicx.zycx.sociax.android.Thinksns;

/**
 * author：qiuchunjia time：
 * 
 * 上午11:53:03 类描述：这个类是实现
 *
 */

public class MedicineEditNotifyActivity extends BaseActivity {

	private EditText et_user;
	private EditText et_medicine_name;
	private TextView tv_once;
	private TextView tv_eat_med_repeat;
	private TextView tv_eat_med_repeatday;
	private ImageView iv_eat_med_edit;
	private TextView tv_eat_time;
	private ImageView iv_eat_time_edit;
	private TextView tv_eat_med_start;
	private TextView tv_eat_med_startday;
	private ImageView iv_eat_med_edit2;
	private TextView tv_start_time;
	private ImageView iv_start_time_edit;
	private ImageView iv_notify_open;
	private RelativeLayout rl_alert_repaet_daily;
	private RelativeLayout rl_alert_repeat_time;
	private RelativeLayout rl_alert_starttime;
	private TextView tv_title_right;
	
	private int id;
	private String userName;
	private String medicineName;
	private String count;
	private String day;
	private String startTime;
	private String timeList;
	private boolean isOpen;
	private boolean isExit;
	private Intent intent;
	private Bundle bundle;

	@Override
	public String setCenterTitle() {
		return "编辑提醒";
	}

	@Override
	public void initIntent() {
		intent = getIntent();
	}

	@Override
	public int getLayoutId() {
		return R.layout.activity_medicine_edit_notify;
	}

	@Override
	public void initView() {
		et_medicine_name = (EditText) findViewById(R.id.et_medicine_name);
		et_user = (EditText) findViewById(R.id.et_user);
		tv_once = (TextView) findViewById(R.id.tv_once);
		tv_eat_med_repeat = (TextView) findViewById(R.id.tv_eat_med_repeat);
		tv_eat_med_repeatday = (TextView) findViewById(R.id.tv_eat_med_repeatday);
		iv_eat_med_edit = (ImageView) findViewById(R.id.iv_eat_med_edit);
		tv_eat_time = (TextView) findViewById(R.id.tv_eat_time);
		iv_eat_time_edit = (ImageView) findViewById(R.id.iv_eat_time_edit);
		tv_start_time = (TextView) findViewById(R.id.tv_start_time);
		iv_start_time_edit = (ImageView) findViewById(R.id.iv_start_time_edit);
		iv_notify_open = (ImageView) findViewById(R.id.iv_notify_open);
		rl_alert_repaet_daily = (RelativeLayout) findViewById(R.id.rl_alert_repaet_daily);
		rl_alert_repeat_time = (RelativeLayout) findViewById(R.id.rl_alert_repeat_time);
		rl_alert_starttime = (RelativeLayout) findViewById(R.id.rl_alert_starttime);
		tv_title_right = (TextView) findViewById(R.id.tv_title_right);
		
		bundle = intent.getExtras();
		if(bundle == null){
			isExit = false;
			titleSetRightTitle("确认");
		} else{
			isExit = true;
			titleSetRightTitle("修改");
		}
		
		
	}

	@Override
	public void initData() {
		count = "1次";
		day = "每天";
		try{
			id = (Integer) SharedPreferencesUtil.getData(this, Config.SHARED_SAVE_KEY, 0);
		} catch(Exception e){
			id = 0;
		}
		isOpen = false;
		if(isExit){
			ModelAlertData mData = (ModelAlertData) bundle.get
					(Config.ACTIVITY_TRANSFER_BUNDLE);
			userName = mData.getUserName();
			medicineName = mData.getMedicineName();
			day = mData.getRepeatDaily();
			count = mData.getRepeatCount();
			timeList = mData.getTimeList();
			startTime = mData.getStartTime();
			isOpen = mData.isOpen();
			et_user.setText(userName);
			et_medicine_name.setText(medicineName);
			tv_once.setText(day+count);
			tv_eat_med_repeatday.setText(day);
			tv_eat_time.setText(timeList);
			tv_start_time.setText(startTime);
			if(isOpen){
				iv_notify_open.setImageResource(R.drawable.switch_on);
			}
		}
	}

	@Override
	public void initListener() {
		rl_alert_repaet_daily.setOnClickListener(this);
		rl_alert_repeat_time.setOnClickListener(this);
		rl_alert_starttime.setOnClickListener(this);
		iv_notify_open.setOnClickListener(this);
		tv_title_right.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_alert_repaet_daily:
			//弹出日期提醒频率框
			PopAlertDaily alertDaily = new PopAlertDaily(this, null, this);
			alertDaily.showPop(rl_alert_repaet_daily, Gravity.BOTTOM, 0, 0);
			break;
		case R.id.rl_alert_repeat_time:
			//弹出时间提醒频率框
			PopAlertTimeList alertTimeList = new PopAlertTimeList(this, null, this);
			alertTimeList.showPop(rl_alert_repeat_time, Gravity.BOTTOM, 0, 0);
			Thinksns.medicineAct = this;
			break;
		case R.id.rl_alert_starttime:
			//弹出开始时间选择框
			PopAlertStartTime datePicker = new PopAlertStartTime(this, null, this);
			datePicker.showPop(rl_alert_starttime, Gravity.BOTTOM, 0, 0);
			break;
		case R.id.iv_notify_open:
			//打开或关闭提醒
			setNotify();
			break;
		case R.id.tv_title_right:
			userName = et_user.getText()+"";
			medicineName = et_medicine_name.getText()+"";
			if(!userName.equals("") && !medicineName.equals("")){
				if(startTime != null){
					StringBuffer totalData = new StringBuffer();
					totalData.append(isOpen+",").append(userName+",").append(medicineName+",").
					append(day+",").append(count+",").append(startTime+",").append(timeList);
					SharedPreferencesUtil.saveData(this, (++id)+"", totalData.toString());
					SharedPreferencesUtil.saveData(this, Config.SHARED_SAVE_KEY, id);
					mApp.startActivity(this, UseMedicineNotifyActivity.class, null);
				} else{
					ToastUtils.showLongToast(this, R.string.alert_time_starttime);
				}
			} else{
				ToastUtils.showLongToast(this, R.string.alert_time_username);
			}
			break;
		default:
			break;
		}
	}

	@Override
	public Object onPopResult(Object object) {
		String type = ((ModelPop)object).getType();
		String timeAndCount;
		if(type.equals(Config.TYPE_DAILY)){
			day = ((ModelPop)object).getDataStr();
			tv_once.setText(day+count);
			tv_eat_med_repeatday.setText(day);
		} else if(type.equals(Config.TYPE_TIME_LIST)){
			timeAndCount = ((ModelPop)object).getDataStr();
			timeList = timeAndCount.split(",")[0];
			count = timeAndCount.split(",")[1];
			tv_eat_time.setText(timeList);
			tv_once.setText(day+count);
		} else if(type.equals(Config.TYPE_DATE)){
			startTime = ((ModelPop)object).getDataStr();
			tv_start_time.setText(startTime);
		}
		return super.onPopResult(object);
	}

	private void setNotify() {
		if(isOpen){
			iv_notify_open.setImageResource(R.drawable.switch_off);
			isOpen = false;
		} else{
			iv_notify_open.setImageResource(R.drawable.switch_on);
			isOpen = true;
		}
	}
}
