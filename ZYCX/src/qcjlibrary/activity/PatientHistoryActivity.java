package qcjlibrary.activity;

import qcjlibrary.activity.base.BaseActivity;
import qcjlibrary.activity.base.Title;
import qcjlibrary.config.Config;
import qcjlibrary.model.ModelAddHistoryCase;
import qcjlibrary.model.ModelMsg;
import qcjlibrary.model.ModelPop;
import qcjlibrary.model.ModelUser;
import qcjlibrary.util.DateUtil;
import qcjlibrary.util.ToastUtils;
import qcjlibrary.widget.popupview.PopChooseDrink;
import qcjlibrary.widget.popupview.PopChooseSmoke;
import qcjlibrary.widget.popupview.PopChooseStopDrink;
import qcjlibrary.widget.popupview.PopChooseStopSmoke;
import qcjlibrary.widget.popupview.PopDatePicker;
import qcjlibrary.widget.popupview.PopEatingHabit;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Date;

import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.TimePickerView.OnTimeSelectListener;
import com.zhiyicx.zycx.R;
import com.zhiyicx.zycx.sociax.unit.SociaxUIUtils;

/**
 * author：qiuchunjia time：上午10:55:26 类描述：这个类是实现
 *
 */

public class PatientHistoryActivity extends BaseActivity {
	private EditText et_name;
	private EditText et_allergy_name;
	private EditText et_single_name;
	private RelativeLayout rl_eat;
	private TextView tv_eat_name;
	private RelativeLayout rl_smoke;
	private TextView tv_smoke_name;
	private EditText et_smoke_year;
	private EditText et_smoke_gen;
	private RelativeLayout rl_stop_smoke;
	private TextView tv_stop_smoke_name;
	private RelativeLayout rl_stop_smoke_time;
	private TextView tv_stop_smoke_time_name;

	private RelativeLayout rl_drink;
	private TextView tv_drink_name;
	private EditText et_drink_year;
	private EditText et_drink_much;
	private RelativeLayout rl_stop_drink;
	private TextView tv_stop_drink_name;
	private EditText et_first;
	private TextView tv_last_time_name;
	private EditText et_stop_yuejins;
	private RelativeLayout rl_children;
	private EditText et_childs_num;
	private EditText et_family_historys;
	private RelativeLayout rl_stop_drink_time;
	private TextView tv_stop_drink_name_time;

	// --------------判断是否抽烟喝酒以及。。。需要初始化的控件-------------------
	private RelativeLayout rl_smoke_year;
	private RelativeLayout rl_smoke_gen;
	private RelativeLayout rl_drink_year;
	private RelativeLayout rl_drink_much;
	private RelativeLayout rl_first;
	private RelativeLayout rl_last_time;
	private RelativeLayout rl_stop_yuejin;
	private LinearLayout ll_line_02;
	private LinearLayout ll_line_01;
	private LinearLayout ll_line_03;
	private ModelUser modelUser;
	
	private TimePickerView pvTime;
	private String timeType;
	private ModelAddHistoryCase history;
	
	@Override
	public String setCenterTitle() {
		return "既往史";
	}

	@Override
	public void initIntent() {
		history = (ModelAddHistoryCase) getDataFromIntent(getIntent(), "history");
	}

	@Override
	public int getLayoutId() {
		return R.layout.activity_patient_history;
	}

	@Override
	public void initView() {
		titleSetRightTitle("保存");
		et_name = (EditText) findViewById(R.id.et_name);
		et_allergy_name = (EditText) findViewById(R.id.et_allergy_name);
		et_single_name = (EditText) findViewById(R.id.et_single_name);
		rl_eat = (RelativeLayout) findViewById(R.id.rl_eat);
		tv_eat_name = (TextView) findViewById(R.id.tv_eat_name);
		rl_smoke = (RelativeLayout) findViewById(R.id.rl_smoke);
		tv_smoke_name = (TextView) findViewById(R.id.tv_smoke_name);
		et_smoke_year = (EditText) findViewById(R.id.et_smoke_year);
		et_smoke_gen = (EditText) findViewById(R.id.et_smoke_gen);
		rl_stop_smoke = (RelativeLayout) findViewById(R.id.rl_stop_smoke);
		tv_stop_smoke_name = (TextView) findViewById(R.id.tv_stop_smoke_name);
		rl_stop_smoke_time = (RelativeLayout) findViewById(R.id.rl_stop_smoke_time);
		tv_stop_smoke_time_name = (TextView) findViewById(R.id.tv_stop_smoke_time_name);

		rl_drink = (RelativeLayout) findViewById(R.id.rl_drink);
		tv_drink_name = (TextView) findViewById(R.id.tv_drink_name);
		et_drink_year = (EditText) findViewById(R.id.et_drink_year);
		et_drink_much = (EditText) findViewById(R.id.et_drink_much);
		rl_stop_drink = (RelativeLayout) findViewById(R.id.rl_stop_drink);
		tv_stop_drink_name = (TextView) findViewById(R.id.tv_stop_drink_name);
		et_first = (EditText) findViewById(R.id.et_first);
		rl_last_time = (RelativeLayout) findViewById(R.id.rl_last_time);
		tv_last_time_name = (TextView) findViewById(R.id.tv_last_time_name);
		et_stop_yuejins = (EditText) findViewById(R.id.et_stop_yuejins);
		rl_children = (RelativeLayout) findViewById(R.id.rl_children);
		et_childs_num = (EditText) findViewById(R.id.et_childs_num);
		et_family_historys = (EditText) findViewById(R.id.et_family_historys);
		rl_stop_drink_time = (RelativeLayout) findViewById(R.id.rl_stop_drink_time);
		tv_stop_drink_name_time = (TextView) findViewById(R.id.tv_stop_drink_name_time);

		rl_smoke_year = (RelativeLayout) findViewById(R.id.rl_smoke_year);
		rl_smoke_gen = (RelativeLayout) findViewById(R.id.rl_smoke_gen);
		rl_drink_year = (RelativeLayout) findViewById(R.id.rl_drink_year);
		rl_drink_much = (RelativeLayout) findViewById(R.id.rl_drink_much);
		rl_first = (RelativeLayout) findViewById(R.id.rl_first);
		rl_stop_yuejin = (RelativeLayout) findViewById(R.id.rl_stop_yuejin);
		
		ll_line_01 = (LinearLayout) findViewById(R.id.ll_line_01);
		ll_line_02 = (LinearLayout) findViewById(R.id.ll_line_02);
		ll_line_03 = (LinearLayout) findViewById(R.id.ll_line_03);
		
		pvTime = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
		pvTime.setTime(new Date());
		pvTime.setCyclic(true);
		pvTime.setCancelable(true);
		
		
	}

	@Override
	public void initData() {
		Title title = getTitleClass();
		title.tv_title_right.setOnClickListener(this);
		// 根据数据设置原始的布局
		isSmoke(false);
		isDrink(false);
		modelUser = mApp.getUser();
		if (modelUser.getSex().equals("1")) {
			isGirl(false);
		} else {
			isGirl(true);
		}
		
		if(history != null){
			//获取数据
			med_history = history.getMed_history();
			allergy_history = history.getAllergy_history();
			per_history = history.getPer_history();
			eating_habit = history.getEating_habit();
			smoke = history.getSmoke();
			drink = history.getDrink();
			menarche_age = history.getMenarche_age();
			menarche_etime = history.getMenarche_etime();
			amenorrhoea_age = history.getAmenorrhoea_age();
			childs = history.getChilds();
			family_history = history.getFamily_history();
			sex = history.getSex();
			//设置数据
			if(sex.equals("0")){
				rl_first.setVisibility(View.GONE);
				rl_last_time.setVisibility(View.GONE);
				rl_stop_yuejin.setVisibility(View.GONE);
				ll_line_01.setVisibility(View.GONE);
				ll_line_02.setVisibility(View.GONE);
				ll_line_03.setVisibility(View.GONE);
			} else{
				rl_first.setVisibility(View.VISIBLE);
				rl_last_time.setVisibility(View.VISIBLE);
				rl_stop_yuejin.setVisibility(View.VISIBLE);
				ll_line_01.setVisibility(View.VISIBLE);
				ll_line_02.setVisibility(View.VISIBLE);
				ll_line_03.setVisibility(View.VISIBLE);
			}
			et_name.setText(med_history);
			et_allergy_name.setText(allergy_history);
			et_single_name.setText(per_history);
			tv_eat_name.setText(eating_habit);
			et_first.setText(menarche_age);
			tv_last_time_name.setText(menarche_etime);
			et_stop_yuejins.setText(amenorrhoea_age);
			et_childs_num.setText(childs);
			et_family_historys.setText(family_history);
			if(smoke.equals("0")){
				tv_smoke_name.setText("不抽烟");
				isSmoke(false);
			} else{
				tv_smoke_name.setText("抽烟");
				smoke_age = history.getSmoke_age();
				smoke_time = history.getSmoke_time();
				stop_smoke = history.getStop_smoke();
				isSmoke(true);
				et_smoke_year.setText(smoke_age);
				et_smoke_gen.setText(smoke_time);
				if(stop_smoke.equals("0")){
					tv_stop_smoke_name.setText("未戒烟");
					isStopSmoke(false);
				} else{
					tv_stop_smoke_name.setText("已戒烟");
					isStopSmoke(true);
					stop_smoke_time = history.getStop_smoke_time();
					tv_stop_smoke_time_name.setText(stop_smoke_time);
					stop_smoke_time = DateUtil.dateToStr2(stop_smoke_time);
				}
			}
			if(drink.equals("0")){
				tv_drink_name.setText("不饮酒");
				isDrink(false);
			} else{
				tv_drink_name.setText("饮酒");
				isDrink(true);
				drink_age = history.getDrink_age();
				drink_consumption = history.getDrink_consumption();
				stop_drink = history.getStop_drink();
				et_drink_year.setText(drink_age);
				et_drink_much.setText(drink_consumption);
				if(stop_drink.equals("0")){
					tv_stop_drink_name.setText("未戒酒");
					isStopDrink(false);
				} else{
					tv_stop_drink_name.setText("已戒酒");
					isStopDrink(true);
					stop_drink_time = history.getStop_drink_time();
					tv_stop_drink_name_time.setText(stop_drink_time);
					stop_drink_time = DateUtil.dateToStr2(stop_drink_time);
				}
			}
		}
	}

	@Override
	public void initListener() {
		rl_eat.setOnClickListener(this);
		rl_smoke.setOnClickListener(this);
		rl_stop_smoke.setOnClickListener(this);
		rl_stop_smoke_time.setOnClickListener(this);
		rl_drink.setOnClickListener(this);
		rl_stop_drink.setOnClickListener(this);
		rl_last_time.setOnClickListener(this);
		rl_children.setOnClickListener(this);
		rl_stop_drink_time.setOnClickListener(this);
		pvTime.setOnTimeSelectListener(new OnTimeSelectListener() {
			
			@Override
			public void onTimeSelect(Date date) {
				if(timeType.equals(Config.TYPE_LAST_TIME)){
					menarche_etime = DateUtil.DateToStamp(date);
					tv_last_time_name.setText(DateUtil.strTodate(menarche_etime));
				} else if(timeType.equals(Config.TYPE_STOP_SMOKE_TIME)){
					stop_smoke_time = DateUtil.DateToStamp(date);
					tv_stop_smoke_time_name.setText(DateUtil.strTodate(stop_smoke_time));
				} else if(timeType.equals(Config.TYPE_STOP_DRINK_TIME)){
					stop_drink_time = DateUtil.DateToStamp(date);
					tv_stop_drink_name_time.setText(DateUtil.strTodate(stop_drink_time));
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_title_right:
			setEditContent();
			hideKeyBoard();
			if (checkTheContent()) {
				ModelAddHistoryCase historyCase = addDataToModel();
				Log.i("addHistory", historyCase.toString());
				sendRequest(mApp.getMedRecordImpl().saveHistory(historyCase),
						ModelMsg.class, REQUEST_GET);
			}
			break;

		case R.id.rl_eat:
			hideKeyBoard();
			PopEatingHabit habit = new PopEatingHabit(this, null, this);
			habit.showPop(rl_eat, Gravity.BOTTOM, 0, 0);
			break;

		case R.id.rl_smoke:
			hideKeyBoard();
			PopChooseSmoke chooseSmoke = new PopChooseSmoke(this, null, this);
			chooseSmoke.showPop(rl_smoke, Gravity.BOTTOM, 0, 0);
			break;
		case R.id.rl_stop_smoke:
			hideKeyBoard();
			PopChooseStopSmoke chooseStopSmoke = new PopChooseStopSmoke(this,
					null, this);
			chooseStopSmoke.showPop(rl_stop_smoke, Gravity.BOTTOM, 0, 0);
			break;
		case R.id.rl_stop_smoke_time:
			hideKeyBoard();
			timeType = Config.TYPE_STOP_SMOKE_TIME;
			pvTime.show();
			/*PopDatePicker datePicker = new PopDatePicker(this, null, this);
			datePicker.setType(Config.TYPE_STOP_SMOKE_TIME);
			datePicker.showPop(rl_stop_smoke_time, Gravity.BOTTOM, 0, 0);*/
			break;
		case R.id.rl_drink:
			hideKeyBoard();
			PopChooseDrink chooseDrink = new PopChooseDrink(this, null, this);
			chooseDrink.showPop(rl_drink, Gravity.BOTTOM, 0, 0);
			break;

		case R.id.rl_stop_drink:
			hideKeyBoard();
			PopChooseStopDrink stopDrink = new PopChooseStopDrink(this, null,
					this);
			stopDrink.showPop(rl_stop_drink, Gravity.BOTTOM, 0, 0);
			break;
		case R.id.rl_stop_drink_time:
			hideKeyBoard();
			timeType = Config.TYPE_STOP_DRINK_TIME;
			pvTime.show();
			/*PopDatePicker stopDrinktime = new PopDatePicker(this, null, this);
			stopDrinktime.setType(Config.TYPE_STOP_DRINK_TIME);
			stopDrinktime.showPop(rl_stop_drink, Gravity.BOTTOM, 0, 0);*/
			break;

		case R.id.rl_last_time:
			hideKeyBoard();
			timeType = Config.TYPE_LAST_TIME;
			pvTime.show();
			/*PopDatePicker lastTime = new PopDatePicker(this, null, this);
			lastTime.setType(Config.TYPE_LAST_TIME);
			lastTime.showPop(rl_last_time, Gravity.BOTTOM, 0, 0);*/
			break;
		case R.id.rl_children:
			
			break;

		}

	}

	@Override
	public Object onResponceSuccess(String str, Class class1) {
		Object object = super.onResponceSuccess(str, class1);
		if (judgeTheMsg(object)) {
			onBackPressed();
		}
		return object;
	}

	@Override
	public Object onPopResult(Object object) {
		if (object instanceof ModelPop) {
			ModelPop modelPop = (ModelPop) object;
			if (modelPop.getType().equals(Config.TYPE_EATING_HABIT)) {
				eating_habit = modelPop.getDataStr();
				tv_eat_name.setText(eating_habit);
			} else if (modelPop.getType().equals(Config.TYPE_SMOKE)) {
				if (modelPop.getDataStr().equals("0")) {
					smoke = "0";
					tv_smoke_name.setText("不抽烟");
					isSmoke(false);
				} else {
					smoke = "1";
					tv_smoke_name.setText("抽烟");
					isSmoke(true);
				}

			} else if (modelPop.getType().equals(Config.TYPE_STOP_SMOKE)) {
				if (modelPop.getDataStr().equals("0")) {
					stop_smoke = "0";
					tv_stop_smoke_name.setText("未戒烟");
					isStopSmoke(false);
				} else {
					stop_smoke = "1";
					tv_stop_smoke_name.setText("已戒烟");
					isStopSmoke(true);
				}

			} else if (modelPop.getType().equals(Config.TYPE_STOP_SMOKE_TIME)) {
				tv_stop_smoke_time_name.setText(modelPop.getDataStr());
				stop_smoke_time = modelPop.getDataStr();

			} else if (modelPop.getType().equals(Config.TYPE_DRINK)) {
				if (modelPop.getDataStr().equals("0")) {
					drink = "0";
					tv_drink_name.setText("不饮酒");
					isDrink(false);
				} else {
					drink = "1";
					tv_drink_name.setText("饮酒");
					isDrink(true);
				}

			} else if (modelPop.getType().equals(Config.TYPE_STOP_DRINK)) {
				if (modelPop.getDataStr().equals("0")) {
					stop_drink = "0";
					tv_stop_drink_name.setText("未戒酒");
					isStopDrink(false);
				} else {
					stop_drink = "1";
					tv_stop_drink_name.setText("已戒酒");
					isStopDrink(true);
				}

			} else if (modelPop.getType().equals(Config.TYPE_STOP_DRINK_TIME)) {
				stop_drink_time = modelPop.getDataStr();
				tv_stop_drink_name_time.setText(stop_drink_time);
			} else if (modelPop.getType().equals(Config.TYPE_LAST_TIME)) {
				menarche_etime = modelPop.getDataStr();
				tv_last_time_name.setText(menarche_etime);
			}

		}
		return super.onPopResult(object);
	}

	/************************ 添加既往史需要的数据 **********************************/
	private String med_history; // 既往病史
	private String allergy_history; // 过敏史
	private String per_history; // 个人史
	private String eating_habit;// 饮食习惯
	private String smoke = "0";// 抽烟 0不抽 1抽
	private String smoke_age;// 抽烟年龄
	private String smoke_time;// 每日抽烟根数
	private String stop_smoke = "1";// 否戒烟 0-未戒 1-已戒
	private String stop_smoke_time;// 戒烟时间
	private String drink = "0";// 是否饮酒 0不饮酒 1饮酒
	private String drink_age;// 饮酒年龄
	private String drink_consumption;// 饮酒量
	private String stop_drink = "1";// 是否戒酒 0-未戒 1-已戒
	private String stop_drink_time;// 戒酒时间
	private String menarche_age;// 初潮年纪
	private String menarche_etime;// 末次月经时间
	private String amenorrhoea_age;// 闭经年龄
	private String childs;// 子女
	private String family_history;// 家族史
	private String sex;

	/**
	 * 设置输入框的内容
	 */
	private void setEditContent() {
		med_history = et_name.getText().toString();
		allergy_history = et_allergy_name.getText().toString();
		per_history = et_single_name.getText().toString();
		childs = et_childs_num.getText().toString();
		smoke_age = et_smoke_year.getText().toString();
		smoke_time = et_smoke_gen.getText().toString();
		drink_age = et_drink_year.getText().toString();
		drink_consumption = et_drink_much.getText().toString();
		menarche_age = et_first.getText().toString();
		amenorrhoea_age = et_stop_yuejins.getText().toString();
		family_history = et_family_historys.getText().toString();

	}
	
	/**
	 * 隐藏软键盘
	 * */
	private void hideKeyBoard(){
		SociaxUIUtils.hideSoftKeyboard(this, et_name);
		SociaxUIUtils.hideSoftKeyboard(this, et_allergy_name);
		SociaxUIUtils.hideSoftKeyboard(this, et_single_name);
		SociaxUIUtils.hideSoftKeyboard(this, et_smoke_year);
		SociaxUIUtils.hideSoftKeyboard(this, et_smoke_gen);
		SociaxUIUtils.hideSoftKeyboard(this, et_drink_year);
		SociaxUIUtils.hideSoftKeyboard(this, et_drink_much);
		SociaxUIUtils.hideSoftKeyboard(this, et_first);
		SociaxUIUtils.hideSoftKeyboard(this, et_stop_yuejins);
		SociaxUIUtils.hideSoftKeyboard(this, et_family_historys);
		SociaxUIUtils.hideSoftKeyboard(this, et_childs_num);
	}

	/**
	 * 检验数据是否存在空，用于需要上传的时候的判断
	 * 
	 * @return
	 */
	private boolean checkTheContent() {
		if (TextUtils.isEmpty(med_history)) {
			ToastUtils.showToast("请输入既往病史");
			return false;
		}
		if (TextUtils.isEmpty(allergy_history)) {
			ToastUtils.showToast("请输入过敏史");
			return false;
		}
		if (TextUtils.isEmpty(per_history)) {
			ToastUtils.showToast("请输入个人史");
			return false;
		}
		if (TextUtils.isEmpty(eating_habit)) {
			ToastUtils.showToast("请选择饮食习惯");
			return false;
		}
		if (TextUtils.isEmpty(smoke)) {
			ToastUtils.showToast("请选择是否抽烟");
			return false;
		}
		if (smoke.equals("1")) {
			if (TextUtils.isEmpty(smoke_age)) {
				ToastUtils.showToast("请输入抽烟年龄");
				return false;
			}
			if (TextUtils.isEmpty(stop_smoke)) {
				ToastUtils.showToast("请选择是否戒烟");
				return false;
			}
			if (stop_smoke.equals("1")) {
				if (TextUtils.isEmpty(stop_smoke_time)) {
					ToastUtils.showToast("选择戒烟年龄");
					return false;
				}
			}
		}
		if (TextUtils.isEmpty(drink)) {
			ToastUtils.showToast("请选择是否饮酒");
			return false;
		}
		if (drink.equals("1")) {
			if (TextUtils.isEmpty(drink_age)) {
				ToastUtils.showToast("请输入饮酒年龄");
				return false;
			}
			if (TextUtils.isEmpty(drink_consumption)) {
				ToastUtils.showToast("请输入饮酒量");
				return false;
			}
			if (TextUtils.isEmpty(stop_drink)) {
				ToastUtils.showToast("请选择是否戒酒");
				return false;
			}
			if (stop_drink.equals("1")) {
				if (TextUtils.isEmpty(stop_drink_time)) {
					ToastUtils.showToast("请选择戒酒时间");
					return false;
				}
			}
		}
		if (modelUser.getSex().equals("女")) {
			if (TextUtils.isEmpty(menarche_age)) {
				ToastUtils.showToast("请输入初潮年纪");
				return false;
			}
			if (TextUtils.isEmpty(menarche_etime)) {
				ToastUtils.showToast("请选择末次月经时间");
				return false;
			}
			if (TextUtils.isEmpty(amenorrhoea_age)) {
				ToastUtils.showToast("请输入闭经年龄");
				return false;
			}
		}
		if (TextUtils.isEmpty(childs)) {
			ToastUtils.showToast("请选择子女情况");
			return false;
		}
		if (TextUtils.isEmpty(family_history)) {
			ToastUtils.showToast("请输入家族史");
			return false;
		}
		return true;
	}

	/**
	 * 添加数据到model用于请求发送
	 * 
	 * @return
	 */
	private ModelAddHistoryCase addDataToModel() {
		ModelAddHistoryCase HistoryCase = new ModelAddHistoryCase();
		HistoryCase.setMed_history(med_history);
		HistoryCase.setAllergy_history(allergy_history);
		HistoryCase.setPer_history(per_history);
		HistoryCase.setEating_habit(eating_habit);
		HistoryCase.setSmoke(smoke);
		HistoryCase.setSmoke_age(smoke_age);
		HistoryCase.setSmoke_time(smoke_time);
		HistoryCase.setStop_smoke(stop_smoke);
		HistoryCase.setStop_smoke_time(stop_smoke_time);
		HistoryCase.setDrink(drink);
		HistoryCase.setDrink_age(drink_age);
		HistoryCase.setDrink_consumption(drink_consumption);
		HistoryCase.setStop_drink(stop_drink);
		HistoryCase.setStop_drink_time(stop_drink_time);
		HistoryCase.setMenarche_age(menarche_age);
		HistoryCase.setMenarche_etime(menarche_etime);
		HistoryCase.setAmenorrhoea_age(amenorrhoea_age);
		HistoryCase.setChilds(childs);
		HistoryCase.setFamily_history(family_history);
		return HistoryCase;
	}

	/*************************** 增加交互 ***********************************/
	/**
	 * 判断是否抽烟
	 * 
	 * @param flag
	 *            true 表示抽烟，false表示不抽烟
	 */
	private void isSmoke(boolean flag) {
		if (flag) {
			smoke = "1";
			rl_smoke_year.setVisibility(View.VISIBLE);
			rl_smoke_gen.setVisibility(View.VISIBLE);
			rl_stop_smoke.setVisibility(View.VISIBLE);
			rl_stop_smoke_time.setVisibility(View.VISIBLE);
		} else {
			smoke = "0";
			rl_smoke_year.setVisibility(View.GONE);
			rl_smoke_gen.setVisibility(View.GONE);
			rl_stop_smoke.setVisibility(View.GONE);
			rl_stop_smoke_time.setVisibility(View.GONE);
		}
	}

	/**
	 * 是否停止抽烟
	 * 
	 * @param flag
	 */
	private void isStopSmoke(boolean flag) {
		if (flag) {
			stop_smoke = "1";
			rl_stop_smoke_time.setVisibility(View.VISIBLE);
		} else {
			stop_smoke = "0";
			rl_stop_smoke_time.setVisibility(View.GONE);
		}
	}

	/**
	 * 是否饮酒
	 * 
	 * @param flag
	 */
	private void isDrink(boolean flag) {
		if (flag) {
			drink = "1";
			rl_drink_year.setVisibility(View.VISIBLE);
			rl_drink_much.setVisibility(View.VISIBLE);
			rl_stop_drink.setVisibility(View.VISIBLE);
			rl_stop_drink_time.setVisibility(View.VISIBLE);
		} else {
			drink = "0";
			rl_drink_year.setVisibility(View.GONE);
			rl_drink_much.setVisibility(View.GONE);
			rl_stop_drink.setVisibility(View.GONE);
			rl_stop_drink_time.setVisibility(View.GONE);
		}
	}

	/**
	 * 是否停止抽烟
	 * 
	 * @param flag
	 */
	private void isStopDrink(boolean flag) {
		if (flag) {
			stop_drink = "1";
			rl_stop_drink_time.setVisibility(View.VISIBLE);
		} else {
			stop_drink = "0";
			rl_stop_drink_time.setVisibility(View.GONE);
		}
	}

	/**
	 * 是否是女生
	 * 
	 * @param flag
	 */
	private void isGirl(boolean flag) {
		if (flag) {
			rl_first.setVisibility(View.VISIBLE);
			rl_last_time.setVisibility(View.VISIBLE);
			rl_stop_yuejin.setVisibility(View.VISIBLE);
		} else {
			rl_first.setVisibility(View.GONE);
			rl_last_time.setVisibility(View.GONE);
			rl_stop_yuejin.setVisibility(View.GONE);
		}
	}
	/*************************** 增加交互 end ***********************************/
}
