package com.zhiyicx.zycx.sociax.android;

import java.io.File;
import java.util.ArrayList;
import java.util.Stack;
import java.util.WeakHashMap;

import qcjlibrary.activity.SearchNewActivity;
import qcjlibrary.activity.base.BaseActivity;
import qcjlibrary.api.api;
import qcjlibrary.config.Config;
import qcjlibrary.model.ModelUser;
import qcjlibrary.util.FileSizeUtil;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.baidu.mapapi.SDKInitializer;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.zhiyicx.zycx.R;
import com.zhiyicx.zycx.activity.HomeActivity;
import com.zhiyicx.zycx.sociax.api.Api;
import com.zhiyicx.zycx.sociax.db.AtMeSqlHelper;
import com.zhiyicx.zycx.sociax.db.AttachSqlHelper;
import com.zhiyicx.zycx.sociax.db.ChannelSqlHelper;
import com.zhiyicx.zycx.sociax.db.ChatMsgSqlhelper;
import com.zhiyicx.zycx.sociax.db.FavoritWeiboSqlHelper;
import com.zhiyicx.zycx.sociax.db.MyCommentSqlHelper;
import com.zhiyicx.zycx.sociax.db.MyMessageSqlhelper;
import com.zhiyicx.zycx.sociax.db.RemindSqlHelper;
import com.zhiyicx.zycx.sociax.db.SitesSqlHelper;
import com.zhiyicx.zycx.sociax.db.SqlHelper;
import com.zhiyicx.zycx.sociax.db.UserSqlHelper;
import com.zhiyicx.zycx.sociax.db.WeibaSqlHelper;
import com.zhiyicx.zycx.sociax.db.WeiboSqlHelper;
import com.zhiyicx.zycx.sociax.exception.ApiException;
import com.zhiyicx.zycx.sociax.exception.UserDataInvalidException;
import com.zhiyicx.zycx.sociax.modle.ApproveSite;
import com.zhiyicx.zycx.sociax.modle.ListData;
import com.zhiyicx.zycx.sociax.modle.SociaxItem;
import com.zhiyicx.zycx.sociax.modle.User;
import com.zhiyicx.zycx.sociax.net.Request;
import com.zhiyicx.zycx.sociax.unit.Anim;
import com.zhiyicx.zycx.sociax.unit.CommonLog;
import com.zhiyicx.zycx.sociax.unit.LogFactory;
import com.zhiyicx.zycx.util.PreferenceUtil;

public class Thinksns extends Application {
	private Api api;
	private Api.Friendships friendships;
	private Api.StatusesApi statuses;
	private Api.Oauth oauth;
	private Api.Favorites favorites;
	private Api.Message messages;
	private Api.Users users;
	private Api.NotifytionApi notifytionApi;
	private Api.Sites sites;
	private Api.STContacts contact;
	private Api.Tasks tasks;
	private Api.Questions questions;
	private Api.Documents documents;
	private Api.WeibaApi weibaApi;
	private Api.ChannelApi channelApi;
	private Api.GroupApi groupApi;
	private static ApproveSite mySite;
	private static int delIndex;

	public static final String NULL = "";
	private static final String TAG = "Thinksns";
	private static User my;
	private Stack<SqlHelper> sqlHelper;
	private static WeakHashMap<String, Bitmap> imageCache;
	private static ListData<SociaxItem> lastWeiboList;
	private CommonLog mCommonLog = LogFactory.createLog();
	public static Activity medicineAct;
	public static int id;
	public static SearchNewActivity searchAct;
	public static HomeActivity homeAct;

	@Override
	public void onCreate() {
		super.onCreate();
		// JPushInterface.setDebugMode(true);
		// JPushInterface.init(this);
		/********** qcj 2015-11-6添加 **************************/
		mApp = this;
		initImageLoader();
		initBaidu();
		/********** qcj 2015-11-6添加end **************************/
	}

	public Thinksns() {
		super();
		sqlHelper = new Stack<SqlHelper>();
		imageCache = new WeakHashMap<String, Bitmap>();
		mCommonLog.d("application start ...");
	}

	public Api.Status initOauth() throws ApiException {
		return this.getOauth().requestEncrypKey();
	}

	public void startActivity(Activity now, Class<? extends Activity> target, Bundle data) {
		Intent intent = new Intent();
		intent.setClass(now, target);
		if (data != null) {
			if (intent.getExtras() != null) {
				intent.replaceExtras(data);
			} else {
				intent.putExtras(data);
			}
		}
		now.startActivity(intent);
		Anim.in(now);
	}

	public void startActivityForResult(Activity now, Class<? extends Activity> target, Bundle data) {
		Intent intent = new Intent();
		intent.setClass(now, target);
		if (data != null) {
			if (intent.getExtras() != null) {
				intent.replaceExtras(data);
			} else {
				intent.putExtras(data);
			}
		}
		now.startActivityForResult(intent, 3456);
		Anim.in(now);
	}

	/**
	 * 初始化当前默认的站点信息 Thinksns.setMySite(as);
	 */
	public void initApi() {
		SitesSqlHelper db = this.getSiteSql();
		ApproveSite as = null;
		if (db.hasSites() == 0) {
			as = new ApproveSite();
			as.setUrl("http://" + getResources().getStringArray(R.array.site_url)[0]);
			as.setName(getResources().getString(R.string.app_name));
			db.addSites(as);
			mCommonLog.d("save initial site ...	");
		}

		try {
			as = db.getInUsed();
		} catch (Exception e) {
			this.api = Api.getInstance(getApplicationContext(), false, null);

			Thinksns.setMySite(as);
			return;
		}
		if (as == null) {
			this.api = Api.getInstance(getApplicationContext(), false, null);
		} else {
			Log.i(TAG, "app site info of db " + as.getUrl());
			this.api = Api.getInstance(getApplicationContext(), true, dealUrl(as.getUrl()));
		}
		Thinksns.setMySite(as);
	}

	public Api getApi() {
		return this.api;
	}

	/**
	 * 判断用户是否登录
	 * 
	 * @return
	 */
	public boolean isLogin() {
		PreferenceUtil preferenceUtil = PreferenceUtil.getInstance(getActivity());
		String token = preferenceUtil.getString("oauth_token", "");
		String token_secret = preferenceUtil.getString("oauth_token_secret", "");
		if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(token_secret)) {
			return true;
		}
		return false;
	}

	public static WeakHashMap<String, Bitmap> getImageCache() {
		return imageCache;
	}

	public boolean HasLoginUser() {
		UserSqlHelper db = this.getUserSql();
		try {
			User user = db.getLoginedUser();
			Request.setToken(user.getToken());
			Request.setSecretToken(user.getSecretToken());
			Thinksns.setMy(user);
			return true;
		} catch (UserDataInvalidException e) {
			return false;
		}
	}

	public UserSqlHelper getUserSql() {
		UserSqlHelper db = UserSqlHelper.getInstance(getApplicationContext());
		sqlHelper.add(db);
		return db;
	}

	public WeiboSqlHelper getWeiboSql() {
		WeiboSqlHelper db = WeiboSqlHelper.getInstance(getApplicationContext());
		sqlHelper.add(db);
		return db;
	}

	public FavoritWeiboSqlHelper getFavoritWeiboSql() {
		FavoritWeiboSqlHelper db = FavoritWeiboSqlHelper.getInstance(getApplicationContext());
		sqlHelper.add(db);
		return db;
	}

	public AtMeSqlHelper getAtMeSql() {
		AtMeSqlHelper db = AtMeSqlHelper.getInstance(getApplicationContext());
		sqlHelper.add(db);
		return db;
	}

	public MyMessageSqlhelper getMyMessageSql() {
		MyMessageSqlhelper db = MyMessageSqlhelper.getInstance(getApplicationContext());
		sqlHelper.add(db);
		return db;
	}

	public ChatMsgSqlhelper getChatMsgSql() {
		ChatMsgSqlhelper db = ChatMsgSqlhelper.getInstance(getApplicationContext());
		sqlHelper.add(db);
		return db;
	}

	public MyCommentSqlHelper getMyCommentSql() {
		MyCommentSqlHelper db = MyCommentSqlHelper.getInstance(getApplicationContext());
		sqlHelper.add(db);
		return db;
	}

	public SitesSqlHelper getSiteSql() {
		SitesSqlHelper db = SitesSqlHelper.getInstance(getApplicationContext());
		sqlHelper.add(db);
		return db;
	}

	public RemindSqlHelper getRemindSql() {
		RemindSqlHelper db = RemindSqlHelper.getInstance(getApplicationContext());
		sqlHelper.add(db);
		return db;
	}

	/*
	 * public MobileAppSqlHelper getMobileAppSql() { MobileAppSqlHelper db =
	 * MobileAppSqlHelper.getInstance(getApplicationContext());
	 * sqlHelper.add(db); return db; }
	 */

	public ChannelSqlHelper getChannelSql() {
		ChannelSqlHelper db = ChannelSqlHelper.getInstance(getApplicationContext());
		sqlHelper.add(db);
		return db;
	}

	public AttachSqlHelper getAttachSqlHelper() {
		AttachSqlHelper db = AttachSqlHelper.getInstance(getApplicationContext());
		sqlHelper.add(db);
		return db;
	}

	public WeibaSqlHelper getWeibaSql() {
		WeibaSqlHelper db = WeibaSqlHelper.getInstance(getApplicationContext());
		sqlHelper.add(db);
		return db;
	}

	public void closeDb() {
		if (!sqlHelper.empty()) {
			for (SqlHelper db : sqlHelper) {
				db.close();
			}
		}
	}

	public Api.Friendships getFriendships() {
		if (friendships == null) {
			friendships = new Api.Friendships();
		}
		return friendships;
	}

	public Api.StatusesApi getStatuses() {
		if (statuses == null) {
			statuses = new Api.StatusesApi();
		}
		return statuses;
	}

	public Api.Oauth getOauth() {
		if (oauth == null) {
			oauth = new Api.Oauth();
		}
		return oauth;
	}

	public Api.Favorites getFavorites() {
		if (favorites == null) {
			favorites = new Api.Favorites();
		}
		return favorites;
	}

	public Api.Message getMessages() {
		if (messages == null) {
			messages = new Api.Message();
		}
		return messages;
	}

	public Api.STContacts getContact() {
		if (contact == null) {
			contact = new Api.STContacts();
		}
		return contact;
	}

	public Api.Tasks getTasksApi() {
		if (tasks == null) {
			tasks = new Api.Tasks();
		}
		return tasks;
	}

	public Api.Questions getQuestionApi() {
		if (questions == null) {
			questions = new Api.Questions();
		}
		return questions;
	}

	public Api.Documents getDocument() {
		if (documents == null) {
			documents = new Api.Documents();
		}
		return documents;
	}

	/*
	 * public Api.MobileApps getMobileApps() { if (mobileApps == null) {
	 * mobileApps = new Api.MobileApps(); } return mobileApps; }
	 */

	public Api.WeibaApi getWeibaApi() {
		if (weibaApi == null) {
			weibaApi = new Api.WeibaApi();
		}
		return weibaApi;
	}

	public Api.ChannelApi getChannelApi() {
		if (channelApi == null) {
			channelApi = new Api.ChannelApi();
		}
		return channelApi;
	}

	public Api.GroupApi getGroupApi() {
		if (groupApi == null) {
			groupApi = new Api.GroupApi();
		}
		return groupApi;
	}

	public Api.Users getUsers() {
		if (users == null) {
			users = new Api.Users();
		}
		return users;
	}

	public Api.Sites getSites() {
		if (sites == null) {
			sites = new Api.Sites();
		}
		return sites;
	}

	public Api.NotifytionApi getApiNotifytion() {
		if (notifytionApi == null) {
			notifytionApi = new Api.NotifytionApi();
		}
		return notifytionApi;
	}

	public static User getMy() {
		return my;
	}

	public static void setMy(User my) {
		Thinksns.my = my;
	}

	public static ListData<SociaxItem> getLastWeiboList() {
		return lastWeiboList;
	}

	public static void setLastWeiboList(ListData<SociaxItem> lastWeiboList) {
		Thinksns.lastWeiboList = lastWeiboList;
	}

	public static String[] dealUrl(String url) {
		String[] tempUrl = new String[2];
		String temp = "";
		String[] buttonText = url.substring(7).split("/");
		if (buttonText.length == 1) {
			tempUrl[0] = buttonText[0];
			tempUrl[1] = "";
		} else {
			tempUrl[0] = buttonText[0];
			for (int i = 1; i < buttonText.length; i++) {
				temp += buttonText[i] + "/";
			}
			tempUrl[1] = "/" + temp;
		}
		Log.d(TAG, "tempUrl" + tempUrl[0] + "----" + tempUrl[1]);
		return tempUrl;
	}

	// 判断网络状态
	public boolean isNetWorkOn() {
		boolean netSataus = false;
		ConnectivityManager cwjManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cwjManager.getActiveNetworkInfo() != null) {
			netSataus = cwjManager.getActiveNetworkInfo().isAvailable();
		}
		return netSataus;
	}

	public static ArrayList<Activity> allActivity = new ArrayList<Activity>();// 保存Activity
	public static int lastActivityId;

	// 通过name获取Activity对象
	public static Activity getActivityByName(String name) {
		Activity getac = null;
		for (Activity ac : allActivity) {
			if (ac.getClass().getName().indexOf(name) >= 0) {
				getac = ac;
			}
		}
		return getac;
	}

	/**
	 * 退出程序
	 */
	public static void exitApp() {
		// if(null !=
		// (MainGridActivity)Thinksns.getActivityByName(MainGridActivity.TAG))
		// ((MainGridActivity)
		// Thinksns.getActivityByName(MainGridActivity.TAG)).exitApp();
		ArrayList<Activity> allActivity = Thinksns.getAllActivity();
		for (Activity activity : allActivity) {
			System.out.println("activityName" + activity.getClass().getName());
			activity.finish();
		}
		System.exit(0);
	}

	public static ArrayList<Activity> getAllActivity() {
		return allActivity;
	}

	public static int getDelIndex() {
		return delIndex;
	}

	public static void setDelIndex(int delIndex) {
		Thinksns.delIndex = delIndex;
	}

	public static ApproveSite getMySite() {
		return mySite;
	}

	public static void setMySite(ApproveSite mySite) {
		Thinksns.mySite = mySite;
	}

	public void clearCache() {
		getWeiboSql().clearCacheDB();
		getWeibaSql().clearCacheDB();
		getMyMessageSql().clearCacheDB();
		getMyCommentSql().clearCacheDB();
		getFavoritWeiboSql().clearCacheDB();
		getChatMsgSql().clearCacheDB();
		getChannelSql().clearCacheDB();
		getAtMeSql().clearCacheDB();
	}

	/*******************************
	 * 2015-11-6 qcj添加
	 ******************************************/
	private static Thinksns mApp;
	/** 定义一个user，整個app都用它 */
	private static ModelUser mUser;
	public ImageLoader mImageLoader;
	private Activity mActivity;

	/**
	 * 初始化百度定位
	 */
	private void initBaidu() {
		SDKInitializer.initialize(getApplicationContext());
	}

	public void setActivity(Activity activity) {
		this.mActivity = activity;
	}

	public Activity getActivity() {
		return mActivity;
	}

	/**
	 * @return 返回上下文
	 */
	public static Context getContext() {
		return mApp;
	}

	// ------------------------获取缓存-----------------------------------------------------------
	/*********************** imageLoader初始化 ***********************************/

	/**
	 * 获取保存图片的路径
	 * 
	 * @return
	 */
	public File getImagePath() {
		File cacheDir = StorageUtils.getOwnCacheDirectory(getApplicationContext(), "qingke/image");
		return cacheDir;
	}

	public File getFilePath() {
		final File dir = StorageUtils.getOwnCacheDirectory(getApplicationContext(), "qingke/file");
		return dir;
	}

	public ImageLoader initImageLoader() {
		// 创建默认的ImageLoader配置参数
		if (mImageLoader == null) {
			File cacheDir = getImagePath();
			ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
					.memoryCacheExtraOptions(480, 800)
					// default = device screen dimensions
					.threadPoolSize(3)
					// default
					.threadPriority(Thread.NORM_PRIORITY - 1)
					// default
					.tasksProcessingOrder(QueueProcessingType.FIFO)
					// default
					.denyCacheImageMultipleSizesInMemory().memoryCache(new LruMemoryCache(10 * 1024 * 1024))
					.memoryCacheSize(10 * 1024 * 1024).memoryCacheSizePercentage(13)
					// default
					.diskCache(new UnlimitedDiskCache(cacheDir))
					// 自定义缓存路径
					// default
					.diskCacheSize(80 * 1024 * 1024).diskCacheFileCount(100)
					.diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
					.imageDownloader(new BaseImageDownloader(this)) // default
					.defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
					.writeDebugLogs().writeDebugLogs().build();
			ImageLoader.getInstance().init(config);// 全局初始化此配置
			mImageLoader = ImageLoader.getInstance();
		}
		return mImageLoader;
	}

	/**
	 * 获取使用过的缓存
	 * 
	 * @return
	 */
	public double getUsedCache() {
		File file = getImagePath();
		double b = FileSizeUtil.getFileOrFilesSize(file.getPath(), 3);
		return b;
	}

	/**
	 * 清理掉缓存
	 * 
	 * @return
	 */
	public boolean cleanCache() {
		ImageLoader loader = initImageLoader();
		if (loader != null) {
			loader.clearDiskCache();
			loader.clearMemoryCache();
			return true;
		}
		return false;
	}

	/**
	 * 获取app版本号
	 * 
	 * @param context
	 * @return
	 */
	public int getAppVersion(Context context) {
		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			return info.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return 1;
	}

	public void displayImage(String path, ImageView imageView) {
		ImageLoader loader = initImageLoader();
		// 显示图片的配置
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.default_image_small).showImageOnFail(R.drawable.default_image_small)
				.cacheInMemory(true).cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565).build();
		loader.displayImage(path, imageView, options);
	}

	/*********************** imageLoader初始化end ***********************************/
	/*
	 * 
	 * 
	 * 
	 * // ------------------------获取缓存 end------------------------------------
	 * /** 获取用户的信息，主要是获取手机号码，登录密码，认证信息
	 * 
	 * @return
	 */
	public static ModelUser getUser() {
		mUser = new ModelUser();
		SharedPreferences preferences = getContext().getSharedPreferences(Config.USER_DATA, MODE_PRIVATE);
		String sex = preferences.getString(Config.SEX, "男");
		String intro = preferences.getString(Config.INTRO, null);
		String cancer = preferences.getString(Config.CANCER, null);
		String birthday = preferences.getString(Config.BIRTHDAY, null);
		String location = preferences.getString(Config.LOCATION, null);
		String city_ids = preferences.getString(Config.CITY_IDS, null);
		String uname = preferences.getString(Config.UNAME, null);
		String avatar = preferences.getString(Config.AVATAR, null);
		mUser.setSex(sex);
		mUser.setIntro(intro);
		mUser.setCancer(cancer);
		mUser.setBirthday(birthday);
		mUser.setLocation(location);
		mUser.setCity_ids(city_ids);
		mUser.setUname(uname);
		mUser.setAvatar(avatar);
		return mUser;
	}

	/**
	 * 把获取的user保存到SharePreference，方便下次调用
	 * 
	 * @param user
	 *            需要保存的用户
	 */
	public void saveUser(ModelUser user) {
		SharedPreferences preferences = getApplicationContext().getSharedPreferences(Config.USER_DATA,
				Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		// public static final String SEX = "sex";
		// public static final String INTRO = "intro";
		// public static final String CANCER = "cancer";
		// public static final String BIRTHDAY = "birthday";
		// public static final String LOCATION = "location";
		// public static final String CITY_IDS = "city_ids";
		// public static final String UNAME = "uname";
		// public static final String AVATAR = "avatar";
		if (user != null) {
			if (!TextUtils.isEmpty(user.getSex())) {
				editor.putString(Config.SEX, user.getSex());
			}
			if (!TextUtils.isEmpty(user.getIntro())) {
				editor.putString(Config.INTRO, user.getIntro());
			}
			if (!TextUtils.isEmpty(user.getCancer())) {
				editor.putString(Config.CANCER, user.getCancer());
			}
			if (!TextUtils.isEmpty(user.getBirthday())) {
				editor.putString(Config.BIRTHDAY, user.getBirthday());
			}
			if (!TextUtils.isEmpty(user.getLocation())) {
				editor.putString(Config.LOCATION, user.getLocation());
			}
			if (!TextUtils.isEmpty(user.getCity_ids())) {
				editor.putString(Config.CITY_IDS, user.getCity_ids());
			}
			if (!TextUtils.isEmpty(user.getUname())) {
				editor.putString(Config.UNAME, user.getUname());
			}

			if (!TextUtils.isEmpty(user.getAvatar())) {
				editor.putString(Config.AVATAR, user.getAvatar());
			}
		}
		editor.commit();
		// if (user.getMobile() != null) {
		// editor.putString(Config.MOBILE, user.getMobile());
		// }
		// if (user.getPwd() != null) {
		// editor.putString(Config.PWD, user.getPwd());
		// }
		//
		// editor.putString(Config.USERID, user.getUserid());
		// editor.putString(Config.OAUTH_TOKEN, user.getOauth_token());
		// editor.putString(Config.OAUTH_TOKEN_SECRET,
		// user.getOauth_token_secret());
		// editor.putString(Config.SCHOOL_ID, user.getschool_id());
		// editor.putString(Config.UNAME, user.getUname());
		// editor.putString(Config.SEX, user.getSex());
		// editor.putString(Config.IS_INIT, user.getIs_init());
		// editor.putString(Config.FACEURL, user.getFaceurl());
		// editor.putString(Config.SCHOOL_NAME, user.getSchool_name());
		// editor.putString(Config.AUTOGRAPH, user.getAutograph());
		// editor.putString(Config.EMAIL, user.getEmail());
	}

	/**
	 * @return 获取主机地址
	 */
	public static String getHostUrl() {
		// TODO 以后把地址写到xml里面
//		return "http://demo-qingko.zhiyicx.com/index.php?";
		return "http://www.qingko.com/index.php?";
	}

	// -----------------------------获取api------------------------------------------------------

	// -----------------------------获取api 结束-----------------------------
	public void startActivity_qcj(Activity now, Class<? extends Activity> target, Bundle data) {
		Intent intent = new Intent();
		intent.setClass(now, target);
		if (data != null) {
			if (intent.getExtras() != null) {
				intent.replaceExtras(data);
			} else {
				intent.putExtras(data);
			}
		}
		now.startActivity(intent);
		System.out.println("now" + now);
		System.out.println("target" + target);
		Anim.in(now);
	}

	public void startActivity(Activity now, Class<? extends Activity> target, Bundle data, int flag) {
		Intent intent = new Intent();
		intent.setClass(now, target);
		intent.setFlags(flag); // 注意本行的FLAG设置
		if (data != null) {
			if (intent.getExtras() != null) {
				intent.replaceExtras(data);
			} else {
				intent.putExtras(data);
			}
		}
		now.startActivity(intent);
		Anim.in(now);
	}

	public void startActivityForResult_qcj(Activity now, Class<? extends Activity> target, Bundle data) {
		Intent intent = new Intent();
		intent.setClass(now, target);
		if (data != null) {
			if (intent.getExtras() != null) {
				intent.replaceExtras(data);
			} else {
				intent.putExtras(data);
			}
		}
		now.startActivityForResult(intent, BaseActivity.ACTIVTIY_TRANFER);
		Anim.in(now);
	}

	/**
	 * 判断网络连接
	 * 
	 * @param context
	 * @return
	 */
	public boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	public static String getCache_path() {
		return "cache_path";
	}

	/*************************** 接口部分 ******************************/
	private qcjlibrary.api.api.ZhiXunImpl mZhixun;
	private qcjlibrary.api.api.RequestImpl mRequestImpl;
	private qcjlibrary.api.api.FoodImpl mFoodImpl;
	private qcjlibrary.api.api.UserImpl mUserImpl;
	private qcjlibrary.api.api.ExperienceImpl mExperienceImpl;
	private qcjlibrary.api.api.NotifyImpl mNotifyImpl;
	private qcjlibrary.api.api.MedRecordImpl mMedRecordImpl;

	public api.ZhiXunImpl getZhiXunImpl() {
		if (mZhixun == null) {
			mZhixun = new api.ZhiXunImpl();
		}
		return mZhixun;
	}

	public api.RequestImpl getRequestImpl() {
		if (mRequestImpl == null) {
			mRequestImpl = new api.RequestImpl();
		}
		return mRequestImpl;
	}

	public api.FoodImpl getFoodImpl() {
		if (mFoodImpl == null) {
			mFoodImpl = new api.FoodImpl();
		}
		return mFoodImpl;
	}

	public api.UserImpl getUserImpl() {
		if (mUserImpl == null) {
			mUserImpl = new api.UserImpl();
		}
		return mUserImpl;
	}

	public api.ExperienceImpl getExperienceImpl() {
		if (mExperienceImpl == null) {
			mExperienceImpl = new api.ExperienceImpl();
		}
		return mExperienceImpl;
	}

	public api.NotifyImpl getNotifyImpl() {
		if (mNotifyImpl == null) {
			mNotifyImpl = new api.NotifyImpl();
		}
		return mNotifyImpl;
	}

	public api.MedRecordImpl getMedRecordImpl() {
		if (mMedRecordImpl == null) {
			mMedRecordImpl = new api.MedRecordImpl();
		}
		return mMedRecordImpl;
	}

	/*************************** 接口部分end ******************************/
}
