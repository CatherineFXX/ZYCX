package qcjlibrary.fragment;


import java.util.List;

import qcjlibrary.activity.FoodCategoryActivity;
import qcjlibrary.fragment.base.BaseFragment;
import qcjlibrary.model.ModelFood;
import qcjlibrary.model.ModelFoodCategory;
import qcjlibrary.model.ModelFoodIndex;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.zhiyicx.zycx.R;

/**
 * author：qiuchunjia time：上午11:30:54 类描述：这个类是实现
 *
 */

public class FragementFoodWay extends BaseFragment {
	private LinearLayout ll_food; // 用于动态添加布局文件
	private RelativeLayout rl_cancer;
	private RelativeLayout rl_cancer_category;

	private ModelFoodIndex mFoodIndex;


	@Override
	public void initIntentData() {
		// TODO Auto-generated method stub

	}

	@Override
	public int getLayoutId() {
		return R.layout.fragment_food_way;
	}

	@Override
	public void initView() {
		ll_food = (LinearLayout) findViewById(R.id.ll_food);
		rl_cancer = (RelativeLayout) findViewById(R.id.rl_cancer);
		rl_cancer_category = (RelativeLayout) findViewById(R.id.rl_cancer_category);

	}

	@Override
	public void initListener() {
		rl_cancer.setOnClickListener(this);
		rl_cancer_category.setOnClickListener(this);
	}

	@Override
	public void initData() {

		sendRequest(mApp.getFoodImpl().index(), ModelFoodIndex.class,
				REQUEST_GET);
	}

	@Override
	public Object onResponceSuccess(String str, Class class1) {
		Object object = super.onResponceSuccess(str, class1);
		if (object instanceof ModelFoodIndex) {
			mFoodIndex = (ModelFoodIndex) object;
			addDataToView(mFoodIndex.getFoodSide());
		} else {
			judgeTheMsg(object);
		}
		return object;
	}

	/**
	 * 添加数据到界面中
	 * 
	 * @param foods
	 */
	private void addDataToView(List<ModelFoodCategory> foods) {
		View view = null;
		if (foods != null) {
			for (int i = 0; i < foods.size(); i++) {
				final ModelFoodCategory foodCategory = foods.get(i);
				view = mInflater.inflate(R.layout.item_food_choose, null);
				TextView tv_foot_name = (TextView) view
						.findViewById(R.id.tv_foot_name);
				TextView tv_number = (TextView) view
						.findViewById(R.id.tv_number);
				tv_number.setText(foodCategory.getCount() + "");
				tv_foot_name.setText(foodCategory.getClass_name() + "");
				view.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						mApp.startActivity_qcj(getActivity(),
								FoodCategoryActivity.class,
								mActivity.sendDataToBundle(foodCategory, null));
					}
				});
				ll_food.addView(view);
			}
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_cancer:

			break;

		case R.id.rl_cancer_category:

			break;
		}

	}

}
