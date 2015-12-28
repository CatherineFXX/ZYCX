package com.zhiyicx.zycx.activity;

import org.json.JSONObject;

import qcjlibrary.activity.base.BaseActivity;
import qcjlibrary.activity.base.Title;
import qcjlibrary.model.ModelMsg;
import qcjlibrary.model.ModelZiXunDetail;
import qcjlibrary.util.ToastUtils;
import qcjlibrary.util.UIUtils;
import qcjlibrary.widget.popupview.PopSizeChoose;
import qcjlibrary.widget.popupview.base.PopView;

import android.content.Intent;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.sso.UMSsoHandler;
import com.zhiyicx.zycx.R;
import com.zhiyicx.zycx.config.MyConfig;
import com.zhiyicx.zycx.net.JsonDataListener;
import com.zhiyicx.zycx.net.NetComTools;
import com.zhiyicx.zycx.sociax.component.TSFaceView;
import com.zhiyicx.zycx.sociax.unit.SociaxUIUtils;
import com.zhiyicx.zycx.util.Utils;

/**
 * Created by Administrator on 2014/12/27.
 */

public class ZiXUnContentActivity extends BaseActivity {

	final private static String TAG = "ZiXUnContentActivity";
	private final UMSocialService mController = UMServiceFactory
			.getUMSocialService("com.umeng.share");
	private WebView mContent;
	private int mId, mUid, mCid;
	private String mTitle;
	private String mUrl = null;
	private EditText mCmtEdit = null;
	private Button mCollBtn = null;
	private ImageView mFace = null;
	private TSFaceView mFaceView = null;
	private int mIsColl = 0;

	private ModelZiXunDetail mDetail = null;
	private Title mTitleLayout;

	@Override
	public String setCenterTitle() {
		return "咨询详情";
	}

	@Override
	public void initIntent() {
		Object object = getDataFromIntent(getIntent(), null);
		if (object instanceof ModelZiXunDetail) {
			mDetail = (ModelZiXunDetail) object;
			mId = Integer.valueOf(mDetail.getId());
			mUid = Integer.valueOf(mDetail.getUid());
			mTitle = mDetail.getTitle();
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
		mContent.getSettings().setJavaScriptEnabled(true);
		// mContent.loadUrl(mUrl);
		// mContent.loadUrl("javascript:getComment()");
		loadData();
		findViewById(R.id.btn_back).setOnClickListener(this);
		findViewById(R.id.btn_share).setOnClickListener(this);
		findViewById(R.id.btn_comment).setOnClickListener(this);
		findViewById(R.id.btn_collect).setOnClickListener(this);
		findViewById(R.id.btn_praise).setOnClickListener(this);
		mCollBtn = (Button) findViewById(R.id.btn_collect);
		mCmtEdit = (EditText) findViewById(R.id.edit_cmt);

		mFace = (ImageView) findViewById(R.id.face);
		mFaceView = (TSFaceView) findViewById(R.id.face_view);
		setBottomClick();
		mCmtEdit.clearFocus();
		mFaceView.setFaceAdapter(mFaceAdapter);

	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initListener() {
		mTitleLayout.iv_title_right1.setOnClickListener(this);
		mTitleLayout.iv_title_right3.setImageResource(R.drawable.aa);
		mTitleLayout.iv_title_right3.setVisibility(View.VISIBLE);
		mTitleLayout.iv_title_right3.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_praise:
			sendRequest(mApp.getZhiXunImpl().doPraise(mDetail), ModelMsg.class,
					BaseActivity.REQUEST_GET);
			break;
		case R.id.btn_back:
			finish();
			break;
		case R.id.iv_title_right3:
			PopView popView = new PopSizeChoose(this, null, this);
			popView.showPop(mTitleLayout.iv_title_right3, Gravity.TOP, 0, 0);
			break;
		case R.id.iv_title_right1:
			Utils.shareText(this, mController, "青稞网资讯分享:" + mTitle + " - ",
					mUrl);
			break;
		case R.id.btn_share:
			/*
			 * Intent intent = new Intent(Intent.ACTION_SEND);
			 * intent.putExtra(Intent.EXTRA_TEXT, mUrl);
			 * intent.setType("text/plain");
			 * intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			 * startActivity(Intent.createChooser(intent, "分享到"));
			 */
			Utils.shareText(this, mController, "青稞网资讯分享:" + mTitle + " - ",
					mUrl);
			break;
		case R.id.btn_comment:
			comment();
			break;
		case R.id.btn_collect:
			if (mIsColl == 1)
				collect(0);
			else
				collect(1);
			break;
		}
	}

	@Override
	public Object onResponceSuccess(String str, Class class1) {
		Object object = super.onResponceSuccess(str, class1);
		if (judgeTheMsg(object)) {
			ModelMsg modelMsg = (ModelMsg) object;
			ToastUtils.showToast(modelMsg.getMessage());
		}
		return object;
	}

	private void comment() {
		String txt = mCmtEdit.getText().toString().trim();
		if (TextUtils.isEmpty(txt))
			return;
		String url = MyConfig.ZIXUN_COMMENT_URL + "&id=" + mId + "&uid=" + mUid
				+ "&content=" + Utils.getUTF8String(txt)
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
						mContent.reload();
						mCmtEdit.setText("");
						// SociaxUIUtils.hideSoftKeyboard(ZiXUnContentActivity.this,
						// mCmtEdit);
					} else
						Utils.showToast(ZiXUnContentActivity.this, "评论失败，原因："
								+ jsonObject.get("message").toString());
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
		String url = MyConfig.ZIXUN_COLLECT_URL + Utils.getTokenString(this)
				+ "&id=" + mId + "&isColl=" + coll;
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
							Utils.showToast(ZiXUnContentActivity.this,
									"取消收藏成功！");
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
					mFace.setImageResource(R.drawable.key_bar);
					SociaxUIUtils.hideSoftKeyboard(ZiXUnContentActivity.this,
							mCmtEdit);
				} else if (mFaceView.getVisibility() == View.VISIBLE) {
					mFaceView.setVisibility(View.GONE);
					mFace.setImageResource(R.drawable.face_bar);
					SociaxUIUtils.showSoftKeyborad(ZiXUnContentActivity.this,
							mCmtEdit);
				}
			}
		});

		mCmtEdit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mFaceView.getVisibility() == View.VISIBLE) {
					mFaceView.setVisibility(View.GONE);
					mFace.setImageResource(R.drawable.key_bar);
					SociaxUIUtils.showSoftKeyborad(ZiXUnContentActivity.this,
							mCmtEdit);
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
			SociaxUIUtils.highlightContent(ZiXUnContentActivity.this,
					localSpannableStringBuilder);
			localEditBlogView.setText(localSpannableStringBuilder,
					TextView.BufferType.SPANNABLE);
			localEditBlogView.setSelection(i + str1.length());
			Log.v("Tag", localEditBlogView.getText().toString());
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		/** 使用SSO授权必须添加如下代码 */
		UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(
				requestCode);
		if (ssoHandler != null) {
			ssoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
	}

	private void loadData() {
		final String url = MyConfig.ZIXUN_GET_URL + Utils.getTokenString(this)
				+ "&id=" + mId + "&uid=" + mUid;
		NetComTools netComTools = NetComTools.getInstance(this);
		netComTools.getNetJson(url, new JsonDataListener() {
			@Override
			public void OnReceive(JSONObject jsonObject) {
				Log.d(TAG, "Zixun get Url data " + jsonObject.toString());
				try {
					int ret = jsonObject.getInt("code");
					if (ret == 0) {
						JSONObject data = jsonObject.getJSONObject("data");
						mUrl = data.getString("url");
						String wholeUrl = mUrl
								+ Utils.getTokenString(ZiXUnContentActivity.this);
						ToastUtils.showToast(wholeUrl);
						mContent.loadUrl(wholeUrl);
						mIsColl = data.getInt("isColl");
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
