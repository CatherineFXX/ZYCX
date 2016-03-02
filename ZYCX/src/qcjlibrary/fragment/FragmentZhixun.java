package qcjlibrary.fragment;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.bigkoo.pickerview.listener.OnItemSelectedListener;
import com.viewpagerindicator.TabPageIndicator;
import com.viewpagerindicator.TabPageIndicator.OnItemChoseLinstener;
import com.viewpagerindicator.TabPageIndicator.OnTabReselectedListener;
import com.zhiyicx.zycx.R;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import qcjlibrary.adapter.FragmentAdapter;
import qcjlibrary.adapter.base.OnTabselectedListener;
import qcjlibrary.api.api;
import qcjlibrary.fragment.base.BaseFragment;
import qcjlibrary.model.ModelQclass;
import qcjlibrary.model.ModelZiXun;
import qcjlibrary.model.ModelZiXunCategory;
import qcjlibrary.util.DefaultLayoutUtil;
import qcjlibrary.util.ToastUtils;

/**
 * author：qiuchunjia time：上午10:23:36 类描述：这个类是实现
 *
 */

public class FragmentZhixun extends BaseFragment {
	private TabPageIndicator tabpagerIndicator;
	private ViewPager vPager;
	private ImageView iv_right_arrow;
	private List<ModelZiXunCategory> mList = new ArrayList<ModelZiXunCategory>();

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
		// sendRequest(new api.ZhiXunImpl().index(), ModelZiXun.class, 0);
	}

	@Override
	public Object onResponceSuccess(String str, Class class1) {
		Object object = super.onResponceSuccess(str, class1);
		if (object instanceof ModelZiXun) {
			Object result = ((ModelZiXun) object).getFenlei();
			ModelZiXunCategory all = new ModelZiXunCategory();
			mList.removeAll(mList);
			all.setTitle("全部");
			all.setFenlei_id(0);
			mList.add(all);
			mList.addAll((List<ModelZiXunCategory>) result);
			ModelZiXunCategory collect = new ModelZiXunCategory();
			collect.setTitle("收藏");
			collect.setFenlei_id(0);
			collect.setIsColl("iscoll");
			mList.add(collect);
			FragmentAdapter fadapter = new FragmentAdapter(getChildFragmentManager(), mList);
			vPager.setAdapter(fadapter);
			tabpagerIndicator.setViewPager(vPager);
			tabpagerIndicator.setVisibility(View.VISIBLE);
		}
		return object;
	}

	@Override
	public void initListener() {
		iv_right_arrow.setOnClickListener(this);
		tabpagerIndicator.setOnItemChoseLinstener(new OnItemChoseLinstener() {
			
			@Override
			public void onItemChose(int position) {
				if(position == mList.size()- 1){
					iv_right_arrow.setVisibility(View.GONE);
				} else{
					iv_right_arrow.setVisibility(View.VISIBLE);
				}
			}
		});
	}
	

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		if (mList != null) {
			sendRequest(new api.ZhiXunImpl().index(), ModelZiXun.class, 0);
		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.iv_right_arrow) {
			if (vPager != null) {
				int currentId = vPager.getCurrentItem();
				if (currentId + 1 < mList.size()) {
					vPager.setCurrentItem(currentId + 1);
				}
			}
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
				sendRequest(new api.ZhiXunImpl().index(), ModelZiXun.class, 0);
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
		// DefaultLayoutUtil.hideDefault(ll_fragment_list, defaultView);
		return defaultView;
	}

}
