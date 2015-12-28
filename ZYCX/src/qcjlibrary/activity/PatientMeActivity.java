package qcjlibrary.activity;

import qcjlibrary.activity.base.BaseActivity;
import qcjlibrary.model.ModelAddCase;
import qcjlibrary.model.ModelAddHistoryCase;
import qcjlibrary.model.ModelMyCaseIndex;
import qcjlibrary.model.base.Model;
import android.view.View;
import android.widget.TextView;

import com.zhiyicx.zycx.R;

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

	/*************** 添加现病史的控件 **************************/
	private TextView tv_now_edit2;

	@Override
	public String setCenterTitle() {
		return "患者信息";
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
	}

	@Override
	public void initData() {
		sendRequest(mApp.getMedRecordImpl().myMedRecord(),
				ModelMyCaseIndex.class, REQUEST_GET);
	}

	@Override
	public Object onResponceSuccess(String str, Class class1) {
		Object object = super.onResponceSuccess(str, class1);
		if (object instanceof ModelMyCaseIndex) {
			ModelMyCaseIndex caseIndex = (ModelMyCaseIndex) object;
			addInfroToView(caseIndex.getInfo());
			addHistroyToView(caseIndex.getHistory());
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
			tv_hometown.setText("籍贯：" + info.getDomicile());
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
			tv_user_histroy.setText("既往史：" + history.getMed_history());
			tv_allegry.setText("过敏史：" + history.getAllergy_history());
			tv_useraddress.setText("个人史：" + history.getPer_history());
			tv_food_habit.setText("饮食习惯：" + history.getEating_habit());
			tv_smoke.setText("抽烟：");
			if (history.getSmoke().equals("0")) {
				tv_smoke.append("抽烟,");
				tv_smoke.append(history.getSmoke_age() + "开始,");
				tv_smoke.append(history.getSmoke_time() + "根/日,");
				if (history.getStop_smoke().equals("0")) {
					tv_smoke.append("已戒烟,");
					tv_smoke.append("戒烟时间" + history.getStop_smoke_time());
				}
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
			 * *****/
			tv_drink.setText("饮酒：");
			if (history.getDrink().equals("1")) {
				tv_drink.append("饮酒，");
				tv_drink.append(history.getDrink_age() + "岁开始，");
				tv_drink.append(history.getDrink_consumption() + "ml,");
				if (history.getStop_drink().equals("1")) {
					tv_drink.append("已戒酒，");
					tv_drink.append("戒酒时间" + history.getStop_drink_time());
				}
			}
			tv_first.setText("月经史：");
			if (history.getMenarche_age() != null) {
				tv_first.append("月经年龄" + history.getMenarche_age() + "岁,");
				tv_first.append("末次月经时间" + history.getMenarche_etime());
			}
			tv_child.setText("子女：" + history.getChilds());
			tv_family.setText("家族史：" + history.getFamily_history());
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
			mApp.startActivity_qcj(this, PatientInforActivity.class,
					sendDataToBundle(new Model(), null));
			break;

		case R.id.tv_edit2:
			mApp.startActivity_qcj(this, PatientHistoryActivity.class,
					sendDataToBundle(new Model(), null));
			break;
		case R.id.tv_now_edit2:
			mApp.startActivity_qcj(this, PatientNowHistoryActivity.class,
					sendDataToBundle(null, null));
			break;

		}

	}

}
