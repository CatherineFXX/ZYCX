
package qcjlibrary.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.http.Header;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.umeng.socialize.utils.Log;
import com.zhiyicx.zycx.R;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import qcjlibrary.activity.base.BaseActivity;
import qcjlibrary.activity.base.Title;
import qcjlibrary.model.ModelAddCase;
import qcjlibrary.model.ModelAddHistoryCase;
import qcjlibrary.model.ModelAddNowCase;
import qcjlibrary.model.ModelAddNowCase.Result;
import qcjlibrary.model.ModelDiagnosis;
import qcjlibrary.model.ModelFoodIdDetail;
import qcjlibrary.model.ModelImage;
import qcjlibrary.model.ModelLab;
import qcjlibrary.model.ModelMyCaseIndex;
import qcjlibrary.model.base.Model;
import qcjlibrary.util.ToastUtils;
import qcjlibrary.widget.popupview.PopImportFile;

/**
 * author：qiuchunjia time：上午10:55:26 类描述：这个类是实现
 *
 */

public class PatientMeActivity extends BaseActivity {
	private TextView tv_edit;
	private TextView tv_username;
	private TextView tv_gender;
	private TextView tv_age;
	private TextView tv_nation;
	private TextView tv_marry;
	private TextView tv_job;
	private TextView tv_education;
	private TextView tv_pretect;
	private TextView tv_hometown;
	private TextView tv_address;
	private TextView tv_height;
	private TextView tv_weight;
	private TextView tv_edit2;

	private TextView tv_user_histroy;
	private TextView tv_allegry;
	private TextView tv_useraddress;
	private TextView tv_food_habit;
	private TextView tv_smoke;
	private TextView tv_drink;
	private TextView tv_first;
	private TextView tv_child;
	private TextView tv_family;
	/** 数据展示项 **/
	private LinearLayout ll_user;
	private LinearLayout ll_once;
	private LinearLayout ll_now;
	/** 缺省页 **/
	private LinearLayout defautl_1;
	private LinearLayout defautl_2;
	private LinearLayout defautl_3;

	/*************** 添加现病史的控件 **************************/
	private TextView tv_now_edit2;
	private TextView tv_commit_time;
	private TextView tv_deal_time;
	private LinearLayout ll_time;

	private ModelMyCaseIndex mCaseIndex;
	
	/** 网络异常时的缺省图**/
	private View defaultView;
	private boolean isFirst = false;
	private int count;
	private LinearLayout ll_patientme_parent;
	private LinearLayout ll_common_default;
	
	private ModelAddCase sendInfo;
	private ModelAddHistoryCase sendHistory;
	private ModelAddNowCase sendPresent;

	@Override
	public String setCenterTitle() {
		return "我的病历";
	}

	@Override
	public void initIntent() {

	}

	@Override
	public int getLayoutId() {
		return R.layout.activity_patient_me;
	}

	@Override
	public void initView() {
		titleSetRightImage(R.drawable.daochu);
		tv_edit = (TextView) findViewById(R.id.tv_edit);
		tv_username = (TextView) findViewById(R.id.tv_username);
		tv_gender = (TextView) findViewById(R.id.tv_gender);
		tv_age = (TextView) findViewById(R.id.tv_age);
		tv_nation = (TextView) findViewById(R.id.tv_nation);
		tv_marry = (TextView) findViewById(R.id.tv_marry);
		tv_job = (TextView) findViewById(R.id.tv_job);
		tv_education = (TextView) findViewById(R.id.tv_education);
		tv_pretect = (TextView) findViewById(R.id.tv_pretect);
		tv_hometown = (TextView) findViewById(R.id.tv_hometown);
		tv_address = (TextView) findViewById(R.id.tv_address);
		tv_height = (TextView) findViewById(R.id.tv_height);
		tv_weight = (TextView) findViewById(R.id.tv_weight);
		tv_edit2 = (TextView) findViewById(R.id.tv_edit2);
		tv_user_histroy = (TextView) findViewById(R.id.tv_user_histroy);
		tv_allegry = (TextView) findViewById(R.id.tv_allegry);
		tv_useraddress = (TextView) findViewById(R.id.tv_useraddress);
		tv_food_habit = (TextView) findViewById(R.id.tv_food_habit);
		tv_smoke = (TextView) findViewById(R.id.tv_smoke);
		tv_drink = (TextView) findViewById(R.id.tv_drink);
		tv_first = (TextView) findViewById(R.id.tv_first);
		tv_now_edit2 = (TextView) findViewById(R.id.tv_now_edit2);
		tv_child = (TextView) findViewById(R.id.tv_child);
		tv_family = (TextView) findViewById(R.id.tv_family);
		tv_commit_time = (TextView) findViewById(R.id.tv_commit_time);
		tv_deal_time = (TextView) findViewById(R.id.tv_deal_time);

		ll_user = (LinearLayout) findViewById(R.id.ll_user);
		ll_once = (LinearLayout) findViewById(R.id.ll_once);
		ll_now = (LinearLayout) findViewById(R.id.ll_now);
		defautl_1 = (LinearLayout) findViewById(R.id.defautl_1);
		defautl_2 = (LinearLayout) findViewById(R.id.defautl_2);
		defautl_3 = (LinearLayout) findViewById(R.id.defautl_3);
		ll_time = (LinearLayout) findViewById(R.id.ll_time);
		
		ll_patientme_parent = (LinearLayout) findViewById(R.id.ll_patientme_parent);
		ll_common_default = (LinearLayout) findViewById(R.id.ll_patientme_parent);
	}

	@Override
	public void initData() {
		count = ll_patientme_parent.getChildCount();
		Title title = getTitleClass();
		title.iv_title_right1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mCaseIndex == null) {
					ToastUtils.showToast("暂无病例信息");
					sendRequest(mApp.getMedRecordImpl().myMedRecord(), ModelMyCaseIndex.class, REQUEST_GET);
					return;
				}
				if (!TextUtils.isEmpty(mCaseIndex.getInfo().getUrl())) {
					downloadFile(mCaseIndex.getInfo().getUrl(), mApp.getFilePath().toString(), "病例导出.png");
				} else {
					ToastUtils.showToast("暂无病例信息");
				}
			}
		});
	}
	
	/**
	 * 下载文件
	 */
	private void downloadFile(String fileUrl, final String dir, String filename) {
		final File file = new File(dir);
		if (!file.exists()) {
			file.mkdir();
		}
		final File wholeFile = new File(dir + "/" + filename);
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(fileUrl, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				ToastUtils.showToast("导出失败！");
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				if (arg0 == 200) {
					FileOutputStream outputStream;
					try {
						outputStream = new FileOutputStream(wholeFile);
						outputStream.write(arg2, 0, arg2.length);
						PopImportFile importFile = new PopImportFile(PatientMeActivity.this, dir.toString(), null);
						importFile.showPop(ll_user, Gravity.CENTER, 0, 0);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});

	}

	@Override
	public Object onResponceSuccess(String str, Class class1) {
		Object object = super.onResponceSuccess(str, class1);
		if (object instanceof ModelMyCaseIndex) {
			mCaseIndex = (ModelMyCaseIndex) object;
			addInfroToView(mCaseIndex.getInfo());
			addHistroyToView(mCaseIndex.getHistory());
			addPresentToView(mCaseIndex.getPresent());
		}
		return object;
	}

	/**
	 * 添加信息到患者信息
	 * 
	 * @param info
	 */
	private void addInfroToView(ModelAddCase info) {
		if (info != null) {
			sendInfo = info;
			ll_user.setVisibility(View.VISIBLE);
			defautl_1.setVisibility(View.GONE);
			tv_username.setText(info.getRealname());
			if (info.getSex().equals("0")) {
				tv_gender.setText("男");
			} else {
				tv_gender.setText("女");
			}
			tv_age.setText(info.getAge() + "岁");
			tv_nation.setText("民族：" + info.getNation());
			if (info.getMarriage().equals("0")) {
				tv_marry.setText("婚姻：未婚");
			} else {
				tv_marry.setText("婚姻：已婚");
			}
			tv_job.setText("职业：" + info.getProfession());
			tv_education.setText("文化程度：" + info.getEducation());
			tv_pretect.setText("保险形式：" + info.getInsform());
			tv_hometown.setText("籍贯：" + info.getNatives());
			tv_address.setText("居住地：" + info.getDomicile()); // TODO
			tv_height.setText("身高：" + info.getHeight() + "cm");
			tv_weight.setText("体重：" + info.getWeight() + "kg");
		}
	}

	/**
	 * 添加既往史到界面
	 * 
	 * @param history
	 */
	private void addHistroyToView(ModelAddHistoryCase history) {
		if (history != null) {
			ll_once.setVisibility(View.VISIBLE);
			defautl_2.setVisibility(View.GONE);
			tv_user_histroy.setText("既往史：" + history.getMed_history());
			tv_allegry.setText("过敏史：" + history.getAllergy_history());
			tv_useraddress.setText("个人史：" + history.getPer_history());
			tv_food_habit.setText("饮食习惯：" + history.getEating_habit());
			tv_smoke.setText("抽烟：");
			if (history.getSmoke().equals("1")) {
				tv_smoke.append("抽烟，");
				tv_smoke.append(history.getSmoke_age() + "岁开始，");
				tv_smoke.append(history.getSmoke_time() + "根/日，");
				if (history.getStop_smoke().equals("1")) {
					tv_smoke.append("已戒烟，");
					tv_smoke.append("戒烟时间：" + history.getStop_smoke_time());
				}
			} else{
				tv_smoke.append("不抽烟");
			}
			/***
			 * 
			 * "med_history": "富士达发生", "allergy_history": "发多少发", "per_history":
			 * "发的沙发上", "eating_habit": "荤", "smoke": "0", "smoke_age": "22",
			 * "smoke_time": "22", "stop_smoke": "0", "stop_smoke_time":
			 * "1970-01-01", "drink": "1", "drink_age": "23",
			 * "drink_consumption": "230", "stop_drink": "1", "stop_drink_time":
			 * "1970-01-01", "menarche_age": "14", "menarche_etime":
			 * "1970-01-01", "amenorrhoea_age": "54", "childs": "一子一女",
			 * "family_history": "打算的", "ctime": "2015-12-23", "utime":
			 * "2015-12-23"
			 * 
			 * 
			 * 
			 *****/
			tv_drink.setText("饮酒：");
			if (history.getDrink().equals("1")) {
				tv_drink.append("饮酒，");
				tv_drink.append(history.getDrink_age() + "岁开始，");
				tv_drink.append(history.getDrink_consumption() + "ml/日，");
				if (history.getStop_drink().equals("1")) {
					tv_drink.append("已戒酒，");
					tv_drink.append("戒酒时间：" + history.getStop_drink_time());
				}
			} else{
				tv_drink.append("不饮酒");
			}
			tv_child.setText("子女：" + history.getChilds());
			tv_family.setText("家族史：" + history.getFamily_history());
			if(sendInfo != null){
				if (sendInfo.getSex().equals("1") && history.getMenarche_age() != null 
						&& history.getMenarche_age().equals(" ")) {
					history.setSex("1");
					tv_first.setText("月经史：");
					tv_first.append("月经年龄" + history.getMenarche_age() + "岁,");
					tv_first.append("末次月经时间" + history.getMenarche_etime());
				} else{
					tv_first.setText("月经史：无");
					history.setSex("0");
				}
			}
			sendHistory = history;
		}
	}

	/**
	 * 添加现病史到界面
	 * 
	 * @param present
	 */
	private void addPresentToView(ModelAddNowCase present) {
		if (present != null) {
			ll_now.removeAllViews();
			sendPresent = present;
			List<Result> diagnosis_result = present.getDiagnosis().getDiagnosis_result();
			List<Result> lab_result = present.getLab_exam().getLab_exam_result();
			List<Result> img_result = present.getImage_exam().getImage_exam_result();
			/**
			 * 添加诊断过程
			 */
			if (diagnosis_result != null && diagnosis_result.size() > 0) {
				setPresentToView(present);
				View item = LayoutInflater.from(this).inflate(R.layout.case_present_item, null);
				// 初始化控件
				TextView tv_title = (TextView) item.findViewById(R.id.tv_title);
				TextView tv_time = (TextView) item.findViewById(R.id.tv_time);
				TextView tv_hospital = (TextView) item.findViewById(R.id.tv_hospital);
				TextView tv_way_or_program = (TextView) item.findViewById(R.id.tv_way_or_program);
				TextView tv_list_name = (TextView) item.findViewById(R.id.tv_list_name);
//				tv_way_or_program.setVisibility(View.VISIBLE);
				LinearLayout ll_present_result = (LinearLayout) item.findViewById(R.id.ll_present_result);
//				tv_time.setVisibility(View.VISIBLE);
				ll_now.addView(item);
				ModelDiagnosis diagnosis = present.getDiagnosis();
				tv_title.setText("治疗过程");
				tv_time.setText("治疗时间:" + diagnosis.getDiagnosis_stime());
				tv_hospital.setText("医院:" + diagnosis.getDiagnosis_hospital());
				tv_way_or_program.setText("方式:" + diagnosis.getDiagnosis_way());
				tv_list_name.setText(diagnosis.getList_name());
				setResultView(diagnosis_result, ll_present_result);
			}
			/**
			 * 添加实验室检查
			 */
			if (lab_result != null && lab_result.size() > 0) {
				setPresentToView(present);
				View item = LayoutInflater.from(this).inflate(R.layout.case_present_item, null);
				// 初始化控件
				TextView tv_title = (TextView) item.findViewById(R.id.tv_title);
				TextView tv_time = (TextView) item.findViewById(R.id.tv_time);
				TextView tv_hospital = (TextView) item.findViewById(R.id.tv_hospital);
				TextView tv_way_or_program = (TextView) item.findViewById(R.id.tv_way_or_program);
				TextView tv_list_name = (TextView) item.findViewById(R.id.tv_list_name);
				LinearLayout ll_present_result = (LinearLayout) item.findViewById(R.id.ll_present_result);
				ll_now.addView(item);
				ModelLab lab = present.getLab_exam();
				tv_title.setText("实验室检查");
				tv_time.setText("检查时间:" + lab.getLab_exam_time());
				tv_hospital.setText("医院:" + lab.getLab_exam_hospital());
				tv_way_or_program.setText("检查项目："+lab.getLab_exam_program());
				tv_list_name.setText(lab.getList_name());
				setResultView(lab_result, ll_present_result);
			}
			/**
			 * 添加影像检查
			 */
			if (img_result != null && img_result.size() > 0) {
				setPresentToView(present);
				View item = LayoutInflater.from(this).inflate(R.layout.case_present_item, null);
				// 初始化控件
				TextView tv_title = (TextView) item.findViewById(R.id.tv_title);
				TextView tv_time = (TextView) item.findViewById(R.id.tv_time);
				TextView tv_hospital = (TextView) item.findViewById(R.id.tv_hospital);
				TextView tv_way_or_program = (TextView) item.findViewById(R.id.tv_way_or_program);
				TextView tv_list_name = (TextView) item.findViewById(R.id.tv_list_name);
				LinearLayout ll_present_result = (LinearLayout) item.findViewById(R.id.ll_present_result);
				ll_now.addView(item);
				ModelImage imgData = present.getImage_exam();
				tv_title.setText("影像学检查");
				tv_time.setText("检查时间:" + imgData.getImage_exam_time());
				tv_hospital.setText("医院:" + imgData.getImage_exam_hospital());
				tv_way_or_program.setText("检查项目："+imgData.getImage_exam_program());
				tv_list_name.setText(imgData.getList_name());
				setResultView(img_result, ll_present_result);
			}

		}
	}

	private void setPresentToView(ModelAddNowCase present) {
		ll_time.setVisibility(View.VISIBLE);
		ll_now.setVisibility(View.VISIBLE);
		defautl_3.setVisibility(View.GONE);
		tv_commit_time.setText("提交时间：" + present.getCtime());
		tv_deal_time.setText("处理时间：" + present.getUtime());
	}

	/**
	 * 设置返回结果布局
	 * 
	 * @param List<Result>
	 *            list
	 * @param LinearLayout
	 *            ll_present_result
	 */
	private void setResultView(List<Result> list, LinearLayout ll_present_result) {
		for (int i = 0; i < list.size(); i++) {
			View resultItem = LayoutInflater.from(this).inflate(R.layout.item_present_result, null);
			ll_present_result.addView(resultItem);
//			TextView tv_list_name = (TextView) resultItem.findViewById(R.id.tv_list_name);
			TextView tv_field = (TextView) resultItem.findViewById(R.id.tv_field);
			Result mResult = list.get(i);
//			tv_list_name.setText(mResult.getList_name());
			tv_field.setText(mResult.getField_name() + ":" + mResult.getField_value());
		}
	}

	@Override
	public void initListener() {
		tv_edit.setOnClickListener(this);
		tv_edit2.setOnClickListener(this);
		tv_now_edit2.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_edit:
			mApp.startActivity_qcj(this, PatientInforActivity.class, sendDataToBundle(sendInfo, "info"));
			break;

		case R.id.tv_edit2:
			mApp.startActivity_qcj(this, PatientHistoryActivity.class, sendDataToBundle(sendHistory, "history"));
			break;
		case R.id.tv_now_edit2:
			mApp.startActivity_qcj(this, PatientNowHistoryActivity.class, sendDataToBundle(new Model(), null));
			break;

		}

	}
	
	@Override
	protected void onResume() {
		// TODO 自动生成的方法存根
		super.onResume();
		sendRequest(mApp.getMedRecordImpl().myMedRecord(), ModelMyCaseIndex.class, REQUEST_GET);
	}
	
	
//	@Override
//	public View onRequestFailed() {
//		// TODO 自动生成的方法存根
//		defaultView = super.onRequestFailed();
//		TextView tv_reload = (TextView) findViewById(R.id.tv_reload);
//		tv_reload.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				sendRequest(mApp.getMedRecordImpl().myMedRecord(), 
//						ModelMyCaseIndex.class, REQUEST_GET);
//			}
//		});
//		for (int i = 0; i < count; i++) {
//			ll_patientme_parent.getChildAt(i).setVisibility(View.GONE);
//		}
//		ll_common_default.setVisibility(View.VISIBLE);
//		return defaultView;
//	}
//	
//	@Override
//	public View onRequestSuccess() {
//		// TODO 自动生成的方法存根
//		for (int i = 0; i < count; i++) {
//			ll_patientme_parent.getChildAt(i).setVisibility(View.VISIBLE);
//		}
//		if(ll_common_default != null){
//			ll_common_default.setVisibility(View.GONE);
//		}
//		return defaultView;
//	}
	

}
