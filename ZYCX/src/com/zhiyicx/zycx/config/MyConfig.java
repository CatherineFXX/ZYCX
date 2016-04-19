package com.zhiyicx.zycx.config;

/**
 * 配置类
 *
 * @author Mr . H
 *
 */
public class MyConfig {

//	public static String HOST = "http://demo-qingko.zhiyicx.com";
	public static String HOST = "http://www.qingko.com";
	// HTTP请求URL
	public static String LOGIN_URL = HOST
			+ "/index.php?app=api&mod=Oauth&act=authorize&";
	public static String REGISTER_URL = HOST
			+ "/index.php?app=api&mod=Oauth&act=register&";
	public static String GETCODE_URL = HOST
			+ "/index.php?app=api&mod=SendMsg&act=sendMsg&";
	public static String PICTURECODE_URL = HOST
			+ "/index.php?app=api&mod=SendMsg&act=imageVerify&";
	public static String INTERFACE_URL = HOST
			+ "/index.php?app=api&mod=Oauth&act=getOpen";
	public static String FORGETPHONE_URL = HOST
			+ "/index.php?app=api&mod=SendMsg&act=sendMsg";
	public static String FORGETMAIL_URL = HOST
			+ "/index.php?app=api&mod=SendMsg&act=sendMail";
	public static String FORGET_BTN_NEXT_URL = HOST
			+ "/index.php?app=api&mod=Oauth&act=verifyCode";
	public static String FORGET_BTN_SUREPW_URL = HOST
			+ "/index.php?app=api&mod=Oauth&act=resetPwd";
	public static String VERIFY_REG_URL = HOST
			+ "/index.php?app=api&mod=Oauth&act=verifyReg";
	public static String GET_PROTOCOL = HOST
			+ "/index.php?app=api&mod=Oauth&act=getProtocol";

	// 资讯
	public static String ZIXUN_LIST_URL = HOST
			+ "/index.php?app=3g&mod=News&act=index";
	public static String ZIXUN_GET_URL = HOST
			+ "/index.php?app=3g&mod=News&act=getUrl";
	public static String ZIXUN_CONTENT_URL = HOST
			+ "/index.php?app=3g&mod=News&act=show";
	public static String ZIXUN_COMMENT_URL = HOST
			+ "/index.php?app=3g&mod=News&act=addcomment";
	public static String ZIXUN_COLLECT_URL = HOST
			+ "/index.php?app=3g&mod=News&act=collection";

	// 轻课堂
	public static String QCLASS_LIST_URL = HOST
			+ "/index.php?app=api&mod=Course&act=index";
	public static String QCLASS_DETAILs_URL = HOST
			+ "/index.php?app=api&mod=Course&act=getDetail";
	public static String QCLASS_CMTLIST_URL = HOST
			+ "/index.php?app=api&mod=Course&act=getComment";
	public static String QCLASS_ADDCMT_URL = HOST
			+ "/index.php?app=api&mod=Course&act=addComment";
	public static String QCLASS_DEL_RECORD_URL = HOST
			+ "/index.php?app=api&mod=Course&act=delRecord";

	// 期刊
	public static String QIKAN_LIST_URL = HOST
			+ "/index.php?app=3g&mod=Periodical&act=index";
	public static String QIKAN_DETAILS_URL = HOST
			+ "/index.php?app=3g&mod=Periodical&act=getDetail";

	// 问答
	public static String QUESTION_LIST_URL = HOST
			+ "/index.php?app=api&mod=Ask&act=index";
	public static String QUESTION_DETAILS_URL = HOST
			+ "/index.php?app=api&mod=Ask&act=answer";
	public static String QUESTION_ANSWER_URL = HOST
			+ "/index.php?app=api&mod=Ask&act=saveAnswer";
	public static String QUESTION_TOPIC_URL = HOST
			+ "/index.php?app=api&mod=Ask&act=getTopic";
	public static String QUESTION_ASK_URL = HOST
			+ "/index.php?app=api&mod=Ask&act=addQuestion";
	public static String QUESTION_BAST_URL = HOST
			+ "/index.php?app=api&mod=Ask&act=setBestAnswer";
	public static String QUESTION_SEARCH_URL = HOST
			+ "/index.php?app=api&mod=Ask&act=search";
	public static String QUESTION_TOPLIST_URL = HOST
			+ "/index.php?app=api&mod=Ask&act=topicQuestion";

	// 微博
	public static String WEB_NEW_URL = HOST
			+ "/index.php?app=api&mod=WeiboStatuses&act=public_timeline&count=5";
	public static String WEB_ZAN_URL = HOST
			+ "/index.php?app=api&mod=WeiboStatuses&act=add_digg";
	public static String WEB_DELZAN_URL = HOST
			+ "/index.php?app=api&mod=WeiboStatuses&act=del_digg";

	// 设置权限范围
	public static final String SCROPE = "read_user_blog read_user_photo read_user_status read_user_album "
			+ "read_user_comment read_user_share publish_blog publish_share "
			+ "send_notification photo_upload status_update create_album "
			+ "publish_comment publish_feed";

	public static final int weiboLenght = 200;
}
