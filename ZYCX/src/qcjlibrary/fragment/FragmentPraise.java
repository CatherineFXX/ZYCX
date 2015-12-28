package qcjlibrary.fragment;

import qcjlibrary.adapter.PraiseAdapter;
import qcjlibrary.fragment.base.BaseFragment;
import qcjlibrary.listview.base.CommonListView;
import android.view.View;

import com.zhiyicx.zycx.R;

/**
 * author：qiuchunjia time：下午3:13:46 类描述：这个类是实现
 *
 */

public class FragmentPraise extends BaseFragment {

	private CommonListView mCommonListView;
	private PraiseAdapter mAdapter;

	@Override
	public void initIntentData() {
		// TODO Auto-generated method stub

	}

	@Override
	public int getLayoutId() {
		return R.layout.listview_common_layout;
	}

	@Override
	public void initView() {
		mCommonListView = (CommonListView) findViewById(R.id.mCommonListView);
		mCommonListView.setDividerHeight(5);
		mAdapter = new PraiseAdapter(this, null);
		mCommonListView.setAdapter(mAdapter);
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initListener() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {

	}

}
