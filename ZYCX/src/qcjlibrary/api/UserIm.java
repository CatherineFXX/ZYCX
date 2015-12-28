package qcjlibrary.api;

import java.io.File;

import qcjlibrary.model.ModelMeAddress;
import qcjlibrary.model.ModelRequestMyAsk;
import qcjlibrary.model.ModelUser;

import com.loopj.android.http.RequestParams;

/**
 * author：qiuchunjia time：下午2:57:11
 * <p/>
 * 这个接口实现用户的相关消息，比如说登陆注册啊， 等等反正跟用户有关的都用这个接口
 */

public interface UserIm {
    // 接口需要的操作参数
    public static final String PERSONAGE = "Personage";
    public static final String EDITUSERDATA = "edituserdata";
    public static final String AREALIST = "arealist";
    public static final String INDEX = "index"; // 获取用户的信息
    public static final String CANCERLIST = "cancerlist"; // 癌种列表
    public static final String EDITAVATAR = "editavatar";
    public static final String MYQUESTION = "myQuestion"; //我的专家提问
    public static final String EVAQUESTION = "evaQuestion"; //对专家提问进行评论
    // 接口需要的操作参数
    public static final String SEX = "sex";
    public static final String INTRO = "intro";
    public static final String CANCER = "cancer";
    public static final String BIRTHDAY = "birthday";
    public static final String LOCATION = "location";
    public static final String CITY_IDS = "city_ids";
    public static final String UNAME = "uname";
    public static final String AREA_ID = "area_id";
    public static final String QID = "qid";
    public static final String EVALUATE = "evaluate";

    /**
     * 个人中心-个人资料修改
     *
     * @param user
     * @return
     */
    public RequestParams edituserdata(ModelUser user);

    /**
     * @param address
     * @return
     */
    public RequestParams arealist(ModelMeAddress address);

    /**
     * 获取用户信息
     *
     * @return
     */
    public RequestParams index();

    /**
     * 癌种列表
     *
     * @return
     */
    public RequestParams cancerlist();

    /**
     * 个人资料-上传头像
     *
     * @param file
     * @return
     */
    public RequestParams editavatar(File file);

    public RequestParams myQuestion(ModelRequestMyAsk myAsk);

    /**
     * 对专家回答进行评论
     *
     * @param myAsk
     * @return
     */
    public RequestParams evaQuestion(ModelRequestMyAsk myAsk);
}
