package com.zhiyicx.zycx.sociax.api;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.zhiyicx.zycx.R;
import com.zhiyicx.zycx.sociax.exception.ApiException;
import com.zhiyicx.zycx.sociax.exception.CommentListAreEmptyException;
import com.zhiyicx.zycx.sociax.exception.DataInvalidException;
import com.zhiyicx.zycx.sociax.exception.HostNotFindException;
import com.zhiyicx.zycx.sociax.exception.ListAreEmptyException;
import com.zhiyicx.zycx.sociax.exception.SiteDataInvalidException;
import com.zhiyicx.zycx.sociax.exception.UpdateContentBigException;
import com.zhiyicx.zycx.sociax.exception.UpdateContentEmptyException;
import com.zhiyicx.zycx.sociax.exception.UpdateException;
import com.zhiyicx.zycx.sociax.exception.UserDataInvalidException;
import com.zhiyicx.zycx.sociax.exception.UserListAreEmptyException;
import com.zhiyicx.zycx.sociax.exception.VerifyErrorException;
import com.zhiyicx.zycx.sociax.exception.WeiBoListAreEmptyException;
import com.zhiyicx.zycx.sociax.exception.WeiboDataInvalidException;
import com.zhiyicx.zycx.sociax.modle.ApproveSite;
import com.zhiyicx.zycx.sociax.modle.Channel;
import com.zhiyicx.zycx.sociax.modle.Comment;
import com.zhiyicx.zycx.sociax.modle.CommentPost;
import com.zhiyicx.zycx.sociax.modle.Contact;
import com.zhiyicx.zycx.sociax.modle.ContactCategory;
import com.zhiyicx.zycx.sociax.modle.Document;
import com.zhiyicx.zycx.sociax.modle.Follow;
import com.zhiyicx.zycx.sociax.modle.ListData;
import com.zhiyicx.zycx.sociax.modle.NotifyCount;
import com.zhiyicx.zycx.sociax.modle.NotifyItem;
import com.zhiyicx.zycx.sociax.modle.Posts;
import com.zhiyicx.zycx.sociax.modle.QuesCate;
import com.zhiyicx.zycx.sociax.modle.Question;
import com.zhiyicx.zycx.sociax.modle.ReceiveComment;
import com.zhiyicx.zycx.sociax.modle.RecentTopic;
import com.zhiyicx.zycx.sociax.modle.SearchUser;
import com.zhiyicx.zycx.sociax.modle.SociaxItem;
import com.zhiyicx.zycx.sociax.modle.StringItem;
import com.zhiyicx.zycx.sociax.modle.SystemNotify;
import com.zhiyicx.zycx.sociax.modle.Task;
import com.zhiyicx.zycx.sociax.modle.TaskCategory;
import com.zhiyicx.zycx.sociax.modle.User;
import com.zhiyicx.zycx.sociax.modle.VersionInfo;
import com.zhiyicx.zycx.sociax.modle.Weiba;
import com.zhiyicx.zycx.sociax.modle.Weibo;
import com.zhiyicx.zycx.sociax.modle.NotifyCount.Type;
import com.zhiyicx.zycx.sociax.net.Get;
import com.zhiyicx.zycx.sociax.net.Post;
import com.zhiyicx.zycx.sociax.net.Request;
import com.zhiyicx.zycx.sociax.unit.Compress;
import com.zhiyicx.zycx.sociax.unit.DES;
import com.zhiyicx.zycx.sociax.unit.FormFile;
import com.zhiyicx.zycx.sociax.unit.FormPost;
import com.zhiyicx.zycx.sociax.unit.MD5;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

public class Api {
	public static final String TAG = "ThinksnsApi";

	public static final String APP_TAG = "Sociax";

	public static enum Status {
		REQUESTING, SUCCESS, ERROR, RESULT_ERROR, REQUEST_ENCRYP_KEY
	}

	private static String mHost;
	private static String mPath;
	private static String url;
	private static Context mContext;
	private static Request post;
	private static Request get;
	private static Api instance;
	private static final String APP_NAME = "api";

	private Api(Context context) {
		Api.setContext(context);
		String[] configHost = context.getResources().getStringArray(
				R.array.site_url);
		Api.setHost(configHost[0]);
		Api.setPath(configHost[1]);
		Api.post = new Post();
		Api.get = new Get();
	}

	private Api(String host, String path, Context context) {
		Api.setContext(context);
		Api.setHost(host);
		Api.setPath(path);
		Api.post = new Post();
		Api.get = new Get();
	}

	public static Api getInstance(Context context, boolean type, String[] url) {
		if (!type) {
			Api.instance = new Api(context);
		} else {
			Api.instance = new Api(url[0], url[1], context);
		}
		return Api.instance;
	}

	private static Uri.Builder createUrlBuild(String mod, String act) {
		return createUrlBuild(APP_NAME, mod, act);
	}

	private static Uri.Builder createUrlBuild(String app, String mod, String act) {
		Uri.Builder uri = new Uri.Builder();
		uri.scheme("http");
		uri.authority(Api.getHost());
		uri.appendEncodedPath(Api.getPath());
		uri.appendQueryParameter("app", app);
		uri.appendQueryParameter("mod", mod);
		uri.appendQueryParameter("act", act);
		Log.d(TAG, " url " + uri.toString());
		return uri;
	}

	private static Uri.Builder createForCheck(String api, String mod, String act) {
		Uri.Builder uri = new Uri.Builder();
		uri.scheme("http");
		uri.authority(Api.getHost());
		uri.appendEncodedPath(Api.getPath());
		uri.appendQueryParameter("app", api);
		uri.appendQueryParameter("mod", mod);
		uri.appendQueryParameter("act", act);
		return uri;
	}

	private static Uri.Builder createThinksnsUrlBuild(String api, String mod,
			String act) {
		Uri.Builder uri = new Uri.Builder();
		uri.scheme("http");
		uri.authority("demo-qingko.zhiyicx.com");
		uri.appendEncodedPath("");
		uri.appendQueryParameter("app", api);
		uri.appendQueryParameter("mod", mod);
		uri.appendQueryParameter("act", act);
		return uri;
	}

	private static Object run(Request req) throws ApiException {
		try {
			Log.e(APP_TAG, "begin http reques ===> ...");
			return req.run();
		} catch (ClientProtocolException e) {
			Log.e(APP_TAG, e.toString());
			throw new ApiException(e.getMessage());
		} catch (HostNotFindException e) {
			Log.e(APP_TAG, e.toString());
			throw new ApiException("服务请求地址不正确，请联系开发者");
		} catch (IOException e) {
			Log.e(APP_TAG, e.toString());
			throw new ApiException("网络服务故障,请稍后重试");
		}
	}

	private static Status checkResult(Object result) {
		if (result == null)
			return Status.ERROR;
		if (result.equals(Api.Status.ERROR)) {
			return Api.Status.ERROR;
		}
		return Api.Status.SUCCESS;
	}

	private static void checkHasVerifyError(JSONObject result)
			throws VerifyErrorException, ApiException {
		if (result.has("code") && result.has("message")) {
			try {
				throw new VerifyErrorException(result.getString("message"));
			} catch (JSONException e) {
				throw new ApiException("暂无更多数据");
			}
		}
	}

	private static SociaxItem getSociaxItem(ListData.DataType type,
			JSONObject jsonObject) throws DataInvalidException, ApiException {
		if (type == ListData.DataType.COMMENT) {
			return new Comment(jsonObject);
		} else if (type == ListData.DataType.WEIBO) {
			return new Weibo(jsonObject);
		} else if (type == ListData.DataType.USER) {
			return new User(jsonObject);
		} else if (type == ListData.DataType.RECEIVE) {
			return new ReceiveComment(jsonObject);
		} else if (type == ListData.DataType.FOLLOW) {
			return new Follow(jsonObject, "");
		} else if (type == ListData.DataType.SEARCH_USER) {
			return new SearchUser(jsonObject);
		} else {
			throw new ApiException("参数错误");
		}
	}

	/**
	 * 账号认证Api类
	 * 
	 * @author Povol
	 */
	public static final class Oauth implements ApiOauth {
		private String encryptKey;

		public Oauth() {
		}

		@Override
		public User authorize(String uname, String password)
				throws ApiException, UserDataInvalidException,
				VerifyErrorException {
			Uri.Builder uri = Api.createUrlBuild(ApiOauth.MOD_NAME,
					ApiOauth.AUTHORIZE);
			Api.post.setUri(uri);
			Log.i(APP_TAG, "uname=" + uname + ",password" + password);
			try {
				Log.i("加密密钥=========", encryptKey + "呵呵哒");
				Api.post.append("uid", DES.encrypt1(uname, this.encryptKey))
						.append("passwd",
								DES.encrypt1(MD5.encryptMD5(password),
										this.encryptKey));
			} catch (Exception e1) {
				Log.d(APP_TAG, "api ====>>>  账号密码加密失败, wm " + e1.toString());
				throw new ApiException("账号密码加密失败");
			}
			Object result = Api.run(Api.post);
			Api.checkResult(result);

			try {
				JSONObject data = new JSONObject((String) result);
				data = data.getJSONObject("data");
				Api.checkHasVerifyError(data);
				String oauth_token = data.getString("oauth_token");
				String oauth_token_secret = data
						.getString("oauth_token_secret");
				int uid = data.getInt("uid");
				return new User(uid, uname, password, oauth_token,
						oauth_token_secret);
			} catch (JSONException e) {
				Log.d(APP_TAG, "api ====>>>  验证失败");
				throw new UserDataInvalidException("验证失败");
			}
		}

		public User authorize(String uname, String password, String type_uid,
				String type) throws ApiException, UserDataInvalidException,
				VerifyErrorException {
			Uri.Builder uri = Api.createUrlBuild(ApiOauth.MOD_NAME,
					ApiOauth.AUTHORIZE);
			Api.post.setUri(uri);
			try {
				Api.post.append("uid", DES.encrypt1(uname, this.encryptKey))
						.append("passwd",
								DES.encrypt1(MD5.encryptMD5(password),
										this.encryptKey))
						.append("type_uid", type_uid).append("type", type);
			} catch (Exception e1) {
				Log.d(APP_TAG, "api ====>>>  账号密码加密失败, wm " + e1.toString());
				throw new ApiException("账号密码加密失败");
			}
			Object result = Api.run(Api.post);
			Api.checkResult(result);

			try {
				JSONObject data = new JSONObject((String) result);
				data = data.getJSONObject("data");
				Api.checkHasVerifyError(data);
				String oauth_token = data.getString("oauth_token");
				String oauth_token_secret = data
						.getString("oauth_token_secret");
				int uid = data.getInt("uid");
				return new User(uid, uname, password, oauth_token,
						oauth_token_secret);
			} catch (JSONException e) {
				Log.d(APP_TAG, "api ====>>>  验证失败");
				throw new UserDataInvalidException("验证失败");
			}
		}

		public void setEmptyKey() {
			this.encryptKey = "";
		}

		@Override
		public Status requestEncrypKey() throws ApiException {
			Uri.Builder uri = Api.createUrlBuild(ApiOauth.MOD_NAME,
					ApiOauth.REQUEST_ENCRYP);
			Api.post.setUri(uri);
			Object result = Api.run(Api.post);

			Api.checkResult(result);
			try {
				JSONArray encrypt = new JSONArray((String) result);
				this.encryptKey = encrypt.getString(0);
			} catch (JSONException e) {
				return Api.Status.RESULT_ERROR;
			}
			return Api.Status.REQUEST_ENCRYP_KEY;
		}

		@Override
		public int register(Object data) throws ApiException {
			Uri.Builder uri = Api.createUrlBuild(ApiOauth.MOD_NAME,
					ApiOauth.REGISTER);
			Post post = new Post();
			post.setUri(uri);
			String[] dataArray = (String[]) data;
			post.append("uname", dataArray[0]);
			post.append("sex", dataArray[3]);
			post.append("password", dataArray[2]);
			post.append("email", dataArray[1]); // (1：男,2：女)
			Object o = Api.run(post); // 成功：1 失败：0 邮箱不合格 2 昵称重复 3
			int result = 0;
			result = Integer.valueOf(o.toString());
			System.err.println(result);
			return result;
		}
	}

	public static final class StatusesApi implements ApiStatuses {
		@Override
		public Weibo show(int id) throws ApiException,
				WeiboDataInvalidException, VerifyErrorException {
			Uri.Builder uri = Api.createUrlBuild(ApiStatuses.MOD_NAME,
					ApiStatuses.SHOW);
			Api.get.setUri(uri);
			Api.get.append("id", id);
			Object result = Api.run(Api.get);
			Api.checkResult(result);
			try {
				JSONObject data = new JSONObject((String) result);
				Api.checkHasVerifyError(data);
				return new Weibo(new JSONObject((String) result));
			} catch (JSONException e) {
				throw new WeiboDataInvalidException("请求微博不存在");
			}
		}

		@Override
		public boolean destroyWeibo(Weibo weibo) throws ApiException,
				VerifyErrorException, DataInvalidException {
			Uri.Builder uri = Api.createUrlBuild(ApiStatuses.MOD_NAME,
					ApiStatuses.DESTROY);
			Api.post.setUri(uri);
			Api.post.append("id", weibo.getWeiboId());
			Api.post.append("uid", weibo.getUid());
			Object result = Api.run(Api.post);
			Api.checkResult(result);
			if (result.equals("\"false\""))
				return false;
			return true;
		}

		@Override
		public boolean destroyComment(Comment coment) throws ApiException,
				VerifyErrorException, DataInvalidException {
			Uri.Builder uri = Api.createUrlBuild(ApiStatuses.MOD_NAME,
					ApiStatuses.COMMENT_DESTROY);
			Api.post.setUri(uri);
			Api.post.append("id", coment.getCommentId());
			Object result = Api.run(Api.post);
			Api.checkResult(result);
			boolean isDel = false;
			try {
				Integer temp = new Integer((String) result);
				if (temp.equals("1")) {
					isDel = false;
				} else {
					isDel = true;
				}
			} catch (Exception e) {
				// TODO: handle exception
				isDel = true;
			}
			return isDel;
		}

		@SuppressWarnings("unchecked")
		@Override
		public ListData<SociaxItem> search(String key, int count)
				throws ApiException, VerifyErrorException,
				ListAreEmptyException, DataInvalidException {
			this.beforeTimeline(ApiStatuses.SEARCH);
			Api.get.append("key", key);
			return (ListData<SociaxItem>) this.afterTimeLine(count,
					ListData.DataType.WEIBO);
		}

		@SuppressWarnings("unchecked")
		@Override
		public ListData<SociaxItem> searchHeader(String key, Weibo item,
				int count) throws ApiException, VerifyErrorException,
				ListAreEmptyException, DataInvalidException {
			this.beforeTimeline(ApiStatuses.SEARCH);
			Api.get.append("since_id", item.getWeiboId());
			Api.get.append("key", key);
			return (ListData<SociaxItem>) this.afterTimeLine(count,
					ListData.DataType.WEIBO);
		}

		@SuppressWarnings("unchecked")
		@Override
		public ListData<SociaxItem> searchFooter(String key, Weibo item,
				int count) throws ApiException, VerifyErrorException,
				ListAreEmptyException, DataInvalidException {
			this.beforeTimeline(ApiStatuses.SEARCH);
			Api.get.append("max_id", item.getWeiboId());
			Api.get.append("key", key);
			return (ListData<SociaxItem>) this.afterTimeLine(count,
					ListData.DataType.WEIBO);
		}

		@SuppressWarnings("unchecked")
		@Override
		public ListData<SociaxItem> mentions(int count) throws ApiException,
				VerifyErrorException, ListAreEmptyException,
				DataInvalidException {
			this.beforeTimeline(ApiStatuses.MENTION);
			return (ListData<SociaxItem>) this.afterTimeLine(count,
					ListData.DataType.WEIBO);
		}

		@SuppressWarnings("unchecked")
		@Override
		public ListData<SociaxItem> mentionsHeader(Weibo item, int count)
				throws ApiException, VerifyErrorException,
				ListAreEmptyException, DataInvalidException {
			this.beforeTimeline(ApiStatuses.MENTION);
			Api.get.append("since_id", item.getWeiboId());
			return (ListData<SociaxItem>) this.afterTimeLine(count,
					ListData.DataType.WEIBO);
		}

		@SuppressWarnings("unchecked")
		@Override
		public ListData<SociaxItem> mentionsFooter(Weibo item, int count)
				throws ApiException, VerifyErrorException,
				ListAreEmptyException, DataInvalidException {
			this.beforeTimeline(ApiStatuses.MENTION);
			Api.get.append("max_id", item.getWeiboId());
			return (ListData<SociaxItem>) this.afterTimeLine(count,
					ListData.DataType.WEIBO);
		}

		@SuppressWarnings("unchecked")
		@Override
		public ListData<SociaxItem> friendsTimeline(int count)
				throws ApiException, VerifyErrorException,
				ListAreEmptyException, DataInvalidException {
			this.beforeTimeline(ApiStatuses.FRIENDS_TIMELINE);
			return (ListData<SociaxItem>) this.afterTimeLine(count,
					ListData.DataType.WEIBO);
		}

		@SuppressWarnings("unchecked")
		@Override
		public ListData<SociaxItem> friendsHeaderTimeline(Weibo item, int count)
				throws ApiException, VerifyErrorException,
				ListAreEmptyException, DataInvalidException {

			this.beforeTimeline(ApiStatuses.FRIENDS_TIMELINE);
			Api.get.append("since_id", item.getWeiboId());
			return (ListData<SociaxItem>) this.afterTimeLine(count,
					ListData.DataType.WEIBO);
		}

		@SuppressWarnings("unchecked")
		@Override
		public ListData<SociaxItem> friendsFooterTimeline(Weibo item, int count)
				throws ApiException, VerifyErrorException,
				ListAreEmptyException, DataInvalidException {
			this.beforeTimeline(ApiStatuses.FRIENDS_TIMELINE);
			Api.get.append("max_id", item.getWeiboId());
			return (ListData<SociaxItem>) this.afterTimeLine(count,
					ListData.DataType.WEIBO);
		}

		@SuppressWarnings("unchecked")
		@Override
		public ListData<SociaxItem> publicTimeline(int count)
				throws ApiException, VerifyErrorException,
				ListAreEmptyException, DataInvalidException {
			this.beforeTimeline(ApiStatuses.PUBLIC_TIMELINE);
			return (ListData<SociaxItem>) this.afterTimeLine(count,
					ListData.DataType.WEIBO);
		}

		@SuppressWarnings("unchecked")
		@Override
		public ListData<SociaxItem> publicHeaderTimeline(Weibo item, int count)
				throws ApiException, VerifyErrorException,
				ListAreEmptyException, DataInvalidException {
			this.beforeTimeline(ApiStatuses.PUBLIC_TIMELINE);
			Api.get.append("since_id", item.getWeiboId());
			return (ListData<SociaxItem>) this.afterTimeLine(count,
					ListData.DataType.WEIBO);
		}

		@SuppressWarnings("unchecked")
		@Override
		public ListData<SociaxItem> publicFooterTimeline(Weibo item, int count)
				throws ApiException, VerifyErrorException,
				ListAreEmptyException, DataInvalidException {
			this.beforeTimeline(ApiStatuses.PUBLIC_TIMELINE);
			Api.get.append("max_id", item.getWeiboId());
			return (ListData<SociaxItem>) this.afterTimeLine(count,
					ListData.DataType.WEIBO);
		}

		@SuppressWarnings("unchecked")
		@Override
		public ListData<SociaxItem> userTimeline(User user, int count)
				throws ApiException, VerifyErrorException,
				ListAreEmptyException, DataInvalidException {
			this.beforeTimeline(ApiStatuses.USER_TIMELINE);
			Api.get.append("user_id", user.getUid());
			return (ListData<SociaxItem>) this.afterTimeLine(count,
					ListData.DataType.WEIBO);
		}

		@SuppressWarnings("unchecked")
		@Override
		public ListData<SociaxItem> userHeaderTimeline(User user, Weibo item,
				int count) throws ApiException, VerifyErrorException,
				ListAreEmptyException, DataInvalidException {
			this.beforeTimeline(ApiStatuses.USER_TIMELINE);
			Api.get.append("since_id", item.getWeiboId());
			Api.get.append("user_id", user.getUid());
			return (ListData<SociaxItem>) this.afterTimeLine(count,
					ListData.DataType.WEIBO);
		}

		@SuppressWarnings("unchecked")
		@Override
		public ListData<SociaxItem> userFooterTimeline(User user, Weibo item,
				int count) throws ApiException, VerifyErrorException,
				ListAreEmptyException, DataInvalidException {
			this.beforeTimeline(ApiStatuses.USER_TIMELINE);
			Api.get.append("max_id", item.getWeiboId());
			Api.get.append("user_id", user.getUid());
			return (ListData<SociaxItem>) this.afterTimeLine(count,
					ListData.DataType.WEIBO);
		}

		@SuppressWarnings("unchecked")
		@Override
		public ListData<Comment> commentTimeline(int count)
				throws ApiException, DataInvalidException,
				VerifyErrorException, ListAreEmptyException {
			this.beforeTimeline(ApiStatuses.COMMENT_TIMELINE);
			return (ListData<Comment>) this.afterTimeLine(count,
					ListData.DataType.COMMENT);
		}

		@SuppressWarnings("unchecked")
		@Override
		public ListData<Comment> commentHeaderTimeline(Comment item, int count)
				throws ApiException, DataInvalidException,
				VerifyErrorException, ListAreEmptyException {
			this.beforeTimeline(ApiStatuses.COMMENT_TIMELINE);
			Api.get.append("since_id", item.getCommentId());
			return (ListData<Comment>) this.afterTimeLine(count,
					ListData.DataType.COMMENT);
		}

		@SuppressWarnings("unchecked")
		@Override
		public ListData<Comment> commentFooterTimeline(Comment item, int count)
				throws ApiException, VerifyErrorException,
				ListAreEmptyException, DataInvalidException {
			this.beforeTimeline(ApiStatuses.COMMENT_TIMELINE);
			Api.get.append("max_id", item.getCommentId());
			return (ListData<Comment>) this.afterTimeLine(count,
					ListData.DataType.COMMENT);
			// TODO Auto-generated method stub
		}

		@SuppressWarnings("unchecked")
		@Override
		public ListData<SociaxItem> commentMyTimeline(int count)
				throws ApiException, DataInvalidException,
				VerifyErrorException, ListAreEmptyException {
			this.beforeTimeline(ApiStatuses.COMMENT_BY_ME);
			return (ListData<SociaxItem>) this.afterTimeLine(count,
					ListData.DataType.RECEIVE);
		}

		@SuppressWarnings("unchecked")
		@Override
		public ListData<SociaxItem> commentMyHeaderTimeline(Comment item,
				int count) throws ApiException, DataInvalidException,
				VerifyErrorException, ListAreEmptyException {
			this.beforeTimeline(ApiStatuses.COMMENT_BY_ME);
			Api.get.append("since_id", item.getCommentId());
			return (ListData<SociaxItem>) this.afterTimeLine(count,
					ListData.DataType.RECEIVE);
		}

		@SuppressWarnings("unchecked")
		@Override
		public ListData<SociaxItem> commentMyFooterTimeline(Comment item,
				int count) throws ApiException, VerifyErrorException,
				ListAreEmptyException, DataInvalidException {
			this.beforeTimeline(ApiStatuses.COMMENT_BY_ME);
			Api.get.append("max_id", item.getCommentId());
			return (ListData<SociaxItem>) this.afterTimeLine(count,
					ListData.DataType.RECEIVE);
		}

		@SuppressWarnings("unchecked")
		@Override
		public ListData<SociaxItem> commentForWeiboTimeline(Weibo item,
				int count) throws ApiException, DataInvalidException,
				VerifyErrorException, ListAreEmptyException {
			this.beforeTimeline(ApiStatuses.COMMENTS);
			Api.get.append("id", item.getWeiboId());
			return (ListData<SociaxItem>) this.afterTimeLine(count,
					ListData.DataType.COMMENT);
		}

		@SuppressWarnings("unchecked")
		@Override
		public ListData<SociaxItem> commentForWeiboHeaderTimeline(Weibo item,
				Comment comment, int count) throws ApiException,
				DataInvalidException, VerifyErrorException,
				ListAreEmptyException {
			this.beforeTimeline(ApiStatuses.COMMENTS);
			Api.get.append("since_id", comment.getCommentId());
			Api.get.append("id", item.getWeiboId());
			return (ListData<SociaxItem>) this.afterTimeLine(count,
					ListData.DataType.COMMENT);
		}

		@SuppressWarnings("unchecked")
		@Override
		public ListData<SociaxItem> commentForWeiboFooterTimeline(Weibo item,
				Comment comment, int count) throws ApiException,
				VerifyErrorException, ListAreEmptyException,
				DataInvalidException {
			this.beforeTimeline(ApiStatuses.COMMENTS);
			Api.get.append("max_id", comment.getCommentId());
			Api.get.append("id", item.getWeiboId());
			return (ListData<SociaxItem>) this.afterTimeLine(count,
					ListData.DataType.COMMENT);
		}

		@SuppressWarnings("unchecked")
		@Override
		public ListData<SociaxItem> commentReceiveMyTimeline(int count)
				throws ApiException, DataInvalidException,
				VerifyErrorException, ListAreEmptyException {
			this.beforeTimeline(ApiStatuses.COMMENT_RECEIVE_ME);
			return (ListData<SociaxItem>) this.afterTimeLine(count,
					ListData.DataType.RECEIVE);
		}

		@SuppressWarnings("unchecked")
		@Override
		public ListData<SociaxItem> commentReceiveMyHeaderTimeline(
				Comment item, int count) throws ApiException,
				DataInvalidException, VerifyErrorException,
				ListAreEmptyException {
			this.beforeTimeline(ApiStatuses.COMMENT_RECEIVE_ME);
			Api.get.append("since_id", item.getCommentId());
			return (ListData<SociaxItem>) this.afterTimeLine(count,
					ListData.DataType.RECEIVE);
		}

		@SuppressWarnings("unchecked")
		@Override
		public ListData<SociaxItem> commentReceiveMyFooterTimeline(
				Comment item, int count) throws ApiException,
				VerifyErrorException, ListAreEmptyException,
				DataInvalidException {
			this.beforeTimeline(ApiStatuses.COMMENT_RECEIVE_ME);
			Api.get.append("max_id", item.getCommentId());
			return (ListData<SociaxItem>) this.afterTimeLine(count,
					ListData.DataType.RECEIVE);
		}

		/**
		 * following 关注
		 */
		@SuppressWarnings("unchecked")
		@Override
		public ListData<SociaxItem> following(User user, int count)
				throws ApiException, ListAreEmptyException,
				DataInvalidException, VerifyErrorException {
			Uri.Builder uri = Api.createUrlBuild(ApiUsers.MOD_NAME,
					ApiUsers.FOOLOWING);
			Log.e("uri", "uri+" + uri.toString());
			Api.get.setUri(uri);
			Api.get.append("user_id", user.getUid());
			return (ListData<SociaxItem>) this.afterTimeLine(count,
					ListData.DataType.FOLLOW);
		}

		@SuppressWarnings("unchecked")
		@Override
		public ListData<SociaxItem> followingHeader(User user,
				Follow firstUser, int count) throws ApiException,
				ListAreEmptyException, DataInvalidException,
				VerifyErrorException {
			Uri.Builder uri = Api.createUrlBuild(ApiUsers.MOD_NAME,
					ApiUsers.FOOLOWING);
			Log.e("uri", "uri+" + uri.toString());
			Api.get.setUri(uri);
			Api.get.append("user_id", user.getUid());
			Api.get.append("since_id", firstUser.getFollowId());
			return (ListData<SociaxItem>) this.afterTimeLine(count,
					ListData.DataType.FOLLOW);
		}

		@SuppressWarnings("unchecked")
		@Override
		public ListData<SociaxItem> followingFooter(User user, Follow lastUser,
				int count) throws ApiException, ListAreEmptyException,
				DataInvalidException, VerifyErrorException {
			Uri.Builder uri = Api.createUrlBuild(ApiUsers.MOD_NAME,
					ApiUsers.FOOLOWING);
			Log.e("uri", "uri+" + uri.toString());
			Api.get.setUri(uri);
			Api.get.append("user_id", user.getUid());
			Api.get.append("max_id", lastUser.getFollowId());
			return (ListData<SociaxItem>) this.afterTimeLine(count,
					ListData.DataType.FOLLOW);
		}

		/**
		 * follower粉丝
		 */
		@SuppressWarnings("unchecked")
		@Override
		public ListData<SociaxItem> followers(User user, int count)
				throws ApiException, ListAreEmptyException,
				DataInvalidException, VerifyErrorException {
			Uri.Builder uri = Api.createUrlBuild(ApiUsers.MOD_NAME,
					ApiUsers.FOLLOWERS);
			Log.e("uri", "uri+" + uri.toString());
			Api.get.setUri(uri);
			Api.get.append("user_id", user.getUid());
			return (ListData<SociaxItem>) this.afterTimeLine(count,
					ListData.DataType.FOLLOW);
		}

		@SuppressWarnings("unchecked")
		@Override
		public ListData<SociaxItem> followersHeader(User user,
				Follow firstUser, int count) throws ApiException,
				ListAreEmptyException, DataInvalidException,
				VerifyErrorException {
			Uri.Builder uri = Api.createUrlBuild(ApiUsers.MOD_NAME,
					ApiUsers.FOLLOWERS);
			Log.e("uri", "uri+" + uri.toString());
			Api.get.setUri(uri);
			Api.get.append("user_id", user.getUid());
			Api.get.append("since_id", firstUser.getFollowId());
			return (ListData<SociaxItem>) this.afterTimeLine(count,
					ListData.DataType.FOLLOW);
		}

		@SuppressWarnings("unchecked")
		@Override
		public ListData<SociaxItem> followersFooter(User user, Follow lastUser,
				int count) throws ApiException, ListAreEmptyException,
				DataInvalidException, VerifyErrorException {
			Uri.Builder uri = Api.createUrlBuild(ApiUsers.MOD_NAME,
					ApiUsers.FOLLOWERS);
			Log.e("uri", "uri+" + uri.toString());
			Api.get.setUri(uri);
			Api.get.append("user_id", user.getUid());
			Api.get.append("max_id", lastUser.getFollowId());
			return (ListData<SociaxItem>) this.afterTimeLine(count,
					ListData.DataType.FOLLOW);
		}

		/**
		 * 互相关注
		 */
		@SuppressWarnings("unchecked")
		@Override
		public ListData<SociaxItem> followEach(User user, int count)
				throws ApiException, ListAreEmptyException,
				DataInvalidException, VerifyErrorException {
			Uri.Builder uri = Api.createUrlBuild(ApiUsers.MOD_NAME,
					ApiUsers.FOLLOWEACH);
			Api.get.setUri(uri);
			Api.get.append("user_id", user.getUid());
			return (ListData<SociaxItem>) this.afterTimeLine(count,
					ListData.DataType.FOLLOW);
		}

		@SuppressWarnings("unchecked")
		@Override
		public ListData<SociaxItem> followEachHeader(User user,
				Follow firstUser, int count) throws ApiException,
				ListAreEmptyException, DataInvalidException,
				VerifyErrorException {
			Uri.Builder uri = Api.createUrlBuild(ApiUsers.MOD_NAME,
					ApiUsers.FOLLOWEACH);
			Api.get.setUri(uri);
			Api.get.append("user_id", user.getUid());
			Api.get.append("since_id", firstUser.getFollowId());
			return (ListData<SociaxItem>) this.afterTimeLine(count,
					ListData.DataType.FOLLOW);
		}

		@SuppressWarnings("unchecked")
		@Override
		public ListData<SociaxItem> followEachFooter(User user,
				Follow lastUser, int count) throws ApiException,
				ListAreEmptyException, DataInvalidException,
				VerifyErrorException {
			Uri.Builder uri = Api.createUrlBuild(ApiUsers.MOD_NAME,
					ApiUsers.FOLLOWEACH);
			Log.e("uri", "uri+" + uri.toString());
			Api.get.setUri(uri);
			Api.get.append("user_id", user.getUid());
			Api.get.append("max_id", lastUser.getFollowId());
			return (ListData<SociaxItem>) this.afterTimeLine(count,
					ListData.DataType.FOLLOW);
		}

		@SuppressWarnings("unchecked")
		@Override
		public ListData<SociaxItem> searchUser(String user, int count)
				throws ApiException, ListAreEmptyException,
				DataInvalidException, VerifyErrorException {
			this.beforeTimeline(ApiStatuses.SEARCH_USER);
			Api.get.append("key", user);

			return (ListData<SociaxItem>) afterTimeLine(count,
					ListData.DataType.SEARCH_USER);
		}

		@SuppressWarnings("unchecked")
		@Override
		public ListData<SociaxItem> searchHeaderUser(String user,
				User firstUser, int count) throws ApiException,
				ListAreEmptyException, DataInvalidException,
				VerifyErrorException {
			// TODO Auto-generated method stub
			this.beforeTimeline(ApiStatuses.SEARCH_USER);
			Api.get.append("key", user);
			Api.get.append("since_id", firstUser.getUid());
			return (ListData<SociaxItem>) afterTimeLine(count,
					ListData.DataType.SEARCH_USER);
		}

		@SuppressWarnings("unchecked")
		@Override
		public ListData<SociaxItem> searchFooterUser(String user,
				User lastUser, int count) throws ApiException,
				ListAreEmptyException, DataInvalidException,
				VerifyErrorException {
			this.beforeTimeline(ApiStatuses.SEARCH_USER);
			Api.get.append("key", user);
			Api.get.append("max_id", lastUser.getUid());
			return (ListData<SociaxItem>) afterTimeLine(count,
					ListData.DataType.SEARCH_USER);
		}

		@Override
		public int update(Weibo weibo) throws ApiException,
				VerifyErrorException, UpdateException {
			if (weibo.isNullForContent())
				throw new UpdateContentEmptyException();
			if (!weibo.checkContent())
				throw new UpdateContentBigException();

			Uri.Builder uri = Api.createUrlBuild(ApiStatuses.MOD_NAME,
					ApiStatuses.UPDATE);
			Api.post.setUri(uri);
			Api.post.append("content", weibo.getContent());
			Api.post.append("from", Weibo.From.ANDROID.ordinal() + "");
			Object result = Api.run(Api.post);
			Api.checkResult(result);
			String data = (String) result;
			if (data.equals("false"))
				throw new UpdateException();
			if (data.indexOf("{") != -1 || data.indexOf("[") != -1) {
				try {
					JSONObject tempData = new JSONObject(data);
					Api.checkHasVerifyError(tempData);
				} catch (JSONException e) {
					throw new ApiException();
				}
			}
			return Integer.parseInt(data);
		}

		@Override
		public boolean comment(Comment comment) throws ApiException,
				VerifyErrorException, UpdateException, DataInvalidException {
			Uri.Builder uri = Api.createUrlBuild(ApiStatuses.MOD_NAME,
					ApiStatuses.COMMENT);
			comment.checkCommentCanAdd();

			Api.post.setUri(uri);

			Api.post.append("content", comment.getContent())
					.append("row_id", comment.getStatus().getWeiboId() + "")
					.append("ifShareFeed", comment.getType().ordinal() + "")
					.append("from", Weibo.From.ANDROID.ordinal() + "");
			if (comment.getAppName() != null)
				Api.post.append("git", comment.getAppName());

			// if (!comment.isNullForReplyComment()) {
			// int replyCommentId = comment.getReplyComment().getCommentId();
			// Api.post.append("to_comment_id", replyCommentId + "");
			// }

			Object result = Api.run(Api.post);
			Api.checkResult(result);
			String data = (String) result;

			int resultConde = 0;

			try {
				resultConde = Integer.valueOf(data);
			} catch (Exception e) {
				Log.d(APP_TAG, "发送评论出错  wm " + e.toString());
				throw new ApiException("服务端出错");
			}
			return resultConde >= 1 ? true : false;
		}

		@Override
		public boolean upload(Weibo weibo, File file) throws ApiException,
				VerifyErrorException, UpdateException {
			String result = null;
			try {
				Uri.Builder uri = Api.createUrlBuild(ApiStatuses.MOD_NAME,
						ApiStatuses.UPLOAD);

				// File file1 = new File("/sdcard","test.jpg");
				// byte [] content = readFileImage(file1);
				// ByteArrayInputStream byteIn = new
				// ByteArrayInputStream(content);
				// FormFile formFile = new FormFile(byteIn
				// ,file1.getName(),"pic","application/octet-stream") ;

				FormFile formFile = new FormFile(Compress.compressPic(file),
						file.getName(), "pic", "application/octet-stream");
				Api.post.setUri(uri);
				HashMap<String, String> param = new HashMap<String, String>();
				param.put("content", weibo.getContent());
				param.put("token", Request.getToken());
				param.put("secretToken", Request.getSecretToken());
				param.put("from", Weibo.From.ANDROID.ordinal() + "");
				result = FormPost.post(uri.toString(), param, formFile);
			} catch (FileNotFoundException e) {
				throw new UpdateException("file not found!");
			} catch (IOException e) {
				throw new UpdateException("file upload faild");
			}
			try {
				Api.checkHasVerifyError(new JSONObject(result));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return Integer.parseInt(result) > 0;
		}

		@Override
		public boolean repost(Weibo weibo, boolean comment)
				throws ApiException, VerifyErrorException, UpdateException,
				DataInvalidException {
			Uri.Builder uri = Api.createUrlBuild(ApiStatuses.MOD_NAME,
					ApiStatuses.REPOST);

			Api.post.setUri(uri);
			if (weibo.getTranspond().isNullForTranspond()) {
				Api.post.append("id", weibo.getTranspond().getWeiboId() + "");
			} else {
				Api.post.append("id", weibo.getTranspond().getTranspondId()
						+ "");
			}
			Api.post.append("content", weibo.getContent());
			if (comment) {
				Api.post.append("comment", 1);
			} else {
				Api.post.append("comment", 0);
			}
			Api.post.append("from", Weibo.From.ANDROID.ordinal() + "");
			Object result = Api.run(Api.post);
			Api.checkResult(result);

			return Integer.valueOf((String) result) > 0 ? true : false;
		}

		private void beforeTimeline(String act) {
			Uri.Builder uri = Api.createUrlBuild(ApiStatuses.MOD_NAME, act);
			Log.e("uri", "uri+" + uri.toString());
			Api.get.setUri(uri);
		}

		private ListData<?> afterTimeLine(int count, ListData.DataType type)
				throws ApiException, ListAreEmptyException,
				VerifyErrorException, DataInvalidException {
			Api.get.append("count", count);
			Object result = Api.run(Api.get);
			Api.checkResult(result);
			if (type == ListData.DataType.COMMENT
					|| type == ListData.DataType.RECEIVE) {
				if (result.equals("null"))
					throw new CommentListAreEmptyException();
			} else if (type == ListData.DataType.WEIBO) {
				if (result.equals("null"))
					throw new WeiBoListAreEmptyException();
			} else if (type == ListData.DataType.USER
					|| type == ListData.DataType.FOLLOW
					|| type == ListData.DataType.SEARCH_USER) {
				if (result.equals("null"))
					throw new UserListAreEmptyException();
			}
			try {
				JSONArray data = new JSONArray((String) result);
				int length = data.length();
				ListData<SociaxItem> list = new ListData<SociaxItem>();
				for (int i = 0; i < length; i++) {
					JSONObject itemData = data.getJSONObject(i);
					try {
						SociaxItem weiboData = this.getSociaxItem(type,
								itemData);
						// if(!weiboData.checkValid()) continue;
						list.add(weiboData);
					} catch (DataInvalidException e) {
						Log.e(TAG, "json error wm :" + e.toString());
						Log.e(TAG,
								"has one invalid item with string:"
										+ data.getString(i));
						continue;
					}
				}
				return list;
			} catch (JSONException e) { // 检查返回值，如果是一个JSONObject,则进行一次验证看看是否是验证失败得提示信息
				try {
					JSONObject data = new JSONObject((String) result);
					Api.checkHasVerifyError(data);
					throw new CommentListAreEmptyException();
				} catch (JSONException e1) {
					Log.e(APP_TAG, "comment json 解析 错误  wm " + e.toString());
					throw new ApiException("无效的数据格式");
				}
			}
		}

		private SociaxItem getSociaxItem(ListData.DataType type,
				JSONObject jsonObject) throws DataInvalidException,
				ApiException {
			if (type == ListData.DataType.COMMENT) {
				return new Comment(jsonObject);
			} else if (type == ListData.DataType.WEIBO) {
				return new Weibo(jsonObject);
			} else if (type == ListData.DataType.USER) {
				return new User(jsonObject);
			} else if (type == ListData.DataType.RECEIVE) {
				ReceiveComment receiveComment = new ReceiveComment(jsonObject);
				return receiveComment;
			} else if (type == ListData.DataType.FOLLOW) {
				return new Follow(jsonObject, "");
			} else if (type == ListData.DataType.SEARCH_USER) {
				return new SearchUser(jsonObject);
			} else {
				throw new ApiException("参数错误");
			}
		}

		@Override
		public int unRead() throws ApiException, VerifyErrorException,
				DataInvalidException {
			// TODO Auto-generated method stub
			Uri.Builder uri = Api.createUrlBuild(ApiStatuses.MOD_NAME,
					ApiStatuses.UN_READ);
			Api.get.setUri(uri);
			Object result = Api.run(Api.get);
			Api.checkResult(result);
			String data = (String) result;
			// if(data.equals("false")) throw new UpdateException();
			if (data.indexOf("{") != -1 || data.indexOf("[") != -1) {
				try {
					JSONObject tempData = new JSONObject(data);
					Api.checkHasVerifyError(tempData);
				} catch (JSONException e) {
					throw new ApiException();
				}
			}
			return Integer.parseInt(data);
		}

	}

	public static final class Message implements ApiMessage {

		@Override
		public ListData<SociaxItem> inbox(int count) throws ApiException,
				ListAreEmptyException, DataInvalidException,
				VerifyErrorException {
			this.beforeTimeline(ApiMessage.BOX);
			Api.get.append("count", count);
			ListData<SociaxItem> list = new ListData<SociaxItem>();
			this.getMessageList(list, true);
			return list;
		}

		@Override
		public ListData<SociaxItem> inboxHeader(
				com.zhiyicx.zycx.sociax.modle.Message message, int count)
				throws ApiException, ListAreEmptyException,
				DataInvalidException, VerifyErrorException {
			this.beforeTimeline(ApiMessage.BOX);
			Api.get.append("count", count);
			Api.get.append("since_id", message.getListId());
			ListData<SociaxItem> list = new ListData<SociaxItem>();
			this.getMessageList(list, true);
			return list;
		}

		@Override
		public ListData<SociaxItem> inboxFooter(
				com.zhiyicx.zycx.sociax.modle.Message message, int count)
				throws ApiException, ListAreEmptyException,
				DataInvalidException, VerifyErrorException {
			this.beforeTimeline(ApiMessage.BOX);
			Api.get.append("count", count);
			Api.get.append("max_id", message.getListId());
			ListData<SociaxItem> list = new ListData<SociaxItem>();
			this.getMessageList(list, true);
			return list;
		}

		@Override
		public ListData<SociaxItem> outbox(
				com.zhiyicx.zycx.sociax.modle.Message message, int count)
				throws ApiException, ListAreEmptyException,
				DataInvalidException, VerifyErrorException {
			this.beforeTimeline(ApiMessage.SHOW);
			Api.get.append("id", message.getListId());
			Api.get.append("count", count);
			ListData<SociaxItem> list = new ListData<SociaxItem>();
			this.getMessageList(list, false);
			return list;
		}

		@Override
		public ListData<SociaxItem> outboxHeader(
				com.zhiyicx.zycx.sociax.modle.Message message, int count)
				throws ApiException, ListAreEmptyException,
				DataInvalidException, VerifyErrorException {
			this.beforeTimeline(ApiMessage.SHOW);
			Api.get.append("count", count);
			Api.get.append("id", message.getListId());
			Api.get.append("since_id", message.getMeesageId());
			ListData<SociaxItem> list = new ListData<SociaxItem>();
			this.getMessageList(list, false);
			return list;
		}

		@Override
		public ListData<SociaxItem> outboxFooter(
				com.zhiyicx.zycx.sociax.modle.Message message, int count)
				throws ApiException, ListAreEmptyException,
				DataInvalidException, VerifyErrorException {
			this.beforeTimeline(ApiMessage.SHOW);
			Api.get.append("count", count);
			Api.get.append("id", message.getListId());
			Api.get.append("max_id", message.getMeesageId());
			ListData<SociaxItem> list = new ListData<SociaxItem>();
			this.getMessageList(list, false, "footer");
			return list;
		}

		@Override
		public com.zhiyicx.zycx.sociax.modle.Message show(
				com.zhiyicx.zycx.sociax.modle.Message message)
				throws ApiException, DataInvalidException, VerifyErrorException {
			this.beforeTimeline(ApiMessage.SHOW);
			Api.get.append("id", message.getListId());
			Api.get.append("show_cascade", 0);
			Object result = Api.run(Api.get);
			Api.checkResult(result);
			try {
				JSONObject data = new JSONObject((String) result);
				Api.checkHasVerifyError(data);
				return new com.zhiyicx.zycx.sociax.modle.Message(data);
			} catch (JSONException e) {
				// throw new ApiException();
			}
			return null;
		}

		private void getMessageList(ListData<SociaxItem> list, boolean type)
				throws DataInvalidException, VerifyErrorException, ApiException {
			Object result = Api.run(Api.get);
			Api.checkResult(result);
			try {
				JSONArray data = new JSONArray((String) result);
				int length = data.length();
				com.zhiyicx.zycx.sociax.modle.Message mainMessage = null;
				for (int i = 0; i < length; i++) {
					if (type) {
						mainMessage = new com.zhiyicx.zycx.sociax.modle.Message(
								data.getJSONObject(i));
					} else {
						mainMessage = new com.zhiyicx.zycx.sociax.modle.Message(
								data.getJSONObject(i), false);
					}
					// if(!tempData.checkValid()) continue;
					list.add(mainMessage);
				}
			} catch (JSONException e) {
				JSONObject data;
				try {
					data = new JSONObject((String) result);
					Api.checkHasVerifyError(data);
					throw new ApiException();
				} catch (JSONException e1) {
					throw new ApiException();
				}
			}
		}

		private void getMessageList(ListData<SociaxItem> list, boolean type,
				String tag) throws DataInvalidException, VerifyErrorException,
				ApiException {
			Object result = Api.run(Api.get);
			Api.checkResult(result);
			try {
				JSONArray data = new JSONArray((String) result);
				int length = data.length();
				com.zhiyicx.zycx.sociax.modle.Message mainMessage = null;
				for (int i = 0; i < length; i++) {
					if (type) {
						mainMessage = new com.zhiyicx.zycx.sociax.modle.Message(
								data.getJSONObject(i));
					} else {
						mainMessage = new com.zhiyicx.zycx.sociax.modle.Message(
								data.getJSONObject(i), false);
					}
					// if(!tempData.checkValid()) continue;
					list.add(mainMessage);
				}
			} catch (JSONException e) {
				JSONObject data;
				try {
					data = new JSONObject((String) result);
					Api.checkHasVerifyError(data);

				} catch (JSONException e1) {
					Log.e(APP_TAG,
							"api =====> get message footer wm " + e1.toString());
				}
			}
		}

		@Override
		public boolean createNew(com.zhiyicx.zycx.sociax.modle.Message message)
				throws ApiException, DataInvalidException, VerifyErrorException {
			// message.checkMessageCanAdd();
			Uri.Builder uri = Api.createUrlBuild(ApiMessage.MOD_NAME,
					ApiMessage.CREATE);
			Api.post.setUri(uri);
			Api.post.append("to_uid", message.getToUid());
			Api.post.append("title", message.getTitle()).append("content",
					message.getContent());
			Object result = Api.run(Api.post);
			Api.checkResult(result);
			if (result.equals("\"false\"") || result.equals("\"0\""))
				return false;
			return true;
		}

		@Override
		public void show(com.zhiyicx.zycx.sociax.modle.Message message,
				ListData<SociaxItem> list) throws ApiException,
				DataInvalidException, VerifyErrorException {
			this.beforeTimeline(ApiMessage.SHOW);
			Api.get.append("id", message.getListId());
			Object result = Api.run(Api.get);
			Api.checkResult(result);
			try {
				JSONArray data = new JSONArray((String) result);
				int length = data.length();
				com.zhiyicx.zycx.sociax.modle.Message mainMessage = null;
				for (int i = 0; i < length; i++) {
					com.zhiyicx.zycx.sociax.modle.Message tempData = new com.zhiyicx.zycx.sociax.modle.Message(
							data.getJSONObject(i));
					if (i == 0) {
						mainMessage = tempData;
					}

					if (!tempData.checkValid())
						continue;
					list.add(tempData);
				}
			} catch (JSONException e) {
				JSONObject data;
				try {
					data = new JSONObject((String) result);
					Api.checkHasVerifyError(data);
					throw new ApiException();
				} catch (JSONException e1) {
					throw new ApiException();
				}
			}
		}

		@Override
		public int[] create(com.zhiyicx.zycx.sociax.modle.Message message)
				throws ApiException, DataInvalidException, VerifyErrorException {
			// message.checkMessageCanAdd();
			Uri.Builder uri = Api.createUrlBuild(ApiMessage.MOD_NAME,
					ApiMessage.CREATE);
			Api.post.setUri(uri);
			Api.post.append("title", message.getTitle()).append("content",
					message.getContent());
			Object result = Api.run(Api.post);
			Api.checkResult(result);
			try {
				JSONArray data = new JSONArray((String) result);
				int[] res = new int[data.length()];
				for (int i = 0; i < data.length(); i++) {
					res[i] = data.getInt(i);
				}
				return res;
			} catch (JSONException e) {
				try {
					JSONObject data = new JSONObject((String) result);
					Api.checkHasVerifyError(data);
					throw new ApiException();
				} catch (JSONException e2) {
					throw new ApiException();
				}

			}

		}

		@Override
		public boolean reply(com.zhiyicx.zycx.sociax.modle.Message message)
				throws ApiException, DataInvalidException, VerifyErrorException {
			// message.checkMessageCanReply();
			Uri.Builder uri = Api.createUrlBuild(ApiMessage.MOD_NAME,
					ApiMessage.REPLY);
			Api.post.setUri(uri);
			// Api.post.append("id",
			// message.getSourceMessage().getMessageId()).append("content",
			// message.getContent());
			Api.post.append("id", message.getListId());
			Api.post.append("content", message.getContent());
			Object result = Api.run(Api.post);
			Api.checkResult(result);
			if (result.equals("\"false\"") || result.equals("\"0\""))
				return false;
			return true;
		}

		private void beforeTimeline(String act) {
			Uri.Builder uri = Api.createUrlBuild(ApiMessage.MOD_NAME, act);
			Api.get.setUri(uri);

		}
	}

	public static final class Friendships implements ApiFriendships {
		@Override
		public boolean show(User friends) throws ApiException,
				VerifyErrorException {
			this.beforeTimeline(ApiFriendships.SHOW);
			Api.get.append("user_id", friends.getUid());
			Object result = Api.run(Api.get);
			Api.checkResult(result);
			try {
				JSONObject data = new JSONObject((String) result);
				Api.checkHasVerifyError(data);
			} catch (JSONException e) {
				throw new ApiException();
			}
			String resultString = (String) result;
			return resultString.equals("\"havefollow\"")
					|| resultString.equals("\"eachfollow\"");
		}

		private void beforeTimeline(String act) {
			Uri.Builder uri = Api.createUrlBuild(ApiFriendships.MOD_NAME, act);
			Api.get.setUri(uri);
		}

		@Override
		public boolean create(User user) throws ApiException,
				VerifyErrorException, DataInvalidException {
			return this.doApiRuning(user, Api.post, ApiUsers.FOLLOW_CREATE);
		}

		@Override
		public boolean destroy(User user) throws ApiException,
				VerifyErrorException, DataInvalidException {
			// TODO Auto-generated method stub
			return this.doApiRuning(user, Api.post, ApiUsers.FOLLOW_DESTROY);
		}

		@Override
		public boolean addBlackList(User user) throws ApiException,
				VerifyErrorException, DataInvalidException {
			return this.doApiRuning(user, Api.post,
					ApiFriendships.ADDTOBLACKLIST);
		}

		@Override
		public boolean delBlackList(User user) throws ApiException,
				VerifyErrorException, DataInvalidException {
			return this.doApiRuning(user, Api.post,
					ApiFriendships.DELTOBLACKLIST);
		}

		private boolean doApiRuning(User user, Request res, String act)
				throws ApiException, VerifyErrorException, DataInvalidException {
			Uri.Builder uri = Api.createUrlBuild(ApiUsers.MOD_NAME, act);
			if (user.isNullForUid())
				throw new DataInvalidException();
			Api.post.setUri(uri);
			Api.post.append("user_id", user.getUid());
			Object result = Api.run(Api.post);
			Api.checkResult(result);
			// {"following":0,"follower":1} get follow info
			String data = (String) result;
			if (data.indexOf("{") != -1 || data.indexOf("[") != -1) {
				try {
					JSONObject datas = new JSONObject((String) result);
					Api.checkHasVerifyError(datas);
					if (datas.has("following")) {
						int stataCode = datas.getInt("following");
						if (act.equals(ApiUsers.FOLLOW_CREATE)) {
							return stataCode == 1 ? true : false;
						} else if (act.equals(ApiUsers.FOLLOW_DESTROY)) {
							return stataCode == 0 ? true : false;
						}
					}
				} catch (JSONException e) {
					Log.d(APP_TAG, " doruning wm" + e.toString());
					throw new ApiException("操作失败");
				}
			}
			return false;
		}

		@Override
		public boolean isFollowTopic(User user, String topic)
				throws ApiException, VerifyErrorException, DataInvalidException {
			// TODO Auto-generated method stub
			return doApiRuning(ApiFriendships.ISFOLLOWTOPIC, topic);
		}

		@Override
		public boolean followTopic(User user, String topic)
				throws ApiException, VerifyErrorException, DataInvalidException {
			// TODO Auto-generated method stub
			return doApiRuning(ApiFriendships.FOLLOWTOPIC, topic);
		}

		@Override
		public boolean unFollowTopic(User user, String topic)
				throws ApiException, VerifyErrorException, DataInvalidException {
			// TODO Auto-generated method stub
			return doApiRuning(ApiFriendships.UNFOLLOWTOPIC, topic);
		}

		private boolean doApiRuning(String act, String topic)
				throws ApiException, VerifyErrorException, DataInvalidException {
			Uri.Builder uri = Api.createUrlBuild(ApiFriendships.MOD_NAME, act);
			Api.get.setUri(uri);
			Api.get.append("topic", topic);
			Object result = Api.run(Api.get);
			Api.checkResult(result);
			String data = (String) result;
			Log.d(APP_TAG, " doApiRuning result" + data);
			if (data.equals("ERROR")) {
				throw new ApiException("网络繁忙，请重试！");
			}
			if (data.indexOf("{") != -1 || data.indexOf("[") != -1) {
				try {
					JSONObject datas = new JSONObject((String) result);
					Api.checkHasVerifyError(datas);
					if (datas.has("is_followed")) {
						String tempString = datas.getString("is_followed");
						if (act.equals(ApiFriendships.FOLLOWTOPIC)) {
							return tempString.equals("havefollow") ? true
									: false;
						} else if (act.equals(ApiFriendships.UNFOLLOWTOPIC)) {
							return tempString.equals("unfollow") ? true : false;
						}
					}
					throw new ApiException();
				} catch (JSONException e) {
					throw new ApiException();
				}
			}
			if (data.equals("\"true\"") || data.equals("1")) {
				return true;
			} else {
				return false;
			}
		}
	}

	public static final class Favorites implements ApiFavorites {

		@Override
		public ListData<SociaxItem> index(int count) throws ApiException,
				ListAreEmptyException, DataInvalidException,
				VerifyErrorException {
			this.beforeTimeline(ApiFavorites.INDEX);
			try {
				Api.get.append("count", count);
				return this.getList();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		public ListData<SociaxItem> indexHeader(Weibo weibo, int count)
				throws ApiException, ListAreEmptyException,
				DataInvalidException, VerifyErrorException {
			this.beforeTimeline(ApiFavorites.INDEX);
			Api.get.append("count", count);
			Api.get.append("since_id", weibo.getWeiboId());
			return this.getList();
		}

		@Override
		public ListData<SociaxItem> indexFooter(Weibo weibo, int count)
				throws ApiException, ListAreEmptyException,
				DataInvalidException, VerifyErrorException {
			this.beforeTimeline(ApiFavorites.INDEX);
			Api.get.append("count", count);
			Api.get.append("max_id", weibo.getWeiboId());
			return this.getList();
		}

		@Override
		public boolean create(Weibo weibo) throws ApiException,
				DataInvalidException, VerifyErrorException {
			return this.doApiRuning(weibo, Api.post, ApiFavorites.CREATE);
		}

		@Override
		public boolean isFavorite(Weibo weibo) throws ApiException,
				DataInvalidException, VerifyErrorException {
			this.beforeTimeline(ApiFavorites.IS_FAVORITE);
			Api.get.append("id", weibo.getWeiboId());
			Object result = Api.run(Api.get);
			Api.checkResult(result);
			String data = (String) result;
			if (data.indexOf("{") != -1 || data.indexOf("[") != -1) {
				try {
					JSONObject datas = new JSONObject((String) result);
					Api.checkHasVerifyError(datas);
					throw new ApiException();
				} catch (JSONException e) {
					throw new ApiException();
				}
			}

			return data.equals("true");
		}

		private void beforeTimeline(String act) {
			Uri.Builder uri = Api.createUrlBuild(ApiFavorites.MOD_NAME, act);
			Api.get.setUri(uri);
		}

		@Override
		public boolean destroy(Weibo weibo) throws ApiException,
				DataInvalidException, VerifyErrorException {
			return this.doApiRuning(weibo, Api.post, ApiFavorites.DESTROY);
		}

		private boolean doApiRuning(Weibo weibo, Request res, String act)
				throws ApiException, VerifyErrorException, DataInvalidException {
			Uri.Builder uri = Api.createUrlBuild(ApiFavorites.MOD_NAME, act);
			if (weibo.isNullForWeiboId())
				throw new DataInvalidException();
			Api.post.setUri(uri);
			Api.post.append("source_table_name", "feed");
			Api.post.append("source_id", weibo.getWeiboId());
			Api.post.append("source_app", "public");
			Object result = Api.run(Api.post);
			Api.checkResult(result);
			String data = (String) result;
			if (data.indexOf("{") != -1 || data.indexOf("[") != -1) {
				try {
					JSONObject datas = new JSONObject((String) result);
					Api.checkHasVerifyError(datas);
					// throw new ApiException();
				} catch (JSONException e) {

					throw new ApiException("操作失败");
				}
			}
			return Integer.parseInt(data) > 0;
		}

		public ListData<SociaxItem> getList() throws VerifyErrorException,
				ApiException, ListAreEmptyException {
			Object result = Api.run(Api.get);
			Api.checkResult(result);
			try {
				JSONArray data = new JSONArray((String) result);
				int length = data.length();
				ListData<SociaxItem> list = new ListData<SociaxItem>();

				for (int i = 0; i < length; i++) {
					JSONObject itemData = data.getJSONObject(i);
					try {
						Weibo weiboData = new Weibo(itemData);
						if (!weiboData.checkValid())
							continue;
						list.add(weiboData);
					} catch (WeiboDataInvalidException e) {
						Log.e(TAG, "has one invalid weibo item with string:"
								+ data.getString(i));
					}
				}
				return list;
			} catch (JSONException e) { // 检查返回值，如果是一个JSONObject,则进行一次验证看看是否是验证失败得提示信息
				try {
					JSONObject data = new JSONObject((String) result);
					Api.checkHasVerifyError(data);
					throw new ListAreEmptyException();
				} catch (JSONException e1) {
					throw new ApiException("暂无更多数据");
				}
			}
		}
	}

	// 多站点网站
	public static final class Sites implements ApiSites {
		@Override
		public ListData<SociaxItem> getSisteList() throws ApiException,
				ListAreEmptyException, DataInvalidException,
				VerifyErrorException {
			ListData<SociaxItem> list = null;
			Object result = Api.run(Api.get);
			Api.checkResult(result);
			Log.d(TAG, "site list result + " + result);
			try {
				if (!result.equals("null")) {
					JSONArray data = new JSONArray((String) result);
					int length = data.length();
					list = new ListData<SociaxItem>();
					for (int i = 0; i < length; i++) {
						JSONObject itemData = data.getJSONObject(i);
						try {
							SociaxItem siteData = new ApproveSite(itemData);
							list.add(siteData);
						} catch (SiteDataInvalidException e) {
							Log.e(TAG,
									"has one invalid weibo item with string:"
											+ data.getString(i));
						}
					}
				}
				return list;
			} catch (JSONException e) {
				try {
					JSONObject data = new JSONObject((String) result);
					Api.checkHasVerifyError(data);
					throw new ListAreEmptyException();
				} catch (JSONException e1) {
					throw new ApiException("暂无更多数据");
				}
			}

		}

		@Override
		public boolean getSiteStatus(ApproveSite as) throws ApiException,
				ListAreEmptyException, DataInvalidException,
				VerifyErrorException {
			this.beforeTimeline(ApiSites.GET_SITE_STATUS);
			Api.get.append("id", as.getSite_id());
			Object result = Api.run(Api.get);
			Api.checkResult(result);
			try {
				JSONObject object = new JSONObject((String) result);
				if (object.has("status") && object.has("alias")) {
					if (object.getInt("status") == 1) {
						return true;
					}
				}
			} catch (JSONException e) {
				Log.d(TAG, "get site status error  " + e.toString());
				e.printStackTrace();
			}
			return false;
		}

		private void beforeTimeline(String act) {
			Uri.Builder uri = Api.createThinksnsUrlBuild(Api.APP_NAME,
					ApiSites.MOD_NAME, act);
			Api.get.setUri(uri);
		}

		@Override
		public ListData<SociaxItem> newSisteList(int count)
				throws ApiException, ListAreEmptyException,
				DataInvalidException, VerifyErrorException {
			// TODO Auto-generated method stub
			this.beforeTimeline(ApiSites.GET_SITE_LIST);
			Api.get.append("count", count);
			return this.getSisteList();
		}

		@Override
		public ListData<SociaxItem> getSisteListHeader(ApproveSite as, int count)
				throws ApiException, ListAreEmptyException,
				DataInvalidException, VerifyErrorException {
			// TODO Auto-generated method stub
			this.beforeTimeline(ApiSites.GET_SITE_LIST);
			Api.get.append("count", count);
			Api.get.append("since_id", as.getSite_id());
			return this.getSisteList();
		}

		@Override
		public ListData<SociaxItem> getSisteListFooter(ApproveSite as, int count)
				throws ApiException, ListAreEmptyException,
				DataInvalidException, VerifyErrorException {
			// TODO Auto-generated method stub
			Api.get.append("count", count);
			Api.get.append("max_id", as.getSite_id());
			return this.getSisteList();
		}

		// dev.thinksns.com/ts/2.0/index.php?app=home&mod=Widget&act=addonsRequest&addon=Login&hook=isSinaLoginAvailable
		@Override
		public boolean isSupport() throws ApiException, ListAreEmptyException,
				DataInvalidException, VerifyErrorException {
			Uri.Builder uri = Api.createForCheck("home", "Widget",
					"addonsRequest");
			Api.get.setUri(uri);
			Api.get.append("addon", "Login").append("hook",
					"isSinaLoginAvailable");
			Object result = Api.run(Api.get);
			Api.checkResult(result);

			Integer object = null;
			try {
				object = new Integer((String) result);
				return object == 1 ? true : false;
			} catch (Exception ex) {
				// TODO Auto-generated catch block
				return false;
			}
		}

		@Override
		public boolean isSupportReg() throws ApiException,
				ListAreEmptyException, DataInvalidException,
				VerifyErrorException {
			Uri.Builder uri = Api.createForCheck("home", "Public",
					"isRegisterAvailable");
			Api.get.setUri(uri);

			Api.get.append("wap_to_normal", 1);

			Object result = Api.run(Api.get);
			Api.checkResult(result);

			Integer object = null;
			try {
				object = new Integer((String) result);
				return object.equals(1) ? true : false;
			} catch (Exception ex) {
				// TODO Auto-generated catch block
				return false;
			} // TODO Auto-generated method stub
		}

		@Override
		public ListData<SociaxItem> searchSisteList(String key, int count)
				throws ApiException, ListAreEmptyException,
				DataInvalidException, VerifyErrorException {
			// TODO Auto-generated method stub
			this.beforeTimeline(ApiSites.GET_SITE_LIST);
			Api.get.append("count", count);
			Api.get.append("content", key);
			return this.getSisteList();
		}

	}

	// app=home&mod=Public&act=isRegisterAvailable&wap_to_normal=1

	// Users
	public static final class Users implements ApiUsers {

		@Override
		public User show(User user) throws ApiException, DataInvalidException,
				VerifyErrorException {
			this.beforeTimeline("show");
			Api.get.append("user_id", user.getUid());
			if (user.getUserName() != null) {
				Api.get.append("user_name", user.getUserName());
			}
			Object result = Api.run(Api.get);
			Api.checkResult(result);
			String data = (String) result;
			if (data.equals("\"false\""))
				throw new DataInvalidException("该用户不存在");

			try {
				JSONObject userData = new JSONObject(data);
				Api.checkHasVerifyError(userData);
				return new User(userData);
			} catch (JSONException e) {
				Log.d(APP_TAG, "======》  解析个人信息出错 。。。" + e.toString());
				throw new DataInvalidException("获取个人信息失败");
			}
		}

		// 返回通知，@，私信
		@Override
		public NotifyCount notificationCount(int uid) throws ApiException,
				ListAreEmptyException, DataInvalidException,
				VerifyErrorException {
			this.beforeTimeline(ApiUsers.NOTIFICATION_COUNT);
			Api.get.append("user_id", uid);
			Object result = Api.run(Api.get);
			Api.checkResult(result);
			String data = (String) result;
			// String data
			// ="{\"message\":2,\"notify\":3,\"appmessage\":\"0\",\"comment\":1,\"atme\":1,\"total\":8,\"weibo_comment\":3,\"global_comment\":0}";
			if (data.equals("\"false\""))
				throw new ListAreEmptyException("请求的数据异常");

			try {
				JSONObject userData = new JSONObject(data);
				Api.checkHasVerifyError(userData);
				Log.d("apiData", "getNotifyCount" + userData.toString());

				NotifyCount notifyCount = new NotifyCount(userData);
				return notifyCount;
			} catch (JSONException e) {
				throw new DataInvalidException("数据格式错误");
			}
		}

		/**
		 * 提醒数据list
		 */
		@Override
		public ListData<SociaxItem> getNotificationList(int uid)
				throws ApiException, ListAreEmptyException,
				DataInvalidException, VerifyErrorException {
			this.beforeTimeline(ApiUsers.NOTIFICATIONLIST);
			Api.get.append("user_id", uid);
			Object result = Api.run(Api.get);
			Api.checkResult(result);
			if (result.equals("\"false\""))
				throw new ListAreEmptyException("请求的数据异常");
			ListData<SociaxItem> list = null;
			try {
				if (!result.equals("null")) {
					JSONArray data = new JSONArray((String) result);
					int length = data.length();
					list = new ListData<SociaxItem>();
					for (int i = 0; i < length; i++) {
						JSONObject jsonObject = data.getJSONObject(i);
						NotifyItem notifyItem = new NotifyItem(jsonObject);
						if (notifyItem.getCount() < 1) {
							continue;
						}
						list.add(notifyItem);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				Log.d(TAG, e.toString());
			}
			return list;
		}

		@Override
		public boolean unsetNotificationCount(Type type, int uid)
				throws ApiException, ListAreEmptyException,
				DataInvalidException, VerifyErrorException {
			this.beforeTimeline("Notifytion", "set_notify_read");
			NotifyCount notifycount = new NotifyCount();
			for (NotifyCount.Type t : NotifyCount.Type.values()) {

				if (t.equals(type)) {
					String name = t.name();
					Api.get.append("type", name);
				}
			}
			Api.get.append("mid", uid);
			Object result = Api.run(Api.get);
			Api.checkResult(result);
			Log.d(TAG, "api unsetNotification......" + result);
			String data = (String) result;
			if (data.equals("\"false\""))
				return false;
			if (data.indexOf("{") != -1 || data.indexOf("[") != -1) {
				try {
					JSONObject userData = new JSONObject(data);
					Api.checkHasVerifyError(userData);
					return false;
				} catch (JSONException e) {
					throw new DataInvalidException("数据格式错误");
				}
			} else {
				if (NotifyCount.Type.atme.name() == type.name()) {
					notifycount.setAtme(0);
				} else if (NotifyCount.Type.message.name() == type.name()) {
					notifycount.setMessage(0);
				} else if (NotifyCount.Type.notify.name() == type.name()) {
					notifycount.setNotify(0);
				} else if (NotifyCount.Type.comment.name() == type.name()) {
					notifycount.setWeiboComment(0);
				}
				Log.d(TAG, "unsetNotificationCount" + type.name());
				return true;
			}

		}

		private void beforeTimeline(String act) {
			Uri.Builder uri = Api.createUrlBuild("User", act);
			Api.get.setUri(uri);
		}

		private void beforeTimeline(String mod, String act) {
			Uri.Builder uri = Api.createUrlBuild(mod, act);
			Api.get.setUri(uri);
		}

		@Override
		public boolean uploadFace(File file) throws ApiException {
			String temp = "0";
			try {
				Uri.Builder uri = Api.createUrlBuild(ApiUsers.MOD_NAME,
						ApiUsers.UPLOAD_FACE);
				FormFile formFile = new FormFile(Compress.compressPic(file),
						file.getName(), "Filedata", "application/octet-stream");
				Api.post.setUri(uri);
				HashMap<String, String> param = new HashMap<String, String>();
				param.put("token", Request.getToken());
				param.put("secretToken", Request.getSecretToken());

				temp = FormPost.post(uri.toString(), param, formFile);

			} catch (Exception e) {
				e.printStackTrace();
				Log.d(APP_TAG, "upload face pic error ..." + e.toString());
			}
			Log.d(APP_TAG, temp);
			return temp.equals("\"1\"") ? true : false;
		}

		@Override
		public boolean uploadFace(Bitmap bitmap, File file) throws ApiException {
			String temp = "0";
			try {
				Uri.Builder uri = Api.createUrlBuild(ApiUsers.MOD_NAME,
						ApiUsers.UPLOAD_FACE);

				FormFile formFile = new FormFile(Compress.compressPic(bitmap),
						file.getName(), "Filedata", "application/octet-stream");
				Api.post.setUri(uri);
				HashMap<String, String> param = new HashMap<String, String>();
				param.put("token", Request.getToken());
				param.put("secretToken", Request.getSecretToken());

				temp = FormPost.post(uri.toString(), param, formFile);

				JSONObject tempdata = new JSONObject(temp);
				temp = tempdata.getString("status");
			} catch (Exception e) {
				e.printStackTrace();
				Log.d(APP_TAG, "upload face pic error ..." + e.toString());
			}
			Log.d(APP_TAG, temp);
			return temp.equals("1") ? true : false;
		}

		@Override
		public ListData<SociaxItem> getRecentTopic() throws ApiException {
			Uri.Builder uri = Api.createUrlBuild(APP_NAME,
					ApiStatuses.MOD_NAME, ApiUsers.RECENT_TOPIC);
			Api.get.setUri(uri);
			Object result = Api.run(Api.get);
			ListData<SociaxItem> listData = null;
			try {
				JSONArray jsonArray = new JSONArray((String) result);
				listData = new ListData<SociaxItem>();
				for (int i = 0; i < jsonArray.length(); i++) {

					JSONObject jsonTopic = jsonArray.getJSONObject(i);
					RecentTopic reTopic = new RecentTopic();
					reTopic.setName(jsonTopic.getString("topic_name"));
					listData.add(reTopic);
				}
			} catch (Exception e) {
				e.printStackTrace();
				Log.d(TAG, "Api get recent at data error " + e.toString());
			}
			return listData;
		}

		@Override
		public ListData<SociaxItem> getRecentAt() throws ApiException {
			Uri.Builder uri = Api.createUrlBuild(APP_NAME,
					ApiStatuses.MOD_NAME, ApiUsers.RECENT_USER);
			Api.get.setUri(uri);
			Object result = Api.run(Api.get);
			ListData<SociaxItem> listData = null;
			try {
				JSONArray jsonArray = new JSONArray((String) result);
				listData = new ListData<SociaxItem>();
				for (int i = 0; i < jsonArray.length(); i++) {

					JSONObject jsonUser = jsonArray.getJSONObject(i);
					User tempUser = new User();
					tempUser.setUserName(jsonUser.getString("uname"));
					tempUser.setFace(jsonUser.getString("avatar_small"));

					listData.add(tempUser);
				}
			} catch (Exception e) {
				e.printStackTrace();
				Log.d(TAG, "Api get recent at data error " + e.toString());
			}
			return listData;
		}

		@Override
		public ListData<SociaxItem> getUserCategory(String type)
				throws ApiException {
			Uri.Builder uri = Api.createUrlBuild(APP_NAME, ApiUsers.MOD_NAME,
					ApiUsers.GET_USER_CATEGORY);
			ListData<SociaxItem> listData = null;
			Get get = new Get();
			get.setUri(uri);
			get.append("type", type);
			Object result = Api.run(get);
			try {
				JSONObject jsonObject = new JSONObject((String) result);
				listData = packageData(jsonObject);
			} catch (Exception e) {
				try {
					listData = packageData(new JSONArray((String) result));
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Log.d(TAG, e.toString());
			}
			return listData;
		}

		private ListData<SociaxItem> packageData(JSONArray ja) throws Exception {

			ListData<SociaxItem> listData = new ListData<SociaxItem>();
			ListData<SociaxItem> listData2 = null;

			for (int i = 0; i < ja.length(); i++) {
				JSONObject joTemp = (JSONObject) ja.get(i);
				int id = joTemp.getInt("user_group_id");
				String title = joTemp.getString("user_group_name");
				StringItem si = new StringItem(id, title);
				if (!joTemp.isNull("child")) {
					JSONArray jaChile = joTemp.getJSONArray("child");
					listData2 = packageChildData(jaChile);
				}
				si.setListData(listData2);
				listData.add(si);
			}
			if (listData != null)
				Collections.sort(listData);
			return listData;

		}

		private ListData<SociaxItem> packageChildData(JSONArray ja)
				throws Exception {
			ListData<SociaxItem> listData = new ListData<SociaxItem>();
			for (int i = 0; i < ja.length(); i++) {
				JSONObject joTemp = (JSONObject) ja.get(i);
				int id = joTemp.getInt("user_verified_category_id");
				String title = joTemp.getString("title");
				StringItem si = new StringItem(id, title);
				listData.add(si);
			}
			return listData;
		}

		private ListData<SociaxItem> packageData(JSONObject jo)
				throws JSONException {

			ListData<SociaxItem> listData = new ListData<SociaxItem>();
			ListData<SociaxItem> listData2 = null;
			for (Iterator iterator = jo.keys(); iterator.hasNext();) {

				int id = Integer.valueOf(iterator.next().toString());
				JSONObject joTemp = jo.getJSONObject(id + "");
				String title = joTemp.getString("title");
				if (!joTemp.isNull("child")) {
					JSONObject joChile = joTemp.getJSONObject("child");
					listData2 = packageData(joChile);
				}
				StringItem si = new StringItem(id, title);
				si.setListData(listData2);
				listData.add(si);
			}
			if (listData != null)
				Collections.sort(listData);
			return listData;
		}

		@Override
		public ListData<SociaxItem> getUserFollower() throws ApiException {
			Uri.Builder uri = Api.createUrlBuild(APP_NAME, ApiUsers.MOD_NAME,
					ApiUsers.GET_USER_FOLLOWER);
			ListData<SociaxItem> listData = new ListData<SociaxItem>();
			;
			Get get = new Get();
			get.setUri(uri);
			get.append("limit", 40);
			try {
				JSONArray jsonArray = new JSONArray((String) Api.run(get));
				for (int i = 0; i < jsonArray.length(); i++) {
					User user = new User(jsonArray.getJSONObject(i));
					listData.add(user);
				}
			} catch (Exception e) {
				Log.d(TAG, e.toString());
			}
			return listData;
		}

		@Override
		public boolean checkint(String la, String lo) throws ApiException {
			Uri.Builder uri = Api.createUrlBuild(APP_NAME, ApiUsers.MOD_NAME,
					ApiUsers.CHECKIN);
			Post post = new Post();
			post.setUri(uri);
			post.append("latitude", la);
			post.append("longitude", lo);
			try {
				Object o = Api.run(post);
				System.out.println(o);
				if ((Integer.valueOf(o.toString().trim())) == 1) {
					return true;
				}
			} catch (Exception e) {
				Log.e(TAG, e.toString());
			}
			return false;
		}

		@Override
		public ListData<SociaxItem> getNeighbor(String la, String lo)
				throws ApiException {
			Uri.Builder uri = Api.createUrlBuild(APP_NAME, ApiUsers.MOD_NAME,
					ApiUsers.NEIGHBOR);
			ListData<SociaxItem> listData = new ListData<SociaxItem>();
			Get get = new Get();
			get.setUri(uri);
			get.append("latitude", la);
			get.append("longitude", lo);
			try {
				JSONObject jsonObject = new JSONObject((String) Api.run(get));
				JSONArray jsonArray = (jsonObject.getJSONArray("data"));
				for (int i = 0; i < jsonArray.length(); i++) {
					User user = new User(jsonArray.getJSONObject(i));
					listData.add(user);
				}
			} catch (Exception e) {
				Log.d(TAG, e.toString());
			}
			return listData;
		}

		@Override
		public ListData<SociaxItem> searchByArea(String key)
				throws ApiException {
			Uri.Builder uri = Api.createUrlBuild(APP_NAME, ApiUsers.MOD_NAME,
					ApiUsers.SEARCH_BY_AREA);
			ListData<SociaxItem> listData = new ListData<SociaxItem>();
			Get get = new Get();
			get.setUri(uri);
			get.append("areaid", key);
			try {
				JSONObject jsonObject = new JSONObject((String) Api.run(get));
				JSONArray jsonArray = (jsonObject.getJSONArray("data"));
				for (int i = 0; i < jsonArray.length(); i++) {
					User user = new User(jsonArray.getJSONObject(i));
					listData.add(user);
				}
			} catch (Exception e) {
				Log.d(TAG, e.toString());
			}
			return listData;
		}

		@Override
		public ListData<SociaxItem> searchByTag(String key) throws ApiException {
			Uri.Builder uri = Api.createUrlBuild(APP_NAME, ApiUsers.MOD_NAME,
					ApiUsers.SEARCH_BY_TAG);
			ListData<SociaxItem> listData = new ListData<SociaxItem>();
			Get get = new Get();
			get.setUri(uri);
			get.append("tagid", key);
			try {
				JSONObject jsonObject = new JSONObject((String) Api.run(get));
				JSONArray jsonArray = (jsonObject.getJSONArray("data"));
				for (int i = 0; i < jsonArray.length(); i++) {
					User user = new User(jsonArray.getJSONObject(i));
					listData.add(user);
				}
			} catch (Exception e) {
				Log.d(TAG, e.toString());
			}
			return listData;
		}

		@Override
		public ListData<SociaxItem> searchByVerifyCategory(String key)
				throws ApiException {
			Uri.Builder uri = Api.createUrlBuild(APP_NAME, ApiUsers.MOD_NAME,
					ApiUsers.SEARCH_BY_VERIFY_CATEGORY);
			ListData<SociaxItem> listData = new ListData<SociaxItem>();
			Get get = new Get();
			get.setUri(uri);
			get.append("verifyid", key);
			try {
				JSONObject jsonObject = new JSONObject((String) Api.run(get));
				JSONArray jsonArray = (jsonObject.getJSONArray("data"));
				for (int i = 0; i < jsonArray.length(); i++) {
					User user = new User(jsonArray.getJSONObject(i));
					listData.add(user);
				}
			} catch (Exception e) {
				Log.d(TAG, e.toString());
			}
			return listData;
		}

		@Override
		public ListData<SociaxItem> searchByUesrCategory(String key)
				throws ApiException {
			Uri.Builder uri = Api.createUrlBuild(APP_NAME, ApiUsers.MOD_NAME,
					ApiUsers.SEARCH_BY_UESR_CATEGORY);
			ListData<SociaxItem> listData = new ListData<SociaxItem>();
			Get get = new Get();
			get.setUri(uri);
			get.append("cateid", key);
			try {
				JSONObject jsonObject = new JSONObject((String) Api.run(get));
				JSONArray jsonArray = (jsonObject.getJSONArray("data"));
				for (int i = 0; i < jsonArray.length(); i++) {
					User user = new User(jsonArray.getJSONObject(i));
					listData.add(user);
				}
			} catch (Exception e) {
				Log.d(TAG, e.toString());
			}
			return listData;
		}
	}

	/**
	 * 联系人 api 类
	 * 
	 */
	public static final class STContacts implements ApiContact {

		@Override
		public ListData<SociaxItem> getContactCategoryList(int departId)
				throws ApiException {
			// TODO Auto-generated method stub
			beforeTimeline(ApiContact.GET_DEPARTMENT_LIST);
			if (departId > 0)
				Api.get.append("deptId", departId);
			Object result = Api.run(Api.get);
			Api.checkResult(result);
			ListData<SociaxItem> list = null;
			try {
				if (!result.equals("null")) {
					JSONArray data = new JSONArray((String) result);
					int length = data.length();
					list = new ListData<SociaxItem>();
					if (departId <= 0) {
						ContactCategory cc1 = new ContactCategory(-2, "我的联系人",
								"department");
						ContactCategory cc2 = new ContactCategory(-1, "所有同事",
								"department");
						list.add(cc1);
						list.add(cc2);
					}
					for (int i = 0; i < length; i++) {
						JSONObject jsonObject = data.getJSONObject(i);
						ContactCategory contactCategory = new ContactCategory(
								jsonObject);
						contactCategory.setType("department");
						list.add(contactCategory);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				Log.d(TAG, e.toString());
			}
			return list;
		}

		/**
		 * 获取联系人列表
		 * 
		 * @return
		 * @throws ApiException
		 */
		@Override
		public ListData<SociaxItem> getAllContactList() throws ApiException {
			beforeTimeline(ApiContact.GET_ALL_COLLEAGUE);
			Object result = Api.run(Api.get);
			Api.checkResult(result);
			ListData<SociaxItem> list = null;
			try {
				if (!result.equals("null")) {

					JSONArray data = new JSONArray((String) result);
					list = new ListData<SociaxItem>();

					for (int i = 0; i < data.length(); i++) {
						JSONObject jsonObject = data.getJSONObject(i);
						Contact contact = new Contact(jsonObject);
						list.add(contact);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				Log.d(TAG, e.toString());
			}
			return list;
		}

		private void beforeTimeline(String act) {
			Uri.Builder uri = Api.createUrlBuild(ApiContact.MOD_NAME, act);
			Api.get.setUri(uri);
		}

		@Override
		public ListData<SociaxItem> getContactListFooter(Contact contact,
				int count) throws ApiException {
			// TODO Auto-generated method stub
			beforeTimeline(ApiContact.GET_ALL_COLLEAGUE);
			Api.get.append("max_id", contact.getUid());
			Api.get.append("count", count);
			Object result = Api.run(Api.get);
			Api.checkResult(result);
			ListData<SociaxItem> list = null;
			try {
				if (!result.equals("null")) {

					JSONArray data = new JSONArray((String) result);
					list = new ListData<SociaxItem>();

					for (int i = 0; i < data.length(); i++) {
						JSONObject jsonObject = data.getJSONObject(i);
						Contact contact2 = new Contact(jsonObject);
						list.add(contact2);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				Log.d(TAG, e.toString());
			}
			return list;
		}

		@Override
		public ListData<SociaxItem> getColleagueByDepartment(int departId)
				throws ApiException {
			beforeTimeline(ApiContact.GET_COLLEAGUE_BY_DEPARTMENT);
			Api.get.append("id", departId);
			Object result = Api.run(Api.get);
			Api.checkResult(result);
			ListData<SociaxItem> list = null;
			try {
				if (!result.equals("null")) {

					JSONArray data = new JSONArray((String) result);
					list = new ListData<SociaxItem>();

					for (int i = 0; i < data.length(); i++) {
						JSONObject jsonObject = data.getJSONObject(i);
						Contact contact2 = new Contact(jsonObject);
						list.add(contact2);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				Log.d(TAG, e.toString());
			}
			return list;
		}

		@Override
		public ListData<SociaxItem> getColleagueByDepartmentFooter(
				int departId, ContactCategory category, int count)
				throws ApiException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public ListData<SociaxItem> getMyContacter() throws ApiException {
			// TODO Auto-generated method stub
			beforeTimeline(ApiContact.GET_MY_CONTACTER);
			Object result = Api.run(Api.get);
			Api.checkResult(result);
			ListData<SociaxItem> list = null;
			try {
				if (!result.equals("null")) {

					JSONArray data = new JSONArray((String) result);
					list = new ListData<SociaxItem>();

					for (int i = 0; i < data.length(); i++) {
						JSONObject jsonObject = data.getJSONObject(i);
						Contact contact = new Contact(jsonObject);
						list.add(contact);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				Log.d(TAG, e.toString());
			}
			return list;
		}

		@Override
		public boolean contacterCreate(User user) throws ApiException {
			// TODO Auto-generated method stub
			return doApiRuning(user, Api.post, ApiContact.CONTACTER_CREATE);
		}

		@Override
		public boolean contacterDestroy(User user) throws ApiException {
			// TODO Auto-generated method stub
			return doApiRuning(user, Api.post, ApiContact.CONTACTER_DESTROY);
		}

		private boolean doApiRuning(User user, Request res, String act)
				throws ApiException {
			Uri.Builder uri = Api.createUrlBuild(ApiContact.MOD_NAME, act);
			Api.post.setUri(uri);
			Api.post.append("user_id", user.getUid());
			Object result = Api.run(Api.post);
			Api.checkResult(result);
			if (result != null) {
				try {
					int stataCode = Integer.valueOf(((String) result));
					return stataCode == 1 ? true : false;

				} catch (Exception e) {
					Log.d(APP_TAG, " doruning wm" + e.toString());
					throw new ApiException("操作失败");
				}
			}
			return false;
		}

		@Override
		public ListData<SociaxItem> getDataByDepartment(int departId,
				int isDepart) throws ApiException {
			// TODO Auto-generated method stub
			beforeTimeline(ApiContact.GET_DATA_BY_DEPARTMENT);
			Api.get.append("id", departId);
			Api.get.append("isDepart", 1);
			Object result = Api.run(Api.get);
			Api.checkResult(result);
			ListData<SociaxItem> list = null;
			try {
				if (!result.equals("null")) {

					JSONArray data = new JSONArray((String) result);
					list = new ListData<SociaxItem>();

					for (int i = 0; i < data.length(); i++) {
						JSONObject jsonObject = data.getJSONObject(i);

						Contact contact2 = null;
						if (jsonObject.has("type")) {
							if (jsonObject.getString("type").equals(
									"department")) {
								contact2 = new ContactCategory(jsonObject);
							} else if (jsonObject.getString("type").equals(
									"user")) {
								contact2 = new Contact(jsonObject);
							}
						} else {
							contact2 = new Contact(jsonObject);
						}
						list.add(contact2);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				Log.d(TAG, e.toString());
			}
			return list;
		}

		@Override
		public ListData<SociaxItem> getDataByDepartmentFooter(int departId,
				int isDepart) throws ApiException {
			beforeTimeline(ApiContact.GET_DATA_BY_DEPARTMENT);
			Api.get.append("id", departId);
			Object result = Api.run(Api.get);
			Api.checkResult(result);
			ListData<SociaxItem> list = null;
			try {
				if (!result.equals("null")) {

					JSONArray data = new JSONArray((String) result);
					list = new ListData<SociaxItem>();

					for (int i = 0; i < data.length(); i++) {
						JSONObject jsonObject = data.getJSONObject(i);

						Contact contact2 = new Contact(jsonObject);
						;
						list.add(contact2);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				Log.d(TAG, e.toString());
			}
			return list;
		}

		@Override
		public ListData<SociaxItem> searchColleague(String key)
				throws ApiException {
			// TODO Auto-generated method stub
			beforeTimeline(ApiContact.SEARCH_COLLEAGUE);
			Api.get.append("key", key);
			Object result = Api.run(Api.get);
			Api.checkResult(result);
			ListData<SociaxItem> list = null;
			try {
				if (!result.equals("null")) {

					JSONArray data = new JSONArray((String) result);
					list = new ListData<SociaxItem>();

					for (int i = 0; i < data.length(); i++) {
						JSONObject jsonObject = data.getJSONObject(i);
						Contact contact = new Contact(jsonObject);
						list.add(contact);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				Log.d(TAG, e.toString());
			}
			return list;
		}
	}

	/**
	 * 类说明：任务API实现类
	 * 
	 * @author Povol
	 */
	public static final class Tasks implements ApiTask {

		private Uri.Builder beforeTimeline(String act) {
			return Api.createUrlBuild(ApiTask.MOD_NAME, act);
		}

		@Override
		public ListData<SociaxItem> getTaskCategoryList() throws ApiException {
			// TODO Auto-generated method stub
			Api.get.setUri(beforeTimeline(ApiTask.GET_ALL_COLLEAGUE));
			Object result = Api.run(Api.get);

			ListData<SociaxItem> list = null;
			try {
				if (!result.equals("null")) {
					JSONArray data = new JSONArray((String) result);
					int length = data.length();
					list = new ListData<SociaxItem>();
					for (int i = 0; i < length; i++) {
						JSONObject jsonObject = data.getJSONObject(i);
						TaskCategory tCategory = new TaskCategory(jsonObject);
						list.add(tCategory);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				Log.d(TAG, e.toString());
			} finally {
				return list;
			}
		}

		@Override
		public boolean createTaskCate(TaskCategory tCategory)
				throws ApiException {
			// TODO Auto-generated method stub
			Api.post.setUri(beforeTimeline(ApiTask.CREATE_TASK_CATEGORY));
			Api.post.append("category_name", tCategory.getName());
			Object result = Api.run(Api.post);
			int code = Integer.valueOf((String) result);

			switch (code) {
			case 0:
				Log.d(APP_TAG, "create task cate fail ....	");
				throw new ApiException("操作失败");
			case 1:
				return true;
			case 2:
				Log.d(APP_TAG, "create task cate fail ....	");
				throw new ApiException("分类名称重复");
			}
			return false;
		}

		@Override
		public boolean destroyTaskCate(TaskCategory tCategory)
				throws ApiException {
			Api.post.setUri(beforeTimeline(ApiTask.DESTROY_TASK_CATEGORY));
			Api.post.append("category_id", tCategory.gettId());
			Object result = Api.run(Api.post);
			int code = Integer.valueOf((String) result);

			switch (code) {
			case 0:
				Log.d(APP_TAG, "destroy task cate fail ....	");
				throw new ApiException("操作失败");
			case 1:
				return true;
			case 2:
				Log.d(APP_TAG, "destroy task cate fail ....	");
				throw new ApiException("分类中存在任务不能删除！");
			}
			return false;
		}

		@Override
		public boolean eidtTaskCate(TaskCategory tCategory) throws ApiException {
			// TODO Auto-generated method stub
			Api.post.setUri(beforeTimeline(ApiTask.EDIT_TASK_CATEGORY));
			Api.post.append("category_id", tCategory.gettId());
			Api.post.append("category_name", tCategory.getName());
			Object result = Api.run(Api.post);
			int code = Integer.valueOf((String) result);

			switch (code) {
			case 0:
				Log.d(APP_TAG, "create task cate fail ....	");
				throw new ApiException("操作失败");
			case 1:
				return true;
			case 2:
				Log.d(APP_TAG, "create task cate fail ....	");
				throw new ApiException("分类名称重复");
			}
			return false;
		}

		@Override
		public boolean shareTaskCate(TaskCategory tCategory)
				throws ApiException {
			// TODO Auto-generated method stub
			Api.post.setUri(beforeTimeline(ApiTask.SHARE_TASK_CATEGORY));
			Api.post.append("category_id", tCategory.gettId());
			Api.post.append("user_emails", tCategory.getEmailList());
			Object result = Api.run(Api.post);
			int code = Integer.valueOf((String) result);

			switch (code) {
			case 0:
				Log.d(APP_TAG, "share task cate fail ....	");
				throw new ApiException("操作失败");
			case 1:
				return true;
			}
			return false;
		}

		@Override
		public boolean delShareTaskCate(TaskCategory tCategory)
				throws ApiException {
			// TODO Auto-generated method stub
			Api.post.setUri(beforeTimeline(ApiTask.CANCEL_SHARE_TASK_CATEGORY));
			Api.post.append("category_id", tCategory.gettId());
			Object result = Api.run(Api.post);
			int code = Integer.valueOf((String) result);

			switch (code) {
			case 0:
				Log.d(APP_TAG, "cancel share task cate fail ....	");
				throw new ApiException("操作失败");
			case 1:
				return true;
			}
			return false;
		}

		@Override
		public ListData<SociaxItem> getTaskListByCategory(TaskCategory tCategory)
				throws ApiException {
			// TODO Auto-generated method stub
			Api.get.setUri(beforeTimeline(ApiTask.GET_TASK_BY_CATEGORY));
			Api.get.append("category_id", tCategory.gettId());
			Object result = Api.run(Api.get);

			ListData<SociaxItem> list = null;
			try {
				if (!result.equals("null")) {
					JSONArray data = new JSONArray((String) result);
					int length = data.length();
					list = new ListData<SociaxItem>();
					for (int i = 0; i < length; i++) {
						JSONObject jsonObject = data.getJSONObject(i);
						Task task = new Task(jsonObject);
						list.add(task);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				Log.d(TAG, e.toString());
			} finally {
				return list;
			}
		}

		@Override
		public boolean destroyTask(Task task) throws ApiException {
			// TODO Auto-generated method stub
			Api.post.setUri(beforeTimeline(ApiTask.DESTROY_TASK));
			Api.post.append("task_id", task.getTaskId());
			Api.post.append("category_id", task.getCateId());
			Object result = Api.run(Api.post);
			int code = Integer.valueOf((String) result);

			switch (code) {
			case 0:
				Log.d(APP_TAG, "destroy task fail ....	");
				throw new ApiException("操作失败");
			case 1:
				return true;
			}
			return false;
		}

		@Override
		public boolean createTask(Task task) throws ApiException {
			// TODO Auto-generated method stub
			Api.post.setUri(beforeTimeline(ApiTask.CREATE_TASK));
			Api.post.append("task", task.getTaskTitle());
			Api.post.append("category_id", task.getCateId());
			if (task.getDeadline() != null && !task.getDeadline().equals(""))
				Api.post.append("date", task.getDeadline());
			if (task.getType() != null && !task.getType().equals(""))
				Api.post.append("type", task.getType());
			Object result = Api.run(Api.post);
			int code = Integer.valueOf((String) result);

			switch (code) {
			case 0:
				Log.d(APP_TAG, "create task cate fail ....	");
				throw new ApiException("操作失败");
			case 2:
				Log.d(APP_TAG, "create task cate fail ....	");
				throw new ApiException("分类名称重复");
			default:
				return true;
			}
		}

		@Override
		public boolean editTask(Task task) throws ApiException {
			// TODO Auto-generated method stub
			Api.post.setUri(beforeTimeline(ApiTask.EDIT_TASK));
			Api.post.append("task_id", task.getTaskId());
			Api.post.append("task", task.getTaskTitle());
			Api.post.append("user_id", task.getJoiner_uid());
			Api.post.append("date", task.getDeadline());
			Api.post.append("task_detail", task.getDesc());
			Object result = Api.run(Api.post);

			int code = -1;

			try {
				code = Integer.valueOf((String) result);
			} catch (Exception e) {
				Log.d(APP_TAG, "create task cate fail ....	");
				throw new ApiException("操作失败");
			}

			switch (code) {
			case 0:
				Log.d(APP_TAG, "create task cate fail ....	");
				throw new ApiException("操作失败");
			case 1:
				return true;
			case 2:
				Log.d(APP_TAG, "create task cate fail ....	");
				throw new ApiException("分类名称重复");
			}
			return false;
		}

		@Override
		public boolean starTask(Task task) throws ApiException {
			// TODO Auto-generated method stub

			Api.post.setUri(beforeTimeline(ApiTask.STARRED_TASK));
			Api.post.append("task_id", task.getTaskId());
			Object result = Api.run(Api.post);
			int code = Integer.valueOf((String) result);

			switch (code) {
			case 0:
				Log.d(APP_TAG, "destroy task fail ....	");
				throw new ApiException("操作失败");
			case 1:
				return true;
			}
			return false;
		}

		@Override
		public boolean unStarTask(Task task) throws ApiException {
			// TODO Auto-generated method stub

			Api.post.setUri(beforeTimeline(ApiTask.CANCEL_STARRED_TASK));
			Api.post.append("task_id", task.getTaskId());
			Object result = Api.run(Api.post);
			int code = Integer.valueOf((String) result);

			switch (code) {
			case 0:
				Log.d(APP_TAG, "un star task fail ....	");
				throw new ApiException("操作失败");
			case 1:
				return true;
			}
			return false;
		}

		@Override
		public boolean doTask(Task task) throws ApiException {
			// TODO Auto-generated method stub
			Api.post.setUri(beforeTimeline(ApiTask.FINISHED_TASK));
			Api.post.append("task_id", task.getTaskId());
			Object result = Api.run(Api.post);
			int code = Integer.valueOf((String) result);

			switch (code) {
			case 0:
				Log.d(APP_TAG, "do task fail ....	");
				throw new ApiException("操作失败");
			case 1:
				return true;
			}
			return false;
		}

		@Override
		public boolean unDoTask(Task task) throws ApiException {
			// TODO Auto-generated method stub
			Api.post.setUri(beforeTimeline(ApiTask.CANCEL_FINISHED_TASK));
			Api.post.append("task_id", task.getTaskId());
			Object result = Api.run(Api.post);
			int code = Integer.valueOf((String) result);

			switch (code) {
			case 0:
				Log.d(APP_TAG, "destroy task fail ....	");
				throw new ApiException("操作失败");
			case 1:
				return true;
			}
			return false;
		}

		@Override
		public ListData<SociaxItem> getTaskByType(TaskCategory tCategory)
				throws ApiException {
			// TODO Auto-generated method stub
			Api.get.setUri(beforeTimeline(ApiTask.GET_TASK_BY_TYPE));
			Api.get.append("type", tCategory.getCataType());
			Object result = Api.run(Api.get);

			ListData<SociaxItem> list = null;
			try {
				if (!result.equals("null")) {
					JSONArray data = new JSONArray((String) result);
					int length = data.length();
					list = new ListData<SociaxItem>();
					for (int i = 0; i < length; i++) {
						JSONObject jsonObject = data.getJSONObject(i);
						Task task = new Task(jsonObject);
						list.add(task);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				Log.d(TAG, e.toString());
			} finally {
				return list;
			}
		}

		@Override
		public String getTaskNotify() throws ApiException {
			// TODO Auto-generated method stub
			Api.get.setUri(beforeTimeline(ApiTask.GET_TASK_NOTIFY));
			Object result = Api.run(Api.get);
			return (String) result;
		}

		@SuppressWarnings("finally")
		@Override
		public ListData<SociaxItem> getShareUser(int ctaeId)
				throws ApiException {
			// TODO Auto-generated method stub
			Api.get.setUri(beforeTimeline(ApiTask.GET_SHARE_USERS));
			Api.get.append("category_id", ctaeId);
			Object result = Api.run(Api.get);

			ListData<SociaxItem> list = null;
			try {
				if (!result.equals("null")) {
					JSONArray data = new JSONArray((String) result);
					int length = data.length();
					list = new ListData<SociaxItem>();
					for (int i = 0; i < length; i++) {
						JSONObject jsonObject = data.getJSONObject(i);
						User user = new User(jsonObject);
						list.add(user);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				Log.d(TAG, e.toString());
			} finally {
				return list;
			}
		}

		@SuppressWarnings("finally")
		@Override
		public ListData<SociaxItem> getTaskBySearchKey(String key)
				throws ApiException {
			// TODO Auto-generated method stub
			Api.get.setUri(beforeTimeline(ApiTask.SEARCH_TASK));
			Api.get.append("keyword", key);
			Object result = Api.run(Api.get);

			ListData<SociaxItem> list = null;
			try {
				if (!result.equals("null")) {
					JSONArray data = new JSONArray((String) result);
					int length = data.length();
					list = new ListData<SociaxItem>();
					for (int i = 0; i < length; i++) {
						JSONObject jsonObject = data.getJSONObject(i);
						Task task = new Task(jsonObject);
						list.add(task);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				Log.d(TAG, e.toString());
			} finally {
				return list;
			}
		}

	}

	/**
	 * 类说明：帮助中心API实现类
	 * 
	 * @author Povol
	 */
	public static final class Questions implements ApiQuestion {

		private Uri.Builder beforeTimeline(String act) {
			return Api.createUrlBuild(ApiQuestion.MOD_NAME, act);
		}

		@SuppressWarnings("finally")
		@Override
		public ListData<SociaxItem> getHotQuestion() throws ApiException {
			Api.get.setUri(beforeTimeline(ApiQuestion.GET_HOT_QUESTION));
			Object result = Api.run(Api.get);

			ListData<SociaxItem> list = null;
			try {
				if (!result.equals("null")) {
					JSONArray data = new JSONArray((String) result);
					int length = data.length();
					list = new ListData<SociaxItem>();
					for (int i = 0; i < length; i++) {
						JSONObject jsonObject = data.getJSONObject(i);
						Question question = new Question(jsonObject);
						list.add(question);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				Log.d(TAG, e.toString());
			} finally {
				return list;
			}
		}

		@SuppressWarnings("finally")
		@Override
		public ListData<SociaxItem> searchQuestion(String key)
				throws ApiException {
			Api.get.setUri(beforeTimeline(ApiQuestion.SEARCH_QUESTION));
			Api.get.append("keyword", key);
			Object result = Api.run(Api.get);

			ListData<SociaxItem> list = null;
			try {
				if (!result.equals("null")) {
					JSONArray data = new JSONArray((String) result);
					int length = data.length();
					list = new ListData<SociaxItem>();
					for (int i = 0; i < length; i++) {
						JSONObject jsonObject = data.getJSONObject(i);
						Question question = new Question(jsonObject);
						list.add(question);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				Log.d(TAG, e.toString());
			} finally {
				return list;
			}
		}

		@SuppressWarnings({ "finally", "null" })
		@Override
		public ListData<SociaxItem> getCategory() throws ApiException {
			Api.get.setUri(beforeTimeline(ApiQuestion.GET_CATEGORY));
			Object result = Api.run(Api.get);

			ListData<SociaxItem> list = null;
			try {
				if (!result.equals("null")) {

					JSONArray data = new JSONArray((String) result);
					int length = data.length();
					list = new ListData<SociaxItem>();
					for (int i = 0; i < length; i++) {
						JSONObject jsonObject = data.getJSONObject(i);
						QuesCate qCate = new QuesCate();
						qCate.setqCateId(jsonObject.getInt("id"));
						qCate.setqCateName(jsonObject.getString("name"));
						;

						JSONArray temp = jsonObject.getJSONArray("child");

						ListData<SociaxItem> cList = null;
						int l = temp.length();
						if (l > 0) {
							cList = new ListData<SociaxItem>();
							for (int j = 0; j < l; j++) {
								JSONObject cjsonObject = temp.getJSONObject(j);
								QuesCate cq = new QuesCate();
								cq.setqCateId(cjsonObject.getInt("id"));
								cq.setqCateName(cjsonObject.getString("name"));
								cList.add(cq);
							}
						}
						qCate.setChildList(cList);
						list.add(qCate);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				Log.d(TAG, e.toString());
			} finally {
				return list;
			}
		}

		@SuppressWarnings("finally")
		@Override
		public ListData<SociaxItem> searchCategory(String key)
				throws ApiException {
			Api.get.setUri(beforeTimeline(ApiQuestion.SEARCH_CATEGORY));
			Api.get.append("keyword", key);
			Object result = Api.run(Api.get);

			ListData<SociaxItem> list = null;
			try {
				if (!result.equals("null")) {
					JSONArray data = new JSONArray((String) result);
					int length = data.length();
					list = new ListData<SociaxItem>();
					for (int i = 0; i < length; i++) {
						JSONObject jsonObject = data.getJSONObject(i);
						QuesCate quesCate = new QuesCate(jsonObject);
						list.add(quesCate);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				Log.d(TAG, e.toString());
			} finally {
				return list;
			}
		}

		@SuppressWarnings("finally")
		@Override
		public ListData<SociaxItem> getQuestionByCate(int cateId)
				throws ApiException {
			Api.get.setUri(beforeTimeline(ApiQuestion.GET_QUESTION_BY_CATEGORY));
			Api.get.append("category_id", cateId);
			Object result = Api.run(Api.get);

			ListData<SociaxItem> list = null;
			try {
				if (!result.equals("null")) {
					JSONArray data = new JSONArray((String) result);
					int length = data.length();
					list = new ListData<SociaxItem>();
					for (int i = 0; i < length; i++) {
						JSONObject jsonObject = data.getJSONObject(i);
						Question question = new Question(jsonObject);
						list.add(question);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				Log.d(TAG, e.toString());
			} finally {
				return list;
			}
		}

		@Override
		public Question questionShow(int quesId) throws ApiException {
			Api.get.setUri(beforeTimeline(ApiQuestion.SHOW_QUESTION));
			Api.get.append("support_id", quesId);
			Object result = Api.run(Api.get);
			try {
				JSONObject jsonObject = new JSONObject((String) result);
				return new Question(jsonObject);
			} catch (Exception e) {
				throw new ApiException("获取信息失败");
			}
		}
	}

	/**
	 * 文档api实现类
	 */
	public static final class Documents implements ApiDocument {

		@SuppressWarnings("finally")
		@Override
		public ListData<SociaxItem> getDocumentCategoryList()
				throws ApiException {
			beforeTimeline(ApiDocument.CATEGORY_LIST);
			Object result = Api.run(Api.get);
			Api.checkResult(result);
			ListData<SociaxItem> list = null;
			try {
				if (!result.equals("null")) {
					JSONArray data = new JSONArray((String) result);
					int length = data.length();
					list = new ListData<SociaxItem>();
					for (int i = 0; i < length; i++) {
						JSONObject jsonObject = data.getJSONObject(i);
						ContactCategory contactCategory = new ContactCategory(
								jsonObject);
						list.add(contactCategory);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				Log.d(TAG, e.toString());
			} finally {
				return list;
			}
		}

		/**
		 * 获取文档列表
		 */
		@Override
		public ListData<SociaxItem> getDocumentList() throws ApiException {
			// TODO Auto-generated method stub

			beforeTimeline(ApiDocument.ALL_DOCUMENTLIST);
			Object result = Api.run(Api.get);
			Api.checkResult(result);
			ListData<SociaxItem> list = null;
			try {
				if (!result.equals("null")) {
					JSONObject odata = new JSONObject((String) result);

					JSONArray data = odata.getJSONArray("data");
					int length = data.length();
					list = new ListData<SociaxItem>();
					for (int i = 0; i < length; i++) {
						JSONObject jsonObject = data.getJSONObject(i);
						Document document = new Document(jsonObject);
						list.add(document);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				Log.d(TAG, e.toString());
			}
			return list;
		}

		private void beforeTimeline(String act) {
			Uri.Builder uri = Api.createUrlBuild(ApiDocument.MOD_NAME, act);
			Api.get.setUri(uri);
		}

	}

	/*
	 * public static final class MobileApps implements ApiMobileApps {
	 * 
	 * @Override public ListData<SociaxItem> getMobileAppsList() throws
	 * ApiException { // TODO Auto-generated method stub
	 * beforeTimeline(ApiMobileApps.GET_APPS_LIST); Object result =
	 * Api.run(Api.get); Api.checkResult(result); ListData<SociaxItem> list =
	 * null; try { if (!result.equals("null")) { JSONArray data = new
	 * JSONArray((String) result); int length = data.length(); list = new
	 * ListData<SociaxItem>(); for (int i = 0; i < length; i++) { JSONObject
	 * jsonObject = data.getJSONObject(i); MobileApp mobileApp = new
	 * MobileApp(jsonObject); list.add(mobileApp); } } } catch (Exception e) {
	 * e.printStackTrace(); Log.d(TAG, e.toString()); } return list; }
	 * 
	 * @Override public ListData<SociaxItem> getUserAppsList() throws
	 * ApiException { // TODO Auto-generated method stub
	 * beforeTimeline(ApiMobileApps.GET_USER_APPS_LIST); Object result =
	 * Api.run(Api.get); Api.checkResult(result); ListData<SociaxItem> list =
	 * null; try { if (!result.equals("null")) { JSONArray data = new
	 * JSONArray((String) result); int length = data.length(); list = new
	 * ListData<SociaxItem>(); for (int i = 0; i < length; i++) { JSONObject
	 * jsonObject = data.getJSONObject(i); MobileApp mobileApp = new
	 * MobileApp(jsonObject); list.add(mobileApp); } } } catch (Exception e) {
	 * e.printStackTrace(); Log.d(TAG, e.toString()); } return list; }
	 * 
	 * @Override public ListData<SociaxItem> searchAppsList() throws
	 * ApiException { // TODO Auto-generated method stub return null; }
	 * 
	 * @Override public boolean installApp(int uid, int appid) throws
	 * ApiException { beforeTimeline(ApiMobileApps.INSTALL);
	 * 
	 * Api.get.append("app_id", appid); Object result = Api.run(Api.get); if
	 * (result.equals("1")) { return true; } else { return true; } }
	 * 
	 * @Override public boolean uninstallApp(int uid, int appid) throws
	 * ApiException { // TODO Auto-generated method stub
	 * beforeTimeline(ApiMobileApps.UN_INSTALL);
	 * 
	 * // Api.get.append("user_id", uid); Api.get.append("app_id", appid);
	 * Object result = Api.run(Api.get); if (result.equals("1")) { return true;
	 * } else { return true; } }
	 * 
	 * private void beforeTimeline(String act) { Uri.Builder uri =
	 * Api.createUrlBuild(ApiMobileApps.MOD_NAME, act); Api.get.setUri(uri); }
	 * 
	 * }
	 */

	/**
	 * 微吧接口实现类
	 */
	public static final class WeibaApi implements ApiWeiba {

		private Uri.Builder beforeTimeline(String act) {
			return Api.createUrlBuild(ApiWeiba.MOD_NAME, act);
		}

		/**
		 * 处理请求微吧返回的数据
		 * 
		 * @param result
		 * @return 微博list
		 */
		@SuppressWarnings("finally")
		private ListData<SociaxItem> getWeibaList(Object result) {
			ListData<SociaxItem> list = null;
			try {
				if (!result.equals("null")) {
					JSONArray data = new JSONArray((String) result);
					int length = data.length();
					list = new ListData<SociaxItem>();
					for (int i = 0; i < length; i++) {
						JSONObject jsonObject = data.getJSONObject(i);
						Weiba weiba = new Weiba(jsonObject);
						list.add(weiba);
					}
				}
			} catch (Exception e) {
				Log.d(TAG, e.toString());
			} finally {
				return list;
			}
		}

		@Override
		public List<SociaxItem> getWeibas() throws ApiException {
			Api.get.setUri(beforeTimeline(ApiWeiba.GET_WEIBAS));
			return getWeibaList(Api.run(Api.get));
		}

		@Override
		public List<SociaxItem> getWeibasHeader(Weiba weiba, int page, int count)
				throws ApiException {
			Api.get.setUri(beforeTimeline(ApiWeiba.GET_WEIBAS));
			Api.get.append("since_id", weiba.getWeibaId());
			return getWeibaList(Api.run(Api.get));
		}

		@Override
		public List<SociaxItem> getWeibasFooter(Weiba weiba, int page, int count)
				throws ApiException {
			Api.get.setUri(beforeTimeline(ApiWeiba.GET_WEIBAS));
			Api.get.append("max_id", weiba.getWeibaId());
			return getWeibaList(Api.run(Api.get));
		}

		@Override
		public boolean create(int weibaId) throws ApiException {
			Api.get.setUri(beforeTimeline(ApiWeiba.CREATE));
			Api.get.append("id", weibaId);
			return getBoolValue(Api.run(Api.get));
		}

		@Override
		public boolean destroy(int weibaId) throws ApiException {
			Api.get.setUri(beforeTimeline(ApiWeiba.DESTROY));
			Api.get.append("id", weibaId);
			return getBoolValue(Api.run(Api.get));
		}

		@Override
		public boolean cretePost(Posts posts) throws ApiException {
			Api.post.setUri(beforeTimeline(ApiWeiba.CREATE_POST));
			Api.post.append("id", posts.getWeibaId());
			Api.post.append("title", posts.getTitle());
			Api.post.append("content", posts.getContent());
			return getBoolValue(Api.run(Api.post));
		}

		/**
		 * 处理请求帖子列表返回的数据
		 * 
		 * @param result
		 * @return 帖子list
		 */
		@SuppressWarnings("finally")
		private ListData<SociaxItem> getPostsList(Object result) {
			ListData<SociaxItem> list = null;
			try {
				if (!result.equals("null")) {
					JSONArray data = new JSONArray((String) result);
					int length = data.length();
					list = new ListData<SociaxItem>();
					for (int i = 0; i < length; i++) {
						JSONObject jsonObject = data.getJSONObject(i);
						Posts post = new Posts(jsonObject);
						list.add(post);
					}
				}
			} catch (Exception e) {
				Log.d(TAG, e.toString());
			} finally {
				return list;
			}
		}

		private boolean getBoolValue(Object result) {
			return Integer.valueOf((String) result) == 1 ? true : false;
		}

		@Override
		public List<SociaxItem> getPosts(int weibaId) throws ApiException {
			Api.get.setUri(beforeTimeline(ApiWeiba.GET_POSTS));
			Api.get.append("id", weibaId);
			return getPostsList(Api.run(Api.get));
		}

		@Override
		public List<SociaxItem> getPostsHeader(int weibaId, int page, int count)
				throws ApiException {
			Api.get.setUri(beforeTimeline(ApiWeiba.GET_POSTS));
			Api.get.append("id", weibaId);
			Api.get.append("page", page);
			Api.get.append("count", count);
			return getPostsList(Api.run(Api.get));
		}

		@Override
		public List<SociaxItem> getPostsFooter(int weibaId, int page, int count)
				throws ApiException {
			Api.get.setUri(beforeTimeline(ApiWeiba.GET_POSTS));
			Api.get.append("id", weibaId);
			Api.get.append("page", page);
			Api.get.append("count", count);
			return getPostsList(Api.run(Api.get));
		}

		@Override
		public Posts postDetail(int postsId) throws ApiException {
			Api.get.setUri(beforeTimeline(ApiWeiba.POST_DETAIL));
			Api.get.append("id", postsId);
			String result = (String) Api.run(get);
			try {
				return (!result.equals("null")) ? new Posts(new JSONObject(
						result)) : null;
			} catch (DataInvalidException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		public List<SociaxItem> getCommentList(int postId) throws ApiException {
			Api.get.setUri(beforeTimeline(ApiWeiba.COMMENT_LIST));
			Api.get.append("id", postId);
			return getCommentList(Api.run(Api.get));
		}

		@Override
		public List<SociaxItem> getCommentListHeader(int postId, int page,
				int count) throws ApiException {
			Api.get.setUri(beforeTimeline(ApiWeiba.COMMENT_LIST));
			Api.get.append("id", postId);
			Api.get.append("page", page);
			Api.get.append("count", count);
			return getCommentList(Api.run(Api.get));
		}

		@Override
		public List<SociaxItem> getCommentListFooter(int postId, int page,
				int count) throws ApiException {
			Api.get.setUri(beforeTimeline(ApiWeiba.COMMENT_LIST));
			Api.get.append("id", postId);
			Api.get.append("page", page);
			Api.get.append("count", count);
			return getCommentList(Api.run(Api.get));
		}

		/**
		 * 处理请求微吧返回的数据
		 * 
		 * @param result
		 * @return 微博list
		 */
		@SuppressWarnings("finally")
		private ListData<SociaxItem> getCommentList(Object result) {
			ListData<SociaxItem> list = null;
			try {
				if (!result.equals("null")) {
					JSONArray data = new JSONArray((String) result);
					int length = data.length();
					list = new ListData<SociaxItem>();
					for (int i = 0; i < length; i++) {
						JSONObject jsonObject = data.getJSONObject(i);
						CommentPost cPost = new CommentPost(jsonObject);
						list.add(cPost);
					}
				}
			} catch (Exception e) {
				Log.d(TAG, e.toString());
			} finally {
				return list;
			}
		}

		@Override
		public boolean commentPost(CommentPost cPost) throws ApiException {
			Api.post.setUri(beforeTimeline(ApiWeiba.COMMENT_POST));
			Api.post.append("id", cPost.getPostId());
			Api.post.append("content", cPost.getContent());
			return getBoolValue(Api.run(Api.post));
		}

		@Override
		public boolean favoritePost(int postId) throws ApiException {
			Api.post.setUri(beforeTimeline(ApiWeiba.POST_FAVORITE));
			Api.post.append("id", postId);
			return getBoolValue(Api.run(Api.post));
		}

		@Override
		public boolean unfavoritePost(int postId) throws ApiException {
			Api.post.setUri(beforeTimeline(ApiWeiba.POST_UNFAVORITE));
			Api.post.append("id", postId);
			return getBoolValue(Api.run(Api.post));
		}

		@Override
		public boolean replyComment(CommentPost cPost) throws ApiException {
			Api.post.setUri(beforeTimeline(ApiWeiba.REPLY_COMMENT));
			Api.post.append("id", cPost.getPostId());
			Api.post.append("content", cPost.getContent());
			return getBoolValue(Api.run(Api.post));
		}

		@Override
		public boolean deleteComment() throws ApiException {
			return false;
		}

		@Override
		public List<SociaxItem> followingPosts() throws ApiException {
			Api.get.setUri(beforeTimeline(ApiWeiba.FOLLOWING_POSTS));
			return getPostsList(Api.run(Api.get));
		}

		@Override
		public List<SociaxItem> followingPostsHeader(int page, int count)
				throws ApiException {
			Api.get.setUri(beforeTimeline(ApiWeiba.FOLLOWING_POSTS));
			Api.get.append("page", page);
			Api.get.append("count", count);
			return getPostsList(Api.run(Api.get));
		}

		@Override
		public List<SociaxItem> followingPostsFooter(int page, int count)
				throws ApiException {
			Api.get.setUri(beforeTimeline(ApiWeiba.FOLLOWING_POSTS));
			Api.get.append("page", page);
			Api.get.append("count", count);
			return getPostsList(Api.run(Api.get));
		}

		@Override
		public List<SociaxItem> posteds(int uid) throws ApiException {
			Api.get.setUri(beforeTimeline(ApiWeiba.POSTEDS));
			return getPostsList(Api.run(Api.get));
		}

		@Override
		public List<SociaxItem> postedsHeader(int page, int count)
				throws ApiException {
			Api.get.setUri(beforeTimeline(ApiWeiba.POSTEDS));
			Api.get.append("page", page);
			Api.get.append("count", count);
			return getPostsList(Api.run(Api.get));
		}

		@Override
		public List<SociaxItem> postedsFooter(int page, int count)
				throws ApiException {
			Api.get.setUri(beforeTimeline(ApiWeiba.POSTEDS));
			Api.get.append("page", page);
			Api.get.append("count", count);
			return getPostsList(Api.run(Api.get));
		}

		@Override
		public List<SociaxItem> commenteds(int uid) throws ApiException {
			Api.get.setUri(beforeTimeline(ApiWeiba.COMMENTEDS));
			return getPostsList(Api.run(Api.get));
		}

		@Override
		public List<SociaxItem> commentedsHeader(int page, int count)
				throws ApiException {
			Api.get.setUri(beforeTimeline(ApiWeiba.COMMENTEDS));
			Api.get.append("page", page);
			Api.get.append("count", count);
			return getPostsList(Api.run(Api.get));
		}

		@Override
		public List<SociaxItem> commentedsFooter(int page, int count)
				throws ApiException {
			Api.get.setUri(beforeTimeline(ApiWeiba.COMMENTEDS));
			Api.get.append("page", page);
			Api.get.append("count", count);
			return getPostsList(Api.run(Api.get));
		}

		@Override
		public List<SociaxItem> favoritePostsList(int uid) throws ApiException {
			Api.get.setUri(beforeTimeline(ApiWeiba.FAVORITE_POSTS));
			return getPostsList(Api.run(Api.get));
		}

		@Override
		public List<SociaxItem> favoritePostsListHeader(int page, int count)
				throws ApiException {
			Api.get.setUri(beforeTimeline(ApiWeiba.FAVORITE_POSTS));
			Api.get.append("page", page);
			Api.get.append("count", count);
			return getPostsList(Api.run(Api.get));
		}

		@Override
		public List<SociaxItem> favoritePostsListFooter(int page, int count)
				throws ApiException {
			Api.get.setUri(beforeTimeline(ApiWeiba.FAVORITE_POSTS));
			Api.get.append("page", page);
			Api.get.append("count", count);
			return getPostsList(Api.run(Api.get));
		}

		@Override
		public List<SociaxItem> searchWeiba(String key) throws ApiException {
			Api.get.setUri(beforeTimeline(ApiWeiba.SEARCH_WEIBA));
			Api.get.append("keyword", key);
			return getWeibaList(Api.run(Api.get));
		}

		@Override
		public List<SociaxItem> searchWeibaHeader(String key, int page,
				int count) throws ApiException {
			Api.get.setUri(beforeTimeline(ApiWeiba.SEARCH_WEIBA));
			Api.get.append("keyword", key);
			Api.get.append("page", page);
			Api.get.append("count", count);
			return getWeibaList(Api.run(Api.get));
		}

		@Override
		public List<SociaxItem> searchWeibaFooter(String key, int page,
				int count) throws ApiException {
			Api.get.setUri(beforeTimeline(ApiWeiba.SEARCH_WEIBA));
			Api.get.append("keyword", key);
			Api.get.append("page", page);
			Api.get.append("count", count);
			return getWeibaList(Api.run(Api.get));
		}

		@Override
		public List<SociaxItem> searchPost(String key) throws ApiException {
			Api.get.setUri(beforeTimeline(ApiWeiba.SEARCH_POST));
			Api.get.append("keyword", key);
			return getPostsList(Api.run(Api.get));
		}

		@Override
		public List<SociaxItem> searchPostHeader(String key, int page, int count)
				throws ApiException {
			Api.get.setUri(beforeTimeline(ApiWeiba.SEARCH_POST));
			Api.get.append("keyword", key);
			Api.get.append("page", page);
			Api.get.append("count", count);
			return getPostsList(Api.run(Api.get));
		}

		@Override
		public List<SociaxItem> searchPostFooter(String key, int page, int count)
				throws ApiException {
			Api.get.setUri(beforeTimeline(ApiWeiba.SEARCH_POST));
			Api.get.append("keyword", key);
			Api.get.append("page", page);
			Api.get.append("count", count);
			return getPostsList(Api.run(Api.get));
		}

	}

	/**
	 * 频道接口实现类
	 */
	public static final class ChannelApi implements ApiChannel {

		@Override
		public ListData<SociaxItem> getAllChannel() throws ApiException {
			Api.get.setUri(Api.createUrlBuild(ApiChannel.MOD_NAME,
					ApiChannel.GET_ALL_CHANNEL));
			return getChannelList(Api.run(Api.get));
		}

		@Override
		public ListData<SociaxItem> getChannelFeed(int channelId)
				throws ApiException {
			Api.get.setUri(Api.createUrlBuild(ApiChannel.MOD_NAME,
					ApiChannel.GET_CHANNEL_FEED));
			Api.get.append("category_id", channelId);
			return getChannelFeedList(Api.run(Api.get));
		}

		@Override
		public ListData<SociaxItem> getChannelHeaderFeed(Weibo weibo,
				int channelId) throws ApiException {
			Api.get.setUri(Api.createUrlBuild(ApiChannel.MOD_NAME,
					ApiChannel.GET_CHANNEL_FEED));
			Api.get.append("category_id", channelId);
			Api.get.append("max_id", weibo.getWeiboId());
			return getChannelFeedList(Api.run(Api.get));
		}

		@Override
		public ListData<SociaxItem> getChannelFooterFeed(Weibo weibo,
				int channelId) throws ApiException {
			Api.get.setUri(Api.createUrlBuild(ApiChannel.MOD_NAME,
					ApiChannel.GET_CHANNEL_FEED));
			Api.get.append("category_id", channelId);
			Api.get.append("since_id", weibo.getWeiboId());
			return getChannelFeedList(Api.run(Api.get));
		}

		@SuppressWarnings("finally")
		private ListData<SociaxItem> getChannelFeedList(Object result) {
			ListData<SociaxItem> list = null;
			try {
				if (!result.equals("null")) {
					JSONArray data = new JSONArray((String) result);
					int length = data.length();
					list = new ListData<SociaxItem>();
					for (int i = 0; i < length; i++) {
						JSONObject jsonObject = data.getJSONObject(i);
						Weibo w = new Weibo(jsonObject);
						list.add(w);
					}
				}
			} catch (Exception e) {
				Log.d(TAG, e.toString());
			} finally {
				return list;
			}
		}

		@SuppressWarnings("finally")
		private ListData<SociaxItem> getChannelList(Object result) {
			ListData<SociaxItem> list = null;
			try {
				if (!result.equals("null")) {

					JSONArray data = new JSONArray((String) result);
					int length = data.length();
					list = new ListData<SociaxItem>();
					for (int i = 0; i < length; i++) {
						JSONObject jsonObject = data.getJSONObject(i);
						Channel c = new Channel(jsonObject);
						list.add(c);
					}

				}
			} catch (Exception e) {
				try {
					list = new ListData<SociaxItem>();
					JSONObject itemData = new JSONObject(result.toString());
					for (Iterator iterator = itemData.keys(); iterator
							.hasNext();) {
						String key = (String) iterator.next();
						JSONObject jsonObject = itemData.getJSONObject(key);
						Channel c = new Channel(jsonObject);
						list.add(c);
					}
				} catch (Exception e2) {
					Log.d(TAG, e2.toString());
				}
				Log.d(TAG, "get channel error ... ");
			}
			return list;
		}
	}

	/**
	 * 群组接口实现类
	 */
	public static final class GroupApi implements ApiGroup {

		private Uri.Builder baseUrl(String act) {
			return Api.createUrlBuild(ApiGroup.MOD_NAME, act);
		}

		@Override
		public ListData<SociaxItem> showStatuesType() throws ApiException {
			Get get = new Get();
			get.setUri(baseUrl(ApiGroup.SHOW_STATUSES_TYPE));
			Object result = Api.run(get);
			ListData<SociaxItem> list = null;
			try {
				list = new ListData<SociaxItem>();
				JSONObject itemData = new JSONObject(result.toString());

				for (Iterator iterator = itemData.keys(); iterator.hasNext();) {
					String key = (String) iterator.next();
					String temp = itemData.getString(key);
					StringItem item = new StringItem(Integer.valueOf(key), temp);
					list.add(item);
				}

			} catch (JSONException e) {
				Log.d(TAG,
						"get group show statues type error wm " + e.toString());
			}
			return list;
		}

		private ListData<SociaxItem> getStatueList(Object result,
				ListData.DataType type) {
			ListData<SociaxItem> list = null;
			try {
				JSONArray data = new JSONArray((String) result);
				int length = data.length();
				list = new ListData<SociaxItem>();
				for (int i = 0; i < length; i++) {
					try {
						JSONObject itemData = data.getJSONObject(i);
						SociaxItem weiboData = Api
								.getSociaxItem(type, itemData);
						list.add(weiboData);
					} catch (Exception e) {
						Log.e(TAG, "json itme  error wm :" + e.toString());
					}
					continue;
				}
			} catch (Exception e) {
				Log.e(TAG, "json result error wm :" + e.toString());
			}
			return list;
		}

		@Override
		public ListData<SociaxItem> showStatuses(int count, int type)
				throws ApiException {
			Get get = new Get();
			get.setUri(baseUrl(ApiGroup.SHOW_STATUSES));
			get.append("count", count);
			get.append("gid", 106);
			get.append("type", type);

			return getStatueList(Api.run(get), ListData.DataType.WEIBO);
		}

		@Override
		public ListData<SociaxItem> showStatusesHeader(Weibo item, int count,
				int type) throws ApiException {
			Get get = new Get();
			get.setUri(baseUrl(ApiGroup.SHOW_STATUSES));
			get.append("count", count);
			get.append("gid", 106);
			get.append("type", type);
			get.append("since_id", item.getWeiboId());

			return getStatueList(Api.run(get), ListData.DataType.WEIBO);
		}

		@Override
		public ListData<SociaxItem> showStatusesFooter(Weibo item, int count,
				int type) throws ApiException {
			Get get = new Get();
			get.setUri(baseUrl(ApiGroup.SHOW_STATUSES));
			get.append("count", count);
			get.append("gid", 106);
			get.append("type", type);
			get.append("max_id", item.getWeiboId());

			return getStatueList(Api.run(get), ListData.DataType.WEIBO);
		}

		@Override
		public ListData<SociaxItem> showAtmeStatuses(int count)
				throws ApiException {
			Api.get.setUri(baseUrl(ApiGroup.SHOW_ATME_STATUSES));
			Api.get.append("count", count);
			Api.get.append("gid", 106);
			return getStatueList(Api.run(Api.get), ListData.DataType.WEIBO);
		}

		@Override
		public ListData<SociaxItem> showAtmeStatusesHeader(Weibo item, int count)
				throws ApiException {
			Api.get.setUri(baseUrl(ApiGroup.SHOW_ATME_STATUSES));
			Api.get.append("count", count);
			Api.get.append("gid", 106);
			Api.get.append("since_id", item.getWeiboId());

			return getStatueList(Api.run(Api.get), ListData.DataType.WEIBO);
		}

		@Override
		public ListData<SociaxItem> showAtmeStatusesFooter(Weibo item, int count)
				throws ApiException {
			Api.get.setUri(baseUrl(ApiGroup.SHOW_ATME_STATUSES));
			Api.get.append("count", count);
			Api.get.append("gid", 106);
			Api.get.append("max_id", item.getWeiboId());
			return getStatueList(Api.run(Api.get), ListData.DataType.WEIBO);
		}

		@Override
		public ListData<SociaxItem> showStatusComments(int count)
				throws ApiException {
			Api.get.setUri(baseUrl(ApiGroup.SHOW_STATUS_COMMENTS));
			Api.get.append("count", count);
			Api.get.append("gid", 106);
			Api.get.append("type", "receive");
			return getStatueList(Api.run(Api.get), ListData.DataType.RECEIVE);
		}

		@Override
		public ListData<SociaxItem> showStatusCommentsHeader(
				ReceiveComment item, int count) throws ApiException {
			Api.get.setUri(baseUrl(ApiGroup.SHOW_STATUS_COMMENTS));
			Api.get.append("count", count);
			Api.get.append("gid", 106);
			Api.get.append("type", "receive");
			Api.get.append("since_id", item.getCommentId());

			return getStatueList(Api.run(Api.get), ListData.DataType.RECEIVE);
		}

		@Override
		public ListData<SociaxItem> showStatusCommentsFooter(
				ReceiveComment item, int count) throws ApiException {
			Api.get.setUri(baseUrl(ApiGroup.SHOW_STATUS_COMMENTS));
			Api.get.append("count", count);
			Api.get.append("gid", 106);
			Api.get.append("type", "receive");
			Api.get.append("max_id", item.getCommentId());

			return getStatueList(Api.run(Api.get), ListData.DataType.RECEIVE);
		}

		@Override
		public ListData<SociaxItem> groupMembers(int count) throws ApiException {
			Api.get.setUri(baseUrl(ApiGroup.GROUP_MEMBERS));
			Api.get.append("count", count);
			Api.get.append("gid", 106);

			return getStatueList(Api.run(Api.get), ListData.DataType.USER);
		}

		@Override
		public ListData<SociaxItem> groupMembersHeader(User user, int count)
				throws ApiException {
			Api.get.setUri(baseUrl(ApiGroup.GROUP_MEMBERS));
			Api.get.append("count", count);
			Api.get.append("gid", 106);
			Api.get.append("since_id", user.getUid());

			return getStatueList(Api.run(Api.get), ListData.DataType.USER);
		}

		@Override
		public ListData<SociaxItem> groupMembersFooter(User user, int count)
				throws ApiException {
			Api.get.setUri(baseUrl(ApiGroup.GROUP_MEMBERS));
			Api.get.append("count", count);
			Api.get.append("gid", 106);
			Api.get.append("max_id", user.getUid());

			return getStatueList(Api.run(Api.get), ListData.DataType.USER);
		}

		@Override
		public ListData<SociaxItem> weiboComments(Weibo item, Comment comment,
				int count) throws ApiException {
			Api.get.setUri(baseUrl(ApiGroup.WEIBO_COMMENTS));
			Api.get.append("count", count);
			Api.get.append("gid", 106);
			Api.get.append("id", item.getWeiboId());
			return getStatueList(Api.run(Api.get), ListData.DataType.COMMENT);
		}

		@Override
		public ListData<SociaxItem> weiboCommentsHeader(Weibo item,
				Comment comment, int count) throws ApiException {
			Api.get.setUri(baseUrl(ApiGroup.WEIBO_COMMENTS));
			Api.get.append("count", count);
			Api.get.append("gid", 106);
			Api.get.append("id", item.getWeiboId());
			Api.get.append("since_id", comment.getCommentId());

			return getStatueList(Api.run(Api.get), ListData.DataType.COMMENT);
		}

		@Override
		public ListData<SociaxItem> weiboCommentsFooter(Weibo item,
				Comment comment, int count) throws ApiException {
			Api.get.setUri(baseUrl(ApiGroup.WEIBO_COMMENTS));
			Api.get.append("count", count);
			Api.get.append("gid", 106);
			Api.get.append("id", item.getWeiboId());
			Api.get.append("max_id", comment.getCommentId());

			return getStatueList(Api.run(Api.get), ListData.DataType.COMMENT);
		}

		@Override
		public boolean updateStatus(Weibo weibo) throws ApiException {
			Uri.Builder uri = Api.createUrlBuild(ApiGroup.MOD_NAME,
					ApiGroup.UPDATE_STATUS);

			Api.post.setUri(uri);
			Api.post.append("content", weibo.getContent());
			Api.post.append("gid", "106");
			Api.post.append("from", Weibo.From.ANDROID.ordinal() + "");
			Object result = Api.run(Api.post);
			Api.checkResult(result);
			String data = (String) result;

			Log.d("apiData", "updateResult" + data.toString());
			return Integer.parseInt(data) > 0;
		}

		@Override
		public boolean uploadStatus(Weibo weibo, File file) throws ApiException {
			String result = null;
			try {
				Uri.Builder uri = Api.createUrlBuild(ApiGroup.MOD_NAME,
						ApiGroup.UPLOAD_STATUS);

				FormFile formFile = new FormFile(Compress.compressPic(file),
						file.getName(), "pic", "application/octet-stream");
				Api.post.setUri(uri);
				HashMap<String, String> param = new HashMap<String, String>();
				param.put("content", weibo.getContent());
				param.put("token", Request.getToken());
				param.put("secretToken", Request.getSecretToken());
				param.put("gid", "106");
				param.put("from", Weibo.From.ANDROID.ordinal() + "");
				result = FormPost.post(uri.toString(), param, formFile);
			} catch (Exception e) {
				Log.e(TAG, "group send pic weibo error wm" + e.toString());
			}
			return Integer.parseInt(result) > 0;
		}

		@Override
		public boolean repostStatuses(Weibo weibo, boolean isComment)
				throws ApiException {
			Uri.Builder uri = Api.createUrlBuild(ApiGroup.MOD_NAME,
					ApiGroup.REPOST_STATUSES);

			Api.post.setUri(uri);
			if (weibo.getTranspond().isNullForTranspond()) {
				Api.post.append("id", weibo.getTranspond().getWeiboId() + "");
			} else {
				Api.post.append("id", weibo.getTranspond().getTranspondId()
						+ "");
			}
			Api.post.append("content", weibo.getContent());
			if (isComment) {
				Api.post.append("comment", 1);
			} else {
				Api.post.append("comment", 0);
			}
			Api.post.append("gid", "106");
			Api.post.append("from", Weibo.From.ANDROID.ordinal() + "");
			Object result = Api.run(Api.post);
			Api.checkResult(result);

			return Integer.valueOf((String) result) > 0 ? true : false;
		}

		@Override
		public boolean commentStatuses(Comment comment) throws ApiException {
			Uri.Builder uri = Api.createUrlBuild(ApiGroup.MOD_NAME,
					ApiGroup.COMMENT_STATUSES);
			Api.post.setUri(uri);

			Api.post.append("content", comment.getContent())
					.append("row_id", comment.getStatus().getWeiboId() + "")
					.append("ifShareFeed", comment.getType().ordinal() + "")
					.append("gid", "106")
					.append("from", Weibo.From.ANDROID.ordinal() + "");

			if (!comment.isNullForReplyComment()) {
				int replyCommentId = comment.getReplyComment().getCommentId();
				Api.post.append("to_comment_id", replyCommentId + "");
			}

			Object result = Api.run(Api.post);
			Api.checkResult(result);
			String data = (String) result;

			int resultConde = 0;

			try {
				resultConde = Integer.valueOf(data);
			} catch (Exception e) {
				Log.d(APP_TAG, "发送评论出错  wm " + e.toString());
				throw new ApiException("服务端出错");
			}
			return resultConde >= 1 ? true : false;
		}
	}

	public static final class CheckinApi implements ApiCheckin {

		private Uri.Builder baseUrl(String act) {
			return Api.createUrlBuild(ApiCheckin.MOD_NAME, act);
		}

		@Override
		public Object checkIn() throws ApiException {
			Api.get.setUri(baseUrl(ApiCheckin.CHECKIN));
			Object result = Api.run(Api.get);
			return result;
		}

		@Override
		public Object getCheckInfo() throws ApiException {
			Api.get.setUri(baseUrl(ApiCheckin.GET_CHECK_INFO));
			Object result = Api.run(Api.get);
			return result;
		}

	}

	public static final class UpgradeApi implements ApiUpgrade {

		private Uri.Builder beforeTimeline(String act) {
			return Api.createUrlBuild(ApiUpgrade.MOD_NAME, act);
		}

		@Override
		public VersionInfo getVersion() throws ApiException {
			Get get = new Get();
			get.setUri(beforeTimeline(ApiUpgrade.GET_VERSION));
			Object result = Api.run(get);
			VersionInfo vInfo = null;
			try {
				vInfo = new VersionInfo(new JSONObject((String) result));
			} catch (JSONException e) {
				e.printStackTrace();
				throw new ApiException("数据解析错误");
			}
			return vInfo;
		}
	}

	/**
	 * 系统通知
	 */
	public static final class NotifytionApi implements ApiNotifytion {

		@Override
		public ListData<SociaxItem> getNotifyByCount(int uid)
				throws ApiException {
			beforeTimeline(ApiNotifytion.GET_NOTIFY_BY_COUNT);
			Object result = Api.run(Api.get);
			Api.checkResult(result);

			ListData<SociaxItem> list = null;
			try {
				if (!result.equals("null")) {
					JSONArray data = new JSONArray((String) result);
					int length = data.length();
					list = new ListData<SociaxItem>();
					for (int i = 0; i < length; i++) {
						JSONObject jsonObject = data.getJSONObject(i);
						NotifyItem notifyItem = new NotifyItem(jsonObject);
						/*
						 * if(notifyItem.getCount() < 1){ continue; }
						 */
						list.add(notifyItem);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				Log.d(TAG, e.toString());
			}
			return list;
		}

		@Override
		public int getMessageCount() throws ApiException {
			int messageCount = 0;
			beforeTimeline(ApiNotifytion.GET_MESSAGE_COUNT);
			Object result = Api.run(Api.get);

			if (Api.Status.SUCCESS == Api.checkResult(result)) {
				if (!result.equals("null") && !result.equals("ERROR")) {
					try {
						messageCount = new Integer((String) result);
					} catch (Exception e) {
						Log.d(TAG, "getMessage Count error " + e.toString());
					}
				}
			}
			return messageCount;
		}

		private void beforeTimeline(String act) {
			Uri.Builder uri = Api.createUrlBuild(ApiNotifytion.MOD_NAME, act);
			Api.get.setUri(uri);
		}

		@Override
		public ListData<SociaxItem> getSystemNotify(int uid)
				throws ApiException {
			beforeTimeline(ApiNotifytion.GET_SYSTEM_NOTIFY);
			Object result = Api.run(Api.get);
			Api.checkResult(result);

			ListData<SociaxItem> list = null;
			try {
				if (!result.equals("null")) {
					JSONArray data = new JSONArray((String) result);
					int length = data.length();
					list = new ListData<SociaxItem>();
					for (int i = 0; i < length; i++) {
						JSONObject jsonObject = data.getJSONObject(i);
						SystemNotify systemNotify = new SystemNotify(jsonObject);
						/*
						 * if(notifyItem.getCount() < 1){ continue; }
						 */
						list.add(systemNotify);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				Log.d(TAG, e.toString());
			}
			return list;
		}

		@Override
		public void setMessageRead(String type) throws ApiException {
			beforeTimeline(ApiNotifytion.SET_MESSAGE_READ);
			Api.get.append("type", type);
			Object result = Api.run(Api.get);
			Api.checkResult(result);
		}

		@Override
		public void setNotifyRead(String type) throws ApiException {
			beforeTimeline(ApiNotifytion.SET_NOTIFY_READ);
			Api.get.append("type", type);
			Object result = Api.run(Api.get);
			Api.checkResult(result);
		}
	}

	// //////////////////////////************************//////////////////////

	public static String getHost() {
		return mHost;
	}

	public static String getPath() {
		return mPath;
	}

	public static Context getContext() {
		return mContext;
	}

	public static void setContext(Context context) {
		Api.mContext = context;
	}

	private static void setHost(String host) {
		Api.mHost = host;
	}

	private static void setPath(String path) {
		Api.mPath = path;
	}

	public static String getUrl() {
		return url;
	}

	public static void setUrl(String url) {
		Api.url = url;
	}

}
