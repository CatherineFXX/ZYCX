package qcjlibrary.activity;

import qcjlibrary.activity.base.BaseActivity;
import qcjlibrary.activity.base.Title;
import qcjlibrary.adapter.ExpertRequestAdapter;
import qcjlibrary.adapter.base.OnRequestLinstner;
import qcjlibrary.listview.base.CommonListView;
import qcjlibrary.model.ModelRequestAsk;
import qcjlibrary.model.ModelRequestItem;
import qcjlibrary.model.ModelRequestMyAsk;
import qcjlibrary.util.DefaultLayoutUtil;
import qcjlibrary.util.DisplayUtils;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhiyicx.zycx.LoginActivity;
import com.zhiyicx.zycx.R;
import com.zhiyicx.zycx.sociax.android.Thinksns;

/**
 * author：qiuchunjia time：下午5:33:01 类描述：这个类是实现
 */

public class ExpertRequestActivity extends BaseActivity {
	private CommonListView mCommonListView;
	private ExpertRequestAdapter mAdapter;
	private RelativeLayout ll_commonlist_parent;
	private boolean fromIndex = false;
	private Title mTitle;
	private ModelRequestAsk mAsk;
	private int type = 0;//判断是否是从个人中心跳转而来,如果是从个人中心跳转而来，那么没有推荐
	
	private RelativeLayout rl_request_to_case;
	private TextView tv_to_model;
	private TextView tv_to_case;
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_to_model:
			//跳转到病历模板
			mApp.startActivity_qcj(this, RequestModelActivity.class, null);
			break;
		case R.id.tv_to_case:
			//跳转到完善病历
			mApp.startActivity_qcj(this, PatientMeActivity.class, null);
			break;

		default:
			break;
		}
	}

	@Override
	public String setCenterTitle() {
		return "专家问答";
	}

	@Override
	public void initIntent() {
		Intent mIntent = getIntent();
		if (mIntent != null) {
			fromIndex = mIntent.getBooleanExtra("fromIndex", false);
			if (fromIndex) {
				type = 2;
			} else {
				type = 1;
			}
		} 
	}

	@Override
	public int getLayoutId() {
		return R.layout.activity_expert_request;
	}

	@Override
	public void initView() {
		rl_request_to_case = (RelativeLayout) findViewById(R.id.rl_request_to_case);
		if(!fromIndex){
			titleSetCenterTitle("专家提问");
			rl_request_to_case.setVisibility(View.VISIBLE);
		}
		tv_to_model = (TextView) findViewById(R.id.tv_to_model);
		tv_to_case = (TextView) findViewById(R.id.tv_to_case);
		mCommonListView = (CommonListView) findViewById(R.id.mCommonListView);
		ll_commonlist_parent = (RelativeLayout) findViewById(R.id.ll_commonlist_parent);
		mCommonListView.setDividerHeight(DisplayUtils.dp2px(mApp, 10));
		mAdapter = new ExpertRequestAdapter(this, null, type);
		mCommonListView.setAdapter(mAdapter);
	}

	@Override
	public Object onResponceSuccess(String str, Class class1) {
		Object object = super.onResponceSuccess(str, class1);
		// 用于更新同意后的adapter
		if (judgeTheMsg(object)) {
			mAdapter.doRefreshHeader();
		}
		return object;
	}

	@Override
	public void initData() {
		mTitle = getTitleClass();
		if (fromIndex) {
			titleSetRightImage(R.drawable.chuangjianjingli);
			mAsk = new ModelRequestAsk();
			mAsk.setIs_expert("1");
		}
	}

	@Override
	public void initListener() {
		tv_to_model.setOnClickListener(this);
		tv_to_case.setOnClickListener(this);
		mAdapter.setOnRequestLinstner(new OnRequestLinstner() {

			@Override
			public void onSuccess(View view) {
				DefaultLayoutUtil.hideDefault(ll_commonlist_parent, view);
			}

			@Override
			public void onFailed(View view) {
				DefaultLayoutUtil.showDefault(ll_commonlist_parent, view);
				TextView tv_reload = (TextView) view.findViewById(R.id.tv_reload);
				tv_reload.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						mAdapter.doRefreshNew();
					}
				});
			}
		});

		mCommonListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ModelRequestMyAsk item = (ModelRequestMyAsk) parent.getItemAtPosition(position);
				if (item.getUid() == Thinksns.getMy().getUid()) {
					mCommonListView.stepToNextActivity(parent, view, position, RequestDetailExpertActivity.class);
				} else{
					//非自己的专家问答,跳转到另外一个单独的界面
					mCommonListView.stepToNextActivity(parent, view, position, RequestOhterDetailExpertActivity.class);
				}
				
			}
		});

		mTitle.iv_title_right1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isLogin()) {
					mAsk.setIs_expert("1");
					// 获取病历信息，完善后才能提专家问答
					mApp.startActivity_qcj(ExpertRequestActivity.this, RequestSendTopicActivity.class,
							sendDataToBundle(mAsk, null));
				} else {
					mApp.startActivity_qcj(ExpertRequestActivity.this, LoginActivity.class, null);
				}
			}
		});
	}

}
