package com.zhiyicx.zycx.util;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;
import qcjlibrary.util.L;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.common.SocializeConstants;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMVideo;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.RenrenSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.TencentWBSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;
import com.zhiyicx.zycx.R;
import com.zhiyicx.zycx.config.MyConfig;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 *
 */

public class Utils {

	public static String getCurrentTime(String format) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
		String currentTime = sdf.format(date);
		return currentTime;
	}

	public static String getCurrentTime() {
		return getCurrentTime("yyyy-MM-dd  HH:mm:ss");
	}

	public static void showToast(Context context, String txt) {
		Toast.makeText(context, txt, Toast.LENGTH_LONG).show();
	}

	public static String getTokenString(Context context) {
		String token = null;
		PreferenceUtil preferenceUtil = PreferenceUtil.getInstance(context);
//		token = "&oauth_token=" + "18e22c9690b5e01ce224a58f401eb995";
//		token += "&oauth_token_secret=" + "be826a6243b7f9c0800ac82ce692c2f7";
		 token = "&oauth_token=" + preferenceUtil.getString("oauth_token",
		 "");
		 token += "&oauth_token_secret=" +
		 preferenceUtil.getString("oauth_token_secret", "");
		return token;
	}

	public static int getUid(Context context) {
		PreferenceUtil preferenceUtil = PreferenceUtil.getInstance(context);
		return preferenceUtil.getInt("uid", -1);
	}

	public static String getUTF8String(String src) {
		String txt = null;
		try {
			txt = new String(URLEncoder.encode(src, "UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return txt;
	}

	public static void shareText(Activity context, UMSocialService controller,
			String txt, String url) {
		SocializeConstants.APPKEY = "54bf4a76fd98c5c53f000816";
		controller.getConfig().setPlatforms(SHARE_MEDIA.RENREN,
				SHARE_MEDIA.TENCENT, SHARE_MEDIA.SINA, SHARE_MEDIA.QQ,
				SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE);
		RenrenSsoHandler renrenSsoHandler = new RenrenSsoHandler(context,
				"264951", "721ad15defe84324aa9147ba80ec07e7",
				"9f2a49b054064bfc9609f90ecfb019b0");
		controller.getConfig().setSsoHandler(renrenSsoHandler);
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(context, "1104001628",
				"T8KvqK3e3EQOyBgA");
		qqSsoHandler.addToSocialSDK();
		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(context,
				"1104001628", "T8KvqK3e3EQOyBgA");
		qZoneSsoHandler.addToSocialSDK();

		// wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
		String appID = "wx6282db259f8105a2";
		String appSecret = "8f7d8fe5c56b8e54fda6234aa2ea0f8b";
		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(context, appID, appSecret);
		wxHandler.addToSocialSDK();
		// 支持微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(context, appID, appSecret);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();

		// 设置微信好友分享内容
		WeiXinShareContent weixinContent = new WeiXinShareContent();
		// 设置分享文字
		weixinContent.setShareContent(txt);
		// 设置title
		weixinContent.setTitle(txt);
		// 设置分享内容跳转URL
		weixinContent.setTargetUrl(url);
		// 设置分享图片
		weixinContent
				.setShareImage(new UMImage(context, R.drawable.ic_launcher));
		controller.setShareMedia(weixinContent);

		// 设置微信朋友圈分享内容
		CircleShareContent circleMedia = new CircleShareContent();
		circleMedia.setShareContent(txt);
		// 设置朋友圈title
		circleMedia.setTitle(txt);
		circleMedia.setShareImage(new UMImage(context, R.drawable.ic_launcher));
		circleMedia.setTargetUrl(url);
		controller.setShareMedia(circleMedia);

		QQShareContent qqShareContent = new QQShareContent();
		qqShareContent.setShareContent(txt + url);
		qqShareContent.setTitle(txt);
		qqShareContent.setShareImage(new UMImage(context,
				R.drawable.ic_launcher));
		qqShareContent.setTargetUrl(url);
		controller.setShareMedia(qqShareContent);

		QZoneShareContent qzone = new QZoneShareContent();
		qzone.setShareContent(txt + url);
		qzone.setTargetUrl(url);
		qzone.setTitle(txt);
		qzone.setShareImage(new UMImage(context, R.drawable.ic_launcher));
		controller.setShareMedia(qzone);

		controller.getConfig().setSsoHandler(new SinaSsoHandler());
		controller.getConfig().setSsoHandler(new TencentWBSsoHandler());
		controller.setAppWebSite(SHARE_MEDIA.RENREN, MyConfig.HOST);
		controller.setShareContent(txt + url);
		controller.setShareMedia(null);
		controller.openShare(context, false);
	}

	public static void shareVidoe(Activity context, UMSocialService controller,
			String txt, String url) {
		SocializeConstants.APPKEY = "54bf4a76fd98c5c53f000816";
		controller.getConfig().setPlatforms(SHARE_MEDIA.RENREN,
				SHARE_MEDIA.TENCENT, SHARE_MEDIA.SINA, SHARE_MEDIA.QQ,
				SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE);
		RenrenSsoHandler renrenSsoHandler = new RenrenSsoHandler(context,
				"264951", "721ad15defe84324aa9147ba80ec07e7",
				"9f2a49b054064bfc9609f90ecfb019b0");
		controller.getConfig().setSsoHandler(renrenSsoHandler);
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(context, "1104001628",
				"T8KvqK3e3EQOyBgA");
		qqSsoHandler.addToSocialSDK();
		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(context,
				"1104001628", "T8KvqK3e3EQOyBgA");
		qZoneSsoHandler.addToSocialSDK();

		// wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
		String appID = "wx6282db259f8105a2";
		String appSecret = "8f7d8fe5c56b8e54fda6234aa2ea0f8b";
		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(context, appID, appSecret);
		wxHandler.addToSocialSDK();
		// 支持微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(context, appID, appSecret);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();

		// 设置微信好友分享内容
		WeiXinShareContent weixinContent = new WeiXinShareContent();
		// 设置分享文字
		weixinContent.setShareContent("轻课堂视频《" + txt + "》分享。");
		// 设置title
		weixinContent.setTitle("轻课堂视频《" + txt + "》分享。");
		// 设置分享内容跳转URL
		weixinContent.setTargetUrl(url);
		// 设置分享图片
		weixinContent
				.setShareImage(new UMImage(context, R.drawable.ic_launcher));
		controller.setShareMedia(weixinContent);

		// 设置微信朋友圈分享内容
		CircleShareContent circleMedia = new CircleShareContent();
		circleMedia.setShareContent("轻课堂视频《" + txt + "》分享。");
		// 设置朋友圈title
		circleMedia.setTitle("轻课堂视频《" + txt + "》分享。");
		circleMedia.setShareImage(new UMImage(context, R.drawable.ic_launcher));
		circleMedia.setTargetUrl(url);
		controller.setShareMedia(circleMedia);

		QQShareContent qqShareContent = new QQShareContent();
		qqShareContent.setShareContent("轻课堂视频《" + txt + "》分享。");
		qqShareContent.setTitle("轻课堂分享!");
		qqShareContent.setShareImage(new UMImage(context,
				R.drawable.ic_launcher));
		qqShareContent.setTargetUrl(url);
		controller.setShareMedia(qqShareContent);

		QZoneShareContent qzone = new QZoneShareContent();
		qzone.setShareContent("轻课堂视频《" + txt + "》分享。");
		qzone.setTargetUrl(url);
		qzone.setTitle("轻课堂分享!");
		qzone.setShareImage(new UMImage(context, R.drawable.ic_launcher));
		controller.setShareMedia(qzone);

		controller.getConfig().setSsoHandler(new SinaSsoHandler());
		controller.getConfig().setSsoHandler(new TencentWBSsoHandler());
		controller.setAppWebSite(SHARE_MEDIA.RENREN, MyConfig.HOST);
		// controller.setShareContent(txt);
		// 设置分享视频
		UMVideo umVideo = new UMVideo(url);
		// 设置视频缩略图
		// umVideo.setThumb("http://www.umeng.com/images/pic/banner_module_social.png");
		umVideo.setTitle("轻课堂分享!");
		controller.setShareMedia(umVideo);
		controller.setShareContent("轻课堂视频《" + txt + "》分享。");
		controller.openShare(context, false);

	}

}
