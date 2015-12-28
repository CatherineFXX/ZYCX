package qcjlibrary.fragment;



import java.util.List;

import qcjlibrary.activity.FoodCategoryActivity;
import qcjlibrary.fragment.base.BaseFragment;
import qcjlibrary.model.ModelFood;

import qcjlibrary.model.ModelFoodIndex;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiyicx.zycx.R;

/**
 * author：qiuchunjia time：上午11:30:54 类描述：这个类是实现
 *
 */

public class FragementFood extends BaseFragment {
	private LinearLayout ll_food; // 用于动态添加布局文件

	private ModelFoodIndex mFoodIndex;

	@Override
	public void initIntentData() {


	}

	@Override
	public int getLayoutId() {
		return R.layout.fragment_food_way;
	}

	@Override
	public void initView() {
		ll_food = (LinearLayout) findViewById(R.id.ll_food);
		findViewById(R.id.rl_cancer_category).setVisibility(View.GONE);
		findViewById(R.id.rl_cancer).setVisibility(View.GONE);
	}

	@Override
	public void initListener() {
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
			addDataToView(mFoodIndex.getFood());
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
	private void addDataToView(List<ModelFood> foods) {
		View view = null;
		if (foods != null) {
			for (int i = 0; i < foods.size(); i++) {
				final ModelFood food = foods.get(i);
				view = mInflater.inflate(R.layout.item_food_choose, null);
				TextView tv_foot_name = (TextView) view
						.findViewById(R.id.tv_foot_name);
				TextView tv_number = (TextView) view
						.findViewById(R.id.tv_number);
				tv_number.setText(food.getCount() + "");
				tv_foot_name.setText(food.getType_name() + "");
				view.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						mApp.startActivity_qcj(getActivity(),
								FoodCategoryActivity.class,
								mActivity.sendDataToBundle(food, null));
					}
				});
				ll_food.addView(view);
			}
		}

	}

	@Override
	public void onClick(View v) {
	}
}
