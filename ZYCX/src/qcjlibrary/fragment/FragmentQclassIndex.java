package qcjlibrary.fragment;

import java.util.ArrayList;
import java.util.List;

import com.viewpagerindicator.TabPageIndicator;
import com.viewpagerindicator.TabPageIndicator.OnItemChoseLinstener;
import com.zhiyicx.zycx.R;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import qcjlibrary.adapter.QclassFragmentAdapter;
import qcjlibrary.api.api;
import qcjlibrary.fragment.base.BaseFragment;
import qcjlibrary.model.ModelQclass;
import qcjlibrary.model.ModelQclassCategory;
import qcjlibrary.model.ModelZiXun;
import qcjlibrary.util.DefaultLayoutUtil;

/**
 * 轻课堂首页
 *
 * @author Tan
 * @since 12.31
 */

public class FragmentQclassIndex extends BaseFragment {
	/** 首页导航栏 **/
	private TabPageIndicator tabpagerIndicator;
	private ViewPager vPager;
	private List<ModelQclassCategory> mCategoryList;
	private ImageView iv_right_arrow;

	private View defaultView;
	private LinearLayout ll_fragment_list;
	private LinearLayout ll_fragment_in;

	@Override
	public void initIntentData() {

	}

	@Override
	public int getLayoutId() {
		return R.layout.zixunfragment;
	}

	@Override
	public void initView() {
		tabpagerIndicator = (TabPageIndicator) findViewById(R.id.tabpagerIndicator);
		vPager = (ViewPager) findViewById(R.id.vPager);
		iv_right_arrow = (ImageView) findViewById(R.id.iv_right_arrow);
		ll_fragment_list = (LinearLayout) findViewById(R.id.ll_fragment_list);
		ll_fragment_in = (LinearLayout) findViewById(R.id.ll_fragment_in);
	}

	@Override
	public Object onResponceSuccess(String str, Class class1) {
		Object object = super.onResponceSuccess(str, class1);
		if (object instanceof ModelQclass) {
			ModelQclass result = (ModelQclass) object;
			ModelQclassCategory recommend = new ModelQclassCategory();
			recommend.setClass_id(0);
			recommend.setImooc_class_name("推荐");
			recommend.setSort(result.getList().size());
			mCategoryList.add(recommend);
			if (result.getCategory() != null) {
				mCategoryList.addAll(result.getCategory());
				QclassFragmentAdapter qAdapter = new QclassFragmentAdapter(getChildFragmentManager(), mCategoryList);
				vPager.setAdapter(qAdapter);
				tabpagerIndicator.setViewPager(vPager);
				tabpagerIndicator.setVisibility(View.VISIBLE);
			}
		}
		return object;
	}

	@Override
	public void initListener() {
		iv_right_arrow.setOnClickListener(this);
		tabpagerIndicator.setOnItemChoseLinstener(new OnItemChoseLinstener() {

			@Override
			public void onItemChose(int position) {
				if (position == mCategoryList.size() - 1) {
					iv_right_arrow.setVisibility(View.GONE);
				} else {
					iv_right_arrow.setVisibility(View.VISIBLE);
				}
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		// 网络不好的时间会用到
		if (mCategoryList != null) {
			sendRequest(new api.QclassImpl().index(), ModelQclass.class, 0);
		}
	}

	@Override
	public void initData() {
		mCategoryList = new ArrayList<ModelQclassCategory>();
		// 发送请求
		// sendRequest(new api.QclassImpl().index(), ModelQclass.class, 0);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_right_arrow:
			if (vPager != null) {
				int currentId = vPager.getCurrentItem();
				if (currentId + 1 < mCategoryList.size()) {
					vPager.setCurrentItem(currentId + 1);
				}
			}
			break;

		default:
			break;
		}
	}

	@Override
	public View onRequestFailed() {
		// TODO 自动生成的方法存根
		defaultView = super.onRequestFailed();
		TextView tv_reload = (TextView) findViewById(R.id.tv_reload);
		tv_reload.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				sendRequest(new api.QclassImpl().index(), ModelQclass.class, 0);
			}
		});
		ll_fragment_in.setVisibility(View.VISIBLE);
		// DefaultLayoutUtil.showDefault(ll_fragment_list, defaultView);
		return defaultView;
	}

	@Override
	public View onRequestSuccess() {
		// TODO 自动生成的方法存根
		defaultView = super.onRequestSuccess();
		ll_fragment_in.setVisibility(View.GONE);
		// ll_fragment_list.removeView(defaultView);
		// DefaultLayoutUtil.hideDefault(ll_fragment_list, defaultView);
		return defaultView;
	}

}
