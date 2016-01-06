package qcjlibrary.fragment;

import java.util.ArrayList;
import java.util.List;
import com.umeng.socialize.utils.Log;
import com.zhiyicx.zycx.R;
import com.zhiyicx.zycx.sociax.android.Thinksns;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import qcjlibrary.activity.RequestDetailCommonActivity;
import qcjlibrary.activity.RequestDetailExpertActivity;
import qcjlibrary.activity.SearchNewActivity;
import qcjlibrary.activity.SearchNewActivity.OnSearchTouchListerer;
import qcjlibrary.adapter.RequestAnswerAdapter;
import qcjlibrary.config.Config;
import qcjlibrary.fragment.base.BaseFragment;
import qcjlibrary.listview.base.CommonListView;
import qcjlibrary.model.ModelRequest;
import qcjlibrary.model.ModelRequestItem;
import qcjlibrary.model.ModelRequestSearch;
import qcjlibrary.model.base.Model;
import qcjlibrary.util.DisplayUtils;
import qcjlibrary.util.L;

public class FragmentRequest extends BaseFragment{

	private CommonListView mCommonListView;
	private RequestAnswerAdapter mRequestAdapter;
	/** 问答搜索 **/
	private ModelRequestSearch mRequestSearch;
	private List<Model> mItemList;
	private boolean isCreate = false;
	
	@Override
	public void onClick(View v) {
		
	}

	@Override
	public void initIntentData() {
		
	}

	@Override
	public int getLayoutId() {
		return R.layout.fragment_search;
	}

	@Override
	public void initView() {
		// TODO 自动生成的方法存根
		mCommonListView = (CommonListView) findViewById(R.id.mCommonListView);
		mCommonListView.setDividerHeight(DisplayUtils.dp2px(mApp, 10));
		
	}
	@Override
	public void initListener() {
		
		mCommonListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ModelRequestItem item = (ModelRequestItem) parent
						.getItemAtPosition(position);
				if (item != null) {
					if (item.getIs_expert().equals("0")) {
						mCommonListView.stepToNextActivity(parent, view,
								position, RequestDetailCommonActivity.class);
					} else if (item.getIs_expert().equals("1")) {
						mCommonListView.stepToNextActivity(parent, view,
								position, RequestDetailExpertActivity.class);
					}
				}

			}
		});
		
	}

	@Override
	public void initData() {
		mItemList = new ArrayList<Model>();
		mRequestSearch = new ModelRequestSearch();
	}

	@Override
	public Object onResponceSuccess(String str, Class class1) {
		Object object = super.onResponceSuccess(str, class1);
		if (object instanceof ModelRequest) {
			ModelRequest request = (ModelRequest) object;
			Object data = request.getList();
			mItemList = (List<Model>) data;
			mRequestAdapter = new RequestAnswerAdapter(FragmentRequest.this, mItemList, mRequestSearch);
			mCommonListView.setAdapter(mRequestAdapter);
		}
		judgeTheMsg(object);
		return object;
	}

	@Override
	public void onResume() {
		// TODO 自动生成的方法存根
		super.onResume();
		Log.d("Cathy", "request:onResume");
		//initData();
	}
	
	 //仅当可见时才加载内容
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
    	// TODO 自动生成的方法存根
    	super.setUserVisibleHint(isVisibleToUser);
    	
    	if(!isCreate){
    		return;
    	}
    	L.d("Cathy", "request isVisibleToUser = "+isVisibleToUser);
    	if(isVisibleToUser){
    		getData();
    	}
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	// TODO 自动生成的方法存根
    	super.onCreate(savedInstanceState);
    	isCreate = true;
    }
    
    //发送请求，获取数据
    private void getData() {
		mApp.searchAct.setOnSearchListener(new OnSearchTouchListerer() {
			
			@Override
			public void onSearchTouch_Weibo(String key) {
			}
			
			@Override
			public void onSearchTouch_Request(String key) {
				Log.d("Cathy", "request:"+key);
				mRequestSearch.setKey(key);
				sendRequest(mApp.getRequestImpl().search(mRequestSearch),
						ModelRequest.class, REQUEST_GET);
			}
			
			@Override
			public void onSearchTouch_Qclass(String key) {
			}
			
			@Override
			public void onSearchTouch_Info(String key) {
			}
			
			@Override
			public void onSearchTouch_Food(String key) {
			}
		});
	}
}
