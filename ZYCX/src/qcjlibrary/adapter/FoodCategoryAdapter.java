package qcjlibrary.adapter;

import qcjlibrary.activity.base.BaseActivity;
import qcjlibrary.adapter.base.BAdapter;
import qcjlibrary.adapter.base.ViewHolder;
import qcjlibrary.fragment.base.BaseFragment;

import qcjlibrary.model.ModelFoodSearch;
import qcjlibrary.model.ModelFoodSearch0;
import qcjlibrary.model.ModelFoodSearch1;
import qcjlibrary.model.ModelFoodSearchIndex;
import qcjlibrary.model.base.Model;
import android.util.Log;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhiyicx.zycx.R;

/**
 * author：qiuchunjia time：下午5:06:10
 * 
 * 类描述：这个类是实现专家提问列表
 *
 */

public class FoodCategoryAdapter extends BAdapter {

	private ModelFoodSearch mSearch;

	public FoodCategoryAdapter(BaseActivity activity, ModelFoodSearch search) {
		super(activity, null);
		this.mSearch = search;
	}

	public FoodCategoryAdapter(BaseFragment fragment, ModelFoodSearch search) {
		super(fragment, null);
		this.mSearch = search;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_food_category, null);
			initView(convertView, holder);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		bindDataToView(holder, position);
		return convertView;
	}

	private void bindDataToView(ViewHolder holder, int position) {
		if (holder != null) {

			Model model = mList.get(position);
			if (model instanceof ModelFoodSearch0) {
				ModelFoodSearch0 search0 = (ModelFoodSearch0) model;
				mApp.displayImage(search0.getImgSrc(), holder.iv_food_icon);
				holder.tv_food_name.setText(search0.getFood_name());
				holder.tv_food_function.setText(search0.getFood_effect());
				holder.tv_cancer.setText(search0.getFood_forcancer());
			} else {
				ModelFoodSearch1 search1 = (ModelFoodSearch1) model;
				Log.i("ModelFoodSearch1", search1.toString());
				mApp.displayImage(search1.getImgSrc(), holder.iv_food_icon);
				holder.tv_food_name.setText(search1.getSide_name());
				holder.tv_food_function.setText(search1.getGongxiao());
				holder.tv_cancer.setText(search1.getFangzhi_cancer());
			}

		}
	}

	/**
	 * 初始化布局
	 * 
	 * @param convertView
	 * @param holder
	 */
	private void initView(View convertView, ViewHolder holder) {
		if (convertView != null && holder != null) {
			holder.iv_food_icon = (ImageView) convertView
					.findViewById(R.id.iv_food_icon);
			holder.tv_food_name = (TextView) convertView
					.findViewById(R.id.tv_food_name);
			holder.tv_food_function = (TextView) convertView
					.findViewById(R.id.tv_food_function);
			holder.tv_cancer = (TextView) convertView
					.findViewById(R.id.tv_cancer);
		}
	}

	@Override
	public void refreshNew() {

		sendRequest(mApp.getFoodImpl().food_search(mSearch),
				ModelFoodSearchIndex.class, 0, REFRESH_NEW);

	}

	@Override
	public void refreshHeader(Model item, int count) {

		// sendRequest(mApp.getFoodImpl().food_search(mSearch),
		// ModelFoodSearchIndex.class, 0, REFRESH_NEW);

	}

	@Override
	public void refreshFooter(Model item, int count) {

		// sendRequest(mApp.getFoodImpl().food_search(mSearch),
		// ModelFoodSearchIndex.class, 0, REFRESH_NEW);

	}

	@Override
	public int getTheCacheType() {

		return 0;
	}

	@Override
	public Object getReallyList(Object object, Class type2) {
		if (object instanceof ModelFoodSearchIndex) {
			ModelFoodSearchIndex index = (ModelFoodSearchIndex) object;
			if (mSearch.getState() == 0) {
				return index.getFoodList();
			} else {
				return index.getSideList();
			}
		}
		return null;
	}

}
