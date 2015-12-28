package qcjlibrary.model;

import qcjlibrary.model.base.Model;

/**
 * author：qiuchunjia time：下午3:08:12 类描述：这个类是实现
 *
 */

public class ModelCaseRecordResult extends Model {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// "time": "12月23日 13:56",
	// "title": "更新了个人信息",
	// "content":
	// "姓名：赢泗/n性别：男/n年龄：58/n婚姻状况：未婚/n民族：汉族/n职业：皇帝/n文化程度：其他/n保险形式：自费/n籍贯：北京市 北京市
	// 朝
	private String time;
	private String title;
	private String content;

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
