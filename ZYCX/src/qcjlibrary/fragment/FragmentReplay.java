package qcjlibrary.fragment;

import qcjlibrary.activity.RequestDetailResponceActivity;
import qcjlibrary.adapter.ReplayAdapter;
import qcjlibrary.fragment.base.BaseFragment;
import qcjlibrary.listview.base.CommonListView;
import qcjlibrary.model.ModelNotifyCommment;
import qcjlibrary.model.ModelRequestAnswerComom;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.zhiyicx.zycx.R;

/**
 * author：qiuchunjia time：下午3:13:46 类描述：这个类是实现
 *
 */

public class FragmentReplay extends BaseFragment {

	private CommonListView mCommonListView;
	private ReplayAdapter mAdapter;

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
		mAdapter = new ReplayAdapter(this, null);
		mCommonListView.setAdapter(mAdapter);

		mCommonListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ModelNotifyCommment commment = (ModelNotifyCommment) parent
						.getItemAtPosition(position);
				if (!TextUtils.isEmpty(commment.getOriginal_answer_id())) {
					ModelRequestAnswerComom common = new ModelRequestAnswerComom();
					common.setAnswer_id(commment.getOriginal_answer_id());
					common.setComment_id(commment.getComment_id());
					common.setShoudGone(true);
					mCommonListView.stepToNextActivity(common,
							RequestDetailResponceActivity.class);
				}
			}
		});

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
