package qcjlibrary.fragment;

import java.util.ArrayList;
import java.util.List;

import com.viewpagerindicator.TabPageIndicator;
import com.zhiyicx.zycx.R;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import qcjlibrary.adapter.FragmentAdapter;
import qcjlibrary.api.api;
import qcjlibrary.fragment.base.BaseFragment;
import qcjlibrary.model.ModelZiXun;
import qcjlibrary.model.ModelZiXunCategory;
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
		sendRequest(new api.ZhiXunImpl().index(), ModelZiXun.class, 0);
	}

	@Override
	public Object onResponceSuccess(String str, Class class1) {
		Object object = super.onResponceSuccess(str, class1);
		if (object instanceof ModelZiXun) {
			Object result = ((ModelZiXun) object).getFenlei();
			mList.addAll((List<ModelZiXunCategory>) result);
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

	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.iv_right_arrow) {
//			if (vPager != null) {
//				int currentId = vPager.getCurrentItem();
//				if (currentId + 1 < mList.size()) {
//					vPager.setCurrentItem(currentId + 1);
//				}
//			}
		}

	}

}
