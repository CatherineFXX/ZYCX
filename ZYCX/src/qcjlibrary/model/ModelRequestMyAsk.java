package qcjlibrary.model;

import org.json.JSONException;
import org.json.JSONObject;

import qcjlibrary.model.base.Model;

/**
 * author：qiuchunjia time：
 * <p/>
 * <p/>
 * 下午5:29:20 类描述：这个类是实现我的专家提问
 */

public class ModelRequestMyAsk extends Model {

    /**
     * "question_id": "100383",
     * "question_content": "得了很严重的脑病，在线等。急!急!急!",
     * "question_detail": "有人说，希望每个人死后尸体都能自动变成一本书，书的内容就是死者的生平。这样一来，有人变成菜谱，有
     * "categoryname": "原发性肝癌",
     * "time": "11月27日 10:35",
     * "evaluate": "2",
     * "answercontent": ""
     */
    private static final long serialVersionUID = 1L;

    private String question_id;
    private String question_detail;
    private String question_content;
    private String categoryname;
    private String time;
    private String evaluate;
    private String answercontent;

    public ModelRequestMyAsk(JSONObject data) {
        try {
            if (data.has("question_id")) {

                setQuestion_id(data.getString("question_id"));
            }
            if (data.has("question_content")) {

                setQuestion_content(data.getString("question_content"));
            }
            if (data.has("question_detail")) {

                setQuestion_detail(data.getString("question_detail"));
            }
            if (data.has("categoryname")) {

                setCategoryname(data.getString("categoryname"));
            }
            if (data.has("time")) {

                setTime(data.getString("time"));
            }
            if (data.has("evaluate")) {

                setEvaluate(data.getString("evaluate"));
            }
            if (data.has("answercontent")) {

                setAnswercontent(data.getString("answercontent"));
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public ModelRequestMyAsk() {

    }

    public String getQuestion_detail() {
        return question_detail;
    }

    public void setQuestion_detail(String question_detail) {
        this.question_detail = question_detail;
    }

    public String getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getQuestion_content() {
        return question_content;
    }

    public void setQuestion_content(String question_content) {
        this.question_content = question_content;
    }

    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAnswercontent() {
        return answercontent;
    }

    public void setAnswercontent(String answercontent) {
        this.answercontent = answercontent;
    }

    public String getEvaluate() {
        return evaluate;
    }

    public void setEvaluate(String evaluate) {
        this.evaluate = evaluate;
    }
}
