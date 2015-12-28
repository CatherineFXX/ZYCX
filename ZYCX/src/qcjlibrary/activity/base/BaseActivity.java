package qcjlibrary.activity.base;

/***********************************************************************
 * Module:  BaseActivity.java
 * Author: qcj qq:260964739
 * Purpose: Defines the Class BaseActivity
 ***********************************************************************/

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Locale;

import org.apache.http.Header;

import qcjlibrary.adapter.base.BAdapter;
import qcjlibrary.config.Config;
import qcjlibrary.fragment.base.BaseFragment;

import qcjlibrary.model.ModelMsg;

import qcjlibrary.model.ModelUser;
import qcjlibrary.model.base.Model;
import qcjlibrary.request.base.Request;
import qcjlibrary.response.DataAnalyze;
import qcjlibrary.response.HttpResponceListener;
import qcjlibrary.util.Anim;
import qcjlibrary.util.BitmapUtil;
import qcjlibrary.util.ToastUtils;
import qcjlibrary.util.Uri2Path;
import qcjlibrary.widget.popupview.base.PopView.PopResultListener;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.zhiyicx.zycx.R;
import com.zhiyicx.zycx.sociax.android.Thinksns;

/**
 * activity的基类，任何activity都要继承它，并且实现里面的方法 一般不要轻易修改它
 * 
 * @pdOid
 */
public abstract class BaseActivity extends FragmentActivity implements
		OnClickListener, HttpResponceListener, TitleInterface,
		PopResultListener {
	/**
	 * activity的总布局，加入mTitleLayout和mBodyLayout
	 * 
	 * 
	 */
	public RelativeLayout mLayout;
	/** title 容器 */
	public LinearLayout mTitlell;
	/** 内容容器 */
	public FrameLayout mContentll;
	/** 底部容器 */
	public LinearLayout mBottomll;
	/** bundle数据 */
	public Bundle mBundle;
	/** title的布局 */
	private View mTitleLayout;
	private Title mTitleClass; // 用来封装title布局的类
	/** 內容的布局 */
	private View mBodyLayout;
	/** 底部的布局 */
	private View mBottomLayout;

	public LayoutInflater mInflater;
	// 全局应用
	public Thinksns mApp;

	// adapter的基类
	private BAdapter mAdapter;
	/**
	 * 方便子类替换content部分
	 */
	public FragmentManager mFManager = getSupportFragmentManager();

	/**
	 * 使用友盟来分享就是爽爽哒
	 */
	// 首先在您的Activity中添加如下成员变量
	final UMSocialService mController = UMServiceFactory
			.getUMSocialService("com.umeng.share");

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 竖屏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		// WindowManager.LayoutParams.FLAG_FULLSCREEN);
		initSet();
	}

	/**
	 * 初始化设置
	 */
	public void initSet() {
		mApp = (Thinksns) getApplication();
		mApp.setActivity(this);
		mInflater = LayoutInflater.from(getApplicationContext());
		initTheCommonLayout();
		// 把内容和title结合
		setContentView(combineTheLayout());
		initIntent();
		initView();
		initData();
		initListener();
		doRefreshNew();
	}

	/**
	 * 判断这个用户是否登录过
	 * 
	 * 如果个别fragement对用户开发的话，就重新这个方法，然后return true就可以了
	 * 
	 * @return
	 */
	public boolean checkTheUser() {
		ModelUser user = mApp.getUser();
		user.toString();
		if (user != null && user.getOauth_token() != null) {
			return true;
		}
		ToastUtils.showToast("请先登录");
		return false;
	}

	/**
	 * 根据布局id替换 fragment
	 * 
	 * @param layoutid
	 * @param fragment
	 */
	public void replaceFragment(int layoutid, BaseFragment fragment) {
		FragmentTransaction transaction = mFManager.beginTransaction();
		if (fragment != null) {
			transaction.replace(layoutid, fragment);
			transaction.commit();
		}
	}

	/** 初始化公共布局 */
	private void initTheCommonLayout() {
		mLayout = (RelativeLayout) mInflater.inflate(R.layout.comom_layout,
				null);
		mTitlell = (LinearLayout) mLayout.findViewById(R.id.ll_Title);
		mContentll = (FrameLayout) mLayout.findViewById(R.id.ll_content);
		mBottomll = (LinearLayout) mLayout.findViewById(R.id.ll_bottom);
	}

	/** 设置title的布局 */
	private void setTitleLayout() {
		String title = setCenterTitle();
		if (title != null && !title.equals("")) {
			mTitleLayout = mInflater.inflate(R.layout.qcj_title, null);
			mTitleClass = new Title(mTitleLayout, this);
			/*********** title的默认设置 ****************/
			this.titleSetCenterTitle(title);
			this.titleOnBackPress();
		} else {
			mTitlell.setVisibility(View.GONE);
		}
	}

	/********************************** title常用的方法 *****************************************/

	@Override
	public Title getTitleClass() {

		return mTitleClass;
	}

	@Override
	public void titleSetLeftImage(int resouceId) {
		if (mTitleClass != null) {
			mTitleClass.titleSetLeftImage(resouceId);
		}

	}

	@Override
	public void titleSetLeftImageAndTxt(int resouceId, String str) {
		if (mTitleClass != null) {
			mTitleClass.titleSetLeftImageAndTxt(resouceId, str);
		}
	}

	@Override
	public void titleOnBackPress() {
		if (mTitleClass != null) {
			mTitleClass.titleOnBackPress();
		}
	}

	@Override
	public void titleSlideMenu(DrawerLayout drawerLayout) {
		if (mTitleClass != null && drawerLayout != null) {
			mTitleClass.titleSlideMenu(drawerLayout);
		}
	}

	@Override
	public void titleSetCenterTitle(String title) {
		if (mTitleClass != null) {
			mTitleClass.titleSetCenterTitle(title);
		}

	}

	@Override
	public void titleSetCenterTwoTitle(String title1, String title2) {
		if (mTitleClass != null) {
			mTitleClass.titleSetCenterTwoTitle(title1, title2);
		}

	}

	@Override
	public void titleSetRightTitle(String rightTitle) {
		if (mTitleClass != null) {
			mTitleClass.titleSetRightTitle(rightTitle);
		}

	}

	@Override
	public void titleSetRightImage(int resouceId) {
		if (mTitleClass != null) {
			mTitleClass.titleSetRightImage(resouceId);
		}

	}

	/********************************** title常用的方法end *****************************************/
	/**
	 * 第一次无数据的时候刷新
	 */
	private void doRefreshNew() {
		if (mAdapter != null) {
			mAdapter.doRefreshNew();
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Anim.exit(this);
	}

	/**
	 * 设置底部布局
	 */
	private void setBottomlayout() {
		int id = getBottomLayoutId();
		if (id != 0) {
			mBottomll.setVisibility(View.VISIBLE);
			mBodyLayout = mInflater.inflate(R.layout.bottom_layout, mBottomll);
		}
	}

	/**
	 * 默认不实现底部布局，如果要实现的话就重新這個方法，增加其扩张性
	 * 
	 * @return 返回底部布局id
	 */
	public int getBottomLayoutId() {
		return 0;
	}

	/** 把内容和title全部加载到这个总布局里面 */
	public View combineTheLayout() {
		setTitleLayout();
		setBottomlayout();
		if (getLayoutId() > 0) {
			mBodyLayout = mInflater.inflate(getLayoutId(), null);
			mContentll.addView(mBodyLayout);
		}
		if (mTitleLayout != null)
			mTitlell.addView(mTitleLayout);
		if (mBottomLayout != null) {
			mBottomll.addView(mBottomLayout);
		}
		return mLayout;
	}

	/** 設置中間的title */
	public abstract String setCenterTitle();

	/**
	 * 设置控件是否显示
	 * 
	 * @param view
	 *            需要改变的控件
	 */
	public void setViewVisable(View view) {
		if (view != null) {
			view.setVisibility(View.VISIBLE);
		}
	}

	/** 獲取intent传来的内容 */
	public abstract void initIntent();

	/** 获取内容布局id */
	public abstract int getLayoutId();

	/** 初始化各个控件 */
	public abstract void initView();

	public abstract void initData();

	/** 初始化设置各个控件的监听器 */
	public abstract void initListener();

	public void refreshHeader() {
		if (mAdapter != null) {
			mAdapter.doRefreshHeader();
		}
	}

	/**
	 * @param adapter
	 *            需要设置的adapter
	 */
	public void setAdapter(BAdapter adapter) {
		this.mAdapter = adapter;
	}

	/**
	 * 设置底部可见
	 */
	public void setBottomVisible() {
		mBottomll.setVisibility(View.VISIBLE);
	}

	/**
	 * 设置底部不可见
	 */
	public void setBottomGone() {
		mBottomll.setVisibility(View.GONE);
	}

	/**
	 * 退出登录
	 */
	public void quitLogin() {
		SharedPreferences preferences = this.getSharedPreferences(
				Config.USER_DATA, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.remove(Config.OAUTH_TOKEN);
		editor.remove(Config.OAUTH_TOKEN_SECRET);
		editor.remove(Config.UID);
		editor.commit();
	}

	// ----------------------------------调用本地的图片，摄像机，文件之类的操作------------------------------------------------------
	public static final int IMAGE_CODE = 1; // 取照片的时做的标记
	public static final int CAPTURE_CODE = 2; // 取照片的时做的标记
	public static final int VEDIO_CODE = 3; // 获取本地视频
	public static final int FILE_CODE = 4; // 本地文件
	public static final int SHOOT_VIDEO = 5; // 拍摄视频

	/**
	 * 打开相册
	 */
	public void openTheGalley() {
		Intent intent = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(intent, IMAGE_CODE);
	}

	/**
	 * 打开照相机
	 */
	public void openTheCamera() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(intent, CAPTURE_CODE);
	}

	/**
	 * 获取本地视频列表
	 */
	public void openVedioFile() {
		Intent intent = new Intent(Intent.ACTION_PICK,
				MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(intent, VEDIO_CODE);
	}

	public void getLocalFile() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("*/*");
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		Intent intentChoose = Intent.createChooser(intent, "请选择文件!");
		try {
			startActivityForResult(intentChoose, FILE_CODE);
		} catch (android.content.ActivityNotFoundException ex) {
			// Potentially direct the user to the Market with a Dialog
			Toast.makeText(this, "请安装文件管理器", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 打开摄像机拍摄视频
	 */
	public void shootVideo() {
		Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
		// 设置视频大小
		intent.putExtra(android.provider.MediaStore.EXTRA_SIZE_LIMIT,
				20 * 1024 * 1024); // 设置为20M
		startActivityForResult(intent, SHOOT_VIDEO);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		try {
			ContentResolver resolver = getContentResolver();
			if (resultCode != Activity.RESULT_OK) {
				return;
			} else if (requestCode == IMAGE_CODE) {
				Uri originalUri = data.getData();
				// Log.i("TestFile", originalUri.toString());
				if (originalUri != null) {
					String filename = getPhotoPath(originalUri);
					getFile(filename);// 获取文件，子类实现它，然后就方便上传这个文件了
				}
			} else if (requestCode == CAPTURE_CODE) {
				String sdStatus = Environment.getExternalStorageState();
				if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
					Log.i("TestFile",
							"SD card is not avaiable/writeable right now.");
					return;
				}
				String name = DateFormat.format("yyyyMMdd_hhmmss",
						Calendar.getInstance(Locale.CHINA))
						+ ".jpg";
				Bundle bundle = data.getExtras();
				Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式
				FileOutputStream b = null;
				File file = new File("/sdcard/myImage/");
				file.mkdirs();// 创建文件夹
				String fileName = "/sdcard/myImage/" + name;
				Log.i("TestFile", fileName.toString());
				try {
					b = new FileOutputStream(fileName);
					bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b); // 把bitmap写入文件
					getFile(fileName); // 获取文件，子类实现它，然后就方便上传这个文件了
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} finally {
					try {
						b.flush();
						b.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			Log.i("tag", e.toString() + "");
			ToastUtils.showToast("获取图片抛出了异常！！");
		}
	}

	/**
	 * 通过图片路径获取bitmap
	 * 
	 * @param file
	 * @return
	 */
	public Bitmap getBitmapByFile(File file) {
		// TODO 以后坐等优化，后期估计是用imageloader
		if (file != null) {
			return BitmapUtil.getImageByPath(file.toString());
		}
		return null;
	}

	/************************* 设置返回上一个activity的数据 以及获取activity传来的数据 ******************************/
	public static final int ACTIVTIY_TRANFER = 2; // 用于activity数据传递

	/**
	 * 设置序列化返回
	 * 
	 * 只要传递的值满足序列化就可以了！不管是对象还是对象集合
	 * 
	 * 
	 * 该方法用于当前activity返回后给上一个acvivity传值 对应解析的方法为getReturnResultSeri
	 * 
	 * @param serializable
	 * @param flag
	 */
	public void setReturnResultSeri(Serializable serializable, String flag) {
		String defaultFlag = Config.ACTIVITY_TRANSFER_BUNDLE;
		if (flag != null) {
			defaultFlag = flag;
		}
		if (serializable != null) {
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putSerializable(defaultFlag, serializable);
			intent.putExtras(bundle);
			this.setResult(BaseActivity.ACTIVTIY_TRANFER, intent);
		}
	}

	/**
	 * 获取结果
	 * 
	 * @param flag
	 *            区分传的值
	 * @return
	 */
	public Serializable getReturnResultSeri(int resultCode, Intent intent,
			String flag) {
		String defaultFlag = Config.ACTIVITY_TRANSFER_BUNDLE;
		if (resultCode == BaseActivity.ACTIVTIY_TRANFER && intent != null) {
			if (flag != null) {
				defaultFlag = flag;
			}
			Bundle bundle = intent.getExtras();
			return bundle.getSerializable(defaultFlag);
		}
		return null;
	}

	/**
	 * 把数据封装到bundel中 用于传递
	 */
	public Bundle sendDataToBundle(Serializable serializable, String flag) {
		String defaultFlag = Config.ACTIVITY_TRANSFER_BUNDLE;
		if (flag != null) {
			defaultFlag = flag;
		}
		Bundle bundle = new Bundle();
		bundle.putSerializable(defaultFlag, serializable);
		return bundle;
	}

	/**
	 * 从intent获取里面的bundle然后在获取里面的值
	 * 
	 * @param intent
	 * @param flag
	 *            可以传可以不传，有默认的
	 * @return
	 */
	public Serializable getDataFromIntent(Intent intent, String flag) {
		String defaultFlag = Config.ACTIVITY_TRANSFER_BUNDLE;
		if (intent != null) {
			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				if (flag != null) {
					defaultFlag = flag;
				}
				return bundle.getSerializable(defaultFlag);
			}
		}
		return null;
	}

	/************************* 设置返回上一个activity的数据 end ******************************/

	/**
	 * 获取文件
	 * 
	 * @param path
	 * @return
	 */
	public File getFile(String path) {
		if (path != null) {
			return new File(path);
		}
		return null;
	}

	/**
	 * 同比例压缩图片，并且显示图片如果需要显示这个图片的话就 显示就交给子类重新这个方法，并实现
	 * 
	 * @param photo
	 */
	public Bitmap compressPhotoAndDisplay(Bitmap originBitmap) {
		// TODO 统统同比例压缩一倍， 这压缩太粗糙， 留在迭代开发做，现在如果做了，迭代开发干什么？
		originBitmap = ThumbnailUtils.extractThumbnail(originBitmap, 51, 108);
		return originBitmap;
	}

	public Bitmap compressOutStream2Bitmap(Bitmap bitmap, OutputStream stream) {
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
		return bitmap;
	}

	// ----------------------------------我是本区域邪恶的分界线------------------------------------------------------

	// ------------------------------------友盟初始化qq，微信，微博，人人等-----------------------
	/**
	 * 初始化需要分享的内容
	 */
	public void initShareContent() {
		// // 设置分享内容
		// mController
		// .setShareContent("友盟社会化组件（SDK）让移动应用快速整合社交分享功能，http://www.umeng.com/social");
		// 设置分享内容
		mController.setShareContent("线团APP");
		// mController.setsh
		// 设置分享图片, 参数2为图片的url地址
		mController.setShareMedia(new UMImage(this, "http://t.univs.cn/"));
		initQQShare();
		initQQZoneShare();
		initWeiXinShare();
		// 设置分享面板上显示的平台
		mController.getConfig().setPlatforms(SHARE_MEDIA.WEIXIN,
				SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE,
				SHARE_MEDIA.SINA, SHARE_MEDIA.TENCENT, SHARE_MEDIA.RENREN);
	}

	/**
	 * 初始化qq分享的内容
	 */
	private void initQQShare() {
		// 参数1为当前Activity， 参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(this, "1104831952",
				"ioLElezMMAJ94NHm");
		qqSsoHandler.addToSocialSDK();
	}

	/**
	 * 初始化qq空间分享的内容
	 */
	private void initQQZoneShare() {
		// 参数1为当前Activity， 参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(this,
				"1104831952", "ioLElezMMAJ94NHm");
		qZoneSsoHandler.addToSocialSDK();
	}

	/**
	 * 初始化微信分享
	 */
	private void initWeiXinShare() {
		// wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
		String appID = "wxf3ff5a169747d2f5";
		String appSecret = "14eeda3b22f601ae91bfd34b8d65574d";
		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(this, appID, appSecret);
		wxHandler.addToSocialSDK();
		// 支持微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(this, appID, appSecret);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();
	}

	/**
	 * 执行分享
	 */
	public void preformShare() {
		mController.openShare(this, false);
	}

	// ------------------------------------友盟初始化qq微信，微博，人人end------------------z

	/**************************** uri 与 filepath互转 *********************************************/
	/**
	 * 通过视频uri获取视频文件路径
	 * 
	 * @param uri
	 * @return
	 */
	public String getVideoPath(Uri uri) {
		if (uri != null) {
			return Uri2Path.getVideoPath(this, uri);
		}
		return null;

	}

	/**
	 * 通过uri获取图片路径
	 * 
	 * @param uri
	 * @return
	 */
	public String getPhotoPath(Uri uri) {
		if (uri != null) {
			return Uri2Path.getPhotoPath(this, uri);
		}
		return null;

	}

	/**
	 * 通过uri获取音乐路径
	 * 
	 * @param uri
	 * @return
	 */
	public String getMusicPath(Uri uri) {
		if (uri != null) {
			return Uri2Path.getMusicPath(this, uri);
		}
		return null;

	}

	/************************************ 网络请求传递，以及返回数据解析 ***************************************/
	private Request mRequst;
	public static final int REQUEST_GET = 0;
	public static final int REQUEST_POST = 1;

	public void sendRequest(RequestParams params,
			Class<? extends Model> modeltype, int requsetType) {
		if (params != null && modeltype != null) {
			if (mRequst == null) {
				mRequst = Request.getSingleRequest();
			}
			if (requsetType == REQUEST_GET) {
				mRequst.get(mApp.getHostUrl(), params,
						new MyAsyncHttpResponseHandler(modeltype));
			} else {
				mRequst.post(mApp.getHostUrl(), params,
						new MyAsyncHttpResponseHandler(modeltype));
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.zhiyisoft.associations.request.base.HttpResponceListener#
	 * onResponseProgress(long, long)
	 */
	@Override
	public void onResponseProgress(long bytesWritten, long totalSize) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.zhiyisoft.associations.request.base.HttpResponceListener#
	 * onResponceSuccess(java.lang.Object)
	 */
	@Override
	public Object onResponceSuccess(String str, Class class1) {
		return DataAnalyze.parseDataByGson(str, class1);
	}

	/**
	 * 判断返回的网络数据是否成功
	 * 
	 * @param object
	 * @return
	 */
	public boolean judgeTheMsg(Object object) {
		if (object instanceof ModelMsg) {
			ModelMsg msg = (ModelMsg) object;
			if (msg.getCode() == 0) {
				return true;
			}
			ToastUtils.showToast(msg.getMessage());
			return false;
		}
		return false;
	}

	private class MyAsyncHttpResponseHandler extends AsyncHttpResponseHandler {
		private Class type;

		public MyAsyncHttpResponseHandler(Class ResultType) {
			this.type = ResultType;
		}

		@Override
		public void onFailure(int arg0, Header[] arg1, byte[] arg2,
				Throwable arg3) {
			ToastUtils.showToast("请求异常");
		}

		@Override
		public void onProgress(long bytesWritten, long totalSize) {
			super.onProgress(bytesWritten, totalSize);
			onResponseProgress(bytesWritten, totalSize);
		}

		@Override
		public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
			if (arg2 != null) {
				String result = new String(arg2);
				Log.i("test", result.toString());
				onResponceSuccess(result, type);
			}
		}

	}

	/************************************ 网络请求传递，以及返回数据解析end ***************************************/
	/************** popWindow返回的数据 *******************************/
	public Object onPopResult(Object object) {
		return object;
	}
	/************** popWindow返回的数据 end *******************************/
}