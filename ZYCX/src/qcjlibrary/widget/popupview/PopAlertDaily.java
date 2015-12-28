package qcjlibrary.widget.popupview;

import java.util.ArrayList;

import com.umeng.socialize.utils.Log;
import com.zhiyicx.zycx.R;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import qcjlibrary.config.Config;
import qcjlibrary.model.ModelPop;
import qcjlibrary.widget.popupview.base.PopView;

public class PopAlertDaily extends PopView {
	
	/**
	 * author：tanxiaomin 
	 * time：上午11:30:44 
	 * 类描述：日期提醒弹出框
	 */

	private RadioGroup rg_alert_daily;
	private RadioButton rb_alert_everyday;
	private RadioButton rb_alert_two;
	private RadioButton rb_alert_three;
	private RadioButton rb_alert_four;
	private RadioButton rb_alert_five;
	private RadioButton rb_alert_sex;
	private RadioButton rb_alert_seven;
	private Button btn_alert_check;
	/** 选中选项的位置**/
	private int index;
	
	public PopAlertDaily(Activity activity, Object object, 
			PopResultListener resultListener) {
		super(activity, object, resultListener);
	}

	@Override
	public int getLayoutId() {
		return R.layout.pop_alert_daily;
	}

	@Override
	public void initPopView() {
		rg_alert_daily = (RadioGroup) findViewbyId(R.id.rg_alert_daily);
		rb_alert_everyday = (RadioButton) findViewbyId(R.id.rb_alert_everyday);
		rb_alert_two = (RadioButton) findViewbyId(R.id.rb_alert_two);
		rb_alert_three = (RadioButton) findViewbyId(R.id.rb_alert_three);
		rb_alert_four = (RadioButton) findViewbyId(R.id.rb_alert_four);
		rb_alert_five = (RadioButton) findViewbyId(R.id.rb_alert_five);
		rb_alert_sex = (RadioButton) findViewbyId(R.id.rb_alert_sex);
		rb_alert_seven = (RadioButton) findViewbyId(R.id.rb_alert_seven);
		btn_alert_check = (Button) findViewbyId(R.id.btn_alert_check);
	}

	@Override
	public void initPopData(Object object) {
	}

	@Override
	public void setPopLisenter(final PopResultListener listener) {
		
		//监听选项改变
		rg_alert_daily.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				//记录当前选择的item的id
				index = checkedId;
			}
		});
		
		
		//监听确认按钮
		btn_alert_check.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				/** 选中的提醒频率**/
				String checkItem = "";
				if(index == rb_alert_everyday.getId()){
					checkItem = "每天";
				} else if(index == rb_alert_two.getId()){
					checkItem = "每2天";
				} else if(index == rb_alert_three.getId()){
					checkItem = "每3天";
				} else if(index == rb_alert_four.getId()){
					checkItem = "每4天";
				} else if(index == rb_alert_five.getId()){
					checkItem = "每5天";
				} else if(index == rb_alert_sex.getId()){
					checkItem = "每6天";
				} else if(index == rb_alert_seven.getId()){
					checkItem = "每7天";
				}
				ModelPop dailyData = new ModelPop();
				dailyData.setType(Config.TYPE_DAILY);
				dailyData.setDataStr(checkItem);
				listener.onPopResult(dailyData);
				mPopWindow.dismiss();
			}
			
		});
	}
	
}
