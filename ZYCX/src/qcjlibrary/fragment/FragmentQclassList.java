package qcjlibrary.fragment;

import java.util.ArrayList;
import java.util.List;

import com.umeng.socialize.utils.Log;
import com.zhiyicx.zycx.R;
import com.zhiyicx.zycx.activity.HomeActivity.onStatusChangedListener;
import com.zhiyicx.zycx.activity.QClassDetailsActivity;
import com.zhiyicx.zycx.sociax.android.Thinksns;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import qcjlibrary.adapter.QclassAdapter;
import qcjlibrary.api.api.QclassImpl;
import qcjlibrary.fragment.base.BaseFragment;
import qcjlibrary.listview.base.CommonListView;
import qcjlibrary.model.ModelQclass;
import qcjlibrary.model.ModelQclassDetail;
import qcjlibrary.model.base.Model;
import qcjlibrary.util.L;
import qcjlibrary.util.ToastUtils;

public class FragmentQclassList extends BaseFragment{

	/**
	 * 轻课堂分类页面
	 * @author Tan
	 * @since 2015.12.31
	 * */
	
	private QclassAdapter mAdapter;
	private CommonListView mCommonListView;
	private int mType = 0;
	private int status = 0;
	private List<Model> mList;
	private ModelQclassDetail detail;
	private QclassImpl qclassImpl;
	private boolean isCreate = false;
	/** 点击的位置**/
	private int posi;
	
	public FragmentQclassList newInstanse(int type){
		FragmentQclassList f = new FragmentQclassList();
		Bundle bundle = new Bundle();
		bundle.putInt("type", type);
		f.setArguments(bundle);
		return f;
	}
	
	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void initIntentData() {
		Bundle bundle = getArguments();
		mType = bundle.getInt("type");
	}

	@Override
	public int getLayoutId() {
		// TODO 自动生成的方法存根
		return R.layout.listview_common_layout;
	}

	@Override
	public void initView() {
		mCommonListView = (CommonListView) findViewById(R.id.mCommonListView);
		mCommonListView.setDividerHeight(15);
	}

	@Override
	public void initListener() {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void initData() {
		mList = new ArrayList<Model>();
		detail = new ModelQclassDetail();
		qclassImpl = new QclassImpl();
		detail.setClass_id(mType);
		mAdapter = new QclassAdapter(this, detail);
		mCommonListView.setAdapter(mAdapter);
//		sendRequest(qclassImpl.indexItem(detail), ModelQclass.class, 0);
		mCommonListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				posi = position;
				mAdapter.updataView(posi - 1,mCommonListView);
				mCommonListView.stepToNextActivity(parent, view, position, 
						QClassDetailsActivity.class);
			}
		});
	}

	@Override
	public Object onResponceSuccess(String str, Class class1) {
		// TODO 自动生成的方法存根
		Object object = super.onResponceSuccess(str, class1);
		if(object instanceof ModelQclass){
			ModelQclass mQclass = (ModelQclass)object;
			if(mList != null){
				mList.clear();
				if(mQclass.getList() != null){
					mList.addAll(mQclass.getList());
//					L.d("Cathy", "mList:"+mList.size()+" mQclass:"+mQclass.getList().size()
//							+" "+mQclass.getList().get(0).getCourse_name()+" mType:"+mType);
					if(mAdapter == null){
						mAdapter = new QclassAdapter(this, detail);
						mCommonListView.setAdapter(mAdapter);
					} else{
						mList.clear();
						mList.addAll(mQclass.getList());
						mAdapter.mList = mList;
						mAdapter.notifyDataSetChanged();
					}
				} else{
					ToastUtils.showLongToast(getActivity(), "没有相关数据");
				}
			} 
		}
		return object;
	}
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		// TODO 自动生成的方法存根
		super.setUserVisibleHint(isVisibleToUser);
		if(!isCreate){
			return;
		}
		if(isVisibleToUser){
			Thinksns.homeAct.setOnStatusChangedListener(new onStatusChangedListener() {
				
				@Override
				public void onStatusChange(int status) {
					if(mAdapter != null){
						mAdapter.setStatus(status);
					}
					detail.setStatus(status);
//					sendRequest(qclassImpl.indexItem(detail), ModelQclass.class, 0);
				}
			});
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		isCreate = true;
	}
	
	@Override
	public void onResume() {
		// TODO 自动生成的方法存根
		super.onResume();
//		if(mAdapter != null 
//				&& mAdapter.mList.size() > 0){
//			mAdapter.refreshNew();;
//		}
	}
	
}
