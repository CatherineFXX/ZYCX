package qcjlibrary.widget.popupview;

import java.util.ArrayList;

import com.renn.rennsdk.param.GetAppParam;
import com.umeng.socialize.utils.Log;
import com.zhiyicx.zycx.R;
import com.zhiyicx.zycx.sociax.android.Thinksns;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import qcjlibrary.activity.MedicineEditNotifyActivity;
import qcjlibrary.config.Config;
import qcjlibrary.model.ModelPop;
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
	private String item_1 = "";
	private String item_2 = "";
	private String item_3 = "";
	private String item_4 = "";

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
	}

	@Override
	public void initPopData(Object object) {
		timeList = new ArrayList<View>();
		time = new StringBuffer();
		inflate = LayoutInflater.from(mActivity);
		childItemOne = inflate.inflate(R.layout.item_alert_time, null);
		childItemTwo = inflate.inflate(R.layout.item_alert_time, null);
		childItemThree = inflate.inflate(R.layout.item_alert_time, null);
		childItemFour = inflate.inflate(R.layout.item_alert_time, null);
		timeList.add(childItemOne);
		timeList.add(childItemTwo);
		timeList.add(childItemThree);
		timeList.add(childItemFour);
		ll_alert_time_list.addView(childItemOne);
		count = 1;
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
					tv_alert_count.setText("提醒" + (index + 1) + "次");
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
					tv_alert_count.setText("提醒" + (index - 1) + "次");
					count = index - 1;
					index --;
				}
			}
		});
		btn_alert_time_check.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ModelPop data = new ModelPop();
				String timeStr = "8: 00";
				time.append(item_1).append(item_2).append(item_3).append(item_4);
				if (time.length() > 4) {
					timeStr = (String) time.subSequence(0, time.length() - 2);
				}
				data.setType(Config.TYPE_TIME_LIST);
				data.setDataStr(timeStr+","+count+"次");
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
					PopAlertTime alertTime = new PopAlertTime(Thinksns.medicineAct, null, listener);
					alertTime.setPopalerttime(PopAlertTimeList.this);
					alertTime.showPop(childItem, Gravity.BOTTOM, 0, 0);
					tv_alert_time = (TextView) childItem.findViewById(R.id.tv_alert_time);
					flag = timeList.indexOf(childItem);
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
		// TODO
		/** 获得到返回数据后将本类的PopWindow展示出来**/
		PopAlertTimeList.this.showPop(btn_alert_time_check, Gravity.BOTTOM, 0, 0);
		switch (flag) {
		case 0:
			item_1 = setTime(object);
			break;
		case 1:
			item_2 = setTime(object);
			break;
		case 2:
			item_3 = setTime(object);
			break;
		case 3:
			item_4 = setTime(object);
			break;

		default:
			break;
		}
		tv_alert_time.setText(object.toString());
	}
	
	private String setTime(Object object){
		if(object.toString() != null){
			return object.toString()+"; ";
		}
		return "";
	}
}
