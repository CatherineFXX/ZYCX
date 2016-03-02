package com.zhiyicx.zycx.activity;

import org.json.JSONObject;

import com.zhiyicx.zycx.LoginActivity;
import com.zhiyicx.zycx.R;
import com.zhiyicx.zycx.config.MyConfig;
import com.zhiyicx.zycx.net.JsonDataListener;
import com.zhiyicx.zycx.net.NetComTools;
import com.zhiyicx.zycx.sociax.component.TSFaceView;
import com.zhiyicx.zycx.sociax.unit.SociaxUIUtils;
import com.zhiyicx.zycx.util.Utils;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import qcjlibrary.activity.ZhiXunFlagActivity;
import qcjlibrary.activity.base.BaseActivity;
import qcjlibrary.activity.base.Title;
import qcjlibrary.config.Config;
import qcjlibrary.model.ModelMsg;
import qcjlibrary.model.ModelShareContent;
import qcjlibrary.model.ModelZiXunDetail;
import qcjlibrary.util.EditTextUtils;
import qcjlibrary.util.LoadingDialogUtl;
import qcjlibrary.util.ToastUtils;
import qcjlibrary.widget.popupview.PopShareContent;
import qcjlibrary.widget.popupview.PopSizeChoose;
import qcjlibrary.widget.popupview.base.PopView;

/**
 * Created by Administrator on 2014/12/27.
 */

public class ZiXUnContentActivity extends BaseActivity {

	final private static String TAG = "ZiXUnContentActivity";
	private WebView mContent;
	private int mId, mUid, mCid;
	private String mTitle;
	private String mUrl = null;
	private EditText mCmtEdit = null;
	private Button mCollBtn = null;
	private ImageView mFace = null;
	private TextView btn_praise;
	private TSFaceView mFaceView = null;
	private int mIsColl = 0;

	private ModelZiXunDetail mDetail = null;
	private Title mTitleLayout;
	private String mChangeSizeUrl; // 改变字体的url

	private int mCurrentPraise;
	private boolean isBig = false;
	private String contnet;
	private String mWebUrl;
	@Override
	public String setCenterTitle() {
		return "资讯详情";
	}

	@Override
	public void initIntent() {
		Object object = getDataFromIntent(getIntent(), null);
		if (object instanceof ModelZiXunDetail) {
			mDetail = (ModelZiXunDetail) object;
			mId = Integer.valueOf(mDetail.getId());
			mUid = Integer.valueOf(mDetail.getUid());
			mTitle = mDetail.getTitle();
		} else {
			Intent intent = getIntent();
			mId = intent.getIntExtra("id", 0);
			mUid = intent.getIntExtra("uid", 0);
			mTitle = intent.getStringExtra("title");
			mDetail = new ModelZiXunDetail();
			mDetail.setId(mId + "");
		}
		// mCid = getIntent().getIntExtra("cid", 0);
		if (mId == 0 || mUid == 0)
			finish();
		// mUrl = MyConfig.ZIXUN_CONTENT_URL + Utils.getTokenString(this) +
		// "&id=" + mId + "&uid=" + mUid;

	}

	@Override
	public int getLayoutId() {
		return R.layout.zixun_content_layout;
	}

	@Override
	public void initView() {
		titleSetRightImage(R.drawable.fenxiang);
		mTitleLayout = getTitleClass();
		mContent = (WebView) findViewById(R.id.content_view);
		btn_praise = (TextView) findViewById(R.id.btn_praise);
		mContent.getSettings().setJavaScriptEnabled(true);
		mContent.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (url.contains("tag_id=")) {
					String id = url.substring(url.indexOf("tag_id=") + 7, url.length());
					ModelZiXunDetail detail = new ModelZiXunDetail();
					detail.setTag_id(id);
					mApp.startActivity_qcj(ZiXUnContentActivity.this, ZhiXunFlagActivity.class,
							sendDataToBundle(detail, null));
				}
				return true;
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				// TODO 自动生成的方法存根
				super.onPageStarted(view, url, favicon);
				LoadingDialogUtl.loadingView(ZiXUnContentActivity.this);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO 自动生成的方法存根
				super.onPageFinished(view, url);
				LoadingDialogUtl.hideLoadingView();
			}
			
			@Override
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				// TODO 自动生成的方法存根
				super.onReceivedError(view, errorCode, description, failingUrl);
				ToastUtils.showToast("网页加载失败");
			}
			
			
		});
		// mContent.loadUrl(mUrl);
		// mContent.loadUrl("javascript:getComment()");
		loadData(false);
		findViewById(R.id.btn_back).setOnClickListener(this);
		findViewById(R.id.btn_share).setOnClickListener(this);
		findViewById(R.id.btn_comment).setOnClickListener(this);
		findViewById(R.id.btn_collect).setOnClickListener(this);
		btn_praise.setOnClickListener(this);
		mCollBtn = (Button) findViewById(R.id.btn_collect);
		mCmtEdit = (EditText) findViewById(R.id.edit_cmt);

		mFace = (ImageView) findViewById(R.id.face);
		mFaceView = (TSFaceView) findViewById(R.id.face_view);
		setBottomClick();
		mCmtEdit.clearFocus();
		mFaceView.setFaceAdapter(mFaceAdapter);
		mCmtEdit.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					if (mFaceView.getVisibility() == View.VISIBLE) {
						mFace.setImageResource(R.drawable.smile_face);
						mFaceView.setVisibility(View.GONE);
					} 
				}
			}
		});
		
		mCmtEdit.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(isLogin()){
					if (mFaceView.getVisibility() == View.VISIBLE) {
						mFace.setImageResource(R.drawable.smile_face);
						mFaceView.setVisibility(View.GONE);
					} 
				} else{
					Intent intent = new Intent(ZiXUnContentActivity.this, LoginActivity.class);
					startActivity(intent);
				}
				return false;
			}
		});

	}

	@Override
	public void initData() {
	}

	/**
	 * 初始化点赞
	 * 
	 * @param ispraise
	 */
	private void initPraise(int ispraise) {
		if (ispraise == 0) {
			setTextDrawable(R.drawable.zanicon);
		} else {
			setTextDrawable(R.drawable.zanicon02_red);
		}
		btn_praise.setText(mCurrentPraise + "");
	}

	/**
	 * 设置图片
	 */
	private void setTextDrawable(int resouceId) {
		Drawable drawable = getResources().getDrawable(resouceId);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
		btn_praise.setCompoundDrawables(drawable, null, null, null);
	}

	@Override
	public void initListener() {
		mTitleLayout.iv_title_right1.setOnClickListener(this);
		mTitleLayout.iv_title_right3.setImageResource(R.drawable.aa);
		mTitleLayout.iv_title_right3.setVisibility(View.VISIBLE);
		mTitleLayout.iv_title_right3.setOnClickListener(this);
		mCmtEdit.setOnClickListener(this);
		mContent.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					if (mFaceView.getVisibility() == View.VISIBLE) {
						mFaceView.setVisibility(View.GONE);
						mFace.setImageResource(R.drawable.smile_face);
					}
					SociaxUIUtils.hideSoftKeyboard(getApplicationContext(), mCmtEdit);
				}
				return false;
			}
		});
		//限制输入字数
		mCmtEdit.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				contnet = s.toString();
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				if(contnet.length() > 400){
					contnet = contnet.substring(0, 400);
					ToastUtils.showLongToast(ZiXUnContentActivity.this, "评论不可超过400字");
					mCmtEdit.setText(contnet);
				}
			}
		});
		// 点击后隐藏标签和输入框 如果有的话
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_praise:
			if (isLogin()) {
				sendRequest(mApp.getZhiXunImpl().doPraise(mDetail), ModelMsg.class, BaseActivity.REQUEST_GET);
			} else {
				mApp.startActivity_qcj(this, LoginActivity.class, null);
			}
			break;
		case R.id.btn_back:
			finish();
			break;
		case R.id.iv_title_right3:
			if (!TextUtils.isEmpty(mChangeSizeUrl)) {
				PopView popView = new PopSizeChoose(this, mChangeSizeUrl, this);
				popView.showPop(mTitleLayout.iv_title_right3, Gravity.TOP, 0, 0);
			} else {
				ToastUtils.showToast("请稍后。。。");
				loadData(false);
			}
			break;
		case R.id.iv_title_right1:
			ModelShareContent shareContent = new ModelShareContent();
			shareContent.setType(Config.SHARE_TEXT);
			shareContent.setTitle("青稞网资讯分享:" + mTitle);
			shareContent.setUrl(mWebUrl);
			PopShareContent PopshareContent = new PopShareContent(this, shareContent, this);
			PopshareContent.showPop(mContent, Gravity.BOTTOM, 0, 0);
			// Utils.shareText(this, mController, "青稞网资讯分享:" + mTitle + " - ",
			// mUrl);
			break;
		case R.id.btn_share:
			break;
		case R.id.btn_comment:
			if (isLogin()) {
				comment();
			} else {
				mApp.startActivity_qcj(this, LoginActivity.class, null);
			}
			break;
		case R.id.btn_collect:
			if (mIsColl == 1)
				collect(0);
			else
				collect(1);
			break;
		case R.id.edit_cmt:
			if (mFaceView.getVisibility() == View.VISIBLE) {
				mFaceView.setVisibility(View.GONE);
			}
			break;
		}
	}

	@Override
	public Object onResponceSuccess(String str, Class class1) {
		Object object = super.onResponceSuccess(str, class1);
		if (judgeTheMsg(object)) {
			// 已点赞， 已取消点赞
			ModelMsg modelMsg = (ModelMsg) object;
			if (modelMsg.getMessage().equals("已点赞")) {
				mCurrentPraise = mCurrentPraise + 1;
				btn_praise.setText(mCurrentPraise + "");
				setTextDrawable(R.drawable.zanicon02_red);
			} else {
				mCurrentPraise = mCurrentPraise - 1;
				btn_praise.setText(mCurrentPraise + "");
				setTextDrawable(R.drawable.zanicon);
			}
			ToastUtils.showToast(modelMsg.getMessage());
		}
		return object;
	}
	/** 是否已经提交成功标记**/
	private boolean isCommit = true; 

	private void comment() {
		String txt = mCmtEdit.getText().toString().trim();
		if (TextUtils.isEmpty(txt)) {
			loadData(true);
			ToastUtils.showToast("评论内容不能为空！");
			return;
		}
		if(EditTextUtils.containsEmoji(txt)){
			loadData(true);
			ToastUtils.showLongToast(this, "不可输入表情！");
			return;
		}
		if(isCommit){
			isCommit = false;
		} else{
			ToastUtils.showToast("提交中...");
			return;
		}
		String url = MyConfig.ZIXUN_COMMENT_URL + "&id=" + mId + "&uid=" + mUid + "&content=" + Utils.getUTF8String(txt)
				+ Utils.getTokenString(this);
		NetComTools.getInstance(this).getNetJson(url, new JsonDataListener() {
			@Override
			public void OnReceive(JSONObject jsonObject) {
				Log.d(TAG, "Comment Data:" + jsonObject.toString());
				try {
					int ret = jsonObject.getInt("code");
					if (ret == 0) {
						Utils.showToast(ZiXUnContentActivity.this, "评论成功！");
						// mContent.loadUrl("javascript:getComment()");
						isCommit = true;
						loadData(true);
						mContent.reload();
						mCmtEdit.setText("");
						// SociaxUIUtils.hideSoftKeyboard(ZiXUnContentActivity.this,
						// mCmtEdit);
					} else
						Utils.showToast(ZiXUnContentActivity.this, "评论失败，原因：" + jsonObject.get("message").toString());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void OnError(String error) {
				Log.d(TAG, "Comment error," + error);
			}
		});
		SociaxUIUtils.hideSoftKeyboard(this, mCmtEdit);
	}

	private void collect(final int coll) {
		String url = MyConfig.ZIXUN_COLLECT_URL + Utils.getTokenString(this) + "&id=" + mId + "&isColl=" + coll;
		NetComTools netComTools = NetComTools.getInstance(this);
		netComTools.getNetJson(url, new JsonDataListener() {
			@Override
			public void OnReceive(JSONObject jsonObject) {
				Log.d(TAG, "ZiXun Collect data " + jsonObject.toString());
				try {
					int ret = jsonObject.getInt("code");
					if (ret == 0) {
						if (coll == 1) {
							Utils.showToast(ZiXUnContentActivity.this, "收藏成功！");
							mCollBtn.setText("不收藏");
							mIsColl = 1;
						} else {
							Utils.showToast(ZiXUnContentActivity.this, "取消收藏成功！");
							mCollBtn.setText("收藏");
							mIsColl = 0;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void OnError(String error) {

			}
		});
	}

	private void setBottomClick() {

		mFace.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mFaceView.getVisibility() == View.GONE) {
					mFaceView.setVisibility(View.VISIBLE);
					mFace.setImageResource(R.drawable.keyboard);
					SociaxUIUtils.hideSoftKeyboard(ZiXUnContentActivity.this, mCmtEdit);
				} else if (mFaceView.getVisibility() == View.VISIBLE) {
					mFaceView.setVisibility(View.GONE);
					mFace.setImageResource(R.drawable.smile_face);
					SociaxUIUtils.showSoftKeyborad(ZiXUnContentActivity.this, mCmtEdit);
				}
			}
		});

		mCmtEdit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mFaceView.getVisibility() == View.VISIBLE) {
					mFaceView.setVisibility(View.GONE);
					mFace.setImageResource(R.drawable.smile_face);
					SociaxUIUtils.showSoftKeyborad(ZiXUnContentActivity.this, mCmtEdit);
				}
			}
		});
	}

	private TSFaceView.FaceAdapter mFaceAdapter = new TSFaceView.FaceAdapter() {

		@Override
		public void doAction(int paramInt, String paramString) {
			// TODO Auto-generated method stub
			EditText localEditBlogView = mCmtEdit;
			int i = localEditBlogView.getSelectionStart();
			int j = localEditBlogView.getSelectionStart();
			String str1 = "[" + paramString + "]";
			String str2 = localEditBlogView.getText().toString();
			SpannableStringBuilder localSpannableStringBuilder = new SpannableStringBuilder();
			localSpannableStringBuilder.append(str2, 0, i);
			localSpannableStringBuilder.append(str1);
			localSpannableStringBuilder.append(str2, j, str2.length());
			SociaxUIUtils.highlightContent(ZiXUnContentActivity.this, localSpannableStringBuilder);
			localEditBlogView.setText(localSpannableStringBuilder, TextView.BufferType.SPANNABLE);
			localEditBlogView.setSelection(i + str1.length());
			Log.v("Tag", localEditBlogView.getText().toString());
		}
	};

	public Object onPopResult(Object object) {
		mChangeSizeUrl = object.toString();
		mContent.loadUrl(mChangeSizeUrl);
		return object;
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	private boolean isComment;
	private void loadData(boolean b) {
		isComment = b;
		final String url = MyConfig.ZIXUN_GET_URL + Utils.getTokenString(this) + "&id=" + mId + "&uid=" + mUid;
		NetComTools netComTools = NetComTools.getInstance(this);
		netComTools.getNetJson(url, new JsonDataListener() {
			@Override
			public void OnReceive(JSONObject jsonObject) {
				Log.d(TAG, "Zixun get Url data " + jsonObject.toString());
				try {
					int ret = jsonObject.getInt("code");
					if (ret == 0) {
						Log.i("loadData", jsonObject.toString());
						JSONObject data = jsonObject.getJSONObject("data");
						mUrl = data.getString("url");
						/**
						 * 是否点击评论而刷新页面，如果是则添加标签"#comment"，可直接定位到评论处
						 * */
						if(isComment){
							mUrl += "#comment";
						}
						mWebUrl = data.getString("weburl");
						mChangeSizeUrl = mUrl + Utils.getTokenString(ZiXUnContentActivity.this);
						mContent.loadUrl(mUrl);
						mIsColl = data.getInt("isColl");
						int is_praise = data.getInt("isPraise");
						mCurrentPraise = Integer.valueOf(data.getString("praiseCount"));
						String count = data.getString("count");
						mCmtEdit.setHint(count + "人评论过");
						initPraise(is_praise);
						if (mIsColl == 1)
							mCollBtn.setText("不收藏");
						else
							mCollBtn.setText("收藏");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void OnError(String error) {
				// Log.d(TAG, error);
			}
		});
	}
}
