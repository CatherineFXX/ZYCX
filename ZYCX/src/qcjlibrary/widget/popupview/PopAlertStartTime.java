package qcjlibrary.widget.popupview;

import qcjlibrary.config.Config;
import qcjlibrary.model.ModelPop;
import qcjlibrary.model.ModelUser;
import qcjlibrary.util.DateUtil;
import qcjlibrary.widget.popupview.base.PopView;
import qcjlibrary.widget.wheelview.ArrayWheelAdapter;
import qcjlibrary.widget.wheelview.WheelView;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.zhiyicx.zycx.R;

/**
 * author：tanxiaomin 
 * time：上午11:30:44 
 * 类描述：开始日期提醒弹出框
 */

public class PopAlertStartTime extends PopView {
	private TextView tv_date_cancle;
	private TextView tv_date_sure;
	private WheelView wv_date_year;
	private WheelView wv_date_month;
	private WheelView wv_date_day;
	/***************************************/
	public static String[] mYear = new String[70];
	public static String[] mMonth = new String[12];
	public static String[] mDay = new String[31];

	private String mCurrentYear; // 当前的年
	private String mCurrentMonth;// 当前的月
	private String mCurrentDay;// 当前的日

	public PopAlertStartTime(Activity activity, Object object,
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
		genateYearMouthDay();
		wv_date_year.setVisibleItems(5);
		wv_date_year.setCyclic(true);
		wv_date_year.setAdapter(new ArrayWheelAdapter<String>(mYear));
		wv_date_month.setVisibleItems(5);
		wv_date_month.setCyclic(true);
		wv_date_month.setAdapter(new ArrayWheelAdapter<String>(mMonth));
		wv_date_day.setVisibleItems(5);
		wv_date_day.setCyclic(true);
		wv_date_day.setAdapter(new ArrayWheelAdapter<String>(mDay));
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
				ModelPop data = new ModelPop();
				data.setType(Config.TYPE_DATE);
				data.setDataStr(pickTime.trim());
				listener.onPopResult(data);
				mPopWindow.dismiss();
			}
		});
	}

	/**
	 * 生成年月日
	 */
	private void genateYearMouthDay() {
		for (int i = 0; i < mYear.length; i++) {
			mYear[i] = "  " + (2015 + i) + "  ";
		}
		for (int i = 0; i < mMonth.length; i++) {
			mMonth[i] = "  " + ((1 + i)) + "  ";
		}
		for (int i = 0; i < mDay.length; i++) {
			mDay[i] = "  " + ((1 + i)) + "  ";
		}
	}

	/**
	 * 计算日期
	 * 
	 * @return
	 */
	private String cacluteDate() {
		mCurrentYear = mYear[wv_date_year.getCurrentItem()];
		mCurrentYear = mCurrentYear.trim();
		mCurrentMonth = mMonth[wv_date_month.getCurrentItem()];
		mCurrentMonth = mCurrentMonth.trim();
		mCurrentDay = mDay[wv_date_day.getCurrentItem()];
		mCurrentDay = mCurrentDay.trim();
		return mCurrentYear + "-" + mCurrentMonth + "-" + mCurrentDay;
	}

}
