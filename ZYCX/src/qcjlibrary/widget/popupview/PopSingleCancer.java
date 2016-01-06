package qcjlibrary.widget.popupview;

import qcjlibrary.activity.base.BaseActivity;
import qcjlibrary.model.ModelExperienceDetailInfor;
import qcjlibrary.widget.RoundImageView;
import qcjlibrary.widget.popupview.base.PopView;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zhiyicx.zycx.R;

/**
 * author：qiuchunjia time：下午4:24:58 类描述：这个类是实现
 *
 */

public class PopSingleCancer extends PopView {

	private RoundImageView iv_cancer_icon;
	private TextView tv_cancer_name;
	private TextView tv_cancer_bangzhuvalue;
	private TextView tv_cancer_membervalue;
	private TextView tv_cancer_experiencevalue;
	private TextView tv_cancer_content;
	private ImageView iv_bottom_arrow;

	public PopSingleCancer(Activity activity, Object object,
			PopResultListener resultListener) {
		super(activity, object, resultListener);
	}

	@Override
	public int getLayoutId() {
		return R.layout.pop_single_cancer;
	}

	@Override
	public void initPopView() {
		iv_cancer_icon = (RoundImageView) findViewbyId(R.id.iv_cancer_icon);
		tv_cancer_name = (TextView) findViewbyId(R.id.tv_cancer_name);
		tv_cancer_bangzhuvalue = (TextView) findViewbyId(R.id.tv_cancer_bangzhuvalue);
		tv_cancer_membervalue = (TextView) findViewbyId(R.id.tv_cancer_membervalue);
		tv_cancer_experiencevalue = (TextView) findViewbyId(R.id.tv_cancer_experiencevalue);
		tv_cancer_content = (TextView) findViewbyId(R.id.tv_cancer_content);
		iv_bottom_arrow = (ImageView) findViewbyId(R.id.iv_bottom_arrow);

	}

	@Override
	public void initPopData(Object object) {
		if (object instanceof ModelExperienceDetailInfor) {
			ModelExperienceDetailInfor infor = (ModelExperienceDetailInfor) object;
			if (mActivity instanceof BaseActivity) {
				BaseActivity activity = (BaseActivity) mActivity;
				activity.mApp.displayImage(infor.getLogo(), iv_cancer_icon);
			}
			tv_cancer_name.setText(infor.getWeiba_name());
			// tv_cancer_bangzhuvalue.setText(infor.getWeiba_name());
			tv_cancer_membervalue.setText(infor.getFollower_count());
			tv_cancer_experiencevalue.setText(infor.getThread_count());
			tv_cancer_content.setText(infor.getIntro());
		}

	}
	@Override
	public void setAnimationStyle(PopupWindow popupWindow) {
		popupWindow.setAnimationStyle(R.style.pop_anim_style_down_and_up);
	}

	@Override
	public void setPopLisenter(PopResultListener listener) {
		iv_bottom_arrow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mPopWindow.dismiss();
			}
		});

	}

}
