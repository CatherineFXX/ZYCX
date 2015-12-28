package qcjlibrary.widget.popupview;

import com.zhiyicx.zycx.R;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import qcjlibrary.config.Config;
import qcjlibrary.model.ModelPop;
import qcjlibrary.widget.popupview.base.PopView;
import qcjlibrary.widget.wheelview.ArrayWheelAdapter;
import qcjlibrary.widget.wheelview.WheelView;

public class PopAlertTime extends PopView{

	/**
	 * author：tanxiaomin 
	 * time：上午11:30:44 
	 * 类描述：时间提醒弹出框
	 */
	
	private TextView tv_date_cancle;
	private TextView tv_date_sure;
	private WheelView wv_date_year;
	private WheelView wv_date_month;
	private WheelView wv_date_day;
	
	public static String[] mAm = new String[2];
	public static String[] mHour = new String[12];
	public static String[] mMin = new String[60];
	
	private String mCurrentAm; // 上下午
	private String mCurrentHour;// 当前的小时数
	private String mCurrentMin;// 当前的分钟数
	private String time;
	private PopAlertTimeList mAlertTimeList;
	public PopAlertTime(Activity activity, Object object,
			PopResultListener resultListener) {
		super(activity, object, resultListener);
	}

	@Override
	public int getLayoutId() {
		return R.layout.pop_date_picker;
	}

	@Override
	public void initPopView() {
		tv_date_cancle = (TextView) findViewbyId(R.id.tv_date_cancle);
		tv_date_sure = (TextView) findViewbyId(R.id.tv_date_sure);
		wv_date_year = (WheelView) findViewbyId(R.id.wv_date_year);
		wv_date_month = (WheelView) findViewbyId(R.id.wv_date_month);
		wv_date_day = (WheelView) findViewbyId(R.id.wv_date_day);
	}

	@Override
	public void initPopData(Object object) {
		initData();
		wv_date_year.setVisibleItems(2);
		wv_date_year.setCyclic(true);
		wv_date_year.setAdapter(new ArrayWheelAdapter<String>(mAm));
		wv_date_month.setVisibleItems(5);
		wv_date_month.setCyclic(true);
		wv_date_month.setAdapter(new ArrayWheelAdapter<String>(mHour));
		wv_date_day.setVisibleItems(5);
		wv_date_day.setCyclic(true);
		wv_date_day.setAdapter(new ArrayWheelAdapter<String>(mMin));
	}

	@Override
	public void setPopLisenter(final PopResultListener listener) {
		
		tv_date_cancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mPopWindow.dismiss();
			}
		});
		
		tv_date_sure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String pickTime = cacluteDate();
				//ModelPop data = new ModelPop();
				//data.setType(Config.TYPE_TIME);
				//data.setDataStr(pickTime);
				//listener.onPopResult(data);
				time = pickTime;
				mAlertTimeList.setOtherPopView(time);
				mPopWindow.dismiss();
			}
		});
	}

	private void initData() {
		mAm[0] = "上午";
		mAm[1] = "下午";
		for (int i = 0; i < mHour.length; i++) {
			mHour[i] = "  " + ((i + 1)) + "  ";
		}
		for (int i = 0; i < mMin.length; i++) {
			mMin[i] = "  " + ((i + 1)) + "  ";
		}
	}
	
	/**
	 * 计算日期
	 * 
	 * @return
	 */
	private String cacluteDate() {
		mCurrentAm = mAm[wv_date_year.getCurrentItem()];
		mCurrentAm = mCurrentAm.trim();
		mCurrentHour = mHour[wv_date_month.getCurrentItem()];
		mCurrentHour = mCurrentHour.trim();
		mCurrentMin = mMin[wv_date_day.getCurrentItem()];
		mCurrentMin = mCurrentMin.trim();
		if(mCurrentAm.equals("下午")){
			mCurrentHour = wv_date_month.getCurrentItem() + 13 +"";
		}
		return mCurrentHour + ":" + mCurrentMin;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
	
	public void setPopalerttime(PopAlertTimeList alertTimeList){
		
		this.mAlertTimeList=alertTimeList;
	}
	
}
