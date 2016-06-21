package qcjlibrary.activity;

import java.util.List;

import com.umeng.socialize.utils.Log;
import com.zhiyicx.zycx.LoginActivity;
import com.zhiyicx.zycx.R;

import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import qcjlibrary.activity.base.BaseActivity;
import qcjlibrary.activity.base.Title;
import qcjlibrary.config.Config;
import qcjlibrary.model.ModelExperience;
import qcjlibrary.model.ModelFoodWayDetail;
import qcjlibrary.model.ModelRequestAnswerComom;
import qcjlibrary.model.ModelRequestDetailCommon;
import qcjlibrary.model.ModelRequestFlag;
import qcjlibrary.model.ModelRequestItem;
import qcjlibrary.model.ModelRequestRelate;
import qcjlibrary.model.ModelShareContent;
import qcjlibrary.model.base.Model;
import qcjlibrary.util.DefaultLayoutUtil;
import qcjlibrary.util.SpanUtil;
import qcjlibrary.widget.RoundImageView;
import qcjlibrary.widget.popupview.PopShareContent;

/**
 * author：qiuchunjia time：下午4:31:08 类描述：这个类是实现
 *
 */

public class RequestDetailCommonActivity extends BaseActivity {
	private TextView request_tv_title;
	private TextView tv_content;
	private TextView tv_flag;
	private TextView tv_flag_value;
	private TextView tv_flag_value2;
	private TextView tv_flag_value3;
	private RoundImageView riv_icon;
	private TextView tv_username;
	private TextView tv_date;
	private LinearLayout ll_add_answer; // 添加评论内容，当评论后要刷新这个控件

	private LinearLayout ll_relate;
	private LinearLayout ll_answer;
	private TextView tv_other;
	
	/** 网络异常时的缺省图**/
	private View defaultView;
	private FrameLayout frame_request_common;

	// 数据model赛
	private ModelRequestItem mRequestItem;
	private String user_name;
	
	@Override
	public String setCenterTitle() {
		return "问题详情";
	}

	@Override
	public void initIntent() {
		mRequestItem = (ModelRequestItem) getDataFromIntent(getIntent(), null);
	}

	@Override
	public int getLayoutId() {
		return R.layout.activity_request_detail_common;
	}

	@Override
	public void initView() {
		titleSetRightImage(R.drawable.fenxiang);
		request_tv_title = (TextView) findViewById(R.id.request_tv_title);
		tv_content = (TextView) findViewById(R.id.tv_content);
		tv_flag = (TextView) findViewById(R.id.tv_flag);
		tv_flag_value = (TextView) findViewById(R.id.tv_flag_value);
		tv_flag_value2 = (TextView) findViewById(R.id.tv_flag_value2);
		tv_flag_value3 = (TextView) findViewById(R.id.tv_flag_value3);
		riv_icon = (RoundImageView) findViewById(R.id.riv_icon);
		tv_username = (TextView) findViewById(R.id.tv_username);
		tv_date = (TextView) findViewById(R.id.tv_date);
		ll_relate = (LinearLayout) findViewById(R.id.ll_relate);
		ll_answer = (LinearLayout) findViewById(R.id.ll_answer);
		ll_add_answer = (LinearLayout) findViewById(R.id.ll_add_answer);
		tv_other = (TextView) findViewById(R.id.tv_other);
		frame_request_common = (FrameLayout) findViewById(R.id.frame_request_common);
	}

	@Override
	public void initData() {
		Title title = getTitleClass();
		title.iv_title_right1.setOnClickListener(this);
//		sendRequest(mApp.getRequestImpl().answer(mRequestItem), ModelRequestDetailCommon.class, REQUEST_GET);
	}
	
	private String shareUrl;

	@Override
	public Object onResponceSuccess(String str, Class class1) {
		Object object = super.onResponceSuccess(str, class1);
		Log.d("RequestDetailCommonActivity","json data//"+str);
		if (object instanceof ModelRequestDetailCommon) {
			ModelRequestDetailCommon detailCommon = (ModelRequestDetailCommon) object;
			shareUrl = detailCommon.getQuestion().getUrl();
			addDataToHeadAndFlag(detailCommon.getQuestion(), detailCommon.getTopic_list());
			addDataToAnswer(detailCommon.getAnswer());
			addDataToRelate(detailCommon.getOther_question());
		}
		return object;
	}

	/**
	 * 添加数据到相关模块
	 * 
	 * @param other_question
	 */
	private void addDataToRelate(List<ModelRequestRelate> other_question) {
		if (other_question != null) {
			if (ll_relate.getChildCount() > 0) {
				ll_relate.removeAllViews();
			}
			for (int i = 0; i < other_question.size(); i++) {
				final ModelRequestRelate relate = other_question.get(i);
				View view = mInflater.inflate(R.layout.item_request_detail_relate, null);
				/*********** 初始化布局问答布局 ********************/
				TextView tv_relate_title = (TextView) view.findViewById(R.id.tv_relate_title);
				TextView tv_watch_num = (TextView) view.findViewById(R.id.tv_watch_num);
				/*********** 初始化布局问答布局 end ********************/
				tv_relate_title.setText(relate.getTitle());
				tv_watch_num.setText("浏览人数" + relate.getView_count());
				view.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						ModelRequestItem item = new ModelRequestItem();
						item.setQuestion_id(relate.getQid());
						if (relate.getIs_expert().equals("0")) {
							mApp.startActivity_qcj(RequestDetailCommonActivity.this, RequestDetailCommonActivity.class,
									sendDataToBundle(item, null));
						} else if (relate.getIs_expert().equals("1")) {
							mApp.startActivity_qcj(RequestDetailCommonActivity.this, RequestDetailExpertActivity.class,
									sendDataToBundle(item, null));
						}
					}
				});
				ll_relate.addView(view);
			}
		}
	}

	/**
	 * 添加数据到问答模块
	 * 
	 * @param answer
	 */
	private void addDataToAnswer(List<ModelRequestAnswerComom> answers) {
		if (answers != null) {
			if (ll_add_answer.getChildCount() > 0) {
				ll_add_answer.removeAllViews();
			}
			tv_other.setText(answers.size() + "条解答");
			for (int i = 0; i < answers.size(); i++) {
				final ModelRequestAnswerComom answerComom = answers.get(i);
				answerComom.setQid(mRequestItem.getQuestion_id());
				View view = mInflater.inflate(R.layout.item_requset_detail_common, null);
				/*********** 初始化布局问答布局 ********************/
				RoundImageView riv_other_icon = (RoundImageView) view.findViewById(R.id.riv_other_icon);
				TextView tv_other_username = (TextView) view.findViewById(R.id.tv_other_username);
				TextView tv_other_content = (TextView) view.findViewById(R.id.tv_other_content);
				TextView tv_other_date = (TextView) view.findViewById(R.id.tv_other_date);
				TextView tv_other_num = (TextView) view.findViewById(R.id.tv_other_num);
				ImageView iv_medat = (ImageView) view.findViewById(R.id.iv_medat);
				/*********** 初始化布局问答布局 end ********************/
				mApp.displayImage(answerComom.getUser_face(), riv_other_icon);
				tv_other_username.setText(answerComom.getUser_name());
				tv_other_content.setText(answerComom.getAnswer_content());
				tv_other_num.setText(answerComom.getComment_count());
				tv_other_date.setText(answerComom.getTime());
				iv_medat.setVisibility(View.GONE);
				if (answerComom.getIs_best().equals("1")) {
					iv_medat.setVisibility(View.VISIBLE);
				}
				view.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						mApp.startActivityForResult_qcj(RequestDetailCommonActivity.this,
								RequestDetailResponceActivity.class, sendDataToBundle(answerComom, null));
					}
				});
				ll_add_answer.addView(view);
			}
		}

	}

	/**
	 * 添加数据到头部和表情部分
	 * 
	 * @param question
	 * @param topic_list
	 */
	private void addDataToHeadAndFlag(ModelRequestItem question, List<ModelRequestFlag> topic_list) {
		if (question != null) {
			request_tv_title.setText("");
			Drawable drawable = getResources().getDrawable(R.drawable.q);
			request_tv_title.append(SpanUtil.setImageSpan("xx", 0, 0, drawable));
			request_tv_title.append(" " + question.getQuestion_content());
			tv_content.setText(question.getQuestion_detail());
			mApp.displayImage(question.getUser_face(), riv_icon);
			user_name = question.getUser_name();
			if(user_name.equals(mApp.getUser().getUname())){
				ll_answer.setVisibility(View.GONE);
			}
			tv_username.setText(question.getUser_name());
			tv_date.setText(question.getTime());
		}
		if (topic_list != null && topic_list.size() > 0) {
			for (int i = 0; i < topic_list.size(); i++) {
				final ModelRequestFlag flag = topic_list.get(i);
				if (i == 0) {
					tv_flag_value.setVisibility(View.VISIBLE);
					tv_flag_value.setText(flag.getTitle());
					tv_flag_value.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							mApp.startActivity_qcj(RequestDetailCommonActivity.this, RequestFlagActivity.class,
									sendDataToBundle(flag, null));
						}
					});
				}
				if (i == 1) {
					tv_flag_value2.setVisibility(View.VISIBLE);
					tv_flag_value2.setText(flag.getTitle());
					tv_flag_value2.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							mApp.startActivity_qcj(RequestDetailCommonActivity.this, RequestFlagActivity.class,
									sendDataToBundle(flag, null));
						}
					});
				}
				if (i == 2) {
					tv_flag_value3.setVisibility(View.VISIBLE);
					tv_flag_value3.setText(flag.getTitle());
					tv_flag_value3.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							mApp.startActivity_qcj(RequestDetailCommonActivity.this, RequestFlagActivity.class,
									sendDataToBundle(flag, null));
						}
					});
				}
			}
		} else{
			tv_flag.setVisibility(View.GONE);
		}
	}

	@Override
	public void initListener() {
		ll_answer.setOnClickListener(this);
		tv_flag_value2.setOnClickListener(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Object object = getReturnResultSeri(resultCode, data, null);
		if (object instanceof Boolean) {
			sendRequest(mApp.getRequestImpl().answer(mRequestItem), ModelRequestDetailCommon.class, REQUEST_GET);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_answer:
			if (isLogin()) {
				mApp.startActivityForResult_qcj(this, RequestDetailReponceSingleActivity.class,
						sendDataToBundle(mRequestItem, null));
			} else {
				mApp.startActivity_qcj(this, LoginActivity.class, null);
			}
			break;

		case R.id.tv_flag_value2:
			mApp.startActivity_qcj(this, RequestFlagActivity.class, sendDataToBundle(new Model(), null));
			break;
		case R.id.iv_title_right1:
			ModelShareContent shareContent = new ModelShareContent();
			shareContent.setType(Config.SHARE_TEXT);
			shareContent.setTitle("问答分享：");
			shareContent.setUrl(mRequestItem.getUrl());
			if(mRequestItem.getUrl() == null){
				shareContent.setUrl(shareUrl);
			}
			PopShareContent popShareContent = new PopShareContent(this, shareContent, this);
			popShareContent.showPop(ll_answer, Gravity.BOTTOM, 0, 0);
			// mApp.startActivity_qcj(this, RequestDetailResponceActivity.class,
			// sendDataToBundle(new Model(), null));
			break;
		}

	}
	
	@Override
	public View onRequestFailed() {
		// TODO 自动生成的方法存根
		defaultView =  super.onRequestFailed();
		TextView tv_reload = (TextView) defaultView.findViewById(R.id.tv_reload);
		tv_reload.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				sendRequest(mApp.getRequestImpl().answer(mRequestItem), 
						ModelRequestDetailCommon.class, REQUEST_GET);
			}
		});
		DefaultLayoutUtil.showDefault(frame_request_common, defaultView);
		return defaultView;
	}
	
	@Override
	public View onRequestSuccess() {
		// TODO 自动生成的方法存根
		defaultView = super.onRequestSuccess();
		DefaultLayoutUtil.hideDefault(frame_request_common, defaultView);
		return defaultView;
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		sendRequest(mApp.getRequestImpl().answer(mRequestItem), ModelRequestDetailCommon.class, REQUEST_GET);
	}
}
