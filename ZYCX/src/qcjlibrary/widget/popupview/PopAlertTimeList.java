package qcjlibrary.widget.popupview;

import java.util.ArrayList;
import java.util.Date;

import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.TimePickerView.OnTimeSelectListener;
import com.renn.rennsdk.param.GetAppParam;
import com.umeng.socialize.utils.Log;
import com.zhiyicx.zycx.R;
import com.zhiyicx.zycx.sociax.android.Thinksns;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import qcjlibrary.activity.MedicineEditNotifyActivity;
import qcjlibrary.config.Config;
import qcjlibrary.model.ModelPop;
import qcjlibrary.util.DateUtil;
import qcjlibrary.util.L;
import qcjlibrary.widget.popupview.base.PopView;

public class PopAlertTimeList extends PopView {

	/**
	 * author：tanxiaomin 
	 * time：12-23 
	 * 类描述：时间列表提醒弹出框
	 */
	
	private TextView tv_alert_count;
	private Button btn_alert_dec;
	private Button btn_alert_add;
	private LinearLayout ll_alert_time_list;
	private Button btn_alert_time_check;
	private LayoutInflater inflate;
	private View childItemOne;
	private View childItemTwo;
	private View childItemThree;
	private View childItemFour;
	private TextView tv_alert_time;
	/** 子项控件 **/
	/** 存储提醒时间的子项 **/
	private ArrayList<View> timeList;
	/** 设置的时间的位置 **/
	private int index;
	private StringBuffer time;
	/** 提醒次数**/
	private int count;
	/** 返回数据的Item的标志**/
	private int flag;
	/** 四个子选项返回的值**/
	private String item_1 = "8:00,";
	private String item_2 = "8:00,";
	private String item_3 = "8:00,";
	private String item_4 = "8:00,";
	private TextView tv1;
	private TextView tv2;
	private TextView tv3;
	private TextView tv4;
	/**时分时间选择框**/
	private TimePickerView pvTimeHour;

	public PopAlertTimeList(Activity activity, Object object, PopResultListener resultListener) {
		super(activity, object, resultListener);
	}

	@Override
	public int getLayoutId() {
		return R.layout.pop_alert_timelist;
	}

	@Override
	public void initPopView() {
		tv_alert_count = (TextView) findViewbyId(R.id.tv_alert_count);
		btn_alert_dec = (Button) findViewbyId(R.id.btn_alert_dec);
		btn_alert_add = (Button) findViewbyId(R.id.btn_alert_add);
		ll_alert_time_list = (LinearLayout) findViewbyId(R.id.ll_alert_time_list);
		btn_alert_time_check = (Button) findViewbyId(R.id.btn_alert_time_check);
		pvTimeHour = new TimePickerView(mActivity, TimePickerView.Type.HOURS_MINS);
        pvTimeHour.setTime(new Date());
        pvTimeHour.setCyclic(false);
        pvTimeHour.setCancelable(true);
	}

	@SuppressLint("InflateParams")
	@Override
	public void initPopData(Object object) {
		timeList = new ArrayList<View>();
		inflate = LayoutInflater.from(mActivity);
		//四个时间选项布局
		childItemOne = inflate.inflate(R.layout.item_alert_time, null);
		childItemTwo = inflate.inflate(R.layout.item_alert_time, null);
		childItemThree = inflate.inflate(R.layout.item_alert_time, null);
		childItemFour = inflate.inflate(R.layout.item_alert_time, null);
		tv1 = (TextView) childItemOne.findViewById(R.id.tv_alert_time);
		tv2 = (TextView) childItemTwo.findViewById(R.id.tv_alert_time);
		tv3 = (TextView) childItemThree.findViewById(R.id.tv_alert_time);
		tv4 = (TextView) childItemFour.findViewById(R.id.tv_alert_time);
		//将四个布局添加到list中
		timeList.add(childItemOne);
		timeList.add(childItemTwo);
		timeList.add(childItemThree);
		timeList.add(childItemFour);
		//默认至少有一个时间
		ll_alert_time_list.addView(childItemOne);
		//如果是修改时间，则将之前的数据添加上去
		if(object != null && !object.equals("null-1")){
			String timeAndCount = object.toString();
			time = new StringBuffer();
			time.append(timeAndCount.split("-")[0]);
			count = Integer.parseInt(timeAndCount.split("-")[1]);
			for (int i = 1; i < count; i++) {
				ll_alert_time_list.addView(timeList.get(i));
			}
			String[] timeArr = timeAndCount.split("-")[0].split(",");
			try{
				tv1.setText(timeArr[0]);
				item_1 = timeArr[0]+",";
				tv2.setText(timeArr[1]);
				item_2 = timeArr[1]+",";
				tv3.setText(timeArr[2]);
				item_3 = timeArr[2]+",";
				tv4.setText(timeArr[3]);
				item_4 = timeArr[3]+",";
			}catch(Exception e){
				
			}
			tv_alert_count.setText((count) + "次");
		} else{
			count = 1;
			tv1.setText("8:00");
		}
	}

	@Override
	public void setPopLisenter(final PopResultListener listener) {
		
		btn_alert_add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (index < 4) {
					if(index == 0){
						index = 1;
					}
					ll_alert_time_list.addView(timeList.get(index));
					tv_alert_count.setText((index + 1) + "次");
					count = index + 1;
					index++;
				}
			}
		});
		btn_alert_dec.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (index > 1) {
					ll_alert_time_list.removeView(timeList.get(index - 1));
					tv_alert_count.setText((index - 1) + "次");
					count = index - 1;
					index --;
				}
			}
		});
		
		/**
		 * 子项选择的监听器
		 * */
		pvTimeHour.setOnTimeSelectListener(new OnTimeSelectListener() {
			
			@Override
			public void onTimeSelect(Date date) {
				switch (flag) {
				case 0:
					item_1 = DateUtil.strTodate4(DateUtil.DateToStamp(date));
					tv1.setText(item_1);
					break;
				case 1:
					item_2 = DateUtil.strTodate4(DateUtil.DateToStamp(date));
					tv2.setText(item_2);
					break;
				case 2:
					item_3 = DateUtil.strTodate4(DateUtil.DateToStamp(date));
					tv3.setText(item_3);
					break;
				case 3:
					item_4 = DateUtil.strTodate4(DateUtil.DateToStamp(date));
					tv4.setText(item_4);
					break;
				default:
					break;
				}
				/** 获得到返回数据后将本类的PopWindow展示出来**/
				showPop(btn_alert_time_check, Gravity.BOTTOM, 0, 0);
			}
		});
		btn_alert_time_check.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ModelPop data = new ModelPop();
				String timeStr = "8:00";
				time = new StringBuffer();
				item_1 = tv1.getText().toString()+",";
				time.append(item_1);
				if(count > 1){
					item_2 = tv2.getText().toString()+",";
					time.append(item_2);
					if(count > 2){
						item_3 = tv3.getText().toString()+",";
						time.append(item_3);
						if(count > 3){
							item_4 = tv4.getText().toString()+",";
							time.append(item_4);
						}
					}
				}
				if (!time.equals("") && time.length() > 2) {
					timeStr = (String) time.subSequence(0, time.length() - 1);
				} else{
					count = 1;
				}
				data.setType(Config.TYPE_TIME_LIST);
				data.setDataStr(timeStr+"-"+count);
				listener.onPopResult(data);
				mPopWindow.dismiss();
			}
		});
		for (int i = 0; i < timeList.size(); i++) {
			final View childItem = timeList.get(i);
			timeList.get(i).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					/** PopWindow必须有一个base activity,因此在弹出子项选择框时需要先关闭当前的选择框**/
					mPopWindow.dismiss();
					flag = timeList.indexOf(childItem);
					pvTimeHour.show();
				}
			});
		}
	}

	/**
	 * 另外一个popview子类用
	 * 
	 * @param object
	 */
	public void setOtherPopView(Object object) {
	}
	
	private String setTime(Object object){
		if(object.toString() != null){
			return object.toString()+",";
		}
		return "";
	}
}
