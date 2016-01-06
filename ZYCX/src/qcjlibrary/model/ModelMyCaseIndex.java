package qcjlibrary.model;

import qcjlibrary.model.base.Model;

/**
 * author：qiuchunjia time：上午10:41:27 类描述：这个类是实现我的病例
 *
 */

public class ModelMyCaseIndex extends Model {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ModelAddCase info;
	private ModelAddHistoryCase history;
	private ModelAddNowCase present;

	public ModelAddCase getInfo() {
		return info;
	}

	public void setInfo(ModelAddCase info) {
		this.info = info;
	}

	public ModelAddHistoryCase getHistory() {
		return history;
	}

	public void setHistory(ModelAddHistoryCase history) {
		this.history = history;
	}


	public ModelAddNowCase getPresent() {
		return present;
	}

	public void setPresent(ModelAddNowCase present) {
		this.present = present;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
