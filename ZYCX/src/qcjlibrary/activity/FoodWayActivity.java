package qcjlibrary.activity;

import qcjlibrary.activity.base.BaseActivity;
import qcjlibrary.activity.base.Title;
import qcjlibrary.fragment.FragementFood;
import qcjlibrary.fragment.FragementFoodWay;
import qcjlibrary.model.ModelFoodSearch;
import qcjlibrary.model.ModelFoodSearchIndex;
import qcjlibrary.util.ToastUtils;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhiyicx.zycx.R;

/**
 * author：qiuchunjia time：上午11:39:34 类描述：这个类是实现
 *
 */

public class FoodWayActivity extends BaseActivity {
	private ImageView iv_find;
	private EditText et_find;

	private TextView tv_find;
	private LinearLayout ll_find;
	private RelativeLayout rl_find;
	private ImageView iv_find_main;


	private RelativeLayout rl_content;
	Title mTitle;

	private FragementFood mFragmentFood;
	private FragementFoodWay mFragmentFoodWay;


	ModelFoodSearch mFoodSearch; // 搜索需要传送的数据

	@Override
	public String setCenterTitle() {
		return "食材和食疗方";

	}

	@Override
	public void initIntent() {


	}

	@Override
	public int getLayoutId() {
		return R.layout.activity_food_way;
	}

	@Override
	public void initView() {
		iv_find = (ImageView) findViewById(R.id.iv_find);
		et_find = (EditText) findViewById(R.id.et_find);

		tv_find = (TextView) findViewById(R.id.tv_find);
		ll_find = (LinearLayout) findViewById(R.id.ll_find);
		rl_find = (RelativeLayout) findViewById(R.id.rl_find);
		iv_find_main = (ImageView) findViewById(R.id.iv_find_main);

		rl_content = (RelativeLayout) findViewById(R.id.rl_content);
		mTitle = getTitleClass();
	}

	@Override
	public void initData() {
		if (mFragmentFood == null) {
			mFragmentFood = new FragementFood();
		}
		replaceFragment(R.id.rl_content, mFragmentFood);
		mTitle.iv_1_choose.setImageResource(R.drawable.segmented_control_02);
		mTitle.tv_title.setVisibility(View.GONE);
		mTitle.iv_1_choose.setVisibility(View.VISIBLE);
	}


	@Override
	public Object onResponceSuccess(String str, Class class1) {
		Object object = super.onResponceSuccess(str, class1);
		if (object instanceof ModelFoodSearchIndex) {
			mApp.startActivity_qcj(this, FoodCategoryActivity.class,
					sendDataToBundle(mFoodSearch, null));
		} else {
			ToastUtils.showToast("暂时没有相关内容！");
		}
		return object;
	}

	private boolean isSecond = false;

	@Override
	public void initListener() {
		mTitle.rl_1_image.setVisibility(View.VISIBLE);
		mTitle.iv_1_choose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!isSecond) {
					isSecond = true;
					// Todo 显示第二个fragment；
					if (mFragmentFoodWay == null) {
						mFragmentFoodWay = new FragementFoodWay();
					}
					replaceFragment(R.id.rl_content, mFragmentFoodWay);
					mTitle.iv_1_choose
							.setImageResource(R.drawable.segmented_control);

				} else {
					isSecond = false;
					// Todo 显示第一个fragment；
					if (mFragmentFood == null) {
						mFragmentFood = new FragementFood();
					}
					replaceFragment(R.id.rl_content, mFragmentFood);
					mTitle.iv_1_choose
							.setImageResource(R.drawable.segmented_control_02);
				}
			}
		});

		tv_find.setOnClickListener(this);
		iv_find_main.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.tv_find:
			ll_find.setVisibility(View.GONE);
			rl_find.setVisibility(View.VISIBLE);
			et_find.setFocusable(true);
			break;

		case R.id.iv_find_main:
			String key = et_find.getText().toString();
			if (!TextUtils.isEmpty(key)) {
				mFoodSearch = new ModelFoodSearch();
				mFoodSearch.setKey(key);
				if (isSecond) {
					mFoodSearch.setState(1);
				} else {
					mFoodSearch.setState(0);
				}
				sendRequest(mApp.getFoodImpl().food_search(mFoodSearch),
						ModelFoodSearchIndex.class, REQUEST_GET);
			}
			break;
		}

	}

}
