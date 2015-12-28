package qcjlibrary.model;

import org.json.JSONException;
import org.json.JSONObject;

import qcjlibrary.model.base.Model;

/**
 * author：qiuchunjia time：下午3:08:12 类描述：这个类是实现
 *
 */

public class ModelCaseRecord extends Model {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// "id": "13",
	// "content": "更新了个人信息",
	// "type": "0",
	// "ctime": "12月24日 10:07"
	private String id;
	private String content;
	private String type;
	private String ctime;

	public ModelCaseRecord() {
	}

	public ModelCaseRecord(JSONObject data) {
		try {
			if (data.has("id")) {
				setId(data.getString("id"));
			}
			if (data.has("content")) {

				setContent(data.getString("content"));
			}
			if (data.has("type")) {

				setType(data.getString("type"));
			}
			if (data.has("ctime")) {

				setCtime(data.getString("ctime"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCtime() {
		return ctime;
	}

	public void setCtime(String ctime) {
		this.ctime = ctime;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
