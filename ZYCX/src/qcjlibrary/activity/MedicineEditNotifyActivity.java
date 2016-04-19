package qcjlibrary.activity;

import qcjlibrary.activity.base.BaseActivity;
import qcjlibrary.api.api.AlarmImpl;
import qcjlibrary.config.Config;
import qcjlibrary.model.ModelAlertData;
import qcjlibrary.model.ModelMsg;
import qcjlibrary.model.ModelPop;
import qcjlibrary.util.DateUtil;
import qcjlibrary.util.EditTextUtils;
import qcjlibrary.util.L;
import qcjlibrary.util.SharedPreferencesUtil;
import qcjlibrary.util.ToastUtils;
import qcjlibrary.widget.popupview.PopAlertDaily;
import qcjlibrary.widget.popupview.PopAlertStartTime;
import qcjlibrary.widget.popupview.PopAlertTime;
import qcjlibrary.widget.popupview.PopAlertTimeList;
import qcjlibrary.widget.popupview.PopDatePicker;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import com.baidu.android.bba.common.util.Util;
import com.bigkoo.pickerview.TimePickerView;
import com.umeng.socialize.utils.Log;
import com.zhiyicx.zycx.R;
import com.zhiyicx.zycx.sociax.android.Thinksns;
import com.zhiyicx.zycx.sociax.unit.SociaxUIUtils;
import com.zhiyicx.zycx.util.PreferenceUtil;

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
	private int med_num;
	private String period;
	private String startTime;
	private String timeList;
	private int isOpen;
	private boolean isExit;
	private Intent intent;
	private Bundle bundle;
	private String timeAndCount;
	/**年月日时间选择框**/
	private TimePickerView pvTime;
	
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
		
		/**
		 * 判断是创建闹钟还是修改闹钟，再修改UI
		 * */
		bundle = intent.getExtras();
		if(bundle == null){
			isExit = false;
			titleSetRightTitle("确认");
		} else{
			isExit = true;
			titleSetRightTitle("修改");
			titleSetCenterTitle("修改提醒");
		}
		
		//设置时间选择框属性
		pvTime = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
		pvTime.setTime(new Date());
		pvTime.setCyclic(true);
        pvTime.setCancelable(true);
	}

	@Override
	public void initData() {
		med_num = 1;
		period = "1";
		isOpen = 1;
		if(isExit){
			//修改闹钟,获取闹钟数据，将原来的数据展示出来
			ModelAlertData mData = (ModelAlertData) bundle.get
					(Config.ACTIVITY_TRANSFER_BUNDLE);
			id = mData.getId();
			userName = mData.getUser();
			medicineName = mData.getMedicine();
			period = mData.getPeriod();
			med_num = mData.getMed_num();
			timeList = mData.getMed_time();
			//用于传递到PopWindow
			timeAndCount = timeList +"-"+med_num;
			startTime = mData.getStime();
			isOpen = mData.getIs_remind();
			et_user.setText(userName);
			et_medicine_name.setText(medicineName);
			if(period.equals("1")){
				tv_once.setText("每天"+med_num+"次");
				tv_eat_med_repeatday.setText("每天");
			} else{
				tv_once.setText("每"+period+"天"+med_num+"次");
				tv_eat_med_repeatday.setText("每"+period+"天");
			}
			tv_start_time.setText(startTime);
			tv_eat_time.setText(timeList);
			if(isOpen == 0){
				iv_notify_open.setImageResource(R.drawable.on_green);
			}
		} else{
			//如果是一个新的闹钟，设置初始信息
			Date cur = new Date();
			startTime = DateUtil.strTodate2(DateUtil.DateToStamp(cur));
        	tv_start_time.setText(startTime);
        	period = "1";
        	med_num = 1;
        	timeList = "8:00";
		}
	}

	@Override
	public void initListener() {
		rl_alert_repaet_daily.setOnClickListener(this);
		rl_alert_repeat_time.setOnClickListener(this);
		rl_alert_starttime.setOnClickListener(this);
		iv_notify_open.setOnClickListener(this);
		tv_title_right.setOnClickListener(this);
		pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
            	startTime = DateUtil.strTodate2(DateUtil.DateToStamp(date));
            	tv_start_time.setText(startTime);
            }
        });
		//输入超出提醒
		et_medicine_name.addTextChangedListener(new EditTextUtils().getMyWatcher(20, et_medicine_name, this));
		et_user.addTextChangedListener(new EditTextUtils().getMyWatcher(20, et_user, this));
	}

	@Override
	public void onClick(View v) {
		//点击弹出选择框或者确认修改时，首先隐藏软键盘
		SociaxUIUtils.hideSoftKeyboard(this, et_medicine_name);
		SociaxUIUtils.hideSoftKeyboard(this, et_user);
		switch (v.getId()) {
		case R.id.rl_alert_repaet_daily:
			//弹出日期提醒频率框
			PopAlertDaily alertDaily = new PopAlertDaily(this, null, this);
			alertDaily.showPop(rl_alert_repaet_daily, Gravity.BOTTOM, 0, 0);
			break;
		case R.id.rl_alert_repeat_time:
			//弹出时间提醒频率框
			L.d("Cathy", "timeAndCount:"+timeAndCount);
			PopAlertTimeList alertTimeList = new PopAlertTimeList(this, timeAndCount, this);
			alertTimeList.showPop(rl_alert_repeat_time, Gravity.BOTTOM, 0, 0);
			Thinksns.medicineAct = this;
			break;
		case R.id.rl_alert_starttime:
			//弹出开始时间选择框
//			PopAlertStartTime datePicker = new PopAlertStartTime(this, startTime, this);
//			datePicker.showPop(rl_alert_starttime, Gravity.BOTTOM, 0, 0);
			//测试PickerView,新开分支……
			pvTime.show();
			break;
		case R.id.iv_notify_open:
			//打开或关闭提醒
			setNotify();
			break;
		case R.id.tv_title_right:
			//获取数据并且上传到服务器
			userName = et_user.getText()+"";
			medicineName = et_medicine_name.getText()+"";
			if(userName.length() > 20){
				ToastUtils.showLongToast(this, "用户名不得超过20字");
				return;
			} else if(medicineName.length() > 20){
				ToastUtils.showLongToast(this, "药品名不得超过20字");
				return;
			}
			if(!userName.equals("") && !medicineName.equals("")){
				if(startTime != null){
					//将数据保存到实体类中
					ModelAlertData mData = new ModelAlertData();
					mData.setUser(userName);
					mData.setMedicine(medicineName);
					mData.setPeriod(period);
					mData.setMed_num(med_num);
					mData.setMed_time(timeList);
					//转换为时间戳
					startTime = DateUtil.dateToStr2(startTime);
					mData.setStime(startTime);
					mData.setIs_remind(isOpen);
					AlarmImpl impl = new AlarmImpl();
					if(isExit){
						//如果是已经存在的闹钟数据，需要修改，则需要传递闹钟的Id
						//http://demo-qingko.zhiyicx.com/index.php?app=api&mod=Medreminder&act=index&oauth_token=304502a0d670f7d29272e5e3a84f8a87&oauth_token_secret=97462e1dec44619ba7d9b67c724b62d1
						mData.setId(id);
						sendRequest(impl.update(mData), ModelMsg.class, REQUEST_POST);
					} else{
						//新建的闹钟数据则不用传递Id，服务器自增
						sendRequest(impl.add(mData), ModelMsg.class, REQUEST_POST);
					}
//					StringBuffer totalData = new StringBuffer();
//					totalData.append(isOpen+";").append(userName+";").append(medicineName+";").
//					append(period+";").append(med_num+";").append(startTime+";").append(timeList);
//					SharedPreferencesUtil.saveData(this, (++id)+"", totalData.toString());
//					SharedPreferencesUtil.saveData(this, Config.SHARED_SAVE_KEY, id);
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
		//通过type判断返回数据的作用
		String type = ((ModelPop)object).getType();
		if(type.equals(Config.TYPE_DAILY)){
			//周期数据
			period = ((ModelPop)object).getDataInter()+"";
			if(period.equals("1")){
				tv_once.setText("每天"+med_num+"次");
				tv_eat_med_repeatday.setText("每天");
			} else{
				tv_once.setText("每"+period+"天"+med_num+"次");
				tv_eat_med_repeatday.setText("每"+period+"天");
			}
		} else if(type.equals(Config.TYPE_TIME_LIST)){
			//提醒时间
			timeAndCount = ((ModelPop)object).getDataStr();
			timeList = timeAndCount.split("-")[0];
			try {
				med_num = Integer.parseInt(timeAndCount.split("-")[1]);
			} catch (Exception e) {

			}
			tv_eat_time.setText(timeList);
			if(period.equals("1")){
				tv_once.setText("每天"+med_num+"次");
			} else{
				tv_once.setText("每"+period+"天"+med_num+"次");
			}
		} 
		return super.onPopResult(object);
	}

	//根据isOpen的值设置是否开启提示图标
	private void setNotify() {
		if(isOpen == 0){
			iv_notify_open.setImageResource(R.drawable.off);
			isOpen = 1;
		} else{
			iv_notify_open.setImageResource(R.drawable.on_green);
			isOpen = 0;
		}
	}
	
	@Override
	public Object onResponceSuccess(String str, Class class1) {
		Object object = super.onResponceSuccess(str, class1);
		if(object instanceof ModelMsg){
			ModelMsg msg = (ModelMsg) object;
			if(msg.getCode() == 0){
				//上传完毕后关闭当前页面，回到闹钟List界面
				//mApp.startActivity(this, UseMedicineNotifyActivity.class, null);
				finish();
			}
			ToastUtils.showLongToast(this, msg.getMessage());
		}
		return object;
	}
}
