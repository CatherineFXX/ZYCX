package qcjlibrary.activity;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

import qcjlibrary.activity.base.BaseActivity;
import qcjlibrary.config.Config;
import qcjlibrary.model.ModelMeAddress;
import qcjlibrary.model.ModelMsg;
import qcjlibrary.model.ModelPop;
import qcjlibrary.model.ModelUser;
import qcjlibrary.model.base.Model;
import qcjlibrary.util.DateUtil;
import qcjlibrary.util.L;
import qcjlibrary.util.ToastUtils;
import qcjlibrary.widget.RoundImageView;
import qcjlibrary.widget.popupview.PopChooseGender;
import qcjlibrary.widget.popupview.PopDatePicker;
import qcjlibrary.widget.popupview.PopUploadIcon;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.TimePickerView.OnTimeSelectListener;
import com.zhiyicx.zycx.R;

/**
 * author：qiuchunjia time：下午5:41:04 类描述：
 * 
 * 
 * 这个类是实现个人中心基本资料设置
 *
 */

public class MeCenterBasicActivity extends BaseActivity {
	private RelativeLayout rl_user;
	private RoundImageView riv_user_icon;
	private RelativeLayout rl_mycase;
	private TextView tv_mycase_value;
	private RelativeLayout rl_nick;
	private TextView tv_nick_value;
	private RelativeLayout rl_gender;
	private TextView tv_gender_value;
	private RelativeLayout rl_birth;
	private TextView tv_birth_value;
	private RelativeLayout rl_address;
	private TextView tv_address_value;
	private RelativeLayout rl_cancer_category;
	private TextView tv_category_value;
	/** 年月日时间选择框 **/
	private TimePickerView pvTime;
	private Bundle bundle;

	@Override
	public String setCenterTitle() {

		return "个人中心";
	}

	@Override
	public void initIntent() {
		// TODO Auto-generated method stub

	}

	@Override
	public int getLayoutId() {
		return R.layout.activity_me_centerbasic;
	}

	@Override
	public void initView() {
		rl_user = (RelativeLayout) findViewById(R.id.rl_user);
		riv_user_icon = (RoundImageView) findViewById(R.id.riv_user_icon);
		rl_mycase = (RelativeLayout) findViewById(R.id.rl_mycase);
		tv_mycase_value = (TextView) findViewById(R.id.tv_mycase_value);
		rl_nick = (RelativeLayout) findViewById(R.id.rl_nick);
		tv_nick_value = (TextView) findViewById(R.id.tv_nick_value);
		rl_gender = (RelativeLayout) findViewById(R.id.rl_gender);
		tv_gender_value = (TextView) findViewById(R.id.tv_gender_value);
		rl_birth = (RelativeLayout) findViewById(R.id.rl_birth);
		tv_birth_value = (TextView) findViewById(R.id.tv_birth_value);
		rl_address = (RelativeLayout) findViewById(R.id.rl_address);
		tv_address_value = (TextView) findViewById(R.id.tv_address_value);
		rl_cancer_category = (RelativeLayout) findViewById(R.id.rl_cancer_category);
		tv_category_value = (TextView) findViewById(R.id.tv_category_value);

		pvTime = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
		pvTime.setTime(new Date());
		pvTime.setCyclic(false);
		pvTime.setCancelable(true);
	}

	@Override
	public void initData() {
		bundle = new Bundle();
	}

	@Override
	public Object onResponceSuccess(String str, Class class1) {
		Object object = super.onResponceSuccess(str, class1);
		if (object instanceof ModelUser) {
			ModelUser user = (ModelUser) object;
			addDataToView(user);
			mApp.saveUser(user); // 保存数据到shareprefrence中
		}
		if (judgeTheMsg(object)) {
			sendRequest(mApp.getUserImpl().index(), ModelUser.class, REQUEST_GET);
		}
		return object;
	}

	/**
	 * 添加数据到界面上
	 * 
	 * @param object
	 */
	private void addDataToView(ModelUser user) {
		if (user != null) {
			mApp.displayImage(user.getAvatar(), riv_user_icon);
			tv_mycase_value.setText(user.getIntro());
			tv_nick_value.setText(user.getUname());
			tv_gender_value.setText(user.getSex());
			tv_birth_value.setText(user.getBirthday());
			tv_address_value.setText(user.getLocation());
			if(!TextUtils.isEmpty(user.getCancer())){
				tv_category_value.setText(user.getCancer());
			} else {
				tv_category_value.setText("暂无");
			}
			bundle.putSerializable("intro", user.getIntro());
			bundle.putSerializable("name", user.getUname());
		}
	}

	@Override
	public void initListener() {
		rl_user.setOnClickListener(this);
		rl_mycase.setOnClickListener(this);
		rl_nick.setOnClickListener(this);
		rl_gender.setOnClickListener(this);
		rl_birth.setOnClickListener(this);
		rl_address.setOnClickListener(this);
		rl_cancer_category.setOnClickListener(this);
		pvTime.setOnTimeSelectListener(new OnTimeSelectListener() {

			@Override
			public void onTimeSelect(Date date) {
				Date cur = new Date();
				if(cur.compareTo(date) > 0){
					ModelUser user = new ModelUser();
					user.setBirthday(DateUtil.DateToStamp(date));
					sendRequest(mApp.getUserImpl().edituserdata(user), ModelMsg.class, REQUEST_GET);
				} else{
					ToastUtils.showToast(MeCenterBasicActivity.this, "不可大于当前日期");
				}
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		sendRequest(mApp.getUserImpl().index(), ModelUser.class, REQUEST_GET);
	}

	@Override
	public File getFile(String path) {
		File file = super.getFile(path);
		// 上传头像
		Log.i("file", file.toString());
		sendRequest(mApp.getUserImpl().editavatar(file), ModelMsg.class, REQUEST_POST);
		return file;
	}

	@Override
	public void onResponseProgress(long bytesWritten, long totalSize) {
		super.onResponseProgress(bytesWritten, totalSize);
		Log.i("Progress", bytesWritten + "           " + totalSize);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_user:
			// TODO
			PopUploadIcon popUploadIcon = new PopUploadIcon(this, null, this);
			popUploadIcon.showPop(rl_user, Gravity.BOTTOM, 0, 0);

			break;

		case R.id.rl_mycase:
			bundle.putInt("type", SettingOneLineEditActivity.DECLARATION);
			mApp.startActivity_qcj(this, SettingOneLineEditActivity.class,
					bundle);
			break;
		case R.id.rl_nick:
			bundle.putInt("type", SettingOneLineEditActivity.NICK);
			mApp.startActivity_qcj(this, SettingOneLineEditActivity.class,
					bundle);
			break;
		case R.id.rl_gender:
			PopChooseGender chooseGender = new PopChooseGender(this, null, this);
			chooseGender.showPop(rl_gender, Gravity.BOTTOM, 0, 0);
			break;
		case R.id.rl_birth:
			// PopDatePicker datePicker = new PopDatePicker(this, null, this);
			// datePicker.showPop(rl_birth, Gravity.BOTTOM, 0, 0);
			pvTime.show();
			break;
		case R.id.rl_address:
			mApp.startActivityForResult_qcj(this, MeChooseProvinceActivity.class, null);
			break;
		case R.id.rl_cancer_category:
			bundle.putInt("type", SettingOneLineEditActivity.CANCERCATEGORY);
			mApp.startActivity_qcj(this, MeChooseCancerActivity.class,
					bundle);
			break;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Object object = getReturnResultSeri(requestCode, data, Config.TYPE_ADDRESS);
		if (object instanceof ModelMeAddress) {
			ModelMeAddress address = (ModelMeAddress) object;
			ModelUser user = new ModelUser();
			String addressId = address.getProvinceId() + "," + address.getCityId() + "," + address.getTowerId();
			String wholeAddress = address.getProvinceName() + "," + address.getCityName() + ","
					+ address.getTowerName();
			user.setCity_ids(addressId);
			user.setLocation(wholeAddress);
			Log.i("city", addressId + "");
			sendRequest(mApp.getUserImpl().edituserdata(user), ModelMsg.class, REQUEST_GET);
		}
	}

	@Override
	public Object onPopResult(Object object) {
		Object object2 = super.onPopResult(object);
		if (object2 instanceof ModelPop) {
			ModelPop data = (ModelPop) object2;
			ModelUser user = new ModelUser();
			if (data.getType().equals(Config.TYPE_GENDER)) {
				user.setSex(data.getDataStr());
			} else {
				user.setBirthday(DateUtil.dateToStr(data.getDataStr()));
			}
			sendRequest(mApp.getUserImpl().edituserdata(user), ModelMsg.class, REQUEST_GET);
		}
		return object2;
	}
}
