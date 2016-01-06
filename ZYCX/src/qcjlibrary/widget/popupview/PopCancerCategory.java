package qcjlibrary.widget.popupview;

import java.util.List;

import qcjlibrary.activity.CancerCategoryActivity;
import qcjlibrary.activity.base.BaseActivity;
import qcjlibrary.adapter.GridViewAdapter;
import qcjlibrary.model.base.Model;
import qcjlibrary.util.UIUtils;
import qcjlibrary.widget.popupview.base.PopView;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zhiyicx.zycx.R;

/**
 * author：qiuchunjia time：下午5:19:54 类描述：这个类是实现
 *
 */

public class PopCancerCategory extends PopView {
	private GridView gv_category;
	private GridViewAdapter mAdapter;

	public PopCancerCategory(Activity activity, Object object,
			PopResultListener resultListener) {
		super(activity, object, resultListener);
	}

	@Override
	public int getLayoutId() {
		return R.layout.pop_cancer_category;
	}

	@Override
	public void initPopView() {
		gv_category = (GridView) findViewbyId(R.id.gv_category);

	}

	@Override
	public PopupWindow setPopWidthAndHeight(View dataView) {
		PopupWindow popupWindow = new PopupWindow(dataView,
				UIUtils.getWindowWidth(mActivity) / 5 * 3,
				LayoutParams.MATCH_PARENT);
		return popupWindow;
	}

	@Override
	public void initPopData(Object object) {
		if (object instanceof List<?>) {
			@SuppressWarnings("unchecked")
			List<Model> list = (List<Model>) object;
			mAdapter = new GridViewAdapter(mActivity, list);
			gv_category.setAdapter(mAdapter);
			gv_category.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					if (view instanceof TextView) {
						TextView textView = (TextView) view;
						textView.setTextColor(mActivity.getResources()
								.getColor(R.color.text_green));
					}
					Model model = (Model) parent.getItemAtPosition(position);
					if (mActivity instanceof BaseActivity) {
						BaseActivity activity = ((BaseActivity) mActivity);
						Bundle data = activity.sendDataToBundle(model, null);
						activity.mApp.startActivity_qcj(activity,
								CancerCategoryActivity.class, data);
					}
					mPopWindow.dismiss();
				}
			});
		}
	}
	@Override
	public void setAnimationStyle(PopupWindow popupWindow) {
		popupWindow.setAnimationStyle(R.style.pop_anim_style_left_and_right);
	}
	@Override
	public void setPopLisenter(PopResultListener listener) {
		// TODO Auto-generated method stub

	}

}
