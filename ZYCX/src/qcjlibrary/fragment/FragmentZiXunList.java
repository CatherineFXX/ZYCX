package qcjlibrary.fragment;

import qcjlibrary.adapter.ZhiXunAdapter;
import qcjlibrary.adapter.base.BAdapter;
import qcjlibrary.fragment.base.BaseFragment;
import qcjlibrary.listview.base.CommonListView;
import qcjlibrary.model.ModelZiXunDetail;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.zhiyicx.zycx.R;
import com.zhiyicx.zycx.activity.ZiXUnContentActivity;
import com.zhiyicx.zycx.activity.HomeActivity.onStatusChangedListener;
import com.zhiyicx.zycx.sociax.android.Thinksns;

/**
 * Created by Administrator on 2015/1/14.
 */
public class FragmentZiXunList extends BaseFragment {
	final private static String TAG = "ZiXunListFragment";
	private ZhiXunAdapter mAdapter;
	private CommonListView mCommonListView;
	private int mType = 0;
	private String isColl;
	/** 点击的位置**/
	private int posi;

	public static FragmentZiXunList newInstanse(int type) {
		FragmentZiXunList f = new FragmentZiXunList();
		Bundle bundle = new Bundle();
		bundle.putInt("type", type);
		f.setArguments(bundle);
		return f;
	}

	@Override
	public int getLayoutId() {
		return R.layout.listview_common_layout;
	}

	@Override
	public void initIntentData() {
		Bundle bundle = getArguments();
		mType = bundle.getInt("type");
		isColl = bundle.getString("isColl");
	}

	@Override
	public void initView() {
		mCommonListView = (CommonListView) findViewById(R.id.mCommonListView);
		ModelZiXunDetail detail = new ModelZiXunDetail();
		detail.setFenlei_id(mType);
		detail.setIsColl(isColl);
		mAdapter = new ZhiXunAdapter(this, detail);
		mCommonListView.setDividerHeight(0);
		mCommonListView.setAdapter(mAdapter);
		mCommonListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				posi = position;
//				mAdapter.updataView(posi - 1,mCommonListView);
				mCommonListView.stepToNextActivity(parent, view, position, ZiXUnContentActivity.class);
			}
		});
	}

	@Override
	public void initListener() {

	}

	@Override
	public void initData() {

	}

	@Override
	public void onClick(View v) {

	}
	
	@Override
	public void onResume() {
		// TODO 自动生成的方法存根
		super.onResume();
	}
	
	
	
	

	// @Override
	// public void onAttach(Activity activity) {
	// super.onAttach(activity);
	// }
	//
	// @Override
	// public OnTouchListListener getListView() {
	// return mListView;
	// }
	//
	// @Override
	// public void doRefreshHeader() {
	// if (mAdapter != null)
	// mAdapter.doRefreshHeader();
	// }
	//
	// @Override
	// public View onCreateView(LayoutInflater inflater, ViewGroup container,
	// Bundle savedInstanceState) {
	// if (mCustView == null)
	// mCustView = inflater.inflate(R.layout.listview_common_layout,
	// container, false);
	// Bundle bundle = getArguments();
	// mType = bundle.getInt("type");
	// mListView = (CommonListView) mCustView
	// .findViewById(R.id.mCommonListView);
	// mListView.setOnItemListener(new LoadListView.OnItemListener() {
	// @Override
	// public void onClick(View view, int position, long id) {
	// Log.d(TAG, "get ZiXunlist item click position : " + position);
	// JSONObject jsonObject = mAdapter.getItem(position - 1);
	// try {
	// int _id = jsonObject.getInt("id");
	// int uid = jsonObject.getInt("uid");
	// String title = jsonObject.getString("title");
	// Intent intent = new Intent(mContext,
	// ZiXUnContentActivity.class);
	// intent.putExtra("id", _id);
	// intent.putExtra("uid", uid);
	// intent.putExtra("title", title);
	// // intent.putExtra("cid", mType);
	// intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
	// | Intent.FLAG_ACTIVITY_NEW_TASK);
	// mContext.startActivity(intent);
	// Anim.in(mContext);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }
	// });
	// ArrayList<JSONObject> data = new ArrayList<JSONObject>();
	// mAdapter = new ZiXunListAdapter(this, data, mType);
	// mListView.setAdapter(mAdapter, System.currentTimeMillis(), mContext);
	// loadData(false);
	// return mCustView;
	// }
	//
	// @Override
	// public void loadData(boolean isLoadNew) {
	// if (mType == 0)
	// mAdapter.loadHomeInitData();
	// else
	// mAdapter.loadInitData();
	// mListView.setSelectionFromTop(0, 20);
	// }
	//
	// //
	// // mListView.setOnItemListener(new LoadListView.OnItemListener() {
	// // @Override
	// // public void onClick(View view, int position, long id) {
	// // Log.d(TAG, "get ZiXunlist item click position : " + position);
	// // JSONObject jsonObject = mAdapter.getItem(position - 1);
	// // try {
	// // int _id = jsonObject.getInt("id");
	// // int uid = jsonObject.getInt("uid");
	// // String title = jsonObject.getString("title");
	// // Intent intent = new Intent(mContext,
	// // ZiXUnContentActivity.class);
	// // intent.putExtra("id", _id);
	// // intent.putExtra("uid", uid);
	// // intent.putExtra("title", title);
	// // // intent.putExtra("cid", mType);
	// // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
	// // | Intent.FLAG_ACTIVITY_NEW_TASK);
	// // mContext.startActivity(intent);
	// // Anim.in(mContext);
	// // } catch (Exception e) {
	// // e.printStackTrace();
	// // }
	// // }
	// // });
}
